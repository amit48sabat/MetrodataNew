package com.incture.metrodata.util;

import java.util.Comparator;

import com.incture.metrodata.entity.WareHouseDetailsDo;

public class WarehouseDoComparator implements Comparator<WareHouseDetailsDo> {

	@Override
	public int compare(WareHouseDetailsDo o1, WareHouseDetailsDo o2) {

		return o1.getWareHouseId().compareTo(o2.getWareHouseId());
	}

}
