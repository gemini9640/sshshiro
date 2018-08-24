package com.sshs.pojo;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "authr_permission")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class AuthrPermmission {
	private String permission;
	private String type;
	private String module;
	private String permissionName;
	private String menuName;
	private Integer menuIndex;
	private String parentMenuId;
	private String url;
	private String remark;
	private Integer flag;

	public AuthrPermmission() {
	}

	public AuthrPermmission(String permission, String type, String module, String permissionName, String menuName,
			Integer menuIndex, String parentMenuId, String url, String remark, Integer flag) {
		this.permission = permission;
		this.type = type;
		this.module = module;
		this.permissionName = permissionName;
		this.menuName = menuName;
		this.menuIndex = menuIndex;
		this.parentMenuId = parentMenuId;
		this.url = url;
		this.remark = remark;
		this.flag = flag;
	}

	@Id
	@javax.persistence.Column(name = "permission")
	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

	@javax.persistence.Column(name = "type")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@javax.persistence.Column(name = "module")
	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}
	
	@javax.persistence.Column(name = "menuName")
	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	@javax.persistence.Column(name = "menuIndex")
	public Integer getMenuIndex() {
		return menuIndex;
	}

	public void setMenuIndex(Integer menuIndex) {
		this.menuIndex = menuIndex;
	}

	@javax.persistence.Column(name = "parentMenuId")
	public String getParentMenuId() {
		return parentMenuId;
	}

	public void setParentMenuId(String parentMenuId) {
		this.parentMenuId = parentMenuId;
	}

	@javax.persistence.Column(name = "url")
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@javax.persistence.Column(name = "remark")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@javax.persistence.Column(name = "permissionName")
	public String getPermissionName() {
		return permissionName;
	}

	public void setPermissionName(String permissionName) {
		this.permissionName = permissionName;
	}

	@javax.persistence.Column(name = "flag")
	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	@Override
	public String toString() {
		return "AuthrPermmission [permission=" + permission + ", type=" + type
				+ ", module=" + module + ", permissionName=" + permissionName
				+ ", menuName=" + menuName + ", menuIndex=" + menuIndex
				+ ", parentMenuId=" + parentMenuId + ", url=" + url
				+ ", remark=" + remark + ", flag="+flag+"]";
	}
}
