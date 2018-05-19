package com.incture.metrodata.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.incture.metrodata.constant.DeliveryNoteStatus;
import com.incture.metrodata.constant.Message;
import com.incture.metrodata.constant.RoleConstant;
import com.incture.metrodata.constant.TripStatus;
import com.incture.metrodata.dao.DeliveryHeaderDAO;
import com.incture.metrodata.dao.TripDAO;
import com.incture.metrodata.dao.UserDAO;
import com.incture.metrodata.dto.DeliveryHeaderDTO;
import com.incture.metrodata.dto.DeliveryItemDTO;
import com.incture.metrodata.dto.FilterDTO;
import com.incture.metrodata.dto.ResponseDto;
import com.incture.metrodata.dto.TripDetailsDTO;
import com.incture.metrodata.dto.UserDetailsDTO;
import com.incture.metrodata.dto.WebLeaderBoardVO;
import com.incture.metrodata.entity.TripDetailsDo;
import com.incture.metrodata.exceptions.ExecutionFault;
import com.incture.metrodata.exceptions.InvalidInputFault;
import com.incture.metrodata.util.RESTInvoker;
import com.incture.metrodata.util.ServicesUtil;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.Barcode128;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;

@Transactional
@Service("tripService")
public class TripService implements TripServiceLocal {

	@Autowired
	TripDAO tripDao;

	@Autowired
	UserDAO userDao;

	@Autowired
	DeliveryHeaderDAO deliveryHeaderDao;

	@Autowired
	RESTInvoker restInvoker;

	private static final Logger LOGGER = LoggerFactory.getLogger(TripService.class);

	/**
	 * api for creating the trip (\/)
	 */
	@Override
	public ResponseDto create(TripDetailsDTO dto) {
		ResponseDto responseDto = new ResponseDto();
		try {
			// setting created at and updated at for dto
			setCreatedAtAndUpdatedAtForDto(dto);

			// assigning tripId before processing
			String tripId = SequenceNumberGen.getInstance().getNextSeqNumber("TRIP", 8, tripDao.getSession());
			dto.setTripId(tripId);

			/*
			 * Date currDate = new Date(); dto.setCreatedAt(currDate);
			 * dto.setUpdateAt(currDate);
			 */

			//
			TripDetailsDo dos = new TripDetailsDo();

			// setTripDoForTripCreate(dto, dos);
			LOGGER.error("INSIDE CREATE TRIP SERVICE. TRIP ID " + tripId);
			dto = tripDao.create(dto, dos);

			responseDto.setStatus(true);
			responseDto.setCode(200);
			responseDto.setData(dto);
			responseDto.setMessage(Message.SUCCESS.toString() + " : Trip created with id " + tripId);
		} catch (InvalidInputFault e) {
			// TODO Auto-generated catch block
			//// LOGGER.error("ERROR WHILE CREATING TRIP : " +e.getMessage());
			responseDto.setStatus(false);
			responseDto.setCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			responseDto.setMessage(e.getMessage());

		} catch (Exception e) {
			responseDto.setStatus(false);
			//// LOGGER.error("ERROR WHILE CREATING TRIP : " +e.getMessage());
			responseDto.setCode(500);
			responseDto.setMessage(Message.FAILED + " : " + e.getMessage());
			e.printStackTrace();
		}
		return responseDto;
	}

	/*
	 * private void setTripDoForTripCreate(TripDetailsDTO dto, TripDetailsDo
	 * dos) throws Exception { DeliveryHeaderDo deliveryHeaderDo; // fetching
	 * dns dos if (!ServicesUtil.isEmpty(dto.getDeliveryHeader())) { for
	 * (DeliveryHeaderDTO d : dto.getDeliveryHeader()) { deliveryHeaderDo =
	 * deliveryHeaderDao.getByKeysForFK(d);
	 * dos.getDeliveryHeader().add(deliveryHeaderDo); } }
	 * 
	 * // fetching user dos if (!ServicesUtil.isEmpty(dto.getUser()))
	 * dos.setUser(userDao.getByKeysForFK(dto.getUser()));
	 * 
	 * }
	 */

