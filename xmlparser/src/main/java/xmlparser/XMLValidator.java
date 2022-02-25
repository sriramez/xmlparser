package xmlparser;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.xml.sax.SAXException;

public class XMLValidator {

	private Validator validator;
	private String contents;

	public XMLValidator(Builder builder) {
		this.contents = builder.contents;
		SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		try {
			Schema schema = schemaFactory.newSchema(new StreamSource(new StringReader(contents)));
			Validator schemaValidator = schema.newValidator();
			validator = schemaValidator;
		} catch (Exception e) {
			System.out.println(e.getLocalizedMessage());
		}

	}

	public boolean validateContents(String contents) {
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

	public static class Builder {
		private String contents;

		private Builder() {

		}

		public static Builder newInstance() {
			return new Builder();
		}

		public Builder setContents(String contents) {
			this.contents = contents;
			return this;
		}

		public XMLValidator build() {
			return new XMLValidator(this);
		}

	}

}
