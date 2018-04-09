package com.incture.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.incture.metrodata.dto.ResponseDto;
import com.incture.metrodata.dto.UserDetailsDTO;
import com.incture.metrodata.service.DeliveryHeaderServiceLocal;
import com.incture.metrodata.service.TripServiceLocal;
import com.incture.metrodata.service.UserServiceLocal;
import com.incture.metrodata.util.ServicesUtil;

@RestController
@CrossOrigin
@ComponentScan("com.incture")
@RequestMapping(value = "/Admin", produces = "application/json")
public class AdminController {
	
	@Autowired
	DeliveryHeaderServiceLocal deliveryHeaderServiceLocal;
  
	@Autowired
	TripServiceLocal tripServiceLocal;
	
	@Autowired
	UserServiceLocal userServiceLocal;
	
	@RequestMapping(value = "/dashboard", method = RequestMethod.GET)
	public ResponseDto adminDashbord(HttpServletRequest request) {
		ResponseDto res = new ResponseDto();
		String userId = "";
		if (!ServicesUtil.isEmpty(request.getUserPrincipal())) {
			userId = request.getUserPrincipal().getName();
		} else {
			return ServicesUtil.getUnauthorizedResponseDto();

		}
		// validating user role if action not permitted then return
		 res = userServiceLocal.validatedUserRoleByUserId(userId);
		 if(!res.isStatus())
			 return res;
		 
		UserDetailsDTO adminDto =  (UserDetailsDTO) res.getData();
		return tripServiceLocal.getAdminDashboardAssociatedWithAdmins(adminDto);
		//return deliveryHeaderServiceLocal.adminDashboardService();
	}
}
