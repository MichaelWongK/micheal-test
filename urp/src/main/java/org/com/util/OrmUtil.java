package org.com.util;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.Arrays;
import java.util.Date;

public class OrmUtil {
	/***
	 * 通过bean的class做ORM映射
	 * 现有问题：
	 * 1、只能select * 查询所有的属性，如果只查询一个，将会导致异常：
	 * java.sql.SQLException: Column 'department' not found.
	 * @param clazz
	 * @param querySQL
	 * @return
	 */
	public static <T> T getBean(Class<T> clazz, String querySQL) {
		T newInstance = null;
		Field[] declaredFields = clazz.getDeclaredFields();
		Statement statement=null;
		Connection connection=null;
		ResultSet rs=null;
		try {
			// 查询之后注入
			connection = JbdcUtil.getConnection();
			statement = connection.createStatement();
			rs = statement.executeQuery(querySQL);
			if(rs.next()){
				newInstance = clazz.newInstance();
				int[] columnToProperty = columnToProperty(rs, declaredFields);
				for (int i = 1; i < columnToProperty.length; i++) {
					if (columnToProperty[i] != -1) {
						// 数组的值映射到fields数组
						Field field = declaredFields[columnToProperty[i]];
						field.setAccessible(true);
						// 首先拿到第一列的值
						Class<?> type = field.getType();
						if (type == Integer.class || type== Integer.TYPE) {
							field.set(newInstance, rs.getInt(i));
						} else if (type == Byte.class || type== Byte.TYPE) {
							field.set(newInstance, rs.getByte(i));
						} else if (type == Date.class) {
							field.set(newInstance, rs.getDate(i));
						} else if (type == String.class) {
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
	}

	private static int[] columnToProperty(ResultSet rs, Field[] declaredFields) throws Exception {
		ResultSetMetaData metaData = rs.getMetaData();
		int columnCount = metaData.getColumnCount();

		int[] columnToProperty = new int[columnCount + 1];
		Arrays.fill(columnToProperty, -1);

		// 建立映射关系
		for (int i = 1; i < columnCount + 1; i++) {
			for (int j = 0; j < declaredFields.length; j++) {
				if (metaData.getColumnName(i).equalsIgnoreCase(declaredFields[j].getName())) {
					columnToProperty[i] = j;
					break;
				}
			}
		}

		return columnToProperty;
	}
}
