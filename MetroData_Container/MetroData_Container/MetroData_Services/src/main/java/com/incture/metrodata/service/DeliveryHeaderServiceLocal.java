
package com.incture.metrodata.service;

import java.util.List;

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
     
	ResponseDto getAllDeliveryNoteByAdminsWareHouse(UserDetailsDTO  adminDto, Long dnId);

	ResponseDto getDeliveryNoteByStatus(UserDetailsDTO adminDto,String deliveryNoteId);

	ResponseDto updateList(List<DeliveryHeaderDTO> dtoList, UserDetailsDTO updaingUserDto);

	ResponseDto refreshDeliveryNoteList(UserDetailsDTO dto);
}
