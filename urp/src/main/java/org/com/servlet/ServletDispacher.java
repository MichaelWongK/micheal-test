package org.com.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.com.context.ActionContext;
import org.com.context.ActionContext.ActionMetaData;
import org.com.util.ActionContextUtil;
import org.com.util.ServletContextUtils;


@WebServlet("*.action")
public class ServletDispacher extends HttpServlet {
	/**
	 * 根据传入的类的参数信息，注入需要的类
	 * @param parameterTypes 
	 * @return 构造好的参数数组
	 */
	public static Object[] genarateMethodArg(Class<?>[] parameterTypes){
		if(parameterTypes.length==0){
			return new Object[]{};
		}else{
			Object[] args=new Object[parameterTypes.length];
			Map<String, ActionMetaData> loadActionData = ActionContextUtil.loadActionData();
			//还可能要的是数据对象：PO
			for (Class<?> clazz : parameterTypes) {
				//如果为http对象则注入，考虑一个问题：我如何拿到httprequest以及其他对象
				//还记得有一个类叫做ServletActionContext???struts2.0里的，通过它可以获取到当前的request对象
				if(clazz==HttpServletRequest.class){
					
				}else if(clazz==HttpServletResponse.class){
					
				}else{
					//这里就是我们定义的bean信息
					
				}
			}
			return args;
		}
	}
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String requestURI = req.getRequestURI();
		//取uri最后的信息
		String actionAdress = requestURI.substring(requestURI.lastIndexOf("/"));
		//做转发处理
		try {
			//spring里有单例模式事实上就是在这里缓存了
			ActionContext.ActionMetaData urlMethod = ActionContext.getActionMeta(actionAdress);
			if(urlMethod!=null){
				//这里是反射的method方法的invoke：
				//第一个参数：调用的对象的方法，如果是静态方法直接复制null，第二个是方法的参数
				Class<?> actionClazz = urlMethod.getActionClazz();
				Method currentMethod = urlMethod.getCurrentMethod();
				Object newInstance = actionClazz.newInstance();
				//执行方法之前参数列表需要构造
				Object[] args={};
				Class<?>[] parameterTypes = urlMethod.getParameterTypes();
				//根据参数的类型注入相应的类,参数的顺序要保证
				args=genarateMethodArg(parameterTypes);
				currentMethod.invoke(newInstance,args);
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
			//可配置全局异常
			e.printStackTrace();
		}
	}
	
}
