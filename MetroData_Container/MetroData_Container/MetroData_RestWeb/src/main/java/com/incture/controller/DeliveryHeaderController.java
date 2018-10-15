package com.incture.controller;

import java.util.List;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.maps.model.LatLng;
import com.incture.metrodata.dto.DeliveryHeaderDTO;
import com.incture.metrodata.dto.ResponseDto;
import com.incture.metrodata.dto.UserDetailsDTO;
import com.incture.metrodata.service.DeliveryHeaderServiceLocal;
import com.incture.metrodata.service.OptimizedRouteService;
import com.incture.metrodata.service.OptimizedRouteServiceLocal;
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

	@Autowired
	OptimizedRouteServiceLocal optimizedRouteService;

	private static final Logger LOGGER = LoggerFactory.getLogger(DeliveryHeaderController.class);

	@RequestMapping(method = RequestMethod.POST)
	public ResponseDto createDeliveryNote(@RequestBody DeliveryHeaderDTO dto) {
		// LOGGER.info("Inside delivery data creation");
		LOGGER.error("INSIDE CREATE DELIVERY NOTE CONTROLLER.");
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

		LOGGER.error("INSIDE UPDATE DELIVERY NOTE CONTROLLER. USER ID " + userId);
		return deliveryHeaderServiceLocal.update(dto, userDto);
	}

	@RequestMapping(method = RequestMethod.GET)
	public @ResponseBody ResponseDto findAllDeliveryHeaders(HttpServletRequest request,
			@RequestParam(value = "firstResult", defaultValue = "0") String firstResult,
			@RequestParam(value = "maxResult", defaultValue = "0") String maxResult,
			@RequestParam(value = "dnId", defaultValue = "0") Long dnId) {

		// setting pagination
		ServicesUtil.setPagination(firstResult, maxResult);

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

		UserDetailsDTO dto = (UserDetailsDTO) res.getData();
		LOGGER.error("INSIDE FIND ALL DELIVERY NOTE CONTROLLER. USER ID " + userId);
		return deliveryHeaderServiceLocal.getAllDeliveryNoteByAdminsWareHouse(dto, dnId);
	}

	@RequestMapping(value = "/status", method = RequestMethod.GET)
	public ResponseDto getDeliveryNoteByStatus(@RequestParam(value = "status") String status,
			HttpServletRequest request) {
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

		UserDetailsDTO dto = (UserDetailsDTO) res.getData();
		LOGGER.error("INSIDE GET DELIVERY NOTE BY STATUS CONTROLLER. USER ID " + userId);
		return deliveryHeaderServiceLocal.getDeliveryNoteByStatus(dto, status);
	}

	@RequestMapping(value = "/{deliveryNoteId}", method = RequestMethod.DELETE)
	public ResponseDto deleteDeliveryNote(@PathVariable Long deliveryNoteId) {
		// LOGGER.info("Inside delivery data creation");
		return deliveryHeaderServiceLocal.delete(deliveryNoteId);
	}

	@RequestMapping(value = "/listUpdate", method = RequestMethod.PUT)
	public ResponseDto updateDeliveryNoteList(@RequestBody List<DeliveryHeaderDTO> dtoList,
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
		LOGGER.error("INSIDE LIST UPDATE DELIVERY NOTE CONTROLLER. USER ID " + userId);
		return deliveryHeaderServiceLocal.updateList(dtoList, userDto);
	}

	@RequestMapping(value = "/refresh", method = RequestMethod.GET)
	public ResponseDto refreshDeliveryNotes(HttpServletRequest request) {
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

		UserDetailsDTO dto = (UserDetailsDTO) res.getData();
		LOGGER.error("INSIDE REFRESH DELIVERY NOTE CONTROLLER. USER ID " + userId);
		return deliveryHeaderServiceLocal.refreshDeliveryNoteList(dto);
	}

	@RequestMapping(value = "/optimizedRoute", method = RequestMethod.PUT)
	public ResponseDto optimizedDeliveryNoteRoute(HttpServletRequest request,
			@RequestBody List<DeliveryHeaderDTO> dtoList, @RequestParam("lat") Double lat,
			@RequestParam("lng") Double lng) {
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

		UserDetailsDTO dto = (UserDetailsDTO) res.getData();
		LOGGER.error("INSIDE OPTIMIZED ROUTE DELIVERY NOTE CONTROLLER. USER ID " + userId);
		LatLng userLatLng = new LatLng(lat, lng);
		return optimizedRouteService.optimizedRoute(dtoList, userLatLng);
	}
}
