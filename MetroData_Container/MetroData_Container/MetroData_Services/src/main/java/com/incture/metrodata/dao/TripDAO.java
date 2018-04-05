package com.incture.metrodata.dao;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.incture.metrodata.constant.DeliveryNoteStatus;
import com.incture.metrodata.constant.TripStatus;
import com.incture.metrodata.dto.DeliveryHeaderDTO;
import com.incture.metrodata.dto.FilterDTO;
import com.incture.metrodata.dto.TripDetailsDTO;
import com.incture.metrodata.dto.WebLeaderBoardVO;
import com.incture.metrodata.entity.DeliveryHeaderDo;
import com.incture.metrodata.entity.TripDetailsDo;
import com.incture.metrodata.exceptions.InvalidInputFault;
import com.incture.metrodata.util.ServicesUtil;
import com.incture.metrodata.util.SortDhDTOByDeliveryOrder;

@Repository("TripDao")
public class TripDAO extends BaseDao<TripDetailsDo, TripDetailsDTO> {

	@Autowired
	DeliveryHeaderDAO headerDao;

	@Override
	TripDetailsDo importDto(TripDetailsDTO dto, TripDetailsDo tripDetailsDo) throws Exception {

		if (ServicesUtil.isEmpty(tripDetailsDo))
			tripDetailsDo = new TripDetailsDo();

		if (!ServicesUtil.isEmpty(dto)) {

			if (!ServicesUtil.isEmpty(dto.getTripId())) {
				tripDetailsDo.setTripId(dto.getTripId());
			}
			if (!ServicesUtil.isEmpty(dto.getCreatedAt())) {
				tripDetailsDo.setCreatedAt(dto.getCreatedAt());
			}
			if (!ServicesUtil.isEmpty(dto.getUpdatedAt())) {
				tripDetailsDo.setUpdatedAt(dto.getUpdatedAt());
			}

			// check trip status if invalid throws expection
			if (!ServicesUtil.isEmpty(dto.getStatus())) {
				if (checkTripStatus(dto.getStatus()))
					tripDetailsDo.setStatus(dto.getStatus());
			}
			if (!ServicesUtil.isEmpty(dto.getCreatedBy())) {
				tripDetailsDo.setCreatedBy(dto.getCreatedBy());
			}
			if (!ServicesUtil.isEmpty(dto.getUpdatedBy())) {
				tripDetailsDo.setUpdatedBy(dto.getUpdatedBy());
			}
			if (!ServicesUtil.isEmpty(dto.getStartTime())) {
				tripDetailsDo.setStartTime(dto.getStartTime());
			}
			if (!ServicesUtil.isEmpty(dto.getEndTime())) {
				tripDetailsDo.setEndTime(dto.getEndTime());
			}
			if (!ServicesUtil.isEmpty(dto.getReasonForCancellation())) {
				tripDetailsDo.setReasonForCancellation(dto.getReasonForCancellation());
			}

			// importing driver details
			if (!ServicesUtil.isEmpty(dto.getUser())) {
				UserDAO userDAO = new UserDAO();
				/*
				 * UserDetailsDo detailsDo = null; try{ detailsDo =
				 * userDAO.getByKeysForFK(dto.getUser()); } catch
				 * (NullPointerException e) { //e.printStackTrace(); }
				 */
				if (!ServicesUtil.isEmpty(tripDetailsDo.getUser()))
					tripDetailsDo.setUser(userDAO.importDto(dto.getUser(), tripDetailsDo.getUser()));
				else	
					tripDetailsDo.setUser(userDAO.importDto(dto.getUser(), null));
			}

			// importing all the delivery headers
			if (!ServicesUtil.isEmpty(dto.getDeliveryHeader())) {
				DeliveryHeaderDAO deliveryHeaderDAO = new DeliveryHeaderDAO();
				Set<DeliveryHeaderDo> deliveryHeaderList = deliveryHeaderDAO.importSet(dto.getDeliveryHeader(),
						tripDetailsDo.getDeliveryHeader());

				tripDetailsDo.setDeliveryHeader(deliveryHeaderList);
			}
		}
		return tripDetailsDo;
	}

