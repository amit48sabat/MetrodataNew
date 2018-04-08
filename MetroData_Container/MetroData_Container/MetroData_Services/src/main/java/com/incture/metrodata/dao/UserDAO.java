package com.incture.metrodata.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.incture.metrodata.constant.RoleConstant;
import com.incture.metrodata.dto.UserDetailsDTO;
import com.incture.metrodata.dto.WareHouseDetailsDTO;
import com.incture.metrodata.entity.UserDetailsDo;
import com.incture.metrodata.util.CourierDetailsComparator;
import com.incture.metrodata.util.ServicesUtil;
import com.incture.metrodata.util.WareHouseComparator;

@Repository("UserDao")
public class UserDAO extends BaseDao<UserDetailsDo, UserDetailsDTO> {

	@Autowired
	RoleDetailDAO roleDao;

	@Autowired
	WareHouseDAO wareHouseDao;

	@Autowired
	CourierDetailsDAO courierDao;

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
			if (!ServicesUtil.isEmpty(userDetailsDTO.getTelephone())) {
				detailsDo.setTelephone(userDetailsDTO.getTelephone());
			}
			if (!ServicesUtil.isEmpty(userDetailsDTO.getRole())) {

				detailsDo.setRole(roleDao.importDto(userDetailsDTO.getRole(), detailsDo.getRole()));
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

			/*
			 * if (!ServicesUtil.isEmpty(userDetailsDTO.getWareHouseId())) {
			 * detailsDo.setWareHouseId(userDetailsDTO.getWareHouseId()); } if
			 * (!ServicesUtil.isEmpty(userDetailsDTO.getCourierId())) {
			 * detailsDo.setCourierId(userDetailsDTO.getCourierId()); }
			 */

			// parsing warehouse details
			if (!ServicesUtil.isEmpty(userDetailsDTO.getWareHouseDetails())) {

				detailsDo.setWareHouseDetails(
						wareHouseDao.importSet(userDetailsDTO.getWareHouseDetails(), detailsDo.getWareHouseDetails()));
			}

			// parsing courier details
			if (!ServicesUtil.isEmpty(userDetailsDTO.getCourierDetails())) {

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
			if (!ServicesUtil.isEmpty(detailsDo.getRole())) {

				userDetailsDTO.setRole(roleDao.exportDto(detailsDo.getRole()));
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

			if (!ServicesUtil.isEmpty(detailsDo.getFirstName())) {
				userDetailsDTO.setFirstName(detailsDo.getFirstName());
			}
			if (!ServicesUtil.isEmpty(detailsDo.getLastName())) {
				userDetailsDTO.setLastName(detailsDo.getLastName());
			}
			if (!ServicesUtil.isEmpty(detailsDo.getEmail())) {
				userDetailsDTO.setEmail(detailsDo.getEmail());
			}
			if (!ServicesUtil.isEmpty(detailsDo.getParentId())) {
				userDetailsDTO.setParentId(detailsDo.getParentId());
			}

			if (!ServicesUtil.isEmpty(detailsDo.getTelephone())) {
				userDetailsDTO.setTelephone(detailsDo.getTelephone());
			}
			/*
			 * if (!ServicesUtil.isEmpty(detailsDo.getWareHouseId())) {
			 * userDetailsDTO.setWareHouseId(detailsDo.getWareHouseId()); } if
			 * (!ServicesUtil.isEmpty(detailsDo.getCourierId())) {
			 * userDetailsDTO.setCourierId(detailsDo.getCourierId()); }
			 */

			// parsing warehouse details
			if (!ServicesUtil.isEmpty(detailsDo.getWareHouseDetails())) {

				userDetailsDTO.setWareHouseDetails(
						wareHouseDao.exportSet(detailsDo.getWareHouseDetails(), new WareHouseComparator()));
			}

			// parsing courier details
			if (!ServicesUtil.isEmpty(detailsDo.getCourierDetails())) {

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

	@SuppressWarnings("unchecked")
	public List<UserDetailsDTO> getUsersAssociateWithAdmin(String adminId, String roleName,
			Set<WareHouseDetailsDTO> wareHouseDetails) {
		List<Long> wareHouseIds = new ArrayList<Long>();
		for (WareHouseDetailsDTO wareHouse : wareHouseDetails)
			wareHouseIds.add(wareHouse.getWareHouseId());
		boolean isSuperAdmin = false;
		String hql = "";
		// get all the user list if role is super_admin or sales_admin
		if (roleName.equals(RoleConstant.SUPER_ADMIN.getValue())
				|| roleName.equals(RoleConstant.SALES_ADMIN.getValue())) {
			hql = "SELECT u FROM UserDetailsDo AS u WHERE u.userId !=:adminId";
			isSuperAdmin = true;
		} else
			hql = "SELECT u FROM UserDetailsDo AS  u inner join u.wareHouseDetails AS w WHERE w.wareHouseId IN (:warehouselist) AND u.userId !=:adminId";
		Query query = getSession().createQuery(hql);
		if(!isSuperAdmin)
		query.setParameterList("warehouselist", wareHouseIds);
		
		query.setParameter("adminId", adminId);
		ArrayList<UserDetailsDo> result = (ArrayList<UserDetailsDo>) query.list();
		return exportList(result);
	}
}
