package com.incture.metrodata.service;

import com.incture.metrodata.dto.ResponseDto;
import com.incture.metrodata.util.HciRestInvoker;

public interface ContainerServiceLocal {

	ResponseDto create(String controllerJson);

	 HciRestInvoker getHciRestInvoker();
}
