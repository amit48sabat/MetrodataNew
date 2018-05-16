package com.incture.metrodata.service;

import java.util.Date;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.incture.metrodata.constant.Message;
import com.incture.metrodata.constant.RoleConstant;
import com.incture.metrodata.dao.RoleDetailDAO;
import com.incture.metrodata.dao.UserDAO;
import com.incture.metrodata.dto.ResponseDto;
import com.incture.metrodata.dto.RoleDetailsDTO;
import com.incture.metrodata.dto.UserDetailsDTO;
import com.incture.metrodata.entity.RoleDetailsDo;
import com.incture.metrodata.util.ServicesUtil;

/**
 * @author Lucky.Barkane
 *
 */
@Service("roleService")
@Transactional
public class RoleService implements RoleServiceLocal {

	@Autowired
	RoleDetailDAO roleDao;

	@Autowired
	UserDAO userDao;

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
			// setCreateAtAndUpdateAt(dto);
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
	public RoleDetailsDTO getRoleByRoleName(String roleName) {
		RoleDetailsDTO roleDto = null;
		if (!ServicesUtil.isEmpty(roleName)) {
			roleDto = roleDao.getRoleByName(roleName);
		}
		return roleDto;
	}

	/**
	 * get role associated with admin
	 */
	@Override
	public ResponseDto getRoleByUser(UserDetailsDTO adminDto) {
		ResponseDto responseDto = new ResponseDto();

		try {

			// adminDto = userDao.findById(adminDto);

			Object data = roleDao.getRoleByUser(adminDto.getUserId(), adminDto.getRole().getRoleName(),
					adminDto.getWareHouseDetails());

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

	/**
	 * This will create all the roles for when app run
	 */
	@Override
	public void createAllRoles() {
		try {
			String salesAdmin = RoleConstant.SALES_ADMIN.getValue();
			String adminInside = RoleConstant.ADMIN_INSIDE_JAKARTA.getValue();
			String outsideAdmin = RoleConstant.ADMIN_OUTSIDE_JAKARTA.getValue();
			String courierAdmin = RoleConstant.COURIER_ADMIN.getValue();
			String driverInside = RoleConstant.INSIDE_JAKARTA_DRIVER.getValue();
			String driverOutside = RoleConstant.OUTSIDE_JAKARTA_DRIVER.getValue();

			RoleDetailsDTO tempRole = null;

			// sales admin
			tempRole = roleDao.getRoleByName(salesAdmin);
			if (ServicesUtil.isEmpty(tempRole.getRoleName())) {
				RoleDetailsDTO salesAdminDto = new RoleDetailsDTO();
				salesAdminDto.setDisplayName(RoleConstant.getDisplayValue(salesAdmin));
				salesAdminDto.setRoleName(salesAdmin);
				setCreateAtAndUpdateAt(salesAdminDto);
				roleDao.create(salesAdminDto,null);
			}

			// admin inside jakarta
			tempRole = null;
			tempRole = roleDao.getRoleByName(adminInside);
			if (ServicesUtil.isEmpty(tempRole.getRoleName())) {
				RoleDetailsDTO adminInsideDto = new RoleDetailsDTO();
				adminInsideDto.setDisplayName(RoleConstant.getDisplayValue(adminInside));
				adminInsideDto.setRoleName(adminInside);
				setCreateAtAndUpdateAt(adminInsideDto);
				roleDao.create(adminInsideDto,null);
			}

			// driver inside jakarta
			tempRole = null;
			tempRole = roleDao.getRoleByName(driverInside);
			if (ServicesUtil.isEmpty(tempRole.getRoleName())) {
				RoleDetailsDTO insideJkDriverDto = new RoleDetailsDTO();
				insideJkDriverDto.setDisplayName(RoleConstant.getDisplayValue(driverInside));
				insideJkDriverDto.setRoleName(driverInside);
				insideJkDriverDto.setUserType(adminInside);
				setCreateAtAndUpdateAt(insideJkDriverDto);
				roleDao.create(insideJkDriverDto,null);
			}

			// admin outside jakarta

			tempRole = null;
			tempRole = roleDao.getRoleByName(outsideAdmin);
			if (ServicesUtil.isEmpty(tempRole.getRoleName())) {
				RoleDetailsDTO adminOutsideDto = new RoleDetailsDTO();
				adminOutsideDto.setDisplayName(RoleConstant.getDisplayValue(outsideAdmin));
				adminOutsideDto.setRoleName(outsideAdmin);
				setCreateAtAndUpdateAt(adminOutsideDto);
				roleDao.create(adminOutsideDto,null);
			}

			// courier admin
			tempRole = null;
			tempRole = roleDao.getRoleByName(courierAdmin);
			if (ServicesUtil.isEmpty(tempRole.getRoleName())) {
				RoleDetailsDTO courierAdminDto = new RoleDetailsDTO();
				courierAdminDto.setDisplayName(RoleConstant.getDisplayValue(courierAdmin));
				courierAdminDto.setRoleName(courierAdmin);
				courierAdminDto.setUserType(outsideAdmin);
				setCreateAtAndUpdateAt(courierAdminDto);
				roleDao.create(courierAdminDto,null);
			}

			// driver outside jakarta
			tempRole = null;
			tempRole = roleDao.getRoleByName(driverOutside);
			if (ServicesUtil.isEmpty(tempRole.getRoleName())) {
				RoleDetailsDTO outsideJkDriver = new RoleDetailsDTO();
				outsideJkDriver.setDisplayName(RoleConstant.getDisplayValue(driverOutside));
				outsideJkDriver.setRoleName(driverOutside);
				outsideJkDriver.setUserType(courierAdmin);
				setCreateAtAndUpdateAt(outsideJkDriver);
				roleDao.create(outsideJkDriver,null);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
