package com.incture.metrodata.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.incture.metrodata.dto.CommentsDTO;
import com.incture.metrodata.dto.UserDetailsDTO;
import com.incture.metrodata.entity.CommentsDetailsDo;
import com.incture.metrodata.exceptions.ExecutionFault;
import com.incture.metrodata.exceptions.InvalidInputFault;
import com.incture.metrodata.exceptions.NoResultFault;
import com.incture.metrodata.util.ServicesUtil;

@Repository("CommentsDAO")
public class CommentsDAO extends BaseDao<CommentsDetailsDo, CommentsDTO> {
	@Autowired
	UserDAO userDao;

	@Override
	CommentsDTO exportDto(CommentsDetailsDo dos) {
		CommentsDTO dto = new CommentsDTO();
		if (!ServicesUtil.isEmpty(dos)) {
			if (!ServicesUtil.isEmpty(dos.getId())) {
				dto.setId(dos.getId());
			}
			if (!ServicesUtil.isEmpty(dos.getComment())) {
				dto.setComment(dos.getComment());
			}
			if (!ServicesUtil.isEmpty(dos.getComment())) {
				dto.setComment(dos.getComment());
			}
			if (!ServicesUtil.isEmpty(dos.getCreatedBy())) {
				dto.setCreatedBy(dos.getCreatedBy());
			}
			if (!ServicesUtil.isEmpty(dos.getUpdatedAt())) {
				UserDetailsDTO userDto = new UserDetailsDTO();
				userDto.setUserId(dos.getCreatedBy());
				try {
					userDto = userDao.findById(userDto);
				} catch (Exception e) {
				}
				if (!ServicesUtil.isEmpty(userDto)) {
					dto.setCreatedBy(userDto);
				}
			}
			if (!ServicesUtil.isEmpty(dos.getCreatedAt())) {
				dto.setCreatedAt(dos.getCreatedAt());
			}

		}
		return dto;
	}

	@Override
	CommentsDetailsDo importDto(CommentsDTO dto, CommentsDetailsDo dos)
			throws InvalidInputFault, ExecutionFault, NoResultFault, Exception {
		if (ServicesUtil.isEmpty(dos))
			dos = new CommentsDetailsDo();

		if (!ServicesUtil.isEmpty(dto)) {
			if (!ServicesUtil.isEmpty(dto.getId())) {
				dos.setId(dto.getId());
			}
			if (!ServicesUtil.isEmpty(dto.getComment())) {
				dos.setComment(dto.getComment());
			}
			if (!ServicesUtil.isEmpty(dto.getCreatedBy())) {
				dos.setCreatedBy((String) dto.getCreatedBy());
			}
			if (!ServicesUtil.isEmpty(dto.getCreatedAt())) {
				dos.setCreatedAt(dto.getCreatedAt());
			}
			if (!ServicesUtil.isEmpty(dto.getUpdatedAt())) {
				dos.setUpdatedAt(dto.getUpdatedAt());
			}

		}

		return dos;
	}

}
