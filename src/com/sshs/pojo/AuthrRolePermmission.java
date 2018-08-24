package com.sshs.pojo;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "authr_role_permission")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class AuthrRolePermmission implements java.io.Serializable {
	private AuthrRolePermmissionId id;

	public AuthrRolePermmission() {
	}

	public AuthrRolePermmission(AuthrRolePermmissionId id) {
		this.id = id;
	}

	@EmbeddedId
	@javax.persistence.Column(name = "id")
	public AuthrRolePermmissionId getId() {
		return id;
	}

	public void setId(AuthrRolePermmissionId id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "AuthrRolePermmission [id=" + id + "]";
	}

}
