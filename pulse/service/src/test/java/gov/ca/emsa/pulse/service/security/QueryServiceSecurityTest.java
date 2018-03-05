package gov.ca.emsa.pulse.service.security;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.sql.SQLException;

import gov.ca.emsa.pulse.ServiceApplicationTestConfig;
import gov.ca.emsa.pulse.auth.jwt.TestUsers;
import gov.ca.emsa.pulse.service.QueryService;
import io.swagger.annotations.ApiOperation;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fasterxml.jackson.core.JsonProcessingException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ServiceApplicationTestConfig.class)
@WebAppConfiguration
public class QueryServiceSecurityTest extends BaseSecurityTest {
	@Autowired
	QueryService queryServiceController;
	protected MockMvc mockMvc;

	String queryUrlPrefix = " /queries";

	@Before
	public void setUp() throws JsonProcessingException, SQLException {
		super.setUp(queryServiceController);

	}

	/*
	 * Tests: @RequestMapping(value="/{queryId}/delete", method =
	 * RequestMethod.POST)
	 * 
	 * @Secured({"ROLE_ADMIN", "ROLE_PROVIDER"})
	 */

	@Test
	public void getQueries() throws Exception {
		mockMvc.perform(get(queryUrlPrefix)).andExpect(status().isUnauthorized());

		TestUsers.setCurrentUser("ROLE_ORG_ADMIN", liferayStateIdUsedForTest + 1, 100L);
		mockMvc.perform(get(queryUrlPrefix)).andExpect(status().isUnauthorized());
	}

	@ApiOperation(value = "Create a Patient from multiple PatientRecords")
	@RequestMapping(value = "/{queryId}/stage", method = RequestMethod.POST)
	@Secured({ "ROLE_ADMIN", "ROLE_PROVIDER" })

	@Test
	public void testGetQueryWithId() throws Exception {
		mockMvc.perform(get("/queries/1")).andExpect(status().isUnauthorized());
	}

	@Test
	public void testDeleteQuery() throws Exception {
		mockMvc.perform(get("/queries/1/delete")).andExpect(status().isUnauthorized());
	}

	@Test
	public void testCancelPatientDiscoveryRequestToEndpoint() throws Exception {
		mockMvc.perform(get("/queries/1/endpoint/1/cancel")).andExpect(status().isUnauthorized());
	}

	@Test
	public void testRequeryPatients() throws Exception {
		mockMvc.perform(get("/queries/1/endpoint/1/requery")).andExpect(status().isUnauthorized());
	}

	@Test
	public void testStagePatientFromQueryResults() throws Exception {
		mockMvc.perform(get("/queries/1/stage")).andExpect(status().isUnauthorized());
	}

}
