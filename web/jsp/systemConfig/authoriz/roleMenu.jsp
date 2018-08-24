<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@include file="/office/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>权限管理</title>
	<link rel="stylesheet" type="text/css" href="<c:url value='/office/themes/default/easyui.css'/>" />
	<link rel="stylesheet" type="text/css" href="<c:url value='/office/themes/icon.css'/>" />
	<link rel="stylesheet" type="text/css" href="<c:url value='/office/themes/demo.css'/>" />
	<link href="<c:url value='/office/css/excel.css' />" rel="stylesheet" type="text/css" />
	<style type="text/css">
		.title {font-size: 13px; color: #FFFFFF; font-weight: bold;}
		.row {font-size: 13px;}
		.a_enable {color: green;}
		.a_disable {color:red;}
		.a_edit {color:blue;}
	</style>
	<script type="text/javascript" src="<c:url value='/office/themes/jquery-1.7.1.min.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/office/themes/jquery.easyui.min.js'/>"></script>
	<script>
		var APP_roleTable = ${requestScope.fmtRoleMenuJSON};
		
		function loadRoleTable() {
			var tr = "";
			for(var key in APP_roleTable) {
				var aRole = APP_roleTable[key];
				tr+= loadRoleDetail(aRole);
			}
			$("#table_role").append(tr);
		}
		
		function loadRoleDetail(aRole) {
			var operation = " <span id='span_edit"+aRole.role+"'><a onclick='editRole("+JSON.stringify(aRole)+");' href='###' class='a_edit'>修改</a></span>";
			
			return  "<tr id='tr_"+aRole.role+"' bgcolor='#e4f2ff' align='center' class='row'>"+
					"<td>"+aRole.role+"</td>"+
					"<td id='td_department"+aRole.role+"'>"+aRole.department+"</td>"+
					"<td id='td_roleName"+aRole.role+"'>"+aRole.roleName+"</td>"+
					"<td id='td_operation"+aRole.role+"'>"+operation+"</td>"+
					"</tr>";
		}
		
		function editRole(aRole) {
			var id = aRole.role;
			var tr = "<tr id='tr_"+id+"' bgcolor='#e4f2ff' align='center' class='row'>"+	
						"<td>"+id+"</td>"+
						"<td id='td_department"+id+"'><input id='edt_department"+id+"' type='text' value='"+$("#td_department"+id).html()+"'/></td>"+
						"<td id='td_roleName"+id+"'><input id='edt_roleName"+id+"' type='text' value='"+$("#td_roleName"+id).html()+"'/></td>"+
						"<td id='td_operation"+id+"'>"+$("#td_operation"+id).html()+"</td>"+
						"</tr>";
			$("#tr_"+id).replaceWith(tr);
			$("#span_edit"+id).html("<a onclick='confirmEditRole(\""+id+"\");' class='a_edit' href='###'>确定</a> | <a onclick='cancelEdtRole("+JSON.stringify(aRole)+");' class='a_edit' href='###'>取消</a>");
		}
		
		function cancelEdtRole(aRole) {
			$("#tr_"+aRole.role).replaceWith(loadRoleDetail(aRole));
		}

		function confirmEditRole(id) {
			$.post("${base}office/authr/editRole.do", {
				role : id,
				department : $("#edt_department"+id).val(),
				roleName : $("#edt_roleName"+id).val()
			}, function(data) {
				var result = $.parseJSON(data);
				if(result.isSuccess == "0") {
					var aRole = result.data;
					$("#tr_"+aRole.role).replaceWith(loadRoleDetail(aRole));
				} 
				alert(result.resultMsg);
			});
		}
		
		function addRole() {
			$.post("${base}office/authr/addRole.do", {
				role : $("#role").val(),
				department : $("#department").val(),
				roleName : $("#roleName").val()
			}, function(data) {
				var result = $.parseJSON(data);
				if(result.isSuccess == "0") {
					window.location.href = "${base}office/authr/loadRolesMenu.do";
				}
				alert(result.resultMsg);
			});
		}
		
		
	</script>
</head>
<body>
	<div id="excel_menu_left">
		其他 --> 权限管理 <a href="javascript:history.back();"><font color="red">上一步</font></a>
	</div>
		
	<div>
		<table id="table_role" width="100%" border="0" align="center" cellpadding="0" cellspacing="1" bgcolor="#99c8d7">
			<tr bgcolor="#0084ff" align="center" class="title">
				<td>角色ID</td>
				<td>部门</td>
				<td>角色名称</td>
				<td>操作</td>
			</tr>
		</table>
			<h3>添加一个角色</h3>
			<table>
				<tr>
					<td>角色ID</td><td><input id="role" type="text"/></td>
				</tr>
				<tr>
					<td>部门</td><td><input id="department" type="text"/></td>
				</tr>
				<tr>
					<td>角色名称</td><td><input id="roleName" type="text"/></td>
				</tr>
				<tr>
					<td></td><td><input type="button" onclick="addRole();" value="提交"/></td>
				</tr>
			</table>
	</div>
	<br/>
	<br/>
	<br/>
	<br/>
	<c:import url="/office/script.jsp" />
	<script>
	$(document).ready(function() {
		loadRoleTable();
	});
	
	</script>
</body>
</html>

