package com.mage.util;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.mage.po.SqlParms;

public class DBUtil {
  static {
	  try {
		Class.forName("com.mysql.jdbc.Driver");
	} catch (ClassNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
  }
  
  public static Connection getConn() {
	  Connection conn = null;
	  try {
		conn=DriverManager.getConnection("jdbc:mysql://localhost:3306/wc?useUnicode=true&characterEncoding=utf-8", "root", "");
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	  return conn;
  }
  
  private static void closeConn(Connection conn) {
	  if(conn!=null) {
		  try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  
	  }
  }
  
  private static void closeResultSet(ResultSet rs) {
	  if(rs!=null) {
		  try {
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	  }
  }
  
  private static void closeStatement(Statement st) {
	  if(st!=null) {
		 try {
			st.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	  }
  }
  
  public static void closeAll(Connection conn,ResultSet rs,Statement st) {
	  closeResultSet(rs);
	  closeStatement(st);
	  closeConn(conn);
  }
  
  /**
   * 
   * @param <T> 泛型
   * @param sql 传入的sql语句
   * @param clz Class对象
   * @param ls  参数集合
   * @return
   */
  public static <T> List<T> query(String sql, Class<T> clz,List<Object> parpms) {
		//通过sql中是否包含select判断读写操作
		if(StringUtil.isEmpty(sql)) {
			return null;
		}
		List<T> ls = new ArrayList<T>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
		conn = getConn();
		ps = conn.prepareStatement(sql);
		//设置绑定变量
		for(int i=0;i<parpms.size();i++) {
			ps.setObject(i+1, parpms.get(i));
		}
		rs = ps.executeQuery();
		while(rs.next()) {
			T t = null;
			try {
				t = clz.newInstance();
				Field[] fs = clz.getDeclaredFields();
				for(int i=0;i<fs.length;i++) {
					fs[i].setAccessible(true);
					fs[i].set(t, rs.getObject(fs[i].getName()));
					fs[i].setAccessible(false);
				}
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ls.add(t);
		}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			closeAll(conn, rs, ps);
		}
		return ls;
		
	}
  /**
   * 
   * @param sql 传入的sql语句
   * @param parms 参数集合
   * @return
   */
  public static int writer(String sql,List<Object> parms) {
	  int count = -1;
	  Connection conn = null;
	  PreparedStatement ps = null;
	  try {
	  conn = getConn();
	  ps = conn.prepareStatement(sql);
	  //设置绑定变量
	  for(int i=0;i<parms.size();i++) {
		  ps.setObject(i+1, parms.get(i));
	  }
	  //执行sql，获取受影响的行数
	 count = ps.executeUpdate();
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}finally {
		closeAll(conn,null,ps);
	}
	return count;
	  
  }
  
  public static int transactionWriter(List<SqlParms> sqlParmsList) {
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
				e1.printStackTrace();
			}
			e.printStackTrace();
		}finally {
			try {
				conn.commit();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			DBUtil.closeAll(conn, null, ps);
		}
		return count;
		}
}
