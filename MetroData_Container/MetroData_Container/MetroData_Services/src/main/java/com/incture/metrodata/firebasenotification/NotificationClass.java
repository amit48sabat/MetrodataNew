package com.incture.metrodata.firebasenotification;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Notification;
import com.google.android.gcm.server.Sender;

@Service("notification")
public class NotificationClass {
	@Autowired
	private Environment environment;

	public void sendNotification(String msgTitle, List<String> tokenList, String msgBody) throws IOException {
		String serverkey = environment.getProperty("fcm.serverkey");
		Sender sender = new Sender(serverkey);
		Notification notification = new Notification.Builder("default").color("cyan").body(msgBody).title(msgTitle)
				.badge(5).build();
		Message message = new Message.Builder().collapseKey("notification").delayWhileIdle(true)
				.notification(notification).priority(Message.Priority.HIGH).build();
		sender.send(message, tokenList, 4);

	}

	public void sendNotification(String msgTitle, String token, String msgBody) throws IOException {
		String serverkey = environment.getProperty("fcm.serverkey");
		Sender sender = new Sender(serverkey);
		Notification notification = new Notification.Builder("default").color("cyan").body(msgBody).title(msgTitle)
				.badge(5).build();
		Message message = new Message.Builder().collapseKey("notification").delayWhileIdle(true)
				.notification(notification).priority(Message.Priority.HIGH).build();
		sender.send(message, token, 4);

	}
}