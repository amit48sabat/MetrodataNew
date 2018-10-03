package com.incture.metrodata.service;

import java.util.Date;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.incture.metrodata.dto.ResponseDto;

@Service("EmailService")
@Lazy
public class EmailService implements EmailServiceLocal {

	

	private static final Logger LOGGER = LoggerFactory.getLogger(EmailService.class);
	
	@Autowired
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
			  
			mimeMessage.setContent(htmlMsg, "text/html");
			helper.setTo(sendTo);
			helper.setSubject("Subject : Bukti Penerimaan DN# "+map.get("dnNo"));
			mailSender.send(mimeMessage);
		} catch (MessagingException e) {
			LOGGER.error("Error in sending mail for DN no : "+map.get("dnNo")+" error => "+e.getMessage()+" Cause"+e.getCause());
			e.printStackTrace();
		}
		/*// TODO Auto-generated method stub
		String receiver = (String)map.get("reciever");
		String sender = (String)map.get("sender");
		String subject = (String)map.get("subject");
		String body = (String)map.get("body");
		
		SimpleMailMessage message = new SimpleMailMessage(); 
        message.setTo(receiver); 
        message.setSubject(subject); 
        message.setText(body);
        emailSender.send(message);*/
		
		return null;
	}
	
	/*@Async
	@Override
	public ResponseDto sendMail(Map<String, Object> map) {
		
		String receiver = (String)map.get("reciever");
		String sender = (String)map.get("sender");
		String subject = (String)map.get("subject");
		String body = (String)map.get("body");
		
	   String mailBody = "{ \"personalizations\": [{"
		   		+ "\"to\": [{ \"email\": \""+receiver+"\" 	}] 	}], "
		   		+ " \"from\": {  \"email\": \"metrodata.pod@gmail.com\"  }, "
		   		+ " \"subject\": \"Metrodata | "+subject+"\","
		   		+ " \"content\": [{ "
		   		+ " \"type\": \"text/plain\", "
		   		+ " \"value\": \""+body+"\"  }]"
		   		+ "}";
		
	   LOGGER.error("Email is send to "+receiver+" for dn id " +subject);
	     System.out.println(mailBody);
		URL url;
		StringBuilder sb = new StringBuilder();
		try {
			url = new URL("https://api.sendgrid.com/v3/mail/send");
			HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setDoOutput(true);
			urlConnection.setRequestMethod("POST");
			urlConnection.setRequestProperty("Content-Type", "application/json");
			urlConnection.setRequestProperty("Authorization", "Bearer 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");
			OutputStream os = urlConnection.getOutputStream();
			os.write(mailBody.getBytes());
			os.flush();
			BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
			String line;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
			reader.close();

			
		} catch (MalformedURLException e) {

			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
		return null;
	}*/

}
