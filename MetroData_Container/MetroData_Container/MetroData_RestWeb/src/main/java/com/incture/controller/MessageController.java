package com.incture.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.incture.metrodata.dto.MessageDetailsDTO;
import com.incture.metrodata.dto.ResponseDto;
import com.incture.metrodata.dto.SearchMessageVO;
import com.incture.metrodata.service.MessageServiceLocal;

@RestController
@CrossOrigin
@ComponentScan("com.incture")
@RequestMapping(value = "/Message", produces = "application/json")
public class MessageController {

	@Autowired
	MessageServiceLocal messageService;

	private static final Logger LOGGER = LoggerFactory.getLogger(MessageController.class);
	
	@RequestMapping(method = RequestMethod.POST)
	public ResponseDto create(@RequestBody MessageDetailsDTO dto, HttpServletRequest request) {
		String userId = "";
		if (request.getUserPrincipal() != null) {
			userId = request.getUserPrincipal().getName();
		}
		dto.setCreatedBy(userId);
		
		LOGGER.error("INSIDE CREATE MESSAGE CONTROLLER. USER ID "+userId);
		
		return messageService.create(dto, userId);
	}

	@RequestMapping(value = "/find", method = RequestMethod.PUT)
	public ResponseDto findByParam(@RequestBody MessageDetailsDTO dto) {
		
		LOGGER.error("INSIDE FIND MESSAGE CONTROLLER.");
		return messageService.findByParam(dto);
	}

	@RequestMapping(value = "/filter", method = RequestMethod.PUT)
	public ResponseDto findAll(@RequestBody SearchMessageVO dto, HttpServletRequest request) {
		String userId = "";
		if (request.getUserPrincipal() != null) {
			userId = request.getUserPrincipal().getName();
		}
		dto.setUserId(userId);
		LOGGER.error("INSIDE FILTER ALL MESSAGE CONTROLLER.");
		return messageService.findAll(dto);
	}

	@RequestMapping(method = RequestMethod.PUT)
	public ResponseDto update(@RequestBody MessageDetailsDTO dto, HttpServletRequest request) {
		String userId = "";
		if (request.getUserPrincipal() != null) {
			userId = request.getUserPrincipal().getName();
		}
		//dto.setCreatedBy(userId);
		LOGGER.error("INSIDE UPDATE MESSAGE CONTROLLER. USER ID "+ userId);
		return messageService.update(dto, userId);
	}

	@RequestMapping(value = "/{messageId}", method = RequestMethod.DELETE)
	public ResponseDto delete(@PathVariable String messageId) {
		MessageDetailsDTO dto = new MessageDetailsDTO();
		dto.setMessageId(messageId);
		
		LOGGER.error("INSIDE DELETE MESSAGE CONTROLLER.");
		return messageService.delete(dto);
	}

	

}
