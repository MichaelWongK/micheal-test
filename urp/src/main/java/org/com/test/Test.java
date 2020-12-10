package org.com.test;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.com.json.JSON;
import org.com.orm.strategy.ListBeanHandler;
import org.com.orm.strategy.SingtonBeanHandler;
import org.com.po.UrpUser;
import org.com.util.JbdcUtil;
import org.com.util.OrmUtil;

import com.sun.org.apache.bcel.internal.generic.NEW;

public class Test {
	@org.junit.Test
	public void simpleTest() throws Exception {
		Object object= "hello";
		//class java.lang.String为什么不是object类型的？？
		//这里的object是一个引用，而指向的真实对象是string。而对象头部持有指向当前对象类型的class对象，所以通过getClass（）拿到的是String
		System.out.println(object.getClass());
	}
	@org.junit.Test
	public void json_HandlerTest() throws Exception {
		// UrpUser urpUser = new UrpUser();
		// urpUser.setUserId(1);
		// urpUser.setUserName("jetty");
		// urpUser.setPassword("root");
		// Class<? extends UrpUser> class1 = urpUser.getClass();
		// Field[] fields = class1.getDeclaredFields();
		// for (Field field : fields) {
		// field.setAccessible(true);
		// System.out.println(field.get(urpUser));
		// }
		ArrayList<UrpUser> arrayList = new ArrayList<UrpUser>() {
			{
				UrpUser urpUser1 = new UrpUser();
				urpUser1.setUserId(1);
				urpUser1.setUserName("jetty1");
				urpUser1.setPassword("root");
				
				UrpUser urpUser2 = new UrpUser();
				urpUser2.setUserId(2);
				urpUser2.setUserName("jetty2");
				urpUser2.setPassword("root");
				
				UrpUser urpUser3 = new UrpUser();
				urpUser3.setUserId(3);
				urpUser3.setUserName("jetty3");
				urpUser3.setPassword("root");
				add(urpUser1);
				add(urpUser2);
				add(urpUser3);
			}
		};
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("data", arrayList);
		hashMap.put("count", arrayList.size());
		String parseObject = JSON.parseObject(hashMap);
		System.out.println(parseObject);
	}

//	@org.junit.Test
//	public void ORM_HandlerTest() throws Exception {
//		// 可变参数可以没有
//		UrpUser query = OrmUtil.query("select * from urp_user limit 1", new ListBeanHandler<UrpUser>(UrpUser.class));
//		System.out.println(query);
//	}

	class TestClass<T> {
		public T printType() {
			Integer integer = new Integer(1);
			return (T) integer;
		}
	}

	@org.junit.Test
	public void geneTest() throws Exception {
		/**
		 * 泛型泛指一切类型，而list也是一个类型，所以可以当做泛型的参数传递
		 */
		TestClass<List<Integer>> testClass = new TestClass<>();
	}

	static class Person {
		private String name;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		@Override
		public int hashCode() {
			// TODO Auto-generated method stub
			return 1;
		}

		@Override
		public boolean equals(Object obj) {
			// TODO Auto-generated method stub
			return true;
		}

	}

	@org.junit.Test
	public void hashMapTest() throws Exception {
		Person person1 = new Person();
		person1.setName("稀里哗啦");
		Person person2 = new Person();
		person2.setName("狗头");
		// 如果这两个对象存放在hashmap里面
		HashMap<Person, Object> hashMap = new HashMap<>();
		hashMap.put(person1, "person1");
		Object put = hashMap.put(person2, "person2");
		System.out.println("踢出去：" + put);
		for (Entry<Person, Object> entry : hashMap.entrySet()) {
			System.out.println(entry.getKey() + "  " + entry.getValue());
		}
		System.out.println(person1.hashCode());
		System.out.println(person2.hashCode());
	}

	@org.junit.Test
	public void jdbcDriverTest() throws Exception {
		Connection connection = JbdcUtil.getConnection();
		System.out.println(connection);
	}

	@org.junit.Test
	public void filePathTest() throws Exception {
		File file = new File("");
		System.out.println(file.getCanonicalPath());
		System.out.println(System.getProperty("user.dir"));
	}

	/*@org.junit.Test
	public void ormBeanTest() throws Exception {
		UrpUser bean = OrmUtil.getBean(UrpUser.class, "select * from Urp_User");
		System.out.println(bean);
	}*/

