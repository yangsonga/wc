package com.mage.crm.model;

import com.mage.crm.base.CrmConstant;

//返回的消息模型
public class MessageModel {
    private int code=CrmConstant.OPS_SUCCESS_CODE;//返回的状态码,默认为200
    private String msg=CrmConstant.OPS_SUCCESS_MSG;//返回的提示信息，默认为操作成功
    private Object result;//返回的结果对象

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
