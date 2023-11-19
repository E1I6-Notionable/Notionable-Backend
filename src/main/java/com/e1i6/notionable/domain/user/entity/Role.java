package com.e1i6.notionable.domain.user.entity;

public enum Role {
	ROLE_USER("ROLE_USER"),
	ROLE_CREATOR("ROLE_CREATOR"),
	ROLE_ADMIN("ROLE_ADMIN");

	String role;

	Role(String role) {
		this.role = role;
	}

	public String value() {
		return role;
	}
}
