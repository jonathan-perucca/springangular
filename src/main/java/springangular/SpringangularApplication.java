package springangular;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({"springangular.web", "springangular.repository", "springangular.config"})
@EnableAutoConfiguration
public class SpringangularApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringangularApplication.class, args);
    }
}
