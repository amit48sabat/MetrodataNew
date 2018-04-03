package com.incture.controller;

import java.io.IOException;

import org.apache.chemistry.opencmis.client.api.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.incture.metrodata.dto.DeliveryHeaderDTO;
import com.incture.metrodata.service.DeliveryHeaderServiceLocal;
import com.incture.metrodata.service.DocumentServiceLocal;

@RestController
@RequestMapping(value = "/documents")
public class DocumentController {

	@Autowired
	DocumentServiceLocal documentServiceLocal;
	
	@Autowired
	DeliveryHeaderServiceLocal deliveryHeaderServiceLocal;

/*	@PostMapping("/upload")
	public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile uploadfile) {

		if (uploadfile.isEmpty()) {
			return new ResponseEntity<Object>("please select a file!", HttpStatus.OK);
		}

		try {
			File file = getTempFile(uploadfile);
			System.err.println(file);
			documentServiceLocal.upload(file,uploadfile.getOriginalFilename());
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<Object>("Successfully uploaded - " + uploadfile.getOriginalFilename(),
				new HttpHeaders(), HttpStatus.OK);

	}

	public File getTempFile(MultipartFile multipartFile) throws IOException {
		File temp = File.createTempFile(multipartFile.getName(), ".tmp");
		FileUtils.writeByteArrayToFile(temp, multipartFile.getBytes());
		return temp;
	}*/
	
	@RequestMapping(path = "/download/{deliveryNoteId}", method = RequestMethod.GET)
	public ResponseEntity<Resource> download(@PathVariable Long deliveryNoteId) throws IOException {

	    // ...
		DeliveryHeaderDTO dto = (DeliveryHeaderDTO) deliveryHeaderServiceLocal.findById(deliveryNoteId).getData();
		Document doc =documentServiceLocal.getDocById(dto.getSignatureDocId());
	    InputStreamResource resource = new InputStreamResource(doc.getContentStream().getStream());
	    HttpHeaders header = new HttpHeaders();
	    header.set(HttpHeaders.CONTENT_DISPOSITION,
	                   "attachment; filename=" + doc.getContentStream().getFileName());
	    header.setContentLength(doc.getContentStream().getLength());
	    return ResponseEntity.ok()
	            .headers(header)
	            //.contentLength(file.length())
	    		.contentType(MediaType.parseMediaType(doc.getContentStream().getMimeType()))
	            .body(resource);
	}
}