	private void setCreatedAtAndUpdatedAtForDto(TripDetailsDTO dto) {
		Date currdate = new Date();

		dto.setUpdatedAt(currdate);
		// setting created by if trip id is empty
		if (ServicesUtil.isEmpty(dto.getTripId())) {
			dto.setCreatedAt(currdate);
		}

		if (!ServicesUtil.isEmpty(dto.getDeliveryHeader())) {
			for (DeliveryHeaderDTO d : dto.getDeliveryHeader()) {
				d.setUpdatedAt(currdate);
			}
		}

	}

	/**
	 * api for updating the trip (\/)
	 */
	@Override
	public ResponseDto update(TripDetailsDTO dto) {
		ResponseDto responseDto = new ResponseDto();
		try {
			// setting created at and updated at for dto
			setCreatedAtAndUpdatedAtForDto(dto);

			if (!ServicesUtil.isEmpty(dto.getStatus()))
				updateTripStatusConstraints(dto);

			// if user/driver is set than add this driver to trip's delivery
			// notes
			if (!ServicesUtil.isEmpty(dto.getUser())) {
				setAssignedUserInDeliveryHeader(dto);
			}
			
			LOGGER.error("INSIDE UPDATE TRIP SERVICE. TRIP ID " + dto.getTripId());
			dto = tripDao.update(dto);

			responseDto.setStatus(true);
			responseDto.setCode(200);
			responseDto.setData(dto);
			responseDto.setMessage(Message.SUCCESS.toString() + " : Trip updated with id " + dto.getTripId());
		} catch (InvalidInputFault e) {
			// LOGGER.error("ERROR WHILE CREATING TRIP : " +e.getMessage());
			responseDto.setStatus(false);
			responseDto.setCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			responseDto.setMessage(e.getMessage());

		} catch (Exception e) {
			responseDto.setStatus(false);
			// LOGGER.error("ERROR WHILE CREATING TRIP : " +e.getMessage());
			responseDto.setCode(500);
			e.printStackTrace();
			responseDto.setMessage(Message.FAILED + " : " + e.getMessage());
		}
		return responseDto;
	}

	private void setAssignedUserInDeliveryHeader(TripDetailsDTO dto) {
		if (!ServicesUtil.isEmpty(dto.getDeliveryHeader())) {
			UserDetailsDTO user = dto.getUser();
			Set<DeliveryHeaderDTO> deliveryHeader = dto.getDeliveryHeader();
			for (DeliveryHeaderDTO d : deliveryHeader) {
				d.setAssignedUser(user.getUserId());
			}
		}
	}

	private void updateTripStatusConstraints(TripDetailsDTO dto) {

		String status = dto.getStatus();
		Date currDate = new Date();
		// if trip status is ENROUTE
		if (status.equalsIgnoreCase(TripStatus.TRIP_STATUS_STARTED.getValue())
				&& ServicesUtil.isEmpty(dto.getStartTime())) {
			dto.setStartTime(currDate);
		}
		// if trip status is ENROUTE
		if (status.equalsIgnoreCase(TripStatus.TRIP_STATUS_COMPLETED.getValue())
				&& ServicesUtil.isEmpty(dto.getEndTime())) {
			dto.setEndTime(currDate);
		}

	}

	/**
	 * api for searching trip by trip id or trip status (\/)
	 */

