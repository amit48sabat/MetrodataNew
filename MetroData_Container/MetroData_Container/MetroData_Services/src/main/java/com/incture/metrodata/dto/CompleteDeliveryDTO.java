package com.incture.metrodata.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CompleteDeliveryDTO {
	public String fileContent;
	public String fileType;
	public String fileName;
	public DeliveryHeaderDTO deliveryHeaderDTO;
}
