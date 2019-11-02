function searchCustomer() {
    $("#dg").datagrid("load",{
       "khno":$("#s_khno").val(),
        "name":$("#s_name").val()
    });
}
function openCustomerAddDialog() {
    $("#fm").form("clear");
   $("#dlg").dialog("open").dialog("setTitle","添加客户信息");
}

function openCustomerModifyDialog() {
   var rows = $("#dg").datagrid("getSelections");
   if(rows.length<1){
       $.messager.alert("来自crm","请选择一条需要修改的记录","info");
       return;
   }
    if(rows.length>1){
        $.messager.alert("来自crm","请选择一条需要修改的记录","info");
        return;
    }
    $("#fm").form("load",rows[0]);
    $("#dlg").dialog("open").dialog("setTitle","修改客户信息");
}
function saveOrUpdateCustomer() {
    var id = $("#id").val();
    var url=ctx+"/customer/update";
    if(isEmpty(id)){
        //为添加
        url=ctx+"/customer/insert";
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
               searchCustomer();
               closeCustomerDialog();
           }else{
               $.messager.alert("来自crm",data.msg,"error");
           }
       }
   });
}
function closeCustomerDialog() {
    $("#dlg").dialog("close");
}

function deleteCustomer() {
    var rows = $("#dg").datagrid("getSelections");
    if(rows.length<1){
        $.messager.alert("来自crm","请至少选择一条需要删除的记录","info");
        return;
    }
    var id ="id=";
    for(var i=0;i<rows.length;i++){
        if(i<rows.length-1){
            id = id+rows[i].id+"&id="
        }else{
            id=id+rows[i].id;
        }
    }
    $.messager.confirm("来自crm","确认删除这些记录吗",function (r) {
        if(r){
            $.ajax({
                url:ctx+"/customer/delete",
                data:id,
                dateType:"json",
                success:function (data) {
                    if(data.code==200){
                     $.messager.alert("来自crm",data.msg,"info");
                        searchCustomer();
                    }else{
                        $.messager.alert("来自crm",data.msg,"info");
                    }
                }
            });
        }
    });
}
function openCustomerOtherInfo(title,type) {
   var rows = $("#dg").datagrid("getSelections");
   if(rows.length<1||rows.length>1){
       $.messager.alert("来自crm","请选择一条记录","info");
       return;
   }
   //打开选项卡
    window.parent.openTab(title,ctx+"/customer/openCustomerOtherInfo/"+type+"/"+rows[0].id);
}