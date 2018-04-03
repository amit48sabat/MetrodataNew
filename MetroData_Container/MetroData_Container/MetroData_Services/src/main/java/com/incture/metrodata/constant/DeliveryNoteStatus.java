package com.incture.metrodata.constant;

public enum DeliveryNoteStatus {

	DELIVERY_NOTE_CREATED("created"), 
	DELIVERY_NOTE_STARTED("del_note_started"), 
	DELIVERY_NOTE_COMPLETED("del_note_completed"),
	DELIVERY_NOTE_REJECTED("del_note_rejected"), 
	DELIVERY_NOTE_PARTIALLY_REJECTED("del_note_partially_rejected"),
	DELIVERY_NOTE_VALIDATED("del_note_validated"),
	DELIVERY_NOTE_INVALIDATED("del_note_invalidated"),
    
	// leader board screen constant
	TOTAL_DEL_NOTE("total_delivery_note");
	
	String value;

	private DeliveryNoteStatus(String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}
}
