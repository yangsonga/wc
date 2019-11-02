function userLogin() {
    var userName = $("#userName").val();
    var userPwd = $("#userPwd").val();
    if(isEmpty(userName)){
        alert("用户名不能为空");
        return ;
    }
    if(isEmpty(userPwd)){
        alert("密码不能为空");
        return ;
    }
    $.ajax({
        type:"post",
        url:"/crm/user/userLogin",
        data:{
            'userName':userName,
            'userPwd':userPwd
        },
        dataType:'json',
        success:function (data) {
            if(data.code==200){
                alert(data.msg);
                //在前台存cookie
                $.cookie("userName",data.result.userName);
                $.cookie("trueName",data.result.trueName);
                $.cookie("id",data.result.id);
                //实现页面的跳转
                window.location.href="main";
            }else {
                alert(data.msg);
            }
        }
    });
}