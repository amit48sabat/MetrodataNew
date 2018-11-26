package com.incture.metrodata.dto;

import java.util.List;
import java.util.Set;

import com.incture.metrodata.exceptions.InvalidInputFault;
import com.incture.metrodata.util.DB_Operation;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class OptimizedRouteDTO extends BaseDto{

	private Double lat;
	private Double lng;
	private String tripId;
	
	
	@Override
	public void validate(DB_Operation enOperation) throws InvalidInputFault {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object getPrimaryKey() {
		// TODO Auto-generated method stub
		return null;
	}

}
