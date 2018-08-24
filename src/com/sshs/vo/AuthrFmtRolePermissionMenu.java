package com.sshs.vo;

import com.sshs.pojo.AuthrRole;

import java.util.List;


public class AuthrFmtRolePermissionMenu {
	private AuthrRole role;
	private List<AuthrFmtPermissionMenu> permissions;

	public AuthrFmtRolePermissionMenu() {
	}

	public AuthrFmtRolePermissionMenu(AuthrRole role, List<AuthrFmtPermissionMenu> permissions) {
		this.role = role;
		this.permissions = permissions;
	}

	public AuthrRole getRole() {
		return role;
	}

	public void setRole(AuthrRole role) {
		this.role = role;
	}

	public List<AuthrFmtPermissionMenu> getPermissions() {
		return permissions;
	}

	public void setPermissions(List<AuthrFmtPermissionMenu> permissions) {
		this.permissions = permissions;
	}

	@Override
	public String toString() {
		return "AuthrFmtRolePermissionMenu [role=" + role + ", permissions="
				+ permissions + "]";
	}


}
