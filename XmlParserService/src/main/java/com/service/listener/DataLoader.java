package com.service.listener;

import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.service.constants.XMLParserConstants;

import xmlparser.XMLValidator;

@Component
public class DataLoader {

	@Autowired
	RedisTemplate<String, Object> redis;

	@Autowired
	Map<String, XMLValidator> validators;

	@PostConstruct
	public void loadData() {
		redis.opsForHash().keys(XMLParserConstants.APP_VALILDATOR_KEY).stream()
				.filter(key -> String.valueOf(key).endsWith("_validator")).forEach(key -> {
					String validatorKey = key.toString().split("_validator")[0];
					String validatorContents = (String) redis.opsForHash().get(XMLParserConstants.APP_KEY, key);
					XMLValidator validator = XMLValidator.Builder.newInstance().setContents(validatorContents).build();
					validators.put(validatorKey, validator);
				});
	}

}