	@Override
	TripDetailsDTO exportDto(TripDetailsDo dos) {
		TripDetailsDTO dto = new TripDetailsDTO();

		if (!ServicesUtil.isEmpty(dos)) {
			if (!ServicesUtil.isEmpty(dos.getTripId())) {
				dto.setTripId(dos.getTripId());
			}
			if (!ServicesUtil.isEmpty(dos.getCreatedAt())) {
				dto.setCreatedAt(dos.getCreatedAt());
			}
			if (!ServicesUtil.isEmpty(dos.getUpdatedAt())) {
				dto.setUpdatedAt(dos.getUpdatedAt());
			}
			if (!ServicesUtil.isEmpty(dos.getStatus())) {
				dto.setStatus(dos.getStatus());
			}
			if (!ServicesUtil.isEmpty(dos.getCreatedBy())) {
				dto.setCreatedBy(dos.getCreatedBy());
			}
			if (!ServicesUtil.isEmpty(dos.getUpdatedBy())) {
				dto.setUpdatedBy(dos.getUpdatedBy());
			}
			if (!ServicesUtil.isEmpty(dos.getStartTime())) {
				dto.setStartTime(dos.getStartTime());
			}
			if (!ServicesUtil.isEmpty(dos.getEndTime())) {
				dto.setEndTime(dos.getEndTime());
			}
			if (!ServicesUtil.isEmpty(dos.getReasonForCancellation())) {
				dto.setReasonForCancellation(dos.getReasonForCancellation());
			}

			// exporting driver details
			if (!ServicesUtil.isEmpty(dos.getUser())) {
				UserDAO userDAO = new UserDAO();
				dto.setUser(userDAO.exportDto(dos.getUser()));
			}

			// exporting all the delivery headers
			if (!ServicesUtil.isEmpty(dos.getDeliveryHeader())) {
				DeliveryHeaderDAO deliveryHeaderDAO = new DeliveryHeaderDAO();
				Set<DeliveryHeaderDTO> deliveryHeaderList = deliveryHeaderDAO.exportSet(dos.getDeliveryHeader(),
						new SortDhDTOByDeliveryOrder());
				dto.setDeliveryHeader(deliveryHeaderList);
			}
		}
		return dto;
	}

	/*
	 * for filtering the trip as per trip status
	 */
	@SuppressWarnings("unchecked")
	public List<TripDetailsDTO> findTripByParam(TripDetailsDTO dto) throws InvalidInputFault {

		if (ServicesUtil.isEmpty(dto.getStatus()))
			throw new InvalidInputFault("Trip Status is required.");

		Criteria criteria = getSession().createCriteria(TripDetailsDo.class);
		if (!ServicesUtil.isEmpty(dto.getStatus()))
			criteria.add(Restrictions.eq("status", dto.getStatus()));

		criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

		List<TripDetailsDo> resultDos = criteria.list();
		List<TripDetailsDTO> resultDtos = exportList(resultDos);
		return resultDtos;
	}

	@Override
	public TripDetailsDTO findById(TripDetailsDTO dto) throws InvalidInputFault {

		Criteria criteria = getSession().createCriteria(TripDetailsDo.class);
		Criteria deliveryHeaderCriteria = criteria.createCriteria("deliveryHeader");
		criteria.add(Restrictions.eq("tripId", dto.getTripId()));
		deliveryHeaderCriteria.addOrder(Order.asc("deliveryOrder"));
		TripDetailsDo resultDos = (TripDetailsDo) criteria.uniqueResult();
		return exportDto(resultDos);
	}

