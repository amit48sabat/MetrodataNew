package com.incture.metrodata.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.maps.GeoApiContext;
import com.incture.metrodata.constant.DeliveryNoteStatus;
import com.incture.metrodata.constant.Message;
import com.incture.metrodata.dao.DeliveryHeaderDAO;
import com.incture.metrodata.dao.TripDAO;
import com.incture.metrodata.dto.DeliveryHeaderDTO;
import com.incture.metrodata.dto.ResponseDto;
import com.incture.metrodata.dto.UserDetailsDTO;
import com.incture.metrodata.entity.DeliveryHeaderDo;
import com.incture.metrodata.exceptions.ExecutionFault;
import com.incture.metrodata.util.ServicesUtil;

@Service("deliveryHeaderService")
@Transactional
public class DeliveryHeaderService implements DeliveryHeaderServiceLocal {

	// private static final Logger LOGGER =
	// LoggerFactory.getLogger(DeliveryHeaderService.class);

	@Autowired
	private DeliveryHeaderDAO deliveryHeaderDao;

	@Autowired
	private DocumentServiceLocal documentServiceLocal;

	@Autowired
	GeoApiContext context;

	@Autowired
	private TripDAO tripDao;
	
	

	/**
	 * api for creating delivery header
	 */
	@Override
	public ResponseDto create(DeliveryHeaderDTO dto) {
		ResponseDto response = new ResponseDto();
		try {
			// setting lat and long
			String address = dto.getShipToAddress();
			Map<String, Double> latAndLong = ServicesUtil.getLatAndLong(address, context);
			if(!ServicesUtil.isEmpty(latAndLong.get("lat")))
			{
				dto.setLatitude(latAndLong.get("lat"));
				dto.setLongitude(latAndLong.get("lng"));
			}

			/*
			 * Date currDate = new Date(); dto.setCreatedDate(currDate);
			 */
           
			DeliveryHeaderDo dos = new DeliveryHeaderDo();
			
			setCreatedAtAndUpdatedAtForDto(dto);
			dto = deliveryHeaderDao.create(dto,dos);

			response.setStatus(true);
			response.setCode(200);
			response.setData(dto);
			response.setMessage(Message.SUCCESS + " : delivery header created");
		} catch (Exception e) {
			response.setStatus(false);
			response.setCode(417);
			response.setMessage(Message.FAILED + " : " + e.toString());
			e.printStackTrace();
		}
		return response;
	}

	private void setCreatedAtAndUpdatedAtForDto(DeliveryHeaderDTO dto) {
		Date currdate = new Date();
		if(ServicesUtil.isEmpty(dto.getCreatedAt()))
		dto.setCreatedAt(currdate);
		
		dto.setUpdatedAt(currdate);

	}

	@Override
	public ResponseDto update(DeliveryHeaderDTO dto) {
		ResponseDto responseDto = new ResponseDto();
		try {
			if (!ServicesUtil.isEmpty(dto.getFileContent())) {

				byte[] decodedString;
				decodedString = Base64.decodeBase64(dto.getFileContent());
				String fileId = documentServiceLocal.upload(decodedString, dto.getFileName(), dto.getFileType());
				if (ServicesUtil.isEmpty(fileId)) {
					throw new ExecutionFault("delivery completion failed due sign upload");
				}
				dto.setSignatureDocId(fileId);
			}
			if (!ServicesUtil.isEmpty(dto.getStatus()))
				updateDeliveryHeaderStatusConstraints(dto);
			// setting lat and long
			if (!ServicesUtil.isEmpty(dto.getShipToAddress())) {
				String address = dto.getShipToAddress();
				Map<String, Double> latAndLong = ServicesUtil.getLatAndLong(address, context);
				dto.setLatitude(latAndLong.get("lat"));
				dto.setLongitude(latAndLong.get("lng"));
			}

			setCreatedAtAndUpdatedAtForDto(dto);
			dto = deliveryHeaderDao.update(dto);

			responseDto.setStatus(true);
			responseDto.setCode(200);
			responseDto.setData(dto);
			responseDto.setMessage(Message.SUCCESS.toString());
		} catch (Exception e) {
			responseDto.setStatus(false);
			responseDto.setCode(417);
			responseDto.setMessage(Message.FAILED + " : " + e.getMessage());
		}
		return responseDto;
	}

	private void updateDeliveryHeaderStatusConstraints(DeliveryHeaderDTO dto) throws Exception {
		Date currDate = new Date();
		DeliveryHeaderDTO tempDTO = new DeliveryHeaderDTO();
		String status = dto.getStatus();
		if (status.equalsIgnoreCase(DeliveryNoteStatus.DELIVERY_NOTE_STARTED.getValue())) {
			dto.setStartedAt(currDate);
		}
		if (status.equalsIgnoreCase(DeliveryNoteStatus.DELIVERY_NOTE_COMPLETED.getValue())
				|| status.equalsIgnoreCase(DeliveryNoteStatus.DELIVERY_NOTE_REJECTED.getValue())
				|| status.equalsIgnoreCase(DeliveryNoteStatus.DELIVERY_NOTE_PARTIALLY_REJECTED.getValue())) {
			dto.setEndedAt(currDate);
		}
		if (status.equals(DeliveryNoteStatus.DELIVERY_NOTE_INVALIDATED.getValue())) {
			tempDTO = deliveryHeaderDao.getByKeys(dto);
			if (!ServicesUtil.isEmpty(tempDTO.getInvalidateIds())) {
				dto.setInvalidateIds(tempDTO.getInvalidateIds() + "," + dto.getInvalidateIds());
			}
		}

	}

