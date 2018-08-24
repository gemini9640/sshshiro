package com.sshs.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import com.sshs.common.Constants;
import com.sshs.pojo.*;
import com.sshs.service.AuthorizeService;
import com.sshs.shiro.ShiroFilterFactory;
import com.sshs.shiro.ShiroUtil;
import com.sshs.util.AuthrUtil;
import com.sshs.vo.AuthrAuthorizationInfo;
import com.sshs.vo.AuthrFmtRolePermissionMenu;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;


@Controller
@Scope("prototype")
public class ConfigAuthrAction extends SubActionSupport {
	private static final long serialVersionUID = 6209026707573350123L;
	private static Logger log = Logger.getLogger(ConfigAuthrAction.class);
	@Autowired
	private AuthorizeService authorizeService;
	@Autowired
	private ShiroFilterFactory shiroFilterFactory;
	
	//无权限访问
	public void permissionDeny() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("isSuccess", AuthrUtil.AuthrConts.DENY);
		map.put("resultMsg", "无权限访问");
		responseWriter(JSONObject.fromObject(map).toString());
	}	
	//无权限访问
	public void autheticationFail() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("isSuccess", AuthrUtil.AuthrConts.LOGIN_TIMEOUT);
		map.put("resultMsg", "登录超时请重新登录logintimeoutplsrelogin");
		responseWriter(JSONObject.fromObject(map).toString());
	}	
	//更新shiro权限缓存
	public void reloadFilterChainDefinitions() throws Exception {
		try {
			ShiroUtil.reloadFilterChainDefinitions(shiroFilterFactory);
			getServltContext().setAttribute("OperatorRole", authorizeService.queryRoles());
			//只更新当前在线用户的权限缓存
			for (Operator operator : AuthrUtil.AuthrConts.HOMEMENU.keySet())
				AuthrUtil.AuthrConts.HOMEMENU.put(operator, authorizeService.organizeHomeMenu(operator));
			for (String operator : AuthrUtil.AuthrConts.AUTHORIZATION_INFO.keySet()) {
			    AuthrAuthorizationInfo authrAuthorizationInfo = AuthrUtil.AuthrConts.AUTHORIZATION_INFO.get(operator);
			    if(authrAuthorizationInfo != null) {
			    	List<String> permissions = authorizeService.queryPermissionsByOperators(AuthrUtil.string2List(operator));
				    authrAuthorizationInfo.setPermission(new HashSet<String>(permissions));
			    }
			}
			responseWriter("更新权限缓存成功");
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
			throw e;
		}
	}
			
	// 添加权限
	public void addPermission() {
		AuthrPermmission authrPermmission = null;
		String resultMsg = null;
		if(StringUtils.isEmpty(permission) || StringUtils.isEmpty(type) || StringUtils.isEmpty(module) || StringUtils.isEmpty(permissionName) || StringUtils.isEmpty(url))
			resultMsg = "添加失败，参数错误";
		else if(StringUtils.equals(type, AuthrUtil.PermissionType.MENU.getCode())) {
			if(StringUtils.isEmpty(menuName) || menuIndex == null)
				resultMsg = "添加失败，参数错误";
			else
				authrPermmission = authorizeService.addMenuPermission(getOperatorLoginname(), permission, module, permissionName, menuName, menuIndex, null, url, remark);
		} else if(StringUtils.equals(type, AuthrUtil.PermissionType.ACTION.getCode()))
			authrPermmission = authorizeService.addPermission(getOperatorLoginname(), permission, module, permissionName, url, remark);
		

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("isSuccess", Constants.FAIL);
		if(resultMsg == null && authrPermmission == null)
			map.put("resultMsg", "添加失败");
		else if(resultMsg != null && authrPermmission == null)
			map.put("resultMsg", resultMsg);
		else {
			map.put("resultMsg", "添加成功");
			map.put("data", authrPermmission);
			map.put("isSuccess", Constants.SUCCESS);
		}
		responseWriter(JSONObject.fromObject(map).toString());
	}
	
	//修改权限内容
	public void editPermission() {
		AuthrPermmission authrPermmission = null;
		String resultMsg = null;
		if(StringUtils.isEmpty(permission) || StringUtils.isEmpty(type) || StringUtils.isEmpty(module) || StringUtils.isEmpty(permissionName) || StringUtils.isEmpty(url))
			resultMsg = "添加失败，参数错误";
		else if(StringUtils.equals(type, AuthrUtil.PermissionType.MENU.getCode())) {
			if(StringUtils.isEmpty(menuName) || menuIndex == null)
				resultMsg = "添加失败，参数错误";
			else
				authrPermmission = authorizeService.editMenuPermission(getOperatorLoginname(), permission, module, permissionName, menuName, menuIndex, null, url, remark);
		} else if(StringUtils.equals(type, AuthrUtil.PermissionType.ACTION.getCode()))
			authrPermmission = authorizeService.editPermission(getOperatorLoginname(), permission, module, permissionName, url, remark);
		

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("isSuccess", Constants.FAIL);
		if(resultMsg == null && authrPermmission == null)
			map.put("resultMsg", "添加失败");
		else if(resultMsg != null && authrPermmission == null)
			map.put("resultMsg", resultMsg);
		else {
			map.put("resultMsg", "添加成功");
			map.put("data", authrPermmission);
			map.put("isSuccess", Constants.SUCCESS);
		}	
		responseWriter(JSONObject.fromObject(map).toString());
	}
	
	public void enableOrDisablePermission() {
		AuthrPermmission authrPermmission = null;
		String resultMsg = null;
		if(StringUtils.isEmpty(permission) || flag == null) 
			resultMsg = "操作失败，参数错误";
		else
			authrPermmission = authorizeService.enableDisablePermission(getOperatorLoginname(), permission, flag);
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("isSuccess", Constants.FAIL);
		if(resultMsg == null && authrPermmission == null)
			map.put("resultMsg", "操作失败");
		else if(resultMsg != null && authrPermmission == null)
			map.put("resultMsg", resultMsg);
		else {
			map.put("resultMsg", "操作成功");
			map.put("data", authrPermmission);
			map.put("isSuccess", Constants.SUCCESS);
		}	
		responseWriter(JSONObject.fromObject(map).toString());
	}
	
	public void queryPermissionsByOperators() {
		List<String> operatorList = AuthrUtil.string2List(operators);
		List<String> permissions;
		if(operatorList.size() > 0)
			permissions = authorizeService.queryPermissionsByOperators(operatorList);
		else 
			permissions = new ArrayList<>();
		responseWriter(JSONArray.fromObject(permissions).toString());
	}
	
	public void queryPermissionsByOperatorRole() {
		List<String> operatorList = AuthrUtil.string2List(operators);
		List<String> permissions;
		if(operatorList.size() > 0)
			permissions = authorizeService.queryPermissionsByOperatorRole(operatorList);
		else 
			permissions = new ArrayList<String>();
		responseWriter(JSONArray.fromObject(permissions).toString());
	}
	
	public void queryPermissionsByRoles() {
		List<String> roleList = AuthrUtil.string2List(roles);
		List<String> permissions;
		if(roleList.size()>0)
			permissions = authorizeService.queryPermissionsByRoles(roleList);
		else 
			permissions = new ArrayList<String>();
		responseWriter(JSONArray.fromObject(permissions).toString());
	}
	
	//按角色查找权限列表
	public void queryFmtRolePermissionMenu() {
		List<String> roleList = AuthrUtil.string2List(roles);
		List<AuthrFmtRolePermissionMenu> permissions;
		if (roleList.size()>0)
			permissions = authorizeService.organizePermissionByModuleRole(roleList);
		else 
			permissions = new ArrayList<>();
		responseWriter(JSONArray.fromObject(permissions).toString());
	}
	
	//加载用户授权列表
	public String loadOperatorPermissionMenu() {
		return loadRolePermissionMenu();
	}
	
	//加载角色授权列表
	public String loadRolePermissionMenu() {
		setFmtOperatorMenuJSON(JSONArray.fromObject(authorizeService.organizeOperatorByRoleDepartment()).toString());
		setFmtPermissionMenuJSON(JSONArray.fromObject(authorizeService.organizePermissionByModule()).toString());
		return INPUT;
	}
	
	//加载模块权限列表
	public String loadModulePermissionMenu() {
		List<AuthrPermmission> permissions = authorizeService.queryPermissions(AuthrUtil.PermissionType.getEnum(type), permission, module);
		setFmtPermissionMenuJSON(JSONArray.fromObject(permissions).toString());
		setPermissionModuleJSON(JSONArray.fromObject(AuthrUtil.extractPermissionModule(permissions)).toString());
		return INPUT;
	}
	
	//加载模块权限列表ajax
	public void queryModulePermissiondeDetail() {
		List<AuthrPermmission> permissions = authorizeService.queryPermissions(AuthrUtil.PermissionType.getEnum(type), permission, module);
		responseWriter(JSONArray.fromObject(permissions).toString());
	}
	
	//加载角色列表
	public String loadRolesMenu() {
		setFmtRoleMenuJSON(JSONArray.fromObject(authorizeService.queryRoles()).toString());
		return INPUT;
	}
	
	//添加一个角色
	public void addRole() {
		AuthrRole aRole = null;
		String resultMsg = null;
		if(StringUtils.isEmpty(role) || StringUtils.isEmpty(department) || StringUtils.isEmpty(roleName)) 
			resultMsg = "操作失败，参数错误";
		else
			aRole = authorizeService.addRole(getOperatorLoginname(), role, department, roleName);
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("isSuccess", Constants.FAIL);
		if(resultMsg == null && aRole == null)
			map.put("resultMsg", "操作失败");
		else if(resultMsg != null && aRole == null)
			map.put("resultMsg", resultMsg);
		else {
			map.put("resultMsg", "操作成功");
			map.put("data", aRole);
			map.put("isSuccess", Constants.SUCCESS);
		}	
		responseWriter(JSONObject.fromObject(map).toString());
	}
	
	//修改一个角色
	public void editRole() {
		AuthrRole aRole = null;
		String resultMsg = null;
		if(StringUtils.isEmpty(role) || StringUtils.isEmpty(department) || StringUtils.isEmpty(roleName)) 
			resultMsg = "操作失败，参数错误";
		else
			aRole = authorizeService.editRole(getOperatorLoginname(), role, department, roleName);
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("isSuccess", Constants.FAIL);
		if(resultMsg == null && aRole == null)
			map.put("resultMsg", "操作失败");
		else if(resultMsg != null && aRole == null)
			map.put("resultMsg", resultMsg);
		else {
			map.put("resultMsg", "操作成功");
			map.put("data", aRole);
			map.put("isSuccess", Constants.SUCCESS);
		}	
		responseWriter(JSONObject.fromObject(map).toString());
	}
	
	// 角色授权
	public void authorizRolePermission() {
		List<String> roleList = AuthrUtil.string2List(roles);
		List<String> permissionList = AuthrUtil.string2List(permissions);
		List<AuthrRolePermmission> result = authorizeService.authorizRolePermission(getOperatorLoginname(), roleList, permissionList);
		responseWriter(JSONArray.fromObject(result).toString());
	}
	
	// 用户授权
	public void authorizOperatorPermission() {
		List<String> permissionList = AuthrUtil.string2List(permissions);
		List<String> operatorList = AuthrUtil.string2List(operators);
		List<AuthrOperatorPermmission> result = authorizeService.authorizOperatorPermission(getOperatorLoginname(), operatorList, permissionList);
		responseWriter(JSONArray.fromObject(result).toString());
	}

	private String operators; 
	private String role;
	private String roles;
	private String roleName;
	private String department;
	private String type;
	private String permission;
	private String permissions;
	private String remark;
	private String module;
	private String permissionName;
	private String menuName;
	private Integer menuIndex;
	private String parentMenuId;
	private String url;
	private Integer flag; 
	private String fmtOperatorMenuJSON;
	private String fmtPermissionMenuJSON;
	private String fmtRoleMenuJSON;
	private String fmtRolePermissionMenuJSON;
	private String permissionModuleJSON;
	
	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
	
	public String getRoles() {
		return roles;
	}

	public void setRoles(String roles) {
		this.roles = roles;
	}
	
	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

	public String getPermissions() {
		return permissions;
	}

	public void setPermissions(String permissions) {
		this.permissions = permissions;
	}
	
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getFmtOperatorMenuJSON() {
		return fmtOperatorMenuJSON;
	}

	public void setFmtOperatorMenuJSON(String fmtOperatorMenuJSON) {
		this.fmtOperatorMenuJSON = fmtOperatorMenuJSON;
	}

	public String getFmtPermissionMenuJSON() {
		return fmtPermissionMenuJSON;
	}

	public void setFmtPermissionMenuJSON(String fmtPermissionMenuJSON) {
		this.fmtPermissionMenuJSON = fmtPermissionMenuJSON;
	}

	public String getFmtRoleMenuJSON() {
		return fmtRoleMenuJSON;
	}

	public void setFmtRoleMenuJSON(String fmtRoleMenuJSON) {
		this.fmtRoleMenuJSON = fmtRoleMenuJSON;
	}

	public String getOperators() {
		return operators;
	}

	public void setOperators(String operators) {
		this.operators = operators;
	}

	public String getFmtRolePermissionMenuJSON() {
		return fmtRolePermissionMenuJSON;
	}

	public void setFmtRolePermissionMenuJSON(String fmtRolePermissionMenuJSON) {
		this.fmtRolePermissionMenuJSON = fmtRolePermissionMenuJSON;
	}
	
	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public String getPermissionName() {
		return permissionName;
	}

	public void setPermissionName(String permissionName) {
		this.permissionName = permissionName;
	}

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public Integer getMenuIndex() {
		return menuIndex;
	}

	public void setMenuIndex(Integer menuIndex) {
		this.menuIndex = menuIndex;
	}

	public String getParentMenuId() {
		return parentMenuId;
	}

	public void setParentMenuId(String parentMenuId) {
		this.parentMenuId = parentMenuId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getPermissionModuleJSON() {
		return permissionModuleJSON;
	}

	public void setPermissionModuleJSON(String permissionModuleJSON) {
		this.permissionModuleJSON = permissionModuleJSON;
	}


}
