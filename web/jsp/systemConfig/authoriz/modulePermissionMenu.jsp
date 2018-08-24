<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@include file="/jsp/common/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>权限管理</title>
	<style type="text/css">
		.title {font-size: 13px; color: #FFFFFF; font-weight: bold;}
		.row {font-size: 13px;}
		.a_enable {color: green;}
		.a_disable {color:red;}
		.a_edit {color:blue;}
	</style>
	<script>
		var APP_permissionTable = ${requestScope.fmtPermissionMenuJSON};
		var APP_permissionModule = ${requestScope.permissionModuleJSON};
		
		function loadPermissionTable(list) {
			$("#table_modulePermission tr").each(function() {
				if(!$(this).hasClass("title")) {
					$(this).remove();
				}
			});
			var tr = "";
			for(var key in list) {
				var permission = list[key];
				tr+= loadPermissionDetail(permission);
			}
			$("#table_modulePermission").append(tr);
		}
		
		function loadPermissionDetail(permission) {
			var operation = "";
			var bgcolor = "";
			bgcolor = "#e4f2ff";
			operation += "<span id='span_edit"+permission.permission+"'><a onclick='editPermission("+JSON.stringify(permission)+");' href='###' class='a_edit'>修改</a></span>";
			
			return  "<tr id='tr_"+permission.permission+"' bgcolor='"+bgcolor+"' align='center' class='row'>"+
					"<td>"+permission.permission+"</td>"+
					"<td id='td_name"+permission.permission+"'>"+permission.permissionName+"</td>"+
					"<td id='td_type"+permission.permission+"'>"+permission.type+"</td>"+
					"<td id='td_module"+permission.permission+"'>"+permission.module+"</td>"+
					"<td id='td_menuName"+permission.permission+"'>"+permission.menuName+"</td>"+
					"<td id='td_menuIndex"+permission.permission+"'>"+permission.menuIndex+"</td>"+
					"<td id='td_url"+permission.permission+"'>"+permission.url+"</td>"+
					"<td id='td_remark"+permission.permission+"'>"+permission.remark+"</td>"+
					"<td id='td_operation"+permission.permission+"'>"+operation+"</td>"+
					"</tr>";
		}
		
		function editPermission(permission) {
			var id = permission.permission;
			var bgcolor = "";
			bgcolor = "#e4f2ff";
			var tr = "<tr id='tr_"+id+"' bgcolor='"+bgcolor+"' align='center' class='row'>"+
						"<td>"+id+"</td>"+
						"<td id='td_name"+id+"'><input id='edt_permissionName"+id+"' type='text' value='"+$("#td_name"+id).html()+"'/></td>"+
						"<td id='td_type"+id+"'>"+getTypeOption(id, $("#td_type"+id).html())+"</td>"+
						"<td id='td_module"+id+"'><input id='edt_module"+id+"' type='text' value='"+$("#td_module"+id).html()+"'/></td>"+
						"<td id='td_menuName"+id+"'><input id='edt_menuName"+id+"' type='text' value='"+$("#td_menuName"+id).html()+"'/></td>"+
						"<td id='td_menuIndex"+id+"'>"+getIndexOption(id, $("#td_menuIndex"+id).html())+"</td>"+
						"<td id='td_url"+id+"'><input id='edt_url"+id+"' type='text' value='"+$("#td_url"+id).html()+"'/></td>"+
						"<td id='td_remark"+id+"'><textarea id='edt_remark"+id+"' type='text'>"+$("#td_remark"+id).html()+"</textarea></td>"+
						"<td id='td_operation"+id+"'>"+$("#td_operation"+id).html()+"</td>"+
						"</tr>";
			$("#tr_"+id).replaceWith(tr);
			$("#span_edit"+id).html("<a onclick='confirmEditPermission(\""+id+"\");' class='a_edit' href='###'>确定</a> | <a onclick='cancelEdtPermission("+JSON.stringify(permission)+");' class='a_edit' href='###'>取消</a>");
			edtTypeChange(id);
		}
		
		function getTypeOption(permission, val) {
			var id = "edt_type"+ permission;
			var menuSelected = "";
			var actionSelected = "";
			if(val == "MENU") {
				menuSelected = "selected";
			} else if(val == "ACTION") {
				actionSelected = "selected";
			} 
			return "<select id='"+id+"' onchange='edtTypeChange(\""+permission+"\");''>"+
						"<option "+menuSelected+">MENU</option>"+
						"<option "+actionSelected+">ACTION</option>"+
					"</select>";
		}
		
		function getIndexOption(permission, val) {
			var id = "edt_menuIndex"+permission;
			var option = "";
			for(var i = 1; i<=100; i++) {
				option += "<option "+(val==i?"selected":"")+">"+i+"</option>";
			}
			return "<select id='"+id+"' >"+option+"</select>";
		}
		
		function edtTypeChange(permission) {
			if($("#edt_type"+permission).val() == "MENU") {
				$("#edt_menuName"+permission).prop("disabled", false);
				$("#edt_menuIndex"+permission).prop("disabled", false);
				$("#edt_menuName"+permission).css("background","white");
				$("#edt_menuIndex"+permission).css("background","white");
			} else if($("#edt_type"+permission).val() == "ACTION") {
				$("#edt_menuName"+permission).prop("disabled", true);
				$("#edt_menuIndex"+permission).prop("disabled", true);
				$("#edt_menuName"+permission).css("background-color","#D4D0C8");
				$("#edt_menuIndex"+permission).css("background-color","#D4D0C8");
			}
		}
		
		function typeChange() {
			if($("#type").val() == "MENU") {
				$("#menuName").prop("disabled", false);
				$("#menuIndex").prop("disabled", false);
				$("#menuName").css("background","white");
				$("#menuIndex").css("background","white");
			} else if($("#type").val() == "ACTION") {
				$("#menuName").prop("disabled", true);
				$("#menuIndex").prop("disabled", true);
				$("#menuName").css("background-color","#D4D0C8");
				$("#menuIndex").css("background-color","#D4D0C8");
			}
		}
		
		function cancelEdtPermission(permission) {
			$("#tr_"+permission.permission).replaceWith(loadPermissionDetail(permission));
		}

		function enableOrDisable(id, flag) {
			$.post("${base}office/authr/enableOrDisablePermission.do", {
				permission : id,
				flag : flag
			}, function(data) {
				var result = $.parseJSON(data);
				if(result.isSuccess == "0") {
					var permission = result.data;
					$("#tr_"+permission.permission).replaceWith(loadPermissionDetail(permission));
				}
				alert(result.resultMsg);
			});
		}
		
		function confirmEditPermission(id) {
			$.post("${base}admin/authr/editPermission.do", {
				permission : id,
				permissionName : $("#edt_permissionName"+id).val(),
				type : $("#edt_type"+id).val(),
				module : $("#edt_module"+id).val(),
				menuName : $("#edt_menuName"+id).val(),
				menuIndex : $("#edt_menuIndex"+id).val(),
				url : $("#edt_url"+id).val(),
				remark : $("#edt_remark"+id).val()
			}, function(data) {
				var result = $.parseJSON(data);
				if(result.isSuccess == "0") {
					var permission = result.data;
					$("#tr_"+permission.permission).replaceWith(loadPermissionDetail(permission));
				} 
				alert(result.resultMsg);
			});
		}
		
		function addPermission() {
			var module = $("#module").val();
			var isNewModule = true;
			$(".module_opt").each(function() {
				console.log($(this).val());
				if($(this).val() == module) {
					isNewModule = !isNewModule;
				}
			});
			if(isNewModule) {
				$("#nav_module").append("<option>"+module+"</option>");
			}
			$("#nav_module").val(module);
			$.post("${base}admin/authr/addPermission.do", {
				permission : $.trim($("#permission").val()),
				permissionName : $("#permissionName").val(),
				type : $("#type").val(),
				module : module,
				menuName : $("#menuName").val(),
				menuIndex : $("#menuIndex").val(),
				url : $("#url").val(),
				remark : $("#remark").val()
			}, function(data) {
				var result = $.parseJSON(data);
				if(result.isSuccess == "0") {
					queryModulePermissionDetail();
					$("#form_reset").click();
				}
				alert(result.resultMsg);
			});
		}
		
		function queryModulePermissionDetail() {
			$.post("${base}admin/authr/queryModulePermissiondeDetail.do", {
				permission : $("#nav_permission").val(),
				type : $("#nav_type").val(),
				module : $("#nav_module").val(),
			}, function(data) {
				loadPermissionTable($.parseJSON(data));
			});
		}
		
	</script>
</head>
<body>
	<div id="excel_menu_left">
		其他 --> 权限管理 <a href="javascript:history.back();"><font color="red">上一步</font></a>
	</div>
		
	<div>
	类型：
	<select id="nav_type">
		<option></option>
		<option>MENU</option>
		<option>ACTION</option>
	</select>
	权限ID：
	<input id="nav_permission" type="text"/>
	模块：
	<select id="nav_module">
	</select>
	<input type="button" onclick="queryModulePermissionDetail();" value="查询"/>
		<table id="table_modulePermission" width="100%" border="0" align="center" cellpadding="0" cellspacing="1" bgcolor="#99c8d7">
			<tr bgcolor="#0084ff" align="center" class="title">
				<td>权限ID</td>
				<td>权限名称</td>
				<td>类型</td>
				<td>模块</td>
				<td>目录名称</td>
				<td>目录顺序</td>
				<td>url</td>
				<td>描述</td>
				<td>操作</td>
			</tr>
		</table>
			<h3>添加一个权限</h3>
			<form>
			<table>
				<tr>
					<td>权限ID</td><td><input id="permission" type="text"/></td>
				</tr>
				<tr>
					<td>权限名称</td><td><input id="permissionName" type="text"/></td>
				</tr>
				<tr>
					<td>类型</td>
					<td>
						<select id="type" onchange="typeChange();">
							<option>MENU</option>
							<option>ACTION</option>
						</select>
				</tr>
				<tr>
					<td>模块</td><td><input id="module" type="text"/></td>
				</tr>
				<tr>
					<td>目录名称</td><td><input id="menuName" type="text"/></td>
				</tr>
				<tr>
					<td>目录顺序</td><td><select id='menuIndex' ></select></td>
				</tr>
				<tr>
					<td>URL</td><td><input id="url" type="text"/></td>
				</tr>
				<tr>
					<td>描述</td><td><textarea id="remark"></textarea></td>
				</tr>
				<tr>
					<td><input id="form_reset" type="reset"/></td><td><input type="button" onclick="addPermission();" value="提交"/></td>
				</tr>
			</table>
			</form>
	</div>
	<br/>
	<br/>
	<br/>
	<br/>
	<script>
	function loadIndexOpt() {
		var option = "";
		for(var i = 1; i<=100; i++) {
			option += "<option>"+i+"</option>";
		}
		$("#menuIndex").html(option);
	}
	
	function loadModulOpt() {
		var option = "";
		for(var key in APP_permissionModule) {
			option += "<option class='module_opt'>"+APP_permissionModule[key]+"</option>";
		}
		$("#nav_module").html(option);
	}
	
	$(document).ready(function() {
		loadPermissionTable(APP_permissionTable);
		loadIndexOpt();
		loadModulOpt();
	});
	
	</script>
</body>
</html>

