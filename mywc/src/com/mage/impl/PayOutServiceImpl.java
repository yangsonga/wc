package com.mage.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.mage.dao.PayOutService;
import com.mage.po.PayOut;
import com.mage.po.SqlParms;
import com.mage.util.DBUtil;

public class PayOutServiceImpl implements PayOutService{

	@Override
	public int insert(String outName, int outTypeChildId, double money, int accountId, String remark) {
	    int count = -1;
	    Connection conn = null;
	    PreparedStatement ps = null;
	    try {
	    conn = DBUtil.getConn();
	    String sql = "INSERT INTO payout (outName,outTypeId,money,accountId,remark,createTime,updateTime) VALUES(?,?,?,?,?,NOW(),NOW());";
		ps = conn.prepareStatement(sql);
		ps.setString(1, outName);
		ps.setInt(2, outTypeChildId);
		ps.setDouble(3, money);
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

	@Override
	public int writer(List<SqlParms> sqlParmsList) {
		int count = -1;
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = DBUtil.getConn();
			conn.setAutoCommit(false);
			for (SqlParms sqlParms : sqlParmsList) {
				String sql = sqlParms.getSql();
				ps = conn.prepareStatement(sql);
				// 设置绑定变量
				for (int i = 0; i < sqlParms.getParms().size(); i++) {
					ps.setObject(i + 1, sqlParms.getParms().get(i));
				}
				count = ps.executeUpdate();
				if(count<0) {
					conn.rollback();
					break;
				}
			}
		} catch (SQLException e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
		}finally {
			DBUtil.closeAll(conn, null, ps);
		}
		return count;
		}

	@Override
	public PayOut queryById(int id) {
		PayOut payOut = null;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
		conn = DBUtil.getConn();
		String sql = "select * from payout where id=?";
		ps = conn.prepareStatement(sql);
		ps.setInt(1, id);
		rs = ps.executeQuery();
		while(rs.next()) {
			payOut = new PayOut();
			payOut.setId(id);
			payOut.setOutName(rs.getString("outName"));
			payOut.setOutTypeId(rs.getInt("outTypeId"));
			payOut.setMoney(rs.getDouble("money"));
			payOut.setAccountId(rs.getInt("accountId"));
			payOut.setCreateTime(rs.getTimestamp("createTime"));
			payOut.setUpdateTime(rs.getTimestamp("updateTime"));
			payOut.setRemark(rs.getString("remark"));
		}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			DBUtil.closeAll(conn, rs, ps);
		}
		return payOut;
	}
	}

