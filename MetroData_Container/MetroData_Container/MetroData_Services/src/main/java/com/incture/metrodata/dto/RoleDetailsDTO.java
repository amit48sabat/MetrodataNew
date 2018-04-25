package com.incture.metrodata.dto;

import java.util.Date;

import com.incture.metrodata.exceptions.InvalidInputFault;
import com.incture.metrodata.util.DB_Operation;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleDetailsDTO extends BaseDto{

	private Long roleId;
	private String roleName;
	private Date createdAt;
	private Date updatedAt;
	private String displayName;
	private String userType;
	@Override
	public void validate(DB_Operation enOperation) throws InvalidInputFault {
     		
	}

	@Override
	public Object getPrimaryKey() {
		// TODO Auto-generated method stub
		return roleId;
	}

}
