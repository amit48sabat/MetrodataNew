package com.incture.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
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
	public @ResponseBody ResponseDto createUser(@RequestBody UserDetailsDTO dto) {
		return userServiceLocal.updateUserData(dto);
	}
	
	@RequestMapping(value = "/{userId}", method = RequestMethod.GET)
	public  ResponseDto getUser( @PathVariable String userId) 
	{
		return userServiceLocal.findById(userId);
		
	}
  
	@RequestMapping(value="/dashboard",method = RequestMethod.GET)
	public  ResponseDto userDashboard(HttpServletRequest request) {
		String userId  = request.getUserPrincipal().getName();
		return tripService.driverDashboardService(userId);
	}
	
}