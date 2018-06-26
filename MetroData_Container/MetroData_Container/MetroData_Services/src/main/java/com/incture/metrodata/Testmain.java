package com.incture.metrodata;

import java.util.Date;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;

import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

import com.google.gson.internal.LinkedTreeMap;
import com.incture.metrodata.dto.ContainerDetailsDTO;
import com.incture.metrodata.util.ServicesUtil;


public class Testmain {
	//static AbstractApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

	public static void main(String[] args) throws SchedulerException {
		long timeStamp = 0;
		String group = "";
		String triggerName = "";
		String jobIdentity = "";
		Date currdate = null;
		Scheduler scheduler  = null;
		Trigger trigger = null;
		JobDetail job  =null;
		try {
			
			timeStamp = System.currentTimeMillis();
			jobIdentity = "DnProcessJob" + timeStamp;
			group = "group" + timeStamp;
			triggerName = "DnProcessTrigger" + timeStamp;
			 job = JobBuilder.newJob(TestJob.class).withIdentity(jobIdentity, group)
					.build();
			 trigger = (SimpleTrigger) TriggerBuilder.newTrigger().withIdentity(triggerName, group).startNow()
					.build();
			 scheduler = new StdSchedulerFactory().getScheduler();
			scheduler.getContext().put("name", "first");
			scheduler.standby();
			scheduler.start();
			scheduler.scheduleJob(job, trigger);
			Thread.sleep(2000);
			scheduler.standby();
			scheduler.getContext().clear();

			timeStamp = System.currentTimeMillis();
			jobIdentity = "DnProcessJob" + timeStamp;
			group = "group" + timeStamp;
			triggerName = "DnProcessTrigger" + timeStamp;
			 job = JobBuilder.newJob(TestJob.class).withIdentity(jobIdentity, group)
					.build();
			 trigger =  TriggerBuilder.newTrigger().withIdentity(triggerName, group).startNow()
					.build();
			 scheduler = new StdSchedulerFactory().getScheduler();
			scheduler.getContext().put("name", "Second");
			scheduler.start();
			scheduler.scheduleJob(job, trigger);
			Thread.sleep(2000);
			scheduler.shutdown(true);
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	static ContainerDetailsDTO getLinkedTreeMapToContainerDetailsDto(LinkedTreeMap<String, String> map) {
		ContainerDetailsDTO dto = new ContainerDetailsDTO();
		if (map.containsKey("id") && !ServicesUtil.isEmpty(map.get("id")))
			dto.setId(Long.parseLong(map.get("id")));
		if (map.containsKey("DELIVNO") && !ServicesUtil.isEmpty(map.get("DELIVNO")))
			dto.setDELIVNO(Long.parseLong(map.get("DELIVNO")));
		if (map.containsKey("CREATEDT") && !ServicesUtil.isEmpty(map.get("CREATEDT")))
			dto.setCREATEDT(map.get("CREATEDT"));
		if (map.containsKey("CREATETM") && !ServicesUtil.isEmpty(map.get("CREATETM")))
			dto.setCREATETM(map.get("CREATETM"));
		if (map.containsKey("SALESGRP") && !ServicesUtil.isEmpty(map.get("SALESGRP")))
			dto.setSALESGRP(map.get("SALESGRP"));
		if (map.containsKey("PURCHORD") && !ServicesUtil.isEmpty(map.get("PURCHORD")))
			dto.setPURCHORD(map.get("PURCHORD"));
		if (map.containsKey("REFNO") && !ServicesUtil.isEmpty(map.get("REFNO")))
			dto.setREFNO(map.get("REFNO"));
		if (map.containsKey("SLOC") && !ServicesUtil.isEmpty(map.get("SLOC")))
			dto.setSLOC(map.get("SLOC"));
		if (map.containsKey("SHIPADD") && !ServicesUtil.isEmpty(map.get("SHIPADD")))
			dto.setSHIPADD(map.get("SHIPADD"));
		if (map.containsKey("CITY") && !ServicesUtil.isEmpty(map.get("CITY")))
			dto.setCITY(map.get("CITY"));
		if (map.containsKey("AREACODE") && !ServicesUtil.isEmpty(map.get("AREACODE")))
			dto.setAREACODE(map.get("AREACODE"));
		if (map.containsKey("TELP") && !ServicesUtil.isEmpty(map.get("TELP")))
			dto.setTELP(map.get("TELP"));
		if (map.containsKey("SOLDADD") && !ServicesUtil.isEmpty(map.get("SOLDADD")))
			dto.setSOLDADD(map.get("SOLDADD"));
		if (map.containsKey("SHIPTYP") && !ServicesUtil.isEmpty(map.get("SHIPTYP")))
			dto.setSHIPTYP(map.get("SHIPTYP"));
		if (map.containsKey("INSTDELV") && !ServicesUtil.isEmpty(map.get("INSTDELV")))
			dto.setINSTDELV(map.get("INSTDELV"));
		if (map.containsKey("SERNUM") && !ServicesUtil.isEmpty(map.get("SERNUM")))
			dto.setSERNUM(map.get("SERNUM"));
		if (map.containsKey("MAT") && !ServicesUtil.isEmpty(map.get("MAT")))
			dto.setMAT(map.get("MAT"));
		if (map.containsKey("BATCH") && !ServicesUtil.isEmpty(map.get("BATCH")))
			dto.setBATCH(map.get("BATCH"));
		if (map.containsKey("DESC") && !ServicesUtil.isEmpty(map.get("DESC")))
			dto.setDESC(map.get("DESC"));
		if (map.containsKey("QTY") && !ServicesUtil.isEmpty(map.get("QTY")))
			dto.setQTY(map.get("QTY"));
		if (map.containsKey("VOL") && !ServicesUtil.isEmpty(map.get("VOL")))
			dto.setVOL(map.get("VOL"));
		if (map.containsKey("STAT") && !ServicesUtil.isEmpty(map.get("STAT")))
			dto.setSTAT(map.get("STAT"));

		return dto;
	}
}
