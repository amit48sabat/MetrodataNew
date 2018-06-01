package com.incture.metrodata.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.incture.metrodata.constant.RoleConstant;
import com.incture.metrodata.dto.UserDetailsDTO;
import com.incture.metrodata.dto.WareHouseDetailsDTO;
import com.incture.metrodata.entity.RoleDetailsDo;
import com.incture.metrodata.entity.UserDetailsDo;
import com.incture.metrodata.util.CourierDetailsComparator;
import com.incture.metrodata.util.PaginationUtil;
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

				RoleDetailsDo roleDetailsDo = new RoleDetailsDo();
				try {
					roleDetailsDo = roleDao.getByKeysForFK(userDetailsDTO.getRole());
				} catch (Exception e) {
					// TODO: handle exception
				}
				detailsDo.setRole(roleDao.importDto(userDetailsDTO.getRole(), roleDetailsDo));
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
			
			// created by for user will set only when dto holds the value of creating user and Do's created by is empty
			if (!ServicesUtil.isEmpty(userDetailsDTO.getCreatedBy()) && ServicesUtil.isEmpty(detailsDo.getCreatedBy())) {
				detailsDo.setCreatedBy(userDetailsDTO.getCreatedBy());
			}
			if (!ServicesUtil.isEmpty(userDetailsDTO.getUpdatedBy())) {
				detailsDo.setUpdatedBy(userDetailsDTO.getUpdatedBy());
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
			if (!ServicesUtil.isEmpty(userDetailsDTO.getTrackFreq())) {
				detailsDo.setTrackFreq(userDetailsDTO.getTrackFreq());
			}
			else
				detailsDo.setTrackFreq(30000L);
			
			if(!ServicesUtil.isEmpty(userDetailsDTO.getDeleted())){
				detailsDo.setDeleted(userDetailsDTO.getDeleted());
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
			if (!ServicesUtil.isEmpty(detailsDo.getCreatedBy())) {
				userDetailsDTO.setCreatedBy(detailsDo.getCreatedBy());
			}
			if (!ServicesUtil.isEmpty(detailsDo.getUpdatedBy())) {
				userDetailsDTO.setUpdatedBy(detailsDo.getUpdatedBy());
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
			if (!ServicesUtil.isEmpty(detailsDo.getTrackFreq())) {
				userDetailsDTO.setTrackFreq(detailsDo.getTrackFreq());
			}
			else
				userDetailsDTO.setTrackFreq(30000L);

			if(!ServicesUtil.isEmpty(detailsDo.getDeleted())){
				userDetailsDTO.setDeleted(detailsDo.getDeleted());
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
			Set<WareHouseDetailsDTO> wareHouseDetails, String queryParam) {
		List<String> wareHouseIds = new ArrayList<String>();
		for (WareHouseDetailsDTO wareHouse : wareHouseDetails)
			wareHouseIds.add(wareHouse.getWareHouseId());
		boolean isSuperAdmin = false;
		String hql = "";
		// get all the user list if role is super_admin or sales_admin
		if (roleName.equals(RoleConstant.SUPER_ADMIN.getValue())
				|| roleName.equals(RoleConstant.SALES_ADMIN.getValue())) {
			hql = "SELECT  u FROM UserDetailsDo AS u INNER JOIN u.role as r WHERE u.userId != :adminId ";
			isSuperAdmin = true;
		} else if(roleName.equals(RoleConstant.ADMIN_INSIDE_JAKARTA.getValue())
				|| roleName.equals(RoleConstant.COURIER_ADMIN.getValue())){
			hql = "SELECT  u from UserDetailsDo as u INNER JOIN u.role as r  where u.userId In "
					+ "( SELECT distinct u.userId from UserDetailsDo as u "
					+ " WHERE u.createdBy = :adminId )";
		}
		else {
			// else admin outside jakarta 
			hql = "SELECT  u from UserDetailsDo as u INNER JOIN u.role as r  where u.createdBy In "
					+ " (SELECT distinct u.userId FROM UserDetailsDo AS  u  WHERE u.createdBy = :adminId) Or u.createdBy = :adminId ";
		}
		
		// if queryParam is not empty apply restriction for that
		 if(!ServicesUtil.isEmpty(queryParam)){
			 hql+=" AND r.roleName =:role";
		 }
		
		Query query = getSession().createQuery(hql);
		 
		if(!ServicesUtil.isEmpty(queryParam))
			query.setParameter("role", queryParam);
		
		query.setFirstResult(PaginationUtil.FIRST_RESULT);
		query.setMaxResults(PaginationUtil.MAX_RESULT);
		/*if (!isSuperAdmin) {
			// send no data on if warehouse if is empty
			if (ServicesUtil.isEmpty(wareHouseIds))
				return new ArrayList<UserDetailsDTO>();

			
		}*/

		query.setParameter("adminId", adminId);
		ArrayList<UserDetailsDo> result = (ArrayList<UserDetailsDo>) query.list();
		return exportList(result);
	}
	
	public UserDetailsDTO getUserByEmailId(UserDetailsDTO userDto){
		UserDetailsDo resDo = null;
        /* String hql = " SELECT u.userId from UserDetailsDo as u where lower(u.email) = lower(:userEmail) ";
         Query query = getSession().createQuery(hql);
         query.setParameter("userEmail", userDto.getEmail());
         resDo = (UserDetailsDo) query.uniqueResult();*/
		Criteria criteria = getSession().createCriteria(UserDetailsDo.class);
		criteria.add(Restrictions.eq("email", userDto.getEmail()).ignoreCase());
		resDo =  (UserDetailsDo) criteria.uniqueResult();
         if(ServicesUtil.isEmpty(resDo))
        	 return null;
         else
    		return exportDto(resDo);
	}
	
	@SuppressWarnings("unchecked")
	public List<String> getDriversAssociatedWithAdmin(String adminId, String role){
		
		if (role.equals(RoleConstant.SUPER_ADMIN.getValue())
				|| role.equals(RoleConstant.SALES_ADMIN.getValue()))
			return null;
		
		List<String> driverRole = new ArrayList<>();
		driverRole.add(RoleConstant.INSIDE_JAKARTA_DRIVER.getValue());
		driverRole.add(RoleConstant.OUTSIDE_JAKARTA_DRIVER.getValue());
		String hql = " SELECT u.userId from UserDetailsDo as u INNER JOIN u.role as r  where u.createdBy = :adminId "
				     +" AND r.roleName in (:role)";
		
		if(role.equalsIgnoreCase(RoleConstant.ADMIN_OUTSIDE_JAKARTA.getValue())){
			hql = "SELECT  u.userId from UserDetailsDo as u INNER JOIN u.role as r where u.createdBy In "
					+ " (SELECT distinct u.userId FROM UserDetailsDo AS  u   WHERE u.createdBy = :adminId ) AND r.roleName in (:role)";
		}
		
		Query query = getSession().createQuery(hql);
		
		query.setParameter("adminId", adminId);
		query.setParameterList("role", driverRole);
		
		List<String> driversList  = (List<String>) query.list();
		return driversList;
	}
}
