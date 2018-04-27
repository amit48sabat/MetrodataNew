package com.incture.metrodata.service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;

import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.incture.metrodata.constant.Message;
import com.incture.metrodata.constant.RoleConstant;
import com.incture.metrodata.dao.UserDAO;
import com.incture.metrodata.dto.ResponseDto;
import com.incture.metrodata.dto.RoleDetailsDTO;
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
			// error if email id is not set
			if (ServicesUtil.isEmpty(dto.getEmail())) {
				responseDto.setStatus(false);
				responseDto.setCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
				responseDto.setMessage("Email id is required.");
				return responseDto;
			}

			if (!ServicesUtil.isEmpty(dto.getUserId())) {
				responseDto.setStatus(false);
				responseDto.setCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
				responseDto.setMessage("Failed : user with email '" + dto.getEmail()
						+ "' is already exits with user Id " + dto.getUserId());
				return responseDto;
			}

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
			userObj.add("name", name);
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
			} else {
				// user is already in idp we need to create in hana db
				getUserByEmail(dto);
				dto = userDAO.create(dto, new UserDetailsDo());
				responseDto.setStatus(true);
				responseDto.setCode(HttpStatus.SC_OK);
				responseDto.setData(dto);
				responseDto.setMessage(Message.SUCCESS.getValue() + " : User created with id " + dto.getUserId());
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

	@Override
	public ResponseDto createDefaultUser(UserDetailsDTO dto) {
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
			// userObj.add("name", name);
			userObj.add("emails", array);
			userObj.add("name", name);
			String respData = restInvoker.postDataToServer("/Users", userObj.toString());
			if (!ServicesUtil.isEmpty(respData)) {
				JSONObject returnObj = new JSONObject(respData);
				// if user id is set mean user is created in idp need to create
				// in our db also
				if (!ServicesUtil.isEmpty(returnObj.getString("id"))) {

					dto.setUserId(returnObj.getString("id"));
					dto = userDAO.create(dto, new UserDetailsDo());
					responseDto.setStatus(true);
					responseDto.setCode(HttpStatus.SC_OK);
					responseDto.setData(dto);
					responseDto.setMessage(Message.SUCCESS.getValue());
				} else {

				}
			} else {
				// user is already in idp we need to create in hana db
				getUserByEmail(dto);
				dto = userDAO.create(dto, new UserDetailsDo());
				responseDto.setStatus(true);
				responseDto.setCode(HttpStatus.SC_OK);
				responseDto.setData(dto);
				responseDto.setMessage(Message.SUCCESS.getValue());
			}
		} catch (Exception e) {
			responseDto.setStatus(false);
			responseDto.setCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			responseDto.setMessage(Message.FAILED.getValue());
			e.printStackTrace();
		}
		return responseDto;
	}

	@Override
	public UserDetailsDTO getUserByEmail(UserDetailsDTO dto) {
		String q = "";
		try {
			q = "/Users?filter=" + URLEncoder.encode("emails eq '" + dto.getEmail() + "'", "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String respData = restInvoker.getDataFromServer("/Users?filter=" + q);
		{
			if (!ServicesUtil.isEmpty(respData)) {
				JSONObject returnObj = new JSONObject(respData);
				// if user id is set mean user is created in idp need to create
				// in our db also

				if (!ServicesUtil.isEmpty(returnObj.get("Resources"))) {
					String userId = parseUseridFromIdpResponse(returnObj);
					dto.setUserId(userId);
				}
			}
		}
		return dto;
	}

	private String parseUseridFromIdpResponse(JSONObject resources) {

		String id = "";
		JSONArray array = (JSONArray) resources.get("Resources");
		JSONObject res = (JSONObject) array.get(0);
		id = res.get("id").toString();
		return id;
	}

	/***
	 * Get user list associated with admin and return all the users if role is
	 * suoer_admin,sales_admin
	 */
	@Override
	public ResponseDto getUsersAssociatedWithAdmin(UserDetailsDTO dto, String queryParam) {
		ResponseDto responseDto = new ResponseDto();

		try {

			// dto = userDAO.findById(dto);
			Object userList = userDAO.getUsersAssociateWithAdmin(dto.getUserId(), dto.getRole().getRoleName(),
					dto.getWareHouseDetails(),queryParam);

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

	@Override
	public ResponseDto validatedUserRoleByUserId(String userId) {
		ResponseDto responseDto = new ResponseDto();
		try {
			UserDetailsDTO userDetailsDTO = new UserDetailsDTO();
			userDetailsDTO.setUserId(userId);
			userDetailsDTO = userDAO.findById(userDetailsDTO);
			responseDto.setData(userDetailsDTO);
			responseDto.setStatus(checkUserRole(userDetailsDTO.getRole()));
		} catch (Exception e) {
			responseDto = ServicesUtil.getUnauthorizedResponseDto();
			responseDto.setStatus(false);
		}
		return responseDto;
	}

	private boolean checkUserRole(RoleDetailsDTO dto) {
		String role = dto.getRoleName();
		if (role.equals(RoleConstant.ADMIN_INSIDE_JAKARTA.getValue())
				|| role.equals(RoleConstant.ADMIN_OUTSIDE_JAKARTA.getValue())
				|| role.equals(RoleConstant.SUPER_ADMIN.getValue()) 
				|| role.equals(RoleConstant.SALES_ADMIN.getValue())
				|| role.equals(RoleConstant.COURIER_ADMIN.getValue())
				|| role.equals(RoleConstant.INSIDE_JAKARTA_DRIVER.getValue())
				|| role.equals(RoleConstant.OUTSIDE_JAKARTA_DRIVER.getValue()))
			return true;

		return false;
	}
}
