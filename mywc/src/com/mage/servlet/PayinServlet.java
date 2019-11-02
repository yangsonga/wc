package com.mage.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mage.dao.PayinService;
import com.mage.impl.PayinServiceImpl;
import com.mage.po.PayIn;
import com.mage.po.SqlParms;
import com.mage.po.User;
import com.mage.util.DBUtil;
import com.mage.util.StringUtil;

/**
 * Servlet implementation class Payin
 */
@WebServlet("/payin")
public class PayinServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private static PayinService payInServiceImpl = new PayinServiceImpl();
	/**
	 * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//获取参数，判断执行那个操作
		String actionName = request.getParameter("actionName");
		if("queryPayinByCondition".equals(actionName)) {
			//分页条件查询
			queryPayinByCondition(request,response);
			return;
		}else if("addPayIn".equals(actionName)) {
			//添加收入
			addPayIn(request,response);
			return;
		}else if("updatePayIn".equals(actionName)) {
			//修改收入
			updatePayIn(request,response);
			return;
		}else if("deletePayIn".equals(actionName)) {
			//删除收入
			deletePayIn(request,response);
			return;
		}
	}
	private void deletePayIn(HttpServletRequest request, HttpServletResponse response) {
		//接收请求参数
		String ids = request.getParameter("ids");
		//参数非空判断
		if(StringUtil.isEmpty(ids)) {
			try {
				response.getWriter().write("0");
				return;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//分割字符串，得到每一个id
		String[] ss = ids.split(",");
		//创建PayIn对象集合
		List<PayIn> ls = new LinkedList<PayIn>();
		for(int i=0;i<ss.length;i++) {
			PayIn p = payInServiceImpl.queryById(Integer.parseInt(ss[i]));
			//加入进集合
			ls.add(p);
		}
		//遍历集合
		//由于集合中的sql语句没变化，只是绑定变量发生改变，所以把sql语句声明咋外面
		String sqlAccount = "update account set money = money-?,updateTime=now() where id=?";
		String sqlPayIn = "delete from payin where id=?";
		int row = -1;
		//每个PayIn对象对应两条sql语句的执行，且所有的sql执行JDBC代码属于一个事务
		List<SqlParms> spList = new LinkedList<SqlParms>();
		for(PayIn p :ls) {
			//先给account表减去对应的金额,通过PayIn对象的accountId
			//再payin表里把数据删除,通过PayIn对象的id
			List<Object> accountList = new LinkedList<Object>();
			accountList.add(p.getMoney());
			accountList.add(p.getAcountId());
			List<Object> payInList = new LinkedList<Object>();
			payInList.add(p.getId());
			//将每个PayIn对象对应的两个SqlParms对象放入List<SqlParms>集合
			spList.add(new SqlParms(sqlAccount, accountList));
			spList.add(new SqlParms(sqlPayIn, payInList));
		}
		//执行sql,所有的sql属于同一个事务
		row = payInServiceImpl.update(spList);
		if(row<1) {
			try {
				response.getWriter().write("0");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else {
			try {
				response.getWriter().write("1");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	private void updatePayIn(HttpServletRequest request, HttpServletResponse response) {
		//接收请求参数
		String inName = request.getParameter("inName");
		String inType = request.getParameter("inType");
		String formMoney = request.getParameter("formMoney");
		String remark = request.getParameter("remark");
		String formAccountId = request.getParameter("formAccountId");
		String pId = request.getParameter("pId");
		String datagridAccountId = request.getParameter("datagridAccountId");
		String datagridMoney = request.getParameter("datagridMoney");
		//判断参数
		if(StringUtil.isEmpty(inName)||StringUtil.isEmpty(inType)||StringUtil.isEmpty(formMoney)||StringUtil.isEmpty(formAccountId)||StringUtil.isEmpty(pId)||StringUtil.isEmpty(datagridAccountId)||StringUtil.isEmpty(datagridMoney)) {
			//只要有一个为空，返回失败给用户
			try {
				response.getWriter().write("0");
				return;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//初始化sql
		//111111111：原来的账户修改，金额减去数据表格中的金额
		String sqlOld = "update account set money=money-?,updateTime=now() where id = ?";
		//创建参数集合，将需要的参数放入集合中
		List<Object> oldParms = new ArrayList<Object>();
		oldParms.add(datagridMoney);
		oldParms.add(datagridAccountId);
		//使用sqlParms对象存储sql语句以及对应参数
		SqlParms oldSqlParms = new SqlParms(sqlOld, oldParms);
		
		//22222222：正确的账户加上正确的金额
		String sqlNew = "update account set money=money+?,updateTime=now() where id = ?";
		//创建参数集合，将需要的参数放入集合中
		List<Object> newParms = new ArrayList<Object>();
		newParms.add(formMoney);
		newParms.add(formAccountId);
		//使用sqlParms对象存储sql语句以及对应参数
		SqlParms newSqlParms = new SqlParms(sqlNew, newParms);
		
		//33333333：修改payin表，把对应的收入记录做更改
		String sqlPayIn = "update payin set inName=?,money=?,inType=?,accountId=?,remark=?,updateTime=now() where id=?";
		//创建参数集合，将需要的参数放入集合中
		List<Object> payInParms = new ArrayList<Object>();
		payInParms.add(inName);
		payInParms.add(formMoney);
		payInParms.add(inType);
		payInParms.add(formAccountId);
		payInParms.add(remark);
		payInParms.add(pId);
		//使用sqlParms对象存储sql语句以及对应参数
		SqlParms payInSqlParms = new SqlParms(sqlPayIn, payInParms);
		
		//把sqlParms对象放入集合中
		List<SqlParms> ls = new ArrayList<>();
		ls.add(oldSqlParms);
		ls.add(newSqlParms);
		ls.add(payInSqlParms);
		//调用方法，获得返回值
		 int row = payInServiceImpl.update(ls);
		//判断返回值，并将结果发送给前台
		 if(row<1) {
			 try {
				response.getWriter().write("0");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 }else {
			 try {
				response.getWriter().write("1");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 }
		
	}
	private void addPayIn(HttpServletRequest request, HttpServletResponse response) {
		//获取参数
		String payInName = request.getParameter("inName");
		String payinType = request.getParameter("inType");
		String accountId = request.getParameter("accountId");
		String money = request.getParameter("money");
		String remark = request.getParameter("remark");
		
		System.out.println(accountId);
		//非空判断
		if(StringUtil.isEmpty(payInName)||StringUtil.isEmpty(payinType)||StringUtil.isEmpty(accountId)||StringUtil.isEmpty(money)||StringUtil.isEmpty(remark)) {
			//给前台发送失败响应
			try {
				response.getWriter().write("0");
				return;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//将参数转为对应的形式
		double payInMoney = Double.parseDouble(money);
		//参数不为空时
		//将参数传入方法中，获取返回结果(受影响的行数)
		int rows = payInServiceImpl.insert(payInName, payInMoney, payinType,Integer.parseInt(accountId), remark);
		if(rows<0) {
			try {
				response.getWriter().write("0");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else {
			 int aid = Integer.parseInt(accountId);
			//account表也要做相应的修改
			StringBuilder sqlAccount = new StringBuilder("UPDATE account SET money=money+?, updateTime=now() WHERE id=?");
			//把参数放入参数集合中
			List<Object> parms = new ArrayList<Object>();
			parms.add(money);
			parms.add(aid);
			//使用DBUtil的静态方法
			int row = DBUtil.writer(sqlAccount.toString(), parms);
			if(row>0) {
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
	}
	//分页条件查询
	private void queryPayinByCondition(HttpServletRequest request, HttpServletResponse response) {
		//获取前台请求参数
		String inName = request.getParameter("inName");
		String inType = request.getParameter("inType");
		String createTime = request.getParameter("createTime");
		String page = request.getParameter("page");
		String rows = request.getParameter("rows");
		//设置初始化页面以及每页记录数
		int currentPage = 1;
		int pageSize = 5;
		//获取作用域中的uid
		  User user = (User)request.getSession().getAttribute("user");
		  Integer uid = user.getId();
		//初始化sql语句，两条，查询总数，以及每页记录
		StringBuilder sqlCount = new StringBuilder("SELECT p.id,inName,p.money,inType,accountId,p.createTime,p.updateTime,p.remark,accountName FROM payin p INNER JOIN account a ON p.accountId = a.id AND a.uid ="+uid);
		StringBuilder sqlList = new StringBuilder("SELECT p.id,inName,p.money,inType,accountId,p.createTime,p.updateTime,p.remark,accountName FROM payin p INNER JOIN account a ON p.accountId = a.id AND a.uid ="+uid);
		//创建参数集合
		List<Object> parms = new ArrayList<Object>();
		//判断参数，如果存在加入参数集合，否则不加
		if(!StringUtil.isEmpty(inName)) {
			parms.add("%"+inName+"%");
			sqlCount.append(" and inName like ?");
			sqlList.append(" and inName like ?");
		}
		if(!StringUtil.isEmpty(inType)) {
			parms.add(inType);
			sqlCount.append(" and inType=?");
			sqlList.append(" and inType=?");
		}
		if(!StringUtil.isEmpty(createTime)) {
			parms.add(createTime);
			sqlCount.append(" and createTime<?");
			sqlList.append(" and createTime<?");
		}
		//分页查询sql
		sqlList.append(" LIMIT ?,?");
		
		if(!StringUtil.isEmpty(page)) {
			currentPage = Integer.parseInt(page);
		}
		if(!StringUtil.isEmpty(rows)) {
			pageSize = Integer.parseInt(rows);
		}
		//执行sql得到返回值
		List<PayIn> ls = DBUtil.query(sqlCount.toString(), PayIn.class, parms);
		//把当前页面以及每页记录数加入参数集合
		int index = (currentPage-1)*pageSize;
		parms.add(index);
		parms.add(pageSize);
		List<PayIn> payInList = DBUtil.query(sqlList.toString(), PayIn.class, parms);
		//把数据转换成easy-ui规定的形式
		HashMap<String, Object> hashMap = new HashMap<String,Object>();
		hashMap.put("total", ls.size());
		hashMap.put("rows", payInList);
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
