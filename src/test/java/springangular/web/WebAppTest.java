package springangular.web;

import com.jayway.restassured.RestAssured;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import springangular.SpringangularApplication;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SpringangularApplication.class)
@WebAppConfiguration
@IntegrationTest("server.port:${local.http.port}")
@Ignore
public class WebAppTest {
    
    @Value("${local.http.port}")
    private int localPort;
    
    /**
     * Do not override this method in your test,
     * otherwise you'll get a Connection refused
     */
    @Before
    public void initialSetup() {
        RestAssured.port = localPort;
    }
}
