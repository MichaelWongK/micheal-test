/**
 * 
 */
package org.com.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;

import com.mysql.jdbc.Statement;

import java.util.Set;

/**
 * @author 黄俊
 * @Date 2017年10月7日 JDBC:这是用来干嘛的？answer：使用jdbc提供一个一对多的关系。 数据库：SQL
 *       SERVER,ORCALE,MYSQL,DB2，MARIDB,DERBY.... 设计思路： Connection接口：提供连接数据库的操作
 *       根据OOP的原则：接口隔离 Statement,PrepareStatement,ResultSet
 *       http://www.baidu.com/xx/xx/xx
 *       这个工具是用来从数据库表生成 PO 也就是java对象的工具
 */
class BlueGenerator {
	public Connection connection;
	private DatabaseMetaData metaData;

	public void init() {
		try {
			Class.forName(Utils.getDriver());// 这个方法加载一个类返回一个类的实例对象：class
			connection = DriverManager.getConnection(Utils.getURL(), Utils.getUserName(), Utils.getPassword());
			metaData = connection.getMetaData();// 获取数据库的原信息
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public void execGenerate() {
		ResultSet tables = null;
		try {
			tables = metaData.getTables(Utils.getDatabase(), null, null, null);
			String createDir = Utils.createDir(Utils.getPackageName());
			while (tables.next()) {
				String tableName = tables.getString(3);// 表名
				StringBuilder contentBuilder = new StringBuilder();// 包装一个方法，来格式化类名
				ResultSet columns = metaData.getColumns(Utils.getDatabase(), Utils.getDatabase(), tableName, null);
				Map<String, String> dataMap = new HashMap<>();
				while (columns.next()) {
					String columnName = columns.getString("COLUMN_NAME");
					String dataType = columns.getString("TYPE_NAME");
					dataMap.put(columnName, dataType);
				}
				contentBuilder.append("package " + Utils.getPackageName() + ";" + "\n");
				Set<Entry<String, String>> entrySet = dataMap.entrySet();
				for (Entry<String, String> entry : entrySet) {
					String nessaryImport = Utils.IMPORT_PACK_MAP.get(entry.getValue());
					if (nessaryImport != null) {
						contentBuilder.append(nessaryImport + "\n");
					}
				}
				String generateClassName = Utils.generateClassName(tableName);
				contentBuilder.append("public class " + generateClassName + "{" + "\n");
				for (Entry<String, String> entry : entrySet) {
					contentBuilder.append("\t" + "private " + Utils.SQL_TYPE2JAVA_TYPE.get(entry.getValue()) + " "
							+ Utils.generateFieldName(entry.getKey()) + ";" + "\n");
				}
				for (Entry<String, String> entry : entrySet) {
					// 生成set方法
					contentBuilder.append(
							"\t" + "public void set" + Utils.firstWordUp(Utils.generateFieldName(entry.getKey())) + "("
									+ Utils.SQL_TYPE2JAVA_TYPE.get(entry.getValue()) + " "
									+ Utils.generateFieldName(entry.getKey()) + ")" + " " + "{" + "\n" + "\t\t"
									+ "this." + Utils.generateFieldName(entry.getKey()) + " " + "=" + " "
									+ Utils.generateFieldName(entry.getKey()) + ";" + "\n" + "\t" + "}" + "\n");
					// 生成get方法
					contentBuilder.append("\t" + "public " + Utils.SQL_TYPE2JAVA_TYPE.get(entry.getValue()) + " get"
							+ Utils.firstWordUp(Utils.generateFieldName(entry.getKey())) + "()" + " " + "{" + "\n"
							+ "\t\t" + "return " + Utils.generateFieldName(entry.getKey()) + ";" + "\n" + "\t" + "}"
							+ "\n");
				}
				contentBuilder.append("}");
				Utils.writeClazz(createDir + File.separator + generateClassName + ".java", contentBuilder.toString());
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			if (tables != null) {
				Utils.gracefulClose(connection, null, tables);
			}
		}
		System.out.println("generate successful");
	}

	/**
	 * 
	 * @author 黄俊
	 * @Date 2017年10月9日 工具类提供在构建JAVA BEAN时使用的方法
	 */
	private static class Utils {
		public final static Map<String, String> SQL_TYPE2JAVA_TYPE = new HashMap<>();
		public final static Map<String, String> IMPORT_PACK_MAP = new HashMap<>();
		private final static Properties JDBC_PROPERTIES = new Properties();
		static {
			SQL_TYPE2JAVA_TYPE.put("INT UNSIGNED", "Integer");
			SQL_TYPE2JAVA_TYPE.put("VARCHAR", "String");
			SQL_TYPE2JAVA_TYPE.put("TIMESTAMP", "Date");
			SQL_TYPE2JAVA_TYPE.put("INT", "Integer");
			SQL_TYPE2JAVA_TYPE.put("TINYINT", "Byte");
			SQL_TYPE2JAVA_TYPE.put("DATETIME", "Date");
			SQL_TYPE2JAVA_TYPE.put("CHAR", "String");

			IMPORT_PACK_MAP.put("DATETIME", "import java.util.Date;");
			IMPORT_PACK_MAP.put("TIMESTAMP", "import java.util.Date;");
			/**
			 * 初始化jdbc信息 1、使用类加载器获得jdbc信息 2、放入JDBC_PROPERTIES
			 */
			InputStream resourceAsStream = Utils.class.getClassLoader().getResourceAsStream("data.properties");
			try {
				JDBC_PROPERTIES.load(resourceAsStream);
			} catch (IOException e) {
				// TODO Auto-generated catch block
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

		public static String getURL() {
			String property = JDBC_PROPERTIES.getProperty("jdbc.url");
			if (property == null) {
				throw new RuntimeException("URL加载失败");
			}
			return property;
		}

		public static String getUserName() {
			String property = JDBC_PROPERTIES.getProperty("jdbc.username");
			if (property == null) {
				throw new RuntimeException("用户名加载失败");
			}
			return property;
		}

		public static String getPassword() {
			String property = JDBC_PROPERTIES.getProperty("jdbc.password");
			if (property == null) {
				throw new RuntimeException("密码加载失败");
			}
			return property;
		}

		public static String getPackageName() {
			String property = JDBC_PROPERTIES.getProperty("jdbc.package.path");
			if (property == null) {
				throw new RuntimeException("包名加载失败");
			}
			return property;
		}

		public static String getDatabase() {
			String property = JDBC_PROPERTIES.getProperty("jdbc.url");
			if (property == null) {
				throw new RuntimeException("URL加载失败");
			}
			String substring = property.substring(property.lastIndexOf("/")+1, property.length());
			if (substring == null) {
				throw new RuntimeException("请在URL地址后加上数据库名");
			}
			return substring;
		}

		/**
		 * 
		 * @param tableName
		 *            表名
		 * @return 根据表名生成的类名
		 */
		public static String generateClassName(String tableName) {
			String clazzName = "";
			String[] split = tableName.split("_");
			// 在JDK1.8中 字符串用+连接会在底层生成一个StringBuilder来优化
			for (String string : split) {
				clazzName += (string.substring(0, 1).toUpperCase() + string.substring(1, string.length()));
			}
			return clazzName;
		}

		/**
		 * 
		 * @param columnName
		 *            列名
		 * @return 根据列名生成属性名
		 */
		public static String generateFieldName(String columnName) {
			String fieldName = "";
			String[] split = columnName.split("_");
			// 在JDK1.8中 字符串用+连接会在底层生成一个StringBuilder来优化
			for (int i = 0; i < split.length; i++) {
				if (i == 0) {
					fieldName += (split[i].substring(0, 1).toLowerCase() + split[i].substring(1, split[i].length()));
				} else {
					fieldName += (split[i].substring(0, 1).toUpperCase() + split[i].substring(1, split[i].length()));
				}
			}
			return fieldName;
		}

		/**
		 * 创建包
		 * 
		 * @param packageName
		 *            放置PO的包名
		 */
		public static String createDir(String packageName) {
			String path = null;
			try {
				// xxx.xxx.xxxx
				String rootPath = new File("").getCanonicalPath() + File.separator + "src";
				String[] fileNames = packageName.split("\\.");
				for (String string : fileNames) {
					rootPath += File.separator + string;
				}
				File file = new File(rootPath);
				file.mkdirs();
				path = file.getCanonicalPath();
				System.out.println("生成路径成功");
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				System.err.println("生成失败");
			}
			return path;
		}

		/**
		 * 将生成的PO写到类路径下
		 * 
		 * @param path
		 * @param clazzContent
		 */
		public static void writeClazz(String path, String clazzContent) {
			FileOutputStream fileOutputStream = null;
			try {
				fileOutputStream = new FileOutputStream(new File(path));
				fileOutputStream.write(clazzContent.getBytes());
				fileOutputStream.close();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			} finally {
				if (fileOutputStream != null) {
					try {
						fileOutputStream.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}

		public static String firstWordUp(String string) {
			return string.substring(0, 1).toUpperCase() + string.substring(1, string.length());
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
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
	}
}

public class JDBC_Demo {
	public static void main(String[] args) {
		BlueGenerator blueGenerator = new BlueGenerator();
		blueGenerator.init();
		blueGenerator.execGenerate();
	}
}