package org.com.util;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Date;

import org.com.orm.strategy.OrmHandler;

public class OrmUtil {
	/**
	 * 通过参数形成最终查询字符串SQL
	 * @param querySQL
	 * @param args
	 * @return
	 * @throws Exception
	 */
	private static String parseSQL(String querySQL, Object... args) throws Exception {
		if(args==null){
			return querySQL;
		}
		// 判斷是否有?
		if (querySQL.contains("?")) {
			if (args.length == 0) {
				throw new RuntimeException("参数为空");
			}
			int argsIndex = 0;
			StringBuilder stringBuilder = new StringBuilder();
			String[] split = querySQL.split(" ");
			for (String string : split) {
				if (string.contains("?")) {
					Object object = args[argsIndex++];
					if (object instanceof String) {
						string = string.replace("?", "\"" + (String) object + "\"");
					}
					if (object instanceof Integer) {
						string = string.replace("?", String.valueOf(object));
					}
					stringBuilder.append(string).append(" ");
				} else {
					stringBuilder.append(string).append(" ");
				}
			}
			querySQL = stringBuilder.toString();
			System.out.println(querySQL);
		}
		return querySQL;
	}

	/**
	 * 通过刚才的getBean我们是通过 传入的class对象和sql还有参数
	 * 然后查询出来之后注入到bean的实例属性里边，在这种情况下变化的是不是返回的 类型？？？返回一个bean 然后现在又要返回一个bean的list
	 */
	public static <T>T query(String querySQL, OrmHandler<T> ormHandler, Object... args) throws Exception {
		querySQL = parseSQL(querySQL,args);
		Statement statement = null;
		Connection connection = null;
		ResultSet rs = null;
		// 查询之后注入
		connection = JbdcUtil.getConnection();
		statement = connection.createStatement();
		System.out.println(querySQL);
		rs = statement.executeQuery(querySQL);
		/**
		 * 策略模式封装了算法，我这里只拿过来直接用就可以，只需要知道返回值和输入值是什么即可，无需关注具体实现
		 * 【暗箱操作】
		 */
		T handler = ormHandler.handler(rs);
		return handler;
	}
	public static <T> T columnToProperty(ResultSet rs, Class<T> clazz) throws Exception {
		Field[] declaredFields = clazz.getDeclaredFields();
		T newInstance = clazz.newInstance();
		ResultSetMetaData metaData = rs.getMetaData();
		int columnCount = metaData.getColumnCount();
		int[] columnToProperty = new int[columnCount + 1];
		Arrays.fill(columnToProperty, -1);
		for (int i = 1; i < columnCount + 1; i++) {
			for (int j = 0; j < declaredFields.length; j++) {
				if (declaredFields[j].getName().equalsIgnoreCase(metaData.getColumnName(i))) {
					columnToProperty[i] = j;
					break;
				}
			}
		}
		for (int i = 1; i < columnToProperty.length; i++) {
			if (columnToProperty[i] != -1) {
				// 数组的值映射到Fields数组
				Field field = declaredFields[columnToProperty[i]];
				field.setAccessible(true);
				// 拿到映射的java bean的属性类型
				Class<?> type = field.getType();
				if (type == Integer.class || type == Integer.TYPE) {
					// field.set(obj, value);
					field.set(newInstance, rs.getInt(i));
				} else if (type == Byte.class || type == Byte.TYPE) {
					// field.set(obj, value);
					field.set(newInstance, rs.getByte(i));
				} else if (type == Date.class) {
					// field.set(obj, value);
					field.set(newInstance, rs.getDate(i));
				} else if (type == String.class) {
					// field.set(obj, value);
					field.set(newInstance, rs.getString(i));
				}
			}
		}
		return newInstance;
	}
	/*public static <T> T getBean(Class<T> clazz, String querySQL) {
		return getBean(clazz, querySQL, new Object[] {});
	}*/

	/***
	 * 通过bean的class做ORM映射 现有问题： 1、只能select * 查询所有的属性，如果只查询一个，将会导致异常：
	 * java.sql.SQLException: Column 'department' not found. 在Mybatis中 #{},${}
	 * 
	 * @param clazz
	 * @param querySQL
	 *            select * from urp_user where userName=? and password=?
	 * @param args
	 * @return
	 */
	/*public static <T> T getBean(Class<T> clazz, String querySQL, Object... args) {
		// 判斷是否有?
		if (querySQL.contains("?")) {
			if (args.length == 0) {
				throw new RuntimeException("参数为空");
			}
			int argsIndex = 0;
			StringBuilder stringBuilder = new StringBuilder();
			String[] split = querySQL.split(" ");
			for (String string : split) {
				if (string.contains("?")) {
					Object object = args[argsIndex++];
					if (object instanceof String) {
						string = string.replace("?", "\"" + (String) object + "\"");
					}
					if (object instanceof Integer) {
						string = string.replace("?", String.valueOf(object));
					}
					stringBuilder.append(string).append(" ");
				} else {
					stringBuilder.append(string).append(" ");
				}
			}
			querySQL = stringBuilder.toString();
			System.out.println(querySQL);
		}
		T newInstance = null;
		Field[] declaredFields = clazz.getDeclaredFields();
		Statement statement = null;
		Connection connection = null;
		ResultSet rs = null;
		try {
			// 查询之后注入
			connection = JbdcUtil.getConnection();
			statement = connection.createStatement();
			rs = statement.executeQuery(querySQL);
			if (rs.next()) {
				newInstance = clazz.newInstance();
				int[] columnToProperty = columnToProperty(rs, declaredFields);
				for (int i = 1; i < columnToProperty.length; i++) {
					if (columnToProperty[i] != -1) {
						// 数组的值映射到Fields数组
						Field field = declaredFields[columnToProperty[i]];
						field.setAccessible(true);
						// 拿到映射的java bean的属性类型
						Class<?> type = field.getType();
						if (type == Integer.class || type == Integer.TYPE) {
							// field.set(obj, value);
							field.set(newInstance, rs.getInt(i));
						} else if (type == Byte.class || type == Byte.TYPE) {
							// field.set(obj, value);
							field.set(newInstance, rs.getByte(i));
						} else if (type == Date.class) {
							// field.set(obj, value);
							field.set(newInstance, rs.getDate(i));
						} else if (type == String.class) {
							// field.set(obj, value);
							field.set(newInstance, rs.getString(i));
						}
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		} finally {
			JbdcUtil.gracefulClose(statement, rs, connection);
		}
		return newInstance;
	}*/

}
