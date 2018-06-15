package com.incture.metrodata.service;

import java.util.Date;
import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.stereotype.Component;

import com.incture.metrodata.configuration.AppConfig;
import com.incture.metrodata.dao.ContainerRecordsDAO;
import com.incture.metrodata.dto.ContainerDTO;
import com.incture.metrodata.dto.ContainerDetailsDTO;
import com.incture.metrodata.dto.ContainerRecordsDTO;
import com.incture.metrodata.entity.ContainerRecordsDo;
import com.incture.metrodata.util.ServicesUtil;

@Component
@Scope("prototype")
public class ContainerToDeliveryNoteProcessingJob implements Job {
	static AbstractApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
	ContainerServiceLocal containerService = (ContainerServiceLocal) context.getBean("containerService");
	DeliveryItemServiceLocal itemService = (DeliveryItemServiceLocal) context.getBean("deliveryItemService");
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ContainerToDeliveryNoteProcessingJob.class);

	@SuppressWarnings("unchecked")
	public void execute(JobExecutionContext con) throws JobExecutionException {

		try {
			Scheduler scheduler = con.getScheduler();
			
			
			if (!ServicesUtil.isEmpty(scheduler.getContext().get("data"))) {
				
			 Date timeStamp = (Date) scheduler.getContext().get("timeStamp");
			 LOGGER.error(" INSIDE DN_PROCESSING_SCHEDULER STARTED => "+scheduler.getSchedulerInstanceId() +" FOR TIMESTAMP "+timeStamp);
			 Long totalItems = 0L;
			 Long containerRecordId =  (Long) scheduler.getContext().get("containerRecordId");
			 Long totalDns = 0L;
				ContainerDTO containerDTO = (ContainerDTO) con.getScheduler().getContext().get("data");
                
				if (!ServicesUtil.isEmpty(containerDTO)) {
					LOGGER.error(" INSIDE CONTAINER_TO_DN_PROS_JOB.");
					totalDns = (long) containerService.createEntryInDeliveryHeader(containerDTO);
					List<ContainerDetailsDTO> items = (List<ContainerDetailsDTO>) containerDTO.getDELIVERY().getITEM();
					totalItems = (long) items.size();
					
					ContainerRecordsDTO dto = new ContainerRecordsDTO();
					
					dto.setCreatedAt(timeStamp);
					dto.setDeleted(1);
					dto.setTotalDns(totalDns);
					dto.setTotalItems(totalItems);
					dto.setId(containerRecordId);
					containerService.update(dto);
					LOGGER.error(" DELETED MARKED THE PAYLOAD OF TIMESTAMP "+timeStamp );
					
					// DELETE THE UNLINK DELIVERY ITEM FROM DELIVERY ITEM TABLE KEEP ONLY ITEMS WHICH ARE MAPPED TO SOME DELIVERY NOTES ONLY
					int rowAffected = itemService.deleteUnlinkDeliveryItems();
					LOGGER.error(" DN_PROCESSING_SCHEDULER. Deleted <"+rowAffected+"> unlinked delivery items from delivery_item table");
					LOGGER.error(" INSIDE DN_PROCESSING_SCHEDULER STOPPING SCHEDULER => "+scheduler.getSchedulerInstanceId());
				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
