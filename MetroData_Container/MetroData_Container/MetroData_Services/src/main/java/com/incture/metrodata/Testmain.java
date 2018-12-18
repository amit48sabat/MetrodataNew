package com.incture.metrodata;
import java.util.HashMap;

import org.quartz.SchedulerException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

import com.incture.metrodata.configuration.HibernateConfiguration;
import com.incture.metrodata.dto.RoleDetailsDTO;
import com.incture.metrodata.service.RoleServiceLocal;

public class Testmain {
	//static AbstractApplicationContext context = new AnnotationConfigApplicationContext(HibernateConfiguration.class);

	public static void main(String[] args) throws SchedulerException {
		 HashMap<Character,Integer> map = new HashMap<>();
		 map.put('c', 1);
		 map.put('v', 2);
		 System.out.println(map.values().toString());
	/*
		RoleServiceLocal roleService = context.getBean(RoleServiceLocal.class);
		roleService.findAll();
		roleService.findAll();
		roleService.findAll();
		
		RoleDetailsDTO dto = new RoleDetailsDTO();
		dto.setRoleId(1L);
		roleService.find(dto);*/
	}

}
