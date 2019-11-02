package com.mage.filter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

/*
 * Servlet Filter implementation class EncodeFilter
 */
@WebFilter("/*")
public class EncodeFilter implements Filter {

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest servletrequest, ServletResponse servletresponse, FilterChain filterchain) throws IOException, ServletException {
		
		//将servletrequest，servletresponse强转
				HttpServletRequest request = (HttpServletRequest)servletrequest;
				HttpServletResponse response = (HttpServletResponse)servletresponse;
				//获取客户端请求方法
				String method = request.getMethod();
				if(method.equalsIgnoreCase("get")) {
					//get方式只有tomact7及以下才会乱码
					//获取服务器信息
					String serverInfo = request.getServletContext().getServerInfo();
					//截取版本号
					String info =serverInfo.substring(serverInfo.indexOf("/")+1, serverInfo.indexOf("/")+2);
					//比较版本号
					if(Integer.parseInt(info)<=7) {
						//创建内部类，把内部类作为参数传给  filterchain.doFilter()方法
						filterchain.doFilter(new MyWrapper(request), response);
						//不执行下面内容
						return ; 
					}else {
						filterchain.doFilter(request, response);
						return;
					}
				}else {
					//post方法
					request.setCharacterEncoding("utf-8");
					response.setContentType("text/html;charset=utf-8");
					filterchain.doFilter(request, response);
				}
	}

	class MyWrapper extends HttpServletRequestWrapper{
        //因为要获取参数，保存一个HttpServletRequest的引用
		HttpServletRequest request;
		
		public MyWrapper(HttpServletRequest request) {
			super(request); 
			this.request = request;
		}

		@Override
		public String getParameter(String name) {
			String value = null;
			try {
				//参数的非空判断
				if(name != null) {
					value = new String(request.getParameter(name).getBytes("ISO-8859-1"),"utf-8");
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			return value;
			
		}

	   
		
	}
	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		System.out.println("初始化");
	}

}
