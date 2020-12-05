package com.micheal.demo.temp.orm;

import com.micheal.demo.po.MsgLog;
import com.micheal.demo.util.JdbcUtil;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Date;

/**
 * @author <a href="mailto:wangmk13@163.com">micheal.wang</a>
 * @date 2020/12/5 13:06
 * @Description
 */
public class ORMResultTest {

    @Test
    public void ormReflectTest() throws Exception {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        connection = JdbcUtil.getConnection();
        statement = connection.createStatement();
        resultSet = statement.executeQuery("select msg from msg_log");

        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();

        Class<MsgLog> msgLogClass = MsgLog.class;
        // 这里为什么能直接newInstance 如果没有写无参构造会发生什么事？ 如何解决？
        MsgLog newInstance = msgLogClass.newInstance();
        Field[] declaredFields = msgLogClass.getDeclaredFields();
        int[] columnToProperty = new int[columnCount + 1];
        // 初始化数组内所有位置为固定值？？
        // 数组工具类：Arrays 集合工具类：Collentions
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

        boolean next = resultSet.next();
        if (next) {
            for (int i = 1; i < columnToProperty.length; i++) {
                if (columnToProperty[i] != -1) {
                    // 数组的值映射到fields数组
                    Field field = declaredFields[columnToProperty[i]];
                    field.setAccessible(true);
                    // 首先拿到第一列的值
                        Class<?> type = field.getType();
                        if (type == Integer.class || type== Integer.TYPE) {
                            field.set(newInstance, resultSet.getInt(i));
                        } else if (type == Byte.class || type== Byte.TYPE) {
                            field.set(newInstance, resultSet.getByte(i));
                        } else if (type == Date.class) {
                            field.set(newInstance, resultSet.getDate(i));
                        } else if (type == String.class) {
                            field.set(newInstance, resultSet.getString(i));
                        }

                } else {
                }
            }
//                return;
        }
        System.out.println(newInstance);

        System.out.println(Arrays.toString(columnToProperty));

    }

    @Test
    public void clazzTest() throws Exception {
        /**
         * 获取类的class的方法有哪些
         * 1.类.class
         * 2.对象.class、
         * 3.forName【特别注意】
         * 4.包装类.TYPE
         */
        Integer integer = new Integer(1);
        Class<?> forName = Class.forName("java.lang.Integer");
        System.out.println(integer.getClass() == Integer.class);  // true
        System.out.println(integer.getClass() == forName);  // true
        System.out.println(integer.getClass() == Integer.TYPE);  // false
        System.out.println(int.class == Integer.TYPE);  // true

    }
}
