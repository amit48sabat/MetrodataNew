package com.incture.metrodata.util;

import java.util.Comparator;

import com.incture.metrodata.dto.DeliveryHeaderDTO;

public class SortDhDTOByDeliveryOrder implements Comparator<DeliveryHeaderDTO> {

	@Override
	public int compare(DeliveryHeaderDTO o1, DeliveryHeaderDTO o2) {
		Long diff = 1L;
		// if delivery order is not null then
		if ( (!ServicesUtil.isEmpty(o1.getDeliveryOrder()) && !ServicesUtil.isEmpty(o2.getDeliveryOrder()))
			 && o1.getDeliveryOrder() != o2.getDeliveryOrder()
			) {
			diff = o1.getDeliveryOrder() - o2.getDeliveryOrder();
			return diff.intValue();
		}
		
		// else take the difference of their ids
		else
		{
			 diff = o1.getDeliveryNoteId() - o2.getDeliveryNoteId();
		}
		return diff.intValue();
	}

}