	@org.junit.Test
	public void resultSetTest() throws Exception {
		Statement statement = null;
		Connection connection = null;
		ResultSet rs = null;
		connection = JbdcUtil.getConnection();
		statement = connection.createStatement();
		rs = statement.executeQuery("select UserName from Urp_User");
		// 我在以前讲JDBC逆向工程的时候讲过，这里不提
		ResultSetMetaData metaData = rs.getMetaData();
		int columnCount = metaData.getColumnCount();
		for (int i = 1; i < columnCount + 1; i++) {
			System.out.println(metaData.getColumnName(i));
		}
	}

	@org.junit.Test
	public void fieldsTest() throws Exception {
		Statement statement = null;
		Connection connection = null;
		ResultSet rs = null;
		connection = JbdcUtil.getConnection();
		statement = connection.createStatement();
		rs = statement.executeQuery("select * from Urp_User");
		// 我在以前讲JDBC逆向工程的时候讲过，这里不提
		ResultSetMetaData metaData = rs.getMetaData();
		int columnCount = metaData.getColumnCount();

		Class<UrpUser> userClazz = UrpUser.class;
		// 这里为什么能直接newIntance如果我没有写无参构造会发生什么事？？如何解决？？
		UrpUser newInstance = userClazz.newInstance();
		Field[] declaredFields = userClazz.getDeclaredFields();
		int[] columnToProperty = new int[columnCount + 1];
		// 初始化数组内所有位置为固定值？？？
		// 数组工具类：Arrays 集合工具类：Collections
		Arrays.fill(columnToProperty, -1);
		// 建立映射关系
		for (int i = 1; i < columnCount + 1; i++) {
			for (int j = 0; j < declaredFields.length; j++) {
				System.out.println(metaData.getColumnLabel(i));
				if (declaredFields[j].getName().equalsIgnoreCase(metaData.getColumnName(i))) {
					columnToProperty[i] = j;
					break;
				}
			}
		}
		System.out.println(Arrays.toString(columnToProperty));
		/**
		 * foreach的原理是什么？为何数组可以foreach？ anwser：因为实现了Interable接口
		 */
		if (rs.next()) {
			for (int i = 1; i < columnToProperty.length; i++) {
				if (columnToProperty[i] != -1) {
					// 数组的值映射到Fields数组
					Field field = declaredFields[columnToProperty[i]];
					field.setAccessible(true);
					// 拿到映射的java bean的属性类型
					Class<?> type = field.getType();
					if (type == Integer.class || type == Integer.TYPE) {
						// field.set(obj, value);
						field.set(newInstance, rs.getInt(i));
					} else if (type == Byte.class || type == Byte.TYPE) {
						// field.set(obj, value);
						field.set(newInstance, rs.getByte(i));
					} else if (type == Date.class) {
						// field.set(obj, value);
						field.set(newInstance, rs.getDate(i));
					} else if (type == String.class) {
						// field.set(obj, value);
						field.set(newInstance, rs.getString(i));
					}
				} else {

				}
			}
		}

		System.out.println(newInstance);

		/*
		 * for (Field field : declaredFields) {
		 *//**
			 * 这里可以获取到所有的fields，如果我们通过java bean内的名字去拿列，如果我sql语句内没有
			 * 查询出相应的列，那么会出现异常，所以不可取！
			 *//*
			 * System.out.println(field.getName()); }
			 */
	}

	@org.junit.Test
	public void clazzTest() throws Exception {
		/**
		 * 获取类的class的方法有哪些？ 1.类.class 2.对象.getClass 3.forName【特别注意】、 4.包装类.TYPE
		 */
		Integer integer = new Integer(1);
		Class<?> forName = Class.forName("java.lang.Integer");
		System.out.println(integer.getClass() == Integer.class);// true
		System.out.println(integer.getClass() == forName);// true
		System.out.println(integer.getClass() == Integer.TYPE);// false
		System.out.println(int.class == Integer.TYPE);// true
	}

	@org.junit.Test
	public void stringTest() throws Exception {
		/**
		 * true false
		 */
		String string1 = new StringBuffer("aa").append("va").toString();
		String string2 = new StringBuffer("ja").append("va").toString();
		System.out.println(string1.intern() == string1);
		System.out.println(string2.intern() == string2);
	}
}