	@Override
	public ResponseDto findByParam(TripDetailsDTO dto, UserDetailsDTO adminDto) {
		ResponseDto responseDto = new ResponseDto();
		try {
			// find by trip id if trip ID is set or else by trip status
			if (!ServicesUtil.isEmpty(dto.getTripId())) {
				TripDetailsDTO trip = tripDao.findById(dto);
				if (!ServicesUtil.isEmpty(trip.getDeliveryHeader()))
					for (DeliveryHeaderDTO deliveryHeaderDTO : trip.getDeliveryHeader()) {
						if (!ServicesUtil.isEmpty(deliveryHeaderDTO))
							for (DeliveryItemDTO deliveryItemDTO : deliveryHeaderDTO.getDeliveryItems()) {
								if (ServicesUtil.isEmpty(deliveryItemDTO.getSerialNum())) {
									deliveryItemDTO.getScanItems().add(deliveryItemDTO.getMaterial());
								} else {
									String[] serialIds = deliveryItemDTO.getSerialNum().split(",");
									for (String serialId : serialIds) {
										deliveryItemDTO.getScanItems().add(serialId);
									}
								}
							}
					}
				responseDto.setData(trip);
			} else {
				List<TripDetailsDTO> tripList = tripDao.findTripByParam(dto, adminDto);
				responseDto.setData(tripList);
			}
			LOGGER.error("INSIDE FIND TRIP BY PARAM SERVICE. TRIP ID " + dto.getTripId());
			responseDto.setStatus(true);
			responseDto.setCode(200);
			responseDto.setMessage(Message.SUCCESS.toString());

		} catch (Exception e) {
			responseDto.setStatus(false);
			responseDto.setCode(500);
			responseDto.setMessage(Message.FAILED + " : " + e.getMessage());
			//// LOGGER.error("ERROR WHILE CREATING TRIP : " +e.getMessage());
			e.printStackTrace();
		}
		return responseDto;
	}

	/**
	 * api for finding trips by id
	 *//*
		 * @Override public ResponseDto findById(String tripId) { ResponseDto
		 * responseDto = new ResponseDto(); try { TripDetailsDTO dto = new
		 * TripDetailsDTO(); dto.setTripId(tripId); dto = tripDao.findById(dto);
		 * List<TripDetailsDTO> dtoList = new ArrayList<TripDetailsDTO>();
		 * dtoList.add(dto); responseDto.setStatus(true);
		 * responseDto.setMessage(Message.SUCCESS.toString());
		 * responseDto.setDataList(dtoList); } catch (Exception e) {
		 * responseDto.setStatus(false);
		 * responseDto.setMessage(Message.FAILED.toString() + " : " +
		 * e.getMessage()); } return responseDto; }
		 */

	/**
	 * api for deleting the trip by trip id (\/)
	 */
	@Override
	public ResponseDto delete(String tripId) {
		ResponseDto responseDto = new ResponseDto();
		try {
			TripDetailsDTO tripDetailsDTO = new TripDetailsDTO();
			tripDetailsDTO.setTripId(tripId);

			tripDao.deleteById(tripDetailsDTO);
			LOGGER.error("INSIDE DELETE TRIP BY ID SERVICE.");
			responseDto.setStatus(true);
			responseDto.setCode(200);
			responseDto.setMessage(Message.SUCCESS.toString());
		} catch (Exception e) {
			responseDto.setStatus(false);
			responseDto.setCode(500);
			responseDto.setMessage(Message.FAILED + " : " + e.getMessage());
			//// LOGGER.error("ERROR WHILE CREATING TRIP : " +e.getMessage());
		}
		return responseDto;
	}

	/**
	 * api for finding all the trips
	 */
	@Override
	public ResponseDto findAll() {
		ResponseDto responseDto = new ResponseDto();
		try {
			TripDetailsDTO tripDetailsDTO = new TripDetailsDTO();

			Object tripList = tripDao.findAll(tripDetailsDTO);
			responseDto.setStatus(true);
			responseDto.setCode(200);
			responseDto.setData(tripList);
			responseDto.setMessage(Message.SUCCESS.toString());
		} catch (Exception e) {
			responseDto.setStatus(false);
			responseDto.setCode(500);
			responseDto.setMessage(Message.FAILED + " : " + e.getMessage());
		}
		return responseDto;
	}

