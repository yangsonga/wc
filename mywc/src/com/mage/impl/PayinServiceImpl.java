package com.mage.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.mage.dao.PayinService;
import com.mage.po.PayIn;
import com.mage.po.SqlParms;
import com.mage.util.DBUtil;

public class PayinServiceImpl implements PayinService{

	@Override
	public int insert(String inName, double money, String inType, int accountId, String remark) {
		int count = -1;
		Connection conn = null;
		PreparedStatement ps = null;
		try {
		conn = DBUtil.getConn();
		String sql = "INSERT INTO payin (inName,money,inType,accountId,remark,createTime,updateTime) VALUES(?,?,?,?,?,now(),now());";
		ps = conn.prepareStatement(sql);
		ps.setString(1, inName);
		ps.setDouble(2, money);
		ps.setString(3, inType);
		ps.setInt(4, accountId);
		ps.setString(5, remark);
		count = ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			DBUtil.closeAll(conn, null, ps);
		}
		return count;
	}

	//���ƶ���д�������������
	@Override
	public int update(List<SqlParms> ls) {
		//������Ӱ�������
		int row = -1;
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = DBUtil.getConn();
			conn.setAutoCommit(false);
			//�Լ��Ͻ��б���
			for(SqlParms sqlParms:ls) {
				// �õ�Ԥ�������
				ps = conn.prepareStatement(sqlParms.getSql());
				// ���ð󶨱���
				for (int i = 0; i < sqlParms.getParms().size(); i++) {
					ps.setObject(i + 1, sqlParms.getParms().get(i));
				}
				// ִ��sql���
				row = ps.executeUpdate();
				// �ж�row�Ƿ����0���������ִ����һ��sql,���������ִֹͣ�У����ع�
				if (row < 1) {
					// �ع�
					conn.rollback();
					break;
				}
			}
		} catch (SQLException e1) {
			try {
				//���������쳣������û��ִ�е�if�����ж���Ļع�ʱ����Ҫ���쳣��������лع�
				conn.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			e1.printStackTrace();
		}finally {
			try {
				conn.commit();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			DBUtil.closeAll(conn, null, ps);
		}
		return row;
	}

	@Override
	public PayIn queryById(int id) {
	   PayIn p = null;
	   Connection conn = null;
	   PreparedStatement ps = null;
	   ResultSet rs = null;
	   try {
	   conn = DBUtil.getConn();
	   String sql = "SELECT p.id,inName,p.money,inType,accountId,p.createTime,p.updateTime,p.remark,accountName FROM "
	   		+ "payin p INNER JOIN account a ON p.accountId=a.id AND p.id=?";
	   ps = conn.prepareStatement(sql);
	   ps.setInt(1, id);
	   rs = ps.executeQuery();
	   while(rs.next()) {
		   p = new PayIn();
		   p.setId(rs.getInt("id"));
		   p.setInName(rs.getString("inName"));
		   p.setMoney(rs.getDouble("money"));
		   p.setInType(rs.getString("inType"));
		   p.setAcountId(rs.getInt("accountId"));
		   p.setCreateTime(rs.getTimestamp("createTime"));
		   p.setUpdateTime(rs.getDate("updateTime"));
		   p.setAccountName(rs.getString("accountName"));
		   p.setRemark(rs.getString("remark"));
	   }
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}finally {
		DBUtil.closeAll(conn, rs, ps);
	}
		return p;
	}


}
