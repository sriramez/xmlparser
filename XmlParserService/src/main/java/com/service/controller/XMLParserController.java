package com.service.controller;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

import com.service.exception.XmlRemoveEmployeeException;
import com.service.exception.XmlAddExployeeException;
import com.service.exception.XmlReadException;
import com.service.exception.XmlValidationException;
import com.service.exception.XmlWriteException;
import com.service.services.impl.FileStorageServiceImpl;
import com.service.services.impl.XMLParserServiceImpl;
import com.service.services.interfaces.FileStorageService;
import com.service.services.interfaces.XmlParserService;

import xmlparser.XmlValidatorUtil;

@RestController
public class XMLParserController {

	@Autowired
	XmlParserService service;

	@Autowired
	FileStorageService fileStorage;

	@PostMapping(path = "employee", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_XML_VALUE)
	public String readXMLFile(@RequestParam("file") MultipartFile file, @RequestParam String key)
			throws XmlValidationException, XmlReadException {

		File fileFromServer = fileStorage.storeFileToServer(file, key);

		return service.readXMLFileToString(fileFromServer.getAbsolutePath(), key);
	}

	@PostMapping(path = "validator", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_XML_VALUE)
	public String uploadValidator(@RequestParam("file") MultipartFile file, @RequestParam String key)
			throws IOException {
		File fileFromServer = fileStorage.storeFileToServer(file, key);
		XmlValidatorUtil.setValidator(fileFromServer.getAbsolutePath());
		return FileUtils.readFileToString(fileFromServer, StandardCharsets.UTF_8);
	}

	@PutMapping(path = "employee", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_XML_VALUE)
	public String addEmployee(@RequestParam("file") MultipartFile file, @RequestParam String key)
			throws XmlValidationException, XmlAddExployeeException, IOException {
		File fileFromServer = fileStorage.storeFileToServer(file, key);
		return service.addEmployees(key, FileUtils.readFileToString(fileFromServer, StandardCharsets.UTF_8));
	}

	@DeleteMapping(path = "employee", produces = MediaType.APPLICATION_XML_VALUE)
	public String removeEmployee(@RequestParam String key, @RequestParam String param, @RequestParam String value)
			throws XmlRemoveEmployeeException {
		return service.removeEmployee(param, value, key);
	}

	@GetMapping(path = "employees", produces = MediaType.APPLICATION_XML_VALUE)
	public String getXmlFile(@RequestParam String key) throws XmlWriteException {
		return service.getOutputFile(key);
	}

}
