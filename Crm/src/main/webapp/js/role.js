function searchRoles() {
    $("#dg").datagrid("load",{
        roleName:$("#roleName").val()
    });
}
function openAddRoleDialog() {
    $("#dlg").dialog("open").dialog("setTitle","添加角色记录");
    $("#fm").form("clear");
}
function openModifyRoleDialog() {
    var rows = $("#dg").datagrid("getSelections");
    if(rows.length!=1){
        $.messager.alert("来自crm","只能选择一条记录修改","info");
        return;
    }
    $("#fm").form("load",rows[0]);
    $("#dlg").dialog("open").dialog("setTitle","修改角色记录");
}
function closeDialog() {
    $("#dlg").dialog("close");
}
function saveOrUpdateRole() {
       var id = $("#id").val();
       var url = ctx+"/role/update";
       if(isEmpty(id)){
         url=ctx+"/role/insert";
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
                   searchRoles();
                   closeDialog();
               }else{
                   $.messager.alert("来自crm",data.msg,"info");
               }
           }
       });
}
function deleteRole() {
    var rows = $("#dg").datagrid("getSelections");
    if(rows.length!=1){
        $.messager.alert("来自crm","只能删除一条记录","info");
        return;
    }
    $.messager.confirm("来自crm","您确认删除吗？",function (r) {
        if(r){}
        $.ajax({
            type:'post',
            url:ctx+"/role/delete",
            data:{
                'id':rows[0].id
            },
            dataType:'json',
            success:function (data) {
                if(data.code==200){
                    $.messager.alert("来自crm",data.msg,"info");
                    searchRoles();
                }else{
                    $.messager.alert("来自crm",data.msg,"error");
                }
            }
        });
    })
}
function openRelatePermissionDlg() {
    var rows = $("#dg").datagrid("getSelections");
    if(rows.length!=1){
        $.messager.alert("来自crm","只能选择一条记录关联权限","info");
        return;
    }
    //将数据表格中的rid加载到form表单的隐藏域中
    $("#rid").val(rows[0].id);
    loadModuleData();
    $("#dlg02").dialog("open");
}
//加载module表中的一些数据
var ztreeObj;
function loadModuleData() {
   $.ajax({
       type:'post',
       url:ctx+"/module/queryAllModuleDtos",
       data:{
           'rid':$("#rid").val()
       },
       dataType:'json',
       success:function (data) {
           // zTree 的参数配置，深入使用请参考 API 文档（setting 配置详解）
           var setting = {
               check: {
                   enable: true
               },
               data: {
                   simpleData: {
                       enable: true
                   }
               },
               callback: {
                   onCheck: zTreeOnCheck
               }
           };
           // zTree 的数据属性，深入使用请参考 API 文档（zTreeNode 节点数据详解）
           var zNodes = data;
           ztreeObj= $.fn.zTree.init($("#treeDemo"), setting, zNodes);
       }
   });
}
function zTreeOnCheck() {
    var znodes = ztreeObj.getCheckedNodes(true);
    var moduleIds = "moduleIds=";
    if(znodes.length>0){
        for (var i=0;i<znodes.length;i++){
            if(i<=znodes.length-2){
                moduleIds = moduleIds+znodes[i].id+"&moduleIds="
            }else{
                moduleIds = moduleIds+znodes[i].id
            }
        }
    }
    $("#moduleIds").val(moduleIds);
}
function closeDialog02() {
    $("#dlg02").dialog("close");
}
//授权
function addPermission() {
    $.ajax({
        type:'post',
        url:ctx+"/role/addPermission",
        data:"rid="+$("#rid").val()+"&"+$("#moduleIds").val(),
        /**
         * {
         *     'rid':$("#rid").val(),
         *     'moduleIds':$("#moduleIds").val()
         * }
         * **/
        dataType:'json',
        success:function (data) {
            if(data.code==200){
                $.messager.alert("来自crm",data.msg,"info");
                //将form表单中的隐藏域中的值重置
                $("#rid").val("");
                $("#moduleIds").val("");
                closeDialog02();
                searchRoles();
            }else {
                $.messager.alert("来自crm",data.msg,"error");
            }
        }
    });
}