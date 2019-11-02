function formatterGrade(val) {
    if(val==0){
        return "根级";
    }
    if(val==1){
        return "第一级";
    }
    if(val==2){
        return "第二级";
    }
}
$(function () {
    //每次打开对话框时，把form表单清空
    $("#dlg").dialog({
        onClose:function () {
            initFormData();
        }
    });
    //把父类型菜单隐藏
    $("#parentMenu").hide();
    //格式化下拉框
    $("#grade").combobox({
        //onChange时间
        onChange:function (grade) {
            if(grade==1||grade==2){
                $("#parentMenu").show();
            }
            if(grade==0){
                $("#parentMenu").hide();
            }
            loadParentModules(grade-1);
        }
    });
})
function loadParentModules(grade) {
    $("#parentId").combobox("clear");
    $("#parentId").combobox({
        url:ctx+"/module/queryModulesByGrade?grade="+grade,
        valueField:'id',
        textField:'moduleName'
    });
}
function initFormData() {
    $("#fm").form("clear");
}
function searchModules() {
    $("#dg").datagrid("load",{
        'moduleName':$("#moduleName").val(),
        'optValue':$("#optValue").val(),
        'parentModuleName':$("#parentModuleName").val()
    });
}
function openAddModuleDialog() {
    $("#dlg").dialog("open");
}
function saveOrUpdateModule() {
    var id = $("#id").val();
    var url=ctx+"/module/update";
    if(isEmpty(id)){
        url = ctx+"/module/insert";
    }
    $("#fm").form("submit",{
        url:url,
        onSubmit:function () {
           return  $("#fm").form("validate");
        },
        success:function (data) {
            data = JSON.parse(data);
            if(data.code==200){
                $.messager.alert("来自crm",data.msg,"info");
                closeDialog();
                searchModules();
            }else{
                $.messager.alert("来自crm",data.msg,"error");
            }
        }
    });
}
function closeDialog(){
    $("#dlg").dialog("close")
}
function openModifyModuleDialog() {
    var rows = $("#dg").datagrid("getSelections");
    if(rows.length!=1){
        $.messager.alert("来自crm","只能选择一条记录修改","info");
        return;
    }
    //下面都是填充表单信息，先判断，在填充
    $("#fm").form("load",rows[0]);
    var grade = rows[0].grade;
    if(grade!=0){
        loadParentModules(grade-1);
        $("#parentMenu").show();
        $("#parentId").combobox("setValue",rows[0].parentId);
    }else{
       $("#grade").combobox("setValue",grade);
    }
    $("#dlg").dialog("open").dialog("setTitle","修改模块");
}
function deleteModule() {
    var rows = $("#dg").datagrid("getSelections");
    if(rows.length!=1){
        $.messager.alert("来自crm","只能删除一条记录","info");
        return;
    }
    $.messager.confirm("来自crm","您确认删除吗？",function (r) {
        if(r){
            $.ajax({
                type:'post',
                url:ctx+"/module/delete",
                data:"id="+rows[0].id,
                dataType:'json',
                success:function (data) {
                    if(data.code==200){
                        $.messager.alert("来自crm",data.msg,"info");
                        searchModules();
                    }else{
                        $.messager.alert("来自crm",data.msg,"error");
                    }
                }
            });
        }
    })
}