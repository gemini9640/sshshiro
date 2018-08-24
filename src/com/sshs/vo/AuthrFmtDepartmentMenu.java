package com.sshs.vo;

import java.util.List;

public class AuthrFmtDepartmentMenu {
	private String department;
	private List<AuthrFmtRoleOperatorMenu> roleOperator;

	public AuthrFmtDepartmentMenu() {
	}

	public AuthrFmtDepartmentMenu(String department,
			List<AuthrFmtRoleOperatorMenu> roleOperator) {
		super();
		this.department = department;
		this.roleOperator = roleOperator;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public List<AuthrFmtRoleOperatorMenu> getRoleOperator() {
		return roleOperator;
	}

	public void setRoleOperator(List<AuthrFmtRoleOperatorMenu> roleOperator) {
		this.roleOperator = roleOperator;
	}

	@Override
	public String toString() {
		return "AuthrFmtDepartmentMenu [department=" + department
				+ ", roleOperator=" + roleOperator + "]";
	}

	
}
