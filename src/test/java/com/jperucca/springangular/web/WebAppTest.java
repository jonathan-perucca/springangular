package com.jperucca.springangular.web;

import com.jayway.restassured.RestAssured;
import com.jperucca.springangular.SpringangularApplication;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static com.jperucca.springangular.config.Profiles.DEV;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SpringangularApplication.class)
@WebIntegrationTest
@ActiveProfiles(DEV)
public abstract class WebAppTest {
    
    @Value("${local.http.baseuri}")
    private String baseURI;
    
    @Value("${server.port}")
    private int localPort;
    
    /**
     * Do not override this method in your test,
     * otherwise you'll get a Connection refused
     */
    @Before
    public void initialSetup() {
        RestAssured.baseURI = baseURI;
        RestAssured.port = localPort;
    }
}
