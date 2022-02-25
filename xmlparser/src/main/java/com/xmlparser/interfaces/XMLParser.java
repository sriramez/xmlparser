package com.xmlparser.interfaces;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;

public interface XMLParser {

	public String readXmlFile(String filepath) throws IOException;

	public String writeContentToFile(String outputPath, String contents) throws IOException;

	public String AddNewElementToXml(String fileContents, String contentsToAdd)
			throws ParserConfigurationException, SAXException, IOException, TransformerException;

	public String removeAnElementBasedOnParam(String param, String value, String xmlContents)
			throws ParserConfigurationException, SAXException, IOException, TransformerException;

	public String removeAnElementBasedOnCondition(String param, String xmlContents, int lowerbound, int upperbound)
			throws ParserConfigurationException, SAXException, IOException, TransformerException;

}
