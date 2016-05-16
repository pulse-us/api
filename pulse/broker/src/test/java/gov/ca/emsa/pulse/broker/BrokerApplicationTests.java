package gov.ca.emsa.pulse.broker;

import static org.junit.Assert.*;
import gov.ca.emsa.pulse.broker.app.BrokerApplication;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = BrokerApplication.class)
@WebAppConfiguration
@EnableAutoConfiguration
@PropertySource("classpath:application.properties")
public class BrokerApplicationTests extends AbstractTransactionalJUnit4SpringContextTests {

	@Test
	public void contextLoads() {
	}
	
	@Test
	public void getDirectoriesTest(){
		assertEquals(super.countRowsInTable("organization"), 3);
	}

}