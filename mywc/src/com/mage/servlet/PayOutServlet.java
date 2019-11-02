package com.mage.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import com.mage.dao.AccountService;
import com.mage.dao.PayOutService;
import com.mage.impl.AccountServiceImpl;
import com.mage.impl.PayOutServiceImpl;
import com.mage.po.Account;
import com.mage.po.PayOut;
import com.mage.po.SqlParms;
import com.mage.po.User;
import com.mage.util.DBUtil;
import com.mage.util.StringUtil;

/**
 * Servlet implementation class PayOut
 */
@WebServlet("/payout")
public class PayOutServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private static AccountService accountServiceImpl = new AccountServiceImpl();
    private static PayOutService payoutServiceImpl = new PayOutServiceImpl();
	/**
	 * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String actionName = request.getParameter("actionName");
		//判断操作，分发请求
		if("queryPayOutByCondition".equals(actionName)) {
			queryPayOutByCondition(request,response);
		}else if("addPayOut".equals(actionName)) {
			addPayOut(request,response);
			return;
		}else if("updatePayOut".equals(actionName)) {
			updatePayOut(request,response);
			return;
		}else if("deletePayOut".equals(actionName)) {
			deletePayOut(request,response);
			return;
		}else if("queryPayOutByUid".equals(actionName)) {
			queryPayOutByUid(request,response);
		}

	
}

	private void queryPayOutByUid(HttpServletRequest request, HttpServletResponse response) {
		User user = (User)request.getSession().getAttribute("user");
		int uid = user.getId();
		String sql = "SELECT sum(p.money) as money,o2.typeName FROM payout p INNER JOIN account a " + 
				"ON p.accountId=a.id " + 
				"INNER JOIN `user` u " + 
				"ON a.uid = u.id " + 
				"INNER JOIN outtype o " + 
				"ON p.outTypeId = o.id " + 
				"INNER JOIN outtype o2 " + 
				"ON o2.id=o.pid " + 
				"WHERE u.id = ?"+
				" group by o2.typeName";
		List<PayOut> ls = new LinkedList<PayOut>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
		conn = DBUtil.getConn();
		ps = conn.prepareStatement(sql);
		ps.setInt(1,uid);
		rs = ps.executeQuery();
		while(rs.next()) {
		    PayOut	payOut = new PayOut();
			payOut.setMoney(rs.getDouble("money"));
			payOut.setTypeName(rs.getString("typeName"));
			ls.add(payOut);
		}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			DBUtil.closeAll(conn, rs, ps);
		}
		String json = new Gson().toJson(ls);
		try {
			response.getWriter().write(json);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private void deletePayOut(HttpServletRequest request, HttpServletResponse response) {
	   //接收请求参数
		String ids = request.getParameter("ids");
		//判断请求参数
		if(StringUtil.isEmpty(ids)) {
			//如果ids为空，则发送失败信息给前台
			try {
				response.getWriter().write("0");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return;
		}
	  //分割字符串
		String[] idstr = ids.split(",");
		//遍历数组，得到List<PayOut>集合
		List<PayOut> payOutList = new LinkedList<PayOut>();
		for(String id :idstr) {
			PayOut payOut = payoutServiceImpl.queryById(Integer.parseInt(id));
			payOutList.add(payOut);
		}
		
		String accountUpdateSql = "update account set money=money+?,updateTime=now() where id=?";
		String payOutDeleteSql = "delete from payout where id=?";
		
		//返回删除的行数给前台
		int count = 0; //计数器，事务成功的数量
		int row = -1; //获得每次事务的返回结果
		//通过List<PayOut> 集合循环执行sql,每两条sql同一个事务
		for(PayOut p:payOutList) {
			//创建SqlParms集合
			List<SqlParms> sqlParmsList = new LinkedList<SqlParms>();
			//给account表里加上对应的金额
			SqlParms accountUpdate = new SqlParms();
			//创建参数集合
			List<Object> accountUpdateParms = new LinkedList<Object>();
			accountUpdateParms.add(p.getMoney());
			accountUpdateParms.add(p.getAccountId());
			//把sql语句以及参数集合放入SqlParms对象
			accountUpdate.setSql(accountUpdateSql);
			accountUpdate.setParms(accountUpdateParms);
			//在payout表里删除记录
			SqlParms payOutDelete = new SqlParms();
			//创建参数集合
			List<Object> payOutDeleteParms = new LinkedList<Object>();
			payOutDeleteParms.add(p.getId());
			//把sql语句以及参数集合放入SqlParms对象
			payOutDelete.setSql(payOutDeleteSql);
			payOutDelete.setParms(payOutDeleteParms);
			//把SqlParms对象放入集合中
			sqlParmsList.add(accountUpdate);
			sqlParmsList.add(payOutDelete);
			//事务控制，执行sql
			 row = DBUtil.transactionWriter(sqlParmsList);
			 if(row>0) {
				 count++;
			 }
		}
		//写出事务成功数量用户
		try {
			response.getWriter().write(""+count);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void updatePayOut(HttpServletRequest request, HttpServletResponse response) {
		//获取参数，非空判断
		String outName = request.getParameter("outName");
		String outParentType = request.getParameter("outType");
		String outChildType = request.getParameter("outChildType");
		String formMoney = request.getParameter("formMoney");
		String remark = request.getParameter("remark");
		String formAccountId = request.getParameter("formAccountId");
		String pId = request.getParameter("pId");
		String datagridAccountId = request.getParameter("datagridAccountId");
		String datagridMoney = request.getParameter("datagridMoney");
		//参数的非空判断
		if(StringUtil.isEmpty(outName)||StringUtil.isEmpty(outParentType)||StringUtil.isEmpty(outChildType)||StringUtil.isEmpty(formMoney)||
				StringUtil.isEmpty(formAccountId)||StringUtil.isEmpty(pId)||StringUtil.isEmpty(datagridAccountId)||StringUtil.isEmpty(datagridMoney)) {
			try {
				response.getWriter().write("0");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return;
		}
		//查询正确账户是否有足够的金额>需要减去的正确金额，如果是则执行下面代码，不是则返回失败
		Account account = accountServiceImpl.queryByid(Integer.parseInt(formAccountId));
		if(account.getMoney()<Double.parseDouble(formMoney)) {
			//如果小于，则不能修改
			try {
				response.getWriter().write("0");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return ;
		}
		//下面的3个sql语句属于同一个事务
		//创建List<SqlParms>集合
		List<SqlParms> sqlParms = new LinkedList<SqlParms>();
		//原来账户加上减去的钱
		SqlParms srcAccount = new SqlParms();
		String srcAccountSql = "update account set money=money+?,updateTime=now() where id=?";
		//创建参数集合
		List<Object> srcAccountParms = new LinkedList<Object>();
		srcAccountParms.add(datagridMoney);
		srcAccountParms.add(datagridAccountId);
		//把sql语句以及参数集合放入对应的SqlParms对象
		srcAccount.setSql(srcAccountSql);
		srcAccount.setParms(srcAccountParms);
		//正确账户减去正确的金额
		SqlParms distAccount = new SqlParms();
		String distAccountSql = "update account set money=money-?,updateTime=now() where id=?";
		//创建参数集合
		List<Object> distAccountParms = new LinkedList<Object>();
		distAccountParms.add(formMoney);
		distAccountParms.add(formAccountId);
		//把sql语句以及参数集合放入对应的SqlParms对象
		distAccount.setSql(distAccountSql);
		distAccount.setParms(distAccountParms);
		//更新支出记录
		SqlParms updatePayOut = new SqlParms();
		String updatePayOutSql = "update payout set outName=?,outTypeId=?,money=?,accountId=?,remark=?,updateTime=now() where id=?";
		//创建参数集合
		List<Object> updatePayOutParms = new LinkedList<Object>();
		updatePayOutParms.add(outName);
		updatePayOutParms.add(outChildType);
		updatePayOutParms.add(formMoney);
		updatePayOutParms.add(formAccountId);
		updatePayOutParms.add(remark);
		updatePayOutParms.add(pId);
		//将sql与参数集合放入SqlParms对象中
		updatePayOut.setSql(updatePayOutSql);
		updatePayOut.setParms(updatePayOutParms);
		//将三个SqlParms对象放入List<SqlParms>集合
		sqlParms.add(srcAccount);
		sqlParms.add(distAccount);
		sqlParms.add(updatePayOut);
		//执行3个sql语句
		int count = DBUtil.transactionWriter(sqlParms);
		if(count<1) {
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

	private void addPayOut(HttpServletRequest request, HttpServletResponse response) {
		// 接收请求参数
		String outName = request.getParameter("outName");
		String outTypeId = request.getParameter("outChildType");
		String accountId = request.getParameter("accountId");
		String money = request.getParameter("money");
		String remark = request.getParameter("remark");
		//对参数的非空判断
		if(StringUtil.isEmpty(outName)||StringUtil.isEmpty(outTypeId)||StringUtil.isEmpty(accountId)||StringUtil.isEmpty(money)) {
			try {
				response.getWriter().write("0");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return;
		}
		//先对账户表进行一次查询，如果账户所剩金额小于支出金额，则不能进行支出操作
		Account account = accountServiceImpl.queryByid(Integer.parseInt(accountId));
		if(account.getMoney()<Double.parseDouble(money)) {
			//发送请求失败给前台
			try {
				response.getWriter().write("0");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return;
		}
		//添加支出，要在相应的账户上减去金额,一个事务，控制两条sql语句的查询
		
		//创建List<SqlParms>对象集合
		List<SqlParms> sqlParmsList = new LinkedList<>();
		//创建添加支出记录的Sqlparms对象
		SqlParms addPayOut = new SqlParms();
		String addPayOutSql = "INSERT INTO payout (outName,outTypeId,money,accountId,remark,createTime,updateTime) VALUES(?,?,?,?,?,NOW(),NOW());";
		List<Object> addPayOutParms = new LinkedList<>();
		addPayOutParms.add(outName);
		addPayOutParms.add(outTypeId);
		addPayOutParms.add(money);
		addPayOutParms.add(accountId);
		addPayOutParms.add(remark);
		addPayOut.setSql(addPayOutSql);
		addPayOut.setParms(addPayOutParms);
		//创建账户金额的减去sql语句
		SqlParms updateAccount = new SqlParms();
		String updateAccountSql = "UPDATE account SET money=money-?,updateTime=NOW() WHERE id=?";
		List<Object> updateAccountParms = new LinkedList<>();
		updateAccountParms.add(money);
		updateAccountParms.add(accountId);
		updateAccount.setSql(updateAccountSql);
		updateAccount.setParms(updateAccountParms);
		
		//把SqlParms对象加入List<SqlParms>集合
		sqlParmsList.add(addPayOut);
		sqlParmsList.add(updateAccount);
		//执行并获得返回结果
		int count = DBUtil.transactionWriter(sqlParmsList);
		if(count<1) {
			//发送失败给用户
			try {
				response.getWriter().write("0");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else {
			//发送成功给用户
			try {
				response.getWriter().write("1");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void queryPayOutByCondition(HttpServletRequest request, HttpServletResponse response) {
		 //接收参数
		String payOutName = request.getParameter("outName");
	    String payOutId = request.getParameter("typeId");
	    String createTime = request.getParameter("createTime");
	    String page = request.getParameter("page");
	    String rows = request.getParameter("rows");
	    int currentPage = 1;
	    int pageSize = 5;
	    //获取作用域中的user对象
	    User user = (User)request.getSession().getAttribute("user");
	    int uid = user.getId();
	    //创建参数集合
	    List<Object> parms = new LinkedList<>();
	    //初始化sql 
	    StringBuilder sqlCount = new StringBuilder("SELECT p.id,p.outTypeId,o2.id AS parentId,p.accountId,p.outName,CONCAT(o.typeName,'-',o2.typeName) AS typeName,p.money,a.accountName,p.remark,p.createTime,p.updateTime FROM payout p INNER JOIN account a ON p.accountId=a.id INNER JOIN outtype o  ON p.outTypeId=o.id INNER JOIN outtype o2 ON o.pid=o2.id WHERE a.uid=?");
	    StringBuilder sqlList = new StringBuilder("SELECT p.id,p.outTypeId,o2.id AS parentId,p.accountId,p.outName,CONCAT(o.typeName,'-',o2.typeName) AS typeName,p.money,a.accountName,p.remark,p.createTime,p.updateTime FROM payout p INNER JOIN account a ON p.accountId=a.id INNER JOIN outtype o  ON p.outTypeId=o.id INNER JOIN outtype o2 ON o.pid=o2.id WHERE a.uid=?");
	    //将uid进入参数集合
	    parms.add(uid);
	    //参数非空判断
	    if(!StringUtil.isEmpty(payOutName)) {
	    	parms.add("%"+payOutName+"%");
	    	sqlCount.append(" and outName like ?");
	    	sqlList.append(" and outName like ?");
	    }
	    if(!StringUtil.isEmpty(payOutId)) {
	    	parms.add(payOutId);
	    	sqlCount.append(" and id=?");
	    	sqlList.append(" and id=?");
	    }
	    if(!StringUtil.isEmpty(createTime)) {
	    	parms.add(createTime);
	    	sqlCount.append(" and createTime<?");
	    	sqlList.append(" and createTime<?");
	    }
	    //总数查询
	   List<PayOut> payOutCount = DBUtil.query(sqlCount.toString(), PayOut.class, parms);
	   
	   //加入分页查询条件
	   if(!StringUtil.isEmpty(page)) {
		   currentPage = Integer.parseInt(page);
	   }
	   if(!StringUtil.isEmpty(rows)) {
		   pageSize = Integer.parseInt(rows);
	   }
	   int index = (currentPage-1)*pageSize;
	   sqlList.append(" limit ?,?");
	   parms.add(index); 
	   parms.add(pageSize);
	   //执行页面查询记录sql
	  List<PayOut> payOutList = DBUtil.query(sqlList.toString(), PayOut.class, parms);
	  //转换成数据表格所需的形式 
	  HashMap<String, Object> hashMap = new HashMap<String,Object>();
	   hashMap.put("total", payOutCount.size());
	   hashMap.put("rows", payOutList);
	   Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
	   String json = gson.toJson(hashMap);
	   //写出给前台
	   try {
		response.getWriter().write(json);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	   
	}
	
	
}