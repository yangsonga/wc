package com.mage.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import com.mage.dao.OutTypeService;
import com.mage.po.OutType;
import com.mage.util.DBUtil;

public class OutTypeServiceImpl implements OutTypeService{

	@Override
	public List<OutType> queryParentType() {
		List<OutType> ls = new LinkedList<OutType>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
		conn = DBUtil.getConn();
		String sql = "select * from outtype where pid=0";
		ps = conn.prepareStatement(sql);
		rs = ps.executeQuery();
		while(rs.next()) {
			OutType outType = new OutType();
			outType.setId(rs.getInt("id"));
			outType.setTypeName(rs.getString("typeName"));
			outType.setPid(rs.getInt("pid"));
			ls.add(outType);
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
	public List<OutType> queryChildType(int pid) {
		List<OutType> ls = new LinkedList<OutType>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
		conn = DBUtil.getConn();
		String sql = "SELECT o.id,o.pid,o.typeName FROM outtype o INNER JOIN outtype ot ON o.pid=ot.id WHERE ot.id=?";
		ps = conn.prepareStatement(sql);
		ps.setInt(1, pid);
		rs = ps.executeQuery();
		while(rs.next()) {
			OutType outType = new OutType();
			outType.setId(rs.getInt("id"));
			outType.setTypeName(rs.getString("typeName"));
			outType.setPid(rs.getInt("pid"));
			ls.add(outType);
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
	public OutType querySpecificParent(int id) {
		OutType outType = null;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
		conn = DBUtil.getConn();
		String sql = "select * from outtype where id=?";
		ps = conn.prepareStatement(sql);
		ps.setInt(1, id);
		rs = ps.executeQuery();
		while(rs.next()) {
			outType = new OutType();
			outType.setId(id);
			outType.setTypeName(rs.getString("typeName"));
			outType.setPid(rs.getInt("pid"));
		}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			DBUtil.closeAll(conn, rs, ps);
		}
		return outType;
	}

}
