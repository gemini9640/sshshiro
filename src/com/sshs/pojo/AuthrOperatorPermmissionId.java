package com.sshs.pojo;

import javax.persistence.Embeddable;

@Embeddable
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class AuthrOperatorPermmissionId implements java.io.Serializable {
	private String operator;
	private String permission;

	public AuthrOperatorPermmissionId() {
	}

	public AuthrOperatorPermmissionId(String operator, String permission) {
		this.operator = operator;
		this.permission = permission;
	}

	@javax.persistence.Column(name = "operator")
	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	@javax.persistence.Column(name = "permission")
	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

	@Override
	public String toString() {
		return "AuthrOperatorPermmissionId [operator=" + operator
				+ ", permission=" + permission + "]";
	}

}
