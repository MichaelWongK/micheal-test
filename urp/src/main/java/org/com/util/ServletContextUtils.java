package org.com.util;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.com.mvc.bean.ServletContextBean;

/**
 * 这个类提供访问当前web环境的信息的方法
 * 
 * @author Smith 如何拿到当前容器的这些信息？？？？？ 工具类里的方法都要是静态的 非静态属性不能被静态方法访问，那么如何是好？？
 *         把这几个servlet容器对象都写为静态对象么？？？
 *         这样是不可行的，因为每个连接都有自己的session和request，response 那么我如何保存这几个信息？？？？？？
 */
public class ServletContextUtils {
	private static final ThreadLocal<ServletContextBean> THREAD_LOCAL = new ThreadLocal<>();
	public static ThreadLocal<ServletContextBean> getThreadLocal(){
		return THREAD_LOCAL;
	}
	public static ServletContext getServetContext() {
		ServletContextBean servletContextBean = THREAD_LOCAL.get();
		return servletContextBean.getServletContext();
	}

	public static HttpServletRequest getHttpRequest() {
		ServletContextBean servletContextBean = THREAD_LOCAL.get();
		return servletContextBean.getRequest();
	}

	public static HttpServletResponse getHttpResponse() {
		ServletContextBean servletContextBean = THREAD_LOCAL.get();
		return servletContextBean.getResponse();
	}

	public static HttpSession getHttpSession() {
		ServletContextBean servletContextBean = THREAD_LOCAL.get();
		return servletContextBean.getHttpSession();
	}
}
