package com.incture.metrodata.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.incture.metrodata.dto.OrderTrackingDTO;
import com.incture.metrodata.entity.OrderTrackingDo;
import com.incture.metrodata.exceptions.ExecutionFault;
import com.incture.metrodata.exceptions.InvalidInputFault;
import com.incture.metrodata.exceptions.NoResultFault;
import com.incture.metrodata.util.ServicesUtil;

@Repository("orderTrackingDao")
public class OrderTrackingDAO extends BaseDao<OrderTrackingDo, OrderTrackingDTO>{

	@Override
	OrderTrackingDo importDto(OrderTrackingDTO dto,OrderTrackingDo dos) throws InvalidInputFault, ExecutionFault, NoResultFault {
		if (ServicesUtil.isEmpty(dos))
		dos = new OrderTrackingDo();
		if (!ServicesUtil.isEmpty(dto)) {
			if (!ServicesUtil.isEmpty(dto.getTripId())) {
				dos.setTripId(dto.getTripId());
			}
			if (!ServicesUtil.isEmpty(dto.getDeliveryNoteId())) {
				dos.setDeliveryNoteId(dto.getDeliveryNoteId());
			}
			if (!ServicesUtil.isEmpty(dto.getDriverId())) {
				dos.setDriverId(dto.getDriverId());
			}
			if (!ServicesUtil.isEmpty(dto.getLongitude())) {
				dos.setLongitude(dto.getLongitude());
			}
			if (!ServicesUtil.isEmpty(dto.getLatitude())) {
				dos.setLatitude(dto.getLatitude());
			}
			if (!ServicesUtil.isEmpty(dto.getCreatedAt())) {
				dos.setCreatedAt(dto.getCreatedAt());
			}
		}
		return dos;
	}

	
	@Override
	OrderTrackingDTO exportDto(OrderTrackingDo dos) {
		OrderTrackingDTO dto = new OrderTrackingDTO();
		if (!ServicesUtil.isEmpty(dos)) {
			if (!ServicesUtil.isEmpty(dos.getTripId())) {
				dto.setTripId(dos.getTripId());
			}
			if (!ServicesUtil.isEmpty(dos.getDeliveryNoteId())) {
				dto.setDeliveryNoteId(dos.getDeliveryNoteId());
			}
			if (!ServicesUtil.isEmpty(dos.getDriverId())) {
				dto.setDriverId(dos.getDriverId());
			}
			if (!ServicesUtil.isEmpty(dos.getLongitude())) {
				dto.setLongitude(dos.getLongitude());
			}
			if (!ServicesUtil.isEmpty(dos.getLatitude())) {
				dto.setLatitude(dos.getLatitude());
			}
			if (!ServicesUtil.isEmpty(dos.getCreatedAt())) {
				dto.setCreatedAt(dos.getCreatedAt());
			}
		}
		return dto;
	}
	
	@SuppressWarnings("unchecked")
	public List<OrderTrackingDTO> findByParam(OrderTrackingDTO dto){
		
		Criteria criteria = getSession().createCriteria(OrderTrackingDo.class);
		
		 if(!ServicesUtil.isEmpty(dto.getTripId()))
		    criteria.add(Restrictions.eq("tripId", dto.getTripId()));
		
		 if(!ServicesUtil.isEmpty(dto.getDeliveryNoteId()))
			criteria.add(Restrictions.eq("deliveryNoteId", dto.getDeliveryNoteId()));
		
		 if(!ServicesUtil.isEmpty(dto.getDriverId()))
			criteria.add(Restrictions.eq("driverId", dto.getDriverId()));
		
		// ADDING CRITERIA FOR CREATEDAT BETWEEN START AND END DATES
		if(!ServicesUtil.isEmpty(dto.getStartedAt()) && !ServicesUtil.isEmpty(dto.getEndedAt()))
		{
			criteria.add(Restrictions.ge("createdAt", dto.getStartedAt())); 
			criteria.add(Restrictions.le("createdAt", dto.getEndedAt()));
		}
		
		criteria.addOrder(Order.desc("createdAt"));
		
		List<OrderTrackingDo> resultDos = criteria.list();
		List<OrderTrackingDTO> resultDtos = exportList(resultDos);
		
		// if no tracking data found in the given timestamp giving the last data associate with the dn 
		if(ServicesUtil.isEmpty(resultDtos))
			resultDtos = getTheLastOrderTrackingDetails(dto);
		
		return resultDtos;
	}
	
	public  List<OrderTrackingDTO> getTheLastOrderTrackingDetails(OrderTrackingDTO dto){
		Criteria criteria = getSession().createCriteria(OrderTrackingDo.class);
		
		 if(!ServicesUtil.isEmpty(dto.getTripId()))
		    criteria.add(Restrictions.eq("tripId", dto.getTripId()));
		
		 if(!ServicesUtil.isEmpty(dto.getDeliveryNoteId()))
			criteria.add(Restrictions.eq("deliveryNoteId", dto.getDeliveryNoteId()));
		
		 if(!ServicesUtil.isEmpty(dto.getDriverId()))
			criteria.add(Restrictions.eq("driverId", dto.getDriverId()));
		
		criteria.addOrder(Order.desc("createdAt"));
		
		List<OrderTrackingDo> resultDos = criteria.setMaxResults(1).list();
		List<OrderTrackingDTO> resultDtos = exportList(resultDos);
		return resultDtos;
	}


	public int deleteTripTrackingData(String tripId) {
		String hql = "DELETE OrderTrackingDo as d where d.tripId = :tripId"; 
		Query  query = getSession().createQuery(hql);
		query.setParameter("tripId", tripId);
		int rowAffected = query.executeUpdate();
		return rowAffected;
	}
}
