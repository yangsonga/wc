<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>支出管理</title>
<%@include file="commons.jsp" %>
</head>
<body>
 <div id="tb">
	 支出名称： <input id="payOutNameSearch" class="easyui-validatebox" />  
	 支出类型：<input id="payOutTypeSearch" class="easyui-combobox" />  
	 创建时间：<input id="createTimeSearch" type="text" class="easyui-datebox"></input>  
	  <a id="btn" href="javascript:search()" class="easyui-linkbutton" data-options="iconCls:'icon-search'">搜索</a>  
 <div>
   <a id="btn" href="javascript:openAddDialog()" class="easyui-linkbutton" data-options="iconCls:'icon-add'">添加</a>  
   <a id="btn" href="javascript:openUpdateDialog()" class="easyui-linkbutton" data-options="iconCls:'icon-edit'">修改</a>  
   <a id="btn" href="javascript:deletePayOut()" class="easyui-linkbutton" data-options="iconCls:'icon-remove'">删除</a>  
 </div>
 </div>
  <table id="payOutData" class="easyui-datagrid" style="width:400px;height:250px"   
        data-options="url:'payout?actionName=queryPayOutByCondition',toolbar:'#tb',fitColumns:true,fit:true,pagination:true">   
    <thead>   
        <tr>   
            <th data-options="field:'ck',checkbox:true"></th>
            <th data-options="field:'parentId',hidden:true">支出类型</th>
            <th data-options="field:'id',width:100">编码</th>   
            <th data-options="field:'outName',width:100">支出名称</th>   
            <th data-options="field:'outTypeId',width:100">支出类型</th> 
            <th data-options="field:'money',width:100">支出金额</th>
            <th data-options="field:'accountName',width:100">所属账户</th> 
            <th data-options="field:'remark',width:100">支出备注</th>
            <th data-options="field:'createTime',width:100">创建时间</th>
            <th data-options="field:'updateTime',width:100">修改时间</th>
        </tr>   
    </thead>   
</table>

<!-- 添加对话框 -->
<div id="add" class="easyui-dialog" title="添加收入" style="width:400px;height:300px;"   
        data-options="iconCls:'icon-add',resizable:true,modal:true,closed:true">   
     <form id="addForm">
         <table>
           <tr>
              <td>支出名称：</td>
              <td>
                <input id="name" class="easyui-validatebox" data-options="required:true" /> 
              </td>
           </tr>
           <tr>
              <td>支出类型：</td>
              <td>
                 <input id="parentType" class="easyui-combobox" /> 
              </td>
           </tr>
           <tr>
              <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
              <td>
                 <input id="sonType" class="easyui-combobox" /> 
              </td>
           </tr>
           <tr>
              <td>所属账户</td>
              <td>
                <input id="AccountId" class="easyui-combobox" />  
              </td>
           </tr>
           <tr>
              <td>支出金额</td>
              <td>
                <input id="money" type="text" class="easyui-numberbox" value="100" data-options="min:0,precision:2"></input>
              </td>
           </tr>
           <tr>
              <td>支出备注：</td>
              <td>
                <input id="remark" class="easyui-validatebox" data-options="required:true" /> 
              </td>
           </tr>
           <tr>
              <td>
                <a  href="javascript:addPayOut()" class="easyui-linkbutton" data-options="iconCls:'icon-add'">添加</a> 
              </td>
              <td>
              <a   href="javascript:closeAddDialog()" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">取消</a> 
              </td>
           </tr>
         </table>
     </form>
</div>  
<!-- 添加对话框 -->

<!-- 修改对话框 -->
<div id="update" class="easyui-dialog" title="修改收入" style="width:400px;height:300px;"   
        data-options="iconCls:'icon-edit',resizable:true,modal:true,closed:true">   
     <form id="updateForm">
		<input type="hidden" id="pId" name="pId" /> 
		<input type="hidden" id="datagridMoney" name="datagridMoney" />
	    <input type="hidden" id="datagridAccountId" name="datagridAccountId" />
			<table>
           <tr>
              <td>支出名称：</td>
              <td>
                <input id="name2" name="name2" class="easyui-validatebox" data-options="required:true" /> 
              </td>
           </tr>
           <tr>
              <td>支出类型：</td>
              <td>
                 <input id="parentType2" name="parentType2" class="easyui-combobox" /> 
              </td>
           </tr>
           <tr>
              <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
              <td>
                 <input id="sonType2" name="sonType2" class="easyui-combobox" /> 
              </td>
           </tr>
           <tr>
              <td>所属账户</td>
              <td>
             	<input id="formAccountId" name="formAccountId" class="easyui-combobox" />
             	  
              </td>
           </tr>
           <tr>
              <td>支出金额</td>
              <td>
                <input id="formMoney" name="formMoney" type="text" class="easyui-numberbox" value="100" data-options="min:0,precision:2"></input>
              </td>
           </tr>
           <tr>
              <td>支出备注：</td>
              <td>
                <input id="remark2" name="remark2" class="easyui-validatebox" data-options="required:true" /> 
              </td>
           </tr>
           <tr>
              <td>
                <a  href="javascript:updatePayOut()" class="easyui-linkbutton" data-options="iconCls:'icon-add'">添加</a> 
              </td>
              <td>
              <a   href="javascript:closeUpdateDialog()" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">取消</a> 
              </td>
           </tr>
         </table>
     </form>
</div>  

<!-- 修改对话框 -->
</body>
<script type="text/javascript" src="statics/js/payOut.js"></script>
</html>