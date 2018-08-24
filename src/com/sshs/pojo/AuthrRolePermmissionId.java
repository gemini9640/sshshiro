package com.sshs.pojo;

import javax.persistence.Embeddable;

@Embeddable
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class AuthrRolePermmissionId implements java.io.Serializable {
	private String role;
	private String permmission;

	public AuthrRolePermmissionId() {
	}

	public AuthrRolePermmissionId(String role, String permmission) {
		this.role = role;
		this.permmission = permmission;
	}

	@javax.persistence.Column(name = "role")
	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	@javax.persistence.Column(name = "permission")
	public String getPermmission() {
		return permmission;
	}

	public void setPermmission(String permmission) {
		this.permmission = permmission;
	}

	@Override
	public String toString() {
		return "AuthrRolePermmissionId [role=" + role + ", permmission="
				+ permmission + "]";
	}

}
