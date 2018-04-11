package com.incture.metrodata.service;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.incture.metrodata.constant.Message;
import com.incture.metrodata.dao.CommentsDAO;
import com.incture.metrodata.dao.UserDAO;
import com.incture.metrodata.dto.CommentsDTO;
import com.incture.metrodata.dto.ResponseDto;

/**
 * @author Lucky.Barkane
 *
 */
@Service("commentsService")
@Transactional
public class CommentsService implements CommentsServiceLocal {

	@Autowired
	UserDAO userDao;

	@Autowired
	CommentsDAO commentsDAO;

	/**
	 * find all messages between the time duration
	 */

	/**
	 * api for updating the message
	 */
	@Override
	public ResponseDto update(CommentsDTO dto, String updatedBy) {
		ResponseDto responseDto = new ResponseDto();
		try {

			dto = commentsDAO.update(dto);

			responseDto.setStatus(true);
			responseDto.setCode(HttpStatus.SC_OK);
			responseDto.setData(dto);
			responseDto.setMessage(Message.SUCCESS.toString());
		} catch (Exception e) {
			responseDto.setStatus(false);
			responseDto.setCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			responseDto.setMessage(Message.FAILED + " : " + e.getMessage());
		}
		return responseDto;
	}

	/**
	 * api for deleting the message
	 */
	@Override
	public ResponseDto delete(CommentsDTO dto) {
		ResponseDto responseDto = new ResponseDto();
		try {

			commentsDAO.deleteById(dto);

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
