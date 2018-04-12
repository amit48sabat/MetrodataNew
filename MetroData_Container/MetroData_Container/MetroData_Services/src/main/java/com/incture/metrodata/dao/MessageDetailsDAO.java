package com.incture.metrodata.dao;

import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.incture.metrodata.constant.MessageType;
import com.incture.metrodata.dto.CommentsDTO;
import com.incture.metrodata.dto.MessageDetailsDTO;
import com.incture.metrodata.dto.SearchMessageVO;
import com.incture.metrodata.dto.UserDetailsDTO;
import com.incture.metrodata.entity.CommentsDetailsDo;
import com.incture.metrodata.entity.MessageDetailsDo;
import com.incture.metrodata.entity.UserDetailsDo;
import com.incture.metrodata.exceptions.InvalidInputFault;
import com.incture.metrodata.util.ServicesUtil;
import com.incture.metrodata.util.UserComparator;

@Repository("messageDetailsDAO")
public class MessageDetailsDAO extends BaseDao<MessageDetailsDo, MessageDetailsDTO> {

	@Autowired
	CommentsDAO commentsDAO;

	@Autowired
	UserDAO userDao;

	@Override
	MessageDetailsDo importDto(MessageDetailsDTO dto, MessageDetailsDo dos) throws Exception {

		if (ServicesUtil.isEmpty(dos))
			dos = new MessageDetailsDo();
		if (!ServicesUtil.isEmpty(dto)) {

			if (!ServicesUtil.isEmpty(dto.getMessageId()))
				dos.setMessageId(dto.getMessageId());

			if (!ServicesUtil.isEmpty(dto.getTripId())) {
				dos.setTripId(dto.getTripId());
			}
			if (!ServicesUtil.isEmpty(dto.getTitle())) {
				dos.setTitle(dto.getTitle());
			}
			if (!ServicesUtil.isEmpty(dto.getBody())) {
				dos.setBody(dto.getBody());
			}
			if (!ServicesUtil.isEmpty(dto.getType())) {
				dos.setType(dto.getType());
			}
			if (!ServicesUtil.isEmpty(dto.getCreatedAt())) {
				dos.setCreatedAt(dto.getCreatedAt());
			}
			if (!ServicesUtil.isEmpty(dto.getUpdatedAt())) {
				dos.setUpdatedAt(dto.getUpdatedAt());
			}
			if (!ServicesUtil.isEmpty(dto.getLatitude())) {
				dos.setLatitude(dto.getLatitude());
			}
			if (!ServicesUtil.isEmpty(dto.getLongitude())) {
				dos.setLongitude(dto.getLongitude());
			}
			if (!ServicesUtil.isEmpty(dto.getCreatedBy())) {
				dos.setCreatedBy(dto.getCreatedBy().toString());
			}
			if (!ServicesUtil.isEmpty(dto.getUpdatedBy())) {
				dos.setUpdatedBy(dto.getUpdatedBy());
			}
			if (!ServicesUtil.isEmpty(dto.getAddress())) {
				dos.setAddress(dto.getAddress());
			}
			if (!ServicesUtil.isEmpty(dto.getDeliveryNoteId())) {
				dos.setDeliveryNoteId(dto.getDeliveryNoteId());
			}

			// parsing comments
			if (!ServicesUtil.isEmpty(dto.getComments())) {
				List<CommentsDetailsDo> comments = commentsDAO.importList(dto.getComments(), dos.getComments());
				dos.setComments(comments);
			}

			// parsing users
			if (!ServicesUtil.isEmpty(dto.getUsers())) {
				Set<UserDetailsDo> userDos = userDao.importSet(dto.getUsers(), dos.getUsers());
				dos.setUsers(userDos);
			}
		}

		return dos;
	}

