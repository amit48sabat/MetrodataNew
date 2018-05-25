package com.incture.metrodata;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.incture.metrodata.dto.ContainerDTO;
import com.incture.metrodata.dto.ContainerDetailsDTO;
import com.incture.metrodata.util.ServicesUtil;
import com.itextpdf.text.DocumentException;

public class Testmain {

	public static void main(String[] args) throws IOException, DocumentException {
		
		/*ApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class);
		ContainerServiceLocal containerService = ctx.getBean(ContainerServiceLocal.class);*/
		Gson gson = new Gson();
		//ObjectMapper mapper = new ObjectMapper();
		String controllerJson = "{\"DELIVERY\":{\"ITEM\":[{\"SALESGRP\":\"54F-RandyTuwoh\",\"DELIVNO\":\"4550335203\",\"CREATEDT\":\"2018-05-25\",\"CREATETM\":\"11:53:28\",\"PURCHORD\":\"\",\"REFNO\":\"2550346761\",\"SLOC\":\"W002\",\"SHIPADD\":\"JL.JEND.SUDIRMANNO.01KRANJI\",\"CITY\":\"BEKASIBARAT\",\"AREACODE\":\"010-Bekasi\",\"TELP\":\"021-88966334\",\"SOLDADD\":\"GAJAHMADAPLASALT.SGJL.GAJAHMADANO.19-26PETOJOUTARA-GAMBIR\",\"SHIPTYP\":\"01-Regular\",\"INSTDELV\":\"\",\"SERNUM\":\"80XU004AIDPF12TK99\",\"MAT\":\"80XU004AID\",\"BATCH\":\"\",\"DESC\":\"NOTEBOOK_LENOVO_IDEAPAD320-14AST\",\"QTY\":\"1.000\",\"VOL\":\"10976.000\",\"STAT\":\"\"}]}}";
		ContainerDTO dto = gson.fromJson(controllerJson.toString(), ContainerDTO.class);
		Object item;
		List<ContainerDetailsDTO> list = new ArrayList<>();
		
		//Object object  = gson.fromJson(dto.getDELIVERY().getITEM().toString(),Object.class);
		System.out.println(dto.getDELIVERY().getITEM());
		
		
		if(dto.getDELIVERY().getITEM() instanceof LinkedTreeMap)
		{
			LinkedTreeMap item2 = (LinkedTreeMap) dto.getDELIVERY().getITEM();
			ContainerDetailsDTO d = getLinkedTreeMapToContainerDetailsDto(item2);
			
			list.add(d);
			
			System.out.println(d.getAREACODE());
		}
		else if(dto.getDELIVERY().getITEM() instanceof ArrayList)
			{
			List<LinkedTreeMap> item2 = (List<LinkedTreeMap>) dto.getDELIVERY().getITEM();
			
		     for(LinkedTreeMap i : item2)
		     {
		    	 ContainerDetailsDTO d = getLinkedTreeMapToContainerDetailsDto(i);
		    	 list.add(d);
		     }
			}
		dto.getDELIVERY().setITEM(list);
		System.out.println(dto);
	}
      
	static ContainerDetailsDTO getLinkedTreeMapToContainerDetailsDto(LinkedTreeMap<String, String> map){
		ContainerDetailsDTO dto = new ContainerDetailsDTO();
		if(!ServicesUtil.isEmpty(map.get("id")))
			 dto.setId(Long.parseLong(map.get("id"))); 
		if(!ServicesUtil.isEmpty(map.get("DELIVNO")))
			 dto.setDELIVNO(Long.parseLong(map.get("DELIVNO")));
		 if(!ServicesUtil.isEmpty(map.get("CREATEDT")))
			 dto.setCREATEDT(map.get("CREATEDT"));
		 if(!ServicesUtil.isEmpty(map.get("CREATETM")))
			 dto.setCREATETM(map.get("CREATETM"));
		 if(!ServicesUtil.isEmpty(map.get("SALESGRP")))
			 dto.setSALESGRP(map.get("SALESGRP"));
		 if(!ServicesUtil.isEmpty(map.get("PURCHORD")))
			 dto.setPURCHORD(map.get("PURCHORD"));
		 if(!ServicesUtil.isEmpty(map.get("REFNO")))
			 dto.setREFNO(map.get("REFNO"));
		 if(!ServicesUtil.isEmpty(map.get("SLOC")))
			 dto.setSLOC(map.get("SLOC"));
		 if(!ServicesUtil.isEmpty(map.get("SHIPADD")))
			 dto.setSHIPADD(map.get("SHIPADD"));
		 if(!ServicesUtil.isEmpty(map.get("CITY")))
			 dto.setCITY(map.get("CITY"));
		 if(!ServicesUtil.isEmpty(map.get("AREACODE")))
			 dto.setAREACODE(map.get("AREACODE"));
		 if(!ServicesUtil.isEmpty(map.get("TELP")))
			 dto.setTELP(map.get("TELP"));
		 if(!ServicesUtil.isEmpty(map.get("SOLDADD")))
			 dto.setSOLDADD(map.get("SOLDADD"));
		 if(!ServicesUtil.isEmpty(map.get("SHIPTYP")))
			 dto.setSHIPTYP(map.get("SHIPTYP"));
		 if(!ServicesUtil.isEmpty(map.get("INSTDELV")))
			 dto.setINSTDELV(map.get("INSTDELV"));
		 if(!ServicesUtil.isEmpty(map.get("SERNUM")))
			 dto.setSERNUM(map.get("SERNUM"));
		 if(!ServicesUtil.isEmpty(map.get("MAT")))
			 dto.setMAT(map.get("MAT"));
		 if(!ServicesUtil.isEmpty(map.get("BATCH")))
			 dto.setBATCH(map.get("BATCH"));
		 if(!ServicesUtil.isEmpty(map.get("DESC")))
			 dto.setDESC(map.get("DESC"));
		 if(!ServicesUtil.isEmpty(map.get("QTY")))
			 dto.setQTY(map.get("QTY"));
		 if(!ServicesUtil.isEmpty(map.get("VOL")))
			 dto.setVOL(map.get("VOL"));
		 if(!ServicesUtil.isEmpty(map.get("STAT")))
			 dto.setSTAT(map.get("STAT"));
			 
		return dto;
	}
	
}



























