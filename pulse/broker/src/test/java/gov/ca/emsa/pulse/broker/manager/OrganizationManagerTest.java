package gov.ca.emsa.pulse.broker.manager;

import gov.ca.emsa.pulse.broker.BrokerApplicationTestConfig;
import gov.ca.emsa.pulse.broker.dto.LocationDTO;
import gov.ca.emsa.pulse.common.domain.Location;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={BrokerApplicationTestConfig.class})
public class OrganizationManagerTest extends TestCase {
	
	@Autowired
	private OrganizationManager organizationManager;

	@Test
	public void contextLoads() {
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void createDirectoryCacheTest(){
		ArrayList<Location> orgs = new ArrayList<Location>();
		Location org1 = new Location();
		org1.setOrganizationId((long) 1);
		org1.setName("OrganizationOne");
		org1.setAdapter("eHealth");
		org1.setIpAddress("127.0.0.1");
		org1.setActive(true);
		org1.setUsername("org1User");
		org1.setPassword("password1");
		orgs.add(org1);
		Location org2 = new Location();
		org2.setOrganizationId((long) 2);
		org2.setName("OrganizationTwo");
		org2.setAdapter("eHealth");
		org2.setIpAddress("127.0.0.1");
		org2.setActive(true);
		org2.setCertificationKey("1234567");
		orgs.add(org2);
		Location org3 = new Location();
		org3.setOrganizationId((long) 3);
		org3.setName("OrganizationThree");
		org3.setAdapter("eHealth");
		org3.setIpAddress("127.0.0.1");
		org3.setActive(true);
		org3.setUsername("org3User");
		org3.setPassword("password3");
		orgs.add(org3);
		organizationManager.updateOrganizations(orgs);
		List<LocationDTO> orgDTO = organizationManager.getAll();
		assertEquals(3, orgDTO.size());
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void removeOrgDirectoryCacheTest(){
		ArrayList<Location> orgs = new ArrayList<Location>();
		Location org1 = new Location();
		org1.setOrganizationId((long) 1);
		org1.setName("OrganizationOne");
		org1.setAdapter("eHealth");
		org1.setIpAddress("127.0.0.1");
		org1.setActive(true);
		org1.setUsername("org1User");
		org1.setPassword("password1");
		orgs.add(org1);
		Location org2 = new Location();
		org2.setOrganizationId((long) 2);
		org2.setName("OrganizationTwo");
		org2.setAdapter("eHealth");
		org2.setIpAddress("127.0.0.1");
		org2.setActive(true);
		org2.setCertificationKey("1234567");
		orgs.add(org2);
		Location org3 = new Location();
		org3.setOrganizationId((long) 3);
		org3.setName("OrganizationThree");
		org3.setAdapter("eHealth");
		org3.setIpAddress("127.0.0.1");
		org3.setActive(true);
		org3.setUsername("org3User");
		org3.setPassword("password3");
		orgs.add(org3);
		organizationManager.updateOrganizations(orgs);
		List<LocationDTO> orgDTO1 = organizationManager.getAll();
		assertEquals(3, orgDTO1.size());
		orgs.remove(orgs.size()-1);
		organizationManager.updateOrganizations(orgs);
		List<LocationDTO> orgDTO2 = organizationManager.getAll();
		assertEquals(2, orgDTO2.size());
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void updateOrgDirectoryCacheTest(){
		ArrayList<Location> orgs = new ArrayList<Location>();
		Location org1 = new Location();
		org1.setOrganizationId((long) 1);
		org1.setName("OrganizationOne");
		org1.setAdapter("eHealth");
		org1.setIpAddress("127.0.0.1");
		org1.setActive(true);
		org1.setUsername("org1User");
		org1.setPassword("password1");
		orgs.add(org1);
		Location org2 = new Location();
		org2.setOrganizationId((long) 2);
		org2.setName("OrganizationTwo");
		org2.setAdapter("eHealth");
		org2.setIpAddress("127.0.0.1");
		org2.setActive(true);
		org2.setCertificationKey("1234567");
		orgs.add(org2);
		Location org3 = new Location();
		org3.setOrganizationId((long) 3);
		org3.setName("OrganizationThree");
		org3.setAdapter("eHealth");
		org3.setIpAddress("127.0.0.1");
		org3.setActive(true);
		org3.setUsername("org3User");
		org3.setPassword("password3");
		orgs.add(org3);
		organizationManager.updateOrganizations(orgs);
		List<LocationDTO> orgDTO1 = organizationManager.getAll();
		assertEquals(3, orgDTO1.size());
		orgs.get(0).setName("OrganizationOneUpdated");
		organizationManager.updateOrganizations(orgs);
		List<LocationDTO> orgDTO2 = organizationManager.getAll();
		for(LocationDTO orgdto: orgDTO2){
			if(orgdto.getId() == 1){
				assertEquals("OrganizationOneUpdated", orgdto.getName());
			}
		}
	}

}