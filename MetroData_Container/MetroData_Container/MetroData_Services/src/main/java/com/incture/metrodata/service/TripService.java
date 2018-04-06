package com.incture.metrodata.service;

import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.incture.metrodata.constant.DeliveryNoteStatus;
import com.incture.metrodata.constant.Message;
import com.incture.metrodata.constant.TripStatus;
import com.incture.metrodata.dao.DeliveryHeaderDAO;
import com.incture.metrodata.dao.TripDAO;
import com.incture.metrodata.dao.UserDAO;
import com.incture.metrodata.dto.DeliveryHeaderDTO;
import com.incture.metrodata.dto.DeliveryItemDTO;
import com.incture.metrodata.dto.FilterDTO;
import com.incture.metrodata.dto.ResponseDto;
import com.incture.metrodata.dto.TripDetailsDTO;
import com.incture.metrodata.dto.UserDetailsDTO;
import com.incture.metrodata.dto.WebLeaderBoardVO;
import com.incture.metrodata.entity.TripDetailsDo;
import com.incture.metrodata.exceptions.ExecutionFault;
import com.incture.metrodata.util.RESTInvoker;
import com.incture.metrodata.util.ServicesUtil;

@Transactional
@Service("tripService")
public class TripService implements TripServiceLocal {

	@Autowired
	TripDAO tripDao;

	@Autowired
	UserDAO userDao;

	@Autowired
	DeliveryHeaderDAO deliveryHeaderDao;

	@Autowired
	RESTInvoker restInvoker;

	/**
	 * api for creating the trip
	 */
	@Override
	public ResponseDto create(TripDetailsDTO dto) {
		ResponseDto responseDto = new ResponseDto();
		try {
			// assigning tripId before processing
			String tripId = SequenceNumberGen.getInstance().getNextSeqNumber("TRIP", 8, tripDao.getSession());
			dto.setTripId(tripId);
			// setting created at and updated at for dto
			setCreatedAtAndUpdatedAtForDto(dto);
			/*
			 * Date currDate = new Date(); dto.setCreatedAt(currDate);
			 * dto.setUpdateAt(currDate);
			 */

			//
			TripDetailsDo dos = new TripDetailsDo();

			//setTripDoForTripCreate(dto, dos);

			dto = tripDao.create(dto, dos);

			responseDto.setStatus(true);
			responseDto.setCode(200);
			responseDto.setData(dto);
			responseDto.setMessage(Message.SUCCESS.toString() + " : Trip created with id " + tripId);
		} catch (Exception e) {
			responseDto.setStatus(false);
			responseDto.setCode(500);
			responseDto.setMessage(Message.FAILED + " : " + e.getMessage());
			e.printStackTrace();
		}
		return responseDto;
	}

	/*private void setTripDoForTripCreate(TripDetailsDTO dto, TripDetailsDo dos) throws Exception {
		DeliveryHeaderDo deliveryHeaderDo;
		// fetching dns dos
		if (!ServicesUtil.isEmpty(dto.getDeliveryHeader())) {
			for (DeliveryHeaderDTO d : dto.getDeliveryHeader()) {
				deliveryHeaderDo = deliveryHeaderDao.getByKeysForFK(d);
				dos.getDeliveryHeader().add(deliveryHeaderDo);
			}
		}

		// fetching user dos
		if (!ServicesUtil.isEmpty(dto.getUser()))
			dos.setUser(userDao.getByKeysForFK(dto.getUser()));

	}*/

	private void setCreatedAtAndUpdatedAtForDto(TripDetailsDTO dto) {
		Date currdate = new Date();

		dto.setUpdatedAt(currdate);
		// setting created by if trip id is empty
		if (ServicesUtil.isEmpty(dto.getTripId())) {
			dto.setCreatedAt(currdate);
		}

		if (!ServicesUtil.isEmpty(dto.getDeliveryHeader())) {
			for (DeliveryHeaderDTO d : dto.getDeliveryHeader()) {
				d.setUpdatedAt(currdate);
			}
		}

	}

