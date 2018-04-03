package com.incture.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.incture.metrodata.dto.ContainerDTO;
import com.incture.metrodata.dto.ResponseDto;
import com.incture.metrodata.service.ContainerServiceLocal;

@RestController
@CrossOrigin
@ComponentScan("com.incture")
@RequestMapping(value = "/Container", produces = "application/json")
public class ContainerController {
  
	@Autowired
	ContainerServiceLocal containerService;
	
	@RequestMapping( method = RequestMethod.POST)
	public ResponseDto create(@RequestBody String controllerJson) {
		
		Gson gson = new Gson();
		ContainerDTO dto = gson.fromJson(controllerJson.toString(), ContainerDTO.class);
		return containerService.create(dto);
	}	
}
