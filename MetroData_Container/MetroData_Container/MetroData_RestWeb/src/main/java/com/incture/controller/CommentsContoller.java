package com.incture.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.incture.metrodata.dto.CommentsDTO;
import com.incture.metrodata.dto.ResponseDto;
import com.incture.metrodata.service.CommentsServiceLocal;

@RestController
@ComponentScan("com.incture")
@RequestMapping(value = "/comments", produces = "application/json")
public class CommentsContoller {

	@Autowired
	CommentsServiceLocal commentsServiceLocal;

	private static final Logger LOGGER = LoggerFactory.getLogger(CommentsContoller.class);
	
	/*
	 * @RequestMapping(method = RequestMethod.POST) public ResponseDto
	 * create(@RequestBody CommentsDTO dto) { return
	 * commentsServiceLocal.create(dto); }
	 */

	@RequestMapping(value = "/{commentId}", method = RequestMethod.PUT)

	public ResponseDto update(@PathVariable Long commentId, @RequestBody CommentsDTO dto, HttpServletRequest request) {
		String userId = request.getUserPrincipal().getName();
		dto.setId(commentId);
        
		LOGGER.error("INSIDE UPDATE COMMENT CONTROLLER");
		
		return commentsServiceLocal.update(dto, userId);
	}

	@RequestMapping(value = "/{commentId}", method = RequestMethod.DELETE)
	public ResponseDto delete(@PathVariable Long commentId) {
		CommentsDTO dto = new CommentsDTO();
		dto.setId(commentId);
		
		LOGGER.error("INSIDE DELETE COMMENT CONTROLLER");
		return commentsServiceLocal.delete(dto);
	}

	/*
	 * @RequestMapping(value = "/{userId}", method = RequestMethod.GET) public
	 * ResponseDto findById(@PathVariable String userId) { UserDetailsDTO dto =
	 * new UserDetailsDTO(); dto.setUserId(userId); return
	 * userServiceLocal.find(dto); }
	 */

}