package com.incture.metrodata.service;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.gson.internal.LinkedTreeMap;
import com.google.maps.GeoApiContext;
import com.incture.metrodata.constant.DeliveryNoteStatus;
import com.incture.metrodata.constant.Message;
import com.incture.metrodata.constant.MessageType;
import com.incture.metrodata.constant.TripStatus;
import com.incture.metrodata.dao.ContainerDAO;
import com.incture.metrodata.dao.ContainerToDeliveryHeaderDao;
import com.incture.metrodata.dao.CourierDetailsDAO;
import com.incture.metrodata.dao.DeliveryHeaderDAO;
import com.incture.metrodata.dao.TripDAO;
import com.incture.metrodata.dao.WareHouseDAO;
import com.incture.metrodata.dto.ContainerDTO;
import com.incture.metrodata.dto.ContainerDetailsDTO;
import com.incture.metrodata.dto.ContainerItemsDTO;
import com.incture.metrodata.dto.DefaultUserDetailsVO;
import com.incture.metrodata.dto.DeliveryHeaderDTO;
import com.incture.metrodata.dto.MessageDetailsDTO;
import com.incture.metrodata.dto.TripDetailsDTO;
import com.incture.metrodata.dto.UserDetailsDTO;
import com.incture.metrodata.entity.DeliveryHeaderDo;
import com.incture.metrodata.entity.DeliveryItemDo;
import com.incture.metrodata.entity.WareHouseDetailsDo;
import com.incture.metrodata.exceptions.ExecutionFault;
import com.incture.metrodata.exceptions.InvalidInputFault;
import com.incture.metrodata.exceptions.NoResultFault;
import com.incture.metrodata.firebasenotification.NotificationClass;
import com.incture.metrodata.util.HciRestInvoker;
import com.incture.metrodata.util.ServicesUtil;

import lombok.Getter;
import lombok.Setter;

@Component
@Scope("prototype")
@Getter
@Setter
public class HciDNProcessingThread  extends Thread{

	
	@Autowired
	ContainerDAO containerDao;

	@Autowired
	ContainerToDeliveryHeaderDao containerToHeaderDao;

	@Autowired
	DeliveryHeaderDAO headerDao;

	@Autowired
	GeoApiContext context;

	@Autowired
	HciRestInvoker hciRestInvoker;

	@Autowired
	CourierDetailsDAO courierDao;

	@Autowired
	DeliveryHeaderDAO deliveryHeaderDao;

	@Autowired
	NotificationClass notification;

	@Autowired
	private TripDAO tripDao;

	@Autowired
	MessageServiceLocal messageService;

	@Autowired
	UserServiceLocal userService;

	@Autowired
	DefaultUserDetailsVO defaultUserVo;

	@Autowired
	WareHouseDAO wareHouseDao;
	
    private ContainerDTO containerDTO;

    private Integer BATCH_SIZE   = 25;
    
    private static final Logger LOGGER = LoggerFactory.getLogger(HciDNProcessingThread.class);
    
