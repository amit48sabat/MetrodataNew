
package com.incture.metrodata.service;

import com.incture.metrodata.dto.DeliveryHeaderDTO;
import com.incture.metrodata.dto.ResponseDto;

public interface DeliveryHeaderServiceLocal {

	ResponseDto create(DeliveryHeaderDTO dto);

	//BaseDto find(String requestNo);

	ResponseDto delete(Long requestNo);

	ResponseDto findAll();

	ResponseDto update(DeliveryHeaderDTO Dto);
	
	/**
	 * api for admin dashbord
	 * @return
	 */
	ResponseDto adminDashboardService();



	ResponseDto findById(Long deliveryNoteId);

}
