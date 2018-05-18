package com.incture.metrodata.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class CompleteDeliveryDTO {
	public String fileContent;
	public String fileType;
	public String fileName;
	public DeliveryHeaderDTO deliveryHeaderDTO;
}
