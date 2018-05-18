package com.incture.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.incture.metrodata.constant.Message;
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
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ContainerController.class);
	
	@RequestMapping( method = RequestMethod.POST)
	public ResponseDto create(@RequestBody String controllerJson) {
		ResponseDto response = new ResponseDto();
	  try{
		    Gson gson = new Gson();
		    
		    LOGGER.error("INSIDE CREATE CONTAINER CONTROLLER");
		    
			ContainerDTO dto = gson.fromJson(controllerJson.toString(), ContainerDTO.class);
			 response = containerService.create(dto);
	   }catch(Exception e){
		    response.setStatus(false);
			response.setMessage(Message.FAILED.getValue());
			response.setData(controllerJson);
			response.setCode(500);
			e.printStackTrace();
	   }
	 
		return response;
	}	
}
