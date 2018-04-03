package com.incture.metrodata.service;

import com.incture.metrodata.dto.MessageDetailsDTO;
import com.incture.metrodata.dto.ResponseDto;
import com.incture.metrodata.dto.SearchMessageVO;

public interface MessageServiceLocal {

	ResponseDto findByParam(MessageDetailsDTO dto);

	ResponseDto sendMessage(MessageDetailsDTO dto);

	ResponseDto findAll(SearchMessageVO dto);

	ResponseDto create(MessageDetailsDTO dto, String cretedBy);
	
	ResponseDto update(MessageDetailsDTO dto, String cretedBy);
	
	ResponseDto delete(MessageDetailsDTO dto);
}
