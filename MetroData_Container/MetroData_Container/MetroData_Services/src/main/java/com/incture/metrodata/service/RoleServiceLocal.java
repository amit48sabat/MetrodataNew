package com.incture.metrodata.service;

import com.incture.metrodata.dto.ResponseDto;
import com.incture.metrodata.dto.RoleDetailsDTO;

public interface RoleServiceLocal {
  
	ResponseDto create(RoleDetailsDTO dto);
	ResponseDto update(RoleDetailsDTO dto);
	ResponseDto find(RoleDetailsDTO dto);
	ResponseDto findAll();
	ResponseDto delete(RoleDetailsDTO dto);
	RoleDetailsDTO getRoleByRoleName(String roleName);
	
	
}
