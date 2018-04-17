package com.incture.metrodata.dto;

import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

import com.incture.metrodata.exceptions.InvalidInputFault;
import com.incture.metrodata.util.DB_Operation;
import com.incture.metrodata.util.SortDhDTOByDeliveryOrder;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TripDetailsDTO extends BaseDto {
	private String tripId;
	private String status;
	private Date updatedAt;
	private Date createdAt;
	private String createdBy;
	private String updatedBy;
	private Date startTime;
	private Date endTime;
	private String reasonForCancellation;
	private Set<DeliveryHeaderDTO> deliveryHeader = new TreeSet<DeliveryHeaderDTO>(new  SortDhDTOByDeliveryOrder());
	private UserDetailsDTO user;
	private Long trackFreq;

	@Override
	public void validate(DB_Operation enOperation) throws InvalidInputFault {
		// TODO Auto-generated method stub

	}

	@Override
	public Object getPrimaryKey() {
		// TODO Auto-generated method stub
		return tripId;
	}

	/*@Override
	public Comparator<?> getComparator() {
		// TODO Auto-generated method stub
		return null;
	}*/

}
