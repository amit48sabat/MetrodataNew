package com.incture.configuration;

import java.util.Date;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.incture.metrodata.util.HciRestInvoker;

@Configuration
@EnableWebMvc
@EnableScheduling
@ComponentScan(basePackages = "com.incture")
public class MetroDataConfiguration {

	@Autowired
	HciRestInvoker invoker;

	@Autowired
	Environment environment;

	private static final Logger LOGGER = LoggerFactory.getLogger(MetroDataConfiguration.class);

	@Scheduled(cron = "0 0/15 * * * ?")
	public void scheduleFixedDelayTask() {

		String isSchedulerStart = environment.getRequiredProperty("hci.scheduler.start").trim();
		if (isSchedulerStart.equalsIgnoreCase("true".trim())) {
			Date date = new Date();
			// Initialize your Date however you like it.
			DateTime datetime = new DateTime(date);
			String day = datetime.toString("dd");
			String month = datetime.toString("MM");
			String year = datetime.toString("YYYY");
			String data = year + month + day;
			String payload = "{ \"DELIVERY\": { \"GI_DATE\": \"" + data + "\" } }";
			LOGGER.error("Hci service request payload => " + payload + " | [ " + date + " ]");
			String response = invoker.postDataToServer("/metrodatadetails", payload);
			LOGGER.error("Hci service response <= " + response + " | [ " + date + " ]");
		} else {
			LOGGER.error("Hci service scheduler will not work in QA/DEV.");
		}
	}

	@Bean
	public CommonsMultipartResolver multipartResolver() {
		CommonsMultipartResolver resolver = new CommonsMultipartResolver();
		// resolver.setDefaultEncoding("utf-8");
		resolver.setMaxUploadSize(40000000);
		return resolver;
	}

}