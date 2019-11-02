package com.mage.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.mage.dao.UserService;
import com.mage.impl.UserserviceImpl;
import com.mage.po.User;
import com.mage.util.StringUtil;

/**
 * Servlet implementation class User
 */
@WebServlet("/user")
public class UserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private UserService userServiceImpl = new UserserviceImpl();
	/**
	 * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String actionName = request.getParameter("actionName");
		if("login".equals(actionName)) {
			login(request,response);
		}else if("logout".equals(actionName)) {
			logout(request,response);
		}
	}
	private void logout(HttpServletRequest request, HttpServletResponse response) {
		//清除session,清除cookie
		request.getSession().invalidate();
		Cookie cookie = new Cookie("user", null);
		response.addCookie(cookie);
		try {
			response.sendRedirect("/mywc/login.jsp");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	private void login(HttpServletRequest request, HttpServletResponse response) {
		String name = request.getParameter("uname");
		String pwd = request.getParameter("upwd");
		if(StringUtil.isEmpty(name)) {
			request.setAttribute("uname", name);
			request.setAttribute("upwd", pwd);
			request.setAttribute("msg", "用户名不能为空");
			try {
				request.getRequestDispatcher("/login.jsp").forward(request, response);
			} catch (ServletException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if(StringUtil.isEmpty(pwd)) {
			request.setAttribute("uname", name);
			request.setAttribute("upwd", pwd);
			request.setAttribute("msg", "密码不能为空");
			try {
				request.getRequestDispatcher("/login.jsp").forward(request, response);
			} catch (ServletException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		User user = (User) userServiceImpl.queryUserByname(name);
		if(pwd.equals(user.getPwd())) {
			//存入作用域
			HttpSession session = request.getSession();
			session.setAttribute("user", user);
			//存Cookie
			Cookie cookie = new Cookie("user",user.getName()+"-"+user.getPwd());
			response.addCookie(cookie);
			//重定向
			try {
				response.sendRedirect("/mywc/index.jsp");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else {
			request.setAttribute("uname", name);
			request.setAttribute("upwd", pwd);
			request.setAttribute("msg", "用户名或者密码不正确");
			try {
				request.getRequestDispatcher("/login.jsp").forward(request, response);
			} catch (ServletException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
