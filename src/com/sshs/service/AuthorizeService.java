package com.sshs.service;

import com.sshs.pojo.*;
import com.sshs.util.AuthrUtil;
import com.sshs.vo.AuthrFmtDepartmentMenu;
import com.sshs.vo.AuthrFmtPermissionMenu;
import com.sshs.vo.AuthrFmtRoleMenu;
import com.sshs.vo.AuthrFmtRolePermissionMenu;

import java.util.List;



public interface AuthorizeService {
	public AuthrRole addRole(String operator, String role, String department, String roleName);
	
	public AuthrPermmission addMenuPermission(String operator, String permission, String module, String permissionName, String menuName, Integer menuIndex, String parentMenuId, String url, String remark);
	
	public AuthrPermmission addPermission(String operator, String permission, String module, String permissionName, String url, String remark);
	
	public AuthrPermmission editMenuPermission(String operator, String permission, String module, String permissionName, String menuName, Integer menuIndex, String parentMenuId, String url, String remark);
	
	public AuthrPermmission editPermission(String operator, String permission, String module, String permissionName, String url, String remark);
	
	public AuthrPermmission enableDisablePermission(String operator, String permission, Integer flag);
	
	public AuthrRole editRole(String operator, String role, String department, String roleName);
	
	public List<AuthrOperatorPermmission> authorizOperatorPermission(String operator, List<String> targetOperators, List<String> permissions);
	
	public List<AuthrRolePermmission> authorizRolePermission(String operator, List<String> roles, List<String> permissions);
	
	public List<AuthrPermmission> queryPermissions(AuthrUtil.PermissionType type, String permission, String module);
	
	public List<AuthrRole> queryRoles();
	
	public List<AuthrRolePermmission> queryRolePermissionSelective(List<String> roles, List<String> permission);
	
	public List<String> queryPermissionsByRoles(List<String> roles);
	
	public List<String> queryPermissionsByOperatorRole(List<String> operators);
	
	public List<AuthrFmtDepartmentMenu> organizeOperatorByRoleDepartment();
	
	public List<AuthrFmtPermissionMenu> organizePermissionByModule();
	
	public List<AuthrFmtRoleMenu> organizeRoleByDepartment();
	
	public List<AuthrFmtRolePermissionMenu> organizePermissionByModuleRole(List<String> roles);
	
	public List<AuthrFmtPermissionMenu> organizeHomeMenu(Operator operator);
	
	public List<String> queryPermissionsByOperators(List<String> operators);
}
