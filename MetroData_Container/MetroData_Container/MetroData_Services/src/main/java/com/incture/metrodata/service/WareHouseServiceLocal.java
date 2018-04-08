package com.incture.metrodata.service;

import com.incture.metrodata.dto.ResponseDto;
import com.incture.metrodata.dto.WareHouseDetailsDTO;

public interface WareHouseServiceLocal {
  
	ResponseDto create(WareHouseDetailsDTO dto);
	ResponseDto update(WareHouseDetailsDTO dto);
	ResponseDto find(WareHouseDetailsDTO dto);
	ResponseDto findAll();
	ResponseDto delete(WareHouseDetailsDTO dto);
	ResponseDto getWareHouseListByUserId(String userId,String role);
}
