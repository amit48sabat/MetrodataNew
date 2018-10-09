package com.incture.metrodata.service;

import java.util.Map;

import javax.activation.DataHandler;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.incture.metrodata.dto.ResponseDto;

@Service("EmailService")
@Lazy
public class EmailService implements EmailServiceLocal {

	

	private static final Logger LOGGER = LoggerFactory.getLogger(EmailService.class);

	@Autowired
	Session session;
	
	@Async
	@Override
	public ResponseDto sendMail(Map<String, Object> map) {
		

		MimeMessage mimeMessage = new MimeMessage(session);
		String endTime = (String) map.get("endTime");
		String receiverName = (String) map.get("receiverName");
		String dnNo = (String) map.get("dnNo");
		String shipTo= (String) map.get("shipTo");
		String address = (String) map.get("address");
		String sendTo = (String) map.get("sendTo");
		String image = (String) map.get("image");
		try {
			String htmlMsg = "" + "<p>Bukti penerimaan barang sbb :</p>" + 
			          "<table>"
					+ "<tr> <th>Tanggal : <th>" + "<td>" + endTime + "</td></tr>" +
			          "<tr> <th>Penerima : <th>" + "<td>"
					+ receiverName + "</td></tr>" + "<tr> <th>DN No : <th>" + "<td>" + dnNo + "</td></tr>"
					+ "<tr> <th>Ship to : <th>" + "<td>" + shipTo + "</td></tr>" + "<tr> <th>Address : <th	>" + "<td>"
					+ address + "</td></tr>" + "</table>" + ""
					+ "<p>Barang telah diterima dengan baik sesuai Delivery Note.<br/>"
					+ "Jika ada pertanyaan silahkan hubungi Logistic@metrodata.co.id atau 088812341234.<br/>"
					+ "Terimakasih atas kerjasamanya.</p>" + "<br/><br/>" + "<p>Regards,<br/>"
					+ "Logistic PT. Synnex Metrodata Indonesia</p>";

			byte[] decodedString = Base64.decodeBase64(image);
			ByteArrayDataSource bads = new ByteArrayDataSource(decodedString, "image/png");
			DataHandler dh = new DataHandler(bads);

			Multipart mp = new MimeMultipart("mixed");
			MimeBodyPart mbp = new MimeBodyPart();
			mbp.setFileName("Attach");
			mbp.setDataHandler(dh);
			mp.addBodyPart(mbp);

			MimeBodyPart mbp2 = new MimeBodyPart();
			mbp2.setContent(htmlMsg, "text/html");
			mp.addBodyPart(mbp2);

			mimeMessage.setContent(mp);
			mimeMessage.setRecipients(Message.RecipientType.TO,
			            InternetAddress.parse(sendTo));
			mimeMessage.setSubject("Metrodata : Bukti Penerimaan DN# " + dnNo);
	 		Transport.send(mimeMessage);
	 		LOGGER.error("EMAIL SEND FOR DELIVERY NOTE : "+dnNo);
  			
		} catch (MessagingException e) {
			LOGGER.error("ERROR : EMAIL DIDNT SEND FOR DELIVERY NOTE : "+dnNo+". CAUSE"+e.getMessage());
		}
       return null;
	}

	/*@Autowired
    public JavaMailSender mailSender;

	@Async
	@Override
	public ResponseDto sendMail(Map<String, Object> map) {
		
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper helper;
		try {
			helper = new MimeMessageHelper(mimeMessage, false, "utf-8");
			String endTime = (String) map.get("endTime");
			String receiverName = (String) map.get("receiverName");
			String dnNo = (String) map.get("dnNo");
			String shipTo= (String) map.get("shipTo");
			String address = (String) map.get("address");
			String sendTo = (String) map.get("sendTo");
			String image = (String) map.get("image");
			String htmlMsg = ""
					+ "<p>Bukti penerimaan barang sbb :</p>"
					+ "<table style='border:0px;'>"
					+ "<tr> <th>Tanggal : <th>"
					+ "<td>"+endTime+"</td></tr>"
					+ "<tr> <th>Penerima : <th>"
					+ "<td>"+receiverName+"</td></tr>"
					+ "<tr> <th>DN No : <th>"
					+ "<td>"+dnNo+"</td></tr>"
					+ "<tr> <th>Ship to : <th>"
					+ "<td>"+shipTo+"</td></tr>"
					+ "<tr> <th>Address : <th	>"
					+ "<td>"+address+"</td></tr>"
					+ "</table>"
					+ ""
					+ "<p>Barang telah diterima dengan baik sesuai Delivery Note.<br/>"
					+ "Jika ada pertanyaan silahkan hubungi Logistic@metrodata.co.id atau 088812341234.<br/>"
					+ "Terimakasih atas kerjasamanya.</p>"
					+ "<br/><br/>"
					+ "<p>Regards,<br/>"
					+ "Logistic PT. Synnex Metrodata Indonesia</p>";
			  
			byte[] decodedString = Base64.decodeBase64(image);

			ByteArrayDataSource bads = new ByteArrayDataSource(decodedString, "image/png");
			DataHandler dh = new DataHandler(bads);
			Multipart mp = new MimeMultipart("mixed");
			MimeBodyPart mbp = new MimeBodyPart();
			mbp.setFileName("Attach");
			mbp.setDataHandler(dh);
			mp.addBodyPart(mbp);

			MimeBodyPart mbp2 = new MimeBodyPart();
			mbp2.setContent(htmlMsg, "text/html");
			mp.addBodyPart(mbp2);

			mimeMessage.setContent(mp);
			helper.setTo(sendTo);
			helper.setSubject("Subject : Bukti Penerimaan DN# " + map.get("dnNo"));
			mailSender.send(mimeMessage);
		} catch (MessagingException e) {
			LOGGER.error("Error in sending mail for DN no : "+map.get("dnNo")+" error => "+e.getMessage()+" Cause"+e.getCause());
			e.printStackTrace();
		}
		// TODO Auto-generated method stub
		String receiver = (String)map.get("reciever");
		String sender = (String)map.get("sender");
		String subject = (String)map.get("subject");
		String body = (String)map.get("body");
		
		SimpleMailMessage message = new SimpleMailMessage(); 
        message.setTo(receiver); 
        message.setSubject(subject); 
        message.setText(body);
        emailSender.send(message);
		
		return null;
	}*/
	

}
