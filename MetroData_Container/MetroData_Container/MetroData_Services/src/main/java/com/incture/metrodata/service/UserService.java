package com.incture.metrodata.service;

import java.util.Date;

import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.incture.metrodata.constant.Message;
import com.incture.metrodata.dao.UserDAO;
import com.incture.metrodata.dto.ResponseDto;
import com.incture.metrodata.dto.UserDetailsDTO;
import com.incture.metrodata.entity.UserDetailsDo;
import com.incture.metrodata.util.RESTInvoker;
import com.incture.metrodata.util.ServicesUtil;

@Service("userService")
@Transactional
public class UserService implements UserServiceLocal {
	@Autowired
	UserDAO userDAO;

	@Autowired
	RESTInvoker restInvoker;

	@Override
	public ResponseDto create(UserDetailsDTO dto) {
		ResponseDto responseDto = new ResponseDto();
		try {
			// setting created at and updated at
			setCreateAtAndUpdateAt(dto);
			JsonObject userObj = new JsonObject();
			JsonObject name = new JsonObject();
			JsonArray array = new JsonArray();
			JsonObject email = new JsonObject();
			email.addProperty("value", dto.getEmail());
			array.add(email);
			name.addProperty("familyName", dto.getLastName());
			name.addProperty("givenName", dto.getFirstName());
			//userObj.add("name", name);
			userObj.add("emails", array);
			String respData = restInvoker.postDataToServer("/Users", userObj.toString());
			if (!ServicesUtil.isEmpty(respData)) {
				JSONObject returnObj = new JSONObject(respData);
				UserDetailsDo dos = new UserDetailsDo();
				dto.setUserId(returnObj.getString("id"));
				dto = userDAO.create(dto, dos);
				responseDto.setStatus(true);
				responseDto.setCode(HttpStatus.SC_OK);
				responseDto.setData(dto);
				responseDto.setMessage(Message.SUCCESS.getValue());
			}
			else{
				
			}
		} catch (Exception e) {
			responseDto.setStatus(false);
			responseDto.setCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			responseDto.setMessage(Message.FAILED.getValue());
			e.printStackTrace();
		}
		return responseDto;
	}

	private void setCreateAtAndUpdateAt(UserDetailsDTO dto) {
		Date currDate = new Date();
		if (!ServicesUtil.isEmpty(dto.getUserId()))
			dto.setCreatedAt(currDate);
		dto.setUpdatedAt(currDate);
	}

	@Override
	public ResponseDto update(UserDetailsDTO dto) {
		ResponseDto responseDto = new ResponseDto();
		try {
			// setting created at and updated at
			setCreateAtAndUpdateAt(dto);
			dto = userDAO.update(dto);
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
	public ResponseDto find(UserDetailsDTO dto) {
		ResponseDto responseDto = new ResponseDto();
		try {
			// setting created at and updated at
			dto = userDAO.findById(dto);
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
	public ResponseDto delete(UserDetailsDTO dto) {
		ResponseDto responseDto = new ResponseDto();
		try {
			// setting created at and updated at
			userDAO.deleteById(dto);
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
			UserDetailsDTO dto = new UserDetailsDTO();

			Object wareHouseList = userDAO.findAll(dto);

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
}
