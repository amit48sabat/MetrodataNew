package com.incture.controller;

import javax.servlet.http.HttpServletRequest;

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
import com.incture.metrodata.dto.UserDetailsDTO;
import com.incture.metrodata.service.DeliveryHeaderServiceLocal;
import com.incture.metrodata.service.UserServiceLocal;
import com.incture.metrodata.util.ServicesUtil;

@RestController
@CrossOrigin
@ComponentScan("com.incture")
@RequestMapping(value = "/DeliveryNotes", produces = "application/json")
class DeliveryHeaderController {

	@Autowired
	DeliveryHeaderServiceLocal deliveryHeaderServiceLocal;

	@Autowired
	UserServiceLocal userServiceLocal;

	@RequestMapping(method = RequestMethod.POST)
	public ResponseDto createDeliveryNote(@RequestBody DeliveryHeaderDTO dto) {
		// LOGGER.info("Inside delivery data creation");
		return deliveryHeaderServiceLocal.create(dto);
	}

	@RequestMapping(value = "/{deliveryNoteId}", method = RequestMethod.PUT)
	public ResponseDto updateDeliveryNote(@PathVariable Long deliveryNoteId, @RequestBody DeliveryHeaderDTO dto,
			HttpServletRequest request) {
		ResponseDto res = new ResponseDto();
		String userId = "";
		if (!ServicesUtil.isEmpty(request.getUserPrincipal())) {
			userId = request.getUserPrincipal().getName();
		}
		// validating user role if action not permitted then return
		UserDetailsDTO userDto = new UserDetailsDTO();
		userDto.setUserId(userId);
		res = userServiceLocal.find(userDto);

		userDto = (UserDetailsDTO) res.getData();
		// LOGGER.info("Inside delivery data creation");
		dto.setDeliveryNoteId(deliveryNoteId);
		return deliveryHeaderServiceLocal.update(dto, userDto);
	}

	@RequestMapping(method = RequestMethod.GET)
	public @ResponseBody ResponseDto findAllDeliveryHeaders(HttpServletRequest request) {
		ResponseDto res = new ResponseDto();
		String userId = "";
		if (!ServicesUtil.isEmpty(request.getUserPrincipal())) {
			userId = request.getUserPrincipal().getName();
		} else {
			return ServicesUtil.getUnauthorizedResponseDto();

		}
		// validating user role if action not permitted then return
		res = userServiceLocal.validatedUserRoleByUserId(userId);
		if (!res.isStatus())
			return ServicesUtil.getUnauthorizedResponseDto();
		;

		UserDetailsDTO dto = (UserDetailsDTO) res.getData();
		// LOGGER.info("Inside delivery data creation");
		return deliveryHeaderServiceLocal.getAllDeliveryNoteByAdminsWareHouse(dto);
	}

	@RequestMapping(value = "/{deliveryNoteId}", method = RequestMethod.DELETE)
	public ResponseDto deleteDeliveryNote(@PathVariable Long deliveryNoteId) {
		// LOGGER.info("Inside delivery data creation");
		return deliveryHeaderServiceLocal.delete(deliveryNoteId);
	}
}
