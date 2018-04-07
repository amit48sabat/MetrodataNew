package com.incture.metrodata;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

import com.incture.metrodata.configuration.AppConfig;
import com.incture.metrodata.dto.SearchMessageVO;
import com.incture.metrodata.service.MessageServiceLocal;

public class Testmain {

	public static void main(String[] args) {

		AbstractApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
		 MessageServiceLocal tripServiceLocal= (MessageServiceLocal) context.getBean("messageService");
		 SearchMessageVO vo = new SearchMessageVO();
		 vo.setType("feed");
		 vo.setUserId("P000027");
		 tripServiceLocal.findAll(vo);
	}
}
