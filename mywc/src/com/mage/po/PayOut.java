package com.mage.po;

import java.util.Date;

public class PayOut {
  private Integer id;
  private String outName;
  private Integer outTypeId;
  private Double money;
  private Integer accountId;
  private Date createTime;
  private Date updateTime;
  private String remark;
  private String typeName;
  private String accountName;
  private Integer parentId;
  
public Integer getId() {
	return id;
}
public void setId(Integer id) {
	this.id = id;
}
public String getOutName() {
	return outName;
}
public void setOutName(String outName) {
	this.outName = outName;
}
public Integer getOutTypeId() {
	return outTypeId;
}
public void setOutTypeId(Integer outTypeId) {
	this.outTypeId = outTypeId;
}
public Double getMoney() {
	return money;
}
public void setMoney(Double money) {
	this.money = money;
}
public Integer getAccountId() {
	return accountId;
}
public void setAccountId(Integer accountId) {
	this.accountId = accountId;
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
public String getTypeName() {
	return typeName;
}
public void setTypeName(String typeName) {
	this.typeName = typeName;
}
public String getAccountName() {
	return accountName;
}
public void setAccountName(String accountName) {
	this.accountName = accountName;
}
public Integer getParentId() {
	return parentId;
}
public void setParentId(Integer parentId) {
	this.parentId = parentId;
}
  
}
