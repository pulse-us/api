package gov.ca.emsa.pulse.broker;

import static org.junit.Assert.*;
import gov.ca.emsa.pulse.broker.BrokerApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = BrokerApplication.class)
@WebAppConfiguration
public class BrokerApplicationTests extends AbstractTransactionalJUnit4SpringContextTests {

	@Test
	public void contextLoads() {
	}
	
	@Test
	public void getDirectoriesTest(){
		assertEquals(super.countRowsInTable("organization"), 3);
	}

}