	@Override
	public void run() {
		try {
			processDNInThread(containerDTO);
		} catch (Exception e) {
			 e.printStackTrace();
		}
	}

	
	@SuppressWarnings({ "unchecked", "unused" })
	private void processDNInThread(ContainerDTO containerDTO){

		List<ContainerDetailsDTO> containerDetailsDTOs = (List<ContainerDetailsDTO>) containerDTO.getDELIVERY().getITEM();
		LOGGER.error(" system time1 " + System.currentTimeMillis());
		try {
			/*int i=1;
			for (ContainerDetailsDTO d : containerDetailsDTOs) {
				containerDao.create(d, new ContainerDetailsDo());
				
				if(i % BATCH_SIZE ==0)
					containerDao.getSession().flush();
				
				i++; 
				
			}*/
			LOGGER.error(" system time2 " + System.currentTimeMillis());
			Object data = createEntryInDeliveryHeader(containerDTO);
			LOGGER.error("INSIDE CREATE CONTAINER SERVIE WITH RESPONSE PAYLOAD <= " + data);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private Map<Long, DeliveryHeaderDo> createEntryInDeliveryHeader(ContainerDTO dto) throws Exception {
		LOGGER.debug("INSIDE createEntryInDeliveryHeader() OF CONTAINER SERVIE");
		Map<Long, String> delNoteAddrMap = new HashMap<>();

		Map<Long, DeliveryHeaderDo> headerMap = importDto(dto.getDELIVERY(), context, delNoteAddrMap);
		DeliveryHeaderDo dos = null;
		int i=1;
		for (Map.Entry<Long, DeliveryHeaderDo> entry : headerMap.entrySet()) {
			dos = entry.getValue();
			LOGGER.error(" Processing DN no => " + dos.getDeliveryNoteId());
			UserDetailsDTO adminDto = new UserDetailsDTO();
			adminDto.setEmail(defaultUserVo.getEmail());
			adminDto = userService.getUserByEmail(adminDto);

			DeliveryHeaderDTO headerDto = new DeliveryHeaderDTO();
			headerDto.setDeliveryNoteId(dos.getDeliveryNoteId());

			if (!ServicesUtil.isEmpty(dos.getStatus())
					&& dos.getStatus().equals(DeliveryNoteStatus.RFC_DN_INVALIDATED.getValue())) {

				sendNotificationToDriverWhenAdminUpdateDnStatus(headerDto, adminDto);

				// deleting the mapping btw trip and delivery note if exits

				// getting the delivery notes corresponding trip
				TripDetailsDTO tripDto = tripDao.getTripDeliveryNotesCountsByDeliveryNoteId(dos.getDeliveryNoteId());
				if (!ServicesUtil.isEmpty(tripDto.getTripId()) && tripDto.getDeliveryHeader().size() == 1) {
					TripDetailsDTO tripDetailsDTO = new TripDetailsDTO();
					tripDetailsDTO.setTripId(tripDto.getTripId());
					tripDetailsDTO.setStatus(TripStatus.TRIP_STATUS_CANCELLED.getValue());
					tripDetailsDTO.setUpdatedAt(new Date());
					tripDetailsDTO.setUpdatedBy(adminDto.getUserId());
					tripDao.cancelTripById(tripDto.getTripId());
					tripDao.getSession().flush();
					tripDao.getSession().clear();

				}
				deliveryHeaderDao.removeTripDeliveryNoteMapping(headerDto);
				dos.setTripped(false);
			}
			if (!ServicesUtil.isEmpty(dos.getStatus())
					&& dos.getStatus().equals(DeliveryNoteStatus.DELIVERY_NOTE_CREATED.getValue())) {
				if (!ServicesUtil.isEmpty(dos.getTripped()) && !dos.getTripped())
					dos.setTripped(false);
			}

			if (!ServicesUtil.isEmpty(dos.getShipToAddress()))
				if (!(dos.getShipToAddress()).equalsIgnoreCase(delNoteAddrMap.get(dos.getDeliveryNoteId()))) {
					try {
						// also set corresponding lat and lng
						Map<String, Double> latAndLong = ServicesUtil.getLatAndLong(dos.getShipToAddress(), context);
						dos.setLatitude(latAndLong.get("lat"));
						dos.setLongitude(latAndLong.get("lng"));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

			headerDao.persist(dos);
			
			if(i % BATCH_SIZE ==0)
				headerDao.getSession().flush();
			
			i++;
			
		}

		return headerMap;

	}

	
	public Map<Long, DeliveryHeaderDo> importDto(ContainerItemsDTO dto, GeoApiContext context,
			Map<Long, String> delNoteAddrMap) throws InvalidInputFault, ExecutionFault, NoResultFault, Exception {
		// List<DeliveryHeaderDo> headerList = new
		// ArrayList<DeliveryHeaderDo>();
		Map<Long, DeliveryHeaderDo> map = new HashMap<>();
		Map<Long, String> currentStatusMap = new HashMap<>();
		Map<Long, String> prevStatusMap = new HashMap<>();
		DeliveryHeaderDo dos = null;
		// Multimap<DeliveryHeaderDo, DeliveryItemDo> map =
		// ArrayListMultimap.create();
		// Set<Long> isVisited = new HashSet<>();
		if (!ServicesUtil.isEmpty(dto)) {
			@SuppressWarnings("unchecked")
			List<ContainerDetailsDTO> items = (List<ContainerDetailsDTO>) dto.getITEM();
			Date currDate = new Date();
			/*
			 * ContainerDetailsDTO t = items.get(0);
			 * visitedHeaders.add(t.getDELIVNO());
			 */
			// Long id;
			for (ContainerDetailsDTO d : items) {

				/*
				 * if (isVisited.add(d.getDELIVNO())) { dos = new
				 * DeliveryHeaderDo(); headerList.add(dos); }
				 */
				if (ServicesUtil.isEmpty(map.get(d.getDELIVNO()))) {
					dos = new DeliveryHeaderDo();
					try {
						dos.setDeliveryNoteId(d.getDELIVNO());
						// fetching the delivery note if exits
						dos = deliveryHeaderDao.find(dos);
						delNoteAddrMap.put(dos.getDeliveryNoteId(), dos.getShipToAddress());
						List<DeliveryItemDo> deliveryItems = new ArrayList<DeliveryItemDo>();
						dos.setDeliveryItems(deliveryItems);
					} catch (InvalidInputFault e) {
						dos = new DeliveryHeaderDo();
					}

					map.put(d.getDELIVNO(), dos);
					if (!currentStatusMap.containsKey(d.getDELIVNO()))
						currentStatusMap.put(d.getDELIVNO(), "");
					prevStatusMap.put(d.getDELIVNO(), dos.getStatus());
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
					dos.setRefNo(Long.parseLong(d.getREFNO()));
				if (!ServicesUtil.isEmpty(d.getSLOC()))
					dos.setStorageLocation(d.getSLOC());
				if (!ServicesUtil.isEmpty(d.getSHIPADD())) {
					String shipToAddress = d.getSHIPADD();
					dos.setShipToAddress(shipToAddress);
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
				if (!ServicesUtil.isEmpty(d.getSLOC())) {
					WareHouseDetailsDo warehouseDo = new WareHouseDetailsDo();
					warehouseDo.setWareHouseId(d.getSLOC());

					try {
						// find warehouse by id and assigned to delivery note
						warehouseDo = wareHouseDao.find(warehouseDo);
					} catch (Exception e) {
						throw new InvalidInputFault("Invalid SLOC id '" + d.getSLOC() + "' no warehouse found");
					}
					dos.setWareHouseDetails(warehouseDo);
				}

				// set other params
				dos.setCreatedDate(currDate);
				dos.setUpdatedAt(currDate);

				if (ServicesUtil.isEmpty(d.getSTAT())) {
					dos.setCreatedAt(currDate);
					if (!ServicesUtil.isEmpty(currentStatusMap.get(dos.getDeliveryNoteId()))
							&& currentStatusMap.get(dos.getDeliveryNoteId())
									.equals(DeliveryNoteStatus.DELIVERY_NOTE_CREATED.getValue())
							|| currentStatusMap.get(dos.getDeliveryNoteId()).equals("")) {
						dos.setStatus(DeliveryNoteStatus.DELIVERY_NOTE_CREATED.getValue());
						currentStatusMap.put(dos.getDeliveryNoteId(),
								DeliveryNoteStatus.DELIVERY_NOTE_CREATED.getValue());
					} else {
						if (!ServicesUtil.isEmpty(prevStatusMap.get(dos.getDeliveryNoteId())) && !prevStatusMap
								.get(dos.getDeliveryNoteId()).equals(DeliveryNoteStatus.RFC_DN_INVALIDATED.getValue()))
							dos.setStatus(prevStatusMap.get(dos.getDeliveryNoteId()));
						else {
							dos.setStatus(DeliveryNoteStatus.DELIVERY_NOTE_CREATED.getValue());
						}

					}
				}

				// if STAT = X means order is cancelled and we need to remove
				// the mapping of trip and this delivery note and
				// set status as RFC_invalidated
				if ((!ServicesUtil.isEmpty(d.getSTAT()) && d.getSTAT().equals(DeliveryNoteStatus.STAT.getValue()))) {

					// setting delivery note status as RFC_Invaidated and set
					// tripped to false again
					if ( !ServicesUtil.isEmpty(currentStatusMap.get(dos.getDeliveryNoteId())) && currentStatusMap.get(dos.getDeliveryNoteId())
							.equals(DeliveryNoteStatus.RFC_DN_INVALIDATED.getValue())
							|| currentStatusMap.get(dos.getDeliveryNoteId()).equals("")) {
						dos.setStatus(DeliveryNoteStatus.RFC_DN_INVALIDATED.getValue());
						currentStatusMap.put(dos.getDeliveryNoteId(), DeliveryNoteStatus.RFC_DN_INVALIDATED.getValue());
					} else {
						if (!ServicesUtil.isEmpty(prevStatusMap.get(dos.getDeliveryNoteId())) && !prevStatusMap.get(dos.getDeliveryNoteId())
								.equals(DeliveryNoteStatus.RFC_DN_INVALIDATED.getValue()))
							dos.setStatus(prevStatusMap.get(dos.getDeliveryNoteId()));
						else {
							dos.setStatus(DeliveryNoteStatus.DELIVERY_NOTE_CREATED.getValue());
						}
					}

				}

				if (ServicesUtil.isEmpty(d.getSTAT()) || !d.getSTAT().equals(DeliveryNoteStatus.STAT.getValue())) {
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
				}
				// map.get(d.getDELIVNO()).getDeliveryItems().add(itemDo);
				// map.put(dos, itemDo);
			}

		}

		return map;
	}

	private void sendNotificationToDriverWhenAdminUpdateDnStatus(DeliveryHeaderDTO headerDto, UserDetailsDTO adminDto)
			throws IOException {
		LOGGER.debug("INSIDE sendNotificationToDriverWhenAdminUpdateDnStatus() OF CONTAINER SERVIE");
		// getting the corresponding trip drive
		UserDetailsDTO driverDto = tripDao.getDriverFromTripByDN(headerDto);
		if (!ServicesUtil.isEmpty(driverDto.getMobileToken())) {
			String title = "Delivery Note Cancelled";
			String body = "The delivery note with id (" + headerDto.getDeliveryNoteId()
					+ ") has been cancelled. Please pull " + "to refresh for getting the updated delivery notes list.";
			// notification.sendNotification(title, driverDto.getMobileToken(),
			// body);

			MessageDetailsDTO messageDto = new MessageDetailsDTO();
			messageDto.setTitle(title);
			messageDto.setBody(body);
			messageDto.getUsers().add(driverDto);
			messageDto.setCreatedBy(adminDto.getUserId());
			messageDto.setUpdatedBy(adminDto.getUserId());
			messageDto.setType(MessageType.NOTIFICATION.getValue());
			messageService.create(messageDto, adminDto.getUserId());
		}

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
