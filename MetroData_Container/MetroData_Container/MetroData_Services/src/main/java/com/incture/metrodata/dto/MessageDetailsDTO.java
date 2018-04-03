package com.incture.metrodata.dto;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.incture.metrodata.exceptions.InvalidInputFault;
import com.incture.metrodata.util.DB_Operation;

import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public class MessageDetailsDTO extends BaseDto{
	private Long messageId;
	private String tripId;
	private String title;
	private String body;
	private String type;
	private Object createdBy;
	private String updatedBy; 
	private Double longitude;
	private Double latitude;
	private Date createdAt;
	private Date updatedAt;
	private String address;
	private Long deliveryNoteId;
	private List<CommentsDTO> comments;
	private Set<UserDetailsDTO> users=new HashSet<UserDetailsDTO>(0);;
	public void validate(DB_Operation enOperation) throws InvalidInputFault {
		// TODO Auto-generated method stub
		
	}
	@Override
	public Object getPrimaryKey() {
		// TODO Auto-generated method stub
		return messageId;
	}

}
