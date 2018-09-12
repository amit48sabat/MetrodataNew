package com.incture.metrodata.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.incture.metrodata.dto.ContainerDTO;
import com.incture.metrodata.dto.ContainerDetailsDTO;
import com.incture.metrodata.dto.ContainerRecordsDTO;
import com.incture.metrodata.util.ServicesUtil;

@Component
@Lazy
public class AsyncComp implements AsyncCompLocal{
 
	@Autowired
	ContainerServiceLocal containerService;
	
	@Autowired
	DeliveryItemServiceLocal itemService;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AsyncComp.class);
	
	@Async
	@Override
	public void backgroudDnProcessing(Map<String,Object> data) {
		try {
			

			if (!ServicesUtil.isEmpty(data) && !ServicesUtil.isEmpty(data.get("data"))) {

				String payload = (String) data.get("data");
				Gson gson = new Gson();
				ContainerDTO containerDTO = gson.fromJson(payload, ContainerDTO.class);
				
				Date timeStamp = (Date) data.get("timeStamp");
				String jobName = (String) data.get("jobName");

				LOGGER.error(" INSIDE DN_PROCESSING_SCHEDULER STARTED => " + jobName
						+ " FOR TIMESTAMP " + timeStamp);
				Long totalItems = 0L;
				Long containerRecordId = (Long) data.get("containerRecordId");
				Long totalDns = 0L;

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
					LOGGER.error(" DELETED MARKED THE PAYLOAD OF TIMESTAMP " + timeStamp);

					// DELETE THE UNLINK DELIVERY ITEM FROM DELIVERY ITEM TABLE
					// KEEP ONLY ITEMS WHICH ARE MAPPED TO SOME DELIVERY NOTES
					// ONLY
					int rowAffected = itemService.deleteUnlinkDeliveryItems();
					LOGGER.error(" DN_PROCESSING_SCHEDULER. Deleted <" + rowAffected
							+ "> unlinked delivery items from delivery_item table");
					LOGGER.error(" INSIDE DN_PROCESSING_SCHEDULER STOPPING SCHEDULER => "
							+ jobName);
				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	}
}
