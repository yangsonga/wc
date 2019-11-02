$(function(){  // 预加载
	loadSearchTypeName();
})

/**
 * 加载工具栏的支出类型搜索下拉框
 */
function loadSearchTypeName(){
	$('#payOutTypeSearch').combobox({    
	    url:'outType?actionName=queryParentType',  // 设置远程访问的url  
	    valueField:'id',    // 基础数据值名称绑定到该下拉列表框。
	    textField:'typeName',   // 基础数据字段名称绑定到该下拉列表框。
	});  
}

function search(){
	// 支出名称
	var outName = $("#payOutNameSearch").val();
	// 账户类型
	var outType = $("#payOutTypeSearch").combo("getValue");
	// 创建时间
	var createTime = $("#createTimeSearch").combo("getValue");
	// console.log(createTime);
	// 加载和显示第一页的所有行。如果指定了'param'，它将取代'queryParams'属性。通常可以通过传递一些参数执行一次查询，通过调用这个方法从服务器加载新数据。
	$('#payOutData').datagrid('load',{
		'outName': outName,
		'typeId': outType,
		'createTime': createTime
	});
}


/****************************** 添加支出 start *****************************/
/**
 * 打开对话框
 */
function openAddDialog(){
	// 清空表单中的数据
	$("#addForm").form("reset");
	// 加载支出类型
	loadOutType();
	// 动态加载所属账户的下拉框数据
	loadAccountName();
	// 打开对话框
	$("#add").dialog("open");
}

function loadOutType(){
	$('#parentType').combobox({    
	    url:'outType?actionName=queryParentType',  // 设置远程访问的url  
	    valueField:'id',    // 基础数据值名称绑定到该下拉列表框。
	    textField:'typeName',   // 基础数据字段名称绑定到该下拉列表框。
	    onChange:function(newValue,oldValue){
	    	$('#sonType').combobox({
	    	    url:'outType?actionName=queryChildType&pId=' + newValue,  // 设置远程访问的url  
	    	    valueField:'id',    // 基础数据值名称绑定到该下拉列表框。
	    	    textField:'typeName',   // 基础数据字段名称绑定到该下拉列表框。
	    	});  
	    }
	});  
}

function loadAccountName(){
	$('#AccountId').combobox({    
	    url:'account?actionName=queryAccountList',  // 设置远程访问的url  
	    valueField:'id',    // 基础数据值名称绑定到该下拉列表框。
	    textField:'accountName',   // 基础数据字段名称绑定到该下拉列表框。
	    value:"请选择所属账户"
	});  
}

/**
 * 关闭对话框
 */
function closeAddDialog(){
	// 关闭对话框
	$("#add").dialog("close");
}

/**
 * 添加支出记录
 */
function addPayOut(){
	//1、获取表单中的值
	var outName = $("#name").val(); // 支出名称
	var outType = $("#parentType").combo("getValue"); // 支出类型
	var outChildType = $("#sonType").combo("getValue"); // 支出子类型
	var accountId = $("#accountId").combo("getValue") // 所属账户
	var money = $("#money").val(); // 支出金额
	var remark = $("#remark").val(); // 支出备注
	//	2、非空判断参数
	if(isEmpty(outName)){
		//如果未空，则显示提示信息
		$.messager.alert('添加支出','支出名称不能为空！',"warning");  
		return;
	}
	if(isEmpty(outType)){
		$.messager.alert('添加支出','支出类型不能为空！',"warning");  
		return;
	}
	if(isEmpty(outChildType)){
		$.messager.alert('添加支出','支出类型不能为空！',"warning");  
		return;
	}
	if(isEmpty(accountId)){
		$.messager.alert('添加支出','所属账户不能为空！',"warning");  
		return;
	}
	if(isEmpty(money)){
		$.messager.alert('添加支出','支出金额不能为空！',"warning");  
		return;
	}
	
	//	3、通过ajax把表单中的数据发送到后台
	$.ajax({
		type:"post",
		url:"payout",
		data:{
			'outName':outName,
			'outChildType':outChildType,
			'accountId':accountId,
			'money':money,
			'remark':remark,
			'actionName':'addPayOut'
		},
		success:function(result){
			//	回调函数 result
			if(result == 1){
				//	result == 1 // 成功
				//	弹出提示信息告诉用户添加成功
				$.messager.alert('添加支出','添加成功！',"info");  
				//	刷新数据表格
				search();
				//	关闭对话框
				$("#add").dialog("close");
			}else{
				//	result == 0 // 失败
				//	弹出提示信息告诉用户添加失败即可
				$.messager.alert('添加支出','添加失败！',"error");  
			}
		}
	});
}
/****************************** 添加支出 end *****************************/

