package com.incture.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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

	private static final Logger LOGGER = LoggerFactory.getLogger(AdminController.class);
	
	@RequestMapping(value = "/dashboard", method = RequestMethod.GET)
	public ResponseDto adminDashbord(HttpServletRequest request,
			@RequestParam(required=false) List<String> warehouseList,
			@RequestParam(required=false) Long from,
            @RequestParam(required=false) Long to) {
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
			 return ServicesUtil.getUnauthorizedResponseDto();;
		 
		UserDetailsDTO adminDto =  (UserDetailsDTO) res.getData();
		LOGGER.error("INSIDE ADMIN DASHBOARD CONTROLLER. ADMIN ID "+userId);
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("adminDto", adminDto);
		paramMap.put("warehouseList", warehouseList);
		if (!ServicesUtil.isEmpty(from) && !ServicesUtil.isEmpty(to)) {
		paramMap.put("from", ServicesUtil.convertTime(from));
		paramMap.put("to", ServicesUtil.convertTime(to));
		}
		return tripServiceLocal.getAdminDashboardAssociatedWithAdmins(paramMap);
		//return deliveryHeaderServiceLocal.adminDashboardService();
	}
}
