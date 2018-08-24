package com.sshs.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.sshs.pojo.*;
import com.sshs.vo.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;


public class AuthrUtil {
	private static Logger log = Logger.getLogger(AuthrUtil.class);
	
	
	public static List<String> extractOperators(List<AuthrOperatorRole> operatorRoles) {
		Set<String> results = new HashSet<String>();
		for (AuthrOperatorRole operatorRole : operatorRoles) 
			results.add(operatorRole.getId().getOperator());
		return new ArrayList<String>(results);
	}
	
	public static List<String> extractRoles(List<AuthrOperatorRole> operatorRoles) {
		Set<String> results = new HashSet<String>();
		for (AuthrOperatorRole operatorRole : operatorRoles) 
			results.add(operatorRole.getId().getRole());
		return new ArrayList<String>(results);
	}
	
	public static List<String> extractPermissionsByRole(List<AuthrRolePermmission> rolePermmissions) {
		List<String> results = new ArrayList<String>();
		for (AuthrRolePermmission rolePermmission : rolePermmissions)
			results.add(rolePermmission.getId().getPermmission());
		return results;
	}
	
	public static List<String> extractPermissionsByOperator(List<AuthrOperatorPermmission> operatorPermmissions) {
		List<String> results = new ArrayList<String>();
		for (AuthrOperatorPermmission authrOperatorPermmission : operatorPermmissions)
			results.add(authrOperatorPermmission.getId().getPermission());
		return results;
	}
	
	public static Set<String> extractPermissionModule(List<AuthrPermmission> permissions) {
		Set<String> results = new HashSet<String>();
		for (AuthrPermmission authrPermmission : permissions)
			results.add(authrPermmission.getModule());
		return results;
	}

	public static List<String> string2List(String s) {
		if(StringUtils.isEmpty(s))
			return new ArrayList<String>();
		return Arrays.asList(s.split(","));
	}
	
	public static Set<String> string2Set(String s) {
		if(StringUtils.isEmpty(s))
			return new HashSet<String>();
		return new HashSet<String>(Arrays.asList(s.split(",")));
	}
	
	public static void sortPermissionsByMenuIndex(List<AuthrPermmission> list, final SortMode sortMode) {
		Collections.sort(list, new Comparator<AuthrPermmission>(){
            public int compare(AuthrPermmission a1, AuthrPermmission a2) {
            	if(a1.getMenuIndex() == null || a2.getMenuIndex() == null)
    				return 0;
            	switch (sortMode) {
				case ASC :
					return a1.getMenuIndex() - a2.getMenuIndex();  //升序
				case DESC :
					return a2.getMenuIndex() - a1.getMenuIndex();  //降序
				default:
					return 0;
				} 
            }
        });
	}

	public static  List<AuthrFmtDepartmentMenu> organizeOperatorByRoleDepartment(List<AuthrOperatorRole> operatorRoles, List<AuthrRole> roles) {
		Map<AuthrRole, List<String>> organizeOperatorByRole = organizeOperatorByRole(operatorRoles, roles);
		return organizeOperatorByRoleDepartment(organizeOperatorByRole);
	}

	public static List<AuthrFmtDepartmentMenu> organizeOperatorByRoleDepartment(Map<AuthrRole, List<String>> operatorMap) {
		Map<String, AuthrFmtDepartmentMenu> departmentMenuMap = new LinkedHashMap<String, AuthrFmtDepartmentMenu>();
		for (AuthrRole role : operatorMap.keySet()) {
			AuthrFmtDepartmentMenu departmentMenu = departmentMenuMap.get(role.getDepartment());
			AuthrFmtRoleOperatorMenu roleOperator = new AuthrFmtRoleOperatorMenu(role ,operatorMap.get(role));
			List<AuthrFmtRoleOperatorMenu> roleOperators;
			if (departmentMenu == null) {
				roleOperators = new ArrayList<AuthrFmtRoleOperatorMenu>(); 
				AuthrFmtDepartmentMenu department = new AuthrFmtDepartmentMenu(role.getDepartment(), roleOperators);
				departmentMenuMap.put(role.getDepartment(), department);
			} else {
				AuthrFmtDepartmentMenu department = departmentMenuMap.get(role.getDepartment());
				roleOperators = department.getRoleOperator();
			}
			roleOperators.add(roleOperator);
		}
		return new ArrayList<AuthrFmtDepartmentMenu>(departmentMenuMap.values());
	}
	
