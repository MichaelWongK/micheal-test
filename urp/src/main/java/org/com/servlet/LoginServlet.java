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

import org.com.po.UrpUser;
import org.com.util.*;
@WebServlet("/loginServlet")
public class LoginServlet extends HttpServlet{
	
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//首先获取 用户名 密码
		//获取JDBC对象进行查询
		String userName = req.getParameter("userName");
		String password = req.getParameter("password");
		try{
			Connection connection = JbdcUtil.getConnection();
			UrpUser user = OrmUtil.getBean(UrpUser.class, "select * from urp_user where userName=\""+userName
					+"\""+" and password=\""+password+"\"");
			if(user==null){
				req.setAttribute("error", "用户名或密码错误");
				req.getRequestDispatcher("/login.jsp").forward(req, resp);
			}else{
				req.getRequestDispatcher("/main.jsp").forward(req, resp);
			}
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
}
