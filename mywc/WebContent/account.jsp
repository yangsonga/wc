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
			账户名称： <input id="aname" class="easyui-validatebox" /> 
			账户类型：
			 <select id="atype" class="easyui-combobox" style="width: 160px;"data-options="editable:false">
				<option value="">请选择账户类型</option>
				<option>招商</option>
				<option>工商</option>
				<option>建设</option>
				<option>农业</option>
			</select> 
		     创建时间：<input id="createTime" type="text" class="easyui-datebox" data-options="editable:false"></input> 
			<a href="javascript:searchAccount()" class="easyui-linkbutton" data-options="iconCls:'icon-search'" />搜索</a>
		</div>
		<a href="javascript:openAddDialog()" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" />添加</a>
		<a href="javascript:openUpdateDialog()"class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true" />修改</a>
		<a href="javascript:deleteAccount()"class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true" />删除</a>
		
	</div>

	<table id="dg" class="easyui-datagrid" style="width:400px;height:200px"   
        data-options="url:'/mywc/account?actionName=queryAccount',toolbar:'#tb',fit:true,rownumbers:true,pagination:true">   
    <thead>   
        <tr>   
            <th data-options="field:'ck',checkbox:true"></th>  
            <th data-options="field:'id',width:100">编号</th>   
            <th data-options="field:'accountName',width:100">账户名称</th>   
            <th data-options="field:'accountType',width:100">账户类型</th>  
            <th data-options="field:'money',width:100">账户金额</th>  
            <th data-options="field:'remark',width:300">账户备注</th> 
            <th data-options="field:'createTime',width:200">创建时间</th>
            <th data-options="field:'updateTime',width:200">修改时间</th>
        </tr>   
    </thead>   
</table>  
  <!-- 添加账户 -->
  <div id="addDialog" class="easyui-dialog" title="添加账户" style="width:400px;height:250px;"   
        data-options="iconCls:'icon-add',resizable:true,modal:true,closed:true">   
      <form id="addForm" style="margin-top: 20px">
        <table align="center">
           <tr>
              <td>账户名称：</td>
              <td>
               <input class="easyui-validatebox" id="accountName" data-options="required:true,missingMessage:'请输入账户名'" />
              </td>
           </tr>
           
            <tr>
              <td>账户类型：</td>
              <td>
                <select id="accountType" class="easyui-combobox" style="width:160px;" data-options="editable:false">   
                  <option value="">请选择账户类型</option>   
                  <option>招商</option>   
                  <option>工商</option>   
                  <option>建设</option>   
                  <option>农业</option>   
               </select>  
                
              </td>
           </tr>
           
            <tr>
              <td>账户金额：</td>
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
              <a href="javascript:addAccount()" class="easyui-linkbutton" data-options="iconCls:'icon-ok'" />保存</a>
              </td>
              <td style="padding-right: 20px">
              <a href="javascript:closeAddDialog()" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" />取消</a>
              </td>
           </tr>
        
        </table>
      </form>
</div>  
<!-- 添加账户 -->

<!-- 修改账户 -->
<div id="updateDialog" class="easyui-dialog" title="修改账户" style="width:400px;height:250px;"   
        data-options="iconCls:'icon-edit',resizable:true,modal:true,closed:true">   
      <form id="updateForm" style="margin-top: 20px">
         <input type="hidden" id="accountId" name="accountId"/> <!-- account.js通过openUpdateDialog()传入accountId -->
        <table align="center">
           <tr>
              <td>账户名称：</td>
              <td>
               <input class="easyui-validatebox" id="accountName2" name="accountName2" data-options="required:true,missingMessage:'请输入账户名'" />
              </td>
           </tr>
           
            <tr>
              <td>账户类型：</td>
              <td>
                <select id="accountType2" name="accountType2" class="easyui-combobox" style="width:160px;" data-options="editable:false">   
                  <option value="">请选择账户类型</option>   
                  <option>招商</option>   
                  <option>工商</option>   
                  <option>建设</option>   
                  <option>农业</option>   
               </select>  
                
              </td>
           </tr>
           
            <tr>
              <td>账户金额：</td>
              <td>
                <input type="text" id="money2" name="money2" class="easyui-numberbox" value="100" data-options="min:0,precision:2"></input>  
              </td>
           </tr>
           
            <tr>
              <td>账户备注</td>
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
              <a href="javascript:updateAccount()" class="easyui-linkbutton" data-options="iconCls:'icon-ok'" />保存</a>
              </td>
              <td style="padding-right: 20px">
              <a href="javascript:closeUpdateDialog()" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" />取消</a>
              </td>
           </tr>
        
        </table>
      </form>
</div>  
<!-- 修改账户 -->

</body>
<script type="text/javascript" src="statics/js/account.js"></script>
</html>