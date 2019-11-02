function searchAccount(){
	// 账户名称
	var aname = $("#aname").val();
	// 账户类型
	var atype = $("#atype").combo("getValue");
	// 创建时间
	var createTime = $("#createTime").combo("getValue");
	// console.log(createTime);
	// 加载和显示第一页的所有行。如果指定了'param'，它将取代'queryParams'属性。通常可以通过传递一些参数执行一次查询，通过调用这个方法从服务器加载新数据。
	$('#dg').datagrid('load',{
		'aname': aname,
		'atype': atype,
		'createTime': createTime
	});
}

/****************************** 添加账户 start *****************************/
/**
 * 打开对话框
 */
function openAddDialog(){
	// 清空表单中的数据
	$("#addForm").form("reset");
	// 打开对话框
	$("#addDialog").dialog("open");
}

/**
 * 关闭对话框
 */
function closeAddDialog(){
	// 关闭对话框
	$("#addDialog").dialog("close");
}

/**
 * 添加账户
 */
function addAccount(){
	//1、获取表单中的值
	var accountName = $("#accountName").val(); // 账户名称
	var accountType = $("#accountType").combo("getValue"); // 账户类型
	console.log(accountType);
	var money = $("#money").val(); // 账户金额
	var remark = $("#remark").val(); // 账户备注
	//	2、非空判断参数
	if(isEmpty(accountName)){
		//如果未空，则显示提示信息
		$.messager.alert('添加账户','账户名称不能为空！',"warning");  
		return;
	}
	if(isEmpty(accountType)){
		$.messager.alert('添加账户','账户类型不能为空！',"warning");  
		return;
	}
	if(isEmpty(money)){
		$.messager.alert('添加账户','账户金额不能为空！',"warning");  
		return;
	}
	
	//	3、通过ajax把表单中的数据发送到后台
	$.ajax({
		type:"post",
		url:"account",
		data:{
			'accountName':accountName,
			'accountType':accountType,
			'money':money,
			'remark':remark,
			'actionName':'addAccount'
		},
		success:function(result){
			//	回调函数 result
			if(result == 1){
				//	result == 1 // 成功
				//	弹出提示信息告诉用户添加成功
				$.messager.alert('添加账户','添加成功！',"info");  
				//	刷新数据表格
				$("#dg").datagrid("reload");
				//	关闭对话框
				$("#addDialog").dialog("close");
			}else{
				//	result == 0 // 失败
				//	弹出提示信息告诉用户添加失败即可
				$.messager.alert('添加账户','添加失败！',"error");  
			}
		}
	});
}
/****************************** 添加账户 end *****************************/



/****************************** 修改账户 start *****************************/
/**
 * 打开对话框
 */
function openUpdateDialog(){
	// 1、得到选中行，调用 Datagrid 组件的方法 getSelected 返回第一个被选中的行或如果没有选中的行则返回null。
	var obj = $("#dg").datagrid("getSelected");
	// console.log(obj);
	// 2、判断是否选中了记录
	if (obj == null){
		$.messager.alert('修改账户','请选择要修改的账户！',"warning"); 
		return;
	}
	// 3、把选中的记录填充进对话框
	$("#updateForm").form('load',{
		accountName2:obj.accountName,
		accountType2:obj.accountType,
		money2:obj.money,
		remark2:obj.remark,
		accountId:obj.id
	});
	// 4、打开对话框
	$("#updateDialog").dialog("open");
}

/**
 * 关闭对话框
 */
function closeUpdateDialog(){
	// 关闭对话框
	$("#updateDialog").dialog("close");
}

/**
 * 添加账户
 */
function updateAccount(){
	//1、获取表单中的值
	var accountName = $("#accountName2").val(); // 账户名称
	var accountType = $("#accountType2").combo("getValue"); // 账户类型
	var money = $("#money2").val(); // 账户金额
	var remark = $("#remark2").val(); // 账户备注
	var accountId = $("#accountId").val(); // 账户ID 
	//	2、非空判断参数
	if(isEmpty(accountName)){
		//如果未空，则显示提示信息
		$.messager.alert('修改账户','账户名称不能为空！',"warning");  
		return;
	}
	if(isEmpty(accountType)){
		$.messager.alert('修改账户','账户类型不能为空！',"warning");  
		return;
	}
	if(isEmpty(money)){
		$.messager.alert('修改账户','账户金额不能为空！',"warning");  
		return;
	}
	
	//	3、通过ajax把表单中的数据发送到后台
	$.ajax({
		type:"post",
		url:"account",
		data:{
			'accountName':accountName,
			'accountType':accountType,
			'money':money,
			'remark':remark,
			'actionName':'updateAccount',
			'accountId':accountId
		},
		success:function(result){
			//	回调函数 result
			if(result == 1){
				//	result == 1 // 成功
				//	弹出提示信息告诉用户修改成功
				$.messager.alert('修改账户','修改成功！',"info");  
				//	刷新数据表格
				$("#dg").datagrid("reload");
				//	关闭对话框
				$("#updateDialog").dialog("close");
			}else{
				//	result == 0 // 失败
				//	弹出提示信息告诉用户添加失败即可
				$.messager.alert('修改账户','修改失败！',"error");  
			}
		}
	});
}
/****************************** 修改账户 end *****************************/


/****************************** 删除账户 start *****************************/
function deleteAccount(){
	// 1、判断用户是否选择要删除的账户
	var objs = $("#dg").datagrid("getChecked");
	 console.log(objs);
	if(objs.length < 1){
		//如果没有，则弹出警告框，提示用户至少选择一个账户
		$.messager.alert('删除账户','请至少选择一条要删除的账户！',"warning");  
		return;
	}
	// 2、如果有选中的账户，则ajax请求后台进行删除操作
	/*	显示一个包含“确定”和“取消”按钮的确认消息窗口。参数：
		title：在头部面板显示的标题文本。
		msg：显示的消息文本。
		fn(b): 当用户点击“确定”按钮的时侯将传递一个true值给回调函数，否则传递一个false值。 
	*/
	$.messager.confirm('删除账户', '您确定要删除选中的账户吗？', function(r){
		if (r){
			//拿到要删除的所有账户 ID，通过 datagrid 的方法 getChecked 获取
			var ids = "";
			for(var i = 0; i < objs.length; i++){
				// 拿到每一个账户对象
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
				url:"account",
				data:{
					"actionName":"deleteAccount",
					"ids":ids
				},
				success:function(result){
					//回调函数，判断是否删除成功
					if(result == 1){
						//删除成功，提示用户删除成功
						$.messager.alert('删除账户','删除成功！',"info"); 
						//刷新数据表格
						$("#dg").datagrid("reload");
					}else{
						//删除失败，提示用户删除失败
						$.messager.alert('删除账户','删除失败！',"error"); 
					}
				}
			});
		}
	});

}
/****************************** 删除账户 end *****************************/