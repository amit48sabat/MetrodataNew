package com.incture.metrodata.service;

import com.incture.metrodata.dto.ResponseDto;
import com.incture.metrodata.dto.RoleDetailsDTO;
import com.incture.metrodata.dto.UserDetailsDTO;

public interface RoleServiceLocal {
  
	ResponseDto create(RoleDetailsDTO dto);
	ResponseDto update(RoleDetailsDTO dto);
	ResponseDto find(RoleDetailsDTO dto);
	ResponseDto findAll();
	ResponseDto delete(RoleDetailsDTO dto);
	RoleDetailsDTO getRoleByRoleName(String roleName);
	ResponseDto getRoleByUser(UserDetailsDTO adminDto);
	void createAllRoles();
	
	
}
