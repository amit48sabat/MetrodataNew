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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.incture.metrodata.dto.ResponseDto;
import com.incture.metrodata.dto.RoleDetailsDTO;
import com.incture.metrodata.dto.UserDetailsDTO;
import com.incture.metrodata.service.RoleServiceLocal;
import com.incture.metrodata.service.UserServiceLocal;
import com.incture.metrodata.util.ServicesUtil;

@RestController
@CrossOrigin
@ComponentScan("com.incture")
@RequestMapping(value = "/role", produces = "application/json")
public class RoleController {

	@Autowired
	RoleServiceLocal roleService;
	
	@Autowired
    UserServiceLocal userServiceLocal;

	private static final Logger LOGGER = LoggerFactory.getLogger(RoleController.class);
	
	@RequestMapping(method = RequestMethod.POST)
	public ResponseDto create(@RequestBody RoleDetailsDTO dto) {
		LOGGER.error("INSIDE CREATE ROLE CONTROLLER");
		return roleService.create(dto);
	}

	@RequestMapping(method = RequestMethod.GET)
	public ResponseDto findAll(HttpServletRequest request,
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

		UserDetailsDTO adminDto = (UserDetailsDTO) res.getData();
		
		LOGGER.error("INSIDE FIND ALL ROLE CONTROLLER");
		
		return roleService.getRoleByUser(adminDto);
	}

	@RequestMapping(value = "/{roleId}", method = RequestMethod.PUT)
	public ResponseDto update(@PathVariable Long roleId,@RequestBody RoleDetailsDTO dto) {
		dto.setRoleId(roleId);
		LOGGER.error("INSIDE UPDATE ROLE CONTROLLER");
		return roleService.update(dto);
	}

	@RequestMapping(value = "/{roleId}", method = RequestMethod.DELETE)
	public ResponseDto delete(@PathVariable Long roleId) {
		RoleDetailsDTO dto = new RoleDetailsDTO();
		dto.setRoleId(roleId);
		LOGGER.error("INSIDE DELETE ROLE CONTROLLER");
		return roleService.delete(dto);
	}

	@RequestMapping(value = "/{roleId}", method = RequestMethod.GET)
	public ResponseDto findById(@PathVariable Long roleId) {
		RoleDetailsDTO dto = new RoleDetailsDTO();
		dto.setRoleId(roleId);
		LOGGER.error("INSIDE FIND ROLE BY ID CONTROLLER");
		return roleService.find(dto);
	}
}
