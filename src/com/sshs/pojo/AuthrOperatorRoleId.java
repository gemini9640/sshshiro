package com.sshs.pojo;

import javax.persistence.Embeddable;

@Embeddable
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class AuthrOperatorRoleId implements java.io.Serializable {
	private String operator;
	private String role;

	public AuthrOperatorRoleId() {
	}

	public AuthrOperatorRoleId(String operator, String role) {
		this.operator = operator;
		this.role = role;
	}
	
	@javax.persistence.Column(name = "operator")
	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	@javax.persistence.Column(name = "role")
	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	@Override
	public String toString() {
		return "AuthrOperatorRoleId [operator=" + operator + ", role=" + role + "]";
	}
}
