package com.service.configuration;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.GenericToStringSerializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.constants.XMLParserConstants;
import com.service.model.RedisMessageModel;

import xmlparser.XMLValidator;

@Configuration
public class RedisConfiguration {

	@Autowired
	Map<String, XMLValidator> validators;

	@Autowired
	ObjectMapper mapper;

	@Bean
	JedisConnectionFactory jedisConnectionFactory() {
		return new JedisConnectionFactory();
	}

	@Bean
	public RedisTemplate<String, Object> redisTemplate() {
		final RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
		template.setConnectionFactory(jedisConnectionFactory());
		template.setValueSerializer(new GenericToStringSerializer<Object>(Object.class));
		return template;
	}

	@Bean
	RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory,
			MessageListenerAdapter listenerAdapter) {

		RedisMessageListenerContainer container = new RedisMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.addMessageListener(listenerAdapter, new PatternTopic(XMLParserConstants.VALIDATORS));

		return container;
	}

	@Bean
	MessageListenerAdapter listenerAdapter(RedisReceiver receiver) {
		return new MessageListenerAdapter(receiver, XMLParserConstants.LISTENER_METHOD);
	}

	@Bean
	RedisReceiver receiver() {
		return new RedisReceiver();
	}

	public class RedisReceiver {

		public void receiveMessage(String message) throws JsonMappingException, JsonProcessingException {
			RedisMessageModel model = mapper.readValue(message, RedisMessageModel.class);
			XMLValidator validator = XMLValidator.Builder.newInstance().setContents(model.getValidatorContents())
					.build();
			validators.put(model.getKey(), validator);
		}

	}

}
