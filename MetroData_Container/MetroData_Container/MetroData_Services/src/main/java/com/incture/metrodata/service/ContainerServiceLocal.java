package com.incture.metrodata.service;

import java.util.List;

import com.incture.metrodata.dto.ContainerDTO;
import com.incture.metrodata.dto.ContainerDetailsDTO;
import com.incture.metrodata.dto.ContainerRecordsDTO;
import com.incture.metrodata.dto.ResponseDto;
import com.incture.metrodata.util.HciRestInvoker;

public interface ContainerServiceLocal {

	ResponseDto create(String controllerJson);

	 HciRestInvoker getHciRestInvoker();
	 
	 List<ContainerDetailsDTO> findAll();
	 
	 public Integer createEntryInDeliveryHeader(ContainerDTO dto) throws Exception;

	void update(ContainerRecordsDTO dos);

	Object test(Long id);

	ContainerRecordsDTO getContainerRecordById(Long id);

	//ContainerDTO stringPayloadToContainerDto(String controllerJson);
}
