package gov.ca.emsa.pulse.broker;

import gov.ca.emsa.pulse.broker.domain.Organization;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class BrokerApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(BrokerApplication.class);
	}

	@Override
	public void run(String... args) throws Exception {
		RestTemplate restTemplate = new RestTemplate();
		Organization[] orgs = restTemplate.getForObject("http://localhost:8090/mock/directory", Organization[].class);
		for(Organization org: orgs){
			System.out.println(org.toString());
		}
	}
}
