package gov.ca.emsa.pulse.broker;

import java.util.List;
import java.util.ArrayList;
import java.util.List;
import junit.framework.TestCase;
import gov.ca.emsa.pulse.broker.domain.Organization;
import gov.ca.emsa.pulse.broker.dto.OrganizationDTO;
import gov.ca.emsa.pulse.broker.manager.OrganizationManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import gov.ca.emsa.pulse.broker.domain.Organization;
import gov.ca.emsa.pulse.broker.dto.OrganizationDTO;
import gov.ca.emsa.pulse.broker.manager.OrganizationManager;
import junit.framework.TestCase;


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
	public void createDirectoryCacheTest(){
		ArrayList<Organization> orgs = new ArrayList<Organization>();
		Organization org1 = new Organization();
		org1.setOrganizationId((long) 1);
		org1.setName("OrganizationOne");
		org1.setAdapter("eHealth");
		org1.setIpAddress("127.0.0.1");
		org1.setActive(true);
		org1.setUsername("org1User");
		org1.setPassword("password1");
		orgs.add(org1);
		Organization org2 = new Organization();
		org2.setOrganizationId((long) 2);
		org2.setName("OrganizationTwo");
		org2.setAdapter("eHealth");
		org2.setIpAddress("127.0.0.1");
		org2.setActive(true);
		org2.setCertificationKey("1234567");
		orgs.add(org2);
		Organization org3 = new Organization();
		org3.setOrganizationId((long) 3);
		org3.setName("OrganizationThree");
		org3.setAdapter("eHealth");
		org3.setIpAddress("127.0.0.1");
		org3.setActive(true);
		org3.setUsername("org3User");
		org3.setPassword("password3");
		orgs.add(org3);
		organizationManager.updateOrganizations(orgs);
		List<OrganizationDTO> orgDTO = organizationManager.getAll();
		assertEquals(3, orgDTO.size());
	}
	
	@Test
	public void removeOrgDirectoryCacheTest(){
		ArrayList<Organization> orgs = new ArrayList<Organization>();
		Organization org1 = new Organization();
		org1.setOrganizationId((long) 1);
		org1.setName("OrganizationOne");
		org1.setAdapter("eHealth");
		org1.setIpAddress("127.0.0.1");
		org1.setActive(true);
		org1.setUsername("org1User");
		org1.setPassword("password1");
		orgs.add(org1);
		Organization org2 = new Organization();
		org2.setOrganizationId((long) 2);
		org2.setName("OrganizationTwo");
		org2.setAdapter("eHealth");
		org2.setIpAddress("127.0.0.1");
		org2.setActive(true);
		org2.setCertificationKey("1234567");
		orgs.add(org2);
		Organization org3 = new Organization();
		org3.setOrganizationId((long) 3);
		org3.setName("OrganizationThree");
		org3.setAdapter("eHealth");
		org3.setIpAddress("127.0.0.1");
		org3.setActive(true);
		org3.setUsername("org3User");
		org3.setPassword("password3");
		orgs.add(org3);
		organizationManager.updateOrganizations(orgs);
		List<OrganizationDTO> orgDTO1 = organizationManager.getAll();
		assertEquals(3, orgDTO1.size());
		orgs.remove(orgs.size()-1);
		organizationManager.updateOrganizations(orgs);
		List<OrganizationDTO> orgDTO2 = organizationManager.getAll();
		assertEquals(2, orgDTO2.size());
	}
	
	@Test
	public void updateOrgDirectoryCacheTest(){
		ArrayList<Organization> orgs = new ArrayList<Organization>();
		Organization org1 = new Organization();
		org1.setOrganizationId((long) 1);
		org1.setName("OrganizationOne");
		org1.setAdapter("eHealth");
		org1.setIpAddress("127.0.0.1");
		org1.setActive(true);
		org1.setUsername("org1User");
		org1.setPassword("password1");
		orgs.add(org1);
		Organization org2 = new Organization();
		org2.setOrganizationId((long) 2);
		org2.setName("OrganizationTwo");
		org2.setAdapter("eHealth");
		org2.setIpAddress("127.0.0.1");
		org2.setActive(true);
		org2.setCertificationKey("1234567");
		orgs.add(org2);
		Organization org3 = new Organization();
		org3.setOrganizationId((long) 3);
		org3.setName("OrganizationThree");
		org3.setAdapter("eHealth");
		org3.setIpAddress("127.0.0.1");
		org3.setActive(true);
		org3.setUsername("org3User");
		org3.setPassword("password3");
		orgs.add(org3);
		organizationManager.updateOrganizations(orgs);
		List<OrganizationDTO> orgDTO1 = organizationManager.getAll();
		assertEquals(3, orgDTO1.size());
		orgs.get(0).setName("OrganizationOneUpdated");
		organizationManager.updateOrganizations(orgs);
		List<OrganizationDTO> orgDTO2 = organizationManager.getAll();
		for(OrganizationDTO orgdto: orgDTO2){
			if(orgdto.getId() == 1){
				assertEquals("OrganizationOneUpdated", orgdto.getName());
			}
		}
	}

}