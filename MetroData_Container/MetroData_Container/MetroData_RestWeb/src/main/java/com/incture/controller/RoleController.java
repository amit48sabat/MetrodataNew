package com.incture.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.incture.metrodata.dto.ResponseDto;
import com.incture.metrodata.dto.RoleDetailsDTO;
import com.incture.metrodata.service.RoleServiceLocal;

@RestController
@CrossOrigin
@ComponentScan("com.incture")
@RequestMapping(value = "/role", produces = "application/json")
public class RoleController {

	@Autowired
	RoleServiceLocal roleService;

	@RequestMapping(method = RequestMethod.POST)
	public ResponseDto create(@RequestBody RoleDetailsDTO dto) {
		return roleService.create(dto);
	}

	@RequestMapping(method = RequestMethod.GET)
	public ResponseDto findAll() {
		return roleService.findAll();
	}

	@RequestMapping(value = "/{roleId}", method = RequestMethod.PUT)
	public ResponseDto update(@PathVariable Long roleId,@RequestBody RoleDetailsDTO dto) {
		dto.setRoleId(roleId);
		return roleService.update(dto);
	}

	@RequestMapping(value = "/{roleId}", method = RequestMethod.DELETE)
	public ResponseDto delete(@PathVariable Long roleId) {
		RoleDetailsDTO dto = new RoleDetailsDTO();
		dto.setRoleId(roleId);
		return roleService.delete(dto);
	}

	@RequestMapping(value = "/{roleId}", method = RequestMethod.GET)
	public ResponseDto findById(@PathVariable Long roleId) {
		RoleDetailsDTO dto = new RoleDetailsDTO();
		dto.setRoleId(roleId);
		return roleService.find(dto);
	}
}
