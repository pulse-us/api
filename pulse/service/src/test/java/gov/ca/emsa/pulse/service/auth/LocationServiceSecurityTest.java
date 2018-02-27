package gov.ca.emsa.pulse.service.auth;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import gov.ca.emsa.pulse.ServiceApplicationTestConfig;
import gov.ca.emsa.pulse.ServiceExceptionControllerAdvice;
import gov.ca.emsa.pulse.service.LocationService;
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
public class LocationServiceSecurityTest {
	@Autowired LocationService locationServiceController;
	protected MockMvc mockMvc;

	@Before
	public void setUp() {
		this.mockMvc = MockMvcBuilders.standaloneSetup(locationServiceController)
                .setControllerAdvice(new ServiceExceptionControllerAdvice())
                .build();
	}
	
	@Test
	public void testGetLocations() throws Exception {
	    mockMvc.perform(get("/locations"))
        .andExpect(status().isUnauthorized());
	}
	
	@Test
	public void testGetPatientDiscoveryRequestStatistics() throws Exception {
	    mockMvc.perform(get("/locations/statistics"))
        .andExpect(status().isUnauthorized());
	}
	
}
