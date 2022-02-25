package xmlparser;

import com.parser.exceptions.UnsupportedParserException;
import com.xmlparser.interfaces.XMLParser;

public class XMLParserFactory {

	public XMLParser getParser(String parser) throws UnsupportedParserException {
		if (parser.equals("DOMParser")) {
			return new XMLDomParser();
		}
		throw new UnsupportedParserException();
	}

}
