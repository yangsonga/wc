package com.mage.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mage.dao.AccountService;
import com.mage.impl.AccountServiceImpl;
import com.mage.po.Account;
import com.mage.po.User;
import com.mage.util.DBUtil;
import com.mage.util.StringUtil;

/**
 * Servlet implementation class AccountServlet
 */
@WebServlet("/account")
public class AccountServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private AccountService accountServiceImpl = new AccountServiceImpl();
	/**
	 * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 String actionName = request.getParameter("actionName");
		 if("queryAccount".equals(actionName)) {
			 //��ҳ������ѯ
			 //queryAccount(request,response);
			 queryAccountPageByCondition(request,response);
		 }else if("addAccount".equals(actionName)){
			 //����˻�
			 addAcount(request,response);
		 }else if("updateAccount".equals(actionName)) {
			 //�޸��˻�
			 updateAccount(request,response);
		 }else if("deleteAccount".equals(actionName)) {
			 //ɾ���˻�
			 deleteAccount(request,response);
		 }else if("queryAccountList".equals(actionName)) {
			 queryAccountListByUid(request,response);
		 }
	}

	private void queryAccountListByUid(HttpServletRequest request, HttpServletResponse response) {
	   //��ȡuid
		User user = (User)request.getSession().getAttribute("user");
		int uid = user.getId();
		//ͨ��uid��ѯ�û���Ӧ���˻�
		List<Account> accountList = accountServiceImpl.queryAccount(uid);
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
		String json = gson.toJson(accountList);
		try {
			response.getWriter().write(json);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private void queryAccountPageByCondition(HttpServletRequest request, HttpServletResponse response) {
		//��ȡ�û�id
		User user = (User) request.getSession().getAttribute("user");
		int uid = user.getId();
	  //��ȡǰ̨�������Ĳ���
		String name = request.getParameter("aname");
		String type = request.getParameter("atype");
		String createTime = request.getParameter("createTime");
		String page = request.getParameter("page");
		String rows = request.getParameter("rows");
		int currentPage = 1;
		int pageSize = 5;
		//������������
		List<Object> parms = new ArrayList<Object>();
		//�жϲ���
		if(!StringUtil.isEmpty(page)) {
			currentPage = Integer.parseInt(page);
		}
		if(!StringUtil.isEmpty(rows)) {
			pageSize = Integer.parseInt(rows);
		}
		
		//������Ҫ��sql��һ����ѯ������¼������һ����ѯ��ҳ�ļ�¼��
		StringBuilder sqlCount = new StringBuilder("select * from account where uid="+uid);
		StringBuilder sqlList = new StringBuilder("select * from account where uid="+uid);
		if(!StringUtil.isEmpty(name)) {
			parms.add("%"+name+"%");
			sqlCount.append(" and accountName like ?");
			sqlList.append(" and accountName like ?");
		}
		if(!StringUtil.isEmpty(type)) {
			parms.add(type);
			sqlCount.append(" and accountType=?");
			sqlList.append(" and accountType=?");
		}
		if(!StringUtil.isEmpty(createTime)) {
			parms.add(createTime);
			sqlCount.append(" and createTime<?");
			sqlList.append(" and createTime<?");
		}
		//sqlList.append(" limit "+(currentPage-1)*pageSize+","+pageSize);
		sqlList.append(" limit ?,?");
		//ִ��sql��䣬��ȡ��ѯ��� 
		 List<Account> ls = DBUtil.query(sqlCount.toString(), Account.class,parms);
		 //��ҳ�����Լ�ÿҳ��¼��
		 int index = (currentPage-1)*pageSize;
		 parms.add(index);
		 parms.add(pageSize);
		 List<Account> pageList = DBUtil.query(sqlList.toString(), Account.class,parms);
		/*
		 * System.out.println(ls.size()); 
		 * System.out.println(sqlCount.toString());
		 * System.out.println(pageList.size()); 
		 * System.out.println(sqlList.toString());
		 */
		 //�Ѹ�ʽת����datagrid��Ӧ����ʽ
		 HashMap<String, Object> hashMap = new HashMap<String,Object>();
		 hashMap.put("total", ls.size());
		 hashMap.put("rows", pageList);
		 //ת����json
		 Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
		 String json = gson.toJson(hashMap);
		 try {
			response.getWriter().write(json);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void deleteAccount(HttpServletRequest request, HttpServletResponse response) {
		String ids = request.getParameter("ids");
		int count = accountServiceImpl.deleteAccount(ids);
		if(count>0) {
			try {
				response.getWriter().write("1");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else {
			try {
				response.getWriter().write("0");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

	private void updateAccount(HttpServletRequest request, HttpServletResponse response) {
		String accountName = request.getParameter("accountName");
		String accountType = request.getParameter("accountType");
		double money = Double.parseDouble(request.getParameter("money"));
		String remark = request.getParameter("remark");
		int accountId = Integer.parseInt(request.getParameter("accountId"));
		int count = accountServiceImpl.updateAccount(accountName, money, accountId, remark, accountType);
		   if(count>0) {
			   try {
				response.getWriter().write("1");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		   }else {
			   try {
				response.getWriter().write("0");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		   }
	}

	private void addAcount(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		User user = (User)session.getAttribute("user");
		int uid = user.getId();
		String accountName = request.getParameter("accountName");
		String accountType = request.getParameter("accountType");
		double money = Double.parseDouble(request.getParameter("money"));
		String remark = request.getParameter("remark");
		int count = accountServiceImpl.insertAccount(accountName, money, uid, remark, accountType);
         if(count>0) {
           try {
			response.getWriter().write("1");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
         }else {
        	 try {
				response.getWriter().write("0");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
         }
	}

	private void queryAccount(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		 User user = (User)session.getAttribute("user");
		String page = request.getParameter("page");
		String rows = request.getParameter("rows");
		//����Ĭ�ϵĵ�ǰҳ�룬�Լ�ÿҳ�ļ�¼��
		int currentPage = 1;
		int pageSize = 5;
		if(!StringUtil.isEmpty(page)) {
			currentPage = Integer.parseInt(page);
		}
		if(!StringUtil.isEmpty(rows)) {
			pageSize = Integer.parseInt(rows);
		}
		//�����������ҳ�Ĳ�ѯ���
		List<Account> ls = accountServiceImpl.queryAccountPages(user.getId(), currentPage, pageSize);
		//��ѯ�ܼ�¼��
		List<Account> List = accountServiceImpl.queryAccount(user.getId());
		 int total = List.size();
		//�����ת����datagrid��Ҫ����ʽ
		HashMap<String, Object> hashMap = new HashMap<String,Object>();
		hashMap.put("total", total);
		hashMap.put("rows", ls);
		//��hashMapת����json��ʽ
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
		String json = gson.toJson(hashMap);
		try {
			response.getWriter().write(json);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
