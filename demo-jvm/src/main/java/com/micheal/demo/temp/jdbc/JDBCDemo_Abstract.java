package com.micheal.demo.temp.jdbc;

import test.Test;

import java.io.*;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
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
class MyGenerator {
    private Connection connection;
    private DatabaseMetaData metaData;

    public void init() {
        try {
            Class.forName(Utils.getDriver()); //这个方法加载一个类返回一个类的实例对象：class
            connection = DriverManager.getConnection(Utils.getUrl(),Utils.getUsername(),Utils.getPassword());
            metaData = connection.getMetaData();// 获取数据库的原信息
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void execGenerator() {
        ResultSet tables = null;
        try {
            tables = metaData.getTables(Utils.getDataBase(), null, null, null);
            String path = Utils.createDir(Utils.getPackageName());
            while (tables.next()) {
                String tableName = tables.getString(3);// 表名
                StringBuilder contentBuilder = new StringBuilder();// 包装一个方法，来格式化类名
                ResultSet columns = metaData.getColumns(Utils.getDataBase(), Utils.getDataBase(), tableName, null);
                Map<String, String> dataMap = new HashMap<>();
                while (columns.next()) {
                    String columnName = columns.getString("COLUMN_NAME");
                    String dataType = columns.getString("TYPE_NAME");
                    dataMap.put(columnName, dataType);
                }

                contentBuilder.append("package " + Utils.getPackageName() + "; \n\n");
                Set<Map.Entry<String, String>> entrySet = dataMap.entrySet();
                for (Map.Entry<String, String> entry : entrySet) {
                    String nessaryImport = Utils.IMPORT_PACK_MAP.get(entry.getValue());
                    if (nessaryImport != null && !contentBuilder.toString().contains(nessaryImport)) {
                        contentBuilder.append(nessaryImport + "\n");
                    }
                }
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
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (tables != null) {
                Utils.gracefulClose(connection, null, tables);
            }
        }

    }

    /**
     * 工具类提供在构建JAVA BEAN时使用方法
     */
    private static class Utils {

        public final static Map<String, String> SQL_TYPE2JAVA_TYPE = new HashMap<>();
        public final static Map<String, String> IMPORT_PACK_MAP = new HashMap<>();
        private final static Properties JDBC_PROPERTIES = new Properties();

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

            /**
             * 初始化jdbc信息
             * 1、使用类加载器或得jdbc信息
             * 2、放入JDBC_PROPERTIES
             */
            InputStream resourceAsStream = Test.class.getClassLoader().getResourceAsStream("application.properties");
            try {
                JDBC_PROPERTIES.load(resourceAsStream);
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(-1);
            }
        }

        public static String getDriver() {
            String property = JDBC_PROPERTIES.getProperty("jdbc.driver");
            if (property == null) {
                throw new RuntimeException("驱动加载失败");
            }
            return property;
        }

        public static String getUrl() {
            String property = JDBC_PROPERTIES.getProperty("jdbc.url");
            if (property == null) {
                throw new RuntimeException("url加载失败");
            }
            return property;
        }

        public static String getUsername() {
            String property = JDBC_PROPERTIES.getProperty("jdbc.username");
            if (property == null) {
                throw new RuntimeException("username加载失败");
            }
            return property;
        }

        public static String getPassword() {
            String property = JDBC_PROPERTIES.getProperty("jdbc.password");
            if (property == null) {
                throw new RuntimeException("password加载失败");
            }
            return property;
        }

        public static String getPackageName() {
            String property = JDBC_PROPERTIES.getProperty("packageName.path");
            if (property == null) {
                throw new RuntimeException("packageName载失败");
            }
            return property;
        }

        public static String getDataBase() {
            String property = getUrl();
            String substring = property.substring(property.lastIndexOf("/") + 1);
            if (substring == null) {
                throw new RuntimeException("请在url地址后加上数据库名");
            }
            return substring;
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

        public static void gracefulClose(Connection connection, Statement statement, ResultSet resultSet) {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }

                if (statement != null) {
                    statement.close();
                }

                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
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

    public Connection getConnection() {
        return connection;
    }
}

public class JDBCDemo_Abstract {

    public static void main(String[] args)throws Exception {
        MyGenerator myGenerator = new MyGenerator();
        myGenerator.init();
        myGenerator.execGenerator();
        System.out.println("successful");
    }
}

