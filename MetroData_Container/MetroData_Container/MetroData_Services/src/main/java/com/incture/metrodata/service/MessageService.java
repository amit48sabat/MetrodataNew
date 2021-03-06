package com.incture.metrodata.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.incture.metrodata.constant.Message;
import com.incture.metrodata.constant.MessageType;
import com.incture.metrodata.constant.RoleConstant;
import com.incture.metrodata.dao.MessageDetailsDAO;
import com.incture.metrodata.dao.UserDAO;
import com.incture.metrodata.dto.CommentsDTO;
import com.incture.metrodata.dto.MessageDetailsDTO;
import com.incture.metrodata.dto.ResponseDto;
import com.incture.metrodata.dto.SearchMessageVO;
import com.incture.metrodata.dto.UserDetailsDTO;
import com.incture.metrodata.entity.MessageDetailsDo;
import com.incture.metrodata.exceptions.InvalidInputFault;
import com.incture.metrodata.firebasenotification.NotificationClass;
import com.incture.metrodata.util.ServicesUtil;

/**
 * @author Lucky.Barkane
 *
 */
@Service("messageService")
@Transactional
public class MessageService implements MessageServiceLocal {

	@Autowired
	MessageDetailsDAO messageDetailsDao;

	@Autowired
	NotificationClass notification;

	@Autowired
	UserDAO userDao;

	private static final Logger LOGGER = LoggerFactory.getLogger(MessageService.class);
	
	/**
	 * api for creating message
	 */
	@Override
	public ResponseDto create(MessageDetailsDTO dto, String createdBy) {
		ResponseDto response = new ResponseDto();
		try {

			setCreatedAtAndUpdatedAtForDto(dto, createdBy);
			List<MessageDetailsDTO> messageDetailsDTOs = new ArrayList<MessageDetailsDTO>();

			// set id for the message
			if (!ServicesUtil.isEmpty(dto.getType())) {
				setMessageId(dto);
			} else {
				throw new InvalidInputFault("Message type is required");
			}
			LOGGER.error("INSIDE CREATE MESSAGE SERVICE. MESSAGE ID "+dto.getMessageId());
			UserDetailsDTO userDto = new UserDetailsDTO();
			// if no user is set than send the mail to all the users
			if (!ServicesUtil.isEmpty(dto.getUsers())) {
				if (!dto.getType().equals(MessageType.INCIDENT.getValue())) {
					for (UserDetailsDTO detailsDTO : dto.getUsers()) {
						userDto.setUserId(detailsDTO.getUserId());
						userDto = userDao.findById(userDto);
						if (!ServicesUtil.isEmpty(userDto.getMobileToken())){
							if(!ServicesUtil.isEmpty(dto.getTripId()))
								notification.sendNotification(dto.getTitle(), userDto.getMobileToken(), dto.getBody(), dto.getTripId());
							else	
							notification.sendNotification(dto.getTitle(), userDto.getMobileToken(), dto.getBody());
						}
							
					}
				}
				dto = messageDetailsDao.create(dto, new MessageDetailsDo());
			} else {
				userDto.setUserId(createdBy);
				userDto = userDao.findById(userDto);
				// send notification to inside jk drivers only
				List<UserDetailsDTO> userList = null;
				if (!ServicesUtil.isEmpty(userDto)) {
					userList = userDao.getUsersAssociateWithAdmin(createdBy, userDto.getRole().getRoleName(),
							userDto.getWareHouseDetails(), RoleConstant.INSIDE_JAKARTA_DRIVER.getValue());
				}

				if (!ServicesUtil.isEmpty(userList)) {
					List<String> tokens = new ArrayList<>();
					for (UserDetailsDTO user : userList) {
						if (!ServicesUtil.isEmpty(user.getMobileToken()))
							tokens.add(user.getMobileToken());
					}
					dto.setUsers(new HashSet<>(userList));
					dto = messageDetailsDao.create(dto, new MessageDetailsDo());
					if (!ServicesUtil.isEmpty(tokens))
					{
						if(!ServicesUtil.isEmpty(dto.getTripId()))
						notification.sendNotification(dto.getTitle(), tokens, dto.getBody(),dto.getTripId());
						else
						 notification.sendNotification(dto.getTitle(), tokens, dto.getBody());
					}
						
				}
			}
			response.setStatus(true);
			response.setCode(HttpStatus.SC_OK);
			messageDetailsDTOs.add(dto);
			response.setData(messageDetailsDTOs);
			response.setMessage(Message.SUCCESS + " : Message created");
		} catch (InvalidInputFault e) {
			response.setStatus(false);
			response.setCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			response.setMessage(Message.FAILED + " : " + e.toString());
			e.printStackTrace();
		} catch (Exception e) {
			response.setStatus(false);
			response.setCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			response.setMessage(Message.FAILED + " : " + e.toString());
			e.printStackTrace();
		}
		return response;
	}

