package org.com.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.com.po.UrpUser;
import org.com.util.*;
@WebServlet("/loginOutServlet")
public class LoginOutServlet extends HttpServlet{
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession();
		//如果这里需要保存session，如电商项目请不要调用这个方法，会把所有的session信息全部清空，如购物车里的内容等等
		session.invalidate();
		req.getRequestDispatcher("/login.jsp").forward(req, resp);
	}
	
}
