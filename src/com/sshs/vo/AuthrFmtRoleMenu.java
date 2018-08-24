package com.sshs.vo;

import com.sshs.pojo.AuthrRole;

import java.util.List;

public class AuthrFmtRoleMenu {
	private String department;
	private List<AuthrRole> roles;

	public AuthrFmtRoleMenu() {
	}

	public AuthrFmtRoleMenu(String department, List<AuthrRole> roles) {
		this.department = department;
		this.roles = roles;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public List<AuthrRole> getRoles() {
		return roles;
	}

	public void setRoles(List<AuthrRole> roles) {
		this.roles = roles;
	}

	@Override
	public String toString() {
		return "AuthrFmtRoleMenu [department=" + department + ", roles="
				+ roles + "]";
	}

}
