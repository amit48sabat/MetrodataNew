package com.incture.metrodata.service;

import java.util.Date;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.incture.metrodata.constant.Message;
import com.incture.metrodata.dao.RoleDetailDAO;
import com.incture.metrodata.dto.ResponseDto;
import com.incture.metrodata.dto.RoleDetailsDTO;
import com.incture.metrodata.entity.RoleDetailsDo;
import com.incture.metrodata.util.ServicesUtil;

/**
 * @author Lucky.Barkane
 *
 */
@Service("roleService")
@Transactional
public class RoleService implements RoleServiceLocal{

	@Autowired
	RoleDetailDAO  roleDao;
	
	@Override
	public ResponseDto create(RoleDetailsDTO dto) {
		ResponseDto responseDto = new ResponseDto();
		try {
			// setting created at and updated at
			setCreateAtAndUpdateAt(dto);
			RoleDetailsDo dos = new RoleDetailsDo();
			dto = roleDao.create(dto, dos);
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
	
	
	private void setCreateAtAndUpdateAt(RoleDetailsDTO dto) {
		Date currDate = new Date();
		if (ServicesUtil.isEmpty(dto.getRoleId()))
			dto.setCreatedAt(currDate);
		dto.setUpdatedAt(currDate);
	}


	@Override
	public ResponseDto update(RoleDetailsDTO dto) {
		ResponseDto responseDto = new ResponseDto();
		try {
			// setting created at and updated at
			setCreateAtAndUpdateAt(dto);
			dto = roleDao.update(dto);
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
	public ResponseDto find(RoleDetailsDTO dto) {
		ResponseDto responseDto = new ResponseDto();
		try {
			// setting created at and updated at
			setCreateAtAndUpdateAt(dto);
			dto = roleDao.findById(dto);
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
	public ResponseDto findAll() {
		ResponseDto responseDto = new ResponseDto();
		try {
			RoleDetailsDTO dto = new RoleDetailsDTO();
			Object wareHouseList = roleDao.findAll(dto);

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
	public ResponseDto delete(RoleDetailsDTO dto) {
		ResponseDto responseDto = new ResponseDto();
		try {
			// setting created at and updated at
			//setCreateAtAndUpdateAt(dto);
			roleDao.deleteById(dto);
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
   
	// for internal use only
	@Override
	public RoleDetailsDTO getRoleByRoleName(String roleName){
		RoleDetailsDTO roleDto = null;
		if(!ServicesUtil.isEmpty(roleName)){
			roleDto = roleDao.getRoleByName(roleName);
		}
		return roleDto;
	}
	
}
