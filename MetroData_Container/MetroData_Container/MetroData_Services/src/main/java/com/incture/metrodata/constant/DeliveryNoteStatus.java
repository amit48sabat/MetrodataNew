package com.incture.metrodata.constant;

public enum DeliveryNoteStatus {

	DELIVERY_NOTE_CREATED("created"), DELIVERY_NOTE_STARTED("del_note_started"), DELIVERY_NOTE_COMPLETED(
			"del_note_completed"), DELIVERY_NOTE_REJECTED("del_note_rejected"), DELIVERY_NOTE_PARTIALLY_REJECTED(
					"del_note_partially_rejected"), DELIVERY_NOTE_VALIDATED(
							"del_note_validated"), DELIVERY_NOTE_CANCELLED("del_note_cancelled"),

	// leader board screen constant
	TOTAL_DEL_NOTE("total_delivery_note"),

	// invalidate dn changes
	ADMIN_DN_INVALIDATED("admin_del_note_invalidated"), DRIVER_DN_INVALIDATED(
			"driver_del_note_invalidated"), RFC_DN_INVALIDATED("rfc_del_note_invalidated"),

	// STAT = X when delivery note got cancelled
	// used in container to delivery header conversion
	STAT("X");
	String value;

	private DeliveryNoteStatus(String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}

	// to get the display value for the delivery note status
	public static String getDnStatusDisplayValue(String status) {
		String displayValue = "";
		switch(status){
		case "created":
			displayValue = "CREATED";
			break;
		case "del_note_started":
			displayValue = "STARTED";
			break;
		case "del_note_completed":
			displayValue = "COMPLETED";
			break;
		case "del_note_rejected":
			displayValue = "REJECTED";
			break;
		case "del_note_partially_rejected":
			displayValue = "PARTIALLY REJECTED";
			break;
		case "del_note_validated":
			displayValue = "VALIDATED";
			break;
		case "admin_del_note_invalidated":
			displayValue = "ADMIN INVALIDATED";
			break;
		case "del_note_cancelled":
			displayValue = "CANCELLED";
			break;
		case "total_delivery_note":
			displayValue = "TOTAL DELIVERY NOTE";
			break;
		case "driver_del_note_invalidated":
			displayValue = "DRIVER INVALIDATED";
			break;
		case "rfc_del_note_invalidated":
			displayValue = "RFC INVALIDATED";
			break;
		
		}
		return displayValue;
	}
}
