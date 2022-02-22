package xmlparser;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class XmlValidatorUtil {

	private static Validator validator = null;

	public static Validator setValidator(String filePath) {

		SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		try {
			Schema schema = schemaFactory.newSchema(new File(filePath));
			Validator schemaValidator = schema.newValidator();
			validator = schemaValidator;
		} catch (Exception e) {
			System.out.println(e.getLocalizedMessage());
		}

		return validator;
	}

	public static boolean validate(String xmlPath) {
		boolean toRet = false;
		try {
			if (validator != null) {
				validator.validate(new StreamSource(new File(xmlPath)));
				toRet = true;
			}

		} catch (SAXException | IOException e) {
			return toRet;
		}
		return toRet;
	}

	public static boolean validateContents(String contents) {
		boolean toRet = false;
		try {
			if (validator != null) {
				validator.validate(new StreamSource(new StringReader(contents)));
				toRet = true;
			}

		} catch (SAXException | IOException e) {
			return false;
		}
		return toRet;
	}

}
