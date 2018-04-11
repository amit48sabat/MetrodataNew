package com.incture.metrodata;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.incture.metrodata.dto.DeliveryHeaderDTO;
import com.incture.metrodata.dto.DeliveryItemDTO;
import com.incture.metrodata.dto.TripDetailsDTO;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.Barcode128;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;

public class Testmain {

	/*
	 * public void process(Table table, PdfFont font, boolean isHeader) { for
	 * (int i = 0; i < 10; i++) { if (isHeader) { table.addHeaderCell( new
	 * Cell().add( new Paragraph("adsd").setFont(font))); } else {
	 * table.addCell( new Cell().add( new Paragraph("asdasd").setFont(font))); }
	 * }
	 * 
	 * 
	 * } public static void main(String[] args) throws IOException { PdfWriter
	 * writer;
	 * 
	 * 
	 * try { writer = new PdfWriter("d://test.pdf"); PdfDocument pdf = new
	 * PdfDocument(writer); Document document = new Document(pdf,
	 * PageSize.A4.rotate()); document.setMargins(20, 20, 20, 20); canvas =
	 * writer.getDirectContent();
	 * 
	 * document.add(new Paragraph("start page")); PdfFont font =
	 * PdfFontFactory.createFont(FontConstants.HELVETICA); PdfFont bold =
	 * PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD); Table table =
	 * new Table(new float[]{4, 1, 3, 4, 3, 3, 3, 3, 1});
	 * table.setWidthPercent(100); Testmain t =new Testmain(); t.process(table,
	 * bold, true); for (String string : args) { t.process(table, font, false);
	 * }
	 * 
	 * for (int i = 0; i < 200; i++) {
	 * 
	 * document.add(new Paragraph("start page")); } document.add(table);
	 * document.close(); } catch (FileNotFoundException e) { // TODO
	 * Auto-generated catch block e.printStackTrace(); }
	 * 
	 * }
	 */

	public static void main(String[] args) throws IOException, DocumentException {
		File file = new File("d://temp-file-name", ".pdf");

		System.err.println("Temp file : " + file.getAbsolutePath());
		file.getParentFile().mkdirs();

		TripDetailsDTO tripDetailsDTO = new TripDetailsDTO();
		tripDetailsDTO.setTripId("TRIP1234567890");

		Set<DeliveryHeaderDTO> deliveryHeaderDTOs = new LinkedHashSet<DeliveryHeaderDTO>();
		List<DeliveryItemDTO> deliveryItemDTOs;
		DeliveryHeaderDTO deliveryHeaderDTO;
		DeliveryItemDTO deliveryItemDTO = null;
		for (long i = 0; i < 10; i++) {
			deliveryHeaderDTO = new DeliveryHeaderDTO();
			deliveryHeaderDTO.setDeliveryNoteId(i);
			deliveryHeaderDTO.setShipToAddress("incture, bangalore");
			deliveryItemDTOs = new ArrayList<DeliveryItemDTO>();
			deliveryItemDTO = new DeliveryItemDTO();
			for (long j = 0; j < 5; j++) {
				deliveryItemDTO.setBatch("batch" + 1);
				deliveryItemDTO.setVolume(j + "");
				deliveryItemDTO.setDescription("desc");
				deliveryItemDTO.setMaterial("mat");
				deliveryItemDTO.setVolume(2 + "");
				deliveryItemDTOs.add(deliveryItemDTO);
			}
			deliveryHeaderDTO.setDeliveryItems(deliveryItemDTOs);
			deliveryHeaderDTOs.add(deliveryHeaderDTO);
		}
		tripDetailsDTO.setDeliveryHeader(deliveryHeaderDTOs);
		new Testmain().createPdf(file.getAbsolutePath(), tripDetailsDTO);
	}

	public void createPdf(String dest, TripDetailsDTO detailsDTO) throws IOException, DocumentException {
		Document document = new Document();

		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(dest));
		document.open();
	      Image image = Image.getInstance("src/main/resources/logo_metrodata.png");
	      image.scaleAbsoluteWidth(140);
	      image.scaleAbsoluteHeight(70);
	      //image.setAbsolutePosition(100, 100);
	      // set Absolute Position
	      //image.setAbsolutePosition(220f, 550f);
	      // set Scaling
	      // image.scalePercent(100f);
	      // set Rotation
	      // image.setRotationDegrees(45f);
	      document.add(image);
		
		PdfContentByte canvas = writer.getDirectContent();
		Barcode128 code128 = new Barcode128();
		code128.setCode("TRIP00000024");
		code128.setCodeType(Barcode128.CODE128);
		code128.setBarHeight(120);
		code128.setN(25);
		PdfTemplate template = code128.createTemplateWithBarcode(canvas, BaseColor.BLACK, BaseColor.BLACK);
		template.setWidth(300);
		template.setHeight(50);
		;
		float x = -300;
		float y = 750;
		float w = template.getWidth();
		float h = template.getHeight();
		canvas.saveState();
		canvas.setColorFill(BaseColor.WHITE);
		canvas.rectangle(x, y, w, h);
		canvas.fill();
		canvas.restoreState();
		canvas.addTemplate(template, 400, 750);
		document.add(new Paragraph("  "));
		document.add(new Paragraph("  "));
		
