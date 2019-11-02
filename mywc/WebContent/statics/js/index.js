/*
 * 打开选项卡
 * 	判断点击的选项卡是否存在
 * 		不存在  新建选项卡
 * 		存在  选中选项卡
 */
function openTabs(title,icon,url){
	// 判断选项卡是否存在，  exists   表明指定的面板是否存在，'which'参数可以是选项卡面板的标题或索引。
	var flag = $("#tabs").tabs("exists",title);
	// console.log(flag);
	if(flag){
		// 存在  选中选项卡  select  选择一个选项卡面板，'which'参数可以是选项卡面板的标题或者索引。
		$("#tabs").tabs("select",title);
	}else{
		// 不存在  新建选项卡
		$('#tabs').tabs('add',{ 
			iconCls:icon,
		    title:title,
		    selected:true,
		    content:'<iframe src='+url+' style="width:100%;height:100%"></iframe>',    
		    closable:true,     
		});  
	}
}