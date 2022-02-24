package com.service.configuration;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;

import xmlparser.XMLValidator;

@Configuration
public class ValidatorConfiguration {

	@Bean
	public Map<String, XMLValidator> validators() {
		return new HashMap<>();
	}

	@Bean
	ObjectMapper mapper() {
		return new ObjectMapper();
	}
}
