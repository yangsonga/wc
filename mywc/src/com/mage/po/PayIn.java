package com.mage.po;

import java.util.Date;

public class PayIn {
  private Integer id; //主键
  private String inName;//收入名称
  private Double money; //收入金额
  private String inType; //收入类型
  private Integer accountId; //所属账户id
  private Date createTime; //创建时间
  private Date updateTime; //更新时间
  private String remark; //收入备注
  private String accountName; //所属账户名
  
  
public Integer getId() {
	return id;
}
public void setId(Integer id) {
	this.id = id;
}
public String getInName() {
	return inName;
}
public void setInName(String inName) {
	this.inName = inName;
}
public Double getMoney() {
	return money;
}
public void setMoney(Double money) {
	this.money = money;
}
public String getInType() {
	return inType;
}
public void setInType(String inType) {
	this.inType = inType;
}
public Integer getAcountId() {
	return accountId;
}
public void setAcountId(Integer acountId) {
	this.accountId = acountId;
}
public Date getCreateTime() {
	return createTime;
}
public void setCreateTime(Date createTime) {
	this.createTime = createTime;
}
public Date getUpdateTime() {
	return updateTime;
}
public void setUpdateTime(Date updateTime) {
	this.updateTime = updateTime;
}
public String getRemark() {
	return remark;
}
public void setRemark(String remark) {
	this.remark = remark;
}
public String getAccountName() {
	return accountName;
}
public void setAccountName(String accountName) {
	this.accountName = accountName;
}
  
  
}
