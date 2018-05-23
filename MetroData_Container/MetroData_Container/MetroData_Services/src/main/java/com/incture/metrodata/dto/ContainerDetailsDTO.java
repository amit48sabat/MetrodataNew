package com.incture.metrodata.dto;

import com.incture.metrodata.exceptions.InvalidInputFault;
import com.incture.metrodata.util.DB_Operation;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ContainerDetailsDTO extends BaseDto{

	private Long id;
	private Long DELIVNO;
	private String CREATEDT;
	private String CREATETM;
	private String SALESGRP;
	private String PURCHORD;
	private String REFNO;
	private String SLOC;
	private String SHIPADD;
	private String CITY;
	private String AREACODE;
	private String TELP;
	private String SOLDADD;
	private String SHIPTYP;
	private String INSTDELV;
	private String SERNUM;
	private String MAT;
	private String BATCH;
	private String DESC;
	private String QTY;
	private String VOL;
	private String STAT;
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
