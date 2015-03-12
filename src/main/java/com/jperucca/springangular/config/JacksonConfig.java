package com.jperucca.springangular.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Defaultly given by spring autoconfigure on spring boot using spring rest mvc
 * But declaring it for demo
 * Here we can add some custom configuration on ObjectMapper (Jackson default http serializer/deserializer)
 */
@Configuration
public class JacksonConfig {

	@Bean
	public ObjectMapper objectMapper() {
		return new ObjectMapper();
	}
}
