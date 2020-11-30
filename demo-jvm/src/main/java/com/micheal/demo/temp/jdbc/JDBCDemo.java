package com.micheal.demo.temp.jdbc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author <a href="mailto:wangmk13@163.com">micheal.wang</a>
 * @date 2020/9/30 11:17
 * @Description
 * JDBC:这是用来干嘛的？answer：使用jdbc提供一个一对多的关系。
 *  * 数据库：SQL SERVER,ORCALE,MYSQL,DB2，MARIDB,DERBY....
 *  * 设计思路：
 *  * Connection接口：提供连接数据库的操作
 *  * 根据OOP的原则：接口隔离
 *  * Statement,PrepareStatement,ResultSet
 *  * http://www.baidu.com/xx/xx/xx
 */
public class JDBCDemo {

    public static void main(String[] args)throws Exception {
        /**
         * 一、反射是什么？就是为了在运行时能够获取到本身类信息的一种技术
         * 还可以动态加载类（在运行一个程序的时候，允许动态的加载类进入JVM）
         * 反射机制它的核心点在哪？？？？-》》》》自然而然想到一个对象：在类加载进入JVM并且已经经过加载，验证，准备，解析
         * 初始化<clinit>后的Class对象（这个对象我讲过代表了一个类本身）。那么拿到这个对象 我就能获取到类的所有信息。
         * 那么怎么拿？1.对象.getClass()2.类名.class
         * 3.4. TYPE = (Class<Integer>) Class.getPrimitiveClass("int");
         *
         *
         * 二、JAVA的类加载机制
         * 怎样保证加载的类的安全性？
         * 你不可能 让危害计算机的代码 也就是类 进入JVM导致用户信息泄露或者信息被删除。。。。恶意操作行为的发生。
         * 所以 java 引入了 JAVA的加载机制：采用双亲委派机制和JAVA安全沙箱来保证JVM的安全
         * 双亲委派：Bootstrap Classloader,Ext Classloader,System Classloader,用户自定义
         */
        Class.forName("com.mysql.cj.jdbc.Driver");//这个方法加载一个类返回一个类的实例对象：class
        Connection connection = DriverManager
                .getConnection("jdbc:mysql://micheal.wang:3306/mq-mail","root","mingkai13");
        System.out.println(connection);

        /**
         * 我拿到一个连接之后 我是不是可以干事情了？ ORM框架是什么、？ OBJECT RELATION MAPPING对象关系映射
         * 也就是原来我们的数据 在关系型数据库中，是以表的形式存储。 那么我们的对象可不可以把它和表对应起来？？ 表：行和列 对象：属性和方法。
         * 行<-->对象对应 列<-->对象属性对应 1.找到数据库 2.找到表 3.读取表的列信息 4.根据列信息构建类属性
         */
        DatabaseMetaData metaData = connection.getMetaData();// 获取数据库的原信息
        ResultSet tables = metaData.getTables("mq-mail", null, null, null);
        /**
         * Exception in thread "main" java.sql.SQLException: Column Index out of
         * range, 0 < 1. 通过这里我们可以看到对于结果集来说必须index的值从1开始
         */
        String packageName="com.micheal.demo.po";
        String path = Utils.createDir(packageName);
        while (tables.next()) {
            // String string1 = tables.getString(1);//数据库名
            // String string2 = tables.getString(2);//未知
            String tableName = tables.getString(3);// 表名
            // 这里就可以进行创建类了
            /**
             * 创建类: 1、指明输出地点 2、构建类名 3、写入属性 4、封装属性
             */
            StringBuilder contentBuilder = new StringBuilder();// 包装一个方法，来格式化类名
            ResultSet columns = metaData.getColumns("mq-mail", "mq-mail", tableName, null);
            Map<String, String> dataMap = new HashMap<>();
            while (columns.next()) {
                String columnName = columns.getString("COLUMN_NAME");
                String dataType = columns.getString("TYPE_NAME");
                dataMap.put(columnName, dataType);
            }


//                "public class " + Utils.generateClassName(tableName) + "{" + "\n"

            // private 类型 变量名；
//                contentBuilder.append("private ")

            /**
             * 1、导包
             * 2、构建类信息
             * 3、构建属性
             * 4、封装属性
             * 5、创建
             * 6、创建 Java Bean
             */
            contentBuilder.append("package " + packageName + "; \n\n");
            Set<Map.Entry<String, String>> entrySet = dataMap.entrySet();
            for (Map.Entry<String, String> entry : entrySet) {
                String nessaryImport = Utils.IMPORT_PACK_MAP.get(entry.getValue());
                if (nessaryImport != null && !contentBuilder.toString().contains(nessaryImport)) {
                    contentBuilder.append(nessaryImport + "\n");
                }
            }
//            contentBuilder.append("import lombok.Data;\n\n").append("@Data\n");
            String generateClassName = Utils.generateClassName(tableName);
            contentBuilder.append("public class " + generateClassName + "{" + "\n");


            for (Map.Entry<String, String> entry : entrySet) {
                contentBuilder.append("\t" + "private " + Utils.SQL_TYPE2JAVA_TYPE.get(entry.getValue()) + " "
                        + Utils.generateFieldName(entry.getKey()) + ";" + "\n");
            }

            //  GET/SET方法
            for (Map.Entry<String, String> entry : entrySet) {
                // 生成set方法
                contentBuilder.append(
                        "\t" + "private void set" + Utils.firstWordUp(Utils.generateFieldName(entry.getKey()))
                        + "(" +  Utils.SQL_TYPE2JAVA_TYPE.get(entry.getValue()) + " " + Utils.generateFieldName(entry.getKey()) + ")"
                        + " " + "{" + "\n"
                        + "\t\t" + "this." + Utils.generateFieldName(entry.getKey()) + " = " + Utils.generateFieldName(entry.getKey()) + ";"
                        + "\n" + "\t" + "}" + "\n");
                // 生成get方法
                contentBuilder.append(
                        "\t" + "private " + Utils.SQL_TYPE2JAVA_TYPE.get(entry.getValue()) + " get" + Utils.firstWordUp(Utils.generateFieldName(entry.getKey()))
                        + "()" + " " + "{" + "\n"
                        + "\t\t" + "return " + Utils.generateFieldName(entry.getKey()) + ";"
                        + "\n" + "\t" + "}" + "\n") ;
            }

            contentBuilder.append("}");
            Utils.writeClazz(path + File.separator + generateClassName + ".java", contentBuilder.toString());

        }

    }
}

