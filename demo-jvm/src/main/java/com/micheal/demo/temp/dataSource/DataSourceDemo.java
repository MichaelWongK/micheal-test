package com.micheal.demo.temp.dataSource;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author <a href="mailto:wangmk13@163.com">micheal.wang</a>
 * @date 2020/12/1 11:27
 * @Description 数据库连接池和线程池区别
 * 保活，跟线程池的作用一模一样
 *  1.得有容器
 *  2.连接拿去用了得回来
 *  3.保证连接不会重复拿
 *  conclusion: 插删修改比较多用什么存储？
 *  容器：链表
 */
class MyDataSource implements DataSource {

    private final List<Connection> POOL = new LinkedList<>();

    {
        // 初始化池子
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            for (int i = 0; i< 10; i++) {
                Connection connection = DriverManager.getConnection("jdbc:mysql://micheal.wang:3306/mq-mail", "root", "mingkai13");
                POOL.add(connection);
            }
            System.out.println("init database pool success");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Connection getConnection() throws SQLException {
        if (POOL.isEmpty()) {
            System.out.println("当前没有连接可用");
        }
        Connection connection = POOL.remove(0);
        System.out.println("get connection success, current pool size: " + POOL.size());
        return connection;
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return null;
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return null;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return null;
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {

    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {

    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return 0;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;
    }
}


public class DataSourceDemo {

    public static void main(String[] args) throws SQLException {
        MyDataSource myDataSource = new MyDataSource();
        Connection connection = myDataSource.getConnection();

        System.out.println(connection);
    }
}