	/**
	 * api for filtering like trip,order,driver id
	 */
	@Override
	public ResponseDto filter(FilterDTO dto) {
		ResponseDto responseDto = new ResponseDto();
		/*
		 * try { Object data = tripDao.filterRecords(dto);
		 * responseDto.setStatus(true); responseDto.setCode(200);
		 * responseDto.setData(data); responseDto.setMessage(Message.SUCCESS +
		 * "");
		 * 
		 * } catch (Exception e) { responseDto.setStatus(false);
		 * responseDto.setCode(500); responseDto.setMessage(Message.FAILED +
		 * " : " + e.getMessage()); }
		 */
		return responseDto;
	}

	@Override
	/**
	 * api for driver dashboard
	 * 
	 * @param dto
	 * @return
	 */
	public ResponseDto driverDashboardService(String userId) {
		ResponseDto responseDto = new ResponseDto();
		
		 try {
		  
		 // fetching driver trip report 
			 HashMap<String,String> onGoingTrip=  (HashMap<String, String>) tripDao.getLatestOngoingTrip(userId);
		  
		  HashMap<String, Long> tripReport =
		  tripDao.getDriverDashboardDetails(userId); Object deliveryNoteReport
		  = tripDao.getDriversDeliveryNoteReport(userId);
		  
		  // fetching driver profile 
		  String   userProfileString =  restInvoker.getDataFromServer("/Users/" + userId); 
		  JsonParser parser  = new JsonParser(); JsonElement mJson =
		  parser.parse(userProfileString); Gson gson = new Gson(); Object
		  object = gson.fromJson(mJson, Object.class);
		  
		  // setting up response 
		  Map<String, Object> map = new HashMap<>();
		  if(!ServicesUtil.isEmpty(onGoingTrip)){ map.put("trip_id",
		  onGoingTrip.get("tripId")); map.put("trip_status",
		  onGoingTrip.get("status")); }
		  
		  map.put("profile", object); map.put("tripReport", tripReport);
		  map.put("deliveryNoteReport", deliveryNoteReport);
		  responseDto.setCode(200); responseDto.setStatus(true);
		  responseDto.setData(map); responseDto.setMessage(Message.SUCCESS +
		  ""); } catch (Exception e) { responseDto.setCode(500);
		  responseDto.setStatus(false); e.printStackTrace();
		  responseDto.setMessage(e.getMessage()); }
		 
		return responseDto;
	}

	/**
	 * check and assign driver to requesting trip (\/)
	 */
	@Override
	public ResponseDto assigTripDriver(String tripId, String userId) {
		// TODO Auto-generated method stub
		ResponseDto responseDto = new ResponseDto();
		UserDetailsDTO driverDto = new UserDetailsDTO();

		TripDetailsDTO dto = new TripDetailsDTO();
		dto.setTripId(tripId);
		try {
			// feching user if role is not driver throw error
			driverDto.setUserId(userId);
			driverDto = userDao.findById(driverDto);
			if (ServicesUtil.isEmpty(driverDto.getRole()))
				throw new InvalidInputFault("Unauthorized request.");

			String driverInside = RoleConstant.INSIDE_JAKARTA_DRIVER.getValue();
			String driverOutside = RoleConstant.OUTSIDE_JAKARTA_DRIVER.getValue();
			String userRole = driverDto.getRole().getRoleName();

			/*
			 * if(!(userRole.equalsIgnoreCase(driverInside)) ||
			 * userRole.equalsIgnoreCase(driverOutside)) throw new
			 * InvalidInputFault("Invalid request.");
			 */

			dto = tripDao.findById(dto);

			if (ServicesUtil.isEmpty(dto) || ServicesUtil.isEmpty(dto.getCreatedBy()))
				throw new InvalidInputFault("Invalid trip id.");

			if (userRole.equalsIgnoreCase(driverInside)
					&& !dto.getCreatedBy().equalsIgnoreCase(driverDto.getCreatedBy()))
				throw new InvalidInputFault("Invalid request.");

			UserDetailsDTO userDetailsDTO = new UserDetailsDTO();
			if (ServicesUtil.isEmpty(dto.getUser())) {

				userDetailsDTO.setUserId(userId);

				try {
					userDetailsDTO = userDao.getByKeys(userDetailsDTO);
				} catch (Exception e) {
					throw new ExecutionFault("Driver details doesn't exist please log in and try again");
				}

				dto.setUser(userDetailsDTO);
				setAssignedUserInDeliveryHeader(dto);
				dto.setStatus(TripStatus.TRIP_STATUS_DRIVER_ASSIGNED.getValue());

				LOGGER.error("INSIDE ASSIGN DRIVER SERVICE. TRIP ID " + dto.getTripId());

				tripDao.update(dto);
				responseDto.setStatus(true);

				TripDetailsDTO detailsDTO = new TripDetailsDTO();
				detailsDTO.setTripId(dto.getTripId());
				responseDto.setData(detailsDTO);
				responseDto.setCode(HttpStatus.SC_OK);
			} else {
				throw new ExecutionFault(
						"Trip with id " + dto.getTripId() + " is already assigned to driver please scan a new trip ");
			}
		} catch (InvalidInputFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			responseDto.setStatus(false);
			responseDto.setCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			responseDto.setMessage(e.getMessage());

		} catch (Exception e) {
			//// LOGGER.error("ERROR WHILE CREATING TRIP : " +e.getMessage());
			e.printStackTrace();
			responseDto.setStatus(false);
			responseDto.setCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			responseDto.setMessage(e.getMessage());

		}

		return responseDto;
	}

