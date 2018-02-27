package gov.ca.emsa.pulse.service.auth;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import gov.ca.emsa.pulse.ServiceApplicationTestConfig;
import gov.ca.emsa.pulse.ServiceExceptionControllerAdvice;
import gov.ca.emsa.pulse.service.DocumentService;
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
public class DocumentServiceSecurityTest {
	@Autowired DocumentService documentServiceController;
	protected MockMvc mockMvc;

	@Before
	public void setUp() {
		this.mockMvc = MockMvcBuilders.standaloneSetup(documentServiceController)
                .setControllerAdvice(new ServiceExceptionControllerAdvice())
                .build();
	}
	
	@Test
	public void testSearchDocuments() throws Exception {
	    mockMvc.perform(get("/patients/1/documents"))
        .andExpect(status().isUnauthorized());
	}

	@Test
	public void testCancelDocumentListQuery() throws Exception {
	    mockMvc.perform(get("/patients/1/endpoints/1/cancel"))
        .andExpect(status().isUnauthorized());
	}

	@Test
	public void testRedoDocumentListQuery() throws Exception {
	    mockMvc.perform(get("/patients/1/endpoints/1/requery"))
        .andExpect(status().isUnauthorized());
	}

	@Test
	public void testGetDocumentContents() throws Exception {
	    mockMvc.perform(get("/patients/1/documents/1"))
        .andExpect(status().isUnauthorized());
	}
	
	@Test
	public void testCancelDocumentContentQuery() throws Exception {
	    mockMvc.perform(get("/patients/1/documents/1/cancel"))
        .andExpect(status().isUnauthorized());
	}
	
}
