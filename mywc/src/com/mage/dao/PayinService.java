package com.mage.dao;

import java.util.List;

import com.mage.po.PayIn;
import com.mage.po.SqlParms;

public interface PayinService {
     int insert(String inName,double money,String inType,int accountId,String remark);
     
     //�÷�������һ���������������ִ�ж���sql��䣬�ֶ��ύ���񣬷�����Ӱ�������
      int update(List<SqlParms> ls);
      PayIn queryById(int id);
}
