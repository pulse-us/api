package gov.ca.emsa.pulse.broker.dao;

import gov.ca.emsa.pulse.broker.BrokerApplicationTestConfig;
import gov.ca.emsa.pulse.broker.dto.AddressDTO;
import gov.ca.emsa.pulse.broker.dto.AlternateCareFacilityDTO;
import gov.ca.emsa.pulse.broker.dto.GivenNameDTO;
import gov.ca.emsa.pulse.broker.dto.NameTypeDTO;
import gov.ca.emsa.pulse.broker.dto.OrganizationDTO;
import gov.ca.emsa.pulse.broker.dto.PatientRecordAddressDTO;
import gov.ca.emsa.pulse.broker.dto.PatientRecordDTO;
import gov.ca.emsa.pulse.broker.dto.PatientOrganizationMapDTO;
import gov.ca.emsa.pulse.broker.dto.PatientRecordDTO;
import gov.ca.emsa.pulse.broker.dto.PatientRecordNameDTO;
import gov.ca.emsa.pulse.common.domain.NameType;
import gov.ca.emsa.pulse.common.domain.PatientRecordName;

import java.util.ArrayList;
import java.util.Date;

import junit.framework.TestCase;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Commit;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={BrokerApplicationTestConfig.class})
public class PatientRecordDaoTest extends TestCase {

	@Autowired QueryDAO queryDao;
	@Autowired PatientRecordAddressDAO addrDao;
	@Autowired OrganizationDAO orgDao;
	@Autowired AlternateCareFacilityDAO acfDao;
	@Autowired PatientRecordDAO patientRecordDao;
	@Autowired PatientDAO patientDao;
	@Autowired PatientRecordNameDAO prNameDao;
	@Autowired GivenNameDAO givenNameDao;
	@Autowired NameTypeDAO nameTypeDao;
	private AlternateCareFacilityDTO acf;
	private OrganizationDTO org1, org2;
	private PatientRecordDTO queryResult1, queryResult2;
	private NameTypeDTO nameTypeCodeLegal;
	
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
		
