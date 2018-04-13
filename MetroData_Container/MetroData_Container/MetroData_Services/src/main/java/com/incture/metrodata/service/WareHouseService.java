package com.incture.metrodata.service;

import java.util.Date;

import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.incture.metrodata.constant.Message;
import com.incture.metrodata.dao.WareHouseDAO;
import com.incture.metrodata.dto.ResponseDto;
import com.incture.metrodata.dto.WareHouseDetailsDTO;
import com.incture.metrodata.entity.WareHouseDetailsDo;
import com.incture.metrodata.util.HciRestInvoker;
import com.incture.metrodata.util.ServicesUtil;

@Service("wareHouseService")
@Transactional
public class WareHouseService implements WareHouseServiceLocal {

	@Autowired
	WareHouseDAO wareHouseDao;

	@Autowired
	HciRestInvoker hciRestInvoker;

	@Override
	public ResponseDto create(WareHouseDetailsDTO dto) {
		ResponseDto responseDto = new ResponseDto();
		try {
			// setting created at and updated at
			setCreateAtAndUpdateAt(dto);
			WareHouseDetailsDo dos = new WareHouseDetailsDo();
			dto = wareHouseDao.create(dto, dos);
			responseDto.setStatus(true);
			responseDto.setCode(HttpStatus.SC_OK);
			responseDto.setData(dto);
			responseDto.setMessage(Message.SUCCESS.getValue());
		} catch (Exception e) {
			responseDto.setStatus(false);
			responseDto.setCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			responseDto.setMessage(Message.FAILED.getValue());
			e.printStackTrace();
		}
		return responseDto;
	}

	private void setCreateAtAndUpdateAt(WareHouseDetailsDTO dto) {
		Date currDate = new Date();
		if (ServicesUtil.isEmpty(dto.getWareHouseId()))
			dto.setCreatedAt(currDate);
		dto.setUpdatedAt(currDate);
	}

	@Override
	public ResponseDto update(WareHouseDetailsDTO dto) {
		ResponseDto responseDto = new ResponseDto();
		try {
			// setting created at and updated at
			setCreateAtAndUpdateAt(dto);
			dto = wareHouseDao.update(dto);
			responseDto.setStatus(true);
			responseDto.setCode(HttpStatus.SC_OK);
			responseDto.setData(dto);
			responseDto.setMessage(Message.SUCCESS.getValue());
		} catch (Exception e) {
			responseDto.setStatus(false);
			responseDto.setCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			responseDto.setMessage(Message.FAILED.getValue());
			e.printStackTrace();
		}
		return responseDto;
	}

	@Override
	public ResponseDto find(WareHouseDetailsDTO dto) {
		ResponseDto responseDto = new ResponseDto();
		try {
			// setting created at and updated at
			// setCreateAtAndUpdateAt(dto);
			dto = wareHouseDao.findById(dto);
			responseDto.setStatus(true);
			responseDto.setCode(HttpStatus.SC_OK);
			responseDto.setData(dto);
			responseDto.setMessage(Message.SUCCESS.getValue());
		} catch (Exception e) {
			responseDto.setStatus(false);
			responseDto.setCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			responseDto.setMessage(Message.FAILED.getValue());
			e.printStackTrace();
		}
		return responseDto;
	}

	@Override
	public ResponseDto delete(WareHouseDetailsDTO dto) {
		ResponseDto responseDto = new ResponseDto();
		try {
			// setting created at and updated at
			// setCreateAtAndUpdateAt(dto);
			wareHouseDao.deleteById(dto);
			responseDto.setStatus(true);
			responseDto.setCode(HttpStatus.SC_OK);

			responseDto.setMessage(Message.SUCCESS.getValue());
		} catch (Exception e) {
			responseDto.setStatus(false);
			responseDto.setCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			responseDto.setMessage(Message.FAILED.getValue());
			e.printStackTrace();
		}
		return responseDto;
	}

	@Override
	public ResponseDto findAll() {
		ResponseDto responseDto = new ResponseDto();
		try {
			WareHouseDetailsDTO dto = new WareHouseDetailsDTO();

			Object wareHouseList = wareHouseDao.findAll(dto);

			responseDto.setStatus(true);
			responseDto.setCode(HttpStatus.SC_OK);
			responseDto.setData(wareHouseList);
			responseDto.setMessage(Message.SUCCESS.getValue());
		} catch (Exception e) {
			responseDto.setStatus(false);
			responseDto.setCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			responseDto.setMessage(Message.FAILED.getValue());
			e.printStackTrace();
		}
		return responseDto;
	}

	@Override
	public ResponseDto getWareHouseListByUserId(String userId, String roleId) {
		ResponseDto responseDto = new ResponseDto();
		try {
			Object data = wareHouseDao.getWarehouseListByUserId(userId, roleId);

			responseDto.setStatus(true);
			responseDto.setCode(HttpStatus.SC_OK);
			responseDto.setData(data);
			responseDto.setMessage(Message.SUCCESS.getValue());
		} catch (Exception e) {
			responseDto.setStatus(false);
			responseDto.setCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			responseDto.setMessage(Message.FAILED.getValue());
			e.printStackTrace();
		}

		return responseDto;
	}

	@Override
	public ResponseDto refreshWareHouseListFromEcc() {
		ResponseDto responseDto = new ResponseDto();
		JSONObject jsonObject = new JSONObject();
		try {
			
			jsonObject.put("MetroReq", new JSONObject().put("PLANT", 5000));
			String wareHouseString = hciRestInvoker.postDataToServer("/warehouseloc", jsonObject.toString());
			JSONObject wareHouseReturnObj = new JSONObject(wareHouseString);
			JSONArray jsonArray = wareHouseReturnObj.getJSONObject("rfc:ZSD_GET_SLOC.Response").getJSONObject("RETURN")
					.getJSONArray("item");
			WareHouseDetailsDTO wareHouseDetailsDTO;
			for (int i = 0; i < jsonArray.length(); i++) {
				wareHouseDetailsDTO = new WareHouseDetailsDTO();

				wareHouseDetailsDTO.setWareHouseId(jsonArray.getJSONObject(i).getString("LGORT"));
				wareHouseDetailsDTO.setWareHouseName(jsonArray.getJSONObject(i).getString("LGOBE"));
				wareHouseDao.create(wareHouseDetailsDTO, null);
				

			}
			System.out.println("printed");
		} catch (Exception e) {
			
			e.printStackTrace();
		}

		return responseDto;
	}

}
