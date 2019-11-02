package com.mage.dao;

import java.util.List;

import com.mage.po.PayIn;
import com.mage.po.SqlParms;

public interface PayinService {
     int insert(String inName,double money,String inType,int accountId,String remark);
     
     //该方法进行一个事务操作，里面执行多条sql语句，手动提交事务，返回受影响的行数
      int update(List<SqlParms> ls);
      PayIn queryById(int id);
}
