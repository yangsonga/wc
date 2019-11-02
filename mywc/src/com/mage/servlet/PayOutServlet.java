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
		//�жϲ������ַ�����
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
	   //�����������
		String ids = request.getParameter("ids");
		//�ж��������
		if(StringUtil.isEmpty(ids)) {
			//���idsΪ�գ�����ʧ����Ϣ��ǰ̨
			try {
				response.getWriter().write("0");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return;
		}
	  //�ָ��ַ���
		String[] idstr = ids.split(",");
		//�������飬�õ�List<PayOut>����
		List<PayOut> payOutList = new LinkedList<PayOut>();
		for(String id :idstr) {
			PayOut payOut = payoutServiceImpl.queryById(Integer.parseInt(id));
			payOutList.add(payOut);
		}
		
		String accountUpdateSql = "update account set money=money+?,updateTime=now() where id=?";
		String payOutDeleteSql = "delete from payout where id=?";
		
		//����ɾ����������ǰ̨
		int count = 0; //������������ɹ�������
		int row = -1; //���ÿ������ķ��ؽ��
		//ͨ��List<PayOut> ����ѭ��ִ��sql,ÿ����sqlͬһ������
		for(PayOut p:payOutList) {
			//����SqlParms����
			List<SqlParms> sqlParmsList = new LinkedList<SqlParms>();
			//��account������϶�Ӧ�Ľ��
			SqlParms accountUpdate = new SqlParms();
			//������������
			List<Object> accountUpdateParms = new LinkedList<Object>();
			accountUpdateParms.add(p.getMoney());
			accountUpdateParms.add(p.getAccountId());
			//��sql����Լ��������Ϸ���SqlParms����
			accountUpdate.setSql(accountUpdateSql);
			accountUpdate.setParms(accountUpdateParms);
			//��payout����ɾ����¼
			SqlParms payOutDelete = new SqlParms();
			//������������
			List<Object> payOutDeleteParms = new LinkedList<Object>();
			payOutDeleteParms.add(p.getId());
			//��sql����Լ��������Ϸ���SqlParms����
			payOutDelete.setSql(payOutDeleteSql);
			payOutDelete.setParms(payOutDeleteParms);
			//��SqlParms������뼯����
			sqlParmsList.add(accountUpdate);
			sqlParmsList.add(payOutDelete);
			//������ƣ�ִ��sql
			 row = DBUtil.transactionWriter(sqlParmsList);
			 if(row>0) {
				 count++;
			 }
		}
		//д������ɹ������û�
		try {
			response.getWriter().write(""+count);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void updatePayOut(HttpServletRequest request, HttpServletResponse response) {
		//��ȡ�������ǿ��ж�
		String outName = request.getParameter("outName");
		String outParentType = request.getParameter("outType");
		String outChildType = request.getParameter("outChildType");
		String formMoney = request.getParameter("formMoney");
		String remark = request.getParameter("remark");
		String formAccountId = request.getParameter("formAccountId");
		String pId = request.getParameter("pId");
		String datagridAccountId = request.getParameter("datagridAccountId");
		String datagridMoney = request.getParameter("datagridMoney");
		//�����ķǿ��ж�
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
		//��ѯ��ȷ�˻��Ƿ����㹻�Ľ��>��Ҫ��ȥ����ȷ���������ִ��������룬�����򷵻�ʧ��
		Account account = accountServiceImpl.queryByid(Integer.parseInt(formAccountId));
		if(account.getMoney()<Double.parseDouble(formMoney)) {
			//���С�ڣ������޸�
			try {
				response.getWriter().write("0");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return ;
		}
		//�����3��sql�������ͬһ������
		//����List<SqlParms>����
		List<SqlParms> sqlParms = new LinkedList<SqlParms>();
		//ԭ���˻����ϼ�ȥ��Ǯ
		SqlParms srcAccount = new SqlParms();
		String srcAccountSql = "update account set money=money+?,updateTime=now() where id=?";
		//������������
		List<Object> srcAccountParms = new LinkedList<Object>();
		srcAccountParms.add(datagridMoney);
		srcAccountParms.add(datagridAccountId);
		//��sql����Լ��������Ϸ����Ӧ��SqlParms����
		srcAccount.setSql(srcAccountSql);
		srcAccount.setParms(srcAccountParms);
		//��ȷ�˻���ȥ��ȷ�Ľ��
		SqlParms distAccount = new SqlParms();
		String distAccountSql = "update account set money=money-?,updateTime=now() where id=?";
		//������������
		List<Object> distAccountParms = new LinkedList<Object>();
		distAccountParms.add(formMoney);
		distAccountParms.add(formAccountId);
		//��sql����Լ��������Ϸ����Ӧ��SqlParms����
		distAccount.setSql(distAccountSql);
		distAccount.setParms(distAccountParms);
		//����֧����¼
		SqlParms updatePayOut = new SqlParms();
		String updatePayOutSql = "update payout set outName=?,outTypeId=?,money=?,accountId=?,remark=?,updateTime=now() where id=?";
		//������������
		List<Object> updatePayOutParms = new LinkedList<Object>();
		updatePayOutParms.add(outName);
		updatePayOutParms.add(outChildType);
		updatePayOutParms.add(formMoney);
		updatePayOutParms.add(formAccountId);
		updatePayOutParms.add(remark);
		updatePayOutParms.add(pId);
		//��sql��������Ϸ���SqlParms������
		updatePayOut.setSql(updatePayOutSql);
		updatePayOut.setParms(updatePayOutParms);
		//������SqlParms�������List<SqlParms>����
		sqlParms.add(srcAccount);
		sqlParms.add(distAccount);
		sqlParms.add(updatePayOut);
		//ִ��3��sql���
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
		// �����������
		String outName = request.getParameter("outName");
		String outTypeId = request.getParameter("outChildType");
		String accountId = request.getParameter("accountId");
		String money = request.getParameter("money");
		String remark = request.getParameter("remark");
		//�Բ����ķǿ��ж�
		if(StringUtil.isEmpty(outName)||StringUtil.isEmpty(outTypeId)||StringUtil.isEmpty(accountId)||StringUtil.isEmpty(money)) {
			try {
				response.getWriter().write("0");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return;
		}
		//�ȶ��˻������һ�β�ѯ������˻���ʣ���С��֧�������ܽ���֧������
		Account account = accountServiceImpl.queryByid(Integer.parseInt(accountId));
		if(account.getMoney()<Double.parseDouble(money)) {
			//��������ʧ�ܸ�ǰ̨
			try {
				response.getWriter().write("0");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return;
		}
		//���֧����Ҫ����Ӧ���˻��ϼ�ȥ���,һ�����񣬿�������sql���Ĳ�ѯ
		
		//����List<SqlParms>���󼯺�
		List<SqlParms> sqlParmsList = new LinkedList<>();
		//�������֧����¼��Sqlparms����
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
		//�����˻����ļ�ȥsql���
		SqlParms updateAccount = new SqlParms();
		String updateAccountSql = "UPDATE account SET money=money-?,updateTime=NOW() WHERE id=?";
		List<Object> updateAccountParms = new LinkedList<>();
		updateAccountParms.add(money);
		updateAccountParms.add(accountId);
		updateAccount.setSql(updateAccountSql);
		updateAccount.setParms(updateAccountParms);
		
		//��SqlParms�������List<SqlParms>����
		sqlParmsList.add(addPayOut);
		sqlParmsList.add(updateAccount);
		//ִ�в���÷��ؽ��
		int count = DBUtil.transactionWriter(sqlParmsList);
		if(count<1) {
			//����ʧ�ܸ��û�
			try {
				response.getWriter().write("0");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else {
			//���ͳɹ����û�
			try {
				response.getWriter().write("1");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void queryPayOutByCondition(HttpServletRequest request, HttpServletResponse response) {
		 //���ղ���
		String payOutName = request.getParameter("outName");
	    String payOutId = request.getParameter("typeId");
	    String createTime = request.getParameter("createTime");
	    String page = request.getParameter("page");
	    String rows = request.getParameter("rows");
	    int currentPage = 1;
	    int pageSize = 5;
	    //��ȡ�������е�user����
	    User user = (User)request.getSession().getAttribute("user");
	    int uid = user.getId();
	    //������������
	    List<Object> parms = new LinkedList<>();
	    //��ʼ��sql 
	    StringBuilder sqlCount = new StringBuilder("SELECT p.id,p.outTypeId,o2.id AS parentId,p.accountId,p.outName,CONCAT(o.typeName,'-',o2.typeName) AS typeName,p.money,a.accountName,p.remark,p.createTime,p.updateTime FROM payout p INNER JOIN account a ON p.accountId=a.id INNER JOIN outtype o  ON p.outTypeId=o.id INNER JOIN outtype o2 ON o.pid=o2.id WHERE a.uid=?");
	    StringBuilder sqlList = new StringBuilder("SELECT p.id,p.outTypeId,o2.id AS parentId,p.accountId,p.outName,CONCAT(o.typeName,'-',o2.typeName) AS typeName,p.money,a.accountName,p.remark,p.createTime,p.updateTime FROM payout p INNER JOIN account a ON p.accountId=a.id INNER JOIN outtype o  ON p.outTypeId=o.id INNER JOIN outtype o2 ON o.pid=o2.id WHERE a.uid=?");
	    //��uid�����������
	    parms.add(uid);
	    //�����ǿ��ж�
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
	    //������ѯ
	   List<PayOut> payOutCount = DBUtil.query(sqlCount.toString(), PayOut.class, parms);
	   
	   //�����ҳ��ѯ����
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
	   //ִ��ҳ���ѯ��¼sql
	  List<PayOut> payOutList = DBUtil.query(sqlList.toString(), PayOut.class, parms);
	  //ת�������ݱ���������ʽ 
	  HashMap<String, Object> hashMap = new HashMap<String,Object>();
	   hashMap.put("total", payOutCount.size());
	   hashMap.put("rows", payOutList);
	   Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
	   String json = gson.toJson(hashMap);
	   //д����ǰ̨
	   try {
		response.getWriter().write(json);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	   
	}
	
	
}