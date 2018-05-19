/**
 * 
 */
package com.incture.metrodata.service;

import java.util.Date;
import java.util.List;

import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.incture.metrodata.constant.Message;
import com.incture.metrodata.dao.OrderTrackingDAO;
import com.incture.metrodata.dao.UserDAO;
import com.incture.metrodata.dto.LocationVO;
import com.incture.metrodata.dto.OrderTrackingDTO;
import com.incture.metrodata.dto.OrderTrackingVO;
import com.incture.metrodata.dto.ResponseDto;
import com.incture.metrodata.dto.UserDetailsDTO;
import com.incture.metrodata.entity.OrderTrackingDo;
import com.incture.metrodata.util.ServicesUtil;

/**
 * @author Lucky.Barkane
 *
 */
@Service("orderTrackingService")
@Transactional
public class OrderTrackingService implements OrderTrackingServiceLocal {

	@Autowired
	OrderTrackingDAO orderTrackingDao;

	@Autowired
	UserDAO userDao;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(OrderTrackingService.class);
	
	@Override
	public ResponseDto create(OrderTrackingVO trackingVO) {
		ResponseDto response = new ResponseDto();
		
		try {
			OrderTrackingDTO dto = new OrderTrackingDTO();
			LOGGER.error("INSIDE CREATE ORDER TRACKING. REQUESTING PAYLOAD => "+trackingVO);
			dto.setTripId(trackingVO.getTripId());
			dto.setDriverId(trackingVO.getDriverId());
			
			Date date = new Date();
			dto.setCreatedAt(date);

			for ( LocationVO location: trackingVO.getLocations()) {
				dto.setDeliveryNoteId(location.getDeliveryNoteId());
				dto.setLatitude(location.getLatitude());
				dto.setLongitude(location.getLongitude());
				dto = orderTrackingDao.create(dto,new OrderTrackingDo());
			}
			

			// updating the drivers lat and lng
			if (!ServicesUtil.isEmpty(dto.getDriverId())) {
				updateDriversLatAndLng(dto);
			}

			response.setStatus(true);
			response.setCode(HttpStatus.SC_OK);
			response.setData(dto);
			response.setMessage(Message.SUCCESS + " : Order track created");
		} catch (Exception e) {
			response.setStatus(false);
			response.setCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			response.setMessage(Message.FAILED + " : " + e.toString());
			e.printStackTrace();
		}
		return response;
	}

	private void updateDriversLatAndLng(OrderTrackingDTO dto) throws Exception {
		LOGGER.error("INSIDE updateDriversLatAndLng() OF ORDER TRACKING SERVICE");
		UserDetailsDTO userDto = new UserDetailsDTO();
		userDto.setUserId(dto.getDriverId());
		userDto.setLatitude(dto.getLatitude());
		userDto.setLongitude(dto.getLongitude());

		userDao.update(userDto);
	}

	@Override
	public ResponseDto findByParam(OrderTrackingDTO dto) {
		ResponseDto response = new ResponseDto();
		try {

			List<OrderTrackingDTO> data = orderTrackingDao.findByParam(dto);
			LOGGER.error("INSIDE FIND BY PARAM OF ORDER TRACKING SERVICE");
			// added trip location as last updated driver location if driver is idle
			if(ServicesUtil.isEmpty(data))
			{
				OrderTrackingDTO orderTrackingDTO = new OrderTrackingDTO();
				UserDetailsDTO detailsDTO =new UserDetailsDTO();
				detailsDTO.setUserId(dto.getDriverId());	
				detailsDTO =userDao.findById(detailsDTO);
				orderTrackingDTO.setLatitude(detailsDTO.getLatitude());
				orderTrackingDTO.setLongitude(detailsDTO.getLongitude());
			}
			response.setStatus(true);
			response.setCode(HttpStatus.SC_OK);
			response.setData(data);
			response.setMessage(Message.SUCCESS + "");
		} catch (Exception e) {
			response.setStatus(false);
			response.setCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			response.setMessage(Message.FAILED + " : " + e.toString());
			e.printStackTrace();
		}
		return response;
	}

}
