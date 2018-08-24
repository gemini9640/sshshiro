<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@include file="/office/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>管理员授权</title>
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
						subMenu2 += "<li><span><div><input id='"+operators[subKey2]+"' onclick='queryPermissionsByRoles(this, \""+roleOperators[subKey].role.role+"\");queryPermissionsByOperators(this);' class='check_operator check_operator_"+roleOperators[subKey].role.role+"' type='checkbox'/>"+operators[subKey2]+"</div></span></li>";
					}
					subMenu += "<li><span><div><b>"+roleOperators[subKey].role.roleName+"</b></div></span>"+
							   "<ul>"+subMenu2+"</ul>"+
							   "</li>";
				}
				mainMenu += "<li><span><div><b>"+APP_operatorMenu[key].department+"</b></div></span>"+
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
			$("#div_modulePermissionMenuTree").append("<ul class='easyui-tree'>"+menu+"</ul>");
			$.parser.parse($("#div_modulePermissionMenuTree"));
		}
		
		function loadRoleModulePermissionMenu(roleModulePermissionMenu) {
			$("#div_rolePermissionMenuTree").html("");
			$(".check_permission").attr("disabled",false);
			var menu = "";
			for(var key in roleModulePermissionMenu) {
				var permissionsMenu = roleModulePermissionMenu[key].permissions;
				for(var key2 in permissionsMenu) {
					var modulePermissions = permissionsMenu[key2].permissions;
					var subMenu = "";
					for(var key3 in modulePermissions) {
						var permissions = modulePermissions[key3].permissions;
						var module = modulePermissions[key3].module;
						var subMenu2 ="";
						for(var key4 in permissions) {
							var homeMenu = "";
							if(permissions[key4].type =="MENU") {
								homeMenu = "(menu)";
							}
	 						subMenu2 += "<li><div>"+permissions[key4].permissionName+homeMenu+"</div></li>";
	 						$("#"+permissions[key4].permission).attr("disabled",true);
	 						$("#"+permissions[key4].permission).parent().parent().css("background","#b5f2a3");
						}
						subMenu += "<li><span><div><b>"+module+"</b></div></span>"+
									"<ul>"+subMenu2+"</ul>"+
									"</li>";
					}
					menu += "<li><span><div><b>"+permissionsMenu[key].role.roleName+"</b></div></span>"+
							"<ul>"+subMenu+"</ul>"+
							"</li>";
				}
			}
			$("#div_rolePermissionMenuTree").append("<ul class='easyui-tree'>"+menu+"</ul>");
			$.parser.parse($("#div_rolePermissionMenuTree"));
		}
		
		function queryPermissionsByOperators(obj) {
			$(".check_permission").parent().parent().css("background","white");
			if($(obj).prop("checked")) {
				$.post("${base}office/authr/queryPermissionsByOperators.do", {
					operators : $(obj).attr("id")
				}, function(data) {
					var permissions = $.parseJSON(data);
					autoCheckPermissionByIds(permissions);
				});
			} else {
				autoCheckPermissionByIds(null);
				$(".check_permission").prop("disabled", false);
			}
		}
		
		function queryPermissionsByRoles(obj, role) {
			if($(obj).prop("checked")) {
				$(".check_operator").each(function() {
					if($(this).attr("id") != $(obj).attr("id")) {
						$(this).prop("checked", false);
					}
				});
				$.post("${base}office/authr/queryFmtRolePermissionMenu.do", {
					roles : role
				}, function(data) {
					var permissions = $.parseJSON(data);
					var menuArr = new Array();
					menuArr.push(new RolePermissionMenu(role, permissions));
					loadRoleModulePermissionMenu(menuArr);
				});
			} else {
				$("#div_rolePermissionMenuTree").html("");
			}
		}
		
		function autoCheckPermissionByIds(permissions) {
			$(".check_permission").prop("checked", false);
			for(var key in permissions) {
				if(!$("#"+permissions[key]).prop("disabled")) {
					$("#"+permissions[key]).prop("checked", true);
				}
			}
		}
		
		function autoCheckPermissionByModule(obj, module) {
			if($(obj).prop("checked")) {
				$(".check_permission_"+module).each(function() {
					if(!$(this).prop("disabled")) {
						$(this).prop("checked", true);
					}
				}); 
			} else {
				$(".check_permission_"+module).prop("checked", false);
			}
		}
		
		function authorizOperatorPermission() {
			var operator = "";
			$(".check_operator").each(function() {
				if($(this).attr('checked')) {
					operator = $(this).attr('id');
				}
			});
			var permissions = new Array();
			$(".check_permission").each(function() {
				if($(this).attr('checked')) {
					permissions.push($(this).attr("id"));
				}
			});
			$.post("${base}office/authr/authorizOperatorPermission.do", {
				operators : operator,
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
		其他 --> 用户授权 <a href="javascript:history.back();"><font color="red">上一步</font></a>
	</div>
		
	<div>
		<input onclick="authorizOperatorPermission();" value="更新" type="button"/>
		<div class="easyui-layout" style=";width:700px;height:660px;">
			<div region="west" title="" style="overflow:auto; height: 100%;width:180px;">
				<h3>管理员列表</h3>
				<div id="div_operatorMenuTree"></div>
			</div>
			<div region="center" title="" style="overflow:auto; height: 100%;">
				<h3>管理员权限</h3>
				<div id="div_modulePermissionMenuTree"></div>
			</div>
			<div region="east" title="" style="overflow:auto; height: 100%;width:250px;">
				<h3>角色权限</h3>
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
	
	function RoleOperators(role, operator) {
		this.role = role;
		this.operator = operator;
	}
	
	function PermissionMenu(module, permissions) {
		this.module = module;
		this.permissions = permissions;
	}
	
	function RolePermissionMenu(role, permissions) {
		this.role = role;
		this.permissions = permissions;
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

