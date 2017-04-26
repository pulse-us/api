package gov.ca.emsa.pulse.broker.dao;

import static org.junit.Assert.*;

import java.io.IOException;
import java.sql.SQLException;

import gov.ca.emsa.pulse.broker.BrokerApplicationTestConfig;
import gov.ca.emsa.pulse.broker.dto.AlternateCareFacilityDTO;
import gov.ca.emsa.pulse.broker.dto.PulseUserDTO;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={BrokerApplicationTestConfig.class})
public class PulserUserDaoTest {
	
	@Autowired private ResourceLoader resourceLoader;
	
	@Autowired private PulseUserDAO pulseUserDao;
	
	public String getAssertion() throws IOException{
		Resource assertionFile = resourceLoader.getResource("classpath:assertion.xml");
		return Resources.toString(assertionFile.getURL(), Charsets.UTF_8);
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void createUser() throws SQLException, IOException {
		PulseUserDTO pulseUserToCreate = new PulseUserDTO();
		pulseUserToCreate.setAssertion(getAssertion());
		
		PulseUserDTO pulseUser = null;
		pulseUser = pulseUserDao.create(pulseUserToCreate);
		assertNotNull(pulseUser);
		assertNotNull(pulseUser.getId());
		assertEquals(pulseUser.getAssertion(),getAssertion());
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void createUserGetById() throws SQLException, IOException {
		PulseUserDTO pulseUserToCreate = new PulseUserDTO();
		pulseUserToCreate.setAssertion(getAssertion());
		
		PulseUserDTO pulseUser = null;
		pulseUser = pulseUserDao.create(pulseUserToCreate);
		PulseUserDTO pulseUserCreated = pulseUserDao.getById(pulseUser.getId());
		assertNotNull(pulseUserCreated);
		assertNotNull(pulseUserCreated.getId());
		assertEquals(pulseUserCreated.getAssertion(),getAssertion());
	}

}
