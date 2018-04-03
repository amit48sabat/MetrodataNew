package com.incture.metrodata.service;

import org.apache.chemistry.opencmis.client.api.Document;


public interface DocumentServiceLocal {


	Document getDocById(String id);

	String upload(byte[] file, String fileName, String fileType);

}
