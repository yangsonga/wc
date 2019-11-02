package com.mage.crm.base;

public interface CrmConstant {
    Integer OPS_SUCCESS_CODE=200;
    String OPS_SUCCESS_MSG="操作成功";

    Integer OPS_FAILED_CODE=300;
    String  OPS_FAILED_MSG="操作失败";

     Integer LOGIN_FAILED_CODE=305;
     String  LOGIN_FAILED_MSG="用户登陆失败";

    Integer LOGIN_NO_CODE=250; //用户未登录状态码
    String  LOGIN_NO_MSG="用户未登陆"; //用户未登录信息
}
