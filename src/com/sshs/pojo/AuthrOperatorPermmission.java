package com.sshs.pojo;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "authr_operator_permission")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class AuthrOperatorPermmission implements java.io.Serializable {
	private AuthrOperatorPermmissionId id;

	public AuthrOperatorPermmission() {
	}

	public AuthrOperatorPermmission(AuthrOperatorPermmissionId id) {
		this.id = id;
	}

	@EmbeddedId
	@javax.persistence.Column(name = "id")
	public AuthrOperatorPermmissionId getId() {
		return id;
	}

	public void setId(AuthrOperatorPermmissionId id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "AuthrOperatorPermmission [id=" + id + "]";
	}

}
