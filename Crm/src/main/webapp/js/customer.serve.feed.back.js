function openFeedBackDlg() {
    var rows = $("#dg").datagrid("getSelections");
    if(rows.length!=1){
        $.messager.alert("来自crm","请选择一条需要处理的记录","info")
        return;
    }
    $("#fm").form("load",rows[0]);
    $("#dlg").dialog("open");
}
function addCustomerServeServiceFeedBack() {
    $("#fm").form("submit",{
        url:ctx+"/customer_serve/update",
        onSubmit:function (params) {
            params.state=4;
            return $("#fm").form("validate");
        },
        success:function (data) {
            data = JSON.parse(data);
            if(data.code==200){
                $.messager.alert("来自crm",data.msg,"info");
                closeCustomerServeDialog();
                $("#dg").datagrid("load");
            }else{
                $.messager.alert("来自crm",data.msg,"error");
            }
        }
    });
}
function closeCustomerServeDialog() {
    $("#dlg").dialog("close");
}