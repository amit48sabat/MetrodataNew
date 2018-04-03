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

import com.incture.metrodata.dto.FilterDTO;
import com.incture.metrodata.dto.ResponseDto;
import com.incture.metrodata.dto.TripDetailsDTO;
import com.incture.metrodata.dto.WebLeaderBoardVO;
import com.incture.metrodata.service.TripServiceLocal;

@RestController
@CrossOrigin
@ComponentScan("com.incture")
@RequestMapping(value = "/Trip", produces = "application/json")
public class TripController {

	@Autowired
	TripServiceLocal tripService;

	@RequestMapping(method = RequestMethod.POST)
	public ResponseDto create(@RequestBody TripDetailsDTO dto, HttpServletRequest request) {
		String userId = "";
		if (request.getUserPrincipal() != null) {
			userId = request.getUserPrincipal().getName();
		}
		dto.setCreatedBy(userId);
		dto.setUpdatedBy(userId);
		return tripService.create(dto);
	}

	@RequestMapping(method = RequestMethod.GET)
	public ResponseDto findAllTrips() {
		return tripService.findAll();
	}

	@RequestMapping(value = "/search", method = RequestMethod.PUT)
	public ResponseDto find(@RequestBody TripDetailsDTO dto) {
		return tripService.findByParam(dto);
	}

	@RequestMapping(value = "/{tripId}", method = RequestMethod.PUT)
	public ResponseDto update(@PathVariable String tripId, @RequestBody TripDetailsDTO dto, HttpServletRequest request) {
		String userId = "";
		if (request.getUserPrincipal() != null) {
			userId = request.getUserPrincipal().getName();
		}
		dto.setUpdatedBy(userId);
		
		dto.setTripId(tripId);
		return tripService.update(dto);
	}

	@RequestMapping(value = "/{tripId}", method = RequestMethod.DELETE)
	public ResponseDto delete(@PathVariable String tripId) {
		return tripService.delete(tripId);
	}

	@RequestMapping(value = "/filter", method = RequestMethod.PUT)
	public ResponseDto filter(@RequestBody FilterDTO dto) {
		return tripService.filter(dto);
	}

	@RequestMapping(value = "/assign/{tripId}", method = RequestMethod.GET)
	public ResponseDto assignDriverForTrip(@PathVariable String tripId, HttpServletRequest request) {

		return tripService.assigTripDriver(tripId, request.getUserPrincipal().getName());
	}

	@RequestMapping(value = "/report/{userId}", method = RequestMethod.GET)
	public ResponseDto driverTripHistory(@PathVariable String userId, HttpServletRequest request,@RequestParam( value = "start") Long start,@RequestParam( value = "end") Long end) {

		return tripService.getTripHistoryByDriverId(userId,start,end);
	}

	@RequestMapping(value = "/leaderboard", method = RequestMethod.PUT)
	public ResponseDto webLeaderBoard(@RequestBody WebLeaderBoardVO dto) {
		return tripService.leaderBoard(dto);
	}
}
