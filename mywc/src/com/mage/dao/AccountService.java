package com.mage.dao;

import java.util.List;

import com.mage.po.Account;

public interface AccountService {
   List<Account> queryAccount(int uid);
   int insertAccount(String accountName,double money,int uid,String remark,String accountType);
   int updateAccount(String accountName,double money,int accountid,String remark,String accountType);
   int deleteAccount(String ids);
   List<Account> queryAccountPages(int uid,int currentPage,int pageSize);
   Account queryByid(int id);
}
