package com.incture.metrodata.service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpStatus;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SimpleTrigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.maps.GeoApiContext;
import com.incture.metrodata.constant.DeliveryNoteStatus;
import com.incture.metrodata.constant.Message;
import com.incture.metrodata.constant.MessageType;
import com.incture.metrodata.constant.TripStatus;
import com.incture.metrodata.dao.ContainerDAO;
import com.incture.metrodata.dao.ContainerRecordsDAO;
import com.incture.metrodata.dao.ContainerToDeliveryHeaderDao;
import com.incture.metrodata.dao.CourierDetailsDAO;
import com.incture.metrodata.dao.DeliveryHeaderDAO;
import com.incture.metrodata.dao.TripDAO;
import com.incture.metrodata.dao.UserDAO;
import com.incture.metrodata.dao.WareHouseDAO;
import com.incture.metrodata.dto.ContainerDTO;
import com.incture.metrodata.dto.ContainerDetailsDTO;
import com.incture.metrodata.dto.ContainerItemsDTO;
import com.incture.metrodata.dto.ContainerRecordsDTO;
import com.incture.metrodata.dto.DefaultUserDetailsVO;
import com.incture.metrodata.dto.DeliveryHeaderDTO;
import com.incture.metrodata.dto.MessageDetailsDTO;
import com.incture.metrodata.dto.ResponseDto;
import com.incture.metrodata.dto.TripDetailsDTO;
import com.incture.metrodata.dto.UserDetailsDTO;
import com.incture.metrodata.entity.ContainerRecordsDo;
import com.incture.metrodata.entity.DeliveryHeaderDo;
import com.incture.metrodata.entity.DeliveryItemDo;
import com.incture.metrodata.entity.UserDetailsDo;
import com.incture.metrodata.entity.WareHouseDetailsDo;
import com.incture.metrodata.exceptions.ExecutionFault;
import com.incture.metrodata.exceptions.InvalidInputFault;
import com.incture.metrodata.exceptions.NoResultFault;
import com.incture.metrodata.firebasenotification.NotificationClass;
import com.incture.metrodata.util.HciRestInvoker;
import com.incture.metrodata.util.ServicesUtil;

@Service("containerService")
@Transactional
public class ContainerService implements ContainerServiceLocal {

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

	@Autowired
	ContainerRecordsDAO containerRecordsDAO;

	@Autowired
	ContainerRecordsServiceLocal containerRecordService;

	@Autowired
	UserDAO userDao;
	
