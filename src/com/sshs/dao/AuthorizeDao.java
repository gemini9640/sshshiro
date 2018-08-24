package com.sshs.dao;

import java.util.ArrayList;
import java.util.List;

import com.sshs.common.Constants;
import com.sshs.pojo.*;
import com.sshs.util.AuthrUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;


@Repository
public class AuthorizeDao extends UniversalDao {
	private static Logger log = Logger.getLogger(AuthorizeDao.class);

	public List<String> queryDepartments() {
		DetachedCriteria dc = DetachedCriteria.forClass(AuthrRole.class);
		dc.setProjection(Projections.groupProperty("department"));
		return findByCriteria(dc);
	}
	
	public List<String> queryPermissionModules() {
		DetachedCriteria dc = DetachedCriteria.forClass(AuthrPermmission.class);
		dc.setProjection(Projections.groupProperty("module"));
		return findByCriteria(dc);
	}
	
	public List<AuthrRole> queryRoleSelective(List<String> departments, List<String> roleIds) {
		DetachedCriteria dc = DetachedCriteria.forClass(AuthrRole.class);
		if(departments != null && !departments.isEmpty())
			dc.add(Restrictions.in("department", departments));
		if(roleIds != null && !roleIds.isEmpty())
			dc.add(Restrictions.in("role", roleIds));
		return findByCriteria(dc);
	}
	
	public List<AuthrPermmission> queryPrmissionSelective(List<String> permissions, List<String> modules, AuthrUtil.PermissionType type, Integer flag) {
		DetachedCriteria dc = DetachedCriteria.forClass(AuthrPermmission.class);
		if(permissions != null && !permissions.isEmpty())
			dc.add(Restrictions.in("permission", permissions));
		if(modules != null && !modules.isEmpty())
			dc.add(Restrictions.in("module", modules));
		if(flag != null)
			dc.add(Restrictions.eq("flag", flag));
		if(type != null)
			dc.add(Restrictions.eq("type", type.getCode()));
		return findByCriteria(dc);
	}
	
	public AuthrOperatorRole saveOperatorRole(String operator, String newRole) {
		List<AuthrOperatorRole> list = saveOperatorRole(AuthrUtil.string2List(operator), AuthrUtil.string2List(newRole));
		return list.size() > 0 ? list.get(0) : null;
	}
	
	//兼容多对多关联
	public List<AuthrOperatorRole> saveOperatorRole(List<String> operators, List<String> newRoles) {
		List<AuthrOperatorRole> list = new ArrayList<AuthrOperatorRole>();
		for (String operator : operators) {
			for (String newRole : newRoles) {
				AuthrOperatorRoleId id = new AuthrOperatorRoleId(operator, newRole);
				AuthrOperatorRole authrOperatorRole = (AuthrOperatorRole) get(AuthrOperatorRole.class, id);
				if(authrOperatorRole == null) {
					authrOperatorRole = new AuthrOperatorRole(id);
					list.add(authrOperatorRole);
					save(authrOperatorRole);
				} else 
					log.info(authrOperatorRole + "[save]:"+operator+"=>"+newRole+" is already exist ...");
			}
		}
		return list;
	}
	
	public void deleteOperatorRole(String operator, String oldRole) {
		deleteOperatorRole(AuthrUtil.string2List(operator), AuthrUtil.string2List(oldRole));
	}
	
	public void deleteOperatorRole(List<String> operators, List<String> roles) {
		if(operators == null || operators.isEmpty())
			return;
		DetachedCriteria dc = DetachedCriteria.forClass(AuthrOperatorRole.class);
		dc.add(Restrictions.in("id.operator", operators));
		if(roles!=null && !roles.isEmpty()) 
			dc.add(Restrictions.in("id.role", roles));
		deleteAll(findByCriteria(dc));
	}
	
//	public List<AuthrOperatorRole> updateOperatorRole(List<String> operators, List<String> newRoles) {
//		deleteOperatorRole(operators, newRoles);
//		return saveOperatorRole(operators, newRoles);
//	}
	
	public AuthrOperatorPermmission saveOperatorPermmission(String operator, String newPermission) {
		List<AuthrOperatorPermmission> list = saveOperatorPermmission(AuthrUtil.string2List(operator), AuthrUtil.string2List(newPermission));
		return list.size() > 0 ? list.get(0) : null;
	}
	
	public List<AuthrOperatorPermmission> saveOperatorPermmission(List<String> operators, List<String> newPermissions) {
		List<AuthrOperatorPermmission> list = new ArrayList<AuthrOperatorPermmission>();
		for (String operator : operators) {
			for (String newPermission: newPermissions) {
				AuthrOperatorPermmissionId id = new AuthrOperatorPermmissionId(operator, newPermission);
				AuthrOperatorPermmission authrOperatorPermmission = (AuthrOperatorPermmission) get(AuthrOperatorPermmission.class, id);
				if(authrOperatorPermmission == null) {
					authrOperatorPermmission = new AuthrOperatorPermmission(id);
					save(authrOperatorPermmission);
					list.add(authrOperatorPermmission);
				} else 
					log.info(authrOperatorPermmission+"[save]:"+operator+"=>"+newPermission+" is already exist ...");
			}
		}
		return list;
	}
	
