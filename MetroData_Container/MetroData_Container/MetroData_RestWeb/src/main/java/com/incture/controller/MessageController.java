package com.incture.controller;

import javax.servlet.http.HttpServletRequest;

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

	@RequestMapping(method = RequestMethod.POST)
	public ResponseDto create(@RequestBody MessageDetailsDTO dto, HttpServletRequest request) {
		String userId = "";
		if (request.getUserPrincipal() != null) {
			userId = request.getUserPrincipal().getName();
		}
		dto.setCreatedBy(userId);
		return messageService.create(dto, userId);
	}

	@RequestMapping(value = "/find", method = RequestMethod.PUT)
	public ResponseDto findByParam(@RequestBody MessageDetailsDTO dto) {
		return messageService.findByParam(dto);
	}

	@RequestMapping(value = "/filter", method = RequestMethod.PUT)
	public ResponseDto findAll(@RequestBody SearchMessageVO dto) {
		return messageService.findAll(dto);
	}

	@RequestMapping(method = RequestMethod.PUT)
	public ResponseDto update(@RequestBody MessageDetailsDTO dto, HttpServletRequest request) {
		String userId = "";
		if (request.getUserPrincipal() != null) {
			userId = request.getUserPrincipal().getName();
		}
		dto.setCreatedBy(userId);
		return messageService.update(dto, userId);
	}

	@RequestMapping(value = "/{messageId}", method = RequestMethod.DELETE)
	public ResponseDto delete(@PathVariable Long messageId) {
		MessageDetailsDTO dto = new MessageDetailsDTO();
		dto.setMessageId(messageId);
		return messageService.delete(dto);
	}

	@RequestMapping(value = "/{messageId}", method = RequestMethod.GET)
	public ResponseDto findById(@PathVariable Long messageId) {
		MessageDetailsDTO dto = new MessageDetailsDTO();
		dto.setMessageId(messageId);
		return messageService.delete(dto);
	}

}
