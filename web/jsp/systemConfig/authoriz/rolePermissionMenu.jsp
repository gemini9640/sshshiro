<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@include file="/office/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>角色授权</title>
	<link rel="stylesheet" type="text/css" href="<c:url value='/office/themes/default/easyui.css'/>" />
	<link rel="stylesheet" type="text/css" href="<c:url value='/office/themes/icon.css'/>" />
	<link rel="stylesheet" type="text/css" href="<c:url value='/office/themes/demo.css'/>" />
	<style>
	</style>
	<script type="text/javascript" src="<c:url value='/office/themes/jquery-1.7.1.min.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/office/themes/jquery.easyui.min.js'/>"></script>
	<script>
		var APP_operatorMenuJSON = ${requestScope.fmtOperatorMenuJSON};
		var APP_PermissionMenuJSON = ${requestScope.fmtPermissionMenuJSON};
		var APP_operatorMenu = new Array();
		var APP_permissionMenu = new Array();

		function initOperatorMenu() {
			for(var key in APP_operatorMenuJSON) {
				var department = APP_operatorMenuJSON[key].department;
				var roleOperators = APP_operatorMenuJSON[key].roleOperator;
				var roleOperatorsArr = new Array();
				for(var roleKey in roleOperators) {
					var role =  roleOperators[roleKey].role;
					var operators =  roleOperators[roleKey].operator;
					roleOperatorsArr.push(new RoleOperators(role, operators));
				}
				APP_operatorMenu.push(new OperatorMenu(department, roleOperatorsArr));
			}
		}
		
		function initPermissionMenu() {
			for(var key in APP_PermissionMenuJSON) {
				var module = APP_PermissionMenuJSON[key].module;
				var permissions = APP_PermissionMenuJSON[key].permissions;
				var permissionArr = new Array();
				for(var subKey in permissions) {
					var permission = permissions[subKey];
					permissionArr.push(permission);
				}
				APP_permissionMenu.push(new PermissionMenu(module, permissionArr));
			}
		}
		
		function loadOperatorMenuTree() {
			var mainMenu = "";
			for(var key in APP_operatorMenu) {
				var subMenu = ""
				var roleOperators = APP_operatorMenu[key].roleOperators;
				for(var subKey in roleOperators) {
					var subMenu2 = "";
					var operators = roleOperators[subKey].operator
					for(var subKey2 in operators) {
						subMenu2 += "<li><span><div>"+operators[subKey2]+"</div></span></li>";
					}
					subMenu += "<li><span><div><input id='"+roleOperators[subKey].role.role+"' onclick='queryPermissionsByRoles();' type='checkbox' class='check_role check_role_"+APP_operatorMenu[key].department+"'/><b>"+roleOperators[subKey].role.roleName+"</b></div></span>"+
							   "<ul>"+subMenu2+"</ul>"+
							   "</li>";
				}
				mainMenu += "<li><span><div><input id='"+APP_operatorMenu[key].department+"' onclick='queryPermissionsByDeparmentRole(this, \""+APP_operatorMenu[key].department+"\");' class='' type='checkbox'/><b>"+APP_operatorMenu[key].department+"</b></div></span>"+
							"<ul>"+subMenu+"</ul></li>";
			}
			$("#div_operatorMenuTree").append("<ul class='easyui-tree'>"+mainMenu+"</ul>");
			$.parser.parse($("#div_operatorMenuTree"));
		}
		
		function loadPermissionMenuTree() {
			var menu = "";
			for(var key in APP_permissionMenu) {
				var permissions = APP_permissionMenu[key];
				var module = permissions.module;
				var permissions = permissions.permissions;
				var subMenu = "";
				for(var subKey in permissions) {
					var homeMenu = "";
					if(permissions[subKey].type =="MENU") {
						homeMenu = "(menu)";
					}
					subMenu += "<li><div><input id='"+permissions[subKey].permission+"' type='checkbox' class='check_permission check_permission_"+module+"'/>"+permissions[subKey].permissionName+homeMenu+"</div></li>";
				}
				menu += "<li><span><div><b><input onclick='autoCheckPermissionByModule(this, \""+module+"\");' type='checkbox' class='check_module'/>"+module+"</b></div></span>"+
						"<ul>"+subMenu+"</ul>"+
						"</li>";
			}
			$("#div_rolePermissionMenuTree").append("<ul class='easyui-tree'>"+menu+"</ul>");
			$.parser.parse($("#div_rolePermissionMenuTree"));
		}
		
		function queryPermissionsByDeparmentRole(obj, department) {
			if($(obj).prop("checked")) {
				$(".check_role_"+department).prop("checked", true);
			} else {
				$(".check_role_"+department).prop("checked", false);
			}
			queryPermissionsByRoles();
		}
		
		function queryPermissionsByRoles() {
			var rolse = new Array();
			$(".check_role").each(function() {
				if($(this).attr('checked')) {
					rolse.push($(this).attr("id"));
				}
			});
			$.post("${base}office/authr/queryPermissionsByRoles.do", {
				roles : rolse.toString()
			}, function(data) {
				var permissions = $.parseJSON(data);
				autoCheckPermissionByIds(permissions);
			});
		}

		function autoCheckRole(checkDepartment, departmentId) {
			if(checkDepartment) {
				$(".check_role_"+departmentId).prop("checked", true);
			} else {
				$(".check_role_"+departmentId).prop("checked", false);
			}
		}
		
		function autoCheckPermissionByIds(permissions) {
			$(".check_permission").prop("checked", false);
			for(var key in permissions) {
				$("#"+permissions[key]).prop("checked", true);
			}
		}
		
		function autoCheckPermissionByModule(obj, module) {
			if($(obj).prop("checked")) {
				$(".check_permission_"+module).prop("checked", true);
			} else {
				$(".check_permission_"+module).prop("checked", false);
			}
		}
		
		
		function authorizRolePermission() {
			var roles = new Array();
			var permissions = new Array();
			$(".check_role").each(function() {
				if($(this).attr('checked')) {
					roles.push($(this).attr("id"));
				}
			});
			$(".check_permission").each(function() {
				if($(this).attr('checked')) {
					permissions.push($(this).attr("id"));
				}
			});
			$.post("${base}office/authr/authorizRolePermission.do", {
				roles : roles.toString(),
				permissions : permissions.toString()
			}, function(data) {
				var permissions = $.parseJSON(data);
				alert(permissions.length+" permissions updated");
			});
		}
	</script>
