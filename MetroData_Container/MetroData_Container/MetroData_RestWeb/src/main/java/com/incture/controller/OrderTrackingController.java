package com.incture.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.incture.metrodata.dto.OrderTrackingDTO;
import com.incture.metrodata.dto.OrderTrackingVO;
import com.incture.metrodata.dto.ResponseDto;
import com.incture.metrodata.service.OrderTrackingServiceLocal;

@RestController
@CrossOrigin
@ComponentScan("com.incture")
@RequestMapping(value = "/OrderTracking", produces = "application/json")
public class OrderTrackingController {
  
	@Autowired
	OrderTrackingServiceLocal orderTrackingService;
	
	@RequestMapping( method = RequestMethod.POST)
	public ResponseDto create(@RequestBody OrderTrackingVO trackingVO) {
		return orderTrackingService.create(trackingVO);
	}
	
	@RequestMapping(value="/find", method = RequestMethod.PUT)
	public ResponseDto findByParam(@RequestBody OrderTrackingDTO dto) {
		return orderTrackingService.findByParam(dto);
	}
}