	/**
	 * api for updating the trip
	 */
	@Override
	public ResponseDto update(TripDetailsDTO dto) {
		ResponseDto responseDto = new ResponseDto();
		try {
			// setting created at and updated at for dto
			setCreatedAtAndUpdatedAtForDto(dto);

			if (!ServicesUtil.isEmpty(dto.getStatus()))
				updateTripStatusConstraints(dto);
            
			// if user/driver is set than add this driver to trip's delivery notes
			if(!ServicesUtil.isEmpty(dto.getUser()))
			{
				setAssignedUserInDeliveryHeader(dto);
			}
				
			dto = tripDao.update(dto);

			responseDto.setStatus(true);
			responseDto.setCode(200);
			responseDto.setData(dto);
			responseDto.setMessage(Message.SUCCESS.toString() + " : Trip updated with id " + dto.getTripId());
		} catch (Exception e) {
			responseDto.setStatus(false);
			responseDto.setCode(500);
			responseDto.setMessage(Message.FAILED + " : " + e.getMessage());
		}
		return responseDto;
	}

	private void setAssignedUserInDeliveryHeader(TripDetailsDTO dto) {
		if(!ServicesUtil.isEmpty(dto.getDeliveryHeader())){
			 UserDetailsDTO user  = dto.getUser();
			 Set<DeliveryHeaderDTO> deliveryHeader = dto.getDeliveryHeader();
			for(DeliveryHeaderDTO d : deliveryHeader){
				d.setAssignedUser(user.getUserId());
			}
		}
	}

	private void updateTripStatusConstraints(TripDetailsDTO dto) {

		String status = dto.getStatus();
		Date currDate = new Date();
		// if trip status is ENROUTE
		if (status.equalsIgnoreCase(TripStatus.TRIP_STATUS_STARTED.getValue())) {
			dto.setStartTime(currDate);
		}
		// if trip status is ENROUTE
		if (status.equalsIgnoreCase(TripStatus.TRIP_STATUS_COMPLETED.getValue())) {
			dto.setEndTime(currDate);
		}

	}

	/**
	 * api for searching trip by trip id or trip status
	 */

	@Override
	public ResponseDto findByParam(TripDetailsDTO dto) {
		ResponseDto responseDto = new ResponseDto();
		try {
			// find by trip id if trip ID is set or else by trip status
			if (!ServicesUtil.isEmpty(dto.getTripId())) {
				TripDetailsDTO trip = tripDao.findById(dto);
				if (!ServicesUtil.isEmpty(trip.getDeliveryHeader()))
					for (DeliveryHeaderDTO deliveryHeaderDTO : trip.getDeliveryHeader()) {
						if (!ServicesUtil.isEmpty(deliveryHeaderDTO))
							for (DeliveryItemDTO deliveryItemDTO : deliveryHeaderDTO.getDeliveryItems()) {
								if (ServicesUtil.isEmpty(deliveryItemDTO.getSerialNum())) {
									deliveryItemDTO.getScanItems().add(deliveryItemDTO.getMaterial());
								} else {
									String[] serialIds = deliveryItemDTO.getSerialNum().split(",");
									for (String serialId : serialIds) {
										deliveryItemDTO.getScanItems().add(serialId);
									}
								}
							}
					}
				responseDto.setData(trip);
			} else {
				List<TripDetailsDTO> tripList = tripDao.findTripByParam(dto);
				responseDto.setData(tripList);
			}

			responseDto.setStatus(true);
			responseDto.setCode(200);
			responseDto.setMessage(Message.SUCCESS.toString());

		} catch (Exception e) {
			responseDto.setStatus(false);
			responseDto.setCode(500);
			responseDto.setMessage(Message.FAILED + " : " + e.getMessage());
			e.printStackTrace();
		}
		return responseDto;
	}

