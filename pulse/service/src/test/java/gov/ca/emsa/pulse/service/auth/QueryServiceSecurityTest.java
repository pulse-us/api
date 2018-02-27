package gov.ca.emsa.pulse.service.auth;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import gov.ca.emsa.pulse.ServiceApplicationTestConfig;
import gov.ca.emsa.pulse.ServiceExceptionControllerAdvice;
import gov.ca.emsa.pulse.service.QueryService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=ServiceApplicationTestConfig.class)
@WebAppConfiguration
public class QueryServiceSecurityTest {
	@Autowired QueryService queryServiceController;
	protected MockMvc mockMvc;

	@Before
	public void setUp() {
		this.mockMvc = MockMvcBuilders.standaloneSetup(queryServiceController)
                .setControllerAdvice(new ServiceExceptionControllerAdvice())
                .build();
	}
	
	@Test
	public void testGetQueries() throws Exception {
	    mockMvc.perform(get("/queries"))
        .andExpect(status().isUnauthorized());
	}
	
	@Test
	public void testGetQueryWithId() throws Exception {
	    mockMvc.perform(get("/queries/1"))
        .andExpect(status().isUnauthorized());
	}
	
	
	@Test
	public void testDeleteQuery() throws Exception {
	    mockMvc.perform(get("/queries/1/delete"))
        .andExpect(status().isUnauthorized());
	}
	
	@Test
	public void testCancelPatientDiscoveryRequestToEndpoint() throws Exception {
	    mockMvc.perform(get("/queries/1/endpoint/1/cancel"))
        .andExpect(status().isUnauthorized());
	}
	
	@Test
	public void testRequeryPatients() throws Exception {
	    mockMvc.perform(get("/queries/1/endpoint/1/requery"))
        .andExpect(status().isUnauthorized());
	}
	
	@Test
	public void testStagePatientFromQueryResults() throws Exception {
	    mockMvc.perform(get("/queries/1/stage"))
        .andExpect(status().isUnauthorized());
	}
	
	
}