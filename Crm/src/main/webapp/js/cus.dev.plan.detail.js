//预加载内容
$(function () {
  var devResult = $("#devResult").val();
  if(devResult==2||devResult==3){
      //开发成功或者失败移除工具栏
      $("#toolbar").remove();
  }
    $("#dg").edatagrid({
        url:ctx+"/cus_dev_plan/queryCusDevPlans?saleChanceId="+$("#saleChanceId").val(),
        saveUrl:ctx+"/cus_dev_plan/insert?saleChanceId="+$("#saleChanceId").val(),
        updateUrl:ctx+"/cus_dev_plan/update?saleChanceId="+$("#saleChanceId").val()
    });
})
//删除客户开发计划
function delCusDevPlan() {
    var row = $("#dg").edatagrid("getSelected");
    if(row==null){
        $.messager.alert("来自crm","请选一条记录","info");
        return ;
    }
    $.messager.confirm("来自crm","您确认删除这条客户开发计划吗",function (r) {
        if(r){
            $.ajax({
                type:"post",
                url:ctx+"/cus_dev_plan/delete",
                data:{
                    id:row.id
                },
                dataType:"json",
                success:function (data) {
                    if(data.code==200){
                        $.messager.alert("来自crm",data.msg,"info");
                        //刷新数据表格
                        $("#dg").edatagrid("load");
                    }else{
                        $.messager.alert("来自crm",data.msg,"error");
                    }
                }
            });
        }
    })
}
//保存客户开发计划
function saveCusDevPlan() {
    $("#dg").edatagrid("saveRow");
    $("#dg").edatagrid("load");
}
//更新客户开发计划
function updateCusDevPlan() {
    $("#dg").edatagrid("saveRow");
    $("#dg").edatagrid("load");
}
//通过开发结果判断执行操作
function updateSaleChanceDevResult(devResult) {
    $.messager.confirm("来自crm", "您确认执行该操作吗", function (r) {
        if (r) {
            $.ajax({
                type: "post",
                url: ctx + "/sale_chance/updateSaleChanceDevResult",
                data: {
                    "devResult": devResult,
                    "saleChanceId": $("#saleChanceId").val()
                },
                dataType: "json",
                success: function (data) {
                    if (data.code == 200) {
                        $.messager.alert("来自crm", data.msg, "info");
                        $("#toolbar").remove();
                    } else {
                        $.messager.alert("来自crm", data.msg, "error");
                    }
                }
            });
        }
    })
}
