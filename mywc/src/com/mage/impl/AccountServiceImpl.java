package com.mage.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mage.dao.AccountService;
import com.mage.po.Account;
import com.mage.util.DBUtil;

public class AccountServiceImpl implements AccountService{

	@Override
	public List<Account> queryAccount(int uid) {
		List<Account> ls = new ArrayList<Account>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
		conn = DBUtil.getConn();
		String sql = "select * from account where uid=?";
		ps = conn.prepareStatement(sql);
		ps.setInt(1, uid);
		rs = ps.executeQuery();
		while(rs.next()) {
			Account account = new Account();
			account.setId(rs.getInt("id"));
			account.setAccountName(rs.getString("accountName"));
			account.setMoney(rs.getDouble("money"));
			account.setUid(uid);
			account.setUpdateTime(rs.getTimestamp("updateTime"));
			account.setCreateTime(rs.getTimestamp("createTime"));
			account.setRemark(rs.getString("remark"));
			account.setAccountType(rs.getString("accountType"));
			ls.add(account);
		}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			DBUtil.closeAll(conn, rs, ps);
		}
		
		return ls;
	}

	@Override
	public int insertAccount(String accountName, double money, int uid, String remark, String accountType) {
		int count = 0;
		Connection conn = null;
		PreparedStatement ps = null;
		try {
		conn = DBUtil.getConn();
		String sql = "INSERT INTO account (accountName,money,uid,createTime,updateTime,remark,accountType) VALUES(?,?,?,NOW(),NOW(),?,?);";
		ps = conn.prepareStatement(sql);
		ps.setString(1, accountName);
		ps.setDouble(2, money);
		ps.setInt(3, uid);
		ps.setString(4, remark);
		ps.setString(5, accountType);
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
	public int updateAccount(String accountName, double money, int accountid, String remark, String accountType) {
		
		int count = 0;
		Connection conn = null;
		PreparedStatement ps = null;
		try {
		conn = DBUtil.getConn();
		String sql = "UPDATE account SET accountName=?,money=?,updateTime=NOW(),remark=?,accountType=? WHERE id=?";
		ps = conn.prepareStatement(sql);
		ps.setString(1, accountName);
		ps.setDouble(2, money);
		ps.setString(3, remark);
		ps.setString(4, accountType);
		ps.setInt(5, accountid);
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
	public int deleteAccount(String ids) {
		int count = 0;
		Connection conn = null;
		PreparedStatement ps = null;
		try {
		conn = DBUtil.getConn();
		String sql = "DELETE FROM account WHERE id IN ("+ids+");";
		ps = conn.prepareStatement(sql);
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
	public List<Account> queryAccountPages(int uid, int currentPage, int pageSize) {
		List<Account> ls = new ArrayList<Account>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
		conn = DBUtil.getConn();
		String sql = "SELECT * FROM account WHERE uid=? LIMIT ?,?";
		ps = conn.prepareStatement(sql);
		ps.setInt(1, uid);
		ps.setInt(2, (currentPage-1)*pageSize);
		ps.setInt(3, pageSize);
		rs = ps.executeQuery();
		while(rs.next()) {
			Account account = new Account();
			account.setId(rs.getInt("id"));
			account.setAccountName(rs.getString("accountName"));
			account.setMoney(rs.getDouble("money"));
			account.setUid(uid);
			account.setCreateTime(rs.getTimestamp("createTime"));
			account.setUpdateTime(rs.getTimestamp("updateTime"));
			account.setRemark(rs.getString("remark"));
			account.setAccountType(rs.getString("accountType"));
			ls.add(account);
		}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			DBUtil.closeAll(conn, rs, ps);
		}
		return ls;
	}

	@Override
	public Account queryByid(int id) {
		Account account = null;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
		conn = DBUtil.getConn();
		String sql = "select * from account where id=?";
		ps = conn.prepareStatement(sql);
		ps.setInt(1, id);
		rs = ps.executeQuery();
		while(rs.next()) {
			account = new Account();
			account.setId(rs.getInt("id"));
			account.setAccountName(rs.getString("accountName"));
			account.setAccountType(rs.getString("accountType"));
			account.setMoney(rs.getDouble("money"));
			account.setRemark(rs.getString("remark"));
			account.setUid(rs.getInt("uid"));
			account.setCreateTime(rs.getTimestamp("createTime"));
			account.setUpdateTime(rs.getTimestamp("updateTime"));
		}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			DBUtil.closeAll(conn, rs, ps);
		}
		
		return account;
	}

}