		nameTypeCodeLegal = new NameTypeDTO();
		nameTypeCodeLegal.setCode("L");
		nameTypeCodeLegal.setDescription("Legal Name.");
		nameTypeCodeLegal = nameTypeDao.create(nameTypeCodeLegal);
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void testCreatePatientRecord() {		
		PatientRecordDTO toCreate = new PatientRecordDTO();
		
		toCreate.setSsn("111223344");
		toCreate.setGender("M");
		toCreate.setOrganizationPatientRecordId("123-456-78");
		PatientRecordDTO created = patientRecordDao.create(toCreate);
		assertNotNull(created);
		assertNotNull(created.getId());
		assertTrue(created.getId().longValue() > 0);
		
		PatientRecordNameDTO prnDto = new PatientRecordNameDTO();
		prnDto.setFamilyName("Lindsey");
		prnDto.setExpirationDate(new Date());
		prnDto.setPatientRecordId(created.getId());
		prnDto.setNameType(nameTypeCodeLegal);
		PatientRecordNameDTO prnCreated = prNameDao.create(prnDto);
		
		assertNotNull(prnCreated);
		assertEquals("Lindsey", prnCreated.getFamilyName());
		
		GivenNameDTO given1 = new GivenNameDTO();
		given1.setGivenName("Brian");
		given1.setPatientRecordNameId(prnCreated.getId());
		GivenNameDTO givenCreated = givenNameDao.create(given1);
		
		assertNotNull(givenCreated);
		assertEquals("Brian", givenCreated.getGivenName());
		//assertEquals("Brian", prnCreated.getGivenName().get(0).getGivenName());
		
		PatientRecordDTO selectedPatientRecord = patientRecordDao.getById(created.getId());
		PatientRecordNameDTO selectedPatientRecordName = prNameDao.getById(prnCreated.getId());
		assertNotNull(selectedPatientRecord);
		assertNotNull(selectedPatientRecordName);
		assertNotNull(selectedPatientRecord.getPatientRecordName());
		assertNotNull(selectedPatientRecordName.getFamilyName());
		//assertNotNull(selectedPatientRecordName.getGivenName().get(0));
		//assertNotNull(selected);
		//assertNotNull(selected.getId());
		//assertEquals("Lindsey", selected.getPatientRecordName());
		//assertEquals("L", selected.getPatientRecordName().get(0).getNameType().getCode());
		//assertEquals("Brian", selected.getPatientRecordName().get(0).getGivenName().get(0).getGivenName());
		//assertTrue(selected.getId().longValue() > 0);
	}
	
	
	@Test
	@Transactional
	@Rollback(true)
	public void testCreatePatientRecordWithNewAddress() {		
		String streetLine1 = "1000 Hilltop Circle";
		String city = "Baltimore";
		String state = "MD";
		String zip = "21227";
		
		PatientRecordDTO toCreate = new PatientRecordDTO();
		
		toCreate.setSsn("111223344");
		toCreate.setGender("M");
		toCreate.setOrganizationPatientRecordId("123-456-789");
		
		PatientRecordDTO created = patientRecordDao.create(toCreate);
		assertNotNull(created);
		assertNotNull(created.getId());
		assertTrue(created.getId().longValue() > 0);
		
		PatientRecordAddressDTO addrDto = new PatientRecordAddressDTO();
		addrDto.setCity(city);
		addrDto.setState(state);
		addrDto.setZipcode(zip);
		addrDto.setPatientRecordId(created.getId());
		PatientRecordAddressDTO addrCreated = addrDao.create(addrDto);
		
		PatientRecordDTO patientRecordCreated = patientRecordDao.getById(created.getId());
		
		assertNotNull(patientRecordCreated);
		assertNotNull(patientRecordCreated.getAddress());
		assertEquals("21227", patientRecordCreated.getAddress().getZipcode());
		assertEquals("Baltimore", patientRecordCreated.getAddress().getCity());
		assertEquals("MD", patientRecordCreated.getAddress().getState());
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void testUpdatePatientRecordFirstName() {		
		String streetLine1 = "1000 Hilltop Circle";
		String city = "Baltimore";
		String state = "MD";
		String zip = "21227";
		PatientRecordAddressDTO addrDto = new PatientRecordAddressDTO();
		addrDto.setCity(city);
		addrDto.setState(state);
		addrDto.setZipcode(zip);
		addrDto = addrDao.create(addrDto);
		Assert.assertNotNull(addrDto);
		Assert.assertNotNull(addrDto.getId());
		Assert.assertTrue(addrDto.getId().longValue() > 0);
		long existingAddrId = addrDto.getId().longValue();
		
		PatientRecordDTO toCreate = new PatientRecordDTO();
		
		toCreate.setSsn("111223344");
		toCreate.setGender("M");
		toCreate.setOrganizationPatientRecordId("123-1345-678");
		
		PatientRecordDTO created = patientRecordDao.create(toCreate);
		assertNotNull(created);
		assertNotNull(created.getId());
		assertTrue(created.getId().longValue() > 0);
		
		PatientRecordNameDTO prnDto = new PatientRecordNameDTO();
		prnDto.setFamilyName("Lindsey");
		prnDto.setExpirationDate(new Date());
		prnDto.setPatientRecordId(created.getId());
		prnDto.setNameType(nameTypeCodeLegal);
		PatientRecordNameDTO prnCreated = prNameDao.create(prnDto);
		
		assertNotNull(prnCreated);
		assertEquals("Lindsey", prnCreated.getFamilyName());
		
		GivenNameDTO given1 = new GivenNameDTO();
		given1.setGivenName("Brian");
		given1.setPatientRecordNameId(prnCreated.getId());
		GivenNameDTO givenCreated = givenNameDao.create(given1);
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void testDeletePatientRecord() {
		PatientRecordDTO toCreate = new PatientRecordDTO();
		toCreate.setSsn("111223344");
		toCreate.setGender("Male");
		toCreate.setOrganizationPatientRecordId("123-456-789");
		
		PatientRecordDTO created = patientRecordDao.create(toCreate);
		patientRecordDao.delete(created.getId());
		
		PatientRecordDTO selected = patientRecordDao.getById(created.getId());
		assertNull(selected);
		
		AlternateCareFacilityDTO selectedAcf = acfDao.getById(acf.getId());
		assertNotNull(selectedAcf);
	}
}