	@Override
	public ResponseDto delete(Long headerId) {
		ResponseDto responseDto = new ResponseDto();
		try {
			DeliveryHeaderDTO headerDto = new DeliveryHeaderDTO();
			headerDto.setDeliveryNoteId(headerId);

			deliveryHeaderDao.deleteById(headerDto);

			responseDto.setStatus(true);
			responseDto.setCode(200);
			responseDto.setMessage(Message.SUCCESS.toString());
		} catch (Exception e) {
			responseDto.setStatus(false);
			responseDto.setCode(417);
			responseDto.setMessage(Message.FAILED + " : " + e.getMessage());
		}
		return responseDto;
	}

	@Override
	public ResponseDto findAll() {
		ResponseDto responseDto = new ResponseDto();
		try {

			List<DeliveryHeaderDTO> headerDTOs = deliveryHeaderDao.findAllDeliveryHeaderDtos();

			responseDto.setCode(200);
			responseDto.setStatus(true);
			responseDto.setMessage(Message.SUCCESS + "");
			responseDto.setData(headerDTOs);

		} catch (Exception e) {
			responseDto.setStatus(false);
			responseDto.setCode(417);
			responseDto.setMessage(Message.FAILED + " : " + e.getMessage());
		}
		return responseDto;
	}

	@Override
	/**
	 * api for admin dashbord
	 * 
	 * @return
	 */
	public ResponseDto adminDashboardService() {
		ResponseDto responseDto = new ResponseDto();
		try {
			Map<String, Long> data = new HashMap<>();
			// fetching total trips and total orders
			Object tripAndOrderCount[] = (Object[]) tripDao.getTotalTripsAndOrders();
			Long totalTrips, totalOrders, avgOrders;
			totalTrips = (Long) tripAndOrderCount[0];
			totalOrders = (Long) tripAndOrderCount[1];

			// preventing / zero
			if (totalTrips > 0)
				avgOrders = totalOrders / totalTrips;
			else
				avgOrders = (long) 0;

			data.put("TOTAL_TRIPS", totalTrips);
			data.put("TOTAL_ORDERS", totalOrders);
			data.put("AVG_TRIP_ORDER", avgOrders);
			Iterator<Object> itr;
			Object[] state;
			List<Object> dnCountList = deliveryHeaderDao.getDeliveryNoteCountAsPerStatus();

			if (!ServicesUtil.isEmpty(dnCountList)) {

				itr = dnCountList.iterator();
				while (itr.hasNext()) {
					state = (Object[]) itr.next();
					data.put(state[0] + "", (Long) state[1]);
				}
			}

			responseDto.setCode(200);
			responseDto.setStatus(true);
			responseDto.setMessage(Message.SUCCESS + "");
			responseDto.setData(data);
		} catch (Exception e) {
			responseDto.setStatus(false);
			responseDto.setCode(417);
			responseDto.setMessage(Message.FAILED + " : " + e.getMessage());
			e.printStackTrace();
		}
		return responseDto;
	}

	@Override
	public ResponseDto findById(Long deliveryNoteId) {
		ResponseDto responseDto = new ResponseDto();
		try {
			DeliveryHeaderDTO dto = new DeliveryHeaderDTO();
			dto.setDeliveryNoteId(deliveryNoteId);
			Object data = deliveryHeaderDao.findById(dto);

			responseDto.setStatus(true);
			responseDto.setCode(200);
			responseDto.setData(data);
			responseDto.setMessage(Message.SUCCESS.toString());
		} catch (Exception e) {
			responseDto.setStatus(false);
			responseDto.setCode(417);
			responseDto.setMessage(Message.FAILED + " : " + e.getMessage());
		}
		return responseDto;
	}

	/***
	 * get admins warehouse delivery note 
	 */
	@Override
	public ResponseDto getAllDeliveryNoteByAdminsWareHouse(UserDetailsDTO adminDto) {
		ResponseDto responseDto = new ResponseDto();
		
		try {
			
			//adminDto = userDao.findById(adminDto);
			Object userList = deliveryHeaderDao.getAllDeliveryNoteByAdminsWareHouse(adminDto.getUserId(), adminDto.getRole().getRoleName(), adminDto.getWareHouseDetails());

			responseDto.setStatus(true);
			responseDto.setCode(HttpStatus.SC_OK);
			responseDto.setData(userList);
			responseDto.setMessage(Message.SUCCESS.getValue());
		} catch (Exception e) {
			responseDto.setStatus(false);
			responseDto.setCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			responseDto.setMessage(Message.FAILED.getValue());
			e.printStackTrace();
		}
		return responseDto;
	}

}
