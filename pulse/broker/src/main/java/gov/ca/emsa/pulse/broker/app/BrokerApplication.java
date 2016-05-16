package gov.ca.emsa.pulse.broker.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages= {"gov.ca.emsa.pulse.broker.manager.**",
											"gov.ca.emsa.pulse.broker.**",
											"gov.ca.emsa.pulse.broker.dao.**",
											"gov.ca.emsa.pulse.broker.entity.**"})
@EnableScheduling
public class BrokerApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(BrokerApplication.class);
	}
	
}
