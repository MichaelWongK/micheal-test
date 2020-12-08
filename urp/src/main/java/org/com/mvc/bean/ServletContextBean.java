package org.com.mvc.bean;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
/**
 * 封装web容器对象
 * @author Smith
 *
 */
public class ServletContextBean {
	private ServletContext servletContext;
	private HttpServletRequest request;
	private HttpSession httpSession;
	private HttpServletResponse response;
	public ServletContextBean(HttpServletRequest request, HttpServletResponse response) {
		this.request = request;
		this.response = response;
		this.httpSession=request.getSession();
		this.servletContext=request.getServletContext();
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
