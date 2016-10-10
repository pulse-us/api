package gov.ca.emsa.pulse.broker.manager;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

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
import gov.ca.emsa.pulse.broker.dao.OrganizationDAO;
import gov.ca.emsa.pulse.broker.dao.PatientDAO;
import gov.ca.emsa.pulse.broker.dao.PatientRecordDAO;
import gov.ca.emsa.pulse.broker.dao.QueryDAO;
import gov.ca.emsa.pulse.broker.dto.AlternateCareFacilityDTO;
import gov.ca.emsa.pulse.broker.dto.GivenNameDTO;
import gov.ca.emsa.pulse.broker.dto.OrganizationDTO;
import gov.ca.emsa.pulse.broker.dto.PatientDTO;
import gov.ca.emsa.pulse.broker.dto.PatientOrganizationMapDTO;
import gov.ca.emsa.pulse.broker.dto.PatientRecordDTO;
import gov.ca.emsa.pulse.broker.dto.QueryDTO;
import gov.ca.emsa.pulse.broker.dto.QueryOrganizationDTO;
import gov.ca.emsa.pulse.common.domain.QueryStatus;
import junit.framework.TestCase;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={BrokerApplicationTestConfig.class})
public class PatientRecordManagerTest extends TestCase {
	@Autowired QueryManager queryManager;
	@Autowired QueryDAO queryDao;
	@Autowired OrganizationDAO orgDao;
	@Autowired AlternateCareFacilityDAO acfDao;
	@Autowired PatientRecordDAO prDao;
	private AlternateCareFacilityDTO acf;
	private OrganizationDTO org1, org2;
	private QueryOrganizationDTO orgQuery1, orgQuery2;
	
	@Before
	public void setup() {
		acf = new AlternateCareFacilityDTO();
		acf.setName("ACF1");
		acf = acfDao.create(acf);
		assertNotNull(acf);
		assertNotNull(acf.getId());
		assertTrue(acf.getId().longValue() > 0);
		
		org1 = new OrganizationDTO();
		org1.setOrganizationId(1L);
		org1.setName("IHE Org");
		org1.setAdapter("IHE");
		org1.setEndpointUrl("http://www.localhost.com");
		org1.setPassword("pwd");
		org1.setUsername("kekey");
		org1.setActive(true);
		org1 = orgDao.create(org1);
		
		org2 = new OrganizationDTO();
		org2.setOrganizationId(2L);
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
		
		orgQuery1 = new QueryOrganizationDTO();
		orgQuery1.setOrgId(org1.getId());
		orgQuery1.setStatus(QueryStatus.ACTIVE.name());
		toInsert.getOrgStatuses().add(orgQuery1);
		
		orgQuery2 = new QueryOrganizationDTO();
		orgQuery2.setOrgId(org2.getId());
		orgQuery2.setStatus(QueryStatus.ACTIVE.name());
		toInsert.getOrgStatuses().add(orgQuery2);
		
		QueryDTO inserted = queryDao.create(toInsert);
		assertNotNull(inserted);
		assertNotNull(inserted.getId());
		assertTrue(inserted.getId().longValue() > 0);
		assertNotNull(inserted.getOrgStatuses());
		assertEquals(2, inserted.getOrgStatuses().size());
		orgQuery1 = inserted.getOrgStatuses().get(0);
		assertNotNull(inserted.getOrgStatuses().get(0).getId());
		assertTrue(inserted.getOrgStatuses().get(0).getId().longValue() > 0);
		orgQuery2 = inserted.getOrgStatuses().get(1);
		assertNotNull(inserted.getOrgStatuses().get(1).getId());
		assertTrue(inserted.getOrgStatuses().get(1).getId().longValue() > 0);

	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void testCreatePatientRecord() {		
		PatientRecordDTO dto = new PatientRecordDTO();
		 LocalDate date = LocalDate.parse("2016-01-10", DateTimeFormatter.ISO_DATE);
		dto.setDateOfBirth(date);
		dto.getPatientName().setFamilyName("Ekey");
		ArrayList<GivenNameDTO> givens = new ArrayList<GivenNameDTO>();
		GivenNameDTO given = new GivenNameDTO();
		given.setGivenName("Jonathon");
		givens.add(given);
		dto.getPatientName().setGivenName(givens);
		dto.getPatientName().setSuffix("MD");
		dto.getPatientName().setPrefix("Dr.");
		dto.getPatientName().setNameTypeCode("G");
		dto.setOrgPatientId("123-456-78");
		dto.setPhoneNumber("443-745-0888");
		dto.setQueryOrganizationId(orgQuery1.getId());
		dto.setSsn("555-55-5555");
		PatientRecordDTO added = queryManager.addPatientRecord(dto);
		
		assertNotNull(added);
		assertNotNull(added.getId());
		assertTrue(added.getDateOfBirth().isEqual(date));
		
		PatientRecordDTO selected = queryManager.getPatientRecordById(added.getId());
		assertNotNull(added);
		assertEquals(added.getId(), selected.getId());
		assertTrue(selected.getDateOfBirth().isEqual(date));
	}
}
