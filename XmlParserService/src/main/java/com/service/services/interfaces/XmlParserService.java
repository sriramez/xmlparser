package com.service.services.interfaces;

import com.service.exception.XmlAddExployeeException;
import com.service.exception.XmlReadException;
import com.service.exception.XmlRemoveEmployeeException;
import com.service.exception.XmlValidationException;
import com.service.exception.XmlValidatorNotPresentException;
import com.service.exception.XmlWriteException;

import xmlparser.XMLValidator;

public interface XmlParserService {

	String readXMLFileToString(String filePath, String key)
			throws XmlValidationException, XmlReadException, XmlValidatorNotPresentException;

	String readXMLFileToString(String filePath)
			throws XmlReadException, XmlValidationException, XmlValidatorNotPresentException;

	String addElements(String key, String contents)
			throws XmlValidationException, XmlAddExployeeException, XmlValidatorNotPresentException;

	String removeElements(String param, String value, String key) throws XmlRemoveEmployeeException;

	String getOutputFile(String key) throws XmlWriteException;

	String loadValidator(String absolutePath, String key) throws XmlValidationException;

	public XMLValidator getValidator(String key) throws XmlValidatorNotPresentException;

	String removeElements(String value, String key, int lowerbound, int upperbound) throws XmlRemoveEmployeeException;

}
