function formatterState(data) {
    if(data==0){
        return "未分配";
    }else if(data==1){
        return "已分配";
    }else {
        return "未定义";
    }

}
//打开页面sale_chance.ftl时调用方法，相当于查询所有数据
$(function () {
    searchSaleChances();
})
function searchSaleChances() {
   $("#dg").datagrid("load",{
       "createMan":$("#createMan").val(),
       "customerName":$("#customerName").val(),
       "createDate":$("#createDate").datebox("getValue"),
       "state":$("#state").combobox("getValue")
   })
}
/**************添加营销机会记录*******************/
function openAddAccountDialog() {
    //打开对话框
    //由于修改与添加共用一个对话框，所以这里要修改对话框的title
    $("#dlg").dialog("setTitle","添加营销机会记录");
    //点击修改时，会把数据表格中的数据加载到from表单中，这里是添加把数据清除
    $("#fm").form("clear");
    //清理对话框中的form表单中的内容
    $("#dlg").dialog("open");
}
function saveAccount() {
    //添加与修改共用一个方法，这里判断是用添加还是修改
    //添加sale_chance没有id,修改有id,通过id判断是修改还是添加
    var id = $("#id").val();
    //默认url为修改sale_chance
    var url = ctx+"/sale_chance/update";
    if(isEmpty(id)){
       //如果为空，则为添加
        url = ctx+"/sale_chance/insert";
    }
  //利用form表单进行提交
    $("#fm").form("submit",{
        url:url,
        onSubmit:function (params) {
            params.createMan=$.cookie("trueName");
            return $("#fm").form("validate");
        },
        success:function (data) {
            //将返回结果转为json格式
            data = JSON.parse(data);
            //判断插入是否成功
            if(data.code==200){
                //提示用户
                $.messager.alert("来自crm",data.msg,"info");
                //关闭对话框
                closeDialog();
                //刷新数据表格
                searchSaleChances();
            }else{
                //提示用户插入失败
                $.messager.alert("来自crm",data.msg,"error");
            }
        }
    });
}
function closeDialog() {
    $("#dlg").dialog("close");
}
/***************添加营销机会记录******************/
/*******************修改营销机会记录**************/
function openModifyAccountDialog() {
    //判断是否选中了数据表格中的数据
    var rows = $("#dg").datagrid("getSelections");
    if(rows.length==0){
        $.messager.alert("来自crm","请选择一条记录","info");
        return;
    }
    if(rows.length>1){
        $.messager.alert("来自crm","请选择一条记录","info");
        return;
    }
    //将选中的数据表格数据加载进对话框中的form表单
    $("#fm").form("load",rows[0]);
    //打开对话框
    $("#dlg").dialog("open").dialog("setTitle","修改营销机会记录");
}
/*******************修改营销机会记录**************/

/*****************删除营销记录*********************/
function deleteAccount() {
    //判断是否至少选中一条记录
    //获取数据表格中的选中记录
     var rows = $("#dg").datagrid("getSelections");
     if(rows.length<1){
         $.messager.alert("来自crm","请至少选中一条记录","error");
         return ;
     }
     //批量删除，拼接id
     var id = "id="
     for (var i=0;i<rows.length;i++){
        if(i<rows.length-1){
            //不是最后一个
            id = id+rows[i].id+"&id=";
        }else{
            id = id+rows[i].id;
        }
     }
     //确认框，确认删除
    $.messager.confirm("来自crm","确认删除这些营销机会记录吗？",function (r) {
        if(r){
            //确认删除，发送ajax请求
            $.ajax({
                url:ctx+"/sale_chance/delete",
                data:id,
                dataType:"json",
                success:function (data) {
                    if(data.code==200){
                        $.messager.alert("来自crm","删除成功","info");
                        //刷新数据表格
                        searchSaleChances();
                    }else{
                        $.messager.alert("来自crm",data.msg,"info");
                    }
                 }
                }
            );
        }
    });
}
/*****************删除营销记录*********************/