	@Override
	MessageDetailsDTO exportDto(MessageDetailsDo dos) {
		MessageDetailsDTO dto = new MessageDetailsDTO();

		if (!ServicesUtil.isEmpty(dos)) {

			if (!ServicesUtil.isEmpty(dos.getMessageId())) {
				dto.setMessageId(dos.getMessageId());
			}
			if (!ServicesUtil.isEmpty(dos.getTripId())) {
				dto.setTripId(dos.getTripId());
			}
			if (!ServicesUtil.isEmpty(dos.getTitle())) {
				dto.setTitle(dos.getTitle());
			}
			if (!ServicesUtil.isEmpty(dos.getBody())) {
				dto.setBody(dos.getBody());
			}
			if (!ServicesUtil.isEmpty(dos.getType())) {
				dto.setType(dos.getType());
			}
			if (!ServicesUtil.isEmpty(dos.getCreatedAt())) {
				dto.setCreatedAt(dos.getCreatedAt());
			}
			if (!ServicesUtil.isEmpty(dos.getUpdatedAt())) {
				dto.setUpdatedAt(dos.getUpdatedAt());
			}
			if (!ServicesUtil.isEmpty(dos.getLatitude())) {
				dto.setLatitude(dos.getLatitude());
			}
			if (!ServicesUtil.isEmpty(dos.getLongitude())) {
				dto.setLongitude(dos.getLongitude());
			}
			if (!ServicesUtil.isEmpty(dos.getAddress())) {
				dto.setAddress(dos.getAddress());
			}
			if (!ServicesUtil.isEmpty(dos.getDeliveryNoteId())) {
				dto.setDeliveryNoteId(dos.getDeliveryNoteId());
			}
			// fetching user by id and setting it to created by
			if (!ServicesUtil.isEmpty(dos.getCreatedBy())) {

				UserDetailsDTO userDto = new UserDetailsDTO();
				userDto.setUserId(dos.getCreatedBy());
				try {
					userDto = userDao.findById(userDto);
				} catch (Exception e) {
				}
				// TODO Auto-generated catch block e.printStackTrace(); }
				if (!ServicesUtil.isEmpty(userDto)) {
					dto.setCreatedBy(userDto);
				}

				//
			}
			if (!ServicesUtil.isEmpty(dos.getUpdatedBy())) {
				dto.setUpdatedBy(dos.getUpdatedBy());
			}

			// parsing comments
			if (!ServicesUtil.isEmpty(dos.getComments())) {

				List<CommentsDTO> comments = commentsDAO.exportList(dos.getComments());
				dto.setComments(comments);
				;
			}

			// parsing users
			if (!ServicesUtil.isEmpty(dos.getUsers())) {
				Set<UserDetailsDTO> userDtos = userDao.exportSet(dos.getUsers(), new UserComparator());
				dto.setUsers(userDtos);
			}
		}
		return dto;
	}

	@SuppressWarnings("unchecked")
	public List<MessageDetailsDTO> findByParam(MessageDetailsDTO dto) {
		Criteria criteria = getSession().createCriteria(MessageDetailsDo.class);
		/*
		 * if(!ServicesUtil.isEmpty(dto.getTripId()))
		 * criteria.add(Restrictions.eq("tripId", dto.getTripId()));
		 * 
		 * if(!ServicesUtil.isEmpty(dto.getUserId()))
		 * criteria.add(Restrictions.eq("userId", dto.getUserId()));
		 * 
		 * // ADDING CRITERIA FOR CREATEDAT BETWEEN START AND END DATES
		 * if(!ServicesUtil.isEmpty(dto.getStartedAt()))
		 * criteria.add(Restrictions.ge("createdAt", dto.getStartedAt()));
		 * if(!ServicesUtil.isEmpty(dto.getEndedAt()))
		 * criteria.add(Restrictions.le("createdAt", dto.getEndedAt()));
		 */

		criteria.addOrder(Order.desc("createdAt"));

		List<MessageDetailsDo> resultDos = criteria.list();
		List<MessageDetailsDTO> resultDtos = exportList(resultDos);
		return resultDtos;
	}

	@SuppressWarnings("unchecked")
	public List<MessageDetailsDTO> findAllMessages(SearchMessageVO dto) throws InvalidInputFault {
		// Criteria criteria =
		// getSession().createCriteria(MessageDetailsDo.class);
		String sql = "SELECT m FROM MessageDetailsDo as m ";

		if (!ServicesUtil.isEmpty(dto.getUserId())) {
			sql += " INNER JOIN m.users as u WHERE (u.userId = '" + dto.getUserId() + "'"+"OR m.createdBy ='" + dto.getUserId() + "')";
		} else {
			sql += " WHERE 1=1 ";
		}

		if (ServicesUtil.isEmpty(dto.getType()))
			throw new InvalidInputFault("'type' is required");
		else
			sql += " AND m.type = '" + dto.getType() + "' ";

		if (!ServicesUtil.isEmpty(dto.getTripId()))
			sql += " AND m.tripId = '" + "'dto.getTripId() ";

		if (!ServicesUtil.isEmpty(dto.getStartedAt()))
			sql += " AND m.createdAt >= :startedAt ";

		if (!ServicesUtil.isEmpty(dto.getEndedAt()))
			sql += " AND m.createdAt <= :endedAt ";

		sql += " order by m.messageId desc";
		Query query = getSession().createQuery(sql);
		if (!ServicesUtil.isEmpty(dto.getStartedAt()))
			query.setDate("startedAt", dto.getStartedAt());
		if (!ServicesUtil.isEmpty(dto.getEndedAt()))
			query.setDate("endedAt", dto.getEndedAt());
		if (!ServicesUtil.isEmpty(dto.getFirstResult()) && !ServicesUtil.isEmpty(dto.getMaxResult())) {
			query.setFirstResult(dto.getFirstResult());
			query.setMaxResults(dto.getMaxResult());
		}

		List<MessageDetailsDo> resultDos = query.list();
		List<MessageDetailsDTO> resultDtos = exportList(resultDos);

		return resultDtos;
	}
}
