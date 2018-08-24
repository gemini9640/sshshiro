package com.sshs.vo;

import com.sshs.pojo.AuthrPermmission;

import java.util.List;


public class AuthrFmtPermissionMenu {
	private String module;
	private List<AuthrPermmission> permissions;

	public AuthrFmtPermissionMenu() {
	}

	public AuthrFmtPermissionMenu(String module,
			List<AuthrPermmission> permissions) {
		super();
		this.module = module;
		this.permissions = permissions;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public List<AuthrPermmission> getPermissions() {
		return permissions;
	}

	public void setPermissions(List<AuthrPermmission> permissions) {
		this.permissions = permissions;
	}

	@Override
	public String toString() {
		return "AuthrFmtPermissionMenu [module=" + module + ", permissions="
				+ permissions + "]";
	}

	
}
