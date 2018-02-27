package gov.ca.emsa.pulse.service.auth;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import gov.ca.emsa.pulse.ServiceApplicationTestConfig;
import gov.ca.emsa.pulse.ServiceExceptionControllerAdvice;
import gov.ca.emsa.pulse.service.AlternateCareFacilityService;
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
public class AlternateCareFacilityServiceSecurityTest {
	@Autowired AlternateCareFacilityService acfServiceController;
	protected MockMvc mockMvc;

	@Before
	public void setUp() {
		this.mockMvc = MockMvcBuilders.standaloneSetup(acfServiceController)
                .setControllerAdvice(new ServiceExceptionControllerAdvice())
                .build();
	}
	
	@Test
	public void testGetAcfs() throws Exception {
		    mockMvc.perform(get("/acfs"))
		        .andExpect(status().isUnauthorized());
	}
	
	@Test
	public void testgetACFById() throws Exception {
	    mockMvc.perform(get("/acfs/1"))
        .andExpect(status().isUnauthorized());
        	//.andExpect(status().isOk());
	}
	
	@Test
	public void testCreate() throws Exception {
	    mockMvc.perform(post("/acfs/create"))
        .andExpect(status().isUnauthorized());
	}

	@Test
	public void testAcfEdit() throws Exception {
	    mockMvc.perform(post("/acfs/1/edit"))
        .andExpect(status().isUnauthorized());
	}
	
	/* 
	
	@Test
	public void testAcfDelete() throws Exception {
	    mockMvc.perform(post("/acfs/1/delete"))
	        .andExpect(status().isOk());
	}
	
	public void testAcfDesignate() throws Exception {
	    mockMvc.perform(post("/acfs/1/delete"))
	        .andExpect(status().isOk());
	}
	
	*
	*
	*
	*
	*
	*
	*
	*/
	
	
}
