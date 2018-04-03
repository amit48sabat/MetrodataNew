package com.incture.metrodata.service;

import com.incture.metrodata.dto.CourierDetailsDTO;
import com.incture.metrodata.dto.ResponseDto;

public interface CourierDetailServiceLocal {
	ResponseDto create(CourierDetailsDTO dto);
	ResponseDto update(CourierDetailsDTO dto);
	ResponseDto find(CourierDetailsDTO dto);
	ResponseDto findAll();
	ResponseDto delete(CourierDetailsDTO dto);
	
}
