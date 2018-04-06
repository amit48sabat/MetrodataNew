package com.incture.metrodata.dto;

import java.util.Date;

import com.incture.metrodata.constant.DeliveryNoteStatus;
import com.incture.metrodata.exceptions.InvalidInputFault;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class WebLeaderBoardVO {
 private String sortBy;
 private Date from;
 private Date to;
 private Integer firstResult;
 private Integer maxResult;
 public boolean validateSortBy() throws InvalidInputFault{	
	 if(DeliveryNoteStatus.DELIVERY_NOTE_PARTIALLY_REJECTED.getValue().equals(sortBy) ||
		DeliveryNoteStatus.DELIVERY_NOTE_REJECTED.getValue().equals(sortBy) ||
		DeliveryNoteStatus.DELIVERY_NOTE_COMPLETED.getValue().equals(sortBy) ||
		DeliveryNoteStatus.TOTAL_DEL_NOTE.getValue().equals(sortBy) 
	  ){
		 return true;
	 }else{
		 throw new InvalidInputFault("delivery_note sortBy '"+sortBy+"' is invalid sortBy code");
	 }
}
 
}
