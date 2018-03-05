package gov.ca.emsa.pulse.broker.service;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import gov.ca.emsa.pulse.auth.jwt.TestUsers;
import gov.ca.emsa.pulse.broker.BrokerApplicationTestConfig;
import gov.ca.emsa.pulse.broker.dto.DomainToDtoConverter;
import gov.ca.emsa.pulse.broker.dto.DtoToDomainConverter;
import gov.ca.emsa.pulse.service.AlternateCareFacilityService;
import gov.ca.emsa.pulse.service.PatientService;
import gov.ca.emsa.pulse.service.SearchService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import com.fasterxml.jackson.core.JsonProcessingException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = BrokerApplicationTestConfig.class)
@WebAppConfiguration
public class SearchServiceSecurityTest extends BaseSecurityTest {
	@Autowired
	SearchService searchServiceController;

	protected String searchUrlPrefix;

	@Before
	public void setUp() throws JsonProcessingException, SQLException {
		super.setUp(searchServiceController);
		searchUrlPrefix = "/search";
	}

	@After
	public void restore() throws SQLException {
		super.restore();
	}

	@Test
	public void testSearch() throws Exception {
		// scenario 0: no user
		mockMvc.perform(post("/search").contentType(contentType).content(requestJsonPatientTest)).andExpect(status().isUnauthorized());

		// scenario 1: role_admin: allowed.
		TestUsers.setCurrentUser("ROLE_ADMIN", liferayStateIdUsedForTest, liferayAcfIdUsedForTest); // the info doesn't
																									// matter for
																									// role_admin
		mockMvc.perform(post("/search").contentType(contentType).content(requestJsonPatientTest)).andExpect(status().isOk());

		// scenario 2: role_org_admin: not allowed.
		TestUsers.setCurrentUser("ROLE_ORG_ADMIN", liferayStateIdUsedForTest, liferayAcfIdUsedForTest); // the info
																										// doesn't
																										// matter for
																										// role_admin
		mockMvc.perform(post("/search").contentType(contentType).content(requestJsonPatientTest)).andExpect(status().isUnauthorized());

		// scenario 3: role_provider: allowed.
		TestUsers.setCurrentUser("ROLE_PROVIDER", liferayStateIdUsedForTest, liferayAcfIdUsedForTest); // the info
																										// doesn't
																										// matter for
																										// role_admin
		mockMvc.perform(post("/search").contentType(contentType).content(requestJsonPatientTest)).andExpect(status().isOk());
	}
}
