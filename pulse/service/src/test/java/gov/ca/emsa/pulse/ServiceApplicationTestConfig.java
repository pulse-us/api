package gov.ca.emsa.pulse;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.TestPropertySource;

@PropertySource("classpath:/application-test.properties")
@SpringBootApplication(scanBasePackages={"gov.ca.emsa.pulse.service"})
@Configuration
public class ServiceApplicationTestConfig {}
