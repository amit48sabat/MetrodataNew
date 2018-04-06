package com.incture.metrodata.service;

import java.util.Map;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.maps.GeoApiContext;
import com.incture.metrodata.constant.Message;
import com.incture.metrodata.dao.ContainerDAO;
import com.incture.metrodata.dao.ContainerToDeliveryHeaderDao;
import com.incture.metrodata.dao.DeliveryHeaderDAO;
import com.incture.metrodata.dto.ContainerDTO;
import com.incture.metrodata.dto.ContainerDetailsDTO;
import com.incture.metrodata.dto.ResponseDto;
import com.incture.metrodata.entity.ContainerDetailsDo;
import com.incture.metrodata.entity.DeliveryHeaderDo;
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

	@Override
	public ResponseDto create(ContainerDTO dto) {
		ResponseDto response = new ResponseDto();

		if (!ServicesUtil.isEmpty(dto) && !ServicesUtil.isEmpty(dto.getDELIVERY())) {
			try {
				for (ContainerDetailsDTO d : dto.getDELIVERY().getItem()) {
					containerDao.create(d, new ContainerDetailsDo());
				}
				Object data = createEntryInDeliveryHeader(dto);
				response.setStatus(true);
				response.setCode(HttpStatus.SC_OK);
				response.setData(data);
				response.setMessage(Message.SUCCESS + "");
			} catch (Exception e) {
				response.setStatus(false);
				response.setCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
				response.setData(dto);
				response.setMessage(Message.FAILED + " : " + e.toString());
				e.printStackTrace();
			}
		} else {
			response.setStatus(true);
			response.setMessage(Message.FAILED.getValue());
			response.setData(dto);
			response.setCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
		}

		return response;
	}

	@SuppressWarnings("unchecked")
	private Map<Long, DeliveryHeaderDo> createEntryInDeliveryHeader(ContainerDTO dto) throws Exception {
		Map<Long, DeliveryHeaderDo> headerMap = containerToHeaderDao.importDto(dto.getDELIVERY(), context);
		DeliveryHeaderDo dos = null;
		for (Map.Entry<Long, DeliveryHeaderDo> entry : headerMap.entrySet()) {
			dos = entry.getValue();
			headerDao.persist(dos);
		}

		return headerMap;

	}

	@Override
	public HciRestInvoker getHciRestInvoker() {
		// TODO Auto-generated method stub
		return hciRestInvoker;
	}

}
