package gov.ca.emsa.pulse.broker;

import gov.ca.emsa.pulse.broker.domain.Organization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.web.client.RestTemplate;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication
public class BrokerApplication implements CommandLineRunner {
	
	@Autowired
    JdbcTemplate jdbcTemplate;

	public static void main(String[] args) {
		SpringApplication.run(BrokerApplication.class);
	}

	@Override
	public void run(String... args) throws Exception {
		RestTemplate restTemplate = new RestTemplate();
		Organization[] orgs = restTemplate.getForObject("http://localhost:8090/mock/directory", Organization[].class);
		for(Organization org: orgs){
			jdbcTemplate.execute("INSERT into organization (name) VALUES ('" + org.getName() +"')");
		}
	}
}
