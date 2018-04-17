package com.incture.metrodata.service;

import com.incture.metrodata.dto.CourierDetailsDTO;
import com.incture.metrodata.dto.ResponseDto;
import com.incture.metrodata.dto.UserDetailsDTO;

public interface CourierDetailServiceLocal {
	ResponseDto create(CourierDetailsDTO dto);
	ResponseDto update(CourierDetailsDTO dto);
	ResponseDto find(CourierDetailsDTO dto);
	ResponseDto findAll(UserDetailsDTO userDto);
	ResponseDto delete(CourierDetailsDTO dto);
	
}
