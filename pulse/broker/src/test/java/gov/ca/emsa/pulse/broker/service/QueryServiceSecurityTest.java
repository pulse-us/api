package gov.ca.emsa.pulse.broker.service;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import gov.ca.emsa.pulse.auth.jwt.TestUsers;
import gov.ca.emsa.pulse.broker.BrokerApplicationTestConfig;
import gov.ca.emsa.pulse.service.AlternateCareFacilityService;
import gov.ca.emsa.pulse.service.PatientService;
import gov.ca.emsa.pulse.service.QueryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fasterxml.jackson.core.JsonProcessingException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = BrokerApplicationTestConfig.class)
@WebAppConfiguration
public class QueryServiceSecurityTest extends BaseSecurityTest {
	@Autowired
	AlternateCareFacilityService acfServiceController;
	@Autowired
	QueryService queryServiceController;

	protected String queryUrlPrefix;

	@Before
	public void setUp() throws JsonProcessingException, SQLException {
		super.setUp(queryServiceController);
		queryUrlPrefix = "/queries";
	}

	@After
	public void restore() throws SQLException {
		super.restore();
	}

	/*
	 * Tests: @RequestMapping(value="/{queryId}/delete", method = RequestMethod.POST)
	@Secured({"ROLE_ADMIN", "ROLE_PROVIDER"})*/
	
	@Test
	public void getQueries() throws Exception {
		mockMvc.perform(get(queryUrlPrefix)).andExpect(status().isUnauthorized());
		
		TestUsers.setCurrentUser("ROLE_ORG_ADMIN", liferayStateIdUsedForTest + 1, 100L);
		mockMvc.perform(get(queryUrlPrefix)).andExpect(status().isUnauthorized());		
	}
}
