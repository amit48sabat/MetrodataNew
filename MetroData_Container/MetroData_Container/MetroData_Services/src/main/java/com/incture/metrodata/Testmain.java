package com.incture.metrodata;

import org.quartz.SchedulerException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

import com.google.gson.internal.LinkedTreeMap;
import com.incture.metrodata.configuration.AppConfig;
import com.incture.metrodata.dto.ContainerDetailsDTO;
import com.incture.metrodata.service.ContainerServiceLocal;
import com.incture.metrodata.util.ServicesUtil;


public class Testmain {
	static AbstractApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
	
	
	public static void main( String[] args ) throws SchedulerException
    {
		String controllerJson = "{\"DELIVERY\":{\"ITEM\":[{\"SALESGRP\":\"55V-IvanaKamoto\",\"DELIVNO\":\"6569259483\",\"CREATEDT\":\"2018-01-25\",\"CREATETM\":\"14:50:30\",\"PURCHORD\":\"ponum-123\",\"REFNO\":\"2560010911\",\"SLOC\":\"W001\",\"SHIPADD\":\"item1EXECUTIVECENTREBLOKIINO.1-2JL.LAKSAMANABINTAN-SEIPANAS\",\"CITY\":\"BATAM\",\"AREACODE\":\"008-Batam\",\"TELP\":\"0778-433007\",\"SOLDADD\":\"KOMPLEKSEXECUTIVECENTREBLOKIINO.1-2JL.LAKSAMANABINTAN-SEIPANAS\",\"SHIPTYP\":\"01-Regular\",\"INSTDELV\":\"FormHeader,headernote1,headernote11,headernote111,headernote3,headernote4,\",\"SERNUM\":\"123\",\"MAT\":\"90PT01U1-M02280\",\"BATCH\":\"item1\",\"DESC\":\"ASUSAIOV221ICUK-BA035D-I36006U/4GB/\",\"QTY\":\"6.000\",\"VOL\":\"191.520\"},{\"SALESGRP\":\"55V-IvanaKamoto\",\"DELIVNO\":\"6569259483\",\"CREATEDT\":\"2018-01-25\",\"CREATETM\":\"14:50:30\",\"PURCHORD\":\"ponum-123\",\"REFNO\":\"2560010911\",\"SLOC\":\"W001\",\"SHIPADD\":\"item2EXECUTIVECENTREBLOKIINO.1-2JL.LAKSAMANABINTAN-SEIPANAS\",\"CITY\":\"BATAM\",\"AREACODE\":\"008-Batam\",\"TELP\":\"0778-433007\",\"SOLDADD\":\"KOMPLEKSEXECUTIVECENTREBLOKIINO.1-2JL.LAKSAMANABINTAN-SEIPANAS\",\"SHIPTYP\":\"01-Regular\",\"INSTDELV\":\"FormHeader,headernote1,headernote11,headernote111,headernote3,headernote4,\",\"SERNUM\":\"123\",\"MAT\":\"90PT01U1-M02280\",\"BATCH\":\"item2\",\"DESC\":\"ASUSAIOV221ICUK-BA035D-I36006U/4GB/\",\"QTY\":\"6.000\",\"VOL\":\"191.520\",\"STAT\":\"\"},{\"SALESGRP\":\"55V-IvanaKamoto\",\"DELIVNO\":\"6569259483\",\"CREATEDT\":\"2018-01-25\",\"CREATETM\":\"14:50:30\",\"PURCHORD\":\"ponum-123\",\"REFNO\":\"2560010911\",\"SLOC\":\"W001\",\"SHIPADD\":\"KOMPLEKSEXECUTIVECENTREBLOKIINO.1-2JL.LAKSAMANABINTAN-SEIPANAS\",\"CITY\":\"BATAM\",\"AREACODE\":\"008-Batam\",\"TELP\":\"0778-433007\",\"SOLDADD\":\"item3EXECUTIVECENTREBLOKIINO.1-2JL.LAKSAMANABINTAN-SEIPANAS\",\"SHIPTYP\":\"01-Regular\",\"INSTDELV\":\"FormHeader,headernote1,headernote11,headernote111,headernote3,headernote4,\",\"SERNUM\":\"2345\",\"MAT\":\"90PT01U1-M02281\",\"BATCH\":\"item3\",\"DESC\":\"ASUSAIOV221ICUK-BA035D-I36006U/4GB/\",\"QTY\":\"6.000\",\"VOL\":\"191.520\",\"STAT\":\"\"}]}}";
		
		ContainerServiceLocal containerService = (ContainerServiceLocal) context.getBean("containerService");
		 System.out.println(containerService.getContainerRecordById(6563L));;
		
		//containerService.findAll();
		/*Gson gson = new Gson();
		ContainerDTO dto = gson.fromJson(controllerJson.toString(), ContainerDTO.class);
		List<ContainerDetailsDTO> list = new ArrayList<>();
		// System.out.println(dto.getDELIVERY().getITEM());
		if (dto.getDELIVERY().getITEM() instanceof LinkedTreeMap) {
			LinkedTreeMap<String, String> item2 = (LinkedTreeMap) dto.getDELIVERY().getITEM();
			ContainerDetailsDTO d = getLinkedTreeMapToContainerDetailsDto(item2);
			list.add(d);
			// System.out.println(d.getAREACODE());
		} else if (dto.getDELIVERY().getITEM() instanceof ArrayList) {
			List<LinkedTreeMap> item2 = (List<LinkedTreeMap>) dto.getDELIVERY().getITEM();
			for (LinkedTreeMap i : item2) {
				ContainerDetailsDTO d = getLinkedTreeMapToContainerDetailsDto(i);
				list.add(d);
			}
		}
		dto.getDELIVERY().setITEM(list);
		
		 JobDetail job = JobBuilder.newJob(ContainerToDeliveryNoteProcessingJob.class).withIdentity("dummyJobName", "group1").build();
		Trigger trigger = TriggerBuilder.newTrigger().withIdentity("DnProcessTrigger", "group1")
				.startNow().build();
		Scheduler scheduler = new StdSchedulerFactory().getScheduler();
		scheduler.getContext().put("data", dto);
		
		scheduler.start();
		scheduler.scheduleJob(job, trigger);
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		scheduler.shutdown(true);*/


    }
	
	static ContainerDetailsDTO getLinkedTreeMapToContainerDetailsDto(LinkedTreeMap<String, String> map) {
		ContainerDetailsDTO dto = new ContainerDetailsDTO();
		if ( map.containsKey("id") && !ServicesUtil.isEmpty(map.get("id")))
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



























