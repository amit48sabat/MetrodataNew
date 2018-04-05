package com.incture.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.incture.metrodata.dto.ResponseDto;
import com.incture.metrodata.dto.UserDetailsDTO;
import com.incture.metrodata.service.TripServiceLocal;
import com.incture.metrodata.service.UserServiceLocal;

@RestController
@ComponentScan("com.incture")
@RequestMapping(value = "/users", produces = "application/json")
public class UserDetailController {

	@Autowired
	UserServiceLocal userServiceLocal;

	@Autowired
	TripServiceLocal tripService;
	
	@RequestMapping(method = RequestMethod.POST)
	public ResponseDto create(@RequestBody UserDetailsDTO dto) {
		return userServiceLocal.create(dto);
	}

	

	@RequestMapping( method = RequestMethod.GET)
	public ResponseDto findAll() {
		return userServiceLocal.findAll();
	}

	@RequestMapping(value = "/{userId}",method = RequestMethod.PUT)
<<<<<<< HEAD
	public ResponseDto update(@PathVariable String userId,@RequestBody  UserDetailsDTO dto) {
	dto.setUserId(userId);
=======
	public ResponseDto update(@RequestBody  UserDetailsDTO dto) {
>>>>>>> branch 'master' of https://github.com/amit48sabat/MetrodataNew.git
		return userServiceLocal.update(dto);
	}

	@RequestMapping(value = "/{userId}", method = RequestMethod.DELETE)
	public ResponseDto delete(@PathVariable String userId) {
		UserDetailsDTO dto = new UserDetailsDTO();
		dto.setUserId(userId);
		return userServiceLocal.delete(dto);
	}

	@RequestMapping(value = "/{userId}", method = RequestMethod.GET)
	public ResponseDto findById(@PathVariable String userId) {
		UserDetailsDTO dto = new UserDetailsDTO();
		dto.setUserId(userId);
		return userServiceLocal.find(dto);
	}
	@RequestMapping(value="/dashboard",method = RequestMethod.GET)
	public  ResponseDto userDashboard(HttpServletRequest request) {
		String userId  = request.getUserPrincipal().getName();
		return tripService.driverDashboardService(userId);
	}
	
}