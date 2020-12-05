package com.micheal.demo.temp.orm;

import com.micheal.demo.po.MsgLog;
import com.micheal.demo.util.JdbcUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

/**
 * @author <a href="mailto:wangmk13@163.com">micheal.wang</a>
 * @date 2020/12/5 19:03
 * @Description
 */
public class Test {

    @org.junit.Test
    public void ResultSetTest() throws Exception {
        Statement statement=null;
        Connection connection=null;
        ResultSet rs=null;
        connection = JdbcUtil.getConnection();
        statement = connection.createStatement();
        rs = statement.executeQuery("select msg from msg_log");
        //我在以前讲JDBC逆向工程的时候讲过，这里不提
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();
        for (int i = 1; i < columnCount+1; i++) {
            System.out.println(metaData.getColumnName(i));
            System.out.println(metaData.getColumnType(i));
            System.out.println(metaData.getColumnTypeName(i));
        }
    }

    @org.junit.Test
    public void ormBeanTest() throws Exception{
        MsgLog bean = OrmUtil.getBean(MsgLog.class, "select * from msg_log");
        System.out.println(bean);
    }
}
