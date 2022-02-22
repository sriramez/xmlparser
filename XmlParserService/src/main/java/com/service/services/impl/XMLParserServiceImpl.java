package com.service.services.impl;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import com.service.exception.XmlRemoveEmployeeException;
import com.service.exception.XmlAddExployeeException;
import com.service.exception.XmlReadException;
import com.service.exception.XmlValidationException;
import com.service.exception.XmlWriteException;
import com.service.services.interfaces.XmlParserService;

import redis.clients.jedis.JedisPool;
import xmlparser.XmlUtil;
import xmlparser.XmlValidatorUtil;

@Service
public class XMLParserServiceImpl implements XmlParserService{

	@Autowired
	JedisPool pool;

	@Autowired
	FileStorageServiceImpl service;

	private static final Logger logger = LoggerFactory.getLogger(XMLParserServiceImpl.class);

	public String readXMLFileToString(String filePath, String key) throws XmlValidationException, XmlReadException {
		String output = null;
		try {
			output = XmlUtil.readXmlFile(filePath);
		} catch (IOException e) {
			e.printStackTrace();
			logger.debug(e.getLocalizedMessage());
			throw new XmlReadException();
		}
		if (!XmlValidatorUtil.validateContents(output)) {
			logger.debug("XMl validation failed");
			logger.info("Test");
			throw new XmlValidationException();
		}
		pool.getResource().set(key, output);
		return output;
	}

	public String readXMLFileToString(String filePath) throws XmlReadException, XmlValidationException {
		return readXMLFileToString(filePath, "Default");
	}

	public String addEmployees(String key, String contents) throws XmlValidationException, XmlAddExployeeException {
		String xmlContents = pool.getResource().get(key);
		if (!XmlValidatorUtil.validateContents(contents)) {
			throw new XmlValidationException();
		}
		String output = null;
		try {
			output = XmlUtil.AddNewElementToXml(xmlContents, contents);
			pool.getResource().set(key, output);
		} catch (ParserConfigurationException | SAXException | IOException | TransformerException e) {
			e.printStackTrace();
			logger.debug(e.getLocalizedMessage());
			throw new XmlAddExployeeException();
		}

		return output;
	}

	public String removeEmployee(String param, String value, String key) throws XmlRemoveEmployeeException {
		String xmlContents = pool.getResource().get(key);
		String output = null;
		try {
			output = XmlUtil.removeAnElementBasedOnParam(param, value, xmlContents);
			pool.getResource().set(key, output);
		} catch (ParserConfigurationException | SAXException | IOException | TransformerException e) {
			e.printStackTrace();
			logger.debug(e.getLocalizedMessage());
			throw new XmlRemoveEmployeeException();
		}
		return output;
	}

	public String getOutputFile(String key) throws XmlWriteException {
		String xmlContents = pool.getResource().get(key);
		try {
			XmlUtil.writeContentToFile(service.getOutputPath(key) + File.separator + "output.xml", xmlContents);
		} catch (IOException e) {
			logger.debug(e.getLocalizedMessage());
			e.printStackTrace();
			throw new XmlWriteException();
		}
		return xmlContents;
	}

}