	@Autowired
	AsyncCompLocal comp;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ContainerService.class);

	private Integer BATCH_SIZE = 50;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public ResponseDto create(String controllerJson) {

		// map to hold data for processing
		Map<String, Object> data = new HashMap<>();
		
		ResponseDto response = new ResponseDto();
		
		Map<Object, Object> inputDataMap = new LinkedHashMap<>();
		inputDataMap.put("inputString", controllerJson);

		Gson gson = new Gson();
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
		inputDataMap.put("processObject", dto);
		// LOGGER.error("INSIDE CREATE CONTAINER SERVIE WITH REQUEST PAYLOAD =>
		// " + dto);
		if (!ServicesUtil.isEmpty(dto) && !ServicesUtil.isEmpty(dto.getDELIVERY())) {
			List<ContainerDetailsDTO> containerDetailsDTOs = (List<ContainerDetailsDTO>) dto.getDELIVERY().getITEM();

			try {

				long timeStamp = System.currentTimeMillis();
				String jobIdentity = "DnProcessJob" + timeStamp;
				String group = "group" + timeStamp;
				String triggerName = "DnProcessTrigger" + timeStamp;
				String jobName = "Job" + timeStamp;
				Date currdate = new Date();

				JobDetail job = JobBuilder.newJob(ContainerToDeliveryNoteProcessingJob.class)
						.withIdentity(jobIdentity, group).build();

				SimpleTrigger trigger = (SimpleTrigger) TriggerBuilder.newTrigger().withIdentity(triggerName, group)
						.startNow().build();
				Scheduler scheduler = new StdSchedulerFactory().getScheduler();

				ContainerRecordsDo recordsDo = new ContainerRecordsDo();
				recordsDo.setPayload(controllerJson.trim());
				recordsDo.setCreatedAt(currdate);

				// containerRecordService.create(recordsDo);

				containerRecordsDAO.getSession().persist(recordsDo);

				containerRecordsDAO.getSession().flush();
				containerRecordsDAO.getSession().clear();
				containerRecordsDAO.getSession().getTransaction().commit();
				
				// adding data to scheduler context
				data.put("data", dto);
				data.put("timeStamp", currdate);
				data.put("containerRecordId", recordsDo.getId());
				data.put("jobName", jobName);

				comp.backgroudDnProcessing(data);
				
				// adding data to scheduler context
				scheduler.getContext().put("data", dto);
				scheduler.getContext().put("timeStamp", currdate);
				scheduler.getContext().put("containerRecordId", recordsDo.getId());
				scheduler.getContext().put("jobName", jobName);
				
				
				
				/*
				 * int i=1; for (ContainerDetailsDTO d : containerDetailsDTOs) {
				 * containerDao.create(d, new ContainerDetailsDo());
				 * 
				 * if(i % BATCH_SIZE ==0) { containerDao.getSession().flush();
				 * containerDao.getSession().clear(); }
				 * 
				 * i++;
				 * 
				 * }
				 * 
				 * // flushing the session data
				 * containerDao.getSession().flush();
				 * containerDao.getSession().clear();
				 */

				/*scheduler.start();
				scheduler.scheduleJob(job, trigger);*/
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				LOGGER.error("INSIDE CREATE CONTAINER SERVICE : JOB STARTED ID [ "+jobName+" ]");
				
				//scheduler.standby();
				// scheduler.shutdown(true);

				/*
				 * Object data = createEntryInDeliveryHeader(dto); LOGGER.
				 * error("INSIDE CREATE CONTAINER SERVIE WITH RESPONSE PAYLOAD <= "
				 * + data);
				 */
				response.setStatus(true);
				response.setCode(HttpStatus.SC_OK);
				response.setMessage(Message.SUCCESS + "");
			} catch (Exception e) {
				response.setStatus(false);
				response.setCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
				response.setMessage(Message.FAILED + " : " + e.toString());
				e.printStackTrace();
			}
		} else {
			response.setStatus(true);
			response.setMessage(Message.SUCCESS.getValue());
			response.setCode(HttpStatus.SC_OK);
		}

		return response;
	}

	public Integer createEntryInDeliveryHeader(ContainerDTO dto) throws Exception {
		LOGGER.debug("INSIDE createEntryInDeliveryHeader() OF CONTAINER SERVIE");
		Map<Long, String> delNoteAddrMap = new HashMap<>();

		Map<Long, DeliveryHeaderDo> headerMap = importDto(dto.getDELIVERY(), context, delNoteAddrMap);
		DeliveryHeaderDo dos = null;
		int i = 0;
		for (Map.Entry<Long, DeliveryHeaderDo> entry : headerMap.entrySet()) {
			dos = entry.getValue();
			i++;
			LOGGER.error(" Processing DN no => " + dos.getDeliveryNoteId());
			UserDetailsDTO adminDto = new UserDetailsDTO();
			adminDto.setEmail(defaultUserVo.getEmail());
			adminDto = userService.getUserByEmail(adminDto);

			DeliveryHeaderDTO headerDto = new DeliveryHeaderDTO();
			headerDto.setDeliveryNoteId(dos.getDeliveryNoteId());
			String tripId = "";
			try {
				if (!ServicesUtil.isEmpty(dos.getStatus())
						&& dos.getStatus().equals(DeliveryNoteStatus.RFC_DN_INVALIDATED.getValue())) {

					// deleting the mapping btw trip and delivery note if exits

					// getting the delivery notes corresponding trip
					Map<String, Object> map = (Map<String, Object>) tripDao
							.getTripDeliveryNotesCountsByDeliveryNoteId(dos.getDeliveryNoteId());

					if (!ServicesUtil.isEmpty(map) && map.containsKey("tripDto")
							&& !ServicesUtil.isEmpty(map.get("tripDto"))) {
						TripDetailsDTO tripDto = (TripDetailsDTO) map.get("tripDto");
						Long dncount = (Long) map.get("deliveryNoteCount");
						// send notification to driver
						sendNotificationToDriverWhenAdminUpdateDnStatus(headerDto, adminDto, tripDto);
						if (dncount == 1) {

							TripDetailsDTO tripDetailsDTO = new TripDetailsDTO();
							tripDetailsDTO.setTripId(tripId);
							tripDetailsDTO.setStatus(TripStatus.TRIP_STATUS_CANCELLED.getValue());
							tripDetailsDTO.setUpdatedAt(new Date());
							tripDetailsDTO.setUpdatedBy(adminDto.getUserId());
							tripDao.cancelTripById(tripDto.getTripId());
							tripDao.getSession().flush();
							tripDao.getSession().clear();

							// setting to default value
							dos.setAirwayBillNo(null);
							dos.setValidationStatus("false");
							dos.setAwbValidated("false");
						}
					}
					/*
					 * if (!ServicesUtil.isEmpty(tripDto.getTripId()) &&
					 * tripDto.getDeliveryHeader().size() == 1) {
					 * 
					 * // send notification to driver
					 * sendNotificationToDriverWhenAdminUpdateDnStatus(
					 * headerDto, adminDto, tripDto);
					 * 
					 * TripDetailsDTO tripDetailsDTO = new TripDetailsDTO();
					 * tripId = tripDto.getTripId();
					 * tripDetailsDTO.setTripId(tripId);
					 * tripDetailsDTO.setStatus(TripStatus.TRIP_STATUS_CANCELLED
					 * .getValue()); tripDetailsDTO.setUpdatedAt(new Date());
					 * tripDetailsDTO.setUpdatedBy(adminDto.getUserId());
					 * tripDao.cancelTripById(tripDto.getTripId());
					 * tripDao.getSession().flush();
					 * tripDao.getSession().clear();
					 * 
					 * // setting to default value dos.setAirwayBillNo(null);
					 * dos.setValidationStatus("false");
					 * dos.setAwbValidated("false");
					 * 
					 * }
					 */
					deliveryHeaderDao.removeTripDeliveryNoteMapping(headerDto);
					dos.setTripped(false);
				}

				if (!ServicesUtil.isEmpty(dos.getShipToAddress()))
					if (!(dos.getShipToAddress()).equalsIgnoreCase(delNoteAddrMap.get(dos.getDeliveryNoteId()))) {
						try {
							// also set corresponding lat and lng
							Map<String, Double> latAndLong = ServicesUtil.getLatAndLong(dos.getShipToAddress(),
									context);
							dos.setLatitude(latAndLong.get("lat"));
							dos.setLongitude(latAndLong.get("lng"));
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

				// setting warehouse from sloc
				if (!ServicesUtil.isEmpty(dos.getStorageLocation())) {
					WareHouseDetailsDo warehouseDo = new WareHouseDetailsDo();
					warehouseDo.setWareHouseId(dos.getStorageLocation());

					try {
						// find warehouse by id and assigned to delivery note
						warehouseDo = wareHouseDao.find(warehouseDo);
					} catch (Exception e) {
						LOGGER.error("Invalid SLOC id '" + dos.getStorageLocation() + "' no warehouse found");
					}
					dos.setWareHouseDetails(warehouseDo);
				}

				// if status is created set tripped =false
				String isStatusCreated = DeliveryNoteStatus.DELIVERY_NOTE_CREATED.getValue();
				if (!ServicesUtil.isEmpty(dos.getStatus()) && dos.getStatus().equalsIgnoreCase(isStatusCreated)) {
					dos.setTripped(false);
					dos.setAirwayBillNo(null);
					dos.setValidationStatus("false");
					dos.setAwbValidated("false");
				}

				headerDao.persist(dos);
				if (i % BATCH_SIZE == 0) {
					headerDao.getSession().flush();
					headerDao.getSession().clear();
				}

			} catch (Exception e) {
				LOGGER.error("ERROR WHILE CREATING DELIVERY_NOTE. EXCEPTION => " + e.getMessage());
			}
			/*
			 * if (!ServicesUtil.isEmpty(dos.getStatus()) &&
			 * dos.getStatus().equals(DeliveryNoteStatus.DELIVERY_NOTE_CREATED.
			 * getValue())) { if (!ServicesUtil.isEmpty(dos.getTripped()) &&
			 * !dos.getTripped()) dos.setTripped(false); }
			 */

		}

		// marking items as deleted as they are processed

		/*
		 * int rowAffected = 0; Set<Long> deliveryNoteIDsSet =
		 * headerMap.keySet(); if (!ServicesUtil.isEmpty(deliveryNoteIDsSet))
		 * rowAffected =
		 * containerDao.markAsItemsAsProcessed(deliveryNoteIDsSet);
		 * 
		 * // flushing the session data containerDao.getSession().flush();
		 * containerDao.getSession().clear();
		 * LOGGER.error(" INSIDE createEntryInDeliveryHeader(). TOTAL ITEMS < "
		 * + rowAffected +
		 * " > WERE PROCESSED SUCCESSFULLY AND MARKED AS DELETED");
		 */

		return i;

	}

	@Override
	public HciRestInvoker getHciRestInvoker() {
		// TODO Auto-generated method stub
		return hciRestInvoker;
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
				if (map.containsKey(d.getDELIVNO()) == false) {
					try {
						dos = new DeliveryHeaderDo();
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
				} 
				else {
					dos = map.get(d.getDELIVNO());
				}

				if (!currentStatusMap.containsKey(d.getDELIVNO()))
					currentStatusMap.put(d.getDELIVNO(), "");
				if (ServicesUtil.isEmpty(dos.getStatus()))
					prevStatusMap.put(d.getDELIVNO(), "");
				else
					prevStatusMap.put(d.getDELIVNO(), dos.getStatus());

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

				if (!ServicesUtil.isEmpty(d.getINSTDELV())) {
					String instruction = d.getINSTDELV().substring(0, Math.min(d.getINSTDELV().length(), 1000));
					dos.setInstructionForDelivery(instruction);
				}

				/*
				 * // setting warehouse from sloc if
				 * (!ServicesUtil.isEmpty(d.getSLOC())) { WareHouseDetailsDo
				 * warehouseDo = new WareHouseDetailsDo();
				 * warehouseDo.setWareHouseId(d.getSLOC());
				 * 
				 * try { // find warehouse by id and assigned to delivery note
				 * warehouseDo = wareHouseDao.find(warehouseDo); } catch
				 * (Exception e) { throw new
				 * InvalidInputFault("Invalid SLOC id '" + d.getSLOC() +
				 * "' no warehouse found"); }
				 * dos.setWareHouseDetails(warehouseDo); }
				 */

				// set other params
				if (!ServicesUtil.isEmpty(d.getCREATEDT()) && !ServicesUtil.isEmpty(d.getCREATETM())) {
					Date eccCreatedDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(d.getCREATEDT()+" "+d.getCREATETM());
					dos.setCreatedDate(eccCreatedDate);
				}

				dos.setUpdatedAt(currDate);
				if (ServicesUtil.isEmpty(d.getSTAT())) {
					dos.setCreatedAt(currDate);
					if (ServicesUtil.isEmpty(currentStatusMap.get(dos.getDeliveryNoteId())) || currentStatusMap
							.get(dos.getDeliveryNoteId()).equals(DeliveryNoteStatus.DELIVERY_NOTE_CREATED.getValue())) {
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
					if (ServicesUtil.isEmpty(currentStatusMap.get(dos.getDeliveryNoteId())) || currentStatusMap
							.get(dos.getDeliveryNoteId()).equals(DeliveryNoteStatus.RFC_DN_INVALIDATED.getValue())) {
						dos.setStatus(DeliveryNoteStatus.RFC_DN_INVALIDATED.getValue());
						currentStatusMap.put(dos.getDeliveryNoteId(), DeliveryNoteStatus.RFC_DN_INVALIDATED.getValue());
					} else {
						if (!ServicesUtil.isEmpty(prevStatusMap.get(dos.getDeliveryNoteId())) && !prevStatusMap
								.get(dos.getDeliveryNoteId()).equals(DeliveryNoteStatus.RFC_DN_INVALIDATED.getValue()))
							dos.setStatus(prevStatusMap.get(dos.getDeliveryNoteId()));
						else {
							dos.setStatus(DeliveryNoteStatus.DELIVERY_NOTE_CREATED.getValue());
						}
					}

				}

				if (ServicesUtil.isEmpty(d.getSTAT())) {

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

	private void sendNotificationToDriverWhenAdminUpdateDnStatus(DeliveryHeaderDTO headerDto, UserDetailsDTO adminDto,
			TripDetailsDTO tripDetailsDTO) throws IOException {
		LOGGER.debug("INSIDE sendNotificationToDriverWhenAdminUpdateDnStatus() OF CONTAINER SERVIE");
		// getting the corresponding trip drive
		UserDetailsDTO driverDto = tripDetailsDTO.getUser();
		if (!ServicesUtil.isEmpty(driverDto.getMobileToken())) {
			String title = "Delivery Note Cancelled";
			String body = "The delivery note with id (" + headerDto.getDeliveryNoteId()
					+ ") has been cancelled. Please pull " + "to refresh for getting the updated delivery notes list.";
			// notification.sendNotification(title, driverDto.getMobileToken(),
			// body);

			MessageDetailsDTO messageDto = new MessageDetailsDTO();
			messageDto.setTitle(title);
			messageDto.setBody(body);
			messageDto.setTripId(tripDetailsDTO.getTripId());
			messageDto.getUsers().add(driverDto);
			messageDto.setCreatedBy(adminDto.getUserId());
			messageDto.setUpdatedBy(adminDto.getUserId());
			messageDto.setType(MessageType.NOTIFICATION.getValue());
			messageService.create(messageDto, adminDto.getUserId());
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

	@Override
	public List<ContainerDetailsDTO> findAll() {
		ContainerDetailsDTO dto = new ContainerDetailsDTO();
		List<ContainerDetailsDTO> list = null;
		try {
			list = containerDao.findAll(dto);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public void update(ContainerRecordsDTO dto) {
		try {
			containerRecordsDAO.update(dto);
			containerRecordsDAO.getSession().flush();
			containerRecordsDAO.getSession().clear();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public Object test(Long id) {
		Map<String, Object> map = (Map<String, Object>) tripDao.getTripDeliveryNotesCountsByDeliveryNoteId(id);
		if (!ServicesUtil.isEmpty(map) && map.containsKey("tripId")) {
			String tripId = (String) map.get("tripId");
			UserDetailsDo user = (UserDetailsDo) map.get("user");
			Long dncount = (Long) map.get("deliveryNoteCount");
		}
		return map;
	}

	@Override
	public ContainerRecordsDTO getContainerRecordById(Long id) {
		ContainerRecordsDTO dto = new ContainerRecordsDTO();
		dto.setId(id);
		try {
			dto = containerRecordsDAO.find(dto);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return dto;
	}
}
