package org.com.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;

import org.com.annotation.Controller;
import org.com.annotation.RequestMapping;
import org.com.context.ActionContext;
import org.com.context.ActionContext.ActionMetaData;
import org.com.servlet.ServletDispacher;
import org.junit.Test;

public class ActionContextUtil {
	private static Properties globalProperties;

	/**
	 * 加載資源文件，默認是類加載路徑的根目錄
	 * 
	 * @param pathStr
	 * @return
	 */
	private static Properties loadFile(String pathStr) throws Exception {
		InputStream resourceAsStream = ActionContextUtil.class.getClassLoader().getResourceAsStream(pathStr);
		Properties properties = new Properties();
		properties.load(resourceAsStream);
		return properties;
	}

	// 加载路径和Action的映射
	public static Map<String, ActionContext.ActionMetaData> loadActionData() {
		Map<String, ActionMetaData> controllerMap = new HashMap<>();
		try {
			globalProperties = loadFile("mvc.properties");
			// 拿到controller的类
			for (Entry<Object, Object> entry : globalProperties.entrySet()) {
				String key = (String) entry.getKey();
				if (key.startsWith("mvc.action")) {
					String clazzStr = (String) entry.getValue();
					Class<?> clazz = Class.forName(clazzStr);
					Controller[] annotationsByType = clazz.getAnnotationsByType(Controller.class);
					if (annotationsByType.length != 0) {
						// 获取所有声明的方法
						Method[] declaredMethods = clazz.getDeclaredMethods();
						for (Method method : declaredMethods) {
							RequestMapping[] declaredAnnotationsByType = method
									.getDeclaredAnnotationsByType(RequestMapping.class);
							if (declaredAnnotationsByType.length != 0) {
								RequestMapping requestMapping = declaredAnnotationsByType[0];
								String requestMappingStr = requestMapping.value();
								// 获取到每个类方法的requestMapping后 还需要获取到其他的类信息么？
								/**
								 * 1、如果不需要
								 */
								/**
								 * 可以在这里做处理，获取到有requestMapping的方法，然后获取到它的参数，通过
								 * 参数给它传递要的东西
								 */
								ActionMetaData ActionMetaData = new ActionMetaData();
								ActionMetaData.setActionClazz(clazz);
								ActionMetaData.setCurrentMethod(method);
								// 获取方法参数
								Class<?>[] parameterTypes = method.getParameterTypes();
								ActionMetaData.setParameterTypes(parameterTypes);
								controllerMap.put(requestMappingStr, ActionMetaData);
							}
						}
					}
				}

			}
			return controllerMap;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	// 加载当前上下文的PO
	public static List<Class<?>> loadBeanDefination() throws Exception {
		String componentScanPath = globalProperties.getProperty("mvc.config.componentScanPath");
		List<Class<?>> recursionLoadBeanDefination = null;
		if (componentScanPath == null) {
			return null;
		} else {
			// 首先获取到类加载的根目录，然后替换掉.
			URL resource = ActionContextUtil.class.getClassLoader().getResource("mvc.properties");
			String string = resource.toString();
			String realPath = string.substring("file:/".length()).replace("/", File.separator);
			String scanPath = realPath.substring(0, realPath.lastIndexOf("\\")) + File.separator
					+ (componentScanPath.replace(".", File.separator));
			File file = new File(scanPath);
			ArrayList<Class<?>> beasList = new ArrayList<>();
			recursionLoadBeanDefination = recursionLoadBeanDefination(file, beasList);
		}
		return recursionLoadBeanDefination;
	}

	@Test
	public void test() throws Exception {
		loadActionData();
		System.out.println(loadBeanDefination());
		/*loadActionData();
		String componentScanPath = globalProperties.getProperty("mvc.config.componentScanPath");
		URL resource = ActionContextUtil.class.getClassLoader().getResource("mvc.properties");
		String string = resource.toString();
		String realPath = string.substring("file:/".length()).replace("/", File.separator);
		String scanPath = realPath.substring(0, realPath.lastIndexOf("\\")) + File.separator
				+ (componentScanPath.replace(".", File.separator));
		File file = new File(scanPath);
		
		 * E:\eclise_fish_new\URP\target\classes\org\com\po
		 * E:\eclise_fish_new\URP\target\classes\org\com\po
		 * E:\eclise_fish_new\URP\target\classes\mvc.properties\org\com\po
		 
		// 开始扫描路径下的类，发现有@Bean注解的都加入到bean定义中
		ArrayList<Class<?>> beasList = new ArrayList<>();
		List<Class<?>> recursionLoadBeanDefination = recursionLoadBeanDefination(file, beasList);
		System.out.println(recursionLoadBeanDefination);*/
	}

	private static List<Class<?>> recursionLoadBeanDefination(File file, ArrayList<Class<?>> arrayList)
			throws Exception {
		File[] listFiles = file.listFiles();
		for (File clazzFile : listFiles) {
			if (clazzFile.isDirectory()) {
				File[] listFiles2 = clazzFile.listFiles();
				for (File file2 : listFiles2) {
					recursionLoadBeanDefination(file2, arrayList);
				}
			} else {
				if (clazzFile.getName().endsWith(".class")) {
					// 如何加载这个类？
					ClassLoader classLoader = ActionContextUtil.class.getClassLoader();
					// E:\eclise_fish_new\URP\target\classes\org\com\po
					String canonicalPath = clazzFile.getCanonicalPath();
					String substring = canonicalPath
							.substring(canonicalPath.indexOf("classes") + "classes".length() + 1);
					substring = substring.substring(0, substring.lastIndexOf("."));
					// 使用app类加载来进行加载，现在主要是要过去类的路径
					Class<?> forName = Class.forName(substring.replace("\\", "."));
					arrayList.add(forName);
				}
			}
		}
		return arrayList;
	}

	@Test
	public void clazzLoaderTest() throws Exception {
		Class<?> forName = Class.forName("org.com.po.UrpUser");
		System.out.println(forName.getName());
		// systemclassloader???
		ClassLoader classLoader = ActionContextUtil.class.getClassLoader();
		Class<?> loadClass = classLoader.loadClass("org.com.po.UrpUser");
		System.out.println(loadClass.getName());
		System.out.println(forName == loadClass);
		// 线程上下文类加载器
		ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
		Class<?> loadClass2 = contextClassLoader.loadClass("org.com.po.UrpUser");
		System.out.println(loadClass2.getName());
		System.out.println(loadClass2 == loadClass);
		// 还有其他方式加载么？
		URL resource = ActionContextUtil.class.getClassLoader().getResource("mvc.properties");
		String string = resource.toString();
		resource = new URL("file:/E:/eclise_fish_new/URP/target/classes");
		System.out.println(resource);
		// 关键点是是什麼？是不是默認使用app類加載器？？？？
		URLClassLoader newInstance = new URLClassLoader(new URL[] { resource });
		Class<?> loadClass3 = newInstance.loadClass("org.com.po.UrpUser");
		System.out.println(loadClass3.getName());
		System.out.println(loadClass3 == loadClass2);
		//
		MyClassloader myClassloader = new MyClassloader();
		File file = new File("E:\\eclise_fish_new\\URP\\target\\classes\\" + "org\\com\\po\\UrpUser.class");
		FileInputStream fileInputStream = new FileInputStream(file);
		System.out.println("file length:" + file.length());
		int i = -1;
		int j = 0;
		byte[] bufferByte = new byte[2960];
		while ((i = fileInputStream.read()) != -1) {
			bufferByte[j++] = (byte) i;
		}
		System.out.println(i);
		Class<?> loadMyClass = myClassloader.loadMyClass(bufferByte);
		System.out.println(loadMyClass.getName());
		// 為什麼是false？？？？，在jvm中不同的类加载器加载的类的class都是不一样的，这叫做java的安全沙箱。为什么上面的
		// 所有加载器加载的对象都是true，因为他们都是同一个类加载器加载的：appclassloader
		System.out.println(loadMyClass == loadClass3);
	}

	static class MyClassloader extends ClassLoader {

		/*
		 * @Override public Class<?> loadClass(String name) throws
		 * ClassNotFoundException { // TODO Auto-generated method stub
		 * this.defineClass(name, b, off, len) return null; }
		 */
		public Class<?> loadMyClass(byte[] byteArray) {
			return this.defineClass(byteArray, 0, byteArray.length);
		}
	}

	@Test
	public void collectionTest() throws Exception {
		String string = "E:\\eclise_fish_new\\URP\\target\\classes\\org\\com\\po";
		System.out.println();
	}

}
