package com.incture.metrodata.service;

import com.incture.metrodata.dto.OrderTrackingDTO;
import com.incture.metrodata.dto.OrderTrackingVO;
import com.incture.metrodata.dto.ResponseDto;

public interface OrderTrackingServiceLocal {
  
	
	ResponseDto findByParam(OrderTrackingDTO dto);

	ResponseDto create(OrderTrackingVO trackingVO);
}
