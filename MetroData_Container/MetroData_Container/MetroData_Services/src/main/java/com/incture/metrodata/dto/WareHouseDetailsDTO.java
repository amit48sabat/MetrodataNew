package com.incture.metrodata.dto;

import java.util.Date;

import com.incture.metrodata.exceptions.InvalidInputFault;
import com.incture.metrodata.util.DB_Operation;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WareHouseDetailsDTO extends BaseDto {
	private String wareHouseId;
	private String wareHouseName;
	private Date createdAt;
	private Date updatedAt;

	@Override
	public void validate(DB_Operation enOperation) throws InvalidInputFault {
		// TODO Auto-generated method stub

	}

	@Override
	public Object getPrimaryKey() {

		return wareHouseId;
	}
}
