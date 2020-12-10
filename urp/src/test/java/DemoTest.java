import java.lang.reflect.Field;

import org.junit.Test;

import sun.misc.Unsafe;

public class DemoTest {
	public String string1 = new String("hello");
	@Test
	public void sqlPaserTest(){
		Object[] args={"root",1};
		int argsIndex=0;
		StringBuilder stringBuilder = new StringBuilder();
		String[] split = "select    * from urp_user where userName= ? and password= ?".split(" ");
		for (String string : split) {
			if(string.contains("?")){
				Object object = args[argsIndex++];
				if(object instanceof String){
					string="\""+string.replace("?", (String)object)+"\"";
				}
				if(object instanceof Integer){
					string=string.replace("?", String.valueOf(object));
				}
				stringBuilder.append(string).append(" ");
			}else{
				stringBuilder.append(string).append(" ");
			}
		}
		System.out.println(stringBuilder.toString());
	}
	@Test
	public void stringTest()throws Exception{
		/**
		 * string类允许继承，final修饰。
		 * hashCode方法能复写么？？？？？？
		 * 那怎么会问：在不修改hashcode的前提下如何让这两个字符串对象相等？？？？？？
		 */
		DemoTest demoTest = new DemoTest();
		String string2 = new String("hello");
		Object[] object={string1};
		 Field field = Unsafe.class.getDeclaredFields()[0];
		 field.setAccessible(true);
		Unsafe unsafe = (Unsafe)field.get(null);
		int arrayBaseOffset = unsafe.arrayBaseOffset(Object[].class);
		int addressSize = unsafe.addressSize();
		long long1 = unsafe.getLong(object, arrayBaseOffset);
		long objectFieldOffset = unsafe.objectFieldOffset(demoTest.getClass().getField("string1"));
		System.out.println(Integer.toHexString((int)objectFieldOffset));
		/**
		 * 0001 0000
		 * 0xd64647a8
		 */
		System.out.println(object);
		System.out.println(Integer.toHexString((int)long1));
		StackTraceElement[] stackTrace = new RuntimeException().getStackTrace();
		for (StackTraceElement stackTraceElement : stackTrace) {
			System.out.println(stackTraceElement.getClassName());
		}
		
	}
}
