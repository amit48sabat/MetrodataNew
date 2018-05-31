package com.incture.metrodata.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.data.ContentStream;
import org.apache.chemistry.opencmis.commons.data.RepositoryInfo;
import org.apache.chemistry.opencmis.commons.enums.VersioningState;
import org.apache.chemistry.opencmis.commons.exceptions.CmisNameConstraintViolationException;
import org.apache.chemistry.opencmis.commons.exceptions.CmisObjectNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.incture.metrodata.util.ServicesUtil;
import com.sap.ecm.api.EcmService;
import com.sap.ecm.api.RepositoryOptions;
import com.sap.ecm.api.RepositoryOptions.Visibility;

@Service("documentservice")
public class DocumentService implements DocumentServiceLocal {
	private Session openCmisSession = null;

	private static final Logger LOGGER = LoggerFactory.getLogger(DocumentService.class);
	
	
	/**
	 * Connect to the document store service in SAP Cloud Platform and create or
	 * look up sample repository.
	 */
	@Override
	public String upload(byte[] file,String fileName,String fileType) {
		LOGGER.error("INSIDE UPLOAD SERVICE OF DOCUMENT SERVICE");
		try {
			connectToEcm();
			return createFolderAndDoc(file,fileName,fileType);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	private void connectToEcm() {
		
		LOGGER.error("INSIDE connectToEcm() OF DOCUMENT SERVICE");
		// Get a handle to the service by performing a JNDI lookup; EcmService
		// must be a <resource-ref> in the web.xml
		EcmService ecmService = null;
		String ecmServiceLookupName = "java:comp/env/" + "EcmService";
		try {
			InitialContext ctx = new InitialContext();
			ecmService = (EcmService) ctx.lookup(ecmServiceLookupName);
		} catch (NamingException e) {
			throw new RuntimeException("Lookup of ECM service " + ecmServiceLookupName
					+ " via JNDI failed with reason: " + e.getMessage());
		}

		// Create a key secured repository identified by a unique name and a
		// secret key (minimum length 10 characters)
		String uniqueName = "POD_PROD_Repo";
		String secretKey = "MetroSCP.2018";
		try {
			// Connect to ECM service accessing the repository
			openCmisSession = ecmService.connect(uniqueName, secretKey);

			// Continue if connection was successful
		} catch (CmisObjectNotFoundException e) {
			// If the session couldn't be created the repository is missing so
			// create it now
			RepositoryOptions options = new RepositoryOptions();
			options.setUniqueName(uniqueName);
			options.setRepositoryKey(secretKey);
			options.setVisibility(Visibility.PROTECTED);
			options.setMultiTenantCapable(true);
			ecmService.createRepository(options);

			// Now try again and connect to ECM service accessing the newly
			// created repository
			openCmisSession = ecmService.connect(uniqueName, secretKey);
		} catch (Exception e) {
			throw new RuntimeException("Connect to ECM service failed with reason: " + e.getMessage(), e);
		}

		// Print some information about the repository
		RepositoryInfo repositoryInfo = openCmisSession.getRepositoryInfo();
		if (repositoryInfo == null) {
			System.out.println("<p>Failed to get repository info!</p>");
		} else {
			System.out.println("<p>Product Name: " + repositoryInfo.getProductName() + "</p>");
			System.out.println("<p>Repository Id: " + repositoryInfo.getId() + "</p>");
			System.out.println("<p>Root Folder Id: " + repositoryInfo.getRootFolderId() + "</p>");
		}
	}

	
	/**
	 * Create folder and document in root folder in sample repository if not yet
	 * done.
	 */
	private String createFolderAndDoc(byte[] file,String fileName,String fileType) throws IOException {
		// If we didn't get a session fail with an appropriate message
		if (openCmisSession == null) {
			return null;
		}
		LOGGER.error("INSIDE createFolderAndDoc() OF DOCUMENT SERVICE");
		// Get root folder from CMIS session
		Folder rootFolder = openCmisSession.getRootFolder();

		// Create new folder (requires at least type id and name of the folder)
		Map<String, String> folderProps = new HashMap<String, String>();
		folderProps.put(PropertyIds.OBJECT_TYPE_ID, "cmis:folder");
		folderProps.put(PropertyIds.NAME, "Signatures");
		try {
			rootFolder.createFolder(folderProps);
			//response.getWriter().println("<p>A folder with name 'Signatures' was created.</p>");
		} catch (CmisNameConstraintViolationException e) {
			// If the folder already exists, we get a
			// CmisNameConstraintViolationException
			//response.getWriter().println("<p>A folder with name 'Signatures' already exists, nothing created.</p>");
		}

		// Create new document (requires at least type id and name of the
		// document and some content)
		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put(PropertyIds.OBJECT_TYPE_ID, "cmis:document");
		properties.put(PropertyIds.NAME, fileName);
		
		try {
			/*InputStream stream = new FileInputStream(file);
			final MimetypesFileTypeMap fileTypeMap = new MimetypesFileTypeMap();  
			String fileType =   fileTypeMap.getContentType(fileName);*/
			InputStream stream = new ByteArrayInputStream(file);
			ContentStream contentStream = openCmisSession.getObjectFactory().createContentStream(fileName,
					file.length,fileType, stream);
			Document document= rootFolder.createDocument(properties, contentStream, VersioningState.NONE);
			System.err.println(document.getId()+"    "+ document.getContentStreamId());
			return document.getId();
			//response.getWriter().println("<p>A document with name 'HelloWorld.txt' was created.</p>");
		} catch (CmisNameConstraintViolationException e) {
			e.printStackTrace();
			return null;
			// If the document already exists, we get a
			// CmisNameConstraintViolationException
			//response.getWriter()
				//	.println("<p>A document with name 'HelloWorld.txt' already exists, nothing created.</p>");
		}
	}

	@Override
	public Document getDocById(String id){
		LOGGER.error("INSIDE getDocById() OF DOCUMENT SERVICE");
		if(ServicesUtil.isEmpty(openCmisSession)){
			connectToEcm();
		}
		Document document = (Document) openCmisSession.getObject(id);
		return document;
	}
}
