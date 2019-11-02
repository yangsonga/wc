$(function () {
    var lossId=$("#lossId").val();
    $("#dg").edatagrid({
        url:ctx + "/customer_repri/customerReprieveByLossId?lossId=" + lossId,
        saveUrl:ctx + "/customer_repri/insertReprive?lossId=" + lossId,
        updateUrl:ctx + "/customer_repri/updateReprive?lossId=" + lossId
    });
    var state = $("#state").val();
    if(state==1){
        $("#toolbar").remove();
        $("#dg").edatagrid("disableEditing");
    }
})
function saveCustomerRepri() {
    $("#dg").edatagrid("addRow");
    $("#dg").edatagrid("load");
}
function delCustomerRepri() {
    var rows = $("#dg").edatagrid("getSelections");
    if(rows.length==0){
        $.messager.alert("来自crm","请选择一条需要删除的记录","info");
        return;
    }
    $.messager.confirm("来自crm","您确认删除吗？",function (r) {
        if(r){
            $.ajax({
                url:ctx+"/customer_repri/deleteReprive",
                data:{
                    'id':rows[0].id
                },
                dataType:'json',
                success:function (data) {
                    if(data.code==200){
                        $.messager.alert("来自crm",data.msg,"info");
                        $("#dg").edatagrid("load");
                    }else{
                        $.messager.alert("来自crm",data.msg,"error");
                    }
                }
            });
        }
    })
}
function updateCustomerLossState() {
    $.messager.confirm("来自crm","您确认流失该客户暂缓信息吗？",function (r) {
        if(r){
            $.messager.prompt("来自crm","请输入原因",function (msg) {
                if(msg){
                    $.ajax({
                        type:'post',
                        url:ctx +"/customer_loss/updateCustomerLossState",
                        data:{
                            'lossId':$("#lossId").val(),
                            'lossReason':msg
                        },
                        dataType:'json',
                        success:function (data) {
                            if(data.code==200){
                                $.messager.alert("来自crm",data.msg,"info");
                                $("#toolbar").remove();
                            }else{
                                $.messager.alert("来自crm",data.msg,"error");
                            }
                        }
                    });
                }else{
                    $.messager.alert("来自crm","流失原因不能为空","info");
                }
            });
        }
    })
}