	/**
	 * api for finding trips by id
	 *//*
		 * @Override public ResponseDto findById(String tripId) { ResponseDto
		 * responseDto = new ResponseDto(); try { TripDetailsDTO dto = new
		 * TripDetailsDTO(); dto.setTripId(tripId); dto = tripDao.findById(dto);
		 * List<TripDetailsDTO> dtoList = new ArrayList<TripDetailsDTO>();
		 * dtoList.add(dto); responseDto.setStatus(true);
		 * responseDto.setMessage(Message.SUCCESS.toString());
		 * responseDto.setDataList(dtoList); } catch (Exception e) {
		 * responseDto.setStatus(false);
		 * responseDto.setMessage(Message.FAILED.toString() + " : " +
		 * e.getMessage()); } return responseDto; }
		 */

	/**
	 * api for deleting the trip by trip id
	 */
	@Override
	public ResponseDto delete(String tripId) {
		ResponseDto responseDto = new ResponseDto();
		try {
			TripDetailsDTO tripDetailsDTO = new TripDetailsDTO();
			tripDetailsDTO.setTripId(tripId);

			tripDao.deleteById(tripDetailsDTO);

			responseDto.setStatus(true);
			responseDto.setCode(200);
			responseDto.setMessage(Message.SUCCESS.toString());
		} catch (Exception e) {
			responseDto.setStatus(false);
			responseDto.setCode(500);
			responseDto.setMessage(Message.FAILED + " : " + e.getMessage());
		}
		return responseDto;
	}

	/**
	 * api for finding all the trips
	 */
	@Override
	public ResponseDto findAll() {
		ResponseDto responseDto = new ResponseDto();
		try {
			TripDetailsDTO tripDetailsDTO = new TripDetailsDTO();

			Object tripList = tripDao.findAll(tripDetailsDTO);

			responseDto.setStatus(true);
			responseDto.setCode(200);
			responseDto.setData(tripList);
			responseDto.setMessage(Message.SUCCESS.toString());
		} catch (Exception e) {
			responseDto.setStatus(false);
			responseDto.setCode(500);
			responseDto.setMessage(Message.FAILED + " : " + e.getMessage());
		}
		return responseDto;
	}

	/**
	 * api for filtering like trip,order,driver id
	 */
	@Override
	public ResponseDto filter(FilterDTO dto) {
		ResponseDto responseDto = new ResponseDto();
		try {
			Object data = tripDao.filterRecords(dto);
			responseDto.setStatus(true);
			responseDto.setCode(200);
			responseDto.setData(data);
			responseDto.setMessage(Message.SUCCESS + "");

		} catch (Exception e) {
			responseDto.setStatus(false);
			responseDto.setCode(500);
			responseDto.setMessage(Message.FAILED + " : " + e.getMessage());
		}
		return responseDto;
	}

	@Override
	/**
	 * api for driver dashboard
	 * 
	 * @param dto
	 * @return
	 */
	public ResponseDto driverDashboardService(String userId) {
		ResponseDto responseDto = new ResponseDto();

		try {

			// fetching driver trip report
			Object tripReport = tripDao.getDriverDashboardDetails(userId);
			Object deliveryNoteReport = tripDao.getDriversDeliveryNoteReport(userId);
			// fetching driver profile
			String userProfileString = restInvoker.getDataFromServer("/Users/" + userId);
			JsonParser parser = new JsonParser();
			JsonElement mJson = parser.parse(userProfileString);
			Gson gson = new Gson();
			Object object = gson.fromJson(mJson, Object.class);

			// setting up response
			Map<String, Object> map = new HashMap<>();
			map.put("profile", object);
			map.put("tripReport", tripReport);
			map.put("deliveryNoteReport", deliveryNoteReport);
			responseDto.setCode(200);
			responseDto.setStatus(true);
			responseDto.setData(map);
			responseDto.setMessage(Message.SUCCESS + "");
		} catch (Exception e) {
			responseDto.setCode(500);
			responseDto.setStatus(false);
			e.printStackTrace();
			responseDto.setMessage(e.getMessage());
		}

		return responseDto;
	}

