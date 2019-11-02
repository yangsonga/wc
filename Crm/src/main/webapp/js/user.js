function searchUsers() {
    $("#dg").datagrid("load",{
        userName:$("#userName").val(),
        trueName:$("#trueName").val(),
        phone:$("#phone").val(),
        email:$("#email").val()
    });
}
function openAddUserDialog() {
    $("#dlg").dialog("open").dialog("setTitle","添加用户");
    $("#fm").form("clear");
}
function closeDialog() {
    $("#dlg").dialog("close");
}
function saveOrUpdateUser() {
  var id = $("#id").val();
  var url = ctx+"/user/update";
  if(isEmpty(id)){
     url= ctx+"/user/insert";
  }
  $("#fm").form("submit",{
      url:url,
      onSubmit:function () {
          return $("#fm").form("validate");
      },
      success:function (data) {
          data = JSON.parse(data);
          if(data.code==200){
              $.messager.alert("来自crm",data.msg,"info");
              closeDialog();
              searchUsers();
          }else{
              $.messager.alert("来自crm",data.msg,"error");
          }
      }

  })
}
function openModifyUserDialog() {
    var rows = $("#dg").datagrid("getSelections");
    if(rows.length!=1){
        $.messager.alert("来自crm","请选择一条需要修改的记录","info");
        return;
    }
    $("#fm").form("load",rows[0]);
    $("#dlg").dialog("open").dialog("setTitle","修改用户");
}
function deleteUser() {
    var rows = $("#dg").datagrid("getSelections");
    if(rows.length!=1){
        $.messager.alert("来自crm","请选择一条需要删除的记录","info");
        return;
    }
    $.messager.confirm("来自crm","确认删除吗？",function (r) {
        if(r){
            $.ajax({
               type:"post",
                url:ctx+"/user/delete",
                data:{
                   'userId':rows[0].id
                },
                dataType:"json",
                success:function (data) {
                    if(data.code==200){
                        $.messager.alert("来自crm",data.msg,"info");
                        searchUsers();
                    }else{
                        $.messager.alert("来自crm",data.msg,"info");
                    }
                }
            });
        }
    });
}