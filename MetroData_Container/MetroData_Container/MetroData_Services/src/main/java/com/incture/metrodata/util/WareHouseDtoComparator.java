package com.incture.metrodata.util;

import java.util.Comparator;

import com.incture.metrodata.dto.WareHouseDetailsDTO;

public class WareHouseDtoComparator implements Comparator<WareHouseDetailsDTO> {

	@Override
	public int compare(WareHouseDetailsDTO o1, WareHouseDetailsDTO o2) {

		return o1.getWareHouseId().compareTo(o2.getWareHouseId());
	}

}
