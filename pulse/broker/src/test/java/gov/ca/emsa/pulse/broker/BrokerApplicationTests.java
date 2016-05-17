package gov.ca.emsa.pulse.broker;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import gov.ca.emsa.pulse.broker.app.BrokerApplication;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = BrokerApplication.class)
public class BrokerApplicationTests extends AbstractTransactionalJUnit4SpringContextTests {

	@Test
	public void contextLoads() {
	}
	
	@Test
	public void getDirectoriesTest(){
		assertEquals(1, super.countRowsInTable("organization"));
	}

}