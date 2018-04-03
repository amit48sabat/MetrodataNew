package com.incture.metrodata.dao;

import org.springframework.stereotype.Repository;

import com.incture.metrodata.dto.UserDetailsDTO;
import com.incture.metrodata.entity.UserDetailsDo;
import com.incture.metrodata.util.CourierDetailsComparator;
import com.incture.metrodata.util.ServicesUtil;
import com.incture.metrodata.util.WareHouseComparator;

@Repository("UserDao")
public class UserDAO extends BaseDao<UserDetailsDo, UserDetailsDTO> {

	@Override
	public UserDetailsDo importDto(UserDetailsDTO userDetailsDTO, UserDetailsDo detailsDo) throws Exception {
		if (ServicesUtil.isEmpty(detailsDo))
			detailsDo = new UserDetailsDo();

		if (!ServicesUtil.isEmpty(userDetailsDTO)) {
			if (!ServicesUtil.isEmpty(userDetailsDTO.getUserId())) {
				detailsDo.setUserId(userDetailsDTO.getUserId());
			}
			if (!ServicesUtil.isEmpty(userDetailsDTO.getFirstName())) {
				detailsDo.setFirstName(userDetailsDTO.getFirstName());
			}
			if (!ServicesUtil.isEmpty(userDetailsDTO.getLastName())) {
				detailsDo.setLastName(userDetailsDTO.getLastName());
			}
			if (!ServicesUtil.isEmpty(userDetailsDTO.getEmail())) {
				detailsDo.setEmail(userDetailsDTO.getEmail());
			}
			if (!ServicesUtil.isEmpty(userDetailsDTO.getName())) {
				detailsDo.setName(userDetailsDTO.getName());
			}
			if (!ServicesUtil.isEmpty(userDetailsDTO.getUserType())) {
				detailsDo.setUserType(userDetailsDTO.getUserType());
			}
			if (!ServicesUtil.isEmpty(userDetailsDTO.getMobileToken())) {
				detailsDo.setMobileToken(userDetailsDTO.getMobileToken());
			}
			if (!ServicesUtil.isEmpty(userDetailsDTO.getWebToken())) {
				detailsDo.setWebToken(userDetailsDTO.getWebToken());
			}
			if (!ServicesUtil.isEmpty(userDetailsDTO.getLatitude())) {
				detailsDo.setLatitude(userDetailsDTO.getLatitude());
			}
			if (!ServicesUtil.isEmpty(userDetailsDTO.getLongitude())) {
				detailsDo.setLongitude(userDetailsDTO.getLongitude());
			}
			if (!ServicesUtil.isEmpty(userDetailsDTO.getCreatedDate())) {
				detailsDo.setCreatedDate(userDetailsDTO.getCreatedDate());
			}
			if (!ServicesUtil.isEmpty(userDetailsDTO.getParentId())) {
				detailsDo.setParentId(userDetailsDTO.getParentId());
			}

			// parsing warehouse details
			if (!ServicesUtil.isEmpty(userDetailsDTO.getWareHouseDetails())) {
				WareHouseDAO wareHouseDao = new WareHouseDAO();
				detailsDo.setWareHouseDetails(
						wareHouseDao.importSet(userDetailsDTO.getWareHouseDetails(), detailsDo.getWareHouseDetails()));
			}

			// parsing courier details
			if (!ServicesUtil.isEmpty(userDetailsDTO.getCourierDetails())) {
				CourierDetailsDAO courierDao = new CourierDetailsDAO();
				detailsDo.setCourierDetails(
						courierDao.importSet(userDetailsDTO.getCourierDetails(), detailsDo.getCourierDetails()));
			}

		}
		return detailsDo;
	}

	@Override
	UserDetailsDTO exportDto(UserDetailsDo detailsDo) {
		UserDetailsDTO userDetailsDTO = new UserDetailsDTO();
		if (!ServicesUtil.isEmpty(detailsDo)) {
			if (!ServicesUtil.isEmpty(detailsDo.getUserId())) {
				userDetailsDTO.setUserId(detailsDo.getUserId());
			}
			if (!ServicesUtil.isEmpty(detailsDo.getName())) {
				userDetailsDTO.setName(detailsDo.getName());
			}
			if (!ServicesUtil.isEmpty(detailsDo.getUserType())) {
				userDetailsDTO.setUserType(detailsDo.getUserType());
			}
			if (!ServicesUtil.isEmpty(detailsDo.getMobileToken())) {
				userDetailsDTO.setMobileToken(detailsDo.getMobileToken());
			}
			if (!ServicesUtil.isEmpty(detailsDo.getWebToken())) {
				userDetailsDTO.setWebToken(detailsDo.getWebToken());
			}
			if (!ServicesUtil.isEmpty(detailsDo.getLatitude())) {
				userDetailsDTO.setLatitude(detailsDo.getLatitude());
			}
			if (!ServicesUtil.isEmpty(detailsDo.getLongitude())) {
				userDetailsDTO.setLongitude(detailsDo.getLongitude());
			}
			if (!ServicesUtil.isEmpty(detailsDo.getCreatedDate())) {
				userDetailsDTO.setCreatedDate(detailsDo.getCreatedDate());
			}

			if (ServicesUtil.isEmpty(detailsDo.getFirstName())) {
				userDetailsDTO.setFirstName(detailsDo.getFirstName());
			}
			if (ServicesUtil.isEmpty(detailsDo.getLastName())) {
				userDetailsDTO.setLastName(detailsDo.getLastName());
			}
			if (ServicesUtil.isEmpty(detailsDo.getEmail())) {
				userDetailsDTO.setEmail(detailsDo.getEmail());
			}
			if (!ServicesUtil.isEmpty(detailsDo.getParentId())) {
				userDetailsDTO.setParentId(detailsDo.getParentId());
			}

			// parsing warehouse details
			if (!ServicesUtil.isEmpty(userDetailsDTO.getWareHouseDetails())) {
				WareHouseDAO wareHouseDao = new WareHouseDAO();
				userDetailsDTO.setWareHouseDetails(
						wareHouseDao.exportSet(detailsDo.getWareHouseDetails(), new WareHouseComparator()));
			}

			// parsing courier details
			if (!ServicesUtil.isEmpty(userDetailsDTO.getCourierDetails())) {
				CourierDetailsDAO courierDao = new CourierDetailsDAO();
				userDetailsDTO.setCourierDetails(
						courierDao.exportSet(detailsDo.getCourierDetails(), new CourierDetailsComparator()));
			}
		}
		return userDetailsDTO;
	}

	public Boolean updateUserData(UserDetailsDTO userDetailsDTO) throws Exception {
		UserDetailsDo userDetailsDo = null;
		try {
			userDetailsDo = getByKeysForFK(userDetailsDTO);

		} catch (Exception e) {
			e.printStackTrace();
		}
		getSession().saveOrUpdate(importDto(userDetailsDTO, userDetailsDo));

		return true;
	}

}
