package com.incture.metrodata.dto;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.incture.metrodata.exceptions.InvalidInputFault;
import com.incture.metrodata.util.DB_Operation;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDetailsDTO extends BaseDto {
	private String userId;
	private String firstName;
	private String lastName;
	private String name;
	private String email;
	private String mobileToken;
	private String telephone;
	private String webToken;
	// private String userType;
	private Double longitude;
	private Double latitude;
	private Date lastLogedIn;
	private Date createdDate;
	private Date createdAt;
	private Date updatedAt;
	private String parentId;
	private RoleDetailsDTO role;

	private Set<WareHouseDetailsDTO> wareHouseDetails = new HashSet<WareHouseDetailsDTO>(0);
	private Set<CourierDetailsDTO> courierDetails = new HashSet<CourierDetailsDTO>(0);

	/*
	 * private Long wareHouseId; private Long courierId;
	 */

	@Override
	public void validate(DB_Operation enOperation) throws InvalidInputFault {
		// TODO Auto-generated method stub

	}

	@Override
	public Object getPrimaryKey() {
		// TODO Auto-generated method stub
		return userId;
	}
}
