package com.incture.metrodata.service;

import com.incture.metrodata.dto.DeliveryItemDTO;
import com.incture.metrodata.dto.ResponseDto;

public interface DeliveryItemServiceLocal {
	
	ResponseDto create(DeliveryItemDTO dto);

	ResponseDto findById(Long requestNo);

	ResponseDto delete(Long requestNo);

	ResponseDto update(DeliveryItemDTO Dto);
	
	Integer deleteUnlinkDeliveryItems();
}
