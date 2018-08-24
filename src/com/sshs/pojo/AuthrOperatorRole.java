package com.sshs.pojo;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "authr_operator_role")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class AuthrOperatorRole implements java.io.Serializable {
	private AuthrOperatorRoleId id;

	public AuthrOperatorRole() {
	}

	public AuthrOperatorRole(AuthrOperatorRoleId id) {
		this.id = id;
	}
	
	@EmbeddedId
	@javax.persistence.Column(name = "id")
	public AuthrOperatorRoleId getId() {
		return id;
	}

	public void setId(AuthrOperatorRoleId id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "AuthrOperatorRole [id=" + id + "]";
	}
	
}
