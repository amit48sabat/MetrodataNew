package com.incture.metrodata.service;

import com.incture.metrodata.dto.ResponseDto;
import com.incture.metrodata.dto.UserDetailsDTO;

public interface UserServiceLocal {

	ResponseDto updateUserData(UserDetailsDTO userDetailsDTO);


	ResponseDto findById(String userId);
}
