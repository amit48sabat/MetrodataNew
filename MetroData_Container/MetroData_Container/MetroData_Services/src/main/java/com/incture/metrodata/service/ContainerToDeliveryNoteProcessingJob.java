package com.incture.metrodata.service;

import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.stereotype.Component;

import com.incture.metrodata.configuration.AppConfig;
import com.incture.metrodata.dto.ContainerDTO;
import com.incture.metrodata.dto.ContainerDetailsDTO;
import com.incture.metrodata.dto.ContainerItemsDTO;
import com.incture.metrodata.util.ServicesUtil;

@Component
@Scope("prototype")
public class ContainerToDeliveryNoteProcessingJob implements Job
{   
	static AbstractApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
	
	
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ContainerToDeliveryNoteProcessingJob.class);
	public void execute(JobExecutionContext con)
	throws JobExecutionException {
		ContainerServiceLocal containerService = (ContainerServiceLocal) context.getBean("containerService");
	
		try {
			List<ContainerDetailsDTO> containeritemList  = containerService.findAll();
	         
			if(!ServicesUtil.isEmpty(containeritemList)){
				LOGGER.error(" INSIDE CONTAINER_TO_DN_PROS_JOB. WITH TOTAL ITME NOS "+containeritemList.size());	
                ContainerDTO containerDto = new ContainerDTO();
                ContainerItemsDTO listItem = new  ContainerItemsDTO();
                listItem.setITEM(containeritemList);
                containerDto.setDELIVERY(listItem);
                
                containerService.createEntryInDeliveryHeader(containerDto);
               
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
