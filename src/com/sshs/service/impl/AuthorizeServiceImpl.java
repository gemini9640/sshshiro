package com.sshs.service.impl;

import java.util.List;

import com.sshs.common.Constants;
import com.sshs.dao.AuthorizeDao;
import com.sshs.pojo.*;
import com.sshs.service.AuthorizeService;
import com.sshs.util.AuthrUtil;
import com.sshs.vo.AuthrFmtDepartmentMenu;
import com.sshs.vo.AuthrFmtPermissionMenu;
import com.sshs.vo.AuthrFmtRoleMenu;
import com.sshs.vo.AuthrFmtRolePermissionMenu;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service("authorizeService")
public class AuthorizeServiceImpl implements AuthorizeService {

	private static Logger log = Logger.getLogger(AuthorizeServiceImpl.class);
	@Autowired
	private AuthorizeDao authorizeDao;
	
	public AuthrRole addRole(String operator, String role, String department, String roleName) {
		AuthrRole aRole = authorizeDao.saveRole(role, department, roleName);
		//logs
		return aRole;
	}
	
	public AuthrRole editRole(String operator, String role, String department, String roleName) {
		AuthrRole aRole = authorizeDao.updateRole(role, department, roleName);
		//logs
		return aRole;
	}
	
	public AuthrPermmission addMenuPermission(String operator, String permission, String module, String permissionName, String menuName, Integer menuIndex, String parentMenuId, String url, String remark) {
		AuthrPermmission aPermmission = authorizeDao.saveMenuPermmission(permission, module, permissionName, menuName, menuIndex, parentMenuId, url, remark);
		//log
		return aPermmission;
	}
	
	public AuthrPermmission addPermission(String operator, String permission, String module, String permissionName, String url, String remark) {
		AuthrPermmission aPermission = authorizeDao.savePermmission(permission, module, permissionName, url, remark);
		//log
		return aPermission;
	}
	
	public AuthrPermmission editMenuPermission(String operator, String permission, String module, String permissionName, String menuName, Integer menuIndex, String parentMenuId, String url, String remark) {
		AuthrPermmission aPermmission = authorizeDao.updateMenuPermmission(permission, module, permissionName, menuName, menuIndex, parentMenuId, url, remark);
		//log
		return aPermmission;
	}
	
	public AuthrPermmission editPermission(String operator, String permission, String module, String permissionName, String url, String remark) {
		AuthrPermmission aPermission = authorizeDao.updatePermmission(permission, module, permissionName, url, remark);
		//log
		return aPermission;
	}
	
	public AuthrPermmission enableDisablePermission(String operator, String permission, Integer flag) {
		AuthrPermmission aPermission = authorizeDao.enableDisablePermission(permission, flag);
		//log
		return aPermission;
	}
	
	
	public List<AuthrRolePermmission> authorizRolePermission(String operator, List<String> roles, List<String> permissions) {
		List<AuthrRolePermmission> list = authorizeDao.updateRolePermmission(roles, permissions);
		//log
		log.info(operator+" authorize roles[" + roles + "] => permissions["+permissions+"]");
		return list;
	}
	
	public List<AuthrOperatorPermmission> authorizOperatorPermission(String operator, List<String> targetOperators, List<String> permissions) {
		List<AuthrOperatorPermmission> list = authorizeDao.updateOperatorPermmission(targetOperators, permissions);
		//log
		return list;
	}
	
	public List<AuthrPermmission> queryPermissions(AuthrUtil.PermissionType type, String permission, String module) {
		List<AuthrPermmission> permissions = authorizeDao.queryPrmissionSelective(AuthrUtil.string2List(permission), AuthrUtil.string2List(module), type, null);
		AuthrUtil.sortPermissionsByMenuIndex(permissions, AuthrUtil.SortMode.ASC);
		return permissions;
	}
	
