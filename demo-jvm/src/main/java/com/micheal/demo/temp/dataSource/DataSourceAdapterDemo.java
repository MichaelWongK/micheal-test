package com.micheal.demo.temp.dataSource;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author <a href="mailto:wangmk13@163.com">micheal.wang</a>
 * @date 2020/12/2 10:15
 * @Description 适配器模式自定义数据库连接池
 */

class DataSourceAdapter implements DataSource {

    @Override
    public Connection getConnection() throws SQLException {
        return null;
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

class MKDataSource extends DataSourceAdapter {

    private final List<Connection> POOL = new LinkedList<>();

    {
        // 初始化池子
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            for (int i = 0; i< 10; i++) {
                Connection connection = DriverManager.getConnection("jdbc:mysql://micheal.wang:3306/mq-mail", "root", "mingkai13");
                DataSourceProxy dataSourceProxy = new DataSourceProxy(connection, POOL);
                POOL.add((Connection) dataSourceProxy.getProxy());
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
}

class DataSourceProxy implements InvocationHandler {

    private Object target;
    private List<Connection> POOL;

    public DataSourceProxy(Object target, List<Connection> POOL) {
        this.target = target;
        this.POOL = POOL;
    }

    public Object getProxy() {
        Class<?> clazz = target.getClass();
        return Proxy.newProxyInstance(clazz.getClassLoader(), clazz.getInterfaces(), this::invoke);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.getName().equals("close")) {
            POOL.add((Connection) target);
            System.out.println("connection was collected,POOL size:"+POOL.size());
            return null;
        }
        return method.invoke(target, args);
    }
}

public class DataSourceAdapterDemo {

    public static void main(String[] args) throws SQLException {
        MKDataSource mkDataSource = new MKDataSource();
        Connection connection = mkDataSource.getConnection();
        connection.close();
        System.out.println(connection);
    }
}
