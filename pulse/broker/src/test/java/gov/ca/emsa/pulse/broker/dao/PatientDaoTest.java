package gov.ca.emsa.pulse.broker.dao;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import gov.ca.emsa.pulse.broker.BrokerApplicationTestConfig;
import gov.ca.emsa.pulse.broker.dto.AddressDTO;
import gov.ca.emsa.pulse.broker.dto.OrganizationDTO;
import gov.ca.emsa.pulse.broker.dto.PatientDTO;
import junit.framework.TestCase;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={BrokerApplicationTestConfig.class})
public class PatientDaoTest extends TestCase {

	@Autowired PatientDAO patientDao;

	@Test
	@Transactional
	public void testInsertPatientWithEverything() {
		PatientDTO toInsert = new PatientDTO();
		toInsert.setFirstName("test");
		toInsert.setLastName("patient");
		toInsert.setDateOfBirth(new Date());
		toInsert.setGender("Male");
		toInsert.setPhoneNumber("410-444-4444");
		toInsert.setSsn("xxx-xx-xxxx");
		toInsert.setPulsePatientId("testpatientuniqueid");
		
		AddressDTO address = new AddressDTO();
		address.setStreetLineOne("1000 Hilltop Circle");
		address.setStreetLineTwo("Suite 350");
		address.setCity("Baltimore");
		address.setState("MD");
		address.setZipcode("21228");
		toInsert.setAddress(address);
		
		OrganizationDTO org = new OrganizationDTO();
		org.setName("Test Org");
		org.setAdapter("eHealth");
		org.setOrganizationId(1L);
		toInsert.setOrganization(org);
		
		PatientDTO created = patientDao.create(toInsert);
		assertNotNull(created);
		assertNotNull(created.getId());
		assertTrue(created.getId() > 0);
		assertNotNull(created.getOrganization());
		assertNotNull(created.getOrganization().getId());
		assertTrue(created.getOrganization().getId().longValue() > 0);
		assertNotNull(created.getAddress());
		assertNotNull(created.getAddress().getId());
		assertTrue(created.getAddress().getId().longValue() > 0);
	}

	@Test
	@Transactional
	public void testPatientWithAddressOnly() {
		PatientDTO toInsert = new PatientDTO();
		toInsert.setFirstName("test");
		toInsert.setLastName("patient2");
		toInsert.setDateOfBirth(new Date());
		toInsert.setGender("Female");
		toInsert.setPhoneNumber("410-444-4444");
		toInsert.setSsn("xxx-xx-xxxx");
		toInsert.setPulsePatientId("testpatient2uniqueid");
		
		AddressDTO address = new AddressDTO();
		address.setStreetLineOne("1001 Hilltop Circle");
		address.setStreetLineTwo("Suite 350");
		address.setCity("Baltimore");
		address.setState("MD");
		address.setZipcode("21228");
		toInsert.setAddress(address);
		
		PatientDTO created = patientDao.create(toInsert);
		assertNotNull(created);
		assertNotNull(created.getId());
		assertTrue(created.getId() > 0);
		assertNull(created.getOrganization());
		assertNotNull(created.getAddress());
		assertNotNull(created.getAddress().getId());
		assertTrue(created.getAddress().getId().longValue() > 0);
	}
	
	@Test
	@Transactional
	public void testInsertPatientWithNothingElse() {
		PatientDTO toInsert = new PatientDTO();
		toInsert.setFirstName("test");
		toInsert.setLastName("patient3");
		toInsert.setDateOfBirth(new Date());
		toInsert.setGender("Female");
		toInsert.setPhoneNumber("410-444-4444");
		toInsert.setSsn("xxx-xx-xxxx");
		toInsert.setPulsePatientId("testpatient3uniqueid");
		
		PatientDTO created = patientDao.create(toInsert);
		assertNotNull(created);
		assertNotNull(created.getId());
		assertTrue(created.getId() > 0);
		assertNull(created.getOrganization());
		assertNull(created.getAddress());
	}
	
	@Test
	@Transactional
	public void testGetPatientById() {
		PatientDTO toInsert = new PatientDTO();
		toInsert.setFirstName("test");
		toInsert.setLastName("patient4");
		toInsert.setDateOfBirth(new Date());
		toInsert.setGender("Male");
		toInsert.setPhoneNumber("410-444-4444");
		toInsert.setSsn("xxx-xx-xxxx");
		toInsert.setPulsePatientId("testpatient4uniqueid");
		
		AddressDTO address = new AddressDTO();
		address.setStreetLineOne("1001 Hilltop Circle");
		address.setStreetLineTwo("Suite 350");
		address.setCity("Baltimore");
		address.setState("MD");
		address.setZipcode("21228");
		toInsert.setAddress(address);
		
		PatientDTO created = patientDao.create(toInsert);
		
		PatientDTO found = patientDao.getById(created.getId());
		assertNotNull(found);
		assertNull(found.getOrganization());
		assertNotNull(found.getAddress());
		assertNotNull(found.getAddress().getId());
		assertNotNull(found.getAddress().getStreetLineOne());
	}
	
	@Test
	@Transactional
	public void testsGetPatientByOrgAndId() {
		PatientDTO toInsert = new PatientDTO();
		toInsert.setFirstName("test5");
		toInsert.setLastName("patient");
		toInsert.setDateOfBirth(new Date());
		toInsert.setGender("Male");
		toInsert.setPhoneNumber("410-444-4444");
		toInsert.setSsn("xxx-xx-xxxx");
		toInsert.setPulsePatientId("testpatient5uniqueid");
		
		AddressDTO address = new AddressDTO();
		address.setStreetLineOne("1005 Hilltop Circle");
		address.setStreetLineTwo("Suite 350");
		address.setCity("Baltimore");
		address.setState("MD");
		address.setZipcode("21228");
		toInsert.setAddress(address);
		
		OrganizationDTO org = new OrganizationDTO();
		org.setName("Test Org5");
		org.setAdapter("eHealth");
		org.setOrganizationId(1L);
		toInsert.setOrganization(org);
		
		PatientDTO created = patientDao.create(toInsert);
		System.out.println(created.getPulsePatientId());
		System.out.println(created.getOrganization().getId());
		
		List<PatientDTO> foundPatients = patientDao.getByPatientIdAndOrg(created);
		assertNotNull(foundPatients);
		assertEquals(1, foundPatients.size());
		PatientDTO found = foundPatients.get(0);
		assertNotNull(found);
		assertNotNull(found.getId());
		assertTrue(found.getId() > 0);
		assertNotNull(found.getOrganization());
		assertNotNull(found.getOrganization().getId());
		System.out.println(found.getOrganization().getId());
		assertTrue(found.getOrganization().getId().longValue() > 0);
		assertNotNull(found.getAddress());
		assertNotNull(found.getAddress().getId());
		assertTrue(found.getAddress().getId().longValue() > 0);
	}
	
}
