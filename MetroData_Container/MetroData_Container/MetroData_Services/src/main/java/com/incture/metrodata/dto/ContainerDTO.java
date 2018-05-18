package com.incture.metrodata.dto;

import com.incture.metrodata.exceptions.InvalidInputFault;
import com.incture.metrodata.util.DB_Operation;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ContainerDTO extends BaseDto{
	ContainerItemsDTO DELIVERY;

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
