package xmlparser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.io.FileUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.xmlparser.interfaces.XMLParser;

public class XMLDomParser implements XMLParser{

	public String readXmlFile(String filepath) throws IOException {
		File input = new File(filepath);
		checkIfFileExists(input);
		String inputXml = FileUtils.readFileToString(input, StandardCharsets.UTF_8);
		return inputXml;
	}

	private void checkIfFileExists(File input) throws FileNotFoundException {
		if (!input.exists()) {
			throw new FileNotFoundException(input.getName() + " doesn't exist");
		}
	}

	public String writeContentToFile(String outputPath, String contents) throws IOException {
		File outputFile = new File(outputPath);
		FileUtils.writeStringToFile(outputFile, contents, StandardCharsets.UTF_8);
		return contents;
	}

	public String AddNewElementToXml(String fileContents, String contentsToAdd)
			throws ParserConfigurationException, SAXException, IOException, TransformerException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		InputSource is = new InputSource();
		is.setCharacterStream(new StringReader(fileContents));
		Document doc = builder.parse(is);

		InputSource contentStream = new InputSource();
		contentStream.setCharacterStream(new StringReader(contentsToAdd));
		Document content = builder.parse(contentStream);

		Element contentElement = content.getDocumentElement();
		String childNodeName = getFirstChildNodeName(contentElement);
		int newContentLength = contentElement.getElementsByTagName(childNodeName).getLength();
		NodeList elementsByTagName = contentElement.getElementsByTagName(childNodeName);
		for (int i = 0; i < newContentLength; i++) {
			Node item = elementsByTagName.item(i);
			Element employee = doc.createElement(childNodeName);
			for (int j = 0; j < item.getChildNodes().getLength(); j++) {

				if (item.getChildNodes().item(j).getNodeType() == Node.ELEMENT_NODE) {
					Element element = doc.createElement(item.getChildNodes().item(j).getNodeName());

					if (item.getChildNodes().item(j).getChildNodes().getLength() > 1) {
						parseChildNodes(element, item.getChildNodes().item(j), doc);
					} else {
						element.appendChild(doc.createTextNode(item.getChildNodes().item(j).getTextContent()));
					}
					employee.appendChild(element);
				}
			}
			doc.getDocumentElement().appendChild(employee);
		}

		return getXmlInString(doc);
	}

	private void parseChildNodes(Element element, Node item, Document doc) {
		for (int j = 0; j < item.getChildNodes().getLength(); j++) {
			Node itemNode = item.getChildNodes().item(j);
			if (itemNode.getNodeType() == Node.ELEMENT_NODE) {
				Element childElement = doc.createElement(itemNode.getNodeName());
				childElement.appendChild(doc.createTextNode(itemNode.getTextContent()));
				element.appendChild(childElement);
			}
		}

	}

	public String removeAnElementBasedOnParam(String param, String value, String xmlContents)
			throws ParserConfigurationException, SAXException, IOException, TransformerException {
		Document doc = getDocumentForXml(xmlContents);

		Element contentElement = doc.getDocumentElement();
		String childNodeName = getFirstChildNodeName(contentElement);

		int newContentLength = contentElement.getElementsByTagName(childNodeName).getLength();
		NodeList elementsByTagName = contentElement.getElementsByTagName(childNodeName);

		List<Node> itemsToRemove = new LinkedList<>();

		filterByStringParam(param, value, newContentLength, elementsByTagName, itemsToRemove);

		itemsToRemove.stream().forEach(item -> {
			doc.getDocumentElement().removeChild(item);
		});

		return getXmlInString(doc);
	}

	public String removeAnElementBasedOnCondition(String param, String xmlContents, int lowerbound,
			int upperbound) throws ParserConfigurationException, SAXException, IOException, TransformerException {
		Document doc = getDocumentForXml(xmlContents);

		Element contentElement = doc.getDocumentElement();
		String childNodeName = getFirstChildNodeName(contentElement);

		int newContentLength = contentElement.getElementsByTagName(childNodeName).getLength();
		NodeList elementsByTagName = contentElement.getElementsByTagName(childNodeName);

		List<Node> itemsToRemove = new LinkedList<>();

		String operation = "";
		if (lowerbound != 0 && upperbound != 0) {
			operation = "BETWEEN";
		} else if (lowerbound != 0) {
			operation = "LESSTHAN";
		} else if (upperbound != 0) {
			operation = "GREATERTHAN";
		}

		for (int i = 0; i < newContentLength; i++) {
			Node item = elementsByTagName.item(i);

			for (int j = 0; j < item.getChildNodes().getLength(); j++) {

				if (item.getChildNodes().item(j).getNodeType() == Node.ELEMENT_NODE) {

					if (item.getChildNodes().item(j).getNodeName().equals(param)) {
						String textContent = item.getChildNodes().item(j).getTextContent();
						boolean isTextContentInteger = checkIfTextContentIsInteger(textContent);
						if (isTextContentInteger) {
							int textContentValue = Integer.parseInt(textContent);
							if (operation.equals("BETWEEN")) {
								if ((textContentValue >= lowerbound) && (textContentValue <= upperbound)) {
									itemsToRemove.add(item);
								}
							} else if (operation.equals("LESSTHAN")) {
								if (textContentValue <= lowerbound) {
									itemsToRemove.add(item);
								}
							} else if (operation.equals("GREATERTHAN")) {
								if (textContentValue >= upperbound) {
									itemsToRemove.add(item);
								}
							}

						}
					}

				}
			}
		}

		itemsToRemove.stream().forEach(item -> {
			doc.getDocumentElement().removeChild(item);
		});

		return getXmlInString(doc);
	}

	private boolean checkIfTextContentIsInteger(String textContent) {
		try {
			Integer.parseInt(textContent);
		} catch (NumberFormatException ex) {
			return false;
		}
		return true;
	}

	private String getXmlInString(Document doc)
			throws TransformerFactoryConfigurationError, TransformerConfigurationException, TransformerException {
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);

		StringWriter writer = new StringWriter();
		StreamResult result = new StreamResult(writer);

		transformer.transform(source, result);
		return writer.toString();
	}

	private void filterByStringParam(String param, String value, int newContentLength,
			NodeList elementsByTagName, List<Node> itemsToRemove) {
		for (int i = 0; i < newContentLength; i++) {
			Node item = elementsByTagName.item(i);

			for (int j = 0; j < item.getChildNodes().getLength(); j++) {

				if (item.getChildNodes().item(j).getNodeType() == Node.ELEMENT_NODE) {

					if (item.getChildNodes().item(j).getNodeName().equals(param)
							&& item.getChildNodes().item(j).getTextContent().equals(value)) {
						itemsToRemove.add(item);
					}

				}
			}
		}
	}

	private String getFirstChildNodeName(Element contentElement) {
		String childNodeName = "";
		for (int i = 0; i < contentElement.getChildNodes().getLength(); i++) {
			if (contentElement.getChildNodes().item(i).getNodeType() == Node.ELEMENT_NODE) {
				childNodeName = contentElement.getChildNodes().item(i).getNodeName();
				break;
			}
		}

		return childNodeName;
	}

	private Document getDocumentForXml(String xmlContents)
			throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		InputSource is = new InputSource();
		is.setCharacterStream(new StringReader(xmlContents));
		Document doc = builder.parse(is);
		return doc;
	}

}
