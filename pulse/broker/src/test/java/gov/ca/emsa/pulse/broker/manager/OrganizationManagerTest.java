package gov.ca.emsa.pulse.broker.manager;

import gov.ca.emsa.pulse.broker.BrokerApplicationTestConfig;
import gov.ca.emsa.pulse.broker.dto.LocationDTO;
import gov.ca.emsa.pulse.broker.dto.EndpointDTO;
import gov.ca.emsa.pulse.common.domain.Endpoint;
import gov.ca.emsa.pulse.common.domain.EndpointMimeType;
import gov.ca.emsa.pulse.common.domain.EndpointStatus;
import gov.ca.emsa.pulse.common.domain.EndpointType;
import gov.ca.emsa.pulse.common.domain.Location;
import gov.ca.emsa.pulse.common.domain.LocationStatus;

import java.util.ArrayList;
import java.util.Date;
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
	private LocationManager locationManager;

	@Test
	public void contextLoads() {
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void createDirectoryCacheTest(){
		LocationStatus locStatus = new LocationStatus();
		locStatus.setId(1L);
		
		ArrayList<Location> locations = new ArrayList<Location>();
		Location location1 = new Location();
		location1.setExternalId("1");
		location1.setName("John's Hopkins Medical Center");
		location1.setDescription("A hospital");
		location1.setType("Hospital");
		location1.setExternalLastUpdateDate(new Date());
		location1.setParentOrgName("EHealth Parent Org");
		location1.setStatus(locStatus);
		locations.add(location1);
		
		Location location2 = new Location();
		location2.setExternalId("2");
		location2.setName("University of Maryland Medical Center");
		location2.setDescription("A hospital");
		location2.setType("Hospital");
		location2.setExternalLastUpdateDate(new Date());
		location2.setParentOrgName("EHealth Parent Org");
		location2.setStatus(locStatus);
		locations.add(location2);
		
		Location location3 = new Location();
		location3.setExternalId("3");
		location3.setName("University of Maryland Medical Service");
		location3.setDescription("A building");
		location3.setType("Hospital");
		location3.setExternalLastUpdateDate(new Date());
		location3.setParentOrgName("EHealth Parent Org");
		location3.setStatus(locStatus);
		Endpoint endpoint = new Endpoint();
		endpoint.setAdapter("eHealth");
		EndpointStatus status = new EndpointStatus();
		status.setName("Active");
		endpoint.setEndpointStatus(status);
		EndpointType type = new EndpointType();
		type.setCode("nwhin-xcpd");
		endpoint.setEndpointType(type);
		endpoint.setExternalId("1");
		endpoint.setExternalLastUpdateDate(new Date());
		EndpointMimeType mimeType = new EndpointMimeType();
		mimeType.setMimeType("application/xml");
		endpoint.getMimeTypes().add(mimeType);
		endpoint.setPayloadType("HL7 CCDA");
		endpoint.setPublicKey("lkdskdshsfdiujjksjewjfdsfdsjfdsfdsfds");
		endpoint.setUrl("http://www.google.com");
		location3.getEndpoints().add(endpoint);
		locations.add(location3);
		
		locationManager.updateLocations(locations);
		List<LocationDTO> locationResults = locationManager.getAll();
		assertEquals(3, locationResults.size());
		
		//make sure one location has an endpoint
		boolean foundEndpoint = false;
		for(LocationDTO loc : locationResults) {
			if(loc.getEndpoints() != null && loc.getEndpoints().size() == 1) {
				foundEndpoint = true;
				
				EndpointDTO locEndpoint = loc.getEndpoints().get(0);
				assertNotNull(locEndpoint);
				assertEquals(1, locEndpoint.getMimeTypes().size());
				assertEquals("application/xml", locEndpoint.getMimeTypes().get(0).getMimeType());
			}
		}
		assertTrue(foundEndpoint);
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void removeLocationDirectoryCacheTest(){
		LocationStatus locStatus = new LocationStatus();
		locStatus.setId(1L);
		
		ArrayList<Location> locations = new ArrayList<Location>();
		Location location1 = new Location();
		location1.setExternalId("1");
		location1.setName("John's Hopkins Medical Center");
		location1.setDescription("A hospital");
		location1.setType("Hospital");
		location1.setExternalLastUpdateDate(new Date());
		location1.setParentOrgName("EHealth Parent Org");
		location1.setStatus(locStatus);
		locations.add(location1);
		
		Location location2 = new Location();
		location2.setExternalId("2");
		location2.setName("University of Maryland Medical Center");
		location2.setDescription("A hospital");
		location2.setType("Hospital");
		location2.setExternalLastUpdateDate(new Date());
		location2.setParentOrgName("EHealth Parent Org");
		location2.setStatus(locStatus);
		locations.add(location2);
		
		Location location3 = new Location();
		location3.setExternalId("3");
		location3.setName("University of Maryland Medical Service");
		location3.setDescription("A building");
		location3.setType("Hospital");
		location3.setExternalLastUpdateDate(new Date());
		location3.setParentOrgName("EHealth Parent Org");
		location3.setStatus(locStatus);
		Endpoint endpoint = new Endpoint();
		endpoint.setAdapter("eHealth");
		EndpointStatus status = new EndpointStatus();
		status.setName("Active");
		endpoint.setEndpointStatus(status);
		EndpointType type = new EndpointType();
		type.setName("Patient Discovery");
		endpoint.setEndpointType(type);
		endpoint.setExternalId("1");
		endpoint.setExternalLastUpdateDate(new Date());
		EndpointMimeType mimeType = new EndpointMimeType();
		mimeType.setMimeType("application/xml");
		endpoint.getMimeTypes().add(mimeType);
		endpoint.setPayloadType("HL7 CCDA");
		endpoint.setPublicKey("lkdskdshsfdiujjksjewjfdsfdsjfdsfdsfds");
		endpoint.setUrl("http://www.google.com");
		location3.getEndpoints().add(endpoint);
		locations.add(location3);
		
		locationManager.updateLocations(locations);
		List<LocationDTO> locationResults = locationManager.getAll();
		assertEquals(3, locationResults.size());
		locations.remove(0);
		locationManager.updateLocations(locations);
		locationResults = locationManager.getAll();
		assertEquals(2, locationResults.size());
	}
}