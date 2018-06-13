package com.incture.metrodata.service;

import java.util.List;
import java.util.Map;

import com.incture.metrodata.dto.ContainerDTO;
import com.incture.metrodata.dto.ContainerDetailsDTO;
import com.incture.metrodata.dto.ResponseDto;
import com.incture.metrodata.entity.ContainerRecordsDo;
import com.incture.metrodata.entity.DeliveryHeaderDo;
import com.incture.metrodata.util.HciRestInvoker;

public interface ContainerServiceLocal {

	ResponseDto create(String controllerJson);

	 HciRestInvoker getHciRestInvoker();
	 
	 List<ContainerDetailsDTO> findAll();
	 
	 public Integer createEntryInDeliveryHeader(ContainerDTO dto) throws Exception;

	void markPayloadAsDeleted(ContainerRecordsDo dos);
}
