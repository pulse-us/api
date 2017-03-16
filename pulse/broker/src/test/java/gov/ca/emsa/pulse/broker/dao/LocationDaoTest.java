package gov.ca.emsa.pulse.broker.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import gov.ca.emsa.pulse.broker.BrokerApplicationTestConfig;
import gov.ca.emsa.pulse.broker.dto.AddressLineDTO;
import gov.ca.emsa.pulse.broker.dto.LocationDTO;
import gov.ca.emsa.pulse.broker.dto.LocationStatusDTO;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={BrokerApplicationTestConfig.class})
public class LocationDaoTest {
	@Autowired LocationDAO locationDao;
	
	@Test
	@Transactional
	@Rollback(true)
	public void createLocation() throws SQLException {
		LocationDTO toCreate = new LocationDTO();
		toCreate.setCity("Baltimore");
		toCreate.setDescription("A test location");
		toCreate.setExternalId("001");
		toCreate.setExternalLastUpdateDate(new Date());
		AddressLineDTO toCreateLine = new AddressLineDTO();
		toCreateLine.setLine("1000 Hilltop Circle");
		toCreate.getLines().add(toCreateLine);
		toCreate.setName("Test Hospital");
		toCreate.setParentOrgName("TEST ORG");
		toCreate.setState("MD");
		LocationStatusDTO status = new LocationStatusDTO();
		status.setName("Active");
		toCreate.setStatus(status);
		toCreate.setType("Hospital");
		toCreate.setZipcode("21227");
		
		LocationDTO created = locationDao.create(toCreate);

		LocationDTO queried = locationDao.findById(created.getId());
		compareLocations(created, queried);
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void createAndUpdateLocationFields() throws SQLException {
		LocationDTO toCreate = new LocationDTO();
		toCreate.setCity("Baltimore");
		toCreate.setDescription("A test location");
		toCreate.setExternalId("001");
		toCreate.setExternalLastUpdateDate(new Date());
		AddressLineDTO toCreateLine = new AddressLineDTO();
		toCreateLine.setLine("1000 Hilltop Circle");
		toCreate.getLines().add(toCreateLine);
		toCreate.setName("Test Hospital");
		toCreate.setParentOrgName("TEST ORG");
		toCreate.setState("MD");
		LocationStatusDTO status = new LocationStatusDTO();
		status.setName("Active");
		toCreate.setStatus(status);
		toCreate.setType("Hospital");
		toCreate.setZipcode("21227");
		
		LocationDTO toUpdate = locationDao.create(toCreate);
		
		toUpdate.setName("Updated Test Hospital");
		toUpdate.setCity("Updated Baltimore");
		toUpdate.setState("PA");
		toUpdate.setZipcode("21229");
		toUpdate.setParentOrgName("UPDATED TEST ORG");
		status = new LocationStatusDTO();
		status.setName("Off");
		toUpdate.setStatus(status);
		
		LocationDTO updated = locationDao.update(toUpdate);
		assertEquals(toUpdate.getId(), updated.getId());
		LocationDTO queried = locationDao.findById(updated.getId());
		compareLocations(updated, queried);
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void createAndUpdateLocationClearAddressLines() throws SQLException {
		LocationDTO toCreate = new LocationDTO();
		toCreate.setCity("Baltimore");
		toCreate.setDescription("A test location");
		toCreate.setExternalId("001");
		toCreate.setExternalLastUpdateDate(new Date());
		AddressLineDTO toCreateLine = new AddressLineDTO();
		toCreateLine.setLine("1000 Hilltop Circle");
		toCreate.getLines().add(toCreateLine);
		toCreate.setName("Test Hospital");
		toCreate.setParentOrgName("TEST ORG");
		toCreate.setState("MD");
		LocationStatusDTO status = new LocationStatusDTO();
		status.setName("Active");
		toCreate.setStatus(status);
		toCreate.setType("Hospital");
		toCreate.setZipcode("21227");
		
		LocationDTO toUpdate = locationDao.create(toCreate);
		toUpdate.getLines().clear();
		
		LocationDTO updated = locationDao.update(toUpdate);
		assertEquals(toUpdate.getId(), updated.getId());
		LocationDTO queried = locationDao.findById(updated.getId());
		compareLocations(updated, queried);
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void createAndUpdateLocationChangeAddressLines() throws SQLException {
		LocationDTO toCreate = new LocationDTO();
		toCreate.setCity("Baltimore");
		toCreate.setDescription("A test location");
		toCreate.setExternalId("001");
		toCreate.setExternalLastUpdateDate(new Date());
		AddressLineDTO toCreateLine = new AddressLineDTO();
		toCreateLine.setLine("1000 Hilltop Circle");
		toCreate.getLines().add(toCreateLine);
		toCreate.setName("Test Hospital");
		toCreate.setParentOrgName("TEST ORG");
		toCreate.setState("MD");
		LocationStatusDTO status = new LocationStatusDTO();
		status.setName("Active");
		toCreate.setStatus(status);
		toCreate.setType("Hospital");
		toCreate.setZipcode("21227");
		
		LocationDTO toUpdate = locationDao.create(toCreate);
		toUpdate.getLines().clear();
		AddressLineDTO line1 = new AddressLineDTO();
		line1.setLine("1001 Hilltop Circle");
		toUpdate.getLines().add(line1);
		AddressLineDTO line2 = new AddressLineDTO();
		line2.setLine("APT 3");
		toUpdate.getLines().add(line2);
		
		LocationDTO updated = locationDao.update(toUpdate);
		assertEquals(toUpdate.getId(), updated.getId());
		LocationDTO queried = locationDao.findById(updated.getId());
		compareLocations(updated, queried);
	}
	
	private void compareLocations(LocationDTO first, LocationDTO second) {
		assertTrue(second.getId().longValue() > 0);
		assertEquals(first.getCity(), second.getCity());
		assertEquals(first.getDescription(), second.getDescription());
		assertEquals(first.getExternalId(), second.getExternalId());
		assertEquals(first.getLines().size(), second.getLines().size());
		for(AddressLineDTO line : first.getLines()) {
			boolean hasCheckedLine = false;
			for(AddressLineDTO otherLine : second.getLines()) {
				if(line.getId().longValue() == otherLine.getId().longValue()) {
					hasCheckedLine = true;
					assertEquals(line.getLine(), otherLine.getLine());
				}
			}
			assertTrue(hasCheckedLine);
		}
		assertEquals(first.getName(), second.getName());
		assertEquals(first.getParentOrgName(), second.getParentOrgName());
		assertEquals(first.getState(), second.getState());
		assertNotNull(first.getStatus());
		assertNotNull(second.getStatus());
		assertEquals(first.getStatus().getName(), second.getStatus().getName());
		assertEquals(first.getType(), second.getType());
		assertEquals(first.getZipcode(), second.getZipcode());
	}
}
