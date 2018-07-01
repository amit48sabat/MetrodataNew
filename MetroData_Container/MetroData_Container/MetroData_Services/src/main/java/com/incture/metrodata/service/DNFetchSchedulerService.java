package com.incture.metrodata.service;

import java.util.Date;

import org.joda.time.DateTime;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

import com.incture.metrodata.configuration.AppConfig;
import com.incture.metrodata.util.HciRestInvoker;


public class DNFetchSchedulerService implements DNFetchSchedulerServiceLocal,Job {

	static AbstractApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
 
	private static final Logger LOGGER = LoggerFactory.getLogger(DNFetchSchedulerService.class);
	
	@Override
	public void execute(JobExecutionContext con) throws JobExecutionException {

		ContainerServiceLocal userService = (ContainerServiceLocal) context.getBean("containerService");
		HciRestInvoker invoker = userService.getHciRestInvoker();
		Date date = new Date();
		//Initialize your Date however you like it.
		
		DateTime datetime = new DateTime(date);
		
        String day = datetime.toString("dd");
        String month = datetime.toString("MM");
        String year = datetime.toString("YYYY");
		String data = year+month+day;
		String payload = "{ \"DELIVERY\": { \"GI_DATE\": \""+data+"\" } }";
		LOGGER.error("Hci service request [ "+date+" ] payload => "+payload);
		String response = invoker.postDataToServer("/metrodatadetails", payload);
		LOGGER.error("Hci service [ "+date+" ] response <= " +response);

	}

}