	/***
	 * Api for getting trip history by driver id (\/)
	 */
	@Override
	public ResponseDto getTripHistoryByDriverId(String userId, Long start, Long end) {
		ResponseDto responseDto = new ResponseDto();

		try {
			List<TripDetailsDTO> tripReport = (List<TripDetailsDTO>) tripDao.getTripHistoryByDriverId(userId, start,
					end);
			Map<String, Long> deliveryNoteReport = tripDao.getDriversDeliveryNoteReport(userId);
			long totalTrips = 0L, totalDn = 0L, avgDnsPerTrip = 0L;

			if (!ServicesUtil.isEmpty(tripReport))
				totalTrips = tripReport.size();

			if (totalTrips > 0) {
				totalDn = deliveryNoteReport.get("total_delivery_note");
				avgDnsPerTrip = totalDn / totalTrips;
			}
			LOGGER.error("INSIDE TRIP DRIVER HISTORY SERVICE");
			Map<String, Object> resMap = new HashMap<>();
			resMap.put("tripReport", tripReport);
			resMap.put("deliveryNoteReport", deliveryNoteReport);
			resMap.put("totalTrips", totalTrips);
			resMap.put("avgDnsPerTrip", avgDnsPerTrip);

			// setting the response
			responseDto.setCode(HttpStatus.SC_OK);
			responseDto.setStatus(true);
			responseDto.setData(resMap);
			responseDto
					.setMessage(Message.SUCCESS + " : Fetching trip and delivery note report for driver id " + userId);

		} catch (Exception e) {

			e.printStackTrace();
			responseDto.setStatus(false);
			responseDto.setCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			responseDto.setMessage(e.getMessage());
		}
		return responseDto;
	}

	/**
	 * api for web leader board
	 */
	@Override
	public ResponseDto leaderBoard(WebLeaderBoardVO dto) {
		ResponseDto responseDto = new ResponseDto();
		/*
		 * try {
		 * 
		 * if (ServicesUtil.isEmpty(dto.getFirstResult()) ||
		 * ServicesUtil.isEmpty(dto.getMaxResult())) { dto.setFirstResult(0);
		 * dto.setMaxResult(10); }
		 * 
		 * if (!ServicesUtil.isEmpty(dto.getSortBy())) dto.validateSortBy();
		 * else dto.setSortBy(DeliveryNoteStatus.TOTAL_DEL_NOTE.getValue());
		 * 
		 * // Set<Map<?, ?>> driverReport = new TreeSet(new //
		 * LeaderBoardComparator(sortBy)); List<Map<String, Object>> resultSet =
		 * null;
		 * 
		 * resultSet = tripDao.getWebLeaderBoard(dto);
		 * 
		 * // setting the response responseDto.setCode(200);
		 * responseDto.setStatus(true); responseDto.setData(resultSet);
		 * responseDto.setMessage(Message.SUCCESS.getValue());
		 * 
		 * } catch (Exception e) { e.printStackTrace();
		 * responseDto.setStatus(false);
		 * responseDto.setCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
		 * responseDto.setMessage(e.getMessage()); }
		 */
		return responseDto;
	}

