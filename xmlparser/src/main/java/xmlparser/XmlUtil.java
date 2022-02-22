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
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.io.FileUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class XmlUtil {

	public static String readXmlFile(String filepath) throws IOException {
		File input = new File(filepath);
		checkIfFileExists(input);
		String inputXml = FileUtils.readFileToString(input, StandardCharsets.UTF_8);
		return inputXml;
	}

	private static void checkIfFileExists(File input) throws FileNotFoundException {
		if (!input.exists()) {
			throw new FileNotFoundException(input.getName() + " doesn't exist");
		}
	}

	public static String loadXsdFileForValidation(String xsdFilePath) throws IOException {
		File xsd = new File(xsdFilePath);
		checkIfFileExists(xsd);
		return FileUtils.readFileToString(xsd, StandardCharsets.UTF_8);
	}

	public static String writeContentToFile(String outputPath, String contents) throws IOException {
		File outputFile = new File(outputPath);
		FileUtils.writeStringToFile(outputFile, contents, StandardCharsets.UTF_8);
		return contents;
	}

	public static String AddNewElementToXml(String fileContents, String contentsToAdd)
			throws ParserConfigurationException, SAXException, IOException, TransformerException {
		boolean isValid = XmlValidatorUtil.validateContents(contentsToAdd);
		if (!isValid) {
			throw new IOException("invalid xml");
		}
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		InputSource is = new InputSource();
		is.setCharacterStream(new StringReader(fileContents));
		Document doc = builder.parse(is);

		InputSource contentStream = new InputSource();
		contentStream.setCharacterStream(new StringReader(contentsToAdd));
		Document content = builder.parse(contentStream);

		Element contentElement = content.getDocumentElement();
		int newContentLength = contentElement.getElementsByTagName("employee").getLength();
		NodeList elementsByTagName = contentElement.getElementsByTagName("employee");
		for (int i = 0; i < newContentLength; i++) {
			Node item = elementsByTagName.item(i);
			Element employee = doc.createElement("employee");
			for (int j = 0; j < item.getChildNodes().getLength(); j++) {

				if (item.getChildNodes().item(j).getNodeType() == Node.ELEMENT_NODE) {
					Element element = doc.createElement(item.getChildNodes().item(j).getNodeName());
					
					if (item.getChildNodes().item(j).getChildNodes().getLength() > 1) {
						parseChildNodes(element, item.getChildNodes().item(j), doc);
					}
					else
					{
						element.appendChild(doc.createTextNode(item.getChildNodes().item(j).getTextContent()));
					}
					employee.appendChild(element);
				}
			}
			doc.getDocumentElement().appendChild(employee);
		}

		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);

		StringWriter writer = new StringWriter();
		StreamResult result = new StreamResult(writer);

		transformer.transform(source, result);
		return writer.toString();
	}

	private static void parseChildNodes(Element element, Node item, Document doc) {
		for (int j = 0; j < item.getChildNodes().getLength(); j++) {
			if (item.getChildNodes().item(j).getNodeType() == Node.ELEMENT_NODE) {
				Element childElement = doc.createElement(item.getChildNodes().item(j).getNodeName());
				childElement.appendChild(doc.createTextNode(item.getChildNodes().item(j).getTextContent()));
				element.appendChild(childElement);
			}
		}

	}

	public static String removeAnElementBasedOnParam(String param, String value, String xmlContents)
			throws ParserConfigurationException, SAXException, IOException, TransformerException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		InputSource is = new InputSource();
		is.setCharacterStream(new StringReader(xmlContents));
		Document doc = builder.parse(is);

		Element contentElement = doc.getDocumentElement();
		int newContentLength = contentElement.getElementsByTagName("employee").getLength();
		NodeList elementsByTagName = contentElement.getElementsByTagName("employee");
		List<Node> itemsToRemove = new LinkedList<>();
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

		itemsToRemove.stream().forEach(item -> {
			doc.getDocumentElement().removeChild(item);
		});

		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);

		StringWriter writer = new StringWriter();
		StreamResult result = new StreamResult(writer);

		transformer.transform(source, result);
		return writer.toString();
	}

}
