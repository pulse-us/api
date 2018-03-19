package gov.ca.emsa.pulse.broker.service;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.fasterxml.jackson.core.JsonProcessingException;

import gov.ca.emsa.pulse.auth.jwt.JWTUserTestHelper;
import gov.ca.emsa.pulse.broker.BrokerApplicationTestConfig;
import gov.ca.emsa.pulse.broker.dto.QueryDTO;
import gov.ca.emsa.pulse.broker.manager.QueryManager;
import gov.ca.emsa.pulse.common.domain.CreatePatientRequest;
import gov.ca.emsa.pulse.service.AlternateCareFacilityService;
import gov.ca.emsa.pulse.service.QueryService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = BrokerApplicationTestConfig.class)
@WebAppConfiguration
public class QueryServiceSecurityTest extends BaseSecurityTest {
    @Autowired
    AlternateCareFacilityService acfServiceController;
    @Autowired
    QueryService queryServiceController;
    @Autowired
    QueryManager queryManager;
    QueryDTO query;

    protected String queryUrlPrefix;

    @Before
    public void setUp() throws JsonProcessingException, SQLException {
        super.setUp(queryServiceController);
        queryUrlPrefix = "/queries";
        query = queryManager.getById(1L);
    }

    @Override
    @After
    public void restore() throws SQLException {
        super.restore();
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

        JWTUserTestHelper.setCurrentUser("ROLE_ORG_ADMIN", liferayStateIdUsedForTest + 1, 100L);
        mockMvc.perform(get(queryUrlPrefix)).andExpect(status().isUnauthorized());
    }

    @Test
    @Ignore
    public void testAllEndpoints() throws Exception {
        String[] endpointsGet = {
                "/queries/1",
        };

        String[] endpointsPost = {
                "/queries/1/endpoint/1/cancel", "/queries/1/endpoint/1/requery", "/queries/1/stage",
                "/queries/1/delete",

        };

        // queries fetch based on the subjectname
        JWTUserTestHelper.testPatternProvider(mockMvc, ow, "/queries", true);

        if (query != null) { // we could create one for this test
            for (String endpoint : endpointsGet) {
                JWTUserTestHelper.testPatternProviderWithUser(mockMvc, ow, endpoint, true,
                        query == null ? "" : query.getUserId());
            }
            for (String endpoint : endpointsPost) {
                JWTUserTestHelper.testPatternProviderWithUser(mockMvc, ow, endpoint, false, new CreatePatientRequest(),
                        query == null ? "" : query.getUserId());
            }
        }
    }
}
