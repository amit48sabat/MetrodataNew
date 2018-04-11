package com.incture.metrodata.service;

import com.incture.metrodata.dto.CommentsDTO;
import com.incture.metrodata.dto.MessageDetailsDTO;
import com.incture.metrodata.dto.ResponseDto;

public interface CommentsServiceLocal {

	

	ResponseDto update(CommentsDTO dto, String updatedBy);

	ResponseDto delete(CommentsDTO dto);
}
