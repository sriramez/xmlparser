package com.service.services.impl;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import com.service.exception.XmlRemoveEmployeeException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.exception.XmlAddExployeeException;
import com.service.exception.XmlReadException;
import com.service.exception.XmlValidationException;
import com.service.exception.XmlWriteException;
import com.service.model.RedisMessageModel;
import com.service.services.interfaces.FileStorageService;
import com.service.services.interfaces.XmlParserService;

import xmlparser.XMLValidator;
import xmlparser.XmlUtil;

@Service
public class XMLParserServiceImpl implements XmlParserService {

	@Autowired
	RedisTemplate<String, Object> redis;

	@Autowired
	ObjectMapper mapper;

	@Autowired
	Map<String, XMLValidator> validators;

	@Autowired
	FileStorageService service;

	private static final String APP_KEY = "xmlparser";

	private static final Logger logger = LoggerFactory.getLogger(XMLParserServiceImpl.class);

	public String readXMLFileToString(String filePath, String key) throws XmlValidationException, XmlReadException {
		String output = null;
		try {
			output = XmlUtil.readXmlFile(filePath);
		} catch (IOException e) {
			throw new XmlReadException(e.getLocalizedMessage());
		}
		if (!getValidator(key).validateContents(output)) {
			logger.debug("XMl validation failed");
			throw new XmlValidationException();
		}
		redis.opsForHash().put(APP_KEY, key, output);
		return output;
	}

	public String readXMLFileToString(String filePath) throws XmlReadException, XmlValidationException {
		return readXMLFileToString(filePath, "Default");
	}

	public String addEmployees(String key, String contents) throws XmlValidationException, XmlAddExployeeException {
		String xmlContents = (String) redis.opsForHash().get(APP_KEY, key);

		if (!getValidator(key).validateContents(contents)) {
			throw new XmlValidationException();
		}
		String output = null;
		try {
			output = XmlUtil.AddNewElementToXml(xmlContents, contents);
			redis.opsForHash().put(APP_KEY, key, output);
		} catch (ParserConfigurationException | SAXException | IOException | TransformerException e) {
			e.printStackTrace();
			logger.debug(e.getLocalizedMessage());
			throw new XmlAddExployeeException();
		}

		return output;
	}

	public String removeEmployee(String param, String value, String key) throws XmlRemoveEmployeeException {
		String xmlContents = (String) redis.opsForHash().get(APP_KEY, key);
		String output = null;
		try {
			output = XmlUtil.removeAnElementBasedOnParam(param, value, xmlContents);
			redis.opsForHash().put(APP_KEY, key, output);
		} catch (ParserConfigurationException | SAXException | IOException | TransformerException e) {
			e.printStackTrace();
			logger.debug(e.getLocalizedMessage());
			throw new XmlRemoveEmployeeException();
		}
		return output;
	}

	public String getOutputFile(String key) throws XmlWriteException {
		String xmlContents = (String) redis.opsForHash().get(APP_KEY, key);
		try {
			XmlUtil.writeContentToFile(service.getOutputPath(key) + File.separator + "output.xml", xmlContents);
		} catch (IOException e) {
			logger.debug(e.getLocalizedMessage());
			e.printStackTrace();
			throw new XmlWriteException();
		}
		return xmlContents;
	}

	@Override
	public String loadValidator(String absolutePath, String key) throws XmlValidationException {
		String validatorContents = "";
		try {
			validatorContents = FileUtils.readFileToString(new File(absolutePath), StandardCharsets.UTF_8);
			redis.convertAndSend("validators",
					mapper.writeValueAsString(new RedisMessageModel(key, validatorContents)));
		} catch (IOException e) {
			throw new XmlValidationException();
		}
		return validatorContents;
	}

	public XMLValidator getValidator(String key) {
		return validators.get(key);
	}

}
