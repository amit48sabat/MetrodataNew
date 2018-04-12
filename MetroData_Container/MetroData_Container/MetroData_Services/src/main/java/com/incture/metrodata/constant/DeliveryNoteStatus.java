package com.incture.metrodata.constant;

public enum DeliveryNoteStatus {

	DELIVERY_NOTE_CREATED("created"), 
	DELIVERY_NOTE_STARTED("del_note_started"), 
	DELIVERY_NOTE_COMPLETED("del_note_completed"),
	DELIVERY_NOTE_REJECTED("del_note_rejected"), 
	DELIVERY_NOTE_PARTIALLY_REJECTED("del_note_partially_rejected"),
	DELIVERY_NOTE_VALIDATED("del_note_validated"),
	DELIVERY_NOTE_CANCELLED("del_note_cancelled"),
    
	// leader board screen constant
	TOTAL_DEL_NOTE("total_delivery_note"),
	
	//invalidate dn changes
	ADMIN_DN_INVALIDATED("admin_del_note_invalidated"),
	DRIVER_DN_INVALIDATED("driver_del_note_invalidated"),
	RFC_DN_INVALIDATED("rfc_del_note_invalidated");
	
	String value;

	private DeliveryNoteStatus(String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}
}
