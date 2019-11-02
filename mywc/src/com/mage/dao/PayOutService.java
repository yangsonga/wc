package com.mage.dao;

import java.util.List;

import com.mage.po.PayOut;
import com.mage.po.SqlParms;

public interface PayOutService {
  int insert(String outName,int outTypeChildId,double money,int accountId,String remark);
  int writer(List<SqlParms> sqlParmsList);
  PayOut queryById(int id);
}
