<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>登录</title>
	<!-- 导入公用的css、js文件 -->
	<%@ include file="commons.jsp" %>
</head>
<body style="background-image: url(statics/img/bgimg.jpg); background-repeat: no-repeat; ">
	<div style="margin-left:50%;margin-top: 150px;background-color: #fafafa;margin-right: 20%">
		<div id="p" class="easyui-panel" style="width:400px;height:250px;padding:10px;background:#fafafa;">   
			 <h2 style="margin-left: 40%;">登&nbsp;&nbsp;录</h2>
			 <div style="margin:10px 0;"></div>
			 <div style="padding:20px 0 10px 60px" >
			 	<form id="loginForm" action="/mywc/user" method="post">
			 	  <input type="hidden" name="actionName" value="login"/>
				 	<table>
						<tr>
							<td>用户名称：</td>
							<td>
								<input id="uname" name="uname" type="text" value="admin" />
							</td>
						</tr>
						<tr>
							<td>用户密码：</td>
							<td>
								<input id="upwd" name="upwd" type="password" value="admin"  />
							</td>
						</tr>	
						<tr>
							<td></td>
							<td>
								<span id="errormsg" style="color:red">${msg }</span>
							</td>
						</tr>
						
						<tr>
							<td></td>
							<td>
								<input id="loginBtn" type="button" value="登录" />
								&nbsp;&nbsp;&nbsp;&nbsp;
								<input type="button" value="重置" />
							</td>
						</tr>
					</table>
			 	</form>
			</div>
		</div>   
	</div>  
</body>
<script type="text/javascript" src="statics/js/login.js"></script>
</html>