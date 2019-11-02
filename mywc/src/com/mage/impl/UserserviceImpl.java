package com.mage.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mage.dao.UserService;
import com.mage.po.User;
import com.mage.util.DBUtil;

public class UserserviceImpl implements UserService{

	@Override
	public User queryUserByname(String name) {
		User user = null;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
		conn = DBUtil.getConn();
		String sql = "select * from user where name=?";
		ps = conn.prepareStatement(sql);
		ps.setString(1, name);
		rs = ps.executeQuery();
		while(rs.next()) {
			user = new User();
			user.setId(rs.getInt("id"));
			user.setName(name);
			user.setPwd(rs.getString("pwd"));
		}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			DBUtil.closeAll(conn, rs, ps);
		}
		return user;
	}

}
