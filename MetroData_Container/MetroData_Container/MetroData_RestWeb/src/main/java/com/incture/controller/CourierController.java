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

import com.incture.metrodata.dto.CourierDetailsDTO;
import com.incture.metrodata.dto.ResponseDto;
import com.incture.metrodata.service.CourierDetailServiceLocal;

@RestController
@CrossOrigin
@ComponentScan("com.incture")
@RequestMapping(value = "/courier", produces = "application/json")
public class CourierController {

	@Autowired
	CourierDetailServiceLocal courierService;

	@RequestMapping(method = RequestMethod.POST)
	public ResponseDto create(@RequestBody CourierDetailsDTO dto, HttpServletRequest request) {
		String userId = "";
		if (request.getUserPrincipal() != null) {
			userId = request.getUserPrincipal().getName();
		}
		// dto.setCreatedBy(userId);
		// dto.setUpdatedBy(userId);
		return courierService.create(dto);
	}

	@RequestMapping(value = "/{courierId}", method = RequestMethod.PUT)
	public ResponseDto update(@PathVariable Long courierId, @RequestBody CourierDetailsDTO dto,
			HttpServletRequest request) {
		String userId = "";
		if (request.getUserPrincipal() != null) {
			userId = request.getUserPrincipal().getName();
		}
		// dto.setCreatedBy(userId);
		// dto.setUpdatedBy(userId);
		dto.setCourierId(courierId);
		return courierService.update(dto);
	}

	@RequestMapping(value = "/{courierId}", method = RequestMethod.GET)
	public ResponseDto find(@PathVariable Long courierId) {
		CourierDetailsDTO dto = new CourierDetailsDTO();
		dto.setCourierId(courierId);
		return courierService.delete(dto);
	}

	@RequestMapping(method = RequestMethod.GET)
	public ResponseDto findAll() {
		return courierService.findAll();
	}
	
	@RequestMapping(value = "/{courierId}", method = RequestMethod.DELETE)
	public ResponseDto delete(@PathVariable Long courierId) {
		CourierDetailsDTO dto = new CourierDetailsDTO();
		dto.setCourierId(courierId);
		return courierService.delete(dto);
	}

}
