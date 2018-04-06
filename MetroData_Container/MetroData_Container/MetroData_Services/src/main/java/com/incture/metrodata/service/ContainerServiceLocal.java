package com.incture.metrodata.service;

import com.incture.metrodata.dto.ContainerDTO;
import com.incture.metrodata.dto.ResponseDto;
import com.incture.metrodata.util.HciRestInvoker;

public interface ContainerServiceLocal {

	ResponseDto create(ContainerDTO dto);

	 HciRestInvoker getHciRestInvoker();
}