	public void deleteOperatorPermmission(String operator, String oldPermission) {
		deleteOperatorPermmission(AuthrUtil.string2List(operator), AuthrUtil.string2List(oldPermission));
	}
	
	public void deleteOperatorPermmission(List<String> operators, List<String> oldPermissions) {
		if(operators == null || operators.isEmpty())
			return;
		DetachedCriteria dc = DetachedCriteria.forClass(AuthrOperatorPermmission.class);
		dc.add(Restrictions.in("id.operator", operators));
		if(oldPermissions!=null && !oldPermissions.isEmpty()) 
			dc.add(Restrictions.in("id.permission", oldPermissions));
		deleteAll(findByCriteria(dc));
	}
	
	public List<AuthrOperatorPermmission> updateOperatorPermmission(List<String> operators, List<String> newPermissions) {
		deleteOperatorPermmission(operators, null);
		return saveOperatorPermmission(operators, newPermissions);
	}
	
	public List<AuthrOperatorPermmission> queryOperatorPermmission(String operator, String permission) {
		DetachedCriteria dc = DetachedCriteria.forClass(AuthrOperatorPermmission.class);
		if(StringUtils.isNotEmpty(operator))
			dc.add(Restrictions.eq("id.operator", operator));
		if(StringUtils.isNotEmpty(permission)) 
			dc.add(Restrictions.eq("id.permission", permission));
		return findByCriteria(dc);
	}
		
	public AuthrRolePermmission saveRolePermmission(String role, String newPermmission) {
		List<AuthrRolePermmission> list = saveRolePermmission(AuthrUtil.string2List(role), AuthrUtil.string2List(newPermmission));
		return list.size() > 0 ? list.get(0) : null;
	}
	
	public List<AuthrRolePermmission> saveRolePermmission(List<String> roles, List<String> newPermmissions) {
		if(newPermmissions != null && !newPermmissions.isEmpty()) {
			List<String> operators = AuthrUtil.extractOperators(queryOperatorRoleSelective(null, roles));
			deleteOperatorPermmission(operators, newPermmissions);
		}
		List<AuthrRolePermmission> list = new ArrayList<AuthrRolePermmission>();
		for (String role : roles) {
			for (String newPermmission : newPermmissions) {
				AuthrRolePermmissionId id = new AuthrRolePermmissionId(role, newPermmission);
				AuthrRolePermmission authrRolePermmission = (AuthrRolePermmission) get(AuthrRolePermmission.class, id);
				if(authrRolePermmission == null) {
					authrRolePermmission = new AuthrRolePermmission(id);
					save(authrRolePermmission);
					list.add(authrRolePermmission);
				}else 
					log.info(authrRolePermmission+"[save]:"+role+"=>"+newPermmission+" is already exist ...");
			}
		}
		return list;
	}
	
	public void deleteRolePermmission(String role, String oldPermmission) {
		deleteRolePermmission(AuthrUtil.string2List(role), AuthrUtil.string2List(oldPermmission));
	}
	
	public void deleteRolePermmission(List<String> roles, List<String> oldPermissions) {
		if(roles == null || roles.isEmpty())
			return;
		DetachedCriteria dc = DetachedCriteria.forClass(AuthrRolePermmission.class);
		dc.add(Restrictions.in("id.role", roles));
		if(oldPermissions!=null && !oldPermissions.isEmpty()) 
			dc.add(Restrictions.in("id.permission", oldPermissions));
		deleteAll(findByCriteria(dc));
	}
	
	public List<AuthrRolePermmission> updateRolePermmission(List<String> roles, List<String> newPermmissions) {
		deleteRolePermmission(roles, null);
		return saveRolePermmission(roles, newPermmissions);
	}
	
	public AuthrPermmission saveMenuPermmission(String permission, String module, String permissionName, String menuName,
			Integer menuIndex, String parentMenuId, String url, String remark) {
		AuthrPermmission authrPermmission = new AuthrPermmission(permission, AuthrUtil.PermissionType.MENU.getCode(), module, permissionName, menuName, menuIndex, parentMenuId, url, remark, Constants.DISABLE);
		save(authrPermmission);
		return authrPermmission; 
	}
	
	public AuthrPermmission savePermmission(String permission, String module, String permissionName, String url, String remark) {
		AuthrPermmission authrPermmission = new AuthrPermmission(permission, AuthrUtil.PermissionType.ACTION.getCode(), module, permissionName, null, 999, null, url, remark, Constants.DISABLE);
		save(authrPermmission);
		return authrPermmission;
	}
	
