package com.incture.metrodata.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.incture.metrodata.dao.ContainerRecordsDAO;
import com.incture.metrodata.entity.ContainerRecordsDo;

@Service("containerRecordService")
@Transactional
public class ContainerRecordService implements ContainerRecordsServiceLocal {

	@Autowired
	ContainerRecordsDAO containerRecordsDAO;
	
	@Override
	@Transactional
	public void create(ContainerRecordsDo dto) {
		containerRecordsDAO.getSession().persist(dto);
        containerRecordsDAO.getSession().flush();
        containerRecordsDAO.getSession().clear();
	}

}
