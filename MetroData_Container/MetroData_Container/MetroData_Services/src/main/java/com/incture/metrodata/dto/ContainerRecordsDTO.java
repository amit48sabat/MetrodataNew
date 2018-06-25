package com.incture.metrodata.dto;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Lob;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.incture.metrodata.exceptions.InvalidInputFault;
import com.incture.metrodata.util.DB_Operation;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ContainerRecordsDTO extends BaseDto {

	private Long id;
	private Date createdAt;
	private String payload;
	private Long totalItems;
	private Long totalDns;
	private Integer deleted = 0;

	@Override
	public void validate(DB_Operation enOperation) throws InvalidInputFault {
		// TODO Auto-generated method stub

	}

	@Override
	public Long getPrimaryKey() {
		// TODO Auto-generated method stub
		return id;
	}

}
