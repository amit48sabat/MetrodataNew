package com.incture.metrodata.dao;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.incture.metrodata.dto.RoleDetailsDTO;
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
	
		if(ServicesUtil.isEmpty(dos))
			dos = new RoleDetailsDo();
		if(!ServicesUtil.isEmpty(dto)){
			if(!ServicesUtil.isEmpty(dto.getRoleId())){
				dos.setRoleId(dto.getRoleId());
			}
			if(!ServicesUtil.isEmpty(dto.getRoleName())){
				dos.setRoleName(dto.getRoleName());
			}
			if(!ServicesUtil.isEmpty(dto.getDisplayName())){
				dos.setDisplayName(dto.getDisplayName());
			}
			if(!ServicesUtil.isEmpty(dto.getCreatedAt())){
				dos.setCreatedAt(dto.getCreatedAt());
			}
			if(!ServicesUtil.isEmpty(dto.getUpdatedAt())){
				dos.setUpdatedAt(dto.getUpdatedAt());
			}
		}
		
		return dos;
	}

	@Override
	RoleDetailsDTO exportDto(RoleDetailsDo dos) {
		RoleDetailsDTO dto = new RoleDetailsDTO();
		
		if(!ServicesUtil.isEmpty(dos)){
			if(!ServicesUtil.isEmpty(dos.getRoleId())){
				dto.setRoleId(dos.getRoleId());
			}
			if(!ServicesUtil.isEmpty(dos.getRoleName())){
				dto.setRoleName(dos.getRoleName());
			}
			if(!ServicesUtil.isEmpty(dos.getDisplayName())){
				dto.setDisplayName(dos.getDisplayName());
			}
			if(!ServicesUtil.isEmpty(dos.getCreatedAt())){
				dto.setCreatedAt(dos.getCreatedAt());
			}
			if(!ServicesUtil.isEmpty(dos.getUpdatedAt())){
				dto.setUpdatedAt(dos.getUpdatedAt());
			}
		}
		
		return dto;
	}

	
	public RoleDetailsDTO getRoleByName(String name){
		Criteria crit = getSession().createCriteria(RoleDetailsDo.class);
		crit.add(Restrictions.eq("roleName",name).ignoreCase());
		RoleDetailsDo roleDo = (RoleDetailsDo) crit.uniqueResult();
		return exportDto(roleDo);
	}
}