	public static Map<AuthrRole, List<String>> organizeOperatorByRole(List<AuthrOperatorRole> operatorRoles, List<AuthrRole> roles) {
		Map<AuthrRole, List<String>> operatorMap = new LinkedHashMap<AuthrRole, List<String>>();
		for (AuthrRole role : roles) {
			boolean noOperatoRole = true;
			for (AuthrOperatorRole operatorRole : operatorRoles) {
				if(StringUtils.equals(role.getRole(), operatorRole.getId().getRole())) {
					String operator = operatorRole.getId().getOperator();
					List<String> operators;
					if(operatorMap.containsKey(role)) 
						operators = operatorMap.get(role);
					else {
						operators = new ArrayList<String>();
						operatorMap.put(role, operators);
					}
					operators.add(operator);
					noOperatoRole = false;
				}
			}
			if(noOperatoRole) 
				operatorMap.put(role, new ArrayList<String>());
		}
		return operatorMap;
	}
	
	public static List<AuthrFmtRoleMenu> organizeRoleByDepartment(List<AuthrRole> roles) {
		Map<String, AuthrFmtRoleMenu> roleMap = new LinkedHashMap<String, AuthrFmtRoleMenu>();
		for (AuthrRole role : roles) {
			AuthrFmtRoleMenu fmtRoles = roleMap.get(role.getDepartment());
			List<AuthrRole> optantRoles;
			if(fmtRoles == null) {
				optantRoles = new ArrayList<AuthrRole>();
				fmtRoles = new AuthrFmtRoleMenu(role.getDepartment(), optantRoles);
				roleMap.put(role.getDepartment(), fmtRoles);
			} else
				optantRoles = fmtRoles.getRoles();
			optantRoles.add(role);
		}
		return new ArrayList<AuthrFmtRoleMenu>(roleMap.values());
	}
	
	public static List<AuthrFmtPermissionMenu> organizePermissionByModule(List<AuthrPermmission> permissions) {
		Map<String, AuthrFmtPermissionMenu> permissionMap = new LinkedHashMap<String, AuthrFmtPermissionMenu>();
		for (AuthrPermmission permission : permissions) {
			AuthrFmtPermissionMenu fmtPermmission = permissionMap.get(permission.getModule());
			List<AuthrPermmission> optantPermissions;
			if(fmtPermmission == null) {
				optantPermissions = new ArrayList<AuthrPermmission>();
				fmtPermmission = new AuthrFmtPermissionMenu(permission.getModule(), optantPermissions);
				permissionMap.put(permission.getModule(), fmtPermmission);
			} else
				optantPermissions = fmtPermmission.getPermissions();
			optantPermissions.add(permission);
		}
		for (String module : permissionMap.keySet()) {
			AuthrFmtPermissionMenu menu = permissionMap.get(module);
			List<AuthrPermmission> list = menu.getPermissions();
			if(list!=null)
				sortPermissionsByMenuIndex(list, SortMode.ASC);
		}
		return new ArrayList<AuthrFmtPermissionMenu>(permissionMap.values());
	}
	
