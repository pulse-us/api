package gov.ca.emsa.pulse;

import org.springframework.boot.SpringApplication;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

@PropertySource("classpath:/application.properties")
@SpringBootApplication
@ComponentScan(basePackages = {"gov.ca.emsa.pulse.service.**", 
		"gov.ca.emsa.pulse.config.**", 
		"gov.ca.emsa.pulse.health.**"})

public class ServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceApplication.class, args);
	}

}