	public List<AuthrRole> queryRoles() {
		return authorizeDao.queryRoleSelective(null, null);
	}

	public List<AuthrRolePermmission> queryRolePermissionSelective(List<String> roles, List<String> permission) {
		return authorizeDao.queryRolePermissionSelective(null, permission);
	}
	
//	public List<String> queryPermissionsExcludeRole(String role) {
//		List<AuthrRolePermmission> rolePermissions = authorizeDao.queryRolePermissionExcludeSelective(AuthrUtil.string2List(role),null);
//		return AuthrUtil.extractPermissionsByRole(rolePermissions);
//	}
	
	public List<String> queryPermissionsByRoles(List<String> roles) {
		List<AuthrRolePermmission> rolePermissions = authorizeDao.queryRolePermissionSelective(roles, null);
		return AuthrUtil.extractPermissionsByRole(rolePermissions);
	}
	
	public List<String> queryPermissionsByOperatorRole(List<String> operators) {
		List<AuthrOperatorRole> operatorRoles = authorizeDao.queryOperatorRoleSelective(operators, null);
		List<AuthrRolePermmission> rolePermissions = authorizeDao.queryRolePermissionSelective(AuthrUtil.extractRoles(operatorRoles), null);
		return AuthrUtil.extractPermissionsByRole(rolePermissions);
	}

	public List<String> queryPermissionsByOperators(List<String> operators) {
		List<AuthrOperatorPermmission> results = authorizeDao.queryOperatorPermissionSelective(operators, null);
		return AuthrUtil.extractPermissionsByOperator(results);
	}
	
	public List<AuthrFmtDepartmentMenu> organizeOperatorByRoleDepartment() {
		List<AuthrOperatorRole> operatorRoleList = authorizeDao.queryOperatorRoleSelective(null, null);
		List<AuthrRole> roleList = authorizeDao.queryRoleSelective(null, null);
		return AuthrUtil.organizeOperatorByRoleDepartment(operatorRoleList, roleList);
	}
	
	public List<AuthrFmtRoleMenu> organizeRoleByDepartment() {
		List<AuthrRole> roles = authorizeDao.queryRoleSelective(null, null);
		return AuthrUtil.organizeRoleByDepartment(roles);
	}
	
	public List<AuthrFmtPermissionMenu> organizePermissionByModule() {
		List<AuthrPermmission> permissions = authorizeDao.queryPrmissionSelective(null, null, null, null); 
		return AuthrUtil.organizePermissionByModule(permissions);
	}
	
	public List<AuthrFmtRolePermissionMenu> organizePermissionByModuleRole(List<String> roles) {
		List<AuthrRolePermmission> rolePermmissions = authorizeDao.queryRolePermissionSelective(roles, null);
		List<String> ids = AuthrUtil.extractPermissionsByRole(rolePermmissions);
		List<AuthrPermmission> permissions = authorizeDao.queryPrmissionSelective(ids, null, null, null);
		List<AuthrRole> authrRoles = authorizeDao.queryRoleSelective(null, roles);
		return AuthrUtil.organizePermissionByModuleRole(rolePermmissions, permissions, authrRoles);
	}
	
	public List<AuthrFmtPermissionMenu> organizeHomeMenu(Operator operator) {
		List<AuthrOperatorPermmission> operatorPermmissions = authorizeDao.queryOperatorPermissionSelective(AuthrUtil.string2List(operator.getUsername()), null);
		List<AuthrRolePermmission> rolePermmissions = authorizeDao.queryRolePermissionSelective(AuthrUtil.string2List(operator.getRole()), null);
		Integer flag = Constants.ENABLE;
		if(StringUtils.equals("admin", operator.getRole()))
			flag = null;
		List<AuthrPermmission> permissions = authorizeDao.queryPrmissionSelective(null, null, null, flag);
		return AuthrUtil.organizeHomeMenu(rolePermmissions, operatorPermmissions, permissions);
	}
	
}