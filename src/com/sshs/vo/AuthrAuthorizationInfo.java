package com.sshs.vo;

import java.util.Set;


public class AuthrAuthorizationInfo {
	private Set<String> roles;
	private Set<String> permission;

	public AuthrAuthorizationInfo() {
	}
	
	public AuthrAuthorizationInfo(Set<String> roles, Set<String> permission) {
		this.roles = roles;
		this.permission = permission;
	}
	
	public Set<String> getRoles() {
		return roles;
	}
	public void setRoles(Set<String> roles) {
		this.roles = roles;
	}
	public Set<String> getPermission() {
		return permission;
	}
	public void setPermission(Set<String> permission) {
		this.permission = permission;
	}

	@Override
	public String toString() {
		return "AuthrAuthorizationInfo [roles=" + roles + ", permission="
				+ permission + "]";
	}
}
