package com.incture.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.incture.metrodata.dto.DeliveryHeaderDTO;
import com.incture.metrodata.dto.ResponseDto;
import com.incture.metrodata.service.DeliveryHeaderServiceLocal;

@RestController
@CrossOrigin
@ComponentScan("com.incture")
@RequestMapping(value = "/DeliveryNotes", produces = "application/json")
class DeliveryHeaderController {

	@Autowired
	DeliveryHeaderServiceLocal deliveryHeaderServiceLocal;

	@RequestMapping( method = RequestMethod.POST)
	public  ResponseDto createDeliveryNote(@RequestBody DeliveryHeaderDTO dto) {
		// LOGGER.info("Inside delivery data creation");
		return deliveryHeaderServiceLocal.create(dto);
	}
	
   
	@RequestMapping( value="/{deliveryNoteId}",method = RequestMethod.PUT)
	public  ResponseDto updateDeliveryNote(@PathVariable Long deliveryNoteId, @RequestBody DeliveryHeaderDTO dto) {
		// LOGGER.info("Inside delivery data creation");
		dto.setDeliveryNoteId(deliveryNoteId);
		return deliveryHeaderServiceLocal.update(dto);
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public @ResponseBody ResponseDto findAllDeliveryHeaders() {
		// LOGGER.info("Inside delivery data creation");
		return deliveryHeaderServiceLocal.findAll();
	}
	
	@RequestMapping( value="/{deliveryNoteId}",method = RequestMethod.DELETE)
	public  ResponseDto deleteDeliveryNote(@PathVariable Long deliveryNoteId) {
		// LOGGER.info("Inside delivery data creation");
		return deliveryHeaderServiceLocal.delete(deliveryNoteId);
	}
}
