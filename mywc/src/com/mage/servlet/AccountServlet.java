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
			 //分页条件查询
			 //queryAccount(request,response);
			 queryAccountPageByCondition(request,response);
		 }else if("addAccount".equals(actionName)){
			 //添加账户
			 addAcount(request,response);
		 }else if("updateAccount".equals(actionName)) {
			 //修改账户
			 updateAccount(request,response);
		 }else if("deleteAccount".equals(actionName)) {
			 //删除账户
			 deleteAccount(request,response);
		 }else if("queryAccountList".equals(actionName)) {
			 queryAccountListByUid(request,response);
		 }
	}

	private void queryAccountListByUid(HttpServletRequest request, HttpServletResponse response) {
	   //获取uid
		User user = (User)request.getSession().getAttribute("user");
		int uid = user.getId();
		//通过uid查询用户对应的账户
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
		//获取用户id
		User user = (User) request.getSession().getAttribute("user");
		int uid = user.getId();
	  //获取前台传过来的参数
		String name = request.getParameter("aname");
		String type = request.getParameter("atype");
		String createTime = request.getParameter("createTime");
		String page = request.getParameter("page");
		String rows = request.getParameter("rows");
		int currentPage = 1;
		int pageSize = 5;
		//创建参数集合
		List<Object> parms = new ArrayList<Object>();
		//判断参数
		if(!StringUtil.isEmpty(page)) {
			currentPage = Integer.parseInt(page);
		}
		if(!StringUtil.isEmpty(rows)) {
			pageSize = Integer.parseInt(rows);
		}
		
		//创建需要的sql，一个查询条件记录总数，一个查询分页的记录数
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
		//执行sql语句，获取查询结果 
		 List<Account> ls = DBUtil.query(sqlCount.toString(), Account.class,parms);
		 //分页索引以及每页记录数
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
		 //把格式转换成datagrid对应的形式
		 HashMap<String, Object> hashMap = new HashMap<String,Object>();
		 hashMap.put("total", ls.size());
		 hashMap.put("rows", pageList);
		 //转换成json
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
		//创建默认的当前页码，以及每页的记录数
		int currentPage = 1;
		int pageSize = 5;
		if(!StringUtil.isEmpty(page)) {
			currentPage = Integer.parseInt(page);
		}
		if(!StringUtil.isEmpty(rows)) {
			pageSize = Integer.parseInt(rows);
		}
		//传入参数，分页的查询结果
		List<Account> ls = accountServiceImpl.queryAccountPages(user.getId(), currentPage, pageSize);
		//查询总记录数
		List<Account> List = accountServiceImpl.queryAccount(user.getId());
		 int total = List.size();
		//将结果转换成datagrid需要的形式
		HashMap<String, Object> hashMap = new HashMap<String,Object>();
		hashMap.put("total", total);
		hashMap.put("rows", ls);
		//将hashMap转换成json格式
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