	/**
	 * set the message id FD000.. if feed request, IN000 if incident, NT00.. for
	 * notification
	 * 
	 * @param dto
	 */
	private void setMessageId(MessageDetailsDTO dto) {
		String messageType = dto.getType();
		LOGGER.error("INSIDE setMessageId() OF MESSAGE SERVICE.");
		String seq = "";
		if (messageType.equalsIgnoreCase(MessageType.FEED.getValue())) {
			seq = "FD";
		} else if (messageType.equalsIgnoreCase(MessageType.NOTIFICATION.getValue())) {
			seq = "NT";
		} else if (messageType.equalsIgnoreCase(MessageType.INCIDENT.getValue())) {
			seq = "IN";
		}

		if (!ServicesUtil.isEmpty(seq)) {
			String messageId = SequenceNumberGen.getInstance().getNextSeqNumber(seq, 8, messageDetailsDao.getSession());
			dto.setMessageId(messageId);
		}

	}

	private void setCreatedAtAndUpdatedAtForDto(MessageDetailsDTO dto, String createdBy) {
		Date currdate = new Date();
		LOGGER.error("INSIDE setCreatedAtAndUpdatedAtForDto() OF MESSAGE SERVICE.");
		dto.setUpdatedAt(currdate);
		dto.setUpdatedBy(createdBy);

		// setting created by if message id is empty
		if (ServicesUtil.isEmpty(dto.getMessageId())) {
			dto.setCreatedBy(createdBy);
			dto.setCreatedAt(currdate);
		}

		if (!ServicesUtil.isEmpty(dto.getComments())) {
			for (CommentsDTO d : dto.getComments()) {
				d.setUpdatedAt(currdate);
				if (ServicesUtil.isEmpty(d.getId())) {
					d.setCreatedBy(createdBy);
					d.setCreatedAt(currdate);
				}

			}
		}
		if (!ServicesUtil.isEmpty(dto.getUsers())) {
			for (UserDetailsDTO d : dto.getUsers()) {
				d.setCreatedAt(currdate);
				d.setUpdatedAt(currdate);
			}
		}
	}

	/*
	 * private void setUpdatedAtForDto(MessageDetailsDTO dto, String updatedBy)
	 * { Date currdate = new Date(); dto.setUpdatedAt(currdate);
	 * dto.setUpdatedBy(updatedBy); if
	 * (!ServicesUtil.isEmpty(dto.getComments())) { for (CommentsDTO d :
	 * dto.getComments()) { d.setUpdatedAt(currdate); } } if
	 * (!ServicesUtil.isEmpty(dto.getUsers())) { for (UserDetailsDTO d :
	 * dto.getUsers()) { d.setUpdatedAt(currdate); } } }
	 */

	/**
	 * api for finding message by trip id or user id
	 */
	@Override
	public ResponseDto findByParam(MessageDetailsDTO dto) {
		ResponseDto response = new ResponseDto();
		try {
			Object data = null;
			// find message by id or else with trip id or user id
			if (!ServicesUtil.isEmpty(dto.getMessageId())) {
				data = messageDetailsDao.findById(dto);
			} else {
				data = messageDetailsDao.findByParam(dto);
			}
			LOGGER.error("INSIDE FIND MESSAGE BY PARAM MESSAGE SERVICE.");
			response.setStatus(true);
			response.setCode(HttpStatus.SC_OK);
			response.setData(data);
			response.setMessage(Message.SUCCESS + "");
		} catch (Exception e) {
			response.setStatus(false);
			response.setCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			response.setMessage(Message.FAILED + " : " + e.toString());
			e.printStackTrace();
		}
		return response;
	}

	/**
	 * api for sending the message to user
	 */
	@Override
	public ResponseDto sendMessage(MessageDetailsDTO dto) {
		ResponseDto response = new ResponseDto();
		try {
			// if no user is set than send the mail to all the users
			/*
			 * if (!ServicesUtil.isEmpty(dto.getUserId())) {
			 * userDto.setUserId(dto.getUserId()); userDto =
			 * userDao.findById(userDto);
			 * 
			 * // sending message to user notification.sendNotification
			 * (dto.getTitle(),userDto.getMobileToken(),dto.getBody());
			 * 
			 * } else { List<UserDetailsDTO> userList =
			 * userDao.findAll(userDto); List<String> tokens = new
			 * ArrayList<>(); for(UserDetailsDTO user : userList){
			 * 
			 * tokens.add(user.getMobileToken()); }
			 * notification.sendNotification(dto.getTitle(), tokens,
			 * dto.getBody()); }
			 */
		} catch (Exception e) {
			response.setStatus(false);
			response.setCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			response.setMessage(Message.FAILED + " : " + e.toString());
			e.printStackTrace();
		}
		return response;
	}

	public boolean send(MessageDetailsDTO dto, String userMobToken) {
		return true;
	}

