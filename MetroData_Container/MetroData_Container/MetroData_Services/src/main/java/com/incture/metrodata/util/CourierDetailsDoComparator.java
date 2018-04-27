package com.incture.metrodata.util;

import java.util.Comparator;

import com.incture.metrodata.entity.CourierDetailsDo;

public class CourierDetailsDoComparator implements Comparator<CourierDetailsDo>{

		@Override
		public int compare(CourierDetailsDo o1, CourierDetailsDo o2) {
			
			return o1.getCourierId().compareTo(o2.getCourierId());
		}

	}
