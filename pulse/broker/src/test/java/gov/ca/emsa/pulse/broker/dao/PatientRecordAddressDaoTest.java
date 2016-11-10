package gov.ca.emsa.pulse.broker.dao;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import gov.ca.emsa.pulse.broker.BrokerApplicationTestConfig;
import gov.ca.emsa.pulse.broker.dto.PatientGenderDTO;
import gov.ca.emsa.pulse.broker.dto.PatientRecordAddressDTO;
import gov.ca.emsa.pulse.broker.dto.PatientRecordAddressLineDTO;
import gov.ca.emsa.pulse.broker.dto.PatientRecordDTO;

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
public class PatientRecordAddressDaoTest {
	
	@Autowired PatientRecordAddressDAO patientAddressDao;
	@Autowired PatientRecordAddressLineDAO patientAddressLineDao;
	@Autowired PatientGenderDAO patientGenderDao;
	@Autowired PatientRecordDAO patientRecordDao;
	
	@Test
	@Transactional
	@Rollback(true)
	public void createPatientAddressTest(){
		
		PatientGenderDTO patientGenderMale =  patientGenderDao.getByCode("M");
		PatientRecordDTO patientRecord = new PatientRecordDTO();
		patientRecord.setPatientGender(patientGenderMale);
		PatientRecordDTO patientRecordCreated = patientRecordDao.create(patientRecord);
		
		PatientRecordAddressLineDTO patientAddressLineDto1 = new PatientRecordAddressLineDTO();
		patientAddressLineDto1.setLine("5523 Research Park Drive");
		patientAddressLineDto1.setLineOrder(1);
		
		PatientRecordAddressLineDTO patientAddressLineDto2 = new PatientRecordAddressLineDTO();
		patientAddressLineDto2.setLine("Suite 370");
		patientAddressLineDto2.setLineOrder(2);
		
		ArrayList<PatientRecordAddressLineDTO> pralDtoArr = new ArrayList<PatientRecordAddressLineDTO>();
		pralDtoArr.add(patientAddressLineDto1);
		pralDtoArr.add(patientAddressLineDto2);
		
		PatientRecordAddressDTO patientAddressDto = new PatientRecordAddressDTO();
		patientAddressDto.setCity("Bel Air");
		patientAddressDto.setCreationDate(new Date());
		patientAddressDto.setState("MD");
		patientAddressDto.setZipcode("21015");
		patientAddressDto.setPatientRecordId(patientRecordCreated.getId());
		patientAddressDto.setPatientRecordAddressLines(pralDtoArr);
		PatientRecordAddressDTO patientAddressCreated = patientAddressDao.create(patientAddressDto);
		
		assertNotNull(patientAddressCreated);
		assertNotNull(patientAddressCreated.getCity());
		assertNotNull(patientAddressCreated.getState());
		assertNotNull(patientAddressCreated.getZipcode());
		
		PatientRecordAddressDTO created = patientAddressDao.getById(patientAddressCreated.getId());
		
		assertNotNull(created);
		assertNotNull(created.getCity());
		assertNotNull(created.getState());
		assertNotNull(created.getZipcode());
		assertNotNull(created.getPatientRecordAddressLines());
		assertEquals(2, created.getPatientRecordAddressLines().size());
		
		List<PatientRecordAddressLineDTO> lines = created.getPatientRecordAddressLines();
		
		assertEquals("5523 Research Park Drive", lines.get(0).getLine());
		assertEquals(1, lines.get(0).getLineOrder());
		assertEquals("Suite 370", lines.get(1).getLine());
		assertEquals(2, lines.get(1).getLineOrder());
		
	}

}
