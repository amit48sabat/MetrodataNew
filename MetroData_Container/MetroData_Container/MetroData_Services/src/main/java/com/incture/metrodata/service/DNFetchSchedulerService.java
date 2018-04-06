package com.incture.metrodata.service;

import java.util.Date;

import org.joda.time.DateTime;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.incture.metrodata.configuration.AppConfig;
import com.incture.metrodata.util.HciRestInvoker;

@Service("dnFetchService")
@Transactional
public class DNFetchSchedulerService implements DNFetchSchedulerServiceLocal {

	static AbstractApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

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
		System.err.println("Hci service request payload=>  "+payload);
		String response = invoker.postDataToServer("", payload);
		System.err.println("Hci service response =>" +response);

	}

}
