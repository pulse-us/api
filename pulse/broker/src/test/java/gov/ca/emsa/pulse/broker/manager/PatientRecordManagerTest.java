package gov.ca.emsa.pulse.broker.manager;

import java.sql.SQLException;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import gov.ca.emsa.pulse.broker.BrokerApplicationTestConfig;
import gov.ca.emsa.pulse.broker.dao.AlternateCareFacilityDAO;
import gov.ca.emsa.pulse.broker.dao.LocationDAO;
import gov.ca.emsa.pulse.broker.dao.PatientGenderDAO;
import gov.ca.emsa.pulse.broker.dao.PatientRecordDAO;
import gov.ca.emsa.pulse.broker.dao.QueryDAO;
import gov.ca.emsa.pulse.broker.dto.AlternateCareFacilityDTO;
import gov.ca.emsa.pulse.broker.dto.LocationDTO;
import gov.ca.emsa.pulse.broker.dto.LocationStatusDTO;
import gov.ca.emsa.pulse.broker.dto.PatientGenderDTO;
import gov.ca.emsa.pulse.broker.dto.PatientRecordDTO;
import gov.ca.emsa.pulse.broker.dto.QueryDTO;
import gov.ca.emsa.pulse.broker.dto.QueryLocationMapDTO;
import gov.ca.emsa.pulse.common.domain.QueryLocationStatus;
import gov.ca.emsa.pulse.common.domain.QueryStatus;
import junit.framework.TestCase;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={BrokerApplicationTestConfig.class})
public class PatientRecordManagerTest extends TestCase {
	@Autowired QueryManager queryManager;
	@Autowired QueryDAO queryDao;
	@Autowired LocationDAO locationDao;
	@Autowired AlternateCareFacilityDAO acfDao;
	@Autowired PatientRecordDAO prDao;
	@Autowired PatientGenderDAO patientGenderDao;
	private AlternateCareFacilityDTO acf;
	private LocationDTO location1, location2;
	private QueryDTO query;
	private QueryLocationMapDTO queryLocation1, queryLocation2;
	private PatientGenderDTO patientGenderMale;
	
	@Before
	public void setup() throws SQLException  {
		acf = new AlternateCareFacilityDTO();
		acf.setName("ACF1");
		acf = acfDao.create(acf);
		assertNotNull(acf);
		assertNotNull(acf.getId());
		assertTrue(acf.getId().longValue() > 0);
		
		LocationStatusDTO locStatus = new LocationStatusDTO();
		locStatus.setId(1L);
		
		location1 = new LocationDTO();
		location1.setExternalId("1");
		location1.setName("John's Hopkins Medical Center");
		location1.setDescription("A hospital");
		location1.setType("Hospital");
		location1.setExternalLastUpdateDate(new Date());
		location1.setParentOrgName("EHealth Parent Org");
		location1.setStatus(locStatus);
		location1 = locationDao.create(location1);
		
		location2 = new LocationDTO();
		location2.setExternalId("2");
		location2.setName("University of Maryland Medical Center");
		location2.setDescription("A hospital");
		location2.setType("Hospital");
		location2.setExternalLastUpdateDate(new Date());
		location2.setParentOrgName("EHealth Parent Org");
		location2.setStatus(locStatus);
		location2 = locationDao.create(location2);
		
		QueryDTO toInsert = new QueryDTO();
		toInsert.setStatus(QueryStatus.Active);
		toInsert.setTerms("terms");
		toInsert.setUserId("kekey");
		
		queryLocation1 = new QueryLocationMapDTO();
		queryLocation1.setLocationId(location1.getId());
		queryLocation1.setStatus(QueryLocationStatus.Active);
		toInsert.getLocationStatuses().add(queryLocation1);
		
		queryLocation2 = new QueryLocationMapDTO();
		queryLocation2.setLocationId(location2.getId());
		queryLocation2.setStatus(QueryLocationStatus.Active);
		toInsert.getLocationStatuses().add(queryLocation2);
		
		QueryDTO inserted = queryDao.create(toInsert);
		assertNotNull(inserted);
		assertNotNull(inserted.getId());
		assertTrue(inserted.getId().longValue() > 0);
		assertNotNull(inserted.getLocationStatuses());
		assertEquals(2, inserted.getLocationStatuses().size());
		queryLocation1 = inserted.getLocationStatuses().get(0);
		assertNotNull(inserted.getLocationStatuses().get(0).getId());
		assertTrue(inserted.getLocationStatuses().get(0).getId().longValue() > 0);
		queryLocation2 = inserted.getLocationStatuses().get(1);
		assertNotNull(inserted.getLocationStatuses().get(1).getId());
		assertTrue(inserted.getLocationStatuses().get(1).getId().longValue() > 0);
		
		patientGenderMale = new PatientGenderDTO();
		patientGenderMale = patientGenderDao.getById(2L);

		query = queryManager.createQuery(toInsert);
		assertNotNull(query);
		assertNotNull(query.getId());
		assertTrue(query.getId().longValue() > 0);
		assertNotNull(query.getLocationStatuses());
		assertEquals(2, query.getLocationStatuses().size());
		queryLocation1 = query.getLocationStatuses().get(0);
		assertNotNull(query.getLocationStatuses().get(0).getId());
		assertTrue(query.getLocationStatuses().get(0).getId().longValue() > 0);
		queryLocation2 = query.getLocationStatuses().get(1);
		assertNotNull(query.getLocationStatuses().get(1).getId());
		assertTrue(query.getLocationStatuses().get(1).getId().longValue() > 0);
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void testCreatePatientRecord() {
		
		PatientRecordDTO dto = new PatientRecordDTO();
		String date = "20160110";
		dto.setDateOfBirth(date);
		dto.setPhoneNumber("443-745-0888");
		dto.setQueryLocationId(queryLocation1.getId());
		dto.setSsn("555-55-5555");

		dto.setPatientGender(patientGenderMale);
		PatientRecordDTO added = queryManager.addPatientRecord(dto);
		
		assertNotNull(added);
		assertNotNull(added.getId());
		assertTrue(added.getDateOfBirth().equals(date));
		
		PatientRecordDTO selected = queryManager.getPatientRecordById(added.getId());
		assertNotNull(added);
		assertEquals(added.getId(), selected.getId());
		assertTrue(selected.getDateOfBirth().equals(date));
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void testCancelPatientDiscoveryQueryToOrganization() {	
		System.out.println("query id " + query.getId());
		queryManager.cancelQueryToLocation(queryLocation1.getId());
		QueryDTO updatedQuery = queryManager.getById(query.getId());
		
		assertNotNull(updatedQuery);
		assertEquals(query.getId(), updatedQuery.getId());
		assertEquals(2, updatedQuery.getLocationStatuses().size());
		boolean queryHadOrg = false;
		for(QueryLocationMapDTO orgStatus : updatedQuery.getLocationStatuses()) {
			assertNotNull(orgStatus.getLocationId());
			if(orgStatus.getLocationId().longValue() == location1.getId().longValue()) {
				queryHadOrg = true;
				
				assertEquals(QueryLocationStatus.Cancelled, orgStatus.getStatus());
			}
		}
		assertTrue(queryHadOrg);
	}
}
