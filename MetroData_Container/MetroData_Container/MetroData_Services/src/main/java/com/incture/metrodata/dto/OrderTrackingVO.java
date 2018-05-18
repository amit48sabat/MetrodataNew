package com.incture.metrodata.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OrderTrackingVO {

	private String tripId;
	private String driverId;
	List<LocationVO> locations;
}
