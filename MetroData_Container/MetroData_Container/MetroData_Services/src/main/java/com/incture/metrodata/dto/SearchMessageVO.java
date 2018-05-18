package com.incture.metrodata.dto;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SearchMessageVO {
	private String userId;
	private String tripId;
	private String type;
	private Integer firstResult;
	private Integer maxResult;
	private Date startedAt;
	private Date endedAt;

}
