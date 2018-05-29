package com.incture.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.incture.metrodata.constant.Message;
import com.incture.metrodata.dto.FilterDTO;
import com.incture.metrodata.dto.ResponseDto;
import com.incture.metrodata.dto.TripDetailsDTO;
import com.incture.metrodata.dto.UserDetailsDTO;
import com.incture.metrodata.dto.WebLeaderBoardVO;
import com.incture.metrodata.service.TripServiceLocal;
import com.incture.metrodata.service.UserServiceLocal;
import com.incture.metrodata.util.ServicesUtil;

@RestController
@CrossOrigin
@ComponentScan("com.incture")
@RequestMapping(value = "/Trip", produces = "application/json")
public class TripController {

	@Autowired
	TripServiceLocal tripService;

	@Autowired
	UserServiceLocal userServiceLocal;

	private static final Logger LOGGER = LoggerFactory.getLogger(TripController.class);
	
	@RequestMapping(method = RequestMethod.POST)
	public ResponseDto create(@RequestBody TripDetailsDTO dto, HttpServletRequest request) {
		String userId = "";
		ResponseDto res = new ResponseDto();
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

		dto.setCreatedBy(userId);
		dto.setUpdatedBy(userId);
		// setting tracking feq for trip as per admin frq.
		dto.setTrackFreq(adminDto.getTrackFreq());
		
		LOGGER.error("INSIDE CREATE TRIP CONTROLLER. TRIP CREATOR ID "+userId);
		try{
			res = tripService.create(dto);
		}
		catch (Exception e) {
			res.setStatus(false);
			//// LOGGER.error("ERROR WHILE CREATING TRIP : " +e.getMessage());
			res.setCode(500);
			res.setData(null);
			res.setMessage(Message.FAILED + " : " + "delivery note is already a part of trip.");
			//e.printStackTrace();
		}
		return res;
	}

	@RequestMapping(method = RequestMethod.GET)
	public ResponseDto findAllTrips(HttpServletRequest request,
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

		LOGGER.error("INSIDE FIND ALL TRIPS CONTROLLER. ADMIN ID "+adminDto.getUserId());
		return tripService.getAllTripsAssociatedWithAdminsDrivers(adminDto);
	}

	@RequestMapping(value = "/search", method = RequestMethod.PUT)
	public ResponseDto find(@RequestBody TripDetailsDTO dto, HttpServletRequest request) {
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
		

		UserDetailsDTO adminDto = (UserDetailsDTO) res.getData();
		LOGGER.error("INSIDE FIND TRIP CONTROLLER WITH USER ID "+userId);
		return tripService.findByParam(dto,adminDto);
	}

	@RequestMapping(value = "/{tripId}", method = RequestMethod.PUT)
	public ResponseDto update(@PathVariable String tripId, @RequestBody TripDetailsDTO dto,
			HttpServletRequest request) {
		String userId = "";
		if (request.getUserPrincipal() != null) {
			userId = request.getUserPrincipal().getName();
		}
		dto.setUpdatedBy(userId);

		dto.setTripId(tripId);
		LOGGER.error("INSIDE UPDATE TRIP CONTROLLER WITH USER ID "+userId);
		return tripService.update(dto);
	}

	@RequestMapping(value = "/{tripId}", method = RequestMethod.DELETE)
	public ResponseDto delete(@PathVariable String tripId) {
		return tripService.delete(tripId);
	}

	@RequestMapping(value = "/filter", method = RequestMethod.PUT)
	public ResponseDto filter(@RequestBody FilterDTO dto, HttpServletRequest request) {
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
		

		UserDetailsDTO adminDto = (UserDetailsDTO) res.getData();
		LOGGER.error("INSIDE FILTER TRIP CONTROLLER WITH USER ID "+userId);
		return tripService.filterTripsAsPerAdmin(adminDto, dto);
		// return tripService.filter(dto);
	}

	@RequestMapping(value = "/assign/{tripId}", method = RequestMethod.GET)
	public ResponseDto assignDriverForTrip(@PathVariable String tripId, HttpServletRequest request) {

		return tripService.assigTripDriver(tripId, request.getUserPrincipal().getName());
	}

	@RequestMapping(value = "/report/{userId}", method = RequestMethod.GET)
	public ResponseDto driverTripHistory(@PathVariable String userId, HttpServletRequest request,
			@RequestParam(value = "start", defaultValue = "0") String start,
			@RequestParam(value = "end", defaultValue = "0") String end) {
		Long s, e;
		s = Long.parseLong(start);
		e = Long.parseLong(end);
		LOGGER.error("INSIDE ASSIGN DRIVER TO TRIP CONTROLLER WITH USER ID "+userId);
		return tripService.getTripHistoryByDriverId(userId, s, e);
	}

	@RequestMapping(value = "/leaderboard", method = RequestMethod.PUT)
	public ResponseDto webLeaderBoard(@RequestBody WebLeaderBoardVO dto, HttpServletRequest request,
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
			return ServicesUtil.getUnauthorizedResponseDto();
		

		UserDetailsDTO adminDto = (UserDetailsDTO) res.getData();
		LOGGER.error("INSIDE LEADERBOARD TRIP CONTROLLER WITH USER ID "+userId);
		return tripService.getLeaderBoardAssociatedWithAdmin(dto, adminDto);
		// return tripService.leaderBoard(dto);
	}

	@RequestMapping(value = "/manifest/{tripId}", method = RequestMethod.GET)
	public ResponseEntity<Resource> getTripManifest(@PathVariable String tripId) throws IOException {

		// ...
		ResponseDto dto = tripService.printTripManiFest(tripId);
		File f = (File) dto.getData();

		HttpHeaders header = new HttpHeaders();
		header.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + "manifest_" + tripId + ".pdf");
		LOGGER.error("INSIDE MENIFEST TRIP CONTROLLER.");
		InputStream stream = new FileInputStream(f);
		InputStreamResource resource = new InputStreamResource(stream);
		return ResponseEntity.ok().headers(header).contentType(MediaType.parseMediaType("application/pdf"))
				.body(resource);
	}
}
