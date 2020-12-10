package org.com.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.com.annotation.ResponseBody;
import org.com.context.ActionContext;
import org.com.context.ActionContext.ActionMetaData;
import org.com.json.JSON;
import org.com.mvc.bean.ServletContextBean;
import org.com.util.ActionContextUtil;
import org.com.util.ServletContextUtils;

@WebServlet("*.action")
public class ServletDispacher extends HttpServlet {
	/**
	 * 泛型方法，生成bean实例
	 * 1、获取request
	 * 2、根据request获得传递过来的参数
	 * 3、根据bean的名字获取到相对应的参数值，然后对bean进行赋值
	 * @param beanClazz 需要封装信息的bean的class对象
	 * @return 封装好属性的bean实例
	 * @throws Exception
	 */
	public <T>T createBeanObj(Class<T> beanClazz)throws Exception{
		HttpServletRequest httpRequest = ServletContextUtils.getHttpRequest();
		Field[] declaredFields = beanClazz.getDeclaredFields();
		T newInstance = beanClazz.newInstance();
		for (Field field : declaredFields) {
			String name = field.getName();
			String parameter = httpRequest.getParameter(name);
			field.setAccessible(true);
			field.set(newInstance, parameter);
		}
		
		return newInstance;
	}
	/**
	 * 根据传入的类的参数信息，注入需要的类
	 * 
	 * @param parameterTypes
	 * @return 构造好的参数数组
	 */
	public Object[] genarateMethodArg(Class<?>[] parameterTypes)throws Exception {
		if (parameterTypes.length == 0) {
			return new Object[] {};
		} else {
			Object[] args = new Object[parameterTypes.length];
			Map<String, ActionMetaData> loadActionData = ActionContextUtil.loadActionData();
			// 数组索引
			int i = 0;
			// 还可能要的是数据对象：PO
			for (Class<?> clazz : parameterTypes) {
				// 如果为http对象则注入，考虑一个问题：我如何拿到httprequest以及其他对象
				// 还记得有一个类叫做ServletActionContext???struts2.0里的，通过它可以获取到当前的request对象
				// 我如何在任何一个地方都可以获取到web容器内的东西，session，application，request，response等
				if (clazz == HttpServletRequest.class) {
					HttpServletRequest httpRequest = ServletContextUtils.getHttpRequest();
					args[i] = httpRequest;
				} else if (clazz == HttpServletResponse.class) {
					HttpServletResponse httpServletResponse = ServletContextUtils.getHttpResponse();
					args[i] = httpServletResponse;
				} else {
					// 这里就是我们定义的bean信息
					List<Class<?>> loadBeanDefination = ActionContext.getLoadBeanDefination();
					for (Class<?> beanClazz : loadBeanDefination) {
						if(clazz==beanClazz){
							args[i]=createBeanObj(beanClazz);
						}
					}
				}
				i++;
			}
			return args;
		}
	}

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		// 保存req，res构造web环境
		ServletContextUtils.getThreadLocal().set(new ServletContextBean(req, resp));
		String requestURI = req.getRequestURI();
		// 取uri最后的信息
		String actionAdress = requestURI.substring(requestURI.lastIndexOf("/"));
		// 做转发处理
		try {
			// spring里有单例模式事实上就是在这里缓存了
			ActionContext.ActionMetaData urlMethod = ActionContext.getActionMeta(actionAdress);
			if (urlMethod != null) {
				// 这里是反射的method方法的invoke：
				// 第一个参数：调用的对象的方法，如果是静态方法直接复制null，第二个是方法的参数
				Class<?> actionClazz = urlMethod.getActionClazz();
				Method currentMethod = urlMethod.getCurrentMethod();
				Object newInstance = actionClazz.newInstance();
				// 执行方法之前参数列表需要构造
				Object[] args = {};
				Class<?>[] parameterTypes = urlMethod.getParameterTypes();
				// 根据参数的类型注入相应的类,参数的顺序要保证
				args = genarateMethodArg(parameterTypes);
				//调用action处理
				Object invoke = currentMethod.invoke(newInstance, args);
				//渲染视图
				if(invoke!=null){
					if(invoke instanceof String)
						req.getRequestDispatcher((String)invoke).forward(req, resp);
					if(currentMethod.getAnnotationsByType(ResponseBody.class).length!=0){
						//进行JSON处理
						resp.setContentType("application/json;charset=utf-8");
						String parseObject = JSON.parseObject(invoke);
						resp.getWriter().println(parseObject);
						return;
					}
					
				}
			}
			/**
			 * 在springmvc中有一个东西叫做全局异常
			 */
			resp.setStatus(404);
			resp.setContentType("text/html;charset=utf-8");
			PrintWriter writer = resp.getWriter();
			writer.println("404 not found");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			// 可配置全局异常
			e.printStackTrace();
		}
	}

}
