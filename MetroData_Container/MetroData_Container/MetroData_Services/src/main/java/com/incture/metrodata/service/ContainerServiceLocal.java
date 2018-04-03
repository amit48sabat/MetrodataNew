package com.incture.metrodata.service;

import com.incture.metrodata.dto.ContainerDTO;
import com.incture.metrodata.dto.ResponseDto;

public interface ContainerServiceLocal {
  
	ResponseDto create(ContainerDTO dto);
	
}