	/**
	 * api for finding all the trip by admin's driver's warehouse ids (\/)
	 */
	@Override
	public ResponseDto getAllTripsAssociatedWithAdminsDrivers(UserDetailsDTO adminDto) {
		ResponseDto responseDto = new ResponseDto();

		try {
			LOGGER.error("INSIDE GET ALL TRIPS ASSOCIATED WITH ADMIN");
			// adminDto = userDao.findById(adminDto);
			Object tripList = tripDao.getAllTripsAssociatedWithAdminsDrivers(adminDto.getUserId(),
					adminDto.getRole().getRoleName(), adminDto.getWareHouseDetails());

			responseDto.setStatus(true);
			responseDto.setCode(HttpStatus.SC_OK);
			responseDto.setData(tripList);
			responseDto.setMessage(Message.SUCCESS.getValue());
		} catch (Exception e) {
			responseDto.setStatus(false);
			responseDto.setCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			responseDto.setMessage(Message.FAILED.getValue());
			e.printStackTrace();
		}
		return responseDto;
	}

	/***
	 * api for admin dashboard as per admin's warehouse id
	 */
	public ResponseDto getAdminDashboardAssociatedWithAdmins(UserDetailsDTO adminDto) {
		ResponseDto responseDto = new ResponseDto();

		try {

			// adminDto = userDao.findById(adminDto);
			Map<String, Long> adminReportData = (Map<String, Long>) tripDao.getAdminDashboardAssociatedWithAdmins(
					adminDto.getUserId(), adminDto.getRole().getRoleName(), adminDto.getWareHouseDetails());

			responseDto.setStatus(true);
			responseDto.setCode(HttpStatus.SC_OK);
			responseDto.setData(adminReportData);
			responseDto.setMessage(Message.SUCCESS.getValue());
		} catch (Exception e) {
			responseDto.setStatus(false);
			responseDto.setCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			responseDto.setMessage(Message.FAILED.getValue());
			e.printStackTrace();
		}
		return responseDto;
	}

	/***
	 * api for filtering trip as per logged in admin (\/)
	 */
	@Override
	public ResponseDto filterTripsAsPerAdmin(UserDetailsDTO adminDto, FilterDTO filterDto) {
		ResponseDto responseDto = new ResponseDto();

		try {

			// adminDto = userDao.findById(adminDto);

			List<TripDetailsDTO> data = tripDao.getFilteredTripsAssociatedWithAdmins(filterDto, adminDto.getUserId(),
					adminDto.getRole().getRoleName(), adminDto.getWareHouseDetails());

			responseDto.setStatus(true);
			responseDto.setCode(HttpStatus.SC_OK);
			responseDto.setData(data);
			responseDto.setMessage(Message.SUCCESS.getValue());
		} catch (Exception e) {
			responseDto.setStatus(false);
			responseDto.setCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			responseDto.setMessage(Message.FAILED.getValue());
			e.printStackTrace();
		}
		return responseDto;
	}

	/**
	 * api for leaderboard report as per logged in admin or super_admin (\/)
	 * 
	 * @param dto
	 * @param adminDto
	 * @return
	 */
	@Override
	public ResponseDto getLeaderBoardAssociatedWithAdmin(WebLeaderBoardVO dto, UserDetailsDTO adminDto) {
		ResponseDto responseDto = new ResponseDto();

		try {

			// adminDto = userDao.findById(adminDto);

			if (ServicesUtil.isEmpty(dto.getSortBy()))
				dto.setSortBy(DeliveryNoteStatus.TOTAL_DEL_NOTE.getValue());

			Object data = tripDao.getLeaderboardAssociatedWithAdminsWarehouse(dto, adminDto.getUserId(),
					adminDto.getRole().getRoleName(), adminDto.getWareHouseDetails());

			responseDto.setStatus(true);
			responseDto.setCode(HttpStatus.SC_OK);
			responseDto.setData(data);
			responseDto.setMessage(Message.SUCCESS.getValue());
		} catch (Exception e) {
			responseDto.setStatus(false);
			responseDto.setCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			responseDto.setMessage(Message.FAILED.getValue());
			e.printStackTrace();
		}
		return responseDto;
	}