	public Object getTotalTripsAndOrders() {
		String hql = " SELECT COUNT(trip_id) AS TOTAL_TRIPS, "
				+ " (SELECT COUNT(deliveryNoteId) FROM DeliveryHeaderDo WHERE tripped = true) AS TOTAL_ORDERS "
				+ "	FROM TripDetailsDo ";
		Query query = getSession().createQuery(hql);
		Object results = query.uniqueResult();
		return results;
	}

	public Object filterRecords(FilterDTO dto) {
		Object data = null;
		Criteria criteria = null;
		String filterBy = dto.getFilterBy();
		String query = dto.getQuery();
		String status = dto.getStatus();

		if (!ServicesUtil.isEmpty(dto)) {

			if (filterBy.equalsIgnoreCase("trip")) {
				criteria = getSession().createCriteria(TripDetailsDo.class);
				criteria.add(Restrictions.ilike("tripId", query, MatchMode.ANYWHERE));
			} else if (filterBy.equalsIgnoreCase("driver")) {
				criteria = getSession().createCriteria(TripDetailsDo.class);
				Criteria userCriteria = criteria.createCriteria("user");
				// query+="%";
				// userCriteria =
				// getSession().createCriteria(UserDetailsDo.class);

				// Criterion userId = Restrictions.ilike("userId", query,
				// MatchMode.ANYWHERE);
				// Criterion name = Restrictions.ilike("name", query,
				// MatchMode.ANYWHERE);

				// To get records matching with OR conditions
				// LogicalExpression orExp = Restrictions.or(userId, name);
				// userCriteria.add( orExp );

				userCriteria.add(Restrictions.ilike("userId", query, MatchMode.ANYWHERE));

			} else if (filterBy.equalsIgnoreCase("delivery_note")) {
				criteria = getSession().createCriteria(TripDetailsDo.class);
				Criteria deliveryHeaderCriteria = criteria.createCriteria("deliveryHeader");
				Long orderId = Long.parseLong(query);
				// criteria =
				// getSession().createCriteria(DeliveryHeaderDo.class);
				deliveryHeaderCriteria
						.add(Restrictions.sqlRestriction(" {alias}.DELIVERY_NOTE_ID LIKE '%" + orderId + "%' "));

			}

			if (!ServicesUtil.isEmpty(status)) {
				criteria.add(Restrictions.eq("status", status).ignoreCase());
			}
			criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
			data = criteria.list();
		}

		return data;
	}

	/**
	 * driverDashbord information
	 */
	@SuppressWarnings("unchecked")
	public Object getDriverDashboardDetails(String userId) {
		String hql = "SELECT status,COUNT(tripId) AS count FROM TripDetailsDo WHERE user ='" + userId
				+ "' GROUP BY status";
		Query query = getSession().createQuery(hql);

		List<List<Object>> result = query.setResultTransformer(Transformers.TO_LIST).list();
		HashMap<String, Long> map = new HashMap<String, Long>();
		for (List<Object> x : result) {
			if (!ServicesUtil.isEmpty(x.get(0)))
				map.put((String) x.get(0), (Long) x.get(1));
		}
		return map;
	}

	/**
	 * driver trip history of driver
	 */
	@SuppressWarnings("unchecked")
	public List<TripDetailsDTO> getTripHistoryByDriverId(String userId,Long start, Long end) {

		List<TripDetailsDo> tripDoList = null;
		Criteria criteria = getSession().createCriteria(TripDetailsDo.class);
		Criteria userCriteria = criteria.createCriteria("user");
		userCriteria.add(Restrictions.eq("userId", userId));
		criteria.addOrder(Order.desc("createdAt"));
		
		if(!ServicesUtil.isEmpty(start) && !ServicesUtil.isEmpty(end))
		{
			criteria.add(Restrictions.ge("endTime", new Date(start))); 
			criteria.add(Restrictions.le("endTime", new Date(end)));
		}
		criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		tripDoList = criteria.list();

		return exportList(tripDoList);
	}

