package com.incture.metrodata.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderTrackingVO {

	private String tripId;
	private String driverId;
	List<LocationVO> locations;
}
