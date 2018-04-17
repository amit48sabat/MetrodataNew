package com.incture.metrodata.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.incture.metrodata.constant.RoleConstant;
import com.incture.metrodata.dto.CourierDetailsDTO;
import com.incture.metrodata.dto.UserDetailsDTO;
import com.incture.metrodata.dto.WareHouseDetailsDTO;
import com.incture.metrodata.entity.CourierDetailsDo;
import com.incture.metrodata.entity.UserDetailsDo;
import com.incture.metrodata.exceptions.ExecutionFault;
import com.incture.metrodata.exceptions.InvalidInputFault;
import com.incture.metrodata.exceptions.NoResultFault;
import com.incture.metrodata.util.PaginationUtil;
import com.incture.metrodata.util.ServicesUtil;

@Repository("CourierDetailsDAO")
public class CourierDetailsDAO extends BaseDao<CourierDetailsDo, CourierDetailsDTO> {

	@Override
	CourierDetailsDo importDto(CourierDetailsDTO dto, CourierDetailsDo dos)
			throws InvalidInputFault, ExecutionFault, NoResultFault, Exception {

		if (ServicesUtil.isEmpty(dos))
			dos = new CourierDetailsDo();

		if (!ServicesUtil.isEmpty(dto)) {
			if (!ServicesUtil.isEmpty(dto.getCourierId())) {
				dos.setCourierId(dto.getCourierId());
			}
			if (!ServicesUtil.isEmpty(dto.getCourierName())) {
				dos.setCourierName(dto.getCourierName());
			}
			if (!ServicesUtil.isEmpty(dto.getCreatedAt())) {
				dos.setCreatedAt(dto.getCreatedAt());
			}
			if (!ServicesUtil.isEmpty(dto.getUpdatedAt())) {
				dos.setUpdatedAt(dto.getUpdatedAt());
			}
		}

		return dos;
	}

	@Override
	CourierDetailsDTO exportDto(CourierDetailsDo dos) {

		CourierDetailsDTO dto = new CourierDetailsDTO();

		if (!ServicesUtil.isEmpty(dos.getCourierId())) {
			dto.setCourierId(dos.getCourierId());
		}
		if (!ServicesUtil.isEmpty(dos.getCourierName())) {
			dto.setCourierName(dos.getCourierName());
		}
		if (!ServicesUtil.isEmpty(dos.getCreatedAt())) {
			dto.setCreatedAt(dos.getCreatedAt());
		}
		if (!ServicesUtil.isEmpty(dos.getUpdatedAt())) {
			dto.setUpdatedAt(dos.getUpdatedAt());
		}
		return dto;
	}

	/**
	 * get the courier of user
	 */
	@SuppressWarnings("unchecked")
	public List<CourierDetailsDTO> getAllCouriersAssociatedWithUser(String userId, String roleName,
			Set<WareHouseDetailsDTO> wareHouseDetails) {
		List<String> wareHouseIds = new ArrayList<String>();
		for (WareHouseDetailsDTO wareHouse : wareHouseDetails)
			wareHouseIds.add(wareHouse.getWareHouseId());

		String hql = "";
		Boolean isSuperAdmin = false;
		// get all the user list if role is super_admin or sales_admin
		if (roleName.equals(RoleConstant.SUPER_ADMIN.getValue())
				|| roleName.equals(RoleConstant.SALES_ADMIN.getValue())) {
			hql = "FROM CourierDetailsDo ";
			isSuperAdmin = true;

		} else {
			hql = "SELECT u.courierDetails FROM UserDetailsDo AS u WHERE  u.userId =:userId";
		}
		Query query = getSession().createQuery(hql);
		query.setFirstResult(PaginationUtil.FIRST_RESULT);
		query.setMaxResults(PaginationUtil.MAX_RESULT);

		if(!isSuperAdmin)
		query.setParameter("userId", userId);
		ArrayList<CourierDetailsDo> result = (ArrayList<CourierDetailsDo>) query.list();
		return exportList(result);

	}

}
