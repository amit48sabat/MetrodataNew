package com.incture.metrodata.dao;

import javax.transaction.Transactional;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.incture.metrodata.dto.ContainerRecordsDTO;
import com.incture.metrodata.entity.ContainerRecordsDo;
import com.incture.metrodata.exceptions.ExecutionFault;
import com.incture.metrodata.exceptions.InvalidInputFault;
import com.incture.metrodata.exceptions.NoResultFault;
import com.incture.metrodata.util.ServicesUtil;

@Transactional
@Repository
public class ContainerRecordsDAO extends BaseDao<ContainerRecordsDo, ContainerRecordsDTO> {

	@Override
	public ContainerRecordsDo importDto(ContainerRecordsDTO dto, ContainerRecordsDo dos)
			throws InvalidInputFault, ExecutionFault, NoResultFault, Exception {

		if (ServicesUtil.isEmpty(dos))
			dos = new ContainerRecordsDo();

		if (!ServicesUtil.isEmpty(dto)) {

			if (!ServicesUtil.isEmpty(dto.getId()))
				dos.setId(dto.getId());

			if (!ServicesUtil.isEmpty(dto.getPayload()))
				dos.setPayload(dto.getPayload());

			if (!ServicesUtil.isEmpty(dto.getCreatedAt()))
				dos.setCreatedAt(dto.getCreatedAt());

			if (!ServicesUtil.isEmpty(dto.getTotalItems()))
				dos.setTotalItems(dto.getTotalItems());

			if (!ServicesUtil.isEmpty(dto.getTotalDns()))
				dos.setTotalDns(dto.getTotalDns());

			if (!ServicesUtil.isEmpty(dto.getDeleted()))
				dos.setDeleted(dto.getDeleted());

		}

		return dos;
	}

	@Override
	public ContainerRecordsDTO exportDto(ContainerRecordsDo dos){
		ContainerRecordsDTO dto = new  ContainerRecordsDTO();
		
		if (!ServicesUtil.isEmpty(dos.getId()))
			dto.setId(dos.getId());

		if (!ServicesUtil.isEmpty(dos.getPayload()))
			dto.setPayload(dos.getPayload());

		if (!ServicesUtil.isEmpty(dos.getCreatedAt()))
			dto.setCreatedAt(dos.getCreatedAt());

		if (!ServicesUtil.isEmpty(dos.getTotalItems()))
			dto.setTotalItems(dos.getTotalItems());

		if (!ServicesUtil.isEmpty(dos.getTotalDns()))
			dto.setTotalDns(dos.getTotalDns());

		if (!ServicesUtil.isEmpty(dos.getDeleted()))
			dto.setDeleted(dos.getDeleted());

		return dto;
	}

	public void markPatloadAsDeleted(ContainerRecordsDo dos) {
		
	/*	Query query= getSession().createQuery("select id from ContainerRecordsDo  WHERE createdAt = :createdAt");
		query.setParameter("createdAt", dos.getCreatedAt());
		Long id = (Long) query.uniqueResult();*/
		
		String hql = "UPDATE ContainerRecordsDo SET deleted = 1, totalItems = :totalItems, totalDns = :totalDns "
				+ " WHERE id = :id";
		Query  query = getSession().createQuery(hql);
       query.setParameter("totalItems", dos.getTotalItems());
       query.setParameter("totalDns", dos.getTotalDns());
       query.setParameter("id", dos.getId());
       query.executeUpdate();
       
       getSession().flush();
       getSession().clear();
	}
}
