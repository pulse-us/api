package gov.ca.emsa.pulse.service.security;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.sql.SQLException;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.fasterxml.jackson.core.JsonProcessingException;

import gov.ca.emsa.pulse.ServiceApplicationTestConfig;
import gov.ca.emsa.pulse.auth.jwt.JWTUserTestHelper;
import gov.ca.emsa.pulse.service.QueryService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ServiceApplicationTestConfig.class)
@WebAppConfiguration
public class QueryServiceSecurityTest extends BaseSecurityTest {
    @Autowired
    QueryService queryServiceController;

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
    @Ignore
    public void getQueries() throws Exception {
        mockMvc.perform(get(queryUrlPrefix)).andExpect(status().isUnauthorized());

        JWTUserTestHelper.setCurrentUser("ROLE_ORG_ADMIN", liferayStateIdUsedForTest + 1, 100L);
        mockMvc.perform(get(queryUrlPrefix)).andExpect(status().isUnauthorized());
    }

    @Test
    @Ignore
    public void testGetQueryWithId() throws Exception {
        mockMvc.perform(get("/queries/1")).andExpect(status().isUnauthorized());
    }

    @Test
    @Ignore
    public void testDeleteQuery() throws Exception {
        mockMvc.perform(get("/queries/1/delete")).andExpect(status().isUnauthorized());
    }

    @Test
    @Ignore
    public void testCancelPatientDiscoveryRequestToEndpoint() throws Exception {
        mockMvc.perform(get("/queries/1/endpoint/1/cancel")).andExpect(status().isUnauthorized());
    }

    @Test
    @Ignore
    public void testRequeryPatients() throws Exception {
        mockMvc.perform(get("/queries/1/endpoint/1/requery")).andExpect(status().isUnauthorized());
    }

    @Test
    @Ignore
    public void testStagePatientFromQueryResults() throws Exception {
        mockMvc.perform(get("/queries/1/stage")).andExpect(status().isUnauthorized());
    }

}
