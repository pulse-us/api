package gov.ca.emsa.pulse.broker.manager;

import gov.ca.emsa.pulse.broker.BrokerApplicationTestConfig;
import gov.ca.emsa.pulse.broker.dto.LocationDTO;
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
public class LocationManagerTest extends TestCase {
	
	@Autowired private LocationManager locationManager;

	@Test
	@Transactional
	@Rollback(true)
	public void createLocationsTest(){
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
		locations.add(location3);
		
		locationManager.updateLocations(locations);
		
		List<LocationDTO> allLocations = locationManager.getAll();
		assertEquals(3, allLocations.size());
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void updateLocationsRemoveOneTest(){
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