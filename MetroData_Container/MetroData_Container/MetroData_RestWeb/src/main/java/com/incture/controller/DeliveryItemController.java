package com.incture.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.incture.metrodata.dto.DeliveryItemDTO;
import com.incture.metrodata.dto.ResponseDto;
import com.incture.metrodata.service.DeliveryItemServiceLocal;

@RestController
@CrossOrigin
@ComponentScan("com.incture")
@RequestMapping(value = "/DeliveryItem", produces = "application/json")
public class DeliveryItemController {

	@Autowired
	DeliveryItemServiceLocal deliveryItemService;
	
	@RequestMapping( method = RequestMethod.POST)
	public ResponseDto create(@RequestBody DeliveryItemDTO dto) {
		return deliveryItemService.create(dto);
	}
	
	@RequestMapping(value = "/{itemId}" ,method = RequestMethod.GET)
	public ResponseDto find(@PathVariable Long itemId) {
		return deliveryItemService.findById(itemId);
	}
	
	@RequestMapping(value="/{itemId}",method = RequestMethod.PUT)
	public ResponseDto update(@PathVariable Long itemId, @RequestBody DeliveryItemDTO dto) {
		dto.setDeliveryItemId(itemId);
		return deliveryItemService.update(dto);
	}
	
	@RequestMapping(value = "/{itemId}" ,method = RequestMethod.DELETE)
	public ResponseDto delete(@PathVariable Long itemId) {
		return deliveryItemService.delete(itemId);
	}
}
