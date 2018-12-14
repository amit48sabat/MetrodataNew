package com.incture.metrodata;
import org.quartz.SchedulerException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

import com.incture.metrodata.configuration.HibernateConfiguration;
import com.incture.metrodata.dto.RoleDetailsDTO;
import com.incture.metrodata.service.RoleServiceLocal;

public class Testmain {
	static AbstractApplicationContext context = new AnnotationConfigApplicationContext(HibernateConfiguration.class);

	public static void main(String[] args) throws SchedulerException {
	
		RoleServiceLocal roleService = context.getBean(RoleServiceLocal.class);
		roleService.findAll();
		roleService.findAll();
		roleService.findAll();
		
		RoleDetailsDTO dto = new RoleDetailsDTO();
		dto.setRoleId(1L);
		roleService.find(dto);
	}

}
