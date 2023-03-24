package com.xuan.model.enums;

/**
 * 角色枚举
 *
 * @author: xuan
 */
public enum UserRoleEnum {
	USER("user", "用户"),
	ADMIN("admin", "管理员");

	private final String role;

	private final String description;

	UserRoleEnum(String role, String description) {
		this.role = role;
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public String getRole() {
		return role;
	}

}
