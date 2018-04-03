package com.incture.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.incture.metrodata.dto.ResponseDto;
import com.incture.metrodata.service.DeliveryHeaderServiceLocal;

@RestController
@CrossOrigin
@ComponentScan("com.incture")
@RequestMapping(value = "/Admin", produces = "application/json")
public class AdminController {
	
	@Autowired
	DeliveryHeaderServiceLocal deliveryHeaderServiceLocal;

	@RequestMapping(value = "/dashboard", method = RequestMethod.GET)
	public ResponseDto adminDashbord() {
		// LOGGER.info("Inside delivery data creation");
		return deliveryHeaderServiceLocal.adminDashboardService();
	}
}
