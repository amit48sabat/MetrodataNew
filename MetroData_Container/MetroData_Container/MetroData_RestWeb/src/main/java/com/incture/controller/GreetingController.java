package com.incture.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class GreetingController {

	/*@Autowired
	 SimpMessagingTemplate template;
	
    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public Greeting greeting(Principal principal,HelloMessage message) throws Exception {
         //template.convertAndSendToUser(principal.getName(), "/queue/reply", );
    	return new Greeting("Hello, " + HtmlUtils.htmlEscape(message.getName()) + "! - "+principal.getName());
    }

    @MessageMapping("/message")
    public void processMessageFromClient(Principal principal,  HelloMessage message){
    	template.convertAndSendToUser(principal.getName(), "/queue/reply", new Greeting("Hi "));
    }*/
    
	
	@Autowired
	private SimpMessagingTemplate messagingTemplate;

	@MessageMapping("/hello")
	public void greeting(Principal principal, HelloMessage message) throws  Exception {
		Greeting greeting = new Greeting();
		greeting.setContent("Hello !");
		messagingTemplate.convertAndSendToUser(message.getToUser(), "/queue/reply", greeting);
	}
}

