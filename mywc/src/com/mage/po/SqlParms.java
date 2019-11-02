package com.mage.po;

import java.util.List;

/**
 * 该类是用来存储sql语句，以及sql对应的参数类
 * @author 87386
 *
 */
public class SqlParms {
  private String sql;
  private List<Object> parms;
  public SqlParms() {
	
}
  
public SqlParms(String sql, List<Object> parms) {
	this.sql = sql;
	this.parms = parms;
}

public String getSql() {
	return sql;
}
public void setSql(String sql) {
	this.sql = sql;
}
public List<Object> getParms() {
	return parms;
}
public void setParms(List<Object> parms) {
	this.parms = parms;
}
  
}
