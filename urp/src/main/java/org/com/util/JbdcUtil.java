package org.com.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

//工具类里边的所有方法都必须是静态的
/**
 * 这里需要配置数据源的信息，
 * 【需要一个数据源的信息】
 * @author Smith
 *
 */
public class JbdcUtil {
	static{
		try {
			/**
			 * 面试题：class.forName和普通自定义类加载器的区别？？？
			 */
			Class.forName("com.mysql.jdbc.Driver");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static Connection getConnection() throws Exception{
		Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/URP","root","root");
		return connection;
	}
	public static void gracefulClose(Statement statement,ResultSet rs,Connection connection){
		try{
			if(rs!=null){
				rs.close();
			}
			if(statement!=null){
				statement.close();
			}
			if(connection!=null){
				connection.close();
			}
		}catch (Exception e) {
			// TODO: handle exception
		}
	}
}
