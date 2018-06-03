package com.incture.metrodata.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.incture.metrodata.constant.Message;
import com.incture.metrodata.dao.DeliveryItemDAO;
import com.incture.metrodata.dto.DeliveryItemDTO;
import com.incture.metrodata.dto.ResponseDto;
import com.incture.metrodata.entity.DeliveryItemDo;

@Service("deliveryItemService")
@Transactional
public class DeliveryItemService implements DeliveryItemServiceLocal{
     
	@Autowired
	DeliveryItemDAO deliveryItemDao;
	
	/**
	 * api for delivery item creation
	 */
	@Override
	public ResponseDto create(DeliveryItemDTO dto) {
		ResponseDto response = new ResponseDto();
		
		try {

			deliveryItemDao.create(dto, new DeliveryItemDo());

			response.setStatus(true);
			response.setCode(200);
			response.setData(dto);
			response.setMessage(Message.SUCCESS + " : delivery item created");
		} catch (Exception e) {
			response.setStatus(false);
			response.setCode(417);
			response.setMessage(Message.FAILED + " : " + e.toString());
			e.printStackTrace();
		}
		return response;
	}
	
	/**
	 * api for updating delivery item 
	 */
	@Override
	public ResponseDto update(DeliveryItemDTO dto) {
		ResponseDto responseDto = new ResponseDto();
		try {
			deliveryItemDao.update(dto);

			responseDto.setStatus(true);
			responseDto.setCode(200);
			//responseDto.setData(dto);
			responseDto.setMessage(Message.SUCCESS.toString());
		} catch (Exception e) {
			responseDto.setStatus(false);
			responseDto.setCode(417);
			responseDto.setMessage(Message.FAILED + " : " + e.getMessage());
		}
		return responseDto;
	}
 
	/**
	 * api for finding delivery item by id
	 */
	@Override
	public ResponseDto findById(Long itemId) {
		ResponseDto responseDto = new ResponseDto();
		try {
			DeliveryItemDTO dto = new DeliveryItemDTO();
			dto.setDeliveryItemId(itemId);
			Object data  = deliveryItemDao.findById(dto);

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

	
	/**
	 * api for deleting delivery item by id
	 */
	@Override
	public ResponseDto delete(Long itemId) {
		ResponseDto responseDto = new ResponseDto();
		try {
			DeliveryItemDTO itemDto = new DeliveryItemDTO();
			itemDto.setDeliveryItemId(itemId);

			deliveryItemDao.deleteById(itemDto);

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
	public Integer deleteUnlinkDeliveryItems() {
		// TODO Auto-generated method stub
		return deliveryItemDao.deleteUnlinkDeliveryItems();
	}
	
}
