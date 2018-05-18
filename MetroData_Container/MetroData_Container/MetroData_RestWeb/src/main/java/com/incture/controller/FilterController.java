package com.incture.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.incture.metrodata.dto.FilterDTO;
import com.incture.metrodata.dto.ResponseDto;
import com.incture.metrodata.service.TripServiceLocal;

@RestController
@CrossOrigin
@ComponentScan("com.incture")
@RequestMapping(value = "/Filter", produces = "application/json")
public class FilterController {
    
	@Autowired
	TripServiceLocal tripService;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FilterController.class);
	
	@RequestMapping( method = RequestMethod.PUT)
	public ResponseDto filter(@RequestBody FilterDTO dto) {
		LOGGER.error("INSIDE FILTER CONTROLLER");
		return tripService.filter(dto);
	}
}
