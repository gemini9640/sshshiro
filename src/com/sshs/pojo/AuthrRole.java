package com.sshs.pojo;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "authr_role")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class AuthrRole {
	private String role;
	private String department;
	private String roleName;

	public AuthrRole() {
	}

	public AuthrRole(String role, String department, String roleName) {
		this.role = role;
		this.department = department;
		this.roleName = roleName;
	}

	@Id
	@javax.persistence.Column(name = "role")
	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	@javax.persistence.Column(name = "department")
	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	@javax.persistence.Column(name = "roleName")
	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	@Override
	public String toString() {
		return "AuthrRole [role=" + role + ", department=" + department
				+ ", roleName=" + roleName + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((role == null) ? 0 : role.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AuthrRole other = (AuthrRole) obj;
		if (role == null) {
			if (other.role != null)
				return false;
		} else if (!role.equals(other.role))
			return false;
		return true;
	}

}
