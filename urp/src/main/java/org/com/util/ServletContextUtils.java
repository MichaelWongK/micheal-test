package org.com.util;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 这个类保存当前web环境的信息
 * @author Smith
 * 如何拿到当前容器的这些信息？？？？？
 */
public class ServletContextUtils {
	private ServletContext servletContext;
	private HttpServletRequest request;
	private HttpSession httpSession;
	private HttpServletResponse response;
	
	public ServletContextUtils(HttpServletRequest request, HttpServletResponse response) {
		this.request = request;
		this.response = response;
		this.servletContext=request.getServletContext();
		this.httpSession=request.getSession();
	}

	public ServletContext getServletContext() {
		return servletContext;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public HttpSession getHttpSession() {
		return httpSession;
	}

	public HttpServletResponse getResponse() {
		return response;
	}
	
}
