package gov.ca.emsa.pulse.service.security;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.fasterxml.jackson.core.JsonProcessingException;

import gov.ca.emsa.pulse.ServiceApplicationTestConfig;
import gov.ca.emsa.pulse.service.LocationService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ServiceApplicationTestConfig.class)
@WebAppConfiguration
public class LocationServiceSecurityTest extends BaseSecurityTest {
    @Autowired
    LocationService locationServiceController;

    @Before
    public void setUp() throws JsonProcessingException, SQLException {
        super.setUp(locationServiceController);

    }

    @Test
    public void testGetLocations() throws Exception {
        mockMvc.perform(get("/locations")).andExpect(status().isUnauthorized());
    }

    @Test
    public void testGetPatientDiscoveryRequestStatistics() throws Exception {
        mockMvc.perform(get("/locations/statistics")).andExpect(status().isUnauthorized());
    }

}
