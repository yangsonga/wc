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
		
		//��servletrequest��servletresponseǿת
				HttpServletRequest request = (HttpServletRequest)servletrequest;
				HttpServletResponse response = (HttpServletResponse)servletresponse;
				//��ȡ�ͻ������󷽷�
				String method = request.getMethod();
				if(method.equalsIgnoreCase("get")) {
					//get��ʽֻ��tomact7�����²Ż�����
					//��ȡ��������Ϣ
					String serverInfo = request.getServletContext().getServerInfo();
					//��ȡ�汾��
					String info =serverInfo.substring(serverInfo.indexOf("/")+1, serverInfo.indexOf("/")+2);
					//�Ƚϰ汾��
					if(Integer.parseInt(info)<=7) {
						//�����ڲ��࣬���ڲ�����Ϊ��������  filterchain.doFilter()����
						filterchain.doFilter(new MyWrapper(request), response);
						//��ִ����������
						return ; 
					}else {
						filterchain.doFilter(request, response);
						return;
					}
				}else {
					//post����
					request.setCharacterEncoding("utf-8");
					response.setContentType("text/html;charset=utf-8");
					filterchain.doFilter(request, response);
				}
	}

	class MyWrapper extends HttpServletRequestWrapper{
        //��ΪҪ��ȡ����������һ��HttpServletRequest������
		HttpServletRequest request;
		
		public MyWrapper(HttpServletRequest request) {
			super(request); 
			this.request = request;
		}

		@Override
		public String getParameter(String name) {
			String value = null;
			try {
				//�����ķǿ��ж�
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
		System.out.println("��ʼ��");
	}

}
