function searchPayIn(){
	// 收入名称
	var inName = $("#inName").val();
	// 账户类型
	var inType = $("#inType").combo("getValue");
	// 创建时间
	var createTime = $("#createTime").combo("getValue");
	// console.log(createTime);
	// 加载和显示第一页的所有行。如果指定了'param'，它将取代'queryParams'属性。通常可以通过传递一些参数执行一次查询，通过调用这个方法从服务器加载新数据。
	$('#payinData').datagrid('load',{
		'inName': inName,
		'inType': inType,
		'createTime': createTime
	});
}

/****************************** 添加收入 start *****************************/
/**
 * 打开对话框
 */
function openAddDialog(){
	// 清空表单中的数据
	$("#addForm").form("reset");
	// 动态加载所属账户的下拉框数据
	loadAccountName();
	// 打开对话框
	$("#addDialog").dialog("open");
}

function loadAccountName(){
	$('#accountId').combobox({    
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
	$("#addDialog").dialog("close");
}

/**
 * 添加收入
 */
function addPayIn(){
	//1、获取表单中的值
	var inName = $("#payInName").val(); // 账户名称
	var inType = $("#payInType").combo("getValue"); // 账户类型
	var accountId = $("#accountId").combo("getValue") // 所属账户
	var money = $("#money").val(); // 账户金额
	var remark = $("#remark").val(); // 账户备注
	//	2、非空判断参数
	if(isEmpty(inName)){
		//如果未空，则显示提示信息
		$.messager.alert('添加收入','收入名称不能为空！',"warning");  
		return;
	}
	if(isEmpty(inType)){
		$.messager.alert('添加收入','收入类型不能为空！',"warning");  
		return;
	}
	if(accountId=="请选择所属账户"){
		$.messager.alert('添加收入','所属账户不能为空！',"warning");  
		return;
	}
	if(isEmpty(money)){
		$.messager.alert('添加收入','收入金额不能为空！',"warning");  
		return;
	}
	
	//	3、通过ajax把表单中的数据发送到后台
	$.ajax({
		type:"post",
		url:"payin",
		data:{
			'inName':inName,
			'inType':inType,
			'accountId':accountId,
			'money':money,
			'remark':remark,
			'actionName':'addPayIn'
		},
		success:function(result){
			//	回调函数 result
			if(result == 1){
				//	result == 1 // 成功
				//	弹出提示信息告诉用户添加成功
				$.messager.alert('添加收入','添加成功！',"info");  
				//	刷新数据表格
				searchPayIn();
				//	关闭对话框
				$("#addDialog").dialog("close");
			}else{
				//	result == 0 // 失败
				//	弹出提示信息告诉用户添加失败即可
				$.messager.alert('添加收入','添加失败！',"error");  
			}
		}
	});
}
/****************************** 添加收入 end *****************************/

/****************************** 修改收入 start *****************************/
/**
 * 打开对话框
 */
function openUpdateDialog(){
	// 1、得到选中行，调用 Datagrid 组件的方法 getSelected 返回第一个被选中的行或如果没有选中的行则返回null。
	var obj = $("#payinData").datagrid("getSelected");
	// console.log(obj);
	// 2、判断是否选中了记录
	if (obj == null){
		$.messager.alert('修改收入','请选择要修改的收入！',"warning"); 
		return;
	}
	// 动态加载所属账户的下拉框
	loadAccountName2();
	// 3、把选中的记录填充进对话框
	$('#updateForm').form('load',{
		payInName2:obj.inName,
		payInType2:obj.inType,
		formAccountId:obj.accountId,
		formMoney:obj.money,
		remark2:obj.remark,
		pId:obj.id,
		datagridAccountId:obj.accountId,
		datagridMoney:obj.money
	});
	// 4、打开对话框
	$("#updateDialog").dialog("open");
}

function loadAccountName2(){
	$('#formAccountId').combobox({    
	    url:'account?actionName=queryAccountList',  // 设置远程访问的url  
	    valueField:'id',    // 基础数据值名称绑定到该下拉列表框。
	    textField:'accountName',   // 基础数据字段名称绑定到该下拉列表框。
	});  
}

/**
 * 关闭对话框
 */
function closeUpdateDialog(){
	// 关闭对话框
	$("#updateDialog").dialog("close");
}

/**
 * 修改收入
 */
function updatePayIn(){
	//1、获取表单中的值
	var inName = $("#payInName2").val(); // 收入名称
	var inType = $("#payInType2").combo("getValue"); // 收入类型
	var formAccountId = $("#formAccountId").combo("getValue"); // 正确的账户id
	var formMoney = $("#formMoney").val(); // 正确收入金额
	var remark = $("#remark2").val(); // 收入备注
	var pId = $("#pId").val(); // 收入ID 
	var datagridAccountId = $("#datagridAccountId").val();//原来的账户id
	var datagridMoney = $("#datagridMoney").val();//不正确的金额
	//	2、非空判断参数
	if(isEmpty(inName)){
		//如果未空，则显示提示信息
		$.messager.alert('修改收入','收入名称不能为空！',"warning");  
		return;
	}
	if(isEmpty(inType)){
		$.messager.alert('修改收入','收入类型不能为空！',"warning");  
		return;
	}
	if(isEmpty(formAccountId)){
		$.messager.alert('修改收入','所属账户不能为空！',"warning");  
		return;
	}
	if(isEmpty(formMoney)){
		$.messager.alert('修改收入','收入金额不能为空！',"warning");  
		return;
	}
	if(isEmpty(pId)){
		$.messager.alert('修改收入','参数异常！',"warning");  
		return;
	}
	if(isEmpty(datagridAccountId)){
		$.messager.alert('修改收入','参数异常！',"warning");  
		return;
	}
	if(isEmpty(datagridMoney)){
		$.messager.alert('修改收入','参数异常！',"warning");  
		return;
	}
	
	//	3、通过ajax把表单中的数据发送到后台
	$.ajax({
		type:"post",
		url:"payin",
		data:{
			'inName':inName,
			'inType':inType,
			'formMoney':formMoney,
			'remark':remark,
			'formAccountId':formAccountId,
			'pId':pId,
			'datagridAccountId':datagridAccountId,
			'datagridMoney':datagridMoney,
			'actionName':'updatePayIn'
		},
		success:function(result){
			//	回调函数 result
			if(result == 1){
				//	result == 1 // 成功
				//	弹出提示信息告诉用户修改成功
				$.messager.alert('修改收入','修改成功！',"info");  
				//	刷新数据表格
				searchPayIn();
				//	关闭对话框
				$("#updateDialog").dialog("close");
			}else{
				//	result == 0 // 失败
				//	弹出提示信息告诉用户添加失败即可
				$.messager.alert('修改收入','修改失败！',"error");  
			}
		}
	});
}
/****************************** 修改收入 end *****************************/


/****************************** 删除收入 start *****************************/
function deletePayIn(){
	// 1、判断用户是否选择要删除的收入
	var objs = $("#payinData").datagrid("getChecked");
	// console.log(objs);
	if(objs.length < 1){
		//如果没有，则弹出警告框，提示用户至少选择一个收入
		$.messager.alert('删除收入','请至少选择一条要删除的收入！',"warning");  
		return;
	}
	// 2、如果有选中的收入，则ajax请求后台进行删除操作
	/*	显示一个包含“确定”和“取消”按钮的确认消息窗口。参数：
		title：在头部面板显示的标题文本。
		msg：显示的消息文本。
		fn(b): 当用户点击“确定”按钮的时侯将传递一个true值给回调函数，否则传递一个false值。 
	*/
	$.messager.confirm('删除收入', '您确定要删除选中的收入吗？', function(r){
		if (r){
			//拿到要删除的所有收入 ID，通过 datagrid 的方法 getChecked 获取
			var ids = "";
			for(var i = 0; i < objs.length; i++){
				// 拿到每一个收入对象
				var obj = objs[i];
				// 拿到对象中的ID
				if( i == objs.length - 1){
					ids += obj.id;
				}else{
					ids += obj.id + ",";
				}
			}
			// console.log(ids);
			$.ajax({
				type:"post",
				url:"payin",
				data:{
					"actionName":"deletePayIn",
					"ids":ids
				},
				success:function(result){
					//回调函数，判断是否删除成功
					if(result == 1){
						//删除成功，提示用户删除成功
						$.messager.alert('删除收入','删除成功！',"info"); 
						//刷新数据表格
						searchPayIn();
					}else{
						//删除失败，提示用户删除失败
						$.messager.alert('删除收入','删除失败！',"error"); 
					}
				}
			});
		}
	});

}
/****************************** 删除收入 end *****************************/