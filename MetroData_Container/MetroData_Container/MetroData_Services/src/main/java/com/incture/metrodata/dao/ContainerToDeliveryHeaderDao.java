package com.incture.metrodata.dao;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.google.maps.GeoApiContext;
import com.incture.metrodata.constant.DeliveryNoteStatus;
import com.incture.metrodata.constant.MessageType;
import com.incture.metrodata.dto.ContainerDetailsDTO;
import com.incture.metrodata.dto.ContainerItemsDTO;
import com.incture.metrodata.dto.DefaultUserDetailsVO;
import com.incture.metrodata.dto.DeliveryHeaderDTO;
import com.incture.metrodata.dto.MessageDetailsDTO;
import com.incture.metrodata.dto.UserDetailsDTO;
import com.incture.metrodata.entity.DeliveryHeaderDo;
import com.incture.metrodata.entity.DeliveryItemDo;
import com.incture.metrodata.entity.WareHouseDetailsDo;
import com.incture.metrodata.exceptions.ExecutionFault;
import com.incture.metrodata.exceptions.InvalidInputFault;
import com.incture.metrodata.exceptions.NoResultFault;
import com.incture.metrodata.firebasenotification.NotificationClass;
import com.incture.metrodata.service.MessageServiceLocal;
import com.incture.metrodata.service.UserService;
import com.incture.metrodata.util.ServicesUtil;

@Repository("containerToDeliveryHeaderDao")
public class ContainerToDeliveryHeaderDao {

	
	
	/*public Map<Long, DeliveryHeaderDo> importDto(ContainerItemsDTO dto, GeoApiContext context)
			throws InvalidInputFault, ExecutionFault, NoResultFault, Exception {
		//List<DeliveryHeaderDo> headerList = new ArrayList<DeliveryHeaderDo>();
		Map<Long, DeliveryHeaderDo>  map  =  new HashMap<>();
		DeliveryHeaderDo dos = null;
		//Multimap<DeliveryHeaderDo, DeliveryItemDo> map = ArrayListMultimap.create();
		//Set<Long> isVisited = new HashSet<>();
		if (!ServicesUtil.isEmpty(dto)) {
			List<ContainerDetailsDTO> items = dto.getItem();
			Date currDate = new Date();
			ContainerDetailsDTO t = items.get(0);
			visitedHeaders.add(t.getDELIVNO());
           //Long id;
			for (ContainerDetailsDTO d : items) {

				if (isVisited.add(d.getDELIVNO())) {
					dos = new DeliveryHeaderDo();
					headerList.add(dos);
				}
		      if(ServicesUtil.isEmpty(map.get(d.getDELIVNO())))
		      {   dos = new DeliveryHeaderDo();
		    	  map.put(d.getDELIVNO(), dos);
		      }
		    	dos = map.get(d.getDELIVNO());  
		    	  DeliveryItemDo itemDo = new DeliveryItemDo();

				// setting delivery header
				if (!ServicesUtil.isEmpty(d.getDELIVNO()))
					dos.setDeliveryNoteId(d.getDELIVNO());
				if (!ServicesUtil.isEmpty(d.getSALESGRP()))
					dos.setSalesGroup(d.getSALESGRP());
				if (!ServicesUtil.isEmpty(d.getPURCHORD()))
					dos.setPurchaseOrder(d.getPURCHORD());
				if (!ServicesUtil.isEmpty(d.getREFNO()))
					dos.setRefNo(d.getREFNO());
				if (!ServicesUtil.isEmpty(d.getSLOC()))
					dos.setStorageLocation(d.getSLOC());
				if (!ServicesUtil.isEmpty(d.getSHIPADD()))
					{
					 String shipToAddress = d.getSHIPADD();
					  dos.setShipToAddress(shipToAddress);
					  
					  // also set corresponding lat and lng
						Map<String, Double> latAndLong = ServicesUtil.getLatAndLong(shipToAddress, context);
						dos.setLatitude(latAndLong.get("lat"));
						dos.setLongitude(latAndLong.get("lng"));
					}
				if (!ServicesUtil.isEmpty(d.getCITY()))
					dos.setCity(d.getCITY());
				if (!ServicesUtil.isEmpty(d.getAREACODE()))
					dos.setAreaCode(d.getAREACODE());
				if (!ServicesUtil.isEmpty(d.getTELP()))
					dos.setTelephone(d.getTELP());
				if (!ServicesUtil.isEmpty(d.getSOLDADD()))
					dos.setSoldToAddress(d.getSOLDADD());
				if (!ServicesUtil.isEmpty(d.getSHIPTYP()))
					dos.setShippingType(d.getSHIPTYP());
				if (!ServicesUtil.isEmpty(d.getINSTDELV()))
					dos.setInstructionForDelivery(d.getINSTDELV());
                
				// setting warehouse from sloc
				if (!ServicesUtil.isEmpty(d.getSLOC()))
				{
					WareHouseDetailsDo warehouseDo = new WareHouseDetailsDo();
					warehouseDo.setWareHouseId(d.getSLOC());
					// find warehouse by id and assigned to delivery note
					warehouseDo = wareHouseDao.find(warehouseDo);
					dos.setWareHouseDetails(warehouseDo);
				}
				
				// set other params
				dos.setCreatedDate(currDate);
				dos.setCreatedAt(currDate);
				dos.setUpdatedAt(currDate);
				dos.setTripped(false);
				dos.setStatus(DeliveryNoteStatus.DELIVERY_NOTE_CREATED.getValue());

				// if STAT = X means order is cancelled and we need to remove the mapping of trip and this delivery note and 
				// set status as RFC_invalidated
				if(!ServicesUtil.isEmpty(d.getSTAT()) && d.getSTAT().equals(DeliveryNoteStatus.STAT.getValue())){
				    UserDetailsDTO adminDto = new UserDetailsDTO();
				    adminDto.setEmail(defaultUserVo.getEmail());
				    adminDto = userService.getUserByEmail(adminDto);
					
					
					DeliveryHeaderDTO headerDto = deliveryHeaderDao.exportDto(dos);
				    
					sendNotificationToDriverWhenAdminUpdateDnStatus(headerDto, adminDto);
					
					// deleting the mapping btw trip and delivery note if exits
					deliveryHeaderDao.removeTripDeliveryNoteMapping(headerDto);
					
					// setting delivery note status as RFC_Invaidated and set tripped to false again
					dos.setStatus(DeliveryNoteStatus.RFC_DN_INVALIDATED.getValue()); 
					dos.setTripped(false);
				}
				
				// parsing item do
				if (!ServicesUtil.isEmpty(d.getSERNUM()))
					itemDo.setSerialNum(d.getSERNUM());
				if (!ServicesUtil.isEmpty(d.getMAT()))
					itemDo.setMaterial(d.getMAT());
				if (!ServicesUtil.isEmpty(d.getBATCH()))
					itemDo.setBatch(d.getBATCH());
				if (!ServicesUtil.isEmpty(d.getDESC()))
					itemDo.setDescription(d.getDESC());
				if (!ServicesUtil.isEmpty(d.getQTY()))
					itemDo.setQuantity(d.getQTY());
				if (!ServicesUtil.isEmpty(d.getVOL()))
					itemDo.setVolume(d.getVOL());
                dos.getDeliveryItems().add(itemDo);
				//map.get(d.getDELIVNO()).getDeliveryItems().add(itemDo);
				//map.put(dos, itemDo);
			}

		}

		return map;
	}*/

	
	
}
