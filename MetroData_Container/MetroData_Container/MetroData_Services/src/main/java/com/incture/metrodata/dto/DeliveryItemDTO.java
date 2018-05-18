package com.incture.metrodata.dto;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;

import com.incture.metrodata.exceptions.InvalidInputFault;
import com.incture.metrodata.util.DB_Operation;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@XmlRootElement
@Getter
@Setter
@ToString
public class DeliveryItemDTO extends BaseDto {
	private long deliveryItemId;
	private String serialNum;
	private String material;
	private String description;
	private String batch;
	private String quantity;
	private String volume;
	private ArrayList<String> ScanItems = new ArrayList<>();

	@Override
	public void validate(DB_Operation enOperation) throws InvalidInputFault {
		// TODO Auto-generated method stub

	}

	@Override
	public Object getPrimaryKey() {
		// TODO Auto-generated method stub
		return deliveryItemId;
	}

}
