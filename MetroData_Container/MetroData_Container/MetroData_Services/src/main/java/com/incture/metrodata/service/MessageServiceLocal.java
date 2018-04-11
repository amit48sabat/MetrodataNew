package com.incture.metrodata.service;

import com.incture.metrodata.dto.MessageDetailsDTO;
import com.incture.metrodata.dto.ResponseDto;
import com.incture.metrodata.dto.SearchMessageVO;

public interface MessageServiceLocal {

	ResponseDto findByParam(MessageDetailsDTO dto);

	ResponseDto sendMessage(MessageDetailsDTO dto);

	ResponseDto findAll(SearchMessageVO dto);

	ResponseDto create(MessageDetailsDTO dto, String createdBy);
	
	ResponseDto update(MessageDetailsDTO dto, String createdBy);
	
	ResponseDto delete(MessageDetailsDTO dto);
}
