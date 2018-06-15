package com.incture.controller;

import org.hibernate.HibernateException;
import org.hibernate.TransactionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.incture.metrodata.constant.Message;
import com.incture.metrodata.dto.ResponseDto;
import com.incture.metrodata.service.ContainerServiceLocal;

@RestController
@CrossOrigin
@ComponentScan("com.incture")
@RequestMapping(value = "/Container", produces = "application/json")
public class ContainerController {

	@Autowired
	ContainerServiceLocal containerService;

	private static final Logger LOGGER = LoggerFactory.getLogger(ContainerController.class);

	@RequestMapping(method = RequestMethod.POST)
	public ResponseDto create(@RequestBody String controllerJson) {
		ResponseDto response = new ResponseDto();
		try {
			LOGGER.error("INSIDE CREATE CONTAINER CONTROLLER ");
			response = containerService.create(controllerJson);
		} catch (TransactionSystemException e) {
			LOGGER.error(" NESTED TRANSACTION EXCEPTION SHOULD NOT BE A ISSUE " + e.getMessage());
			response.setStatus(true);
			response.setMessage(Message.SUCCESS.getValue());
			response.setCode(200);
		} catch (Exception e) {
			LOGGER.error("INSIDE CREATE CONTAINER CONTROLLER CATCH EXCEPTION " + e.getMessage());
			response.setStatus(false);
			response.setMessage(Message.FAILED.getValue());
			response.setCode(500);
		}

		return response;
	}
}
