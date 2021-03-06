package com.incture.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.incture.metrodata.dto.ResponseDto;
import com.incture.metrodata.dto.UserDetailsDTO;
import com.incture.metrodata.service.TripServiceLocal;
import com.incture.metrodata.service.UserServiceLocal;
import com.incture.metrodata.util.ServicesUtil;

@RestController
@ComponentScan("com.incture")
@RequestMapping(value = "/users", produces = "application/json")
public class UserDetailController {

	@Autowired
	UserServiceLocal userServiceLocal;

	@Autowired
	TripServiceLocal tripService;

	private static final Logger LOGGER = LoggerFactory.getLogger(UserDetailController.class);
	
	@RequestMapping(method = RequestMethod.POST)
	public ResponseDto create(@RequestBody UserDetailsDTO dto, HttpServletRequest request) {
		ResponseDto res = new ResponseDto();
		String createdBy = "";
		if (!ServicesUtil.isEmpty(request.getUserPrincipal())) {
			createdBy = request.getUserPrincipal().getName();
		} else {
			return ServicesUtil.getUnauthorizedResponseDto();

		}

		// setting created by and updated by of new user
		dto.setCreatedBy(createdBy);
		dto.setUpdatedBy(createdBy);

		LOGGER.error("INSIDE CREATE USER CONTROLLER");
		
		return userServiceLocal.create(dto);
	}

	@RequestMapping(method = RequestMethod.GET)
	public ResponseDto findAll(HttpServletRequest request, @RequestParam(value = "role", defaultValue = "") String role,
			@RequestParam(value = "firstResult", defaultValue = "0") String firstResult,
			@RequestParam(value = "maxResult", defaultValue = "0") String maxResult) {

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
			return res;

		UserDetailsDTO dto = (UserDetailsDTO) res.getData();
		
		LOGGER.error("INSIDE FIND ALL USER CONTROLLER");
		
		return userServiceLocal.getUsersAssociatedWithAdmin(dto, role);
	}

	@RequestMapping(value = "/{userId}", method = RequestMethod.PUT)
	public ResponseDto update(@PathVariable String userId, @RequestBody UserDetailsDTO dto,
			HttpServletRequest request) {
		ResponseDto res = new ResponseDto();
		String createdBy = "";
		if (!ServicesUtil.isEmpty(request.getUserPrincipal())) {
			createdBy = request.getUserPrincipal().getName();
		} else {
			return ServicesUtil.getUnauthorizedResponseDto();

		}

		// setting updated by of new user

		dto.setUpdatedBy(createdBy);
		dto.setUserId(userId);

		LOGGER.error("INSIDE UPDATE USER CONTROLLER.");
		
		return userServiceLocal.update(dto);
	}

	@RequestMapping(value = "/{userId}", method = RequestMethod.DELETE)
	public ResponseDto delete(@PathVariable String userId, HttpServletRequest request) {
		ResponseDto res = new ResponseDto();
		String updatedBy = "";
		if (!ServicesUtil.isEmpty(request.getUserPrincipal())) {
			updatedBy = request.getUserPrincipal().getName();
		} else {
			return ServicesUtil.getUnauthorizedResponseDto();

		}

		UserDetailsDTO dto = new UserDetailsDTO();
		dto.setUserId(userId);
		dto.setUpdatedBy(updatedBy);
		return userServiceLocal.delete(dto);
	}

	@RequestMapping(value = "/{userId}", method = RequestMethod.GET)
	public ResponseDto findById(@PathVariable String userId) {
		UserDetailsDTO dto = new UserDetailsDTO();
		dto.setUserId(userId);
		LOGGER.error("INSIDE FIND USER BY ID CONTROLLER.");
		return userServiceLocal.find(dto);
	}

	@RequestMapping(value = "/dashboard", method = RequestMethod.GET)
	public ResponseDto userDashboard(HttpServletRequest request) {
		String userId = request.getUserPrincipal().getName();
		
		LOGGER.error("INSIDE DRIVER DASHBOARD CONTROLLER.");
		return tripService.driverDashboardService(userId);
	}

}