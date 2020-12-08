package org.com.action;

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

import org.com.annotation.Controller;
import org.com.annotation.RequestMapping;
import org.com.po.UrpUser;
import org.com.util.*;
/**
 * 事实上加了这个注解只是个标记，如果是用配置文件配置的就不需要加这个标记。在spring中加这个标记只是为了在扫描过程中能够知道这个类是一个控制器
 * @author Smith
 *
 */
@Controller
public class UserAction{
	//反射机制中的getMthod只能拿到public也就是公有类型的方法，我们这里为了加速项目的开发而不是局限于开发mvc的框架所以都设置为public
	//如果你们下去想自己研究，请使用getDeclareMethods来做
	/**
	 * springmvc
	 * 		@Controller
	 * 		class xxx{
	 * 			@RequestMapping
	 * 		}
	 * @param req
	 * @param resp
	 * @Return 视图信息
	 * @throws ServletException
	 * @throws IOException
	 */
	@RequestMapping("/login")
	/**
	 * @ResponseBody这个问题用到解决，也就是直接返回JSON数据 不需要我自己转，是不是 都是在偷懒‘？？？ 
	 * 
	 * 
	 */
	public String login(UrpUser user,HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String str=null;
		//首先获取 用户名 密码
		//获取JDBC对象进行查询
		//我们这里是不是需要自己把客户端传递的参数都要自己通过request对象获取
		/**
		 * 考虑一个问题：能不能通过封装req 把我要的对象直接获取到，也就是我在参数声明我要什么就给我什么？？
		 */
		try{
			/**
			 * 想想？？如何做？？
			 * query("select * from xxx where id=?,name=?",new Object[]{1,"zhangsan"})
			 * 继续偷懒，不想自己在这里弄一堆\转译字符 只想弄一个写sql 然后加一个参数数组，程序帮我自动构建sql语句
			 * user.getUserName()
			 * user.getPassword()
			 */
			UrpUser queryUser = OrmUtil.getBean(UrpUser.class
					,"select * from urp_user where userName=? and password=?"
					,new Object[]{user.getUserName(),user.getPassword()});
			if(queryUser==null){
				req.setAttribute("error", "用户名或密码错误");
				str="/login.jsp";
			}else{
				//保存用户上下文到session中
				req.getSession().setAttribute("user", queryUser);
				str="/main.jsp";
			}
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return str;
	}
	
}
