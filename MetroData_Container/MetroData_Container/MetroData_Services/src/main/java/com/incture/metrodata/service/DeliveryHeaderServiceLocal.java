
package com.incture.metrodata.service;

import com.incture.metrodata.dto.DeliveryHeaderDTO;
import com.incture.metrodata.dto.ResponseDto;
import com.incture.metrodata.dto.UserDetailsDTO;

public interface DeliveryHeaderServiceLocal {

	ResponseDto create(DeliveryHeaderDTO dto);

	//BaseDto find(String requestNo);

	ResponseDto delete(Long requestNo);

	ResponseDto findAll();

	ResponseDto update(DeliveryHeaderDTO Dto, UserDetailsDTO updaingUserDto);
	
	/**
	 * api for admin dashbord
	 * @return
	 */
	ResponseDto adminDashboardService();



	ResponseDto findById(Long deliveryNoteId);
     
	ResponseDto getAllDeliveryNoteByAdminsWareHouse(UserDetailsDTO  adminDto);
}
