package com.mage.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.mage.dao.OutTypeService;
import com.mage.impl.OutTypeServiceImpl;
import com.mage.po.OutType;

/**
 * Servlet implementation class OutType
 */
@WebServlet("/outType")
public class OutTypeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private static OutTypeService outTypeServiceImpl = new OutTypeServiceImpl();

    /**
	 * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//���ղ���
		String actionName = request.getParameter("actionName");
		if("queryParentType".equals(actionName)) {
			queryParentType(request,response);
		}else if("queryChildType".equals(actionName)) {
			queryChildType(request,response);
		}else if("querySpecificParentType".equals(actionName)) {
			querySpecificParentType(request,response);
		}
	}

	private void querySpecificParentType(HttpServletRequest request, HttpServletResponse response) {
		 //��ȡ����
		String pId = request.getParameter("pId");
		OutType outType = outTypeServiceImpl.querySpecificParent(Integer.parseInt(pId));
		//ת����combobox�������ʽ
		Gson gson = new Gson();
		String json = gson.toJson(outType);
		//д����ǰ̨
		try {
			response.getWriter().write(json);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void queryChildType(HttpServletRequest request, HttpServletResponse response) {
		//���ղ���
	   String pid = request.getParameter("pId");
	   List<OutType> ls = outTypeServiceImpl.queryChildType(Integer.parseInt(pid));
	   //ת��json��ʽ
	   Gson gson = new Gson();
	   String json = gson.toJson(ls);
	   try {
		response.getWriter().write(json);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}

	private void queryParentType(HttpServletRequest request, HttpServletResponse response) {
		List<OutType> ls = outTypeServiceImpl.queryParentType();
		//������ת����combobox��Ҫ�ĸ�ʽ
		Gson gson = new Gson();
		String json = gson.toJson(ls);
		//д����ǰ̨
		try {
			response.getWriter().write(json);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