/****************************** 修改支出 start *****************************/
/**
 * 打开对话框
 */
function openUpdateDialog(){
	// 1、得到选中行，调用 Datagrid 组件的方法 getSelected 返回第一个被选中的行或如果没有选中的行则返回null。
	var obj = $("#payOutData").datagrid("getSelected");
	// 2、判断是否选中了记录
	if (obj == null){
		$.messager.alert('修改支出','请选择要修改的支出！',"warning"); 
		return;
	}
	// 动态加载所属账户的下拉框
	loadAccountName2();
	// 加载支出类型
	loadOutType2(obj.parentId,obj.outTypeId);
	// 3、把选中的记录填充进对话框
	$('#updateForm').form('load',{
		name2:obj.outName,
		parentType2:obj.parentId,
		sonType2:obj.outTypeId,
		datagridAccountId:obj.accountId,
		datagridMoney:obj.money,
		formAccountId:obj.accountId,
		formMoney:obj.money,
		remark2:obj.remark,
		pId:obj.id
	});
	// 4、打开对话框
	$("#update").dialog("open");
}

function loadOutType2(parentId,outTypeId){
	$('#parentType2').combobox({    
	    url:'outType?actionName=queryParentType',  // 设置远程访问的url  
	    valueField:'id',    // 基础数据值名称绑定到该下拉列表框。id = parentId
	    textField:'typeName',   // 基础数据字段名称绑定到该下拉列表框。
	    onChange:function(newValue,oldValue){ // 选择下拉框选项触发的事件
	    	$('#sonType2').combobox({    
	    	    url:'outType?actionName=queryChildType&pId=' + newValue,  // 设置远程访问的url  
	    	    valueField:'id',    // 基础数据值名称绑定到该下拉列表框。
	    	    textField:'typeName'   // 基础数据字段名称绑定到该下拉列表框。
	    	});  
	    }/*,
	    //在加载远程数据成功的时候触发。在弹出对话框时，把数据表格中的数据加载进form表单 
		onLoadSuccess:function(){
			console.log(parentId);
			$('#sonType2').combobox({    
	    	    url:'outType?actionName=queryChildType&pId=' + parentId,  // 设置远程访问的url  
	    	    valueField:'id',    // 基础数据值名称绑定到该下拉列表框。
	    	    textField:'typeName'   // 基础数据字段名称绑定到该下拉列表框。
	    	});  
			$("#parentType2").combobox("setValue",parentId);
			// 给支出类型的子类型赋值
			$("#sonType2").combobox("setValue",outTypeId);
			
		}*/
	});  
}

function loadAccountName2(){
	$('#formAccountId').combobox({
	    url:'account?actionName=queryAccountList',  // 设置远程访问的url  
	    valueField:'id',    // 基础数据值名称绑定到该下拉列表框。
	    textField:'accountName'  // 基础数据字段名称绑定到该下拉列表框。
	});
}


/**
 * 关闭对话框
 */
function closeUpdateDialog(){
	// 关闭对话框
	$("#update").dialog("close");
}

/**
 * 修改支出
 */
