package com.incture.metrodata.util;

import java.util.Comparator;

import com.incture.metrodata.dto.CourierDetailsDTO;

public class CourierDetailsDtoComparator implements Comparator<CourierDetailsDTO>{

	@Override
	public int compare(CourierDetailsDTO o1, CourierDetailsDTO o2) {
		
		return o1.getCourierId().compareTo(o2.getCourierId());
	}

}