	public AuthrPermmission updatePermmission(String permission, String module, String permissionName, String url, String remark) {
		AuthrPermmission authrPermmission = (AuthrPermmission) get(AuthrPermmission.class, permission);
		if(authrPermmission == null)
			return null;
		authrPermmission.setType(AuthrUtil.PermissionType.ACTION.getCode());
		if(module!=null)
			authrPermmission.setModule(module);
		if(permissionName!=null)
			authrPermmission.setPermissionName(permissionName);
		authrPermmission.setMenuName(null);
		authrPermmission.setMenuIndex(999);
		authrPermmission.setParentMenuId(null);
		if(url!=null)
			authrPermmission.setUrl(url);
		if(remark!=null)
			authrPermmission.setRemark(remark);
		update(authrPermmission);
		return authrPermmission;
	}
	
	public AuthrPermmission updateMenuPermmission(String permission, String module, String permissionName, String menuName, 
			Integer menuIndex, String parentMenuId, String url, String remark) {
		AuthrPermmission authrPermmission = (AuthrPermmission) get(AuthrPermmission.class, permission);
		if(authrPermmission == null)
			return null;
		authrPermmission.setType(AuthrUtil.PermissionType.MENU.getCode());
		if(module!=null)
			authrPermmission.setModule(module);
		if(permissionName!=null)
			authrPermmission.setPermissionName(permissionName);
		if(menuName!=null)
			authrPermmission.setMenuName(menuName);
		if(menuIndex!=null)
			authrPermmission.setMenuIndex(menuIndex);
		if(parentMenuId!=null)
			authrPermmission.setParentMenuId(parentMenuId);
		if(url!=null)
			authrPermmission.setUrl(url);
		if(remark!=null)
			authrPermmission.setRemark(remark);
		update(authrPermmission);
		return authrPermmission;
	}
	
	public AuthrPermmission enableDisablePermission(String permission, Integer flag) {
		AuthrPermmission authrPermmission = (AuthrPermmission) get(AuthrPermmission.class, permission);
		if(authrPermmission == null)
			return null;
		authrPermmission.setFlag(flag);
		return authrPermmission;
	}
	
	public AuthrRole updateRole(String role, String department, String roleName) {
		AuthrRole authrRole = (AuthrRole) get(AuthrRole.class, role);
		if(authrRole == null) 
			return null;
		if(department !=null)
			authrRole.setDepartment(department);
		if(roleName != null)
			authrRole.setRoleName(roleName);
		update(authrRole);
		return authrRole;
	}
	
	public AuthrRole saveRole(String role, String department, String roleName) {
		AuthrRole authrRole = new AuthrRole(role, department, roleName);
		save(authrRole);
		return authrRole;
	}
	
	public List<AuthrRolePermmission> queryRolePermissionSelective(List<String> roles, List<String> permissions) {
		DetachedCriteria dc = DetachedCriteria.forClass(AuthrRolePermmission.class);
		if(roles != null && !roles.isEmpty()) 
			dc.add(Restrictions.in("id.role", roles));
		if(permissions != null && !permissions.isEmpty()) 
			dc.add(Restrictions.in("id.permission", permissions));
		return findByCriteria(dc);
	}
	
	public List<AuthrRolePermmission> queryRolePermissionExcludeSelective(List<String> roles, List<String> permissions) {
		DetachedCriteria dc = DetachedCriteria.forClass(AuthrRolePermmission.class);
		if(roles != null && !roles.isEmpty()) 
			dc.add(Restrictions.not(Restrictions.in("id.role", roles)));
		if(permissions != null && !permissions.isEmpty()) 
			dc.add(Restrictions.not(Restrictions.in("id.permission", permissions)));
		return findByCriteria(dc);
	}

	public List<AuthrOperatorRole> queryOperatorRoleSelective(List<String> operators, List<String> roles) {
		DetachedCriteria dc = DetachedCriteria.forClass(AuthrOperatorRole.class);
		if(operators != null && !operators.isEmpty())
			dc.add(Restrictions.in("id.operator", operators));
		if(roles != null && !roles.isEmpty())
			dc.add(Restrictions.in("id.role", roles));
		return findByCriteria(dc);
	}
	
	public List<AuthrOperatorPermmission> queryOperatorPermissionSelective(List<String> operators, List<String> permissions) {
		DetachedCriteria dc = DetachedCriteria.forClass(AuthrOperatorPermmission.class);
		if(operators != null && !operators.isEmpty())
			dc.add(Restrictions.in("id.operator", operators));
		if(permissions != null && !permissions.isEmpty())
			dc.add(Restrictions.in("id.permission", permissions));
		return findByCriteria(dc);
	}
}