function updatePayOut(){
	//1、获取表单中的值
	var outName = $("#name2").val(); // 支出名称
	var outType = $("#parentType2").combo("getValue"); // 支出父类型
	var outChildType = $("#sonType2").combo("getValue"); //支出子类型 
	var formAccountId = $("#formAccountId").combo("getValue"); // 正确所属账户
	var formMoney = $("#formMoney").val(); // 正确支出金额
	var remark = $("#remark2").val(); // 支出备注
	var pId = $("#pId").val(); // 支出ID 
	var datagridAccountId = $("#datagridAccountId").val(); // 正确所属账户
	var datagridMoney = $("#datagridMoney").val(); // 正确支出金额
	//	2、非空判断参数
	if(isEmpty(outName)){
		//如果未空，则显示提示信息
		$.messager.alert('修改支出','支出名称不能为空！',"warning");  
		return;
	}
	if(isEmpty(outType)){
		$.messager.alert('修改支出','支出类型不能为空！',"warning");  
		return;
	}
	if(isEmpty(outChildType)){
		$.messager.alert('修改支出','支出类型不能为空！',"warning");  
		return;
	}
	if(isEmpty(formAccountId)){
		$.messager.alert('修改支出','支出类型不能为空！',"warning");  
		return;
	}
	if(isEmpty(formMoney)){
		$.messager.alert('修改支出','支出金额不能为空！',"warning");  
		return;
	}
	if(isEmpty(pId)){
		$.messager.alert('修改支出','参数异常！',"warning");  
		return;
	}
	if(isEmpty(datagridAccountId)){
		$.messager.alert('修改支出','参数异常！',"warning");  
		return;
	}
	if(isEmpty(datagridMoney)){
		$.messager.alert('修改支出','参数异常！',"warning");  
		return;
	}
	//	3、通过ajax把表单中的数据发送到后台
	$.ajax({
		type:"post",
		url:"payout",
		data:{
			'outName':outName,
			'outType':outType,
			'outChildType':outChildType,
			'formMoney':formMoney,
			'remark':remark,
			'formAccountId':formAccountId,
			'pId':pId,
			'datagridAccountId':datagridAccountId,
			'datagridMoney':datagridMoney,
			'actionName':'updatePayOut'
		},
		success:function(result){
			//	回调函数 result
			if(result == 1){
				//	result == 1 // 成功
				//	弹出提示信息告诉用户修改成功
				$.messager.alert('修改支出','修改成功！',"info");  
				//	刷新数据表格
				search();
				//	关闭对话框
				$("#update").dialog("close");
			}else{
				//	result == 0 // 失败
				//	弹出提示信息告诉用户添加失败即可
				$.messager.alert('修改支出','修改失败！',"error");  
			}
		}
	});
}
/****************************** 修改支出 end *****************************/


/****************************** 删除支出 start *****************************/
/**
 * 给删除按钮绑定事件
 * 	得到所有选中的记录，调用datagrid的getChecked方法，返回复选框被选中的所有支出记录
 * 	判断是否至少选中一条记录
 * 		否，则提示用户
 * 	弹出确认框，询问用户是否删除
 * 		如果确认删除
 * 			通过循环得到被选中所有记录的id，把id拼接成需要的格式，1,2,3
 * 			通过ajax发出请求，删除选中的记录
 */
function deletePayOut(){
	// 得到所有选中的记录，调用datagrid的getChecked方法，返回复选框被选中的所有支出记录
	var objs = $("#payOutData").datagrid("getChecked");
	// 判断是否至少选中一条记录
	if(objs.length < 1){
		// 否，则提示用户
		$.messager.alert('删除支出','请至少选择一条需要删除的支出记录！','warning');
		return;
	}
	// 弹出确认框，询问用户是否删除
	$.messager.confirm('删除支出','您确认想要删除选中的支出记录吗？',function(r){    
	    if (r){  // 如果确认删除  
	    	// 通过循环得到被选中所有记录的id，把id拼接成需要的格式，1,2,3
	    	var ids = "";
	    	for (var i = 0; i < objs.length; i++){
	    		// 得到支出记录对象
	    		var obj = objs[i];
	    		if(i == objs.length - 1){
	    			ids += obj.id;
	    			break;
	    		}
	    		ids += obj.id + ",";
	    	}
	    	// 通过ajax发出请求，删除选中的记录
	    	$.ajax({
	    		type:"post",
	    		url:"payout",
	    		data:{
	    			"actionName":"deletePayOut",
	    			"ids":ids
	    		},
	    		success:function(result){
	    			// 判断是否删除成功
	    			if(result > 0){// 成功
	    				$.messager.alert('删除支出','成功删除'+result+'条记录！','info');
	    				// 刷新数据表格
	    				search();
	    			}else{ // 失败
	    				$.messager.alert('删除支出','删除失败！','error');
	    			}
	    		}
	    	});
	    }    
	});  
}
/****************************** 删除支出 end *****************************/