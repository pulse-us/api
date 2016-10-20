package gov.ca.emsa.pulse.broker.dao;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.LinkedList;

import gov.ca.emsa.pulse.broker.BrokerApplicationTestConfig;
import gov.ca.emsa.pulse.broker.dto.PatientAddressDTO;
import gov.ca.emsa.pulse.broker.dto.PatientAddressLineDTO;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={BrokerApplicationTestConfig.class})
public class PatientAddressDaoTest {
	
	@Autowired PatientAddressDAO patientAddressDao;
	@Autowired PatientAddressLineDAO patientAddressLineDao;
	
	@Test
	@Transactional
	@Rollback(true)
	public void createPatientAddressTest(){
		PatientAddressDTO patientAddressDto = new PatientAddressDTO();
		patientAddressDto.setCity("Bel Air");
		patientAddressDto.setCreationDate(new Date());
		patientAddressDto.setState("MD");
		patientAddressDto.setZipcode("21015");
		PatientAddressDTO patientAddressCreated = patientAddressDao.create(patientAddressDto);
		
		assertNotNull(patientAddressCreated);
		assertNotNull(patientAddressCreated.getCity());
		assertNotNull(patientAddressCreated.getState());
		assertNotNull(patientAddressCreated.getZipcode());
		
		PatientAddressLineDTO patientAddressLineDto1 = new PatientAddressLineDTO();
		patientAddressLineDto1.setLine("5523 Research Park Drive");
		patientAddressLineDto1.setLineOrder(1);
		patientAddressLineDto1.setPatientAddressId(patientAddressCreated.getId());
		PatientAddressLineDTO patientAddressLineCreated1 = patientAddressLineDao.create(patientAddressLineDto1);
		
		assertNotNull(patientAddressLineCreated1);
		assertNotNull(patientAddressLineCreated1.getLine());
		
		PatientAddressLineDTO patientAddressLineDto2 = new PatientAddressLineDTO();
		patientAddressLineDto2.setLine("Suite 370");
		patientAddressLineDto2.setLineOrder(2);
		patientAddressLineDto2.setPatientAddressId(patientAddressCreated.getId());
		PatientAddressLineDTO patientAddressLineCreated2 = patientAddressLineDao.create(patientAddressLineDto2);
		
		assertNotNull(patientAddressLineCreated2);
		assertNotNull(patientAddressLineCreated2.getLine());
		
		PatientAddressDTO created = patientAddressDao.getById(patientAddressCreated.getId());
		
		assertNotNull(created);
		assertNotNull(created.getCity());
		assertNotNull(created.getState());
		assertNotNull(created.getZipcode());
		assertNotNull(created.getPatientAddressLines());
		assertNotNull(created.getPatientAddressLines().size() > 0);
		
		LinkedList<PatientAddressLineDTO> lines = patientAddressDao.getById(created.getId()).getPatientAddressLines();
		
		assertEquals("5523 Research Park Drive", lines.get(0).getLine());
		assertEquals(1, lines.get(0).getLineOrder());
		assertEquals("Suite 370", lines.get(1).getLine());
		assertEquals(2, lines.get(1).getLineOrder());
		
	}

}