	@Override
	public ResponseDto assigTripDriver(String tripId, String userId) {
		// TODO Auto-generated method stub
		ResponseDto responseDto = new ResponseDto();
		TripDetailsDTO dto = new TripDetailsDTO();
		dto.setTripId(tripId);
		try {
			dto = tripDao.findById(dto);
			UserDetailsDTO userDetailsDTO = new UserDetailsDTO();
			if (ServicesUtil.isEmpty(dto.getUser())) {

				userDetailsDTO.setUserId(userId);
				try {
					userDetailsDTO = userDao.getByKeys(userDetailsDTO);
				} catch (Exception e) {
					throw new ExecutionFault("Driver details doesn't exist please log in and try again");
				}

				dto.setUser(userDetailsDTO);
				tripDao.update(dto);
				responseDto.setStatus(true);

				TripDetailsDTO detailsDTO = new TripDetailsDTO();
				detailsDTO.setTripId(dto.getTripId());
				responseDto.setData(detailsDTO);
				responseDto.setCode(HttpStatus.SC_OK);
			} else {
				throw new ExecutionFault(
						"Trip with id " + dto.getTripId() + " is already assigned to driver please scan a new trip ");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			responseDto.setStatus(false);
			responseDto.setCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			responseDto.setMessage(e.getMessage());

		}

		return responseDto;
	}

	/***
	 * Api for getting trip history by driver id
	 */
	@Override
	public ResponseDto getTripHistoryByDriverId(String userId,Long start, Long end) {
		ResponseDto responseDto = new ResponseDto();

		try {
			List<TripDetailsDTO> tripReport = (List<TripDetailsDTO>) tripDao.getTripHistoryByDriverId(userId,start,end);
			Map<String, BigInteger> deliveryNoteReport = tripDao.getDriversDeliveryNoteReport(userId);
			long totalTrips = 0L, totalDeliveryNotes = 0L, avgDnsPerTrip = 0;

			if (!ServicesUtil.isEmpty(tripReport))
				totalTrips = tripReport.size();
			if (!ServicesUtil.isEmpty(deliveryNoteReport))
				totalDeliveryNotes = deliveryNoteReport.values().stream().mapToInt(Number::intValue).sum();

			if (totalTrips > 0)
				avgDnsPerTrip = totalDeliveryNotes / totalTrips;

			Map<String, Object> resMap = new HashMap<>();
			resMap.put("tripReport", tripReport);
			resMap.put("deliveryNoteReport", deliveryNoteReport);
			resMap.put("totalTrips", totalTrips);
			resMap.put("totalDeliveryNotes", totalDeliveryNotes);
			resMap.put("avgDnsPerTrip", avgDnsPerTrip);

			// setting the response
			responseDto.setCode(HttpStatus.SC_OK);
			responseDto.setStatus(true);
			responseDto.setData(resMap);
			responseDto
					.setMessage(Message.SUCCESS + " : Fetching trip and delivery note report for driver id " + userId);

		} catch (Exception e) {
			e.printStackTrace();
			responseDto.setStatus(false);
			responseDto.setCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			responseDto.setMessage(e.getMessage());
		}
		return responseDto;
	}

	/**
	 * api for web leader board
	 */
	@Override
	public ResponseDto leaderBoard(WebLeaderBoardVO dto) {
		ResponseDto responseDto = new ResponseDto();
		try {
            
			if(ServicesUtil.isEmpty(dto.getFirstResult()) || ServicesUtil.isEmpty(dto.getMaxResult()))
			{
				dto.setFirstResult(0); dto.setMaxResult(10);
			}
			
			if (!ServicesUtil.isEmpty(dto.getSortBy()))
				dto.validateSortBy();
			else
				dto.setSortBy(DeliveryNoteStatus.TOTAL_DEL_NOTE.getValue());

			//Set<Map<?, ?>> driverReport = new TreeSet(new LeaderBoardComparator(sortBy));
			List<Map<String, Object>> resultSet = null;

			resultSet = tripDao.getWebLeaderBoard(dto);

			// setting the response
			responseDto.setCode(200);
			responseDto.setStatus(true);
			responseDto.setData(resultSet);
			responseDto.setMessage(Message.SUCCESS.getValue());

		} catch (Exception e) {
			e.printStackTrace();
			responseDto.setStatus(false);
			responseDto.setCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			responseDto.setMessage(e.getMessage());
		}
		return responseDto;
	}
}
