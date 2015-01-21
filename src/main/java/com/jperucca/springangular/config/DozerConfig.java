package com.jperucca.springangular.config;

import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class DozerConfig {

    @Bean
    public Mapper dozerBeanMapper() {
        return new DozerBeanMapper(Arrays.asList("dozer-mapping.xml"));
    }
}
