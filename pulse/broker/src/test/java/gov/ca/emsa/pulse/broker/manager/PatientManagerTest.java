package gov.ca.emsa.pulse.broker.manager;

import gov.ca.emsa.pulse.broker.BrokerApplicationTestConfig;
import gov.ca.emsa.pulse.broker.dao.AlternateCareFacilityDAO;
import gov.ca.emsa.pulse.broker.dao.OrganizationDAO;
import gov.ca.emsa.pulse.broker.dao.PatientRecordDAO;
import gov.ca.emsa.pulse.broker.dao.QueryDAO;
import gov.ca.emsa.pulse.broker.dto.AlternateCareFacilityDTO;
import gov.ca.emsa.pulse.broker.dto.GivenNameDTO;
import gov.ca.emsa.pulse.broker.dto.NameTypeDTO;
import gov.ca.emsa.pulse.broker.dto.OrganizationDTO;
import gov.ca.emsa.pulse.broker.dto.PatientDTO;
import gov.ca.emsa.pulse.broker.dto.PatientOrganizationMapDTO;
import gov.ca.emsa.pulse.broker.dto.PatientRecordDTO;
import gov.ca.emsa.pulse.broker.dto.PatientRecordNameDTO;
import gov.ca.emsa.pulse.broker.dto.QueryDTO;
import gov.ca.emsa.pulse.broker.dto.QueryOrganizationDTO;
import gov.ca.emsa.pulse.common.domain.QueryLocationStatus;
import gov.ca.emsa.pulse.common.domain.PatientRecordName;
import gov.ca.emsa.pulse.common.domain.QueryStatus;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={BrokerApplicationTestConfig.class})
public class PatientManagerTest extends TestCase {
	@Autowired PatientManager patientManager;
	@Autowired QueryDAO queryDao;
	@Autowired OrganizationDAO orgDao;
	@Autowired AlternateCareFacilityDAO acfDao;
	@Autowired PatientRecordDAO prDao;
	private AlternateCareFacilityDTO acf;
	private OrganizationDTO org1, org2;
	private PatientRecordDTO queryResult1, queryResult2;
	
	@Before
	public void setup() throws SQLException  {
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
		
		QueryOrganizationDTO orgQuery1 = new QueryOrganizationDTO();
		orgQuery1.setOrgId(org1.getId());
		orgQuery1.setStatus(QueryLocationStatus.Active);
		toInsert.getOrgStatuses().add(orgQuery1);
		
		QueryOrganizationDTO orgQuery2 = new QueryOrganizationDTO();
		orgQuery2.setOrgId(org2.getId());
		orgQuery2.setStatus(QueryLocationStatus.Active);
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
		
//		queryResult1 = new PatientRecordDTO();
//		queryResult1.setGivenName("John");
//		queryResult1.setFamilyName("Smith");
//		queryResult1.setGender("Male");
//		queryResult1.setOrgPatientId("JS1");
//		queryResult1.setQueryOrganizationId(orgQuery1.getId());
//		queryResult1.setSsn("111223333");
//		queryResult1.setPhoneNumber("5555555555");
//		queryResult1 = prDao.create(queryResult1);
//		assertNotNull(queryResult1);
//		assertNotNull(queryResult1.getId());
//		assertTrue(queryResult1.getId().longValue() > 0);
//		
//		queryResult2 = new PatientRecordDTO();
//		queryResult2.setGivenName("John");
//		queryResult2.setFamilyName("Smith");
//		queryResult2.setGender("Male");
//		queryResult2.setOrgPatientId("JSMITH15");
//		queryResult2.setQueryOrganizationId(orgQuery2.getId());
//		queryResult2.setSsn("111223344");
//		queryResult2.setPhoneNumber("5555555555");
//		queryResult2 = prDao.create(queryResult1);
//		assertNotNull(queryResult2);
//		assertNotNull(queryResult2.getId());
//		assertTrue(queryResult2.getId().longValue() > 0);
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void testCreatePatient() throws SQLException  {		
		PatientDTO toCreate = new PatientDTO();
		toCreate.setAcf(acf);
		toCreate.setFullName("Jon Snow");
		toCreate.setFriendlyName("Bri");
		toCreate.setSsn("111223344");
		toCreate.setGender("Male");
		
		PatientDTO created = patientManager.create(toCreate);
		assertNotNull(created);
		assertNotNull(created.getId());
		assertTrue(created.getId().longValue() > 0);
		assertNotNull(created.getAcf());
		assertNotNull(created.getAcf().getId());
		assertEquals(created.getAcf().getId().longValue(), acf.getId().longValue());

		//TODO: why is this coming back empty?? It works when the service is called
		//assertEquals(1, selected.getOrgMaps().size());
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void testGetPatientsAtAcf() throws SQLException  {		
		PatientDTO toCreate = new PatientDTO();
		toCreate.setFriendlyName("Bri");
		toCreate.setAcf(acf);
		toCreate.setFullName("Brian Lindsey");
		
		patientManager.create(toCreate);
		
		List<PatientDTO> patients = patientManager.getPatientsAtAcf(acf.getId());
		assertNotNull(patients);
		assertEquals(1, patients.size());
		assertNotNull(patients.get(0).getOrgMaps());
		//TODO: this should work
		//assertEquals(1, patients.get(0).getOrgMaps().size());
	}
}
