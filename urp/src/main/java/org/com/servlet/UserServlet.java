package org.com.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/userServlet")
public class UserServlet extends HttpServlet {
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String method = req.getParameter("method");
		if ("login".equals(method)) {
				//映射方法
		}
		if ("loginOut".equals(method)) {
				
		}
		if ("login".equals(method)) {

		}
		if ("login".equals(method)) {

		}
		if ("login".equals(method)) {

		}
		if ("login".equals(method)) {

		}
		if ("login".equals(method)) {

		}

	}
}
