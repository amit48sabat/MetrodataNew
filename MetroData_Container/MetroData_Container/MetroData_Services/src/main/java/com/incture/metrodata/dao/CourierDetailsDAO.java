package com.incture.metrodata.dao;

import com.incture.metrodata.dto.CourierDetailsDTO;
import com.incture.metrodata.entity.CourierDetailsDo;
import com.incture.metrodata.exceptions.ExecutionFault;
import com.incture.metrodata.exceptions.InvalidInputFault;
import com.incture.metrodata.exceptions.NoResultFault;
import com.incture.metrodata.util.ServicesUtil;

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

}
