function openTab(text, url, iconCls){
    if($("#tabs").tabs("exists",text)){
        $("#tabs").tabs("select",text);
    }else{
        var content="<iframe frameborder=0 scrolling='auto' style='width:100%;height:100%' src='" + url + "'></iframe>";
        $("#tabs").tabs("add",{
            title:text,
            iconCls:iconCls,
            closable:true,
            content:content
        });
    }
}

function logout() {
    $.messager.confirm("来自crm系统","您确认要退出吗",function (r) {
        if(r){
           setTimeout(function () {
               //退出系统清楚cookie,重定向到index.ftl首页
               $.removeCookie("userName");
               $.removeCookie("trueName");
               $.removeCookie("id");
               window.location.href="index";
           },1500)
        }
    });
}

function openPasswordModifyDialog() {
    $("#dlg").dialog("open");
}
function modifyPassword() {
    $("#fm").form("submit",{
        url:"/crm/user/updatePwd",
        onsubmit:function () {
            //验证form表单项
            return $("#fm").form("validate");
        },
        success:function (data) {
            data=JSON.parse(data);//将返回值转换成为json串
            if(data.code==200){
               setTimeout(function () {
                   $.removeCookie("userName");
                   $.removeCookie("trueName");
                   $.removeCookie("id");
                   window.location.href="index";
               },1500)
            }else{
                $.messager.alert("来自crm系统",data.msg,"info");
            }
        }
    });
}
function closePasswordModifyDialog() {
    $("#dlg").dialog("close");
}