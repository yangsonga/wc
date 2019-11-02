<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>旺财后台管理系统首页</title>
<%@ include file="commons.jsp" %>
</head>
<body class="easyui-layout">   
    <div data-options="region:'north' " style="height:100px;background-image: url('statics/img/dog.jpg');background-repeat: no-repeat;">
        <div style="margin-left: 90px;margin-top: 75px;font-size: 15px">
          <a href="login.jsp" style="text-decoration: none;">旺财管理系统</a>
          <span style="float: right;font-size: 20px;margin-right: 20px">
                                当前登录用户：<b>${user.name }</b>
              &nbsp;&nbsp;&nbsp;&nbsp;
            <a href="/mywc/user?actionName=logout">注销</a>
          </span>
        </div>
    </div>   
    <div data-options="region:'south'" style="height:50px;text-align: center;color: blue;">
       <br>
		Copyright © 2006-2026 旺财 All Rights Reserved 电话：0086-88888888
		QQ：123456 <a href="#" class="go-top"> <i class="icon-bon-arrow-up"></i></a>
    </div>   
       
   <div data-options="region:'west',title:'West',split:true" style="width:170px;">
    <div id="aa" class="easyui-accordion" fit=true>   
    <div title="用户管理" data-options="iconCls:'icon-user'" style="padding-left: 20px;padding-top: 10px;">   
        <a id="btn" href="javascript:openTabs('账户管理' ,'icon-jwjl','account.jsp')" class="easyui-linkbutton" data-options="iconCls:'icon-jwjl',plain:true">账户管理</a> 
 		<a id="btn" href="javascript:openTabs('收入管理','icon-yxjhgl','payIn.jsp')" class="easyui-linkbutton" data-options="iconCls:'icon-yxjhgl',plain:true">收入管理</a>
 		<a id="btn" href="javascript:openTabs('支出管理','icon-yxjhgl','payOut.jsp')" class="easyui-linkbutton" data-options="iconCls:'icon-yxjhgl',plain:true">支出管理</a>
 		<a id="btn" href="javascript:openTabs('报表管理','icon-tjbb','echarts.jsp')" class="easyui-linkbutton" data-options="iconCls:'icon-tjbb',plain:true">报表管理</a> 
    </div>   
     
    <div title="系统管理" data-options="iconCls:'icon-item'" style="padding-left: 20px;padding-top: 10px;">   
      <a id="btn" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-item',plain:true">系统设置</a>
    </div>   
</div>  
    
    </div>   
    <div data-options="region:'center',title:'center title'" style="padding:5px;background:#eee;">
       <div id="tabs" class="easyui-tabs" data-options="fit:true">   
    <div title="主页" iconCls="icon-home" style="padding:20px;text-align: center;">   
        <h1>欢迎来到旺财管理系统 </h1>
    </div>   
   </div>    
  </div>   
</body> 
<script type="text/javascript" src="statics/js/index.js"></script> 
</html>