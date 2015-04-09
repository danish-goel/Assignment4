package assignment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;

@EnableWebMvcSecurity
@SpringBootApplication
public class Assignment5Application {

    public static void main(String[] args) {
        SpringApplication.run(Assignment5Application.class, args);
    }
}
