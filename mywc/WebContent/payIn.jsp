<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>账户管理</title>
<%@ include file="commons.jsp" %>
</head>
<body>
   
	<div id="tb">
		<div>
			收入名称： <input id="inName" class="easyui-validatebox" /> 
			收入类型：
			 <select id="inType" class="easyui-combobox" style="width: 160px;"data-options="editable:false">
				<option value="">请选择收入类型</option>
				<option>工资</option>
				<option>奖金</option>
				<option>其他</option>
			</select> 
		     创建时间：<input id="createTime" type="text" class="easyui-datebox" data-options="editable:false"></input> 
			<a href="javascript:searchPayIn()" class="easyui-linkbutton" data-options="iconCls:'icon-search'" />搜索</a>
		</div>
		<a href="javascript:openAddDialog()" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" />添加</a>
		<a href="javascript:openUpdateDialog()"class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true" />修改</a>
		<a href="javascript:deletePayIn()"class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true" />删除</a>
		
	</div>

	<table id="payinData" class="easyui-datagrid" style="width:400px;height:200px"   
        data-options="url:'/mywc/payin?actionName=queryPayinByCondition',toolbar:'#tb',fit:true,rownumbers:true,pagination:true">   
    <thead>   
        <tr>   
            <th data-options="field:'ck',checkbox:true"></th>
            <th data-options="field:'accountId',hidden:true"></th>  
            <th data-options="field:'id',width:100">编号</th>   
            <th data-options="field:'inName',width:100">收入名称</th>   
            <th data-options="field:'inType',width:100">收入类型</th>  
            <th data-options="field:'money',width:100">收入金额</th>
            <th data-options="field:'accountName',width:100">所属账户</th>  
            <th data-options="field:'remark',width:300">收入备注</th> 
            <th data-options="field:'createTime',width:200">创建时间</th>
            <th data-options="field:'updateTime',width:200">修改时间</th>
        </tr>   
    </thead>   
</table>  
  <!-- 添加收入 -->
  <div id="addDialog" class="easyui-dialog" title="添加收入" style="width:400px;height:250px;"   
        data-options="iconCls:'icon-add',resizable:true,modal:true,closed:true">   
      <form id="addForm" style="margin-top: 20px">
        <table align="center">
           <tr>
              <td>收入名称：</td>
              <td>
               <input class="easyui-validatebox" id="payInName" data-options="required:true,missingMessage:'请输入账户名'" />
              </td>
           </tr>
           
            <tr>
              <td>收入类型：</td>
              <td>
                <select id="payInType" class="easyui-combobox" style="width:160px;" data-options="editable:false">   
                <option value="">请选择收入类型</option>
				<option>工资</option>
				<option>奖金</option>
				<option>其他</option>  
               </select>  
              </td>
           </tr>
           <tr>
              <td>所属账户：</td>
              <td>
                <select id="accountId" class="easyui-combobox" style="width:160px;" data-options="editable:false">   
                   
               </select>  
              </td>
           </tr>
            <tr>
              <td>收入金额：</td>
              <td>
                <input type="text" id="money" class="easyui-numberbox" value="100" data-options="min:0,precision:2"></input>  
              </td>
           </tr>
           
            <tr>
              <td>账户备注</td>
              <td>
               <input id="remark" class="easyui-validatebox" />  
              </td>
           </tr>
             <tr>
               <td></td>
               <td></td>
             </tr>
            <tr>
              <td style="padding-left: 20px">
              <a href="javascript:addPayIn()" class="easyui-linkbutton" data-options="iconCls:'icon-ok'" />保存</a>
              </td>
              <td style="padding-right: 20px">
              <a href="javascript:closeAddDialog()" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" />取消</a>
              </td>
           </tr>
        
        </table>
      </form>
</div>  
<!-- 添加收入 -->

<!-- 修改收入 -->
<div id="updateDialog" class="easyui-dialog" title="修改收入" style="width:400px;height:250px;"   
        data-options="iconCls:'icon-edit',resizable:true,modal:true,closed:true">   
      <form id="updateForm" style="margin-top: 20px">
         <input type="hidden" id="datagridAccountId" name="datagridAccountId"/>
         <input type="hidden" id="datagridMoney" name="datagridMoney"/>
         <input type="hidden" id="pId" name="pId"/>
        <table align="center">
           <tr>
              <td>收入名称：</td>
              <td>
               <input class="easyui-validatebox" id="payInName2" name="payInName2" data-options="required:true,missingMessage:'请输入账户名'" />
              </td>
           </tr>
           
            <tr>
              <td>收入类型：</td>
              <td>
                <select id="payInType2" name="payInType2" class="easyui-combobox" style="width:160px;" data-options="editable:false">   
                <option value="">请选择收入类型</option>
				<option>工资</option>
				<option>奖金</option>
				<option>其他</option>   
               </select>  
                
              </td>
           </tr>
           <tr>
              <td>所属账户：</td>
              <td>
                <select id="formAccountId" name="formAccountId" class="easyui-combobox" style="width:160px;" data-options="editable:false">   
              
               </select>  
                
              </td>
           </tr>
            <tr>
              <td>收入金额：</td>
              <td>
                <input type="text" id="formMoney" name="formMoney" class="easyui-numberbox" value="100" data-options="min:0,precision:2"></input>  
              </td>
           </tr>
           
            <tr>
              <td>收入备注</td>
              <td>
               <input id="remark2" name="remark2" class="easyui-validatebox" />  
              </td>
           </tr>
             <tr>
               <td></td>
               <td></td>
             </tr>
            <tr>
              <td style="padding-left: 20px">
              <a href="javascript:updatePayIn()" class="easyui-linkbutton" data-options="iconCls:'icon-ok'" />保存</a>
              </td>
              <td style="padding-right: 20px">
              <a href="javascript:closeUpdateDialog()" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" />取消</a>
              </td>
           </tr>
        </table>
      </form>
</div>  
<!-- 修改收入 -->

<!-- 删除收入 -->
<div id="deletePayIn" class="easyui-dialog" title="My Dialog" style="width:400px;height:200px;"   
        data-options="iconCls:'icon-remove',resizable:true,modal:true,closed:true">   
           <a href="javascript:deletePayIn()" class="easyui-linkbutton" data-options="iconCls:'icon-ok'" />保存</a>
           <a href="javascript:closeDeleteDialog()" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" />取消</a>
    
</div>  
<!-- 删除账户 -->

</body>
<script type="text/javascript" src="statics/js/payIn.js"></script>
</html>