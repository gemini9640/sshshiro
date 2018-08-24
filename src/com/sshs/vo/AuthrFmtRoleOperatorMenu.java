package com.sshs.vo;

import com.sshs.pojo.AuthrRole;

import java.util.List;

public class AuthrFmtRoleOperatorMenu {
	
	private AuthrRole role;
	private List<String> operators;

	public AuthrFmtRoleOperatorMenu() {
	}

	public AuthrFmtRoleOperatorMenu(AuthrRole role, List<String> operators) {
		super();
		this.role = role;
		this.operators = operators;
	}

	public AuthrRole getRole() {
		return role;
	}

	public void setRole(AuthrRole role) {
		this.role = role;
	}

	public List<String> getOperator() {
		return operators;
	}

	public void setOperator(List<String> operators) {
		this.operators = operators;
	}

	@Override
	public String toString() {
		return "AuthrFmtRoleOperatorMenu [role=" + role + ", operators="
				+ operators + "]";
	}
}