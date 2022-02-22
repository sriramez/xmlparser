package xmlparser;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;

public class Main {

	public static void main(String[] args)
			throws IOException, ParserConfigurationException, SAXException, TransformerException {
		String input = XmlUtil.readXmlFile("C:\\Users\\sriram\\Desktop\\input.xml");
		XmlValidatorUtil.setValidator("C:\\Users\\sriram\\Desktop\\employee_validator.xsd");
		//XmlValidatorUtil.setValidator("C:\\Users\\sriram\\Desktop\\employee_validator_new.xsd");
		String contents = XmlUtil.readXmlFile("C:\\Users\\sriram\\Desktop\\emp_add.xml");
		String output = XmlUtil.AddNewElementToXml(input,contents);
		//String removeOutput = XmlUtil.removeAnElementBasedOnParam("age", "40", output);
		System.out.println(output);
		XmlUtil.writeContentToFile("C:\\Users\\sriram\\Desktop\\output.xml", output);
	}

}