	/**
	 * find all messages between the time duration
	 */
	@Override
	public ResponseDto findAll(SearchMessageVO dto) {
		ResponseDto response = new ResponseDto();
		try {
			
			if (!ServicesUtil.isEmpty(dto.getUserId())){
				UserDetailsDTO userDto = new  UserDetailsDTO();
				userDto.setUserId(dto.getUserId());
				
				userDto = userDao.findById(userDto);
				LOGGER.error("INSIDE FIND ALL MESSAGES SERVICE.");
				List<MessageDetailsDTO> messageList = messageDetailsDao.findAllMessages(dto, userDto.getRole().getRoleName());
				setUserDetailsDtoByCreatedAt(messageList);
				response.setStatus(true);
				response.setCode(HttpStatus.SC_OK);
				response.setData(messageList);
				response.setMessage(Message.SUCCESS + "");
			}
		} catch (Exception e) {
			response.setStatus(false);
			response.setCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			response.setMessage(Message.FAILED + " : " + e.toString());
			e.printStackTrace();
		}
		return response;
	}

	/**
	 * setting the user details dto to message details dto
	 * 
	 * @param messageList
	 */
	private void setUserDetailsDtoByCreatedAt(List<MessageDetailsDTO> messageList) {
		Map<String, UserDetailsDTO> userMap = new TreeMap<>();
		UserDetailsDTO userDto = null;
		String createdBy = null;
		LOGGER.error("INSIDE setUserDetailsDtoByCreatedAt() OF MESSAGE SERVICE.");
		for (MessageDetailsDTO m : messageList) {
			createdBy = m.getCreatedBy().toString();
			userDto = userMap.get(createdBy);
			if (!ServicesUtil.isEmpty(userDto)) {
				m.setCreatedBy(userDto);
			} else {
				userDto = new UserDetailsDTO();
				userDto.setUserId(createdBy);
				try {
					userDto = userDao.findById(userDto);
					m.setCreatedBy(userDto);
					userMap.put(createdBy, userDto);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
				}
			}
		}
	}

	/**
	 * api for updating the message
	 */
	@Override
	public ResponseDto update(MessageDetailsDTO dto, String updatedBy) {
		ResponseDto responseDto = new ResponseDto();
		try {

			setCreatedAtAndUpdatedAtForDto(dto, updatedBy);
			List<MessageDetailsDTO> messageDetailsDTOs = new ArrayList<MessageDetailsDTO>();
			MessageDetailsDTO msgDto = messageDetailsDao.findById(dto);
			UserDetailsDTO detailsDTO = new UserDetailsDTO();
			detailsDTO.setUserId(updatedBy);
			detailsDTO = userDao.findById(detailsDTO);
			int lastCommentIndex = dto.getComments().size() - 1;
			String comment = dto.getComments().get(lastCommentIndex).getComment();
			sendCommentNotification(msgDto, detailsDTO, comment);
			dto = messageDetailsDao.update(dto);

			responseDto.setStatus(true);
			responseDto.setCode(HttpStatus.SC_OK);
			messageDetailsDTOs.add(dto);
			responseDto.setData(messageDetailsDTOs);
			responseDto.setMessage(Message.SUCCESS.toString());
		} catch (Exception e) {
			responseDto.setStatus(false);
			responseDto.setCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			responseDto.setMessage(Message.FAILED + " : " + e.getMessage());
		}
		return responseDto;
	}

	private void sendCommentNotification(MessageDetailsDTO msgDto, UserDetailsDTO detailsDTO, String comment)
			throws IOException {

		if (detailsDTO.getRole().getRoleName().equals(RoleConstant.ADMIN_INSIDE_JAKARTA.getValue())
				|| detailsDTO.getRole().getRoleName().equals(RoleConstant.SUPER_ADMIN.getValue())
				|| detailsDTO.getRole().getRoleName().equals(RoleConstant.COURIER_ADMIN.getValue())) {
			Set<UserDetailsDTO> users = msgDto.getUsers();
			if (!ServicesUtil.isEmpty(users)) {
				List<String> tokens = new ArrayList<>();
				for (UserDetailsDTO user : users) {
					if (!ServicesUtil.isEmpty(user.getMobileToken()))
						tokens.add(user.getMobileToken());
				}

				if (!ServicesUtil.isEmpty(tokens))
					notification.sendNotification("Admin Commented", tokens, comment);
			}
		}

	}

	/**
	 * api for deleting the message
	 */
	@Override
	public ResponseDto delete(MessageDetailsDTO dto) {
		ResponseDto responseDto = new ResponseDto();
		try {

			messageDetailsDao.deleteById(dto);
			responseDto.setStatus(true);
			responseDto.setCode(HttpStatus.SC_OK);
			responseDto.setMessage(Message.SUCCESS.toString());
		} catch (Exception e) {
			responseDto.setStatus(false);
			responseDto.setCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			responseDto.setMessage(Message.FAILED + " : " + e.getMessage());
		}
		return responseDto;
	}

}
