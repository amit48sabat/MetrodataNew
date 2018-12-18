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
import com.incture.metrodata.constant.RoleConstant;
import com.incture.metrodata.constant.TripStatus;
import com.incture.metrodata.dto.DeliveryHeaderDTO;
import com.incture.metrodata.dto.FilterDTO;
import com.incture.metrodata.dto.ResponseDto;
import com.incture.metrodata.dto.TripDetailsDTO;
import com.incture.metrodata.dto.UserDetailsDTO;
import com.incture.metrodata.dto.WareHouseDetailsDTO;
import com.incture.metrodata.dto.WebLeaderBoardVO;
import com.incture.metrodata.entity.DeliveryHeaderDo;
import com.incture.metrodata.entity.TripDetailsDo;
import com.incture.metrodata.entity.UserDetailsDo;
import com.incture.metrodata.exceptions.ExecutionFault;
import com.incture.metrodata.exceptions.InvalidInputFault;
import com.incture.metrodata.util.PaginationUtil;
import com.incture.metrodata.util.ServicesUtil;
import com.incture.metrodata.util.SortDhDTOByDeliveryOrder;

@Repository("TripDao")
public class TripDAO extends BaseDao<TripDetailsDo, TripDetailsDTO> {

	@Autowired
	DeliveryHeaderDAO deliveryHeaderDAO;