	@Override
	public ResponseDto printTripManiFest(String tripId) {
		ResponseDto responseDto = new ResponseDto();
		TripDetailsDTO detailsDTO = new TripDetailsDTO();
		detailsDTO.setTripId(tripId);
		try {
			detailsDTO = tripDao.getByKeys(detailsDTO);
			File file = File.createTempFile("manifest", ".pdf");
			createTripPdf(file.getAbsolutePath(), detailsDTO);
			responseDto.setData(file);
			responseDto.setStatus(true);
		} catch (Exception e) {
			responseDto.setStatus(false);
			e.printStackTrace();
		}

		return responseDto;
	}

	private Document createTripPdf(String dest, TripDetailsDTO detailsDTO) throws IOException, DocumentException {
		Document document = new Document();

		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(dest));
		document.open();
		URL u = TripService.class.getResource("/logo_metrodata.png");
		System.err.println(u);
		Image image = Image.getInstance(u);
		image.scaleAbsoluteWidth(140);
		image.scaleAbsoluteHeight(70);
		document.add(image);

		PdfContentByte canvas = writer.getDirectContent();
		Barcode128 code128 = new Barcode128();
		code128.setCode(detailsDTO.getTripId());
		code128.setCodeType(Barcode128.CODE128);
		code128.setBarHeight(120);
		code128.setN(25);
		PdfTemplate template = code128.createTemplateWithBarcode(canvas, BaseColor.BLACK, BaseColor.BLACK);
		template.setWidth(300);
		template.setHeight(50);
		;
		float x = -300;
		float y = 750;
		float w = template.getWidth();
		float h = template.getHeight();
		canvas.saveState();
		canvas.setColorFill(BaseColor.WHITE);
		canvas.rectangle(x, y, w, h);
		canvas.fill();
		canvas.restoreState();
		canvas.addTemplate(template, 400, 750);
		document.add(new Paragraph("  "));
		document.add(new Paragraph("  "));

		for (DeliveryHeaderDTO deliveryHeaderDTO : detailsDTO.getDeliveryHeader()) {
			Paragraph p = new Paragraph("DELIVERY NOTE : " + deliveryHeaderDTO.getDeliveryNoteId() + " ( "
					+ deliveryHeaderDTO.getShipToAddress() + " )");
			document.add(p);
			document.add(new Paragraph("  "));
			/*
			 * PdfPTable table = new PdfPTable(new float[] { 2, 1, 2 ,2,3});
			 * table.getDefaultCell().setHorizontalAlignment(Element.
			 * ALIGN_CENTER); table.addCell("Material"); table.addCell("Batch");
			 * table.addCell("Description"); table.addCell("QTY");
			 * table.addCell("VOL"); table.setHeaderRows(1); PdfPCell[] cells =
			 * table.getRow(0).getCells(); for (int j = 0; j < cells.length;
			 * j++) { cells[j].setBackgroundColor(BaseColor.GRAY); } for
			 * (DeliveryItemDTO deliveryItemDTO :
			 * deliveryHeaderDTO.getDeliveryItems()) {
			 * table.addCell(deliveryItemDTO.getMaterial());
			 * table.addCell(deliveryItemDTO.getBatch());
			 * table.addCell(deliveryItemDTO.getDescription());
			 * table.addCell(deliveryItemDTO.getQuantity());
			 * table.addCell(deliveryItemDTO.getVolume()); }
			 * document.add(table);
			 */
		}
		document.addTitle("TRIP Manifest");
		document.close();
		return document;

	}
}
