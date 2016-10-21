package gov.ca.emsa.pulse.broker.dao;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import gov.ca.emsa.pulse.broker.BrokerApplicationTestConfig;
import gov.ca.emsa.pulse.broker.dto.PatientRecordAddressDTO;
import gov.ca.emsa.pulse.broker.dto.PatientRecordAddressLineDTO;

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
	
	@Test
	@Transactional
	@Rollback(true)
	public void createPatientAddressTest(){
		PatientRecordAddressDTO patientAddressDto = new PatientRecordAddressDTO();
		patientAddressDto.setCity("Bel Air");
		patientAddressDto.setCreationDate(new Date());
		patientAddressDto.setState("MD");
		patientAddressDto.setZipcode("21015");
		PatientRecordAddressDTO patientAddressCreated = patientAddressDao.create(patientAddressDto);
		
		assertNotNull(patientAddressCreated);
		assertNotNull(patientAddressCreated.getCity());
		assertNotNull(patientAddressCreated.getState());
		assertNotNull(patientAddressCreated.getZipcode());
		
		PatientRecordAddressLineDTO patientAddressLineDto1 = new PatientRecordAddressLineDTO();
		patientAddressLineDto1.setLine("5523 Research Park Drive");
		patientAddressLineDto1.setLineOrder(1);
		patientAddressLineDto1.setPatientRecordAddressId(patientAddressCreated.getId());
		PatientRecordAddressLineDTO patientAddressLineCreated1 = patientAddressLineDao.create(patientAddressLineDto1);
		
		assertNotNull(patientAddressLineCreated1);
		assertNotNull(patientAddressLineCreated1.getLine());
		
		PatientRecordAddressLineDTO patientAddressLineGetById1 = patientAddressLineDao.getById(patientAddressLineCreated1.getId());
		
		assertNotNull(patientAddressLineGetById1);
		assertNotNull(patientAddressLineGetById1.getLine());
		assertNotNull(patientAddressLineGetById1.getLineOrder());
		
		PatientRecordAddressLineDTO patientAddressLineDto2 = new PatientRecordAddressLineDTO();
		patientAddressLineDto2.setLine("Suite 370");
		patientAddressLineDto2.setLineOrder(2);
		patientAddressLineDto2.setPatientRecordAddressId(patientAddressCreated.getId());
		PatientRecordAddressLineDTO patientAddressLineCreated2 = patientAddressLineDao.create(patientAddressLineDto2);
		
		PatientRecordAddressLineDTO patientAddressLineGetById2 = patientAddressLineDao.getById(patientAddressLineCreated2.getId());
		
		assertNotNull(patientAddressLineGetById2);
		assertNotNull(patientAddressLineGetById2.getLine());
		assertNotNull(patientAddressLineGetById2.getLineOrder());
		
		assertNotNull(patientAddressLineCreated2);
		assertNotNull(patientAddressLineCreated2.getLine());
		
		PatientRecordAddressDTO created = patientAddressDao.getById(patientAddressCreated.getId());
		
		assertNotNull(created);
		assertNotNull(created.getCity());
		assertNotNull(created.getState());
		assertNotNull(created.getZipcode());
		assertNotNull(created.getPatientRecordAddressLines());
		assertNotNull(created.getPatientRecordAddressLines().size() > 0);
		
		List<PatientRecordAddressLineDTO> lines = created.getPatientRecordAddressLines();
		
		assertEquals("5523 Research Park Drive", lines.get(0).getLine());
		assertEquals(1, lines.get(0).getLineOrder());
		assertEquals("Suite 370", lines.get(1).getLine());
		assertEquals(2, lines.get(1).getLineOrder());
		
	}

}
