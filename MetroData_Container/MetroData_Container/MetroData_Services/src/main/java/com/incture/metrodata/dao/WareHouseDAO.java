package com.incture.metrodata.dao;

import com.incture.metrodata.dto.WareHouseDetailsDTO;
import com.incture.metrodata.entity.WareHouseDetailsDo;
import com.incture.metrodata.exceptions.ExecutionFault;
import com.incture.metrodata.exceptions.InvalidInputFault;
import com.incture.metrodata.exceptions.NoResultFault;
import com.incture.metrodata.util.ServicesUtil;

public class WareHouseDAO extends BaseDao<WareHouseDetailsDo, WareHouseDetailsDTO> {

	@Override
	WareHouseDetailsDo importDto(WareHouseDetailsDTO dto, WareHouseDetailsDo dos)
			throws InvalidInputFault, ExecutionFault, NoResultFault, Exception {

		if (ServicesUtil.isEmpty(dos))
			dos = new WareHouseDetailsDo();

		if (!ServicesUtil.isEmpty(dto)) {

			if (!ServicesUtil.isEmpty(dto.getWareHouseId())) {
				dos.setWareHouseId(dto.getWareHouseId());
			}
			if (!ServicesUtil.isEmpty(dto.getWareHouseName())) {
				dos.setWareHouseName(dto.getWareHouseName());
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
	WareHouseDetailsDTO exportDto(WareHouseDetailsDo dos) {
		WareHouseDetailsDTO dto = new WareHouseDetailsDTO();

		if (!ServicesUtil.isEmpty(dos.getWareHouseId())) {
			dto.setWareHouseId(dos.getWareHouseId());
		}
		if (!ServicesUtil.isEmpty(dos.getWareHouseName())) {
			dto.setWareHouseName(dos.getWareHouseName());
		}
		if (!ServicesUtil.isEmpty(dos.getCreatedAt())) {
			dto.setCreatedAt(dto.getCreatedAt());
		}
		if (!ServicesUtil.isEmpty(dos.getUpdatedAt())) {
			dto.setUpdatedAt(dos.getUpdatedAt());
		}

		return dto;
	}

}
