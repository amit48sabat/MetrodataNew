package com.incture.metrodata.dto;

import java.util.Date;

import com.incture.metrodata.exceptions.InvalidInputFault;
import com.incture.metrodata.util.DB_Operation;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CommentsDTO extends BaseDto {

	private Long id;
	private String comment;
	private Date createdAt;
	private Date updatedAt;
	
	private Object createdBy;
	// private Long msg_id;

	@Override
	public void validate(DB_Operation enOperation) throws InvalidInputFault {
		// TODO Auto-generated method stub

	}

	@Override
	public Object getPrimaryKey() {
		// TODO Auto-generated method stub
		return id;
	}
    
	
}
