package com.incture.metrodata.service;

import com.incture.metrodata.dto.ResponseDto;
import com.incture.metrodata.dto.UserDetailsDTO;

public interface UserServiceLocal {



	ResponseDto create(UserDetailsDTO dto);

	ResponseDto update(UserDetailsDTO dto);

	ResponseDto find(UserDetailsDTO dto);

	ResponseDto delete(UserDetailsDTO dto);

	ResponseDto findAll();
	
	// for creating metrodata default technical user
	ResponseDto createDefaultUser(UserDetailsDTO dto);
}
