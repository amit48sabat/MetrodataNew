package com.incture.metrodata.dao;

import java.util.ArrayList;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.incture.metrodata.constant.RoleConstant;
import com.incture.metrodata.dto.RoleDetailsDTO;
import com.incture.metrodata.dto.WareHouseDetailsDTO;
import com.incture.metrodata.entity.RoleDetailsDo;
import com.incture.metrodata.exceptions.ExecutionFault;
import com.incture.metrodata.exceptions.InvalidInputFault;
import com.incture.metrodata.exceptions.NoResultFault;
import com.incture.metrodata.util.ServicesUtil;

@Repository("roleDetailDao")
public class RoleDetailDAO extends BaseDao<RoleDetailsDo, RoleDetailsDTO> {

	@Override
	RoleDetailsDo importDto(RoleDetailsDTO dto, RoleDetailsDo dos)
			throws InvalidInputFault, ExecutionFault, NoResultFault, Exception {

		if (ServicesUtil.isEmpty(dos))
			dos = new RoleDetailsDo();
		if (!ServicesUtil.isEmpty(dto)) {
			if (!ServicesUtil.isEmpty(dto.getRoleId())) {
				dos.setRoleId(dto.getRoleId());
			}
			if (!ServicesUtil.isEmpty(dto.getRoleName())) {
				dos.setRoleName(dto.getRoleName());
			}
			if (!ServicesUtil.isEmpty(dto.getDisplayName())) {
				dos.setDisplayName(dto.getDisplayName());
			}
			if (!ServicesUtil.isEmpty(dto.getUserType())) {
				dos.setUserType(dto.getUserType());
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
	RoleDetailsDTO exportDto(RoleDetailsDo dos) {
		RoleDetailsDTO dto = new RoleDetailsDTO();

		if (!ServicesUtil.isEmpty(dos)) {
			if (!ServicesUtil.isEmpty(dos.getRoleId())) {
				dto.setRoleId(dos.getRoleId());
			}
			if (!ServicesUtil.isEmpty(dos.getRoleName())) {
				dto.setRoleName(dos.getRoleName());
			}
			if (!ServicesUtil.isEmpty(dos.getDisplayName())) {
				dto.setDisplayName(dos.getDisplayName());
			}
			if (!ServicesUtil.isEmpty(dos.getCreatedAt())) {
				dto.setCreatedAt(dos.getCreatedAt());
			}
			if (!ServicesUtil.isEmpty(dos.getUpdatedAt())) {
				dto.setUpdatedAt(dos.getUpdatedAt());
			}
		}

		return dto;
	}

	public RoleDetailsDTO getRoleByName(String name) {
		Criteria crit = getSession().createCriteria(RoleDetailsDo.class);
		crit.add(Restrictions.eq("roleName", name).ignoreCase());
		RoleDetailsDo roleDo = (RoleDetailsDo) crit.uniqueResult();
		return exportDto(roleDo);
	}

	@SuppressWarnings("unchecked")
	public Object getRoleByUser(String userId, String roleName, Set<WareHouseDetailsDTO> wareHouseDetails) {

		String superAdmin = RoleConstant.SUPER_ADMIN.getValue();
		ArrayList<String> outsideJkRoles = new ArrayList<>();
		outsideJkRoles.add(RoleConstant.ADMIN_OUTSIDE_JAKARTA.getValue());
		//outsideJkRoles.add(RoleConstant.COURIER_ADMIN.getValue());

		// base query
		String hql = "FROM RoleDetailsDo r WHERE r.roleName <> '" + roleName + "'";

		if (roleName.equals(RoleConstant.ADMIN_OUTSIDE_JAKARTA.getValue())) {
			hql += " AND r.userType in (:outsideJkRoles)";
		}

		else if (!roleName.equals(superAdmin)) {
			hql += " AND r.userType ='" + roleName + "' ";
		} else if (roleName.equals(superAdmin)) {
			String driverInside = RoleConstant.INSIDE_JAKARTA_DRIVER.getValue();
			String driverOutside = RoleConstant.OUTSIDE_JAKARTA_DRIVER.getValue();
			String courierAdmin  = RoleConstant.COURIER_ADMIN.getValue() ;
			hql += " AND r.roleName not in ('" + driverInside + "', '"+ driverOutside + "', '"+ courierAdmin+ "' )";
		}
		hql += " ORDER BY r.displayName asc";
		Query query = getSession().createQuery(hql);

		if (roleName.equals(RoleConstant.ADMIN_OUTSIDE_JAKARTA.getValue())) {
			query.setParameterList("outsideJkRoles", outsideJkRoles);
		}
		ArrayList<RoleDetailsDo> resultSer = (ArrayList<RoleDetailsDo>) query.list();

		return exportList(resultSer);
	}
}
