package com.incture.metrodata.service;

import java.util.Map;

import com.incture.metrodata.dto.ResponseDto;

public interface EmailServiceLocal {
	
	
	public ResponseDto sendMail(Map<String,Object> map);
	
}
