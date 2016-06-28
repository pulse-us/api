package gov.ca.emsa.pulse.broker.dao;


import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;

import gov.ca.emsa.pulse.broker.BrokerApplicationTestConfig;
import gov.ca.emsa.pulse.broker.dto.AddressDTO;
import gov.ca.emsa.pulse.broker.dto.OrganizationDTO;
import gov.ca.emsa.pulse.broker.dto.PatientRecordDTO;
import junit.framework.TestCase;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={BrokerApplicationTestConfig.class})
public class PatientDaoTest extends TestCase {

	@Autowired PatientRecordDAO patientDao;

	@Test
	@Transactional
	public void testInsertPatientWithEverything() {
		PatientRecordDTO toInsert = new PatientRecordDTO();
		toInsert.setFirstName("test");
		toInsert.setLastName("patient");
		toInsert.setOrgPatientId("EHR-PAT-1");
		toInsert.setDateOfBirth(new Date());
		toInsert.setGender("Male");
		toInsert.setPhoneNumber("410-444-4444");
		toInsert.setSsn("xxx-xx-xxxx");
		
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
		
		PatientRecordDTO created = patientDao.create(toInsert);
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
		PatientRecordDTO toInsert = new PatientRecordDTO();
		toInsert.setFirstName("test");
		toInsert.setLastName("patient2");
		toInsert.setOrgPatientId("EHR-PAT-2");
		toInsert.setDateOfBirth(new Date());
		toInsert.setGender("Female");
		toInsert.setPhoneNumber("410-444-4444");
		toInsert.setSsn("xxx-xx-xxxx");
		
		AddressDTO address = new AddressDTO();
		address.setStreetLineOne("1001 Hilltop Circle");
		address.setStreetLineTwo("Suite 350");
		address.setCity("Baltimore");
		address.setState("MD");
		address.setZipcode("21228");
		toInsert.setAddress(address);
		
		PatientRecordDTO created = patientDao.create(toInsert);
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
		PatientRecordDTO toInsert = new PatientRecordDTO();
		toInsert.setFirstName("test");
		toInsert.setLastName("patient3");
		toInsert.setOrgPatientId("EHR-PAT-3");
		toInsert.setDateOfBirth(new Date());
		toInsert.setGender("Female");
		toInsert.setPhoneNumber("410-444-4444");
		toInsert.setSsn("xxx-xx-xxxx");
		
		PatientRecordDTO created = patientDao.create(toInsert);
		assertNotNull(created);
		assertNotNull(created.getId());
		assertTrue(created.getId() > 0);
		assertNull(created.getOrganization());
		assertNull(created.getAddress());
	}
	
	@Test
	@Transactional
	public void testGetPatientById() {
		PatientRecordDTO toInsert = new PatientRecordDTO();
		toInsert.setFirstName("test");
		toInsert.setLastName("patient4");
		toInsert.setOrgPatientId("EHR-PAT-4");
		toInsert.setDateOfBirth(new Date());
		toInsert.setGender("Male");
		toInsert.setPhoneNumber("410-444-4444");
		toInsert.setSsn("xxx-xx-xxxx");
		
		AddressDTO address = new AddressDTO();
		address.setStreetLineOne("1001 Hilltop Circle");
		address.setStreetLineTwo("Suite 350");
		address.setCity("Baltimore");
		address.setState("MD");
		address.setZipcode("21228");
		toInsert.setAddress(address);
		
		PatientRecordDTO created = patientDao.create(toInsert);
		
		PatientRecordDTO found = patientDao.getById(created.getId());
		assertNotNull(found);
		assertNull(found.getOrganization());
		assertNotNull(found.getAddress());
		assertNotNull(found.getAddress().getId());
		assertNotNull(found.getAddress().getStreetLineOne());
	}
}
