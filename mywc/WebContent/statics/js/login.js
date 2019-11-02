/**
 * 解决iframe  session失效后直接在iframe内联框架中显示页面问题
 */
if(top!=self){
	if(top.location!=self.location){
		top.location = self.location
	}6
}

// 给登录按钮绑定点击事件
$("#loginBtn").click(function(){
	// 判断用户名和密码是否为空
	// 获取参数
	var uname = $("#uname").val();
	var upwd = $("#upwd").val();
	if(isEmpty(uname)){
		// 提示用户
		$("#errormsg").html("* 用户名不能为空！");
		return;
	}
	if(isEmpty(upwd)){
		// 提示用户
		$("#errormsg").html("* 密码不能为空！");
		return;
	}
	// 提交表单
	$("#loginForm").submit();
});
