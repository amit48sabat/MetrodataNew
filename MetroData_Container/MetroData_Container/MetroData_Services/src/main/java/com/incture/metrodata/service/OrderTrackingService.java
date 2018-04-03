/**
 * 
 */
package com.incture.metrodata.service;

import java.util.Date;

import org.apache.http.HttpStatus;
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

	@Override
	public ResponseDto create(OrderTrackingVO trackingVO) {
		ResponseDto response = new ResponseDto();
		
		try {
			OrderTrackingDTO dto = new OrderTrackingDTO();
			
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

			Object data = orderTrackingDao.findByParam(dto);

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