/**
 * 工具类提供在构建JAVA BEAN时使用方法
 */
class Utils {

    public final static Map<String, String> SQL_TYPE2JAVA_TYPE = new HashMap<>();
    public final static Map<String, String> IMPORT_PACK_MAP = new HashMap<>();

    static {
        SQL_TYPE2JAVA_TYPE.put("INT UNSIGNED", "Integer");
        SQL_TYPE2JAVA_TYPE.put("INT", "Integer");
        SQL_TYPE2JAVA_TYPE.put("VARCHAR", "String");
        SQL_TYPE2JAVA_TYPE.put("DATETIME", "Date");
        SQL_TYPE2JAVA_TYPE.put("TIMESTAMP", "TimeStamp");
        SQL_TYPE2JAVA_TYPE.put("CHAR", "String");
        SQL_TYPE2JAVA_TYPE.put("TEXT", "String");

        IMPORT_PACK_MAP.put("INT UNSIGNED", "");
        IMPORT_PACK_MAP.put("DATETIME", "import java.util.Date;");
        IMPORT_PACK_MAP.put("TIMESTAMP", "import java.util.Date;");
    }

    /**
     *
     * @param tableName 表名
     * @return 根据表名生成类名
     */
    public static String generateClassName(String tableName) {
        String clazzName = "";
        String[] split = tableName.split("_");
        // 在jdk1.8中 字符串用+连接会在底层生成一个StringBuilder来优化
        for (String string : split) {
            clazzName += (string.substring(0, 1).toUpperCase() + string.substring(1));
        }

        return clazzName;
    }

    /**
     *
     * @param columnName 列名
     * @return 根据列名生成属性名
     */
    public static String generateFieldName(String columnName) {
        String fieldName = "";
        String[] split = columnName.split("_");
        // 在jdk1.8中 字符串用+连接会在底层生成一个StringBuilder来优化
        for (int i = 0; i < split.length; i++) {
            if (i == 0) {
                fieldName += (split[i].substring(0, 1).toLowerCase() + split[i].substring(1));
            } else {
                fieldName += (split[i].substring(0, 1).toUpperCase() + split[i].substring(1));
            }
        }

        return fieldName;
    }

    /**
     * 创建包
     * @param packageName 放置po的包名
     */
    public static String createDir(String packageName) {
        String path = "";
        try {
            String rootPath = new File("").getCanonicalPath()
                    + File.separator + "demo-jvm"
                    + File.separator + "src"
                    + File.separator + "main"
                    + File.separator + "java";
            String[] fileNames = packageName.split("\\.");
            for (String fileName : fileNames) {
                rootPath += File.separator + fileName;
            }
            File file = new File(rootPath);
            file.mkdirs();
            path = file.getCanonicalPath();
            System.out.println("生成路径成功");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("生成路径失败");
        }
        return path;
    }

    /**
     * 将生成的po写到类路径下
     * @param path
     * @param clazzContent
     */
    public static void writeClazz(String path, String clazzContent) {
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(new File(path));
            fileOutputStream.write(clazzContent.getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    public static String firstWordUp(String word) {
        return word.substring(0, 1).toUpperCase() + word.substring(1);
    }

    public static void main(String[] args) {
        /**
         * 创建PO类
         * 类名.java
         */
        try {
            File packageFile = new File(new File("").getCanonicalPath()
                    + File.separator + "demo-jvm"
                    + File.separator + "src"
                    + File.separator + "main"
                    + File.separator + "java"
                    + File.separator + "po");
            packageFile.mkdirs();
            File poFile = new File(packageFile.getCanonicalPath() + File.separator + "Demo.java");
            FileOutputStream fileOutputStream = new FileOutputStream(poFile);
            fileOutputStream.write("public class Demo {\tprivate int ab;}".getBytes());
            fileOutputStream.close();


//            poFile.createNewFile();
//            System.out.println("生成路径成功");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}