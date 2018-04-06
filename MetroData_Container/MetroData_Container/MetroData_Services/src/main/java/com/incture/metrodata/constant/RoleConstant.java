package com.incture.metrodata.constant;

public enum RoleConstant {

	SUPER_ADMIN("super_admin"), 
	SALES_ADMIN("sales_admin"), 
	COURIER_ADMIN("courier_admin"), 
	ADMIN_INSIDE_JAKARTA("admin_inside_jakarta"), 
	ADMIN_OUTSIDE_JAKARTA("admin_outside_jakarta"),
	INSIDE_JAKARTA_DRIVER("inside_jakarta_driver"),
	OUTSIDE_JAKARTA_DRIVER("outside_jakarta_driver");

	String value;

	private RoleConstant(String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}
}
