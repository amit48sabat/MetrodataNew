package com.incture.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.incture.metrodata.dto.OrderTrackingDTO;


@Controller
@RequestMapping
public class TrackingController {

	
	@Autowired
	private SimpMessagingTemplate messagingTemplate;

	@MessageMapping("/tracking")
	public void greeting(@Payload OrderTrackingDTO dto) throws  Exception {
		messagingTemplate.convertAndSend("/topic/"+dto.getTripId(), dto);
	}
}