</head>
<body>
	<div id="excel_menu_left">
		其他 --> 角色授权 <a href="javascript:history.back();"><font color="red">上一步</font></a>
	</div>
		
	<div>
		<input onclick="authorizRolePermission();" value="更新" type="button"/>
		<div class="easyui-layout" style=";width:600px;height:660px;">
			<div region="west" title="" style="overflow:auto; height: 100%;width:180px;">
				<div id="div_operatorMenuTree"></div>
			</div>
			<div region="center" title="" style="overflow:auto; height: 100%;">
				<div id="div_rolePermissionMenuTree"></div>
			</div>
		</div>
	</div>
	<br/>
	<br/>
	<br/>
	<br/>
	<c:import url="/office/script.jsp" />
	<script>
	
	function OperatorMenu(department, roleOperators) {
		this.department = department;
		this.roleOperators = roleOperators;
	}
	
	function PermissionMenu(module, permissions) {
		this.module = module;
		this.permissions = permissions;
	}
	
	function RoleMenu(department, roles) {
		this.department = department;
		this.roles = roles;
	}
	
	function RoleOperators(role, operator) {
		this.role = role;
		this.operator = operator;
	}
	
	$(document).ready(function() {
		initOperatorMenu();
		initPermissionMenu();
		loadOperatorMenuTree();
		loadPermissionMenuTree();
	});
	</script>
</body>
</html>

