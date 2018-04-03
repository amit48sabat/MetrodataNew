package com.incture.metrodata;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.Barcode128;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;

public class Testmain {
	
	public static final String DEST = "results/barcodes/barcode_background.pdf";
	 
    public static void main(String[] args) throws IOException, DocumentException {
    	File file = File.createTempFile("temp-file-name", ".pdf"); 
		
 	   System.err.println("Temp file : " + file.getAbsolutePath());
        file.getParentFile().mkdirs();
        new Testmain().createPdf(file.getAbsolutePath());
    }
    public void createPdf(String dest) throws IOException, DocumentException {
        /*Document document = new Document();
        
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
        document.close();*/
        
        
        
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
    }
 
   
    
   
}