	@Autowired
	UserDAO userDAO;

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
			if (!ServicesUtil.isEmpty(dto.getCreatedBy()) && ServicesUtil.isEmpty(tripDetailsDo.getCreatedBy())) {
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
			if (!ServicesUtil.isEmpty(dto.getTrackFreq())) {
				tripDetailsDo.setTrackFreq(dto.getTrackFreq());
			}

			// importing driver details
			if (!ServicesUtil.isEmpty(dto.getUser())) {

				UserDetailsDo userDetailsDo = new UserDetailsDo();
				try {
					userDetailsDo = userDAO.getByKeysForFK(dto.getUser());
				} catch (Exception e) {
					// TODO: handle exception
				}
				tripDetailsDo.setUser(userDAO.importDto(dto.getUser(), userDetailsDo));

			}

			// importing all the delivery headers
			if (!ServicesUtil.isEmpty(dto.getDeliveryHeader())) {
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
			if (!ServicesUtil.isEmpty(dos.getTrackFreq())) {
				dto.setTrackFreq(dos.getTrackFreq());
			}

			// exporting driver details
			if (!ServicesUtil.isEmpty(dos.getUser())) {

				dto.setUser(userDAO.exportDto(dos.getUser()));
			}

			// exporting all the delivery headers
			if (!ServicesUtil.isEmpty(dos.getDeliveryHeader())) {
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
	public List<TripDetailsDTO> findTripByParam(TripDetailsDTO dto, UserDetailsDTO adminDto) throws InvalidInputFault {

		if (ServicesUtil.isEmpty(dto.getStatus()))
			throw new InvalidInputFault("Trip Status is required.");

		String roleName = adminDto.getRole().getRoleName();

		Criteria criteria = getSession().createCriteria(TripDetailsDo.class);
		if (!ServicesUtil.isEmpty(dto.getStatus()))
			criteria.add(Restrictions.eq("status", dto.getStatus()));

		// if not a super admin or sales admin than show trips that are created
		// by him only
		if (roleName.equals(RoleConstant.ADMIN_INSIDE_JAKARTA.getValue())
				|| roleName.equals(RoleConstant.ADMIN_OUTSIDE_JAKARTA.getValue())) {
			criteria.add(Restrictions.eq("createdBy", adminDto.getUserId()));
		}
		// else if role is courier admin than show trips where the driver and
		// courier
		// admin belong to same warehouse
		else if (roleName.equals(RoleConstant.COURIER_ADMIN.getValue())) {
			Criteria userCriteria = criteria.createCriteria("user");
			userCriteria.add(Restrictions.eq("createdBy", adminDto.getUserId()));
		}

		criteria.addOrder(Order.desc("createdAt"));
		criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		criteria.setFirstResult(PaginationUtil.FIRST_RESULT);
		criteria.setMaxResults(PaginationUtil.MAX_RESULT);

		List<TripDetailsDo> resultDos = criteria.list();
		List<TripDetailsDTO> resultDtos = exportList(resultDos);
		return resultDtos;
	}

	@Override
	public TripDetailsDTO findById(TripDetailsDTO dto) throws ExecutionFault {
		if (ServicesUtil.isEmpty(dto.getTripId())) {
			throw new ExecutionFault("Trip id is required for searching");
		}
		String hql = "SELECT t FROM TripDetailsDo AS t  left outer join t.deliveryHeader tdh where "
				+ "lower(t.tripId) like lower(:tripId) ORDER BY tdh.deliveryOrder";
		Query query = getSession().createQuery(hql);
		query.setString("tripId", "%" + dto.getTripId() + "%");
		TripDetailsDo tripDetailsDo = (TripDetailsDo) query.setCacheable(true).uniqueResult();
		if (ServicesUtil.isEmpty(tripDetailsDo)) {
			throw new ExecutionFault("No trip is found for id " + dto.getTripId());
		}
		return exportDto(tripDetailsDo);
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
	public HashMap<String, Long> getDriverDashboardDetails(String userId) {
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
	public List<TripDetailsDTO> getTripHistoryByDriverId(String userId, Long start, Long end) {

		List<TripDetailsDo> tripDoList = null;
		Criteria criteria = getSession().createCriteria(TripDetailsDo.class);
		Criteria userCriteria = criteria.createCriteria("user");
		userCriteria.add(Restrictions.eq("userId", userId));
		criteria.addOrder(Order.desc("updatedAt"));

		if (!ServicesUtil.isEmpty(start) && !ServicesUtil.isEmpty(end)) {
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
	public Map<String, Long> getDriversDeliveryNoteReport(String userId) {

		ArrayList<String> deliveryStatus = new ArrayList<>();
		deliveryStatus.add("del_note_completed");
		deliveryStatus.add("del_note_partially_rejected");
		deliveryStatus.add("del_note_rejected");
		deliveryStatus.add("created");
		deliveryStatus.add("del_note_started");

		String hql = " SELECT new map("
				+ " (Select count(d.deliveryNoteId) from d where d.status in (:deliverystatus) and d.tripped = true and d.assignedUser = :driverId) as total_delivery_note, "
				+ " (Select count(d.deliveryNoteId) from d where d.status='del_note_completed' and d.tripped = true and d.assignedUser = :driverId) as del_note_completed, "
				+ " (Select count(d.deliveryNoteId) from d where d.status='del_note_partially_rejected' and d.tripped = true and d.assignedUser = :driverId) as del_note_partially_rejected, "
				+ " (Select count(d.deliveryNoteId) from d where d.status='del_note_rejected' and d.tripped = true and d.assignedUser = :driverId) as del_note_rejected, "
				+ " (Select count(d.deliveryNoteId) from d where d.status='created' and d.tripped = true and d.assignedUser = :driverId) as created, "
				+ " (Select count(d.deliveryNoteId) from d where d.status='del_note_started' and d.tripped = true  and d.assignedUser = :driverId) as del_note_started "
				+ ") from DeliveryHeaderDo d where d.assignedUser = :driverId and d.tripped = true group by d.assignedUser";
		Query query = getSession().createQuery(hql);
		query.setParameterList("deliverystatus", deliveryStatus);
		query.setParameter("driverId", userId);
		Map<String, Long> result = (Map<String, Long>) query.uniqueResult();

		return result;
	}

	@SuppressWarnings("unchecked")
	public List<TripDetailsDTO> getLatestOngoingTrip(String userId) throws Exception {

		//
		UserDetailsDTO userDto = new UserDetailsDTO();
		userDto.setUserId(userId);
		userDto = userDAO.findById(userDto);

		String roleName = userDto.getRole().getRoleName();
		String outsideDriver = RoleConstant.OUTSIDE_JAKARTA_DRIVER.getValue();
		List<String> dnList = new ArrayList<>();
		dnList.add(TripStatus.TRIP_STATUS_STARTED.getValue());
		dnList.add(TripStatus.TRIP_STATUS_DRIVER_ASSIGNED.getValue());

		/*
		 * if (!outsideDriver.equalsIgnoreCase(roleName))
		 * dnList.add(TripStatus.TRIP_STATUS_CANCELLED.getValue());
		 */

		String hql = "select distinct t from TripDetailsDo t " + "where t.user.userId= :userId "
				+ "and t.status in (:tripStatus) " + "order by t.updatedAt desc";
		Query query = getSession().createQuery(hql);
		query.setParameterList("tripStatus", dnList);
		query.setParameter("userId", userId);

		if (!outsideDriver.equalsIgnoreCase(roleName)) {
			query.setMaxResults(1);
		}

		List<TripDetailsDo> result = (List<TripDetailsDo>) query.list();
		return exportList(result);
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
		if (!ServicesUtil.isEmpty(dto.getFrom()) && !ServicesUtil.isEmpty(dto.getTo())) {
			java.sql.Date sqlDate = new java.sql.Date(dto.getFrom().getTime());
			String fromDate = sqlDate + " 00:00:00";
			sqlDate = new java.sql.Date(dto.getTo().getTime());
			String toDate = sqlDate + " 00:00:00";
			del_note_completed += "  AND dh.UPDATED_AT BETWEEN '" + fromDate + "' AND '" + toDate + "' ";
			del_note_partially_rejected += "  AND dh.UPDATED_AT BETWEEN '" + fromDate + "' AND '" + toDate + "' ";
			del_note_rejected += "  AND dh.UPDATED_AT BETWEEN '" + fromDate + "' AND '" + toDate + "' ";
		}
		/*
		 * if (!ServicesUtil.isEmpty(dto.getTo())) { del_note_completed +=
		 * "  AND dh.UPDATED_AT <= :toCompleted "; del_note_partially_rejected
		 * += "  AND dh.UPDATED_AT <= :toPartiallyRejected "; del_note_rejected
		 * += "  AND dh.UPDATED_AT >= :toRejected "; }
		 */

		String sql = " SELECT user_id, email, name, user_type, del_note_completed, del_note_partially_rejected, del_note_rejected, del_note_completed + del_note_partially_rejected+ del_note_rejected AS total_delivery_note "
				+ " FROM " + "(SELECT *," + " (" + del_note_completed + ") AS del_note_completed, " + "" + " ("
				+ del_note_rejected + ") AS del_note_rejected, " + "	(" + del_note_partially_rejected
				+ ") AS del_note_partially_rejected " + "  FROM user_details " + " ) ORDER BY " + orderBy + " DESC";

		/*
		 * if (!orderBy.equals(DeliveryNoteStatus.TOTAL_DEL_NOTE.getValue()))
		 * sql += "  ORDER BY " + dto.getSortBy() + " DESC	";
		 */

		SQLQuery query = getSession().createSQLQuery(sql);
		// apply condition for created at if from and to are given
		/*
		 * if (!ServicesUtil.isEmpty(dto.getFrom())) {
		 * query.setParameter("fromCompleted", dto.getFrom());
		 * query.setParameter("fromPartiallyRejected", dto.getFrom());
		 * query.setParameter("fromRejected", dto.getFrom()); } // apply
		 * condition for created at if from and to are given if
		 * (!ServicesUtil.isEmpty(dto.getTo())) {
		 * query.setParameter("toCompleted", dto.getTo());
		 * query.setParameter("toPartiallyRejected", dto.getTo());
		 * query.setParameter("toRejected", dto.getTo()); }
		 */
		// query.setParameter("fromDate",dto.getFrom(),TemporalType.DATE);
		query.setFirstResult(dto.getFirstResult());
		query.setMaxResults(dto.getMaxResult());
		List<List<Object>> result = (List<List<Object>>) query.setResultTransformer(Transformers.TO_LIST).list();
		Long totalDns = 0L;
		String userId, name, userType, email;
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
				|| TripStatus.TRIP_STATUS_CANCELLED.getValue().equals(status)) {
			return true;
		} else {
			throw new InvalidInputFault("trip status '" + status + "' is invalid status code");
		}
	}

	/**
	 * get all trips as per logged in admin or super_admin
	 * 
	 * @param userId
	 * @param roleName
	 * @param wareHouseDetails
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ResponseDto getAllTripsAssociatedWithAdminsDrivers(Map<String, Object> paramMap) {

		UserDetailsDTO adminDto = paramMap.containsKey("adminDto") ? (UserDetailsDTO) paramMap.get("adminDto") : null;
		String dnStatus = paramMap.containsKey("dnStatus") ? (String) paramMap.get("dnStatus") : "";
		List<String> warehouseList = paramMap.containsKey("warehouseList")
				? (List<String>) paramMap.get("warehouseList") : null;
		Date from = paramMap.containsKey("from") ? (Date) paramMap.get("from") : null;
		Date to = paramMap.containsKey("to") ? (Date) paramMap.get("to") : null;

		String userId = adminDto.getUserId();
		String roleName = adminDto.getRole().getRoleName();
		Set<WareHouseDetailsDTO> wareHouseDetails = adminDto.getWareHouseDetails();

		boolean isSuperAdmin = false;
		ResponseDto res = new ResponseDto();
		StringBuffer hql = new StringBuffer("");
		StringBuffer countHql = new StringBuffer("");
		Long totalCount = 0L;
		// get all the user list if role is super_admin or sales_admin
		if (roleName.equals(RoleConstant.SUPER_ADMIN.getValue())
				|| roleName.equals(RoleConstant.SALES_ADMIN.getValue())) {
			hql.append(
					"SELECT distinct t FROM TripDetailsDo AS t join t.deliveryHeader dh  join dh.wareHouseDetails w  where (t.status != :status or t.status is null) ");
			countHql.append(
					"SELECT count(distinct t.tripId) FROM TripDetailsDo AS t join t.deliveryHeader dh  join dh.wareHouseDetails w  where (t.status != :status or t.status is null) ");
			isSuperAdmin = true;
		} else if (roleName.equals(RoleConstant.ADMIN_INSIDE_JAKARTA.getValue())
				|| roleName.equals(RoleConstant.ADMIN_OUTSIDE_JAKARTA.getValue())) {
			hql.append(
					" SELECT distinct t FROM TripDetailsDo AS t join t.deliveryHeader dh  join dh.wareHouseDetails w  where t.createdBy = :createdBy AND (t.status != :status or t.status is null) ");
			countHql.append(
					" SELECT count(distinct t.tripId) FROM TripDetailsDo AS t join t.deliveryHeader dh  join dh.wareHouseDetails w  where t.createdBy = :createdBy AND (t.status != :status or t.status is null) ");
		} else if (roleName.equals(RoleConstant.COURIER_ADMIN.getValue())) {
			hql.append(
					" SELECT distinct t FROM TripDetailsDo AS t join t.deliveryHeader dh  join dh.wareHouseDetails w  where t.user.createdBy = :createdBy AND (t.status != :status or t.status is null) ");
			countHql.append(
					" SELECT count(distinct t.tripId) FROM TripDetailsDo AS t join t.deliveryHeader dh  join dh.wareHouseDetails w  where t.user.createdBy = :createdBy AND (t.status != :status or t.status is null) ");
		}

		Query query = null;
		Query query2 = null;
		if (!ServicesUtil.isEmpty(dnStatus)) {
			hql.append(" AND dh.status ='" + dnStatus + "' ");
			countHql.append(" AND dh.status ='" + dnStatus + "' ");
		}

		if (!ServicesUtil.isEmpty(warehouseList)) {
			hql.append(" AND w.wareHouseId in (:warehouseList) ");
			countHql.append(" AND w.wareHouseId in (:warehouseList) ");
		}

		if (!ServicesUtil.isEmpty(from) && !ServicesUtil.isEmpty(to)) {
			hql.append(
					" AND ( (t.createdAt between :from and :to) OR (t.startTime between :from and :to) OR (t.endTime between :from and :to)) ");
			countHql.append(
					" AND ( (t.createdAt between :from and :to) OR (t.startTime between :from and :to) OR (t.endTime between :from and :to)) ");
		}

		hql.append(" ORDER BY t.tripId desc");
		query = getSession().createQuery(hql.toString());
		query2 = getSession().createQuery(countHql.toString());
		if (!isSuperAdmin) {
			/*
			 * if (ServicesUtil.isEmpty(wareHouseIds)) return new
			 * ArrayList<TripDetailsDo>();
			 * 
			 * query.setParameterList("warehouselist", wareHouseIds);
			 */
			query.setParameter("createdBy", userId);
			query2.setParameter("createdBy", userId);
			// [1051, 1101, 11S1]
		}

		if (!ServicesUtil.isEmpty(warehouseList)) {
			query.setParameterList("warehouseList", warehouseList);
			query2.setParameterList("warehouseList", warehouseList);
		}
		if (!ServicesUtil.isEmpty(from) && !ServicesUtil.isEmpty(to)) {
			query.setParameter("from", from);
			query.setParameter("to", to);

			query2.setParameter("from", from);
			query2.setParameter("to", to);
		}

		query.setParameter("status", TripStatus.TRIP_STATUS_CANCELLED.getValue());
		query2.setParameter("status", TripStatus.TRIP_STATUS_CANCELLED.getValue());
		query.setFirstResult(PaginationUtil.FIRST_RESULT);
		query.setMaxResults(PaginationUtil.MAX_RESULT);

		
			totalCount = (Long) query2.setCacheable(true).uniqueResult();
		
		ArrayList<TripDetailsDo> result = (ArrayList<TripDetailsDo>) query.setCacheable(true).list();
		res.setTotalCount(totalCount);
		res.setData(exportList(result));
		return res;
	}

	/***
	 * admin dashboard service with associated warehouse only
	 * 
	 * @param userId
	 * @param roleName
	 * @param wareHouseDetails
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Long> getAdminDashboardAssociatedWithAdmins(Map<String, Object> paramMap) {

		
		UserDetailsDTO adminDto = paramMap.containsKey("adminDto") ? (UserDetailsDTO) paramMap.get("adminDto") : null;
		List<String> warehouseList = paramMap.containsKey("warehouseList")
				? (List<String>) paramMap.get("warehouseList") : null;
		Date from = paramMap.containsKey("from") ? (Date) paramMap.get("from") : null;
		Date to = paramMap.containsKey("to") ? (Date) paramMap.get("to") : null;
		
		String userId =adminDto.getUserId();
		String roleName = adminDto.getRole().getRoleName();
		Set<WareHouseDetailsDTO> wareHouseDetails = adminDto.getWareHouseDetails();
		
		ArrayList<String> deliveryNoteStatusList = new ArrayList<String>();
		deliveryNoteStatusList.add("del_note_rejected");
		deliveryNoteStatusList.add("del_note_partially_rejected");
		deliveryNoteStatusList.add("del_note_started");
		deliveryNoteStatusList.add("created"); // as del_note_validated
		deliveryNoteStatusList.add("del_note_completed");

		

		boolean isSuperAdmin = false;
		String hql = "";
		Query query;
		Map<String, Long> dashBoardCountMap = new HashMap<>();
		Map<String, Long> tempMap = null;
		// get all the user list if role is super_admin or sales_admin
		if (roleName.equals(RoleConstant.SUPER_ADMIN.getValue())
				|| roleName.equals(RoleConstant.SALES_ADMIN.getValue())) {

			// total trips
			hql = "SELECT new map(count(distinct t.tripId) as TOTAL_TRIPS)  FROM TripDetailsDo AS t join t.deliveryHeader dh join dh.wareHouseDetails w where (t.status != :status)";
			
			if(!ServicesUtil.isEmpty(from)&& !ServicesUtil.isEmpty(to)){
				hql += " AND ( (t.createdAt between :from and :to) OR (t.startTime between :from and :to) OR (t.endTime between :from and :to)) ";
			}
			if (!ServicesUtil.isEmpty(warehouseList))
				hql+=" AND w.wareHouseId in (:warehouseList) ";
			
			query = getSession().createQuery(hql);
			if(!ServicesUtil.isEmpty(from)&& !ServicesUtil.isEmpty(to)){
				query.setParameter("from", from);
				query.setParameter("to", to);
			}
			if (!ServicesUtil.isEmpty(warehouseList))
				query.setParameterList("warehouseList", warehouseList);
			
			query.setParameter("status", TripStatus.TRIP_STATUS_CANCELLED.getValue());
			dashBoardCountMap = (Map<String, Long>) query.uniqueResult();

			// total enroute trips
			hql = "SELECT new map(count(distinct t.tripId) as TOTAL_ENROUTE_TRIPS)  FROM TripDetailsDo AS t join t.deliveryHeader dh join dh.wareHouseDetails w where t.status = :status";
			
			if(!ServicesUtil.isEmpty(from)&& !ServicesUtil.isEmpty(to)){
				hql += " AND ( (t.createdAt between :from and :to) OR (t.startTime between :from and :to) OR (t.endTime between :from and :to)) ";
			}
			if (!ServicesUtil.isEmpty(warehouseList))
				hql+=" AND w.wareHouseId in (:warehouseList) ";
			
			
			query = getSession().createQuery(hql);
			if(!ServicesUtil.isEmpty(from)&& !ServicesUtil.isEmpty(to)){
				query.setParameter("from", from);
				query.setParameter("to", to);
			}
			if (!ServicesUtil.isEmpty(warehouseList))
				query.setParameterList("warehouseList", warehouseList);
			
			query.setParameter("status", TripStatus.TRIP_STATUS_STARTED.getValue());
			tempMap = (Map<String, Long>) query.uniqueResult();
			if (tempMap.containsKey("TOTAL_ENROUTE_TRIPS"))
				dashBoardCountMap.put("TOTAL_ENROUTE_TRIPS", tempMap.get("TOTAL_ENROUTE_TRIPS"));

			hql = "SELECT tdh.status,count(distinct tdh.deliveryNoteId) FROM TripDetailsDo AS t join t.deliveryHeader "
					+ "tdh join tdh.wareHouseDetails w  WHERE tdh.tripped = true AND tdh.status IN(:deliveryNoteStatusList) ";
			if(!ServicesUtil.isEmpty(from)&& !ServicesUtil.isEmpty(to)){
				hql += " AND ( (t.createdAt between :from and :to) OR (t.startTime between :from and :to) OR (t.endTime between :from and :to)) ";
			}
			if (!ServicesUtil.isEmpty(warehouseList))
				hql+=" AND w.wareHouseId in (:warehouseList) ";
			
			hql += " group by tdh.status ";
			query = getSession().createQuery(hql);
			if(!ServicesUtil.isEmpty(from)&& !ServicesUtil.isEmpty(to)){
				query.setParameter("from", from);
				query.setParameter("to", to);
			}
			if (!ServicesUtil.isEmpty(warehouseList))
				query.setParameterList("warehouseList", warehouseList);
			

			query.setParameterList("deliveryNoteStatusList", deliveryNoteStatusList);
			List<Object[]> noteCountResultList = query.list();
			Long totalOrders = 0l;
			for (Object[] noteCountResult : noteCountResultList) {
				dashBoardCountMap.put((String) noteCountResult[0], (Long) noteCountResult[1]);
				totalOrders += (Long) noteCountResult[1];
			}
			if (!ServicesUtil.isEmpty(totalOrders)) {
				dashBoardCountMap.put("TOTAL_ORDERS", totalOrders);
				Long avgOrders = totalOrders / dashBoardCountMap.get("TOTAL_TRIPS");
				dashBoardCountMap.put("AVG_TRIP_ORDER", avgOrders);
			}

		} else if (roleName.equals(RoleConstant.ADMIN_INSIDE_JAKARTA.getValue())
				|| roleName.equals(RoleConstant.ADMIN_OUTSIDE_JAKARTA.getValue())) {

			// total trips
			hql = "SELECT new map(count(distinct t.tripId) as TOTAL_TRIPS)  FROM TripDetailsDo AS t join t.deliveryHeader dh join dh.wareHouseDetails w where t.createdBy = :userId AND (t.status != :status)";
			
			if(!ServicesUtil.isEmpty(from)&& !ServicesUtil.isEmpty(to)){
				hql += " AND ( (t.createdAt between :from and :to) OR (t.startTime between :from and :to) OR (t.endTime between :from and :to)) ";
			}
			if (!ServicesUtil.isEmpty(warehouseList))
				hql+=" AND w.wareHouseId in (:warehouseList) ";
			
			query = getSession().createQuery(hql);
			if(!ServicesUtil.isEmpty(from)&& !ServicesUtil.isEmpty(to)){
				query.setParameter("from", from);
				query.setParameter("to", to);
			}
			if (!ServicesUtil.isEmpty(warehouseList))
				query.setParameterList("warehouseList", warehouseList);
			
			
			query.setParameter("userId", userId);
			query.setParameter("status", TripStatus.TRIP_STATUS_CANCELLED.getValue());
			dashBoardCountMap = (Map<String, Long>) query.uniqueResult();

			// total enroute trips
			hql = "SELECT new map(count(distinct t.tripId) as TOTAL_ENROUTE_TRIPS)  FROM TripDetailsDo AS t join t.deliveryHeader dh join dh.wareHouseDetails w where t.createdBy = :userId AND (t.status = :status)";
			
			if(!ServicesUtil.isEmpty(from)&& !ServicesUtil.isEmpty(to)){
				hql += " AND ( (t.createdAt between :from and :to) OR (t.startTime between :from and :to) OR (t.endTime between :from and :to)) ";
			}
			if (!ServicesUtil.isEmpty(warehouseList))
				hql+=" AND w.wareHouseId in (:warehouseList) ";
			
			query = getSession().createQuery(hql);
			if(!ServicesUtil.isEmpty(from)&& !ServicesUtil.isEmpty(to)){
				query.setParameter("from", from);
				query.setParameter("to", to);
			}
			if (!ServicesUtil.isEmpty(warehouseList))
				query.setParameterList("warehouseList", warehouseList);
			
			query.setParameter("userId", userId);
			query.setParameter("status", TripStatus.TRIP_STATUS_STARTED.getValue());
			tempMap = (Map<String, Long>) query.uniqueResult();
			if (tempMap.containsKey("TOTAL_ENROUTE_TRIPS"))
				dashBoardCountMap.put("TOTAL_ENROUTE_TRIPS", tempMap.get("TOTAL_ENROUTE_TRIPS"));

			hql = "SELECT tdh.status,count(distinct tdh.deliveryNoteId) FROM TripDetailsDo AS t join t.deliveryHeader "
					+ "tdh join tdh.wareHouseDetails w WHERE tdh.tripped = true AND t.createdBy = :userId AND tdh.status IN(:deliveryNoteStatusList) ";
			
			if(!ServicesUtil.isEmpty(from)&& !ServicesUtil.isEmpty(to)){
				hql += " AND ( (t.createdAt between :from and :to) OR (t.startTime between :from and :to) OR (t.endTime between :from and :to)) ";
			}
			if (!ServicesUtil.isEmpty(warehouseList))
				hql+=" AND w.wareHouseId in (:warehouseList) ";
			
			hql+= "  group by tdh.status ";
			query = getSession().createQuery(hql);
			if(!ServicesUtil.isEmpty(from)&& !ServicesUtil.isEmpty(to)){
				query.setParameter("from", from);
				query.setParameter("to", to);
			}
			if (!ServicesUtil.isEmpty(warehouseList))
				query.setParameterList("warehouseList", warehouseList);
			
			query.setParameter("userId", userId);
			query.setParameterList("deliveryNoteStatusList", deliveryNoteStatusList);
			List<Object[]> noteCountResultList = query.list();
			Long totalOrders = 0l;
			for (Object[] noteCountResult : noteCountResultList) {
				dashBoardCountMap.put((String) noteCountResult[0], (Long) noteCountResult[1]);
				totalOrders += (Long) noteCountResult[1];
			}
			if (!ServicesUtil.isEmpty(totalOrders)) {
				dashBoardCountMap.put("TOTAL_ORDERS", totalOrders);
				Long avgOrders = totalOrders / dashBoardCountMap.get("TOTAL_TRIPS");
				dashBoardCountMap.put("AVG_TRIP_ORDER", avgOrders);
			}
		} else if (roleName.equals(RoleConstant.COURIER_ADMIN.getValue())) {

			// total trips
			hql = "SELECT new map(count(distinct t.tripId) as TOTAL_TRIPS)  FROM TripDetailsDo AS t t.deliveryHeader dh join dh.wareHouseDetails w  where t.user.createdBy = :userId AND (t.status != :status)";
			query = getSession().createQuery(hql);
			query.setParameter("userId", userId);
			query.setParameter("status", TripStatus.TRIP_STATUS_CANCELLED.getValue());
			dashBoardCountMap = (Map<String, Long>) query.uniqueResult();

			// total enroute trips
			hql = "SELECT new map(count(distinct t.tripId) as TOTAL_ENROUTE_TRIPS)  FROM TripDetailsDo AS t t.deliveryHeader dh join dh.wareHouseDetails w  where t.user.createdBy = :userId AND (t.status = :status )";
			
			if(!ServicesUtil.isEmpty(from)&& !ServicesUtil.isEmpty(to)){
				hql += " AND ( (t.createdAt between :from and :to) OR (t.startTime between :from and :to) OR (t.endTime between :from and :to)) ";
			}
			if (!ServicesUtil.isEmpty(warehouseList))
				hql+=" AND w.wareHouseId in (:warehouseList) ";
			
			query = getSession().createQuery(hql);
			
			if(!ServicesUtil.isEmpty(from)&& !ServicesUtil.isEmpty(to)){
				hql += " AND ( (t.createdAt between :from and :to) OR (t.startTime between :from and :to) OR (t.endTime between :from and :to)) ";
			}
			if (!ServicesUtil.isEmpty(warehouseList))
				hql+=" AND w.wareHouseId in (:warehouseList) ";
			
			query.setParameter("userId", userId);
			query.setParameter("status", TripStatus.TRIP_STATUS_STARTED.getValue());
			tempMap = (Map<String, Long>) query.uniqueResult();
			if (tempMap.containsKey("TOTAL_ENROUTE_TRIPS"))
				dashBoardCountMap.put("TOTAL_ENROUTE_TRIPS", tempMap.get("TOTAL_ENROUTE_TRIPS"));

			hql = "SELECT tdh.status,count(distinct tdh.deliveryNoteId) FROM TripDetailsDo AS t join t.deliveryHeader "
					+ "tdh join tdh.wareHouseDetails w  WHERE tdh.tripped = true AND  t.user.createdBy = :userId AND tdh.status IN(:deliveryNoteStatusList) ";
			
			if(!ServicesUtil.isEmpty(from)&& !ServicesUtil.isEmpty(to)){
				hql += " AND ( (t.createdAt between :from and :to) OR (t.startTime between :from and :to) OR (t.endTime between :from and :to)) ";
			}
			if (!ServicesUtil.isEmpty(warehouseList))
				hql+=" AND w.wareHouseId in (:warehouseList) ";
			
			hql += " group by tdh.status ";
			query = getSession().createQuery(hql);
			
			if(!ServicesUtil.isEmpty(from)&& !ServicesUtil.isEmpty(to)){
				hql += " AND ( (t.createdAt between :from and :to) OR (t.startTime between :from and :to) OR (t.endTime between :from and :to)) ";
			}
			if (!ServicesUtil.isEmpty(warehouseList))
				hql+=" AND w.wareHouseId in (:warehouseList) ";
			
			query.setParameter("userId", userId);
			query.setParameterList("deliveryNoteStatusList", deliveryNoteStatusList);
			List<Object[]> noteCountResultList = query.list();
			Long totalOrders = 0l;
			for (Object[] noteCountResult : noteCountResultList) {
				dashBoardCountMap.put((String) noteCountResult[0], (Long) noteCountResult[1]);
				totalOrders += (Long) noteCountResult[1];
			}
			if (!ServicesUtil.isEmpty(totalOrders)) {
				dashBoardCountMap.put("TOTAL_ORDERS", totalOrders);
				Long avgOrders = totalOrders / dashBoardCountMap.get("TOTAL_TRIPS");
				dashBoardCountMap.put("AVG_TRIP_ORDER", avgOrders);
			}
		}

		if (!dashBoardCountMap.containsKey("AVG_TRIP_ORDER"))
			dashBoardCountMap.put("AVG_TRIP_ORDER", 0L);

		if (!dashBoardCountMap.containsKey("TOTAL_TRIPS"))
			dashBoardCountMap.put("TOTAL_TRIPS", 0L);

		if (!dashBoardCountMap.containsKey("TOTAL_ORDERS"))
			dashBoardCountMap.put("TOTAL_ORDERS", 0L);

		if (!dashBoardCountMap.containsKey("del_note_rejected"))
			dashBoardCountMap.put("del_note_rejected", 0L);

		if (!dashBoardCountMap.containsKey("del_note_started"))
			dashBoardCountMap.put("del_note_started", 0L);

		if (!dashBoardCountMap.containsKey("del_note_partially_rejected"))
			dashBoardCountMap.put("del_note_partially_rejected", 0L);

		if (!dashBoardCountMap.containsKey("created"))
			dashBoardCountMap.put("created", 0L);

		if (!dashBoardCountMap.containsKey("del_note_completed"))
			dashBoardCountMap.put("del_note_completed", 0L);

		return dashBoardCountMap;
	}

	/**
	 * filter api chages as per logged in admin or super_admin
	 */
	@SuppressWarnings("unchecked")
	public List<TripDetailsDTO> getFilteredTripsAssociatedWithAdmins(FilterDTO dto, Map<String, Object> paramMap) {

		UserDetailsDTO adminDto = paramMap.containsKey("adminDto") ? (UserDetailsDTO) paramMap.get("adminDto") : null;
		List<String> warehouseList = paramMap.containsKey("warehouseList")
				? (List<String>) paramMap.get("warehouseList") : null;
		Date from = paramMap.containsKey("from") ? (Date) paramMap.get("from") : null;
		Date to = paramMap.containsKey("to") ? (Date) paramMap.get("to") : null;

		String userId = adminDto.getUserId();
		String roleName = adminDto.getRole().getRoleName();
		Set<WareHouseDetailsDTO> wareHouseDetails = adminDto.getWareHouseDetails();

		List<String> wareHouseIds = new ArrayList<String>();
		for (WareHouseDetailsDTO wareHouse : wareHouseDetails)
			wareHouseIds.add(wareHouse.getWareHouseId());
		StringBuffer hql = new StringBuffer("");
		Boolean isSuperAdmin = false;
		if (roleName.equals(RoleConstant.SUPER_ADMIN.getValue())
				|| roleName.equals(RoleConstant.SALES_ADMIN.getValue())) {
			hql.append(
					"SELECT distinct t FROM TripDetailsDo AS t join t.deliveryHeader tdh join tdh.wareHouseDetails w where (t.status != :status or t.status is null)");
			isSuperAdmin = true;
		}

		else if (roleName.equals(RoleConstant.ADMIN_INSIDE_JAKARTA.getValue())
				|| roleName.equals(RoleConstant.ADMIN_OUTSIDE_JAKARTA.getValue())) {
			hql.append(
					" SELECT distinct t FROM TripDetailsDo AS t  join t.deliveryHeader tdh join tdh.wareHouseDetails w "
							+ " where t.createdBy = :createdBy AND (t.status != :status or t.status is null)");
		} else if (roleName.equals(RoleConstant.COURIER_ADMIN.getValue())) {
			hql.append(
					" SELECT distinct t FROM TripDetailsDo AS t join t.deliveryHeader tdh join tdh.wareHouseDetails w "
							+ "where t.user.createdBy = :createdBy AND (t.status != :status or t.status is null)");
		}

		String filterBy = dto.getFilterBy();
		String q = dto.getQuery();
		boolean isStatus = false;
		List<TripDetailsDo> data = null;
		String status = "";
		if (!ServicesUtil.isEmpty(dto.getStatus())) {
			status = dto.getStatus();
			isStatus = true;
		}

		if (!ServicesUtil.isEmpty(dto)) {

			if (filterBy.equalsIgnoreCase("trip")) {
				hql.append(" AND lower(t.tripId) like lower(:searchParam)");
			} else if (filterBy.equalsIgnoreCase("driver")) {
				hql.append(// " inner join u.role as r where u.userId is not
							// null "
						" AND ( lower(t.user.userId) like lower(:searchParam) Or lower(t.user.firstName) "
								+ "like lower(:searchParam) Or"
								+ " lower(t.user.lastName) like lower(:searchParam) ) ");
			} else if (filterBy.equalsIgnoreCase("delivery_note")) {
				hql.append(" AND  str(tdh.deliveryNoteId) like :searchParam ");
			} else if (filterBy.equalsIgnoreCase("airwayBillNo")) {
				hql.append(" AND  tdh.airwayBillNo like :searchParam ");
			}

			if (isStatus) {
				hql.append(" AND t.status = :tripStatus ");
			}

			if (!ServicesUtil.isEmpty(warehouseList))
				hql.append(" AND w.wareHouseId in (:warehouseList) ");

			if (!ServicesUtil.isEmpty(from) && !ServicesUtil.isEmpty(to))
				hql.append(
						" AND ( (t.createdAt between :from and :to) OR (t.startTime between :from and :to) OR (t.endTime between :from and :to)) ");


			hql.append("order by t.tripId desc");

			Query query = getSession().createQuery(hql.toString());

			if (isStatus)
				query.setParameter("tripStatus", status);

			if (!isSuperAdmin) {
				query.setParameter("createdBy", userId);
			}
			if (!ServicesUtil.isEmpty(warehouseList)) {
				query.setParameterList("warehouseList", warehouseList);
			}
			if (!ServicesUtil.isEmpty(from) && !ServicesUtil.isEmpty(to)) {
				query.setParameter("from", from);
				query.setParameter("to", to);
			}
			
			query.setMaxResults(10);
			query.setParameter("searchParam", "%" + q.toLowerCase() + "%");
			query.setParameter("status", TripStatus.TRIP_STATUS_CANCELLED.getValue());
			data = (List<TripDetailsDo>) query.setCacheable(true).list();
		}
		return exportList(data);
	}

	/**
	 * get the leader board drivers report as per logged in admin or super_admin
	 * 
	 * @param userId
	 * @param roleName
	 * @param wareHouseDetails
	 * @return
	 * @throws InvalidInputFault
	 */
	public Object getLeaderboardAssociatedWithAdminsWarehouse(WebLeaderBoardVO dto, String userId, String roleName,
			Set<WareHouseDetailsDTO> wareHouseDetails) throws InvalidInputFault {

		if (ServicesUtil.isEmpty(dto.getFrom()) || ServicesUtil.isEmpty(dto.getTo()))
			throw new InvalidInputFault("Invalid time range.");

		List<String> wareHouseIds = new ArrayList<String>();
		for (WareHouseDetailsDTO wareHouse : wareHouseDetails)
			wareHouseIds.add(wareHouse.getWareHouseId());

		List<String> driverList = null;
		List<String> driverRole = new ArrayList<>();
		driverRole.add(RoleConstant.INSIDE_JAKARTA_DRIVER.getValue());
		driverRole.add(RoleConstant.OUTSIDE_JAKARTA_DRIVER.getValue());
		boolean isSuperAdmin = false;
		String andUserCreatedBy = "";
		if (roleName.equals(RoleConstant.SUPER_ADMIN.getValue())
				|| roleName.equals(RoleConstant.SALES_ADMIN.getValue())) {
			andUserCreatedBy = "";
			isSuperAdmin = true;
		} else {
			driverList = userDAO.getDriversAssociatedWithAdmin(userId, roleName);
			andUserCreatedBy = " AND u.userId IN (:driverList) ";
		}

		ArrayList<String> totalStatus = new ArrayList<>();
		totalStatus.add("del_note_completed");
		totalStatus.add("del_note_partially_rejected");
		totalStatus.add("del_note_rejected");

		String sDateAndeDate = " AND dh.updatedAt BETWEEN :stDate AND :edDate ";

		String hql = " select new map(u.userId as userId, u.email as email, u.firstName as firstName, u.lastName as lastName,"
				+ " (select Count(dh.deliveryNoteId) from DeliveryHeaderDo as dh WHERE dh.assignedUser = u.userId AND dh.status IN (:totalstatus) "
				+ sDateAndeDate + ") as total_delivery_note, "
				+ " (select Count(dh.status) from DeliveryHeaderDo as dh WHERE dh.assignedUser = u.userId AND dh.status ='del_note_completed' "
				+ sDateAndeDate + ") as del_note_completed, "
				+ " (select Count(dh.status) from DeliveryHeaderDo as dh WHERE dh.assignedUser = u.userId AND dh.status ='del_note_partially_rejected' "
				+ sDateAndeDate + ") as del_note_partially_rejected, "
				+ " (select Count(dh.status) from DeliveryHeaderDo as dh WHERE dh.assignedUser = u.userId AND dh.status ='del_note_rejected' "
				+ sDateAndeDate + ") as del_note_rejected " + " ) "
				+ " from TripDetailsDo as t  inner join t.deliveryHeader as dh inner join t.user as u  inner join u.role as r "
				+ " where r.roleName IN (:driver) " + andUserCreatedBy + " ";

		/*
		 * if (!ServicesUtil.isEmpty(dto.getFrom()) &&
		 * !ServicesUtil.isEmpty(dto.getTo())) { hql += sDateAndeDate; }
		 */

		hql += " GROUP BY u.userId, u.email, u.firstName, u.lastName" + " ORDER BY " + dto.getSortBy() + " desc ";

		Query query = getSession().createQuery(hql);
		query.setFirstResult(PaginationUtil.FIRST_RESULT);
		query.setMaxResults(PaginationUtil.MAX_RESULT);

		query.setParameterList("driver", driverRole);
		query.setParameterList("totalstatus", totalStatus);
		// setting time range
		query.setParameter("stDate", dto.getFrom());
		query.setParameter("edDate", dto.getTo());

		if (!isSuperAdmin) {
			query.setParameterList("driverList", driverList);
		}

		return query.setCacheable(true).list();
	}

	public UserDetailsDTO getDriverFromTripByDN(DeliveryHeaderDTO headerDto) {
		String hql = " SELECT t.user from TripDetailsDo t inner join t.deliveryHeader d where d.deliveryNoteId = :deliveryNoteId ";
		Query query = getSession().createQuery(hql);
		query.setParameter("deliveryNoteId", headerDto.getDeliveryNoteId());
		UserDetailsDo dos = (UserDetailsDo) query.uniqueResult();
		return userDAO.exportDto(dos);
	}

	public Map<String, Object> getTripDeliveryNotesCountsByDeliveryNoteId(Long deliveryNoteId) {
		String hql = "SELECT new map(t.tripId as tripId,count( elements(t.deliveryHeader)) as deliveryNoteCount, t.user as user) from TripDetailsDo t  join t.deliveryHeader d where d.deliveryNoteId = :deliveryNoteId "
				+ "   group by t.user,t.tripId";
		Query query = getSession().createQuery(hql);
		query.setParameter("deliveryNoteId", deliveryNoteId);
		Object result = query.uniqueResult();
		getSession().flush();
		getSession().clear();
		Map<String, Object> resultSet = new HashMap<>();
		Long dncount = -1L;
		TripDetailsDTO tripDto = null;
		if (!ServicesUtil.isEmpty(result)) {
			Map<String, Object> map = (Map<String, Object>) result;

			if (!ServicesUtil.isEmpty(map) && map.containsKey("tripId")) {
				String tripId = (String) map.get("tripId");
				tripDto = new TripDetailsDTO();
				tripDto.setTripId(tripId);
				UserDetailsDTO userDto = null;
				if (map.containsKey("user") && !ServicesUtil.isEmpty(map.get("user"))) {
					UserDetailsDo userDo = (UserDetailsDo) map.get("user");
					if (!ServicesUtil.isEmpty(userDo))
						userDto = userDAO.exportDto(userDo);

				}
				tripDto.setUser(userDto);
				dncount = (Long) map.get("deliveryNoteCount");

			}
		}
		resultSet.put("tripDto", tripDto);
		resultSet.put("deliveryNoteCount", dncount);

		return resultSet;
	}

	public int cancelTripById(String tripId) {
		String hql = " UPDATE TripDetailsDo t set t.status = :tripStatus where t.tripId = :tripId ";
		Query query = getSession().createQuery(hql);
		query.setParameter("tripStatus", TripStatus.TRIP_STATUS_CANCELLED.getValue());
		query.setParameter("tripId", tripId);
		int result = query.executeUpdate();
		return result;

	}

	public ResponseDto findTripByParamAssociatedWithAdmin(TripDetailsDTO dto, Map<String, Object> paramMap) {

		UserDetailsDTO adminDto = paramMap.containsKey("adminDto") ? (UserDetailsDTO) paramMap.get("adminDto") : null;
		String dnStatus = paramMap.containsKey("dnStatus") ? (String) paramMap.get("dnStatus") : "";
		List<String> warehouseList = paramMap.containsKey("warehouseList")
				? (List<String>) paramMap.get("warehouseList") : null;
		Date from = paramMap.containsKey("from") ? (Date) paramMap.get("from") : null;
		Date to = paramMap.containsKey("to") ? (Date) paramMap.get("to") : null;

		boolean isSuperAdmin = false;
		StringBuffer hql = new StringBuffer("");

		String roleName = adminDto.getRole().getRoleName();
		String userId = adminDto.getUserId();
		// get all the user list if role is super_admin or sales_admin
		StringBuffer countHql = new StringBuffer("");
		if (roleName.equals(RoleConstant.SUPER_ADMIN.getValue())
				|| roleName.equals(RoleConstant.SALES_ADMIN.getValue())) {
			hql.append(
					"SELECT distinct t FROM TripDetailsDo AS t join t.deliveryHeader dh join dh.wareHouseDetails w where t.status = :status ");
			countHql.append(
					"SELECT count(distinct t.tripId) FROM TripDetailsDo AS t join t.deliveryHeader dh join dh.wareHouseDetails w where t.status = :status ");
			isSuperAdmin = true;
		} else if (roleName.equals(RoleConstant.ADMIN_INSIDE_JAKARTA.getValue())
				|| roleName.equals(RoleConstant.ADMIN_OUTSIDE_JAKARTA.getValue())) {
			hql.append(
					" SELECT distinct t FROM TripDetailsDo AS t join t.deliveryHeader dh  join dh.wareHouseDetails w where t.createdBy = :createdBy AND (t.status = :status) ");
			countHql.append(
					" SELECT count(distinct t.tripId) FROM TripDetailsDo AS t join t.deliveryHeader dh  join dh.wareHouseDetails w where t.createdBy = :createdBy AND (t.status = :status) ");
		} else if (roleName.equals(RoleConstant.COURIER_ADMIN.getValue())) {
			hql.append(
					" SELECT distinct t FROM TripDetailsDo AS t join t.deliveryHeader dh  join dh.wareHouseDetails w  where t.user.createdBy = :createdBy AND (t.status = :status) ");
			countHql.append(
					" SELECT count(distinct t.tripId) FROM TripDetailsDo AS t join t.deliveryHeader dh  join dh.wareHouseDetails w where t.user.createdBy = :createdBy AND (t.status = :status) ");
		}

		if (!ServicesUtil.isEmpty(dnStatus)) {
			hql.append(" AND dh.status = :dnStatus ");
			countHql.append(" AND dh.status = :dnStatus ");
		}

		if (!ServicesUtil.isEmpty(warehouseList)) {
			hql.append(" AND w.wareHouseId in (:warehouseList) ");
			countHql.append(" AND w.wareHouseId in (:warehouseList) ");
		}

		if (!ServicesUtil.isEmpty(from) && !ServicesUtil.isEmpty(to)) {
			hql.append(
					" AND ( (t.createdAt between :from and :to) OR (t.startTime between :from and :to) OR (t.endTime between :from and :to)) ");
			countHql.append(
					" AND ( (t.createdAt between :from and :to) OR (t.startTime between :from and :to) OR (t.endTime between :from and :to)) ");
		}

		hql.append(" ORDER BY t.tripId desc");
		Query query = getSession().createQuery(hql.toString());
		Query countQuery = getSession().createQuery(countHql.toString());
		if (!isSuperAdmin) {
			/*
			 * if (ServicesUtil.isEmpty(wareHouseIds)) return new
			 * ArrayList<TripDetailsDo>();
			 * 
			 * query.setParameterList("warehouselist", wareHouseIds);
			 */

			query.setParameter("createdBy", userId);
			countQuery.setParameter("createdBy", userId);
			// [1051, 1101, 11S1]
		}
		if (!ServicesUtil.isEmpty(warehouseList)) {
			query.setParameterList("warehouseList", warehouseList);
			countQuery.setParameterList("warehouseList", warehouseList);
		}
		if (!ServicesUtil.isEmpty(from) && !ServicesUtil.isEmpty(to)) {
			query.setParameter("from", from);
			query.setParameter("to", to);

			countQuery.setParameter("from", from);
			countQuery.setParameter("to", to);
		}

		Long totalCount = 0L;

		query.setParameter("status", dto.getStatus());
		countQuery.setParameter("status", dto.getStatus());
		query.setFirstResult(PaginationUtil.FIRST_RESULT);
		query.setMaxResults(PaginationUtil.MAX_RESULT);

		if (!ServicesUtil.isEmpty(dnStatus)) {
			query.setParameter("dnStatus", dnStatus);
			countQuery.setParameter("dnStatus", dnStatus);

			totalCount = (Long) countQuery.uniqueResult();
		}

		ArrayList<TripDetailsDo> result = (ArrayList<TripDetailsDo>) query.setCacheable(true).list();
		ResponseDto res = new ResponseDto();
		res.setData(exportList(result));
		res.setTotalCount(totalCount);
		return res;
	}
}
