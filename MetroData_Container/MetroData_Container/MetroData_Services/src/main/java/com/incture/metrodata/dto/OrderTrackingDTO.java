package com.incture.metrodata.dto;

import java.util.Date;

import com.incture.metrodata.exceptions.InvalidInputFault;
import com.incture.metrodata.util.DB_Operation;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderTrackingDTO extends BaseDto{

	private String tripId;
	private String deliveryNoteId;
	private String driverId;
	private Double longitude;
	private Double latitude;
	private Date createdAt;
	private Date startedAt;
	private Date endedAt;
	
	@Override
	public void validate(DB_Operation enOperation) throws InvalidInputFault {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object getPrimaryKey() {
		// TODO Auto-generated method stub
		return tripId;
	}

}
