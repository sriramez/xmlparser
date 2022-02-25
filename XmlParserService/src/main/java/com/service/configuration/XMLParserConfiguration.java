package com.service.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.parser.exceptions.UnsupportedParserException;
import com.xmlparser.interfaces.XMLParser;

import xmlparser.XMLParserFactory;

@Configuration
public class XMLParserConfiguration {

	@Value("${xmlservice.parser}")
	private String parser;

	@Bean
	XMLParser parser() throws UnsupportedParserException {
		return new XMLParserFactory().getParser(parser);
	}

}
