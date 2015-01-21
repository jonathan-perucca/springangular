package com.jperucca.springangular;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({
        "com.jperucca.springangular.web", 
        "com.jperucca.springangular.repository", 
        "com.jperucca.springangular.config"
})
@EnableAutoConfiguration
public class SpringangularApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringangularApplication.class, args);
    }
}
