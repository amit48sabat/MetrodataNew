package com.incture.metrodata.constant;

public enum RoleConstant {

	SUPER_ADMIN("super_admin"), SALES_ADMIN("sales_admin"), COURIER_ADMIN("courier_admin"), ADMIN("admin"), DRIVER(
			"driver");

	String value;

	private RoleConstant(String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}
}
