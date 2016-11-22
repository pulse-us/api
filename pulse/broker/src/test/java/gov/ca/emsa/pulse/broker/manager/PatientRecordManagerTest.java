package gov.ca.emsa.pulse.broker.manager;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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
	@Autowired LocationDAO orgDao;
	@Autowired AlternateCareFacilityDAO acfDao;
	@Autowired PatientRecordDAO prDao;
	@Autowired PatientGenderDAO patientGenderDao;
	private AlternateCareFacilityDTO acf;
	private LocationDTO org1, org2;
	private QueryDTO query;
	private QueryLocationMapDTO orgQuery1, orgQuery2;
	private PatientGenderDTO patientGenderMale;
	
	@Before
	public void setup() throws SQLException  {
		acf = new AlternateCareFacilityDTO();
		acf.setName("ACF1");
		acf = acfDao.create(acf);
		assertNotNull(acf);
		assertNotNull(acf.getId());
		assertTrue(acf.getId().longValue() > 0);
		
		org1 = new LocationDTO();
		org1.setLocationId(1L);
		org1.setName("IHE Org");
		org1.setAdapter("IHE");
		org1.setEndpointUrl("http://www.localhost.com");
		org1.setPassword("pwd");
		org1.setUsername("kekey");
		org1.setActive(true);
		org1 = orgDao.create(org1);
		
		org2 = new LocationDTO();
		org2.setLocationId(2L);
		org2.setName("eHealth Org");
		org2.setAdapter("eHealth");
		org2.setEndpointUrl("http://www.localhost.com");
		org2.setPassword("pwd");
		org2.setUsername("kekey");
		org2.setActive(true);
		org2 = orgDao.create(org2);
		
		QueryDTO toInsert = new QueryDTO();
		toInsert.setStatus(QueryStatus.ACTIVE.name());
		toInsert.setTerms("terms");
		toInsert.setUserId("kekey");
		
		orgQuery1 = new QueryLocationMapDTO();
		orgQuery1.setLocationId(org1.getId());
		orgQuery1.setStatus(QueryLocationStatus.Active);
		toInsert.getLocationStatuses().add(orgQuery1);
		
		orgQuery2 = new QueryLocationMapDTO();
		orgQuery2.setLocationId(org2.getId());
		orgQuery2.setStatus(QueryLocationStatus.Active);
		toInsert.getLocationStatuses().add(orgQuery2);
		
		QueryDTO inserted = queryDao.create(toInsert);
		assertNotNull(inserted);
		assertNotNull(inserted.getId());
		assertTrue(inserted.getId().longValue() > 0);
		assertNotNull(inserted.getLocationStatuses());
		assertEquals(2, inserted.getLocationStatuses().size());
		orgQuery1 = inserted.getLocationStatuses().get(0);
		assertNotNull(inserted.getLocationStatuses().get(0).getId());
		assertTrue(inserted.getLocationStatuses().get(0).getId().longValue() > 0);
		orgQuery2 = inserted.getLocationStatuses().get(1);
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
		orgQuery1 = query.getLocationStatuses().get(0);
		assertNotNull(query.getLocationStatuses().get(0).getId());
		assertTrue(query.getLocationStatuses().get(0).getId().longValue() > 0);
		orgQuery2 = query.getLocationStatuses().get(1);
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
		dto.setQueryLocationId(orgQuery1.getId());
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
		queryManager.cancelQueryToOrganization(query.getId(), org1.getId());
		QueryDTO updatedQuery = queryManager.getById(query.getId());
		
		assertNotNull(updatedQuery);
		assertEquals(query.getId(), updatedQuery.getId());
		assertEquals(2, updatedQuery.getLocationStatuses().size());
		boolean queryHadOrg = false;
		for(QueryLocationMapDTO orgStatus : updatedQuery.getLocationStatuses()) {
			assertNotNull(orgStatus.getLocationId());
			if(orgStatus.getLocationId().longValue() == org1.getId().longValue()) {
				queryHadOrg = true;
				
				assertEquals(QueryLocationStatus.Cancelled, orgStatus.getStatus());
			}
		}
		assertTrue(queryHadOrg);
	}
}
