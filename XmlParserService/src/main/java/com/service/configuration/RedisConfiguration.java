package com.service.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import redis.clients.jedis.JedisPool;

@Configuration
public class RedisConfiguration {

	@Bean
	JedisPool pool() {
		return new JedisPool("localhost", 6379);
	}

}
