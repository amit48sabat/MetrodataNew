package com.incture.metrodata.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class LocationVO {
	private Double longitude;
	private Double latitude;
	private String deliveryNoteId;

}
