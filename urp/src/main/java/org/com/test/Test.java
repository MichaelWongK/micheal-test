package org.com.test;
import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map.Entry;

import org.com.po.UrpUser;
import org.com.util.JbdcUtil;
import org.com.util.OrmUtil;
public class Test {
	static class Person{
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
	public void hashMapTest() throws Exception{
		Person person1 = new Person();
		person1.setName("稀里哗啦");
		Person person2 = new Person();
		person2.setName("狗头");
		//如果这两个对象存放在hashmap里面
		HashMap<Person, Object> hashMap = new HashMap<>();
		hashMap.put(person1, "person1");
		Object put = hashMap.put(person2, "person2");
		System.out.println("踢出去："+put);
		for (Entry<Person, Object> entry : hashMap.entrySet()) {
			System.out.println(entry.getKey()+"  "+entry.getValue());
		}
		System.out.println(person1.hashCode());
		System.out.println(person2.hashCode());
	}
	@org.junit.Test
	public void jdbcDriverTest() throws Exception{
		Connection connection = JbdcUtil.getConnection();
		System.out.println(connection);
	}
	@org.junit.Test
	public void filePathTest() throws Exception{
		File file = new File("");
		System.out.println(file.getCanonicalPath());
		System.out.println(System.getProperty("user.dir"));			
	}
	@org.junit.Test
	public void ormBeanTest() throws Exception{
		UrpUser bean = OrmUtil.getBean(UrpUser.class, "select UserName from Urp_User");
		System.out.println(bean);
	}
	@org.junit.Test
	public void resultSetTest() throws Exception{
		Statement statement=null;
		Connection connection=null;
		ResultSet rs=null;
		connection = JbdcUtil.getConnection();
		statement = connection.createStatement();
		rs = statement.executeQuery("select * from Urp_User");
		//我在以前讲JDBC逆向工程的时候讲过，这里不提
		ResultSetMetaData metaData = rs.getMetaData();
		int columnCount = metaData.getColumnCount();
		for (int i = 1; i < columnCount+1; i++) {
			System.out.println(metaData.getColumnName(i));
			System.out.println(metaData.getColumnType(i));
			System.out.println(metaData.getColumnTypeName(i));
		}
	}
}
