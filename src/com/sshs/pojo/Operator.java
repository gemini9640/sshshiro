package com.sshs.pojo;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "operator")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class Operator implements java.io.Serializable {

    // Fields

    private String username;
    private String password;
    private String role;

    public Operator() {
    }

    public Operator(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    @Id
    @javax.persistence.Column(name = "username")
    public String getUsername() {
	return this.username;
    }

    public void setUsername(String username) {
	this.username = username;
    }

    @javax.persistence.Column(name = "password")
    public String getPassword() {
	return this.password;
    }

    public void setPassword(String password) {
	this.password = password;
    }


    @javax.persistence.Column(name = "role")
    public String getRole() {
	return this.role;
    }

    public void setRole(String role) {
	this.role = role;
    }

}