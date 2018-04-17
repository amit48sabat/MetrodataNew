package com.incture.controller;

import javax.servlet.http.HttpServletRequest;

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
import com.incture.metrodata.dto.UserDetailsDTO;
import com.incture.metrodata.dto.WareHouseDetailsDTO;
import com.incture.metrodata.service.UserServiceLocal;
import com.incture.metrodata.service.WareHouseServiceLocal;
import com.incture.metrodata.util.ServicesUtil;

@RestController
@CrossOrigin
@ComponentScan("com.incture")
@RequestMapping(value = "/warehouse", produces = "application/json")
public class WareHouseController {

	@Autowired
	WareHouseServiceLocal wareHouseService;

	@Autowired
	UserServiceLocal userService;

	@RequestMapping(method = RequestMethod.POST)
	public ResponseDto create(@RequestBody WareHouseDetailsDTO dto) {
		return wareHouseService.create(dto);
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
		res = userService.validatedUserRoleByUserId(userId);
		if (!res.isStatus())
			return ServicesUtil.getUnauthorizedResponseDto();

		UserDetailsDTO adminDto = (UserDetailsDTO) res.getData();
		
		return wareHouseService.getWareHouseListByUserId(userId, adminDto.getRole().getRoleName());
	}

	@RequestMapping(value = "/{wareHouseId}", method = RequestMethod.PUT)
	public ResponseDto update(@PathVariable String wareHouseId, @RequestBody WareHouseDetailsDTO dto) {
		dto.setWareHouseId(wareHouseId);
		return wareHouseService.update(dto);
	}

	@RequestMapping(value = "/{wareHouseId}", method = RequestMethod.DELETE)
	public ResponseDto delete(@PathVariable String wareHouseId) {
		WareHouseDetailsDTO dto = new WareHouseDetailsDTO();
		dto.setWareHouseId(wareHouseId);
		return wareHouseService.delete(dto);
	}

	@RequestMapping(value = "/{wareHouseId}", method = RequestMethod.GET)
	public ResponseDto findById(@PathVariable String wareHouseId) {
		WareHouseDetailsDTO dto = new WareHouseDetailsDTO();
		dto.setWareHouseId(wareHouseId);
		return wareHouseService.find(dto);
	}

	@RequestMapping(value = "/ecc", method = RequestMethod.GET)
	public ResponseDto getAllWareHouseFromECC() {
		return wareHouseService.refreshWareHouseListFromEcc();
	}

}
