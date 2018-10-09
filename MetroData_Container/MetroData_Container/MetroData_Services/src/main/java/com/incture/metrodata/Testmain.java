package com.incture.metrodata;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.PreencodedMimeBodyPart;
import javax.mail.util.ByteArrayDataSource;

import org.apache.commons.codec.binary.Base64;

public class Testmain {

	public static void main(String[] args) throws javax.mail.MessagingException, IOException {

		String value = "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";

		Session session = getJavaMailSender();

		MimeMessage mimeMessage = new MimeMessage(session);
		Map<String, Object> map = new HashMap<>();
		map.put("endTime", new Date().toString());
		map.put("receiverName", "acb");
		map.put("dnNo", "1526");
		map.put("shipTo", "125364y5");
		map.put("address", "142356446");
		map.put("sendTo", "luckybarkaneofficial@gmail.com");
		try {
			String endTime = (String) map.get("endTime");
			String receiverName = (String) map.get("receiverName");
			String dnNo = (String) map.get("dnNo");
			String shipTo = (String) map.get("shipTo");
			String address = (String) map.get("address");
			String sendTo = (String) map.get("sendTo");
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

			byte[] decodedString = Base64.decodeBase64(value);

			ByteArrayDataSource bads = new ByteArrayDataSource(decodedString, "image/png");
			DataHandler dh = new DataHandler(bads);
			// mimeMessage.setDataHandler(dh);
			// mimeMessage.setFileName("Attach");

			Multipart mp = new MimeMultipart("mixed");
			MimeBodyPart mbp = new MimeBodyPart();
			mbp.setFileName("Attach");
			mbp.setDataHandler(dh);
			mp.addBodyPart(mbp);

			MimeBodyPart mbp2 = new MimeBodyPart();
			mbp2.setContent(htmlMsg, "text/html");
			mp.addBodyPart(mbp2);

			// mimeMessage.addHeader("Content-Transfer-Encoding", "UTF-16");
			mimeMessage.setContent(mp);
			// mimeMessage.setContent("Hello", "UTF-8");
			// helper.addAttachment("MyImageName.jpg", new
			// ByteArrayResource(value.getBytes()));
	       
			mimeMessage.setRecipients(Message.RecipientType.TO,
			            InternetAddress.parse(sendTo));
			mimeMessage.setSubject("Test");
	 		Transport.send(mimeMessage);
  			
		} catch (MessagingException e) {
			e.printStackTrace();
		}

	}

	private static MimeBodyPart addAttachment(final String fileName, final String fileContent)
			throws MessagingException {
		if (fileName == null || fileContent == null) {
			return null;
		}

		MimeBodyPart filePart = new PreencodedMimeBodyPart("base64");
		filePart.setText(fileContent);
		return filePart;
	}

	public static Session getJavaMailSender() {
		//JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		/*mailSender.setHost();
		mailSender.setPort(587);
		mailSender.setUsername();
		mailSender.setPassword();*/

		/*Properties props = new Properties();
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.debug", "true");*/
		Properties props = new Properties();
	      props.put("mail.smtp.auth", "true");
	      props.put("mail.smtp.starttls.enable", "true");
	      props.put("mail.smtp.host", "smtp.gmail.com");
	      props.put("mail.smtp.port", "587");

		Session session = Session.getInstance(props,
		         new javax.mail.Authenticator() {
		            protected PasswordAuthentication getPasswordAuthentication() {
		               return new PasswordAuthentication("metrodata.pod@gmail.com", "Incture@123");
		            }
		         });
		
		return session;
	}

}