		for (DeliveryHeaderDTO deliveryHeaderDTO : detailsDTO.getDeliveryHeader()) {
			Paragraph p = new Paragraph("DELIVERY NOTE : "+deliveryHeaderDTO.getDeliveryNoteId()+" ( "+deliveryHeaderDTO.getShipToAddress()+" )");
			document.add(p );
			document.add(new Paragraph("  "));
			/*PdfPTable table = new PdfPTable(new float[] { 2, 1, 2 ,2,3});
			table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell("Material");
			table.addCell("Batch");
			table.addCell("Description");
			table.addCell("QTY");
			table.addCell("VOL");
			table.setHeaderRows(1);
			PdfPCell[] cells = table.getRow(0).getCells();
			for (int j = 0; j < cells.length; j++) {
				cells[j].setBackgroundColor(BaseColor.GRAY);
			}
			for (DeliveryItemDTO deliveryItemDTO : deliveryHeaderDTO.getDeliveryItems()) {
					table.addCell(deliveryItemDTO.getMaterial());
					table.addCell(deliveryItemDTO.getBatch());
					table.addCell(deliveryItemDTO.getDescription());
					table.addCell(deliveryItemDTO.getQuantity());
					table.addCell(deliveryItemDTO.getVolume());
			}
			document.add(table);*/
		}
		//document.add(table);
		document.addTitle("TRIP Manifest");
		document.close();

	}

	/*
	 * // http://localhost:8080/RESTfulExample/json/product/post public static
	 * void main(String[] args) {
	 * 
	 * try {
	 * 
	 * URL url = new
	 * URL("https://metrodata.accounts.ondemand.com/service/scim/Users");
	 * HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	 * conn.setDoOutput(true); conn.setRequestMethod("POST");
	 * conn.setRequestProperty("Content-Type", "application/scim+json"); String
	 * authString = "T000003" + ":" + "Am12345!"; String authStringEnc = new
	 * String(Base64.encodeBase64(authString.getBytes()));
	 * conn.setRequestProperty("Authorization", "Basic " + authStringEnc);
	 * JsonObject obj = new JsonObject(); JsonObject name = new JsonObject();
	 * JsonArray array = new JsonArray(); JsonObject email = new JsonObject();
	 * email.addProperty("value", "luckybarkane54118@gmail.com");
	 * array.add(email); name.addProperty("familyName", "abcd"); //
	 * obj.addProperty("name", name.toString()); obj.add("emails", array);
	 * 
	 * 
	 * 
	 * OutputStream os = conn.getOutputStream();
	 * os.write(obj.toString().getBytes()); os.flush();
	 * 
	 * if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED) { throw new
	 * RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
	 * } if(conn.getResponseCode() == HttpURLConnection.HTTP_CONFLICT){ throw
	 * new RuntimeException("user already exists"); }
	 * 
	 * BufferedReader br = new BufferedReader(new InputStreamReader(
	 * (conn.getInputStream())));
	 * 
	 * String output; System.out.println("Output from Server .... \n"); while
	 * ((output = br.readLine()) != null) { System.out.println(output); }
	 * 
	 * conn.disconnect();
	 * 
	 * } catch (MalformedURLException e) {
	 * 
	 * e.printStackTrace();
	 * 
	 * } catch (IOException e) {
	 * 
	 * e.printStackTrace();
	 * 
	 * }
	 * 
	 * }
	 */

	/*
	 * public static void main(String[] args) {
	 * 
	 * Sender sender = new Sender(
	 * "AAAALvsOsWI:APA91bHcyMLOehvRvG4PIrjkWcfOrMhMVAT6v4VElu5v45R8amrQuN5zBPFdsAFOUqXyHtzmsM3jrhtopiuzyh9W4KGb5ukTOorOB6X9T-YYb8SAEDQQw8CU06TSQEQJKVq3xBltD_GU"
	 * ); Message message = new Message.Builder() .addData("title",
	 * "asdasdd").addData("body", "aasda").collapseKey("type_a") .build(); try {
	 * sender.send(message,
	 * "fLVxRcyd3F4:APA91bE4U7O2d77q3RYKw1H109BZcncU3AyEM80IRJNxzlvTwVo_74S8hv6TdvGjF3l1zxlOHerkNJLgwn5ZYRwI-nFhtcG3tleuKI8dyutISPLDvVdGein7t1fm5Hr7xDBg1jy989Wi",
	 * 4); } catch (IOException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); } }
	 */
}
