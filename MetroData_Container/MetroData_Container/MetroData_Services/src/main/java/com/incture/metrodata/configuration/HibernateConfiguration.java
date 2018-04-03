package com.incture.metrodata.configuration;

import java.util.Date;
import java.util.Properties;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.google.maps.GeoApiContext;
import com.incture.metrodata.MyJobTwo;
import com.incture.metrodata.util.RESTInvoker;

@Configuration
@EnableTransactionManagement
@ComponentScan({ "com.incture.metrodata.configuration" })
@PropertySource(value = { "classpath:application.properties" })
public class HibernateConfiguration {

	@Autowired
	private Environment environment;

	@Bean
	public LocalSessionFactoryBean sessionFactory() {
		LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
		sessionFactory.setDataSource(dataSource());
		sessionFactory.setPackagesToScan(new String[] { "com.incture" });
		sessionFactory.setHibernateProperties(hibernateProperties());
		return sessionFactory;
	}

	@Bean
	public DataSource dataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(environment.getRequiredProperty("jdbc.driverClassName"));
		dataSource.setUrl(environment.getRequiredProperty("jdbc.url"));
		dataSource.setUsername(environment.getRequiredProperty("jdbc.username"));
		dataSource.setPassword(environment.getRequiredProperty("jdbc.password"));
		return dataSource;
	}

	private Properties hibernateProperties() {
		Properties properties = new Properties();
		properties.put("hibernate.dialect", environment.getRequiredProperty("hibernate.dialect"));
		properties.put("hibernate.show_sql", environment.getRequiredProperty("hibernate.show_sql"));
		properties.put("hibernate.format_sql", environment.getRequiredProperty("hibernate.format_sql"));
		properties.put("hibernate.hbm2ddl.auto", environment.getRequiredProperty("hibernate.hbm2ddl.auto"));
		return properties;
	}

	@Bean
	@Autowired
	public HibernateTransactionManager transactionManager(SessionFactory s) {
		HibernateTransactionManager txManager = new HibernateTransactionManager();
		txManager.setSessionFactory(s);
		return txManager;
	}

	@Bean
	public GeoApiContext getGeoCodeContext() {
		GeoApiContext context = new GeoApiContext();
		String apiKey = environment.getRequiredProperty("geocode.api.key");
		context.setApiKey(apiKey);

		return context;
	}

	@Bean
	public RESTInvoker getRESTInvoker() {
		String url = environment.getRequiredProperty("idp.base.url");
		String username = environment.getRequiredProperty("idp.tech.username");
		String password = environment.getRequiredProperty("jdbc.tech.password");
		return new RESTInvoker(url, username, password);
	}

	@Bean
	public JobDetail jobDetailFactoryBean() {
		JobDetail job = JobBuilder.newJob(MyJobTwo.class).
				withIdentity("ECCFetchJob", "group1").build();
		return job;
	}

	// Job is scheduled after every 1 minute
	@Bean
	public CronTrigger cronTriggerFactoryBean() {

		CronTrigger crontrigger = TriggerBuilder.newTrigger()
				.withIdentity("ECCFetchTrigger", "group1").startAt(new Date())
				// .startNow()
				.withSchedule(CronScheduleBuilder.cronSchedule("0/20 * * * * ?")).build();
		return crontrigger;
	}

	@Bean
	public SchedulerFactory schedulerFactoryBean() {
		SchedulerFactory sf = new StdSchedulerFactory();
		try {
			Scheduler scheduler = sf.getScheduler();
			scheduler.scheduleJob(jobDetailFactoryBean(), cronTriggerFactoryBean());
			scheduler.start();
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sf;

	}
}
