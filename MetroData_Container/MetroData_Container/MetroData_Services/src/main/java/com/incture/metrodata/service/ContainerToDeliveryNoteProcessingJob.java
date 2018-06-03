package com.incture.metrodata.service;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.stereotype.Component;

import com.incture.metrodata.configuration.AppConfig;
import com.incture.metrodata.dto.ContainerDTO;
import com.incture.metrodata.util.ServicesUtil;

@Component
@Scope("prototype")
public class ContainerToDeliveryNoteProcessingJob implements Job {
	static AbstractApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
	ContainerServiceLocal containerService = (ContainerServiceLocal) context.getBean("containerService");

	private static final Logger LOGGER = LoggerFactory.getLogger(ContainerToDeliveryNoteProcessingJob.class);

	public void execute(JobExecutionContext con) throws JobExecutionException {

		try {
			Scheduler scheduler = con.getScheduler();
			
			LOGGER.error("SCHEDULER STARTED "+scheduler.isStarted());
			if (!ServicesUtil.isEmpty(scheduler.getContext().get("data"))) {
				ContainerDTO containerDTO = (ContainerDTO) con.getScheduler().getContext().get("data");

				if (!ServicesUtil.isEmpty(containerDTO)) {
					LOGGER.error(" INSIDE CONTAINER_TO_DN_PROS_JOB.");
					containerService.createEntryInDeliveryHeader(containerDTO);

				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
