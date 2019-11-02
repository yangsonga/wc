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
		//��ȡ�������ж�ִ���Ǹ�����
		String actionName = request.getParameter("actionName");
		if("queryPayinByCondition".equals(actionName)) {
			//��ҳ������ѯ
			queryPayinByCondition(request,response);
			return;
		}else if("addPayIn".equals(actionName)) {
			//�������
			addPayIn(request,response);
			return;
		}else if("updatePayIn".equals(actionName)) {
			//�޸�����
			updatePayIn(request,response);
			return;
		}else if("deletePayIn".equals(actionName)) {
			//ɾ������
			deletePayIn(request,response);
			return;
		}
	}
	private void deletePayIn(HttpServletRequest request, HttpServletResponse response) {
		//�����������
		String ids = request.getParameter("ids");
		//�����ǿ��ж�
		if(StringUtil.isEmpty(ids)) {
			try {
				response.getWriter().write("0");
				return;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//�ָ��ַ������õ�ÿһ��id
		String[] ss = ids.split(",");
		//����PayIn���󼯺�
		List<PayIn> ls = new LinkedList<PayIn>();
		for(int i=0;i<ss.length;i++) {
			PayIn p = payInServiceImpl.queryById(Integer.parseInt(ss[i]));
			//���������
			ls.add(p);
		}
		//��������
		//���ڼ����е�sql���û�仯��ֻ�ǰ󶨱��������ı䣬���԰�sql�������զ����
		String sqlAccount = "update account set money = money-?,updateTime=now() where id=?";
		String sqlPayIn = "delete from payin where id=?";
		int row = -1;
		//ÿ��PayIn�����Ӧ����sql����ִ�У������е�sqlִ��JDBC��������һ������
		List<SqlParms> spList = new LinkedList<SqlParms>();
		for(PayIn p :ls) {
			//�ȸ�account���ȥ��Ӧ�Ľ��,ͨ��PayIn�����accountId
			//��payin���������ɾ��,ͨ��PayIn�����id
			List<Object> accountList = new LinkedList<Object>();
			accountList.add(p.getMoney());
			accountList.add(p.getAcountId());
			List<Object> payInList = new LinkedList<Object>();
			payInList.add(p.getId());
			//��ÿ��PayIn�����Ӧ������SqlParms�������List<SqlParms>����
			spList.add(new SqlParms(sqlAccount, accountList));
			spList.add(new SqlParms(sqlPayIn, payInList));
		}
		//ִ��sql,���е�sql����ͬһ������
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
		//�����������
		String inName = request.getParameter("inName");
		String inType = request.getParameter("inType");
		String formMoney = request.getParameter("formMoney");
		String remark = request.getParameter("remark");
		String formAccountId = request.getParameter("formAccountId");
		String pId = request.getParameter("pId");
		String datagridAccountId = request.getParameter("datagridAccountId");
		String datagridMoney = request.getParameter("datagridMoney");
		//�жϲ���
		if(StringUtil.isEmpty(inName)||StringUtil.isEmpty(inType)||StringUtil.isEmpty(formMoney)||StringUtil.isEmpty(formAccountId)||StringUtil.isEmpty(pId)||StringUtil.isEmpty(datagridAccountId)||StringUtil.isEmpty(datagridMoney)) {
			//ֻҪ��һ��Ϊ�գ�����ʧ�ܸ��û�
			try {
				response.getWriter().write("0");
				return;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//��ʼ��sql
		//111111111��ԭ�����˻��޸ģ�����ȥ���ݱ���еĽ��
		String sqlOld = "update account set money=money-?,updateTime=now() where id = ?";
		//�����������ϣ�����Ҫ�Ĳ������뼯����
		List<Object> oldParms = new ArrayList<Object>();
		oldParms.add(datagridMoney);
		oldParms.add(datagridAccountId);
		//ʹ��sqlParms����洢sql����Լ���Ӧ����
		SqlParms oldSqlParms = new SqlParms(sqlOld, oldParms);
		
		//22222222����ȷ���˻�������ȷ�Ľ��
		String sqlNew = "update account set money=money+?,updateTime=now() where id = ?";
		//�����������ϣ�����Ҫ�Ĳ������뼯����
		List<Object> newParms = new ArrayList<Object>();
		newParms.add(formMoney);
		newParms.add(formAccountId);
		//ʹ��sqlParms����洢sql����Լ���Ӧ����
		SqlParms newSqlParms = new SqlParms(sqlNew, newParms);
		
		//33333333���޸�payin���Ѷ�Ӧ�������¼������
		String sqlPayIn = "update payin set inName=?,money=?,inType=?,accountId=?,remark=?,updateTime=now() where id=?";
		//�����������ϣ�����Ҫ�Ĳ������뼯����
		List<Object> payInParms = new ArrayList<Object>();
		payInParms.add(inName);
		payInParms.add(formMoney);
		payInParms.add(inType);
		payInParms.add(formAccountId);
		payInParms.add(remark);
		payInParms.add(pId);
		//ʹ��sqlParms����洢sql����Լ���Ӧ����
		SqlParms payInSqlParms = new SqlParms(sqlPayIn, payInParms);
		
		//��sqlParms������뼯����
		List<SqlParms> ls = new ArrayList<>();
		ls.add(oldSqlParms);
		ls.add(newSqlParms);
		ls.add(payInSqlParms);
		//���÷�������÷���ֵ
		 int row = payInServiceImpl.update(ls);
		//�жϷ���ֵ������������͸�ǰ̨
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
		//��ȡ����
		String payInName = request.getParameter("inName");
		String payinType = request.getParameter("inType");
		String accountId = request.getParameter("accountId");
		String money = request.getParameter("money");
		String remark = request.getParameter("remark");
		
		System.out.println(accountId);
		//�ǿ��ж�
		if(StringUtil.isEmpty(payInName)||StringUtil.isEmpty(payinType)||StringUtil.isEmpty(accountId)||StringUtil.isEmpty(money)||StringUtil.isEmpty(remark)) {
			//��ǰ̨����ʧ����Ӧ
			try {
				response.getWriter().write("0");
				return;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//������תΪ��Ӧ����ʽ
		double payInMoney = Double.parseDouble(money);
		//������Ϊ��ʱ
		//���������뷽���У���ȡ���ؽ��(��Ӱ�������)
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
			//account��ҲҪ����Ӧ���޸�
			StringBuilder sqlAccount = new StringBuilder("UPDATE account SET money=money+?, updateTime=now() WHERE id=?");
			//�Ѳ����������������
			List<Object> parms = new ArrayList<Object>();
			parms.add(money);
			parms.add(aid);
			//ʹ��DBUtil�ľ�̬����
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
	//��ҳ������ѯ
	private void queryPayinByCondition(HttpServletRequest request, HttpServletResponse response) {
		//��ȡǰ̨�������
		String inName = request.getParameter("inName");
		String inType = request.getParameter("inType");
		String createTime = request.getParameter("createTime");
		String page = request.getParameter("page");
		String rows = request.getParameter("rows");
		//���ó�ʼ��ҳ���Լ�ÿҳ��¼��
		int currentPage = 1;
		int pageSize = 5;
		//��ȡ�������е�uid
		  User user = (User)request.getSession().getAttribute("user");
		  Integer uid = user.getId();
		//��ʼ��sql��䣬��������ѯ�������Լ�ÿҳ��¼
		StringBuilder sqlCount = new StringBuilder("SELECT p.id,inName,p.money,inType,accountId,p.createTime,p.updateTime,p.remark,accountName FROM payin p INNER JOIN account a ON p.accountId = a.id AND a.uid ="+uid);
		StringBuilder sqlList = new StringBuilder("SELECT p.id,inName,p.money,inType,accountId,p.createTime,p.updateTime,p.remark,accountName FROM payin p INNER JOIN account a ON p.accountId = a.id AND a.uid ="+uid);
		//������������
		List<Object> parms = new ArrayList<Object>();
		//�жϲ�����������ڼ���������ϣ����򲻼�
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
		//��ҳ��ѯsql
		sqlList.append(" LIMIT ?,?");
		
		if(!StringUtil.isEmpty(page)) {
			currentPage = Integer.parseInt(page);
		}
		if(!StringUtil.isEmpty(rows)) {
			pageSize = Integer.parseInt(rows);
		}
		//ִ��sql�õ�����ֵ
		List<PayIn> ls = DBUtil.query(sqlCount.toString(), PayIn.class, parms);
		//�ѵ�ǰҳ���Լ�ÿҳ��¼�������������
		int index = (currentPage-1)*pageSize;
		parms.add(index);
		parms.add(pageSize);
		List<PayIn> payInList = DBUtil.query(sqlList.toString(), PayIn.class, parms);
		//������ת����easy-ui�涨����ʽ
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
