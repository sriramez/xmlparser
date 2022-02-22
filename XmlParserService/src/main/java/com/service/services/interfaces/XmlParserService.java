package com.service.services.interfaces;

import com.service.exception.XmlAddExployeeException;
import com.service.exception.XmlReadException;
import com.service.exception.XmlRemoveEmployeeException;
import com.service.exception.XmlValidationException;
import com.service.exception.XmlWriteException;

public interface XmlParserService {

	String readXMLFileToString(String filePath, String key) throws XmlValidationException, XmlReadException;

	String readXMLFileToString(String filePath) throws XmlReadException, XmlValidationException;

	String addEmployees(String key, String contents) throws XmlValidationException, XmlAddExployeeException;

	String removeEmployee(String param, String value, String key) throws XmlRemoveEmployeeException;

	String getOutputFile(String key) throws XmlWriteException;

}