	public static List<AuthrFmtRolePermissionMenu> organizePermissionByModuleRole(List<AuthrRolePermmission> rolePermmissions, List<AuthrPermmission> permissions, List<AuthrRole> authrRoles) {
		Map<String, List<AuthrPermmission>> rolePermissionMap = new LinkedHashMap<String, List<AuthrPermmission>>();
		for (AuthrRolePermmission authrRolePermmission : rolePermmissions) {
			for (AuthrPermmission permission : permissions) {
				if(StringUtils.equals(authrRolePermmission.getId().getPermmission(), permission.getPermission())) {
					List<AuthrPermmission> optantPermissions = rolePermissionMap.get(authrRolePermmission.getId().getRole());
					if(optantPermissions == null) {
						optantPermissions = new ArrayList<AuthrPermmission>();
						rolePermissionMap.put(authrRolePermmission.getId().getRole(), optantPermissions);
					} 
					optantPermissions.add(permission);
				}
			}
		}
		List<AuthrFmtRolePermissionMenu> result = new ArrayList<AuthrFmtRolePermissionMenu>(); 
		for (String role : rolePermissionMap.keySet()) {
			for (AuthrRole authrRole : authrRoles) {
				if(StringUtils.equals(role, authrRole.getRole()))
					result.add(new AuthrFmtRolePermissionMenu(authrRole, organizePermissionByModule(rolePermissionMap.get(role))));
			}
		}	
		return result;
	}
	
	public static List<AuthrFmtPermissionMenu> organizeHomeMenu(List<AuthrRolePermmission> rolePermissions, List<AuthrOperatorPermmission> operatorPermissions, List<AuthrPermmission> permissions) {
		Set<String> permissionIds = new HashSet<String>();
		permissionIds.addAll(extractPermissionsByRole(rolePermissions));
		permissionIds.addAll(extractPermissionsByOperator(operatorPermissions));
		List<AuthrPermmission> menu = new ArrayList<AuthrPermmission>();
		for (String permissionId : permissionIds) {
			for (AuthrPermmission permission : permissions) {
				if(StringUtils.equals(permissionId, permission.getPermission()) && StringUtils.equals(permission.getType(), PermissionType.MENU.getCode()) ) {
					menu.add(permission);
					break;
				}
			}
		}
		return organizePermissionByModule(menu);
	}
	
	public static String getMenuHTML(Operator operator, String contextPath) {
		return getMenuHTML(AuthrConts.HOMEMENU.get(operator), contextPath);
	}
	
	public static String getMenuHTML(List<AuthrFmtPermissionMenu> menus, String contextPath) {
		String html = "";
		String newLine = "";
		if (menus != null) {
			for (AuthrFmtPermissionMenu menu : menus) {
				String folderStartHtml = "<li><span><b>"+menu.getModule()+"</b></span><ul>";
				html += (folderStartHtml + newLine);
				for (AuthrPermmission subMenu : menu.getPermissions()) {
					String functionHtml = "<li><b onclick=\"return addTab('" + subMenu.getMenuName() + "','" + contextPath + subMenu.getUrl() + "','" + subMenu.getPermission() + "');\" />" + subMenu.getMenuName() + "</b></li>";
					html += (functionHtml + newLine);
				}
				String folderEndHtml = ("</ul></li>" + newLine);
				html += folderEndHtml;
			}
		} else {
			log.info("document parse failed:" + menus);
		}
		return html;
	}

	public enum PermissionType {
		MENU("MENU"),
		ACTION("ACTION");
		
		public static PermissionType getEnum(String code) {
			for (PermissionType type : values()) {
				if(StringUtils.equals(code, type.code))
					return type;
			}
			return null;
		}
		private String code;
		private PermissionType(String code) {
			this.code = code;
		}
		public String getCode() {
			return code;
		}
	}
	
	public enum SortMode {
		ASC, DESC;
	}
	
	public interface AuthrConts {
		public static int DENY = -1;
		public static int LOGIN_TIMEOUT = -2;
		public static Map<String, AuthrAuthorizationInfo> AUTHORIZATION_INFO = new HashMap<String, AuthrAuthorizationInfo>();
		public static Map<Operator, List<AuthrFmtPermissionMenu>> HOMEMENU = new HashMap<Operator, List<AuthrFmtPermissionMenu>>();
	}
}
