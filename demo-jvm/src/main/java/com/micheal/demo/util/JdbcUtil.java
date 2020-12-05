package com.micheal.demo.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * @author <a href="mailto:wangmk13@163.com">micheal.wang</a>
 * @date 2020/12/5 13:24
 * @Description
 */
public class JdbcUtil {

    static{
        try {
            /**
             * 面试题：class.forName和普通自定义类加载器的区别？？？
             */
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public static Connection getConnection() throws Exception{
        Connection connection = DriverManager.getConnection("jdbc:mysql://micheal.wang:3306/mq-mail","root","mingkai13");
        return connection;
    }
    public static void gracefulClose(Statement statement, ResultSet rs, Connection connection){
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
