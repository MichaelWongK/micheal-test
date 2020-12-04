package org.com.util;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

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
				for (Field field : declaredFields) {
					String name = field.getName();
					field.setAccessible(true);
					//获取到当前属性的类型 jdbcType="java.lang.Integer"
					Class<?> type = field.getType();
					switch(type.getName()){
					case "java.lang.String":
						field.set(newInstance, rs.getString(name));
						break;
					case "java.lang.Byte":
						field.set(newInstance, rs.getByte(name));
						break;
					case "java.lang.Integer":
						field.set(newInstance, rs.getInt(name));
						break;
					case "java.util.Date":
						field.set(newInstance, rs.getDate(name));
						break;
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
}
