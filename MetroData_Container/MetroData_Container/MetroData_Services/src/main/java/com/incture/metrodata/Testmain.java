package com.incture.metrodata;

import java.io.IOException;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Sender;


public class Testmain {
	
	 
   /* public static void main(String[] args) throws IOException, DocumentException {
    	File file = File.createTempFile("temp-file-name", ".pdf"); 
		
 	   System.err.println("Temp file : " + file.getAbsolutePath());
        file.getParentFile().mkdirs();
        new Testmain().createPdf(file.getAbsolutePath());
    }
    public void createPdf(String dest) throws IOException, DocumentException {
        Document document = new Document();
        
        PdfPTable table = new PdfPTable(new float[] { 2, 1, 2 });
  	  table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
  	  table.addCell("Name");
            table.addCell("Age");
            table.addCell("Location");
  	  table.setHeaderRows(1);
  	  PdfPCell[] cells = table.getRow(0).getCells(); 
  	  for (int j=0;j<cells.length;j++){
  	     cells[j].setBackgroundColor(BaseColor.GRAY);
  	  }
            for (int i=1;i<5;i++){
      	     table.addCell("Name:"+i);
               table.addCell("Age:"+i);
               table.addCell("Location:"+i);
            }
        
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(dest));
        document.open();
        
        PdfContentByte canvas = writer.getDirectContent();
        Barcode128 code128 = new Barcode128();
        code128.setCode("TRIP00000024");
        code128.setCodeType(Barcode128.CODE128);
        code128.setBarHeight(100);
        code128.setN(20);
        PdfTemplate template = code128.createTemplateWithBarcode(
                canvas, BaseColor.BLACK, BaseColor.BLACK);
        template.setWidth(200);
        template.setHeight(50);;
        float x = 36;
        float y = 750;
        float w = template.getWidth();
        float h = template.getHeight();
        canvas.saveState();
        canvas.setColorFill(BaseColor.WHITE);
        canvas.rectangle(x, y, w, h);
        canvas.fill();
        canvas.restoreState();
        canvas.addTemplate(template, 50, 750);
        document.close();
        
        
        
        Document document = new Document();
  	  PdfPTable table = new PdfPTable(new float[] { 2, 1, 2 });
  	  table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
  	  table.addCell("Name");
            table.addCell("Age");
            table.addCell("Location");
  	  table.setHeaderRows(1);
  	  PdfPCell[] cells = table.getRow(0).getCells(); 
  	  for (int j=0;j<cells.length;j++){
  	     cells[j].setBackgroundColor(BaseColor.GRAY);
  	  }
            for (int i=1;i<5;i++){
      	     table.addCell("Name:"+i);
               table.addCell("Age:"+i);
               table.addCell("Location:"+i);
            }
  	  PdfWriter.getInstance(document, new FileOutputStream("sample3.pdf"));
  	  document.open();
            document.add(table);
  	  document.close();
  	  System.out.println("Done");
    }*/
 
   
    
	


		/*// http://localhost:8080/RESTfulExample/json/product/post
		public static void main(String[] args) {

		  try {

			URL url = new URL("https://metrodata.accounts.ondemand.com/service/scim/Users");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/scim+json");
			 String authString = "T000003" + ":" + "Am12345!";
		        String authStringEnc = new String(Base64.encodeBase64(authString.getBytes()));
		        conn.setRequestProperty("Authorization", "Basic " + authStringEnc);
		        JsonObject obj = new JsonObject();
		        JsonObject name = new  JsonObject();
		        JsonArray array = new JsonArray();
		        JsonObject email = new JsonObject();
		        email.addProperty("value", "luckybarkane54118@gmail.com");
		        array.add(email);
		        name.addProperty("familyName", "abcd");
		       // obj.addProperty("name", name.toString());
		        obj.add("emails", array);
		        


			OutputStream os = conn.getOutputStream();
			os.write(obj.toString().getBytes());
			os.flush();

			if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
				throw new RuntimeException("Failed : HTTP error code : "
					+ conn.getResponseCode());
			}
			if(conn.getResponseCode() == HttpURLConnection.HTTP_CONFLICT){
				throw new RuntimeException("user already exists");
			}

			BufferedReader br = new BufferedReader(new InputStreamReader(
					(conn.getInputStream())));

			String output;
			System.out.println("Output from Server .... \n");
			while ((output = br.readLine()) != null) {
				System.out.println(output);
			}

			conn.disconnect();

		  } catch (MalformedURLException e) {

			e.printStackTrace();

		  } catch (IOException e) {

			e.printStackTrace();

		 }

}*/
	
	public static void main(String[] args) {
		
		Sender sender = new Sender("AAAALvsOsWI:APA91bHcyMLOehvRvG4PIrjkWcfOrMhMVAT6v4VElu5v45R8amrQuN5zBPFdsAFOUqXyHtzmsM3jrhtopiuzyh9W4KGb5ukTOorOB6X9T-YYb8SAEDQQw8CU06TSQEQJKVq3xBltD_GU");
		Message message = new Message.Builder()
				.addData("title", "asdasdd").addData("body", "aasda").collapseKey("type_a")
				.build();
		try {
			sender.send(message, "fLVxRcyd3F4:APA91bE4U7O2d77q3RYKw1H109BZcncU3AyEM80IRJNxzlvTwVo_74S8hv6TdvGjF3l1zxlOHerkNJLgwn5ZYRwI-nFhtcG3tleuKI8dyutISPLDvVdGein7t1fm5Hr7xDBg1jy989Wi", 4);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