	/*
	 * count delivery header's status report
	 * 
	 */
	@SuppressWarnings("unchecked")
	public Map<String, BigInteger> getDriversDeliveryNoteReport(String userId) {
		Map<String, BigInteger> map = new TreeMap<>();
		String sql = "SELECT dh.status, count(dh.status) FROM delivery_header dh "
				+ " INNER JOIN TRIP_DELIVERY_HEADER_MAPPING th ON th.delivery_note_id = dh.delivery_note_id "
				+ " INNER JOIN trip_details td ON td.trip_id = th.trip_id " + " WHERE td.user_user_id = '" + userId
				+ "' " + " GROUP  BY dh.status ";
		SQLQuery query = getSession().createSQLQuery(sql);
		List<List<Object>> result = (List<List<Object>>) query.setResultTransformer(Transformers.TO_LIST).list();
		for (List<Object> x : result) {
			map.put((String) x.get(0), (BigInteger) x.get(1));
		}
		return map;
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getWebLeaderBoard(WebLeaderBoardVO dto) throws Exception {

		List<Map<String, Object>> resList = new ArrayList<>();

		BigInteger temp = new BigInteger("0");

		// Map<UserDetailsDTO,Object> driverReport = new TreeMap<>(new
		// UserComparator());

		String del_note_completed = " SELECT Count(dh.status) FROM delivery_header dh  INNER JOIN trip_delivery_header_mapping th ON th.delivery_note_id = dh.delivery_note_id "
				+ " INNER JOIN trip_details td ON td.trip_id = th.trip_id  WHERE td.user_user_id = user_id AND dh.status = 'del_note_completed' ";

		String del_note_partially_rejected = " SELECT Count(dh.status) FROM delivery_header dh  INNER JOIN trip_delivery_header_mapping th ON th.delivery_note_id = dh.delivery_note_id "
				+ " INNER JOIN trip_details td ON td.trip_id = th.trip_id  WHERE td.user_user_id = user_id AND dh.status = 'del_note_partially_rejected' ";

		String del_note_rejected = " SELECT Count(dh.status) FROM delivery_header dh  INNER JOIN trip_delivery_header_mapping th ON th.delivery_note_id = dh.delivery_note_id "
				+ " INNER JOIN trip_details td ON td.trip_id = th.trip_id  WHERE td.user_user_id = user_id AND dh.status = 'del_note_rejected' ";

		String orderBy = dto.getSortBy();
		if (!ServicesUtil.isEmpty(dto.getFrom())  && !ServicesUtil.isEmpty(dto.getTo())) {
			java.sql.Date sqlDate = new java.sql.Date(dto.getFrom().getTime()); 
			String fromDate = sqlDate+" 00:00:00";
			sqlDate = new java.sql.Date(dto.getTo().getTime()); 
			String toDate = sqlDate+" 00:00:00";
			del_note_completed += "  AND dh.UPDATED_AT BETWEEN '"+fromDate+"' AND '"+toDate+"' ";
			del_note_partially_rejected += "  AND dh.UPDATED_AT BETWEEN '"+fromDate+"' AND '"+toDate+"' ";
			del_note_rejected += "  AND dh.UPDATED_AT BETWEEN '"+fromDate+"' AND '"+toDate+"' ";
		}
		/*if (!ServicesUtil.isEmpty(dto.getTo())) {
			del_note_completed += "  AND dh.UPDATED_AT <= :toCompleted ";
			del_note_partially_rejected += "  AND dh.UPDATED_AT <= :toPartiallyRejected ";
			del_note_rejected += "  AND dh.UPDATED_AT >= :toRejected ";
		}*/

		String sql = " SELECT user_id, email, name, user_type, del_note_completed, del_note_partially_rejected, del_note_rejected, del_note_completed + del_note_partially_rejected+ del_note_rejected AS total_delivery_note "
				    + " FROM "
				    + "(SELECT *,"
				    + " (" + del_note_completed + ") AS del_note_completed, " + ""
				    + " (" + del_note_rejected + ") AS del_note_rejected, " 
				    + "	(" + del_note_partially_rejected + ") AS del_note_partially_rejected " 
				    + "  FROM user_details "
				    + " ) ORDER BY "+orderBy+" DESC";

		/*if (!orderBy.equals(DeliveryNoteStatus.TOTAL_DEL_NOTE.getValue()))
			sql += "  ORDER BY " + dto.getSortBy() + " DESC	";*/

		SQLQuery query = getSession().createSQLQuery(sql);
		// apply condition for created at if from and to are given
		/*if (!ServicesUtil.isEmpty(dto.getFrom())) {
			query.setParameter("fromCompleted", dto.getFrom());
			query.setParameter("fromPartiallyRejected", dto.getFrom());
			query.setParameter("fromRejected", dto.getFrom());
		}
		// apply condition for created at if from and to are given
		if (!ServicesUtil.isEmpty(dto.getTo())) {
			query.setParameter("toCompleted", dto.getTo());
			query.setParameter("toPartiallyRejected", dto.getTo());
			query.setParameter("toRejected", dto.getTo());
		}
*/
		// query.setParameter("fromDate",dto.getFrom(),TemporalType.DATE);
		query.setFirstResult(dto.getFirstResult());
		query.setMaxResults(dto.getMaxResult());
		List<List<Object>> result = (List<List<Object>>) query.setResultTransformer(Transformers.TO_LIST).list();
		Long totalDns = 0L;
		String userId, name, userType,email;
		Long delCompleted, delRej, delParRej;
		for (List<Object> x : result) {
			Map<String, Object> map = new TreeMap<>();
			map.put(DeliveryNoteStatus.DELIVERY_NOTE_COMPLETED.getValue(), 0L);
			map.put(DeliveryNoteStatus.DELIVERY_NOTE_PARTIALLY_REJECTED.getValue(), 0L);
			map.put(DeliveryNoteStatus.DELIVERY_NOTE_REJECTED.getValue(), 0L);
			if (!ServicesUtil.isEmpty(x.get(0))) {
				userId = (String) x.get(0); // id;
				email = (String) x.get(1);
				name = (String) x.get(2); // id;
				userType = (String) x.get(3); // id;

				temp = (BigInteger) x.get(4); // del note completed
				delCompleted = temp.longValue();
				temp = (BigInteger) x.get(5); // del note parti. rej
				delParRej = temp.longValue();
				temp = (BigInteger) x.get(6); // del note completed
				delRej = temp.longValue();
				
				temp = (BigInteger) x.get(7); // total delivery note 
				totalDns = temp.longValue();

				map.put("userId", userId);
				map.put("name", name);
				map.put("userType", userType);
				map.put("email", email);
				map.put(DeliveryNoteStatus.DELIVERY_NOTE_COMPLETED.getValue(), delCompleted);
				map.put(DeliveryNoteStatus.DELIVERY_NOTE_REJECTED.getValue(), delRej);
				map.put(DeliveryNoteStatus.DELIVERY_NOTE_PARTIALLY_REJECTED.getValue(), delParRej);
				map.put(DeliveryNoteStatus.TOTAL_DEL_NOTE.getValue(), totalDns);

				resList.add(map);
			}
		}

		// driverReport.put(userDto, map);

		// pass those ids to query we will get dn report for that id
		// apply sort by now on this list of records
		return resList;
	}

	private boolean checkTripStatus(String status) throws InvalidInputFault {

		if (TripStatus.TRIP_STATUS_CREATED.getValue().equals(status)
				|| TripStatus.TRIP_STATUS_DRIVER_ASSIGNED.getValue().equals(status)
				|| TripStatus.TRIP_STATUS_STARTED.getValue().equals(status)
				|| TripStatus.TRIP_STATUS_COMPLETED.getValue().equals(status)

		) {
			return true;
		} else {
			throw new InvalidInputFault("trip status '" + status + "' is invalid status code");
		}
	}
}
