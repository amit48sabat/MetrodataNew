package com.incture.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.incture.metrodata.dto.ResponseDto;
import com.incture.metrodata.dto.WareHouseDetailsDTO;
import com.incture.metrodata.service.WareHouseServiceLocal;

@RestController
@CrossOrigin
@ComponentScan("com.incture")
@RequestMapping(value = "/warehouse", produces = "application/json")
public class WareHouseController {

	@Autowired
	WareHouseServiceLocal wareHouseService;
	
	
	@RequestMapping(method = RequestMethod.POST)
	public ResponseDto create(@RequestBody WareHouseDetailsDTO dto) {
		return wareHouseService.create(dto);
	}

	

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ResponseDto findAll() {
		return wareHouseService.findAll();
	}

	@RequestMapping(method = RequestMethod.PUT)
	public ResponseDto update(@RequestBody  WareHouseDetailsDTO dto) {
		return wareHouseService.update(dto);
	}

	@RequestMapping(value = "/{wareHouseId}", method = RequestMethod.DELETE)
	public ResponseDto delete(@PathVariable Long wareHouseId) {
		WareHouseDetailsDTO dto = new WareHouseDetailsDTO();
		dto.setWareHouseId(wareHouseId);
		return wareHouseService.delete(dto);
	}

	@RequestMapping(value = "/{wareHouseId}", method = RequestMethod.GET)
	public ResponseDto findById(@PathVariable Long wareHouseId) {
		WareHouseDetailsDTO dto = new WareHouseDetailsDTO();
		dto.setWareHouseId(wareHouseId);
		return wareHouseService.find(dto);
	}

}
