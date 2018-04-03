package com.incture.metrodata;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class MyJobTwo implements Job {
	
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		System.err.println("hello");
	}
} 