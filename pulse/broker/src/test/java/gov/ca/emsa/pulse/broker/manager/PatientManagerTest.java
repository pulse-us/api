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
import gov.ca.emsa.pulse.common.domain.PatientRecordName;
import gov.ca.emsa.pulse.common.domain.QueryStatus;

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
	public void setup() {
		acf = new AlternateCareFacilityDTO();
		acf.setName("ACF1");
		acf = acfDao.create(acf);
		assertNotNull(acf);
		assertNotNull(acf.getId());
		assertTrue(acf.getId().longValue() > 0);
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void testCreatePatient() {		
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
	public void testGetPatientsAtAcf() {		
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
