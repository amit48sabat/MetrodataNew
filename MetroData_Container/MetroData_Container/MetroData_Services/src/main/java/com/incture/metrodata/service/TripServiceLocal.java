package com.incture.metrodata.service;

import com.incture.metrodata.dto.FilterDTO;
import com.incture.metrodata.dto.ResponseDto;
import com.incture.metrodata.dto.TripDetailsDTO;
import com.incture.metrodata.dto.UserDetailsDTO;
import com.incture.metrodata.dto.WebLeaderBoardVO;

public interface TripServiceLocal {
	
	ResponseDto create(TripDetailsDTO dto);

//	ResponseDto findById(String requestNo);
	
	ResponseDto findByParam(TripDetailsDTO dto);
    
	ResponseDto delete(String tripId);

	ResponseDto findAll();

	ResponseDto update(TripDetailsDTO Dto);
	
	ResponseDto filter(FilterDTO dto);

	ResponseDto driverDashboardService(String userId);

	ResponseDto assigTripDriver(String tripId, String userId);
	
	ResponseDto getTripHistoryByDriverId(String userDto, Long start, Long end);
	
	ResponseDto leaderBoard(WebLeaderBoardVO dto);
	
	/***
	 * api for finding all the trip as per admin's warehouse id
	 */
	ResponseDto getAllTripsAssociatedWithAdminsDrivers(UserDetailsDTO adminDto);
	
	/***
	 * api for admin dashboard as per admin's warehouse id
	 */
	ResponseDto getAdminDashboardAssociatedWithAdmins(UserDetailsDTO adminDto);

	/***
	 * api for filtering trip as per logged in admin
	 */
	ResponseDto filterTripsAsPerAdmin(UserDetailsDTO adminDto, FilterDTO filterDto);
}
