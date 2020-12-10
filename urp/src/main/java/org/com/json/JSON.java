package org.com.json;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * JSON解析器入口
 * 
 * @author Smith
 *
 */
public class JSON {
	private static final String DOUBLE_QUOTE="\"";
	/**
	 * List<UrpUser> 1、获取到object的class 2、通过class获取到类型信息 3、根据不同的信息进行不同的操作 private
	 * String department; private Byte type; private String userName; private
	 * Integer userTell; private Integer userId; private Date createTime;
	 * private String password; [{department:"",type:1,Date:??},{}]
	 * 
	 * @param object
	 * @return
	 */
	private static void listJsonObj(StringBuilder stringBuilder,List<?> list)throws Exception{
		stringBuilder.append("[");
		//遍历list内的对象
		for (Object object2 : list) {
			stringBuilder.append("{");
			Field[] declaredFields = object2.getClass().getDeclaredFields();
			for (Field field : declaredFields) {
				field.setAccessible(true);
				String name = field.getName();
				Object object3 = field.get(object2);
				if(object3 instanceof String){
					stringBuilder.append(DOUBLE_QUOTE+name+DOUBLE_QUOTE+":"+DOUBLE_QUOTE+object3+DOUBLE_QUOTE).append(",");
				}
				if(object3 instanceof Number){
					stringBuilder.append(DOUBLE_QUOTE+name+DOUBLE_QUOTE+":"+object3).append(",");
				}
				if(object3 instanceof Date){
					stringBuilder.append(DOUBLE_QUOTE+name+DOUBLE_QUOTE+":"+DOUBLE_QUOTE+object3+DOUBLE_QUOTE).append(",");
				}
			}
			//去掉末尾最后一个,
			stringBuilder.deleteCharAt(stringBuilder.length()-1);
			stringBuilder.append("}");
			stringBuilder.append(",");
		}
		//去掉末尾最后一个,
		stringBuilder.deleteCharAt(stringBuilder.length()-1);
		stringBuilder.append("]");
		
	}
	public static String parseObject(Object object) {
		StringBuilder stringBuilder=new StringBuilder();
		try {
			//List处理方式
			if (List.class.isInstance(object)) {
				//集合里是什么东西？？保存的类型是什么知道么？
				List<?> list = (List<?>) object;
				listJsonObj(stringBuilder,list);
			}
			//Map处理方式
			if (object instanceof Map) {
				Map<Object,Object> map=(Map<Object,Object>) object;
				Set<Entry<Object, Object>> entrySet = map.entrySet();
				stringBuilder.append("{");
				/**
				 * {
				 * 	count:100,
				 * 	data:[]
				 * }
				 */
				StringBuilder stringBuilder_inner=new StringBuilder();
				for (Entry<Object, Object> entry : entrySet) {
					Object key = entry.getKey();
					Object value = entry.getValue();
					if(key instanceof String){
						if(value instanceof List){
							List<?> list=(List<?>) value;
							listJsonObj(stringBuilder_inner,list);
							stringBuilder.append(DOUBLE_QUOTE+key+DOUBLE_QUOTE+":"+stringBuilder_inner.toString()).append(",");
							//清空用于保存 内部list字符串的stringbuilder
							stringBuilder_inner.delete(0, stringBuilder_inner.length());
						}
						if(value instanceof Integer){
							//因为子类复写了 object的toString方法，所以我直接就输出
							stringBuilder.append(DOUBLE_QUOTE+key+DOUBLE_QUOTE+":"+value).append(",");
						}
						if(value instanceof String){
							stringBuilder.append(DOUBLE_QUOTE+key+DOUBLE_QUOTE+":"+DOUBLE_QUOTE+value+DOUBLE_QUOTE).append(",");
						}
					}
				}
				//去掉末尾最后一个,
				stringBuilder.deleteCharAt(stringBuilder.length()-1);
				stringBuilder.append("}");
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return stringBuilder.toString();
	}
}
