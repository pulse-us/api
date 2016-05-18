package gov.ca.emsa.pulse.broker;

import static org.junit.Assert.*;
import java.util.List;

import junit.framework.TestCase;
import gov.ca.emsa.pulse.broker.app.BrokerApplication;
import gov.ca.emsa.pulse.broker.domain.Organization;
import gov.ca.emsa.pulse.broker.dto.OrganizationDTO;
import gov.ca.emsa.pulse.broker.manager.OrganizationManager;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.github.springtestdbunit.annotation.DatabaseSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={BrokerApplicationTestConfig.class})
//@DatabaseSetup("classpath:data/testData.xml")
public class BrokerApplicationTests extends TestCase {
	
	@Autowired
	private OrganizationManager organizationManager;

	@Test
	public void contextLoads() {
	}
	
	@Test
	public void createDirectoryTest(){
		Organization[] orgs = new Organization[3];
		Organization org1 = new Organization();
		org1.setId((long) 1);
		org1.setName("OrganizationOne");
		orgs[0] = org1;
		Organization org2 = new Organization();
		org2.setId((long) 2);
		org2.setName("OrganizationTwo");
		orgs[1] = org2;
		Organization org3 = new Organization();
		org3.setId((long) 3);
		org3.setName("OrganizationThree");
		orgs[2] = org3;
		organizationManager.updateOrganizations(orgs);
		List<OrganizationDTO> orgDTO = organizationManager.getAll();
		assertEquals(3, orgDTO.size());
	}
}