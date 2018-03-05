package gov.ca.emsa.pulse.service.security;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.sql.SQLException;

import gov.ca.emsa.pulse.ServiceApplicationTestConfig;
import gov.ca.emsa.pulse.service.DocumentService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import com.fasterxml.jackson.core.JsonProcessingException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ServiceApplicationTestConfig.class)
@WebAppConfiguration
public class DocumentServiceSecurityTest extends BaseSecurityTest {
	@Autowired
	DocumentService documentServiceController;

	@Before
	public void setUp() throws JsonProcessingException, SQLException {
		super.setUp(documentServiceController);

	}

	@Test
	public void testSearchDocuments() throws Exception {
		mockMvc.perform(get("/patients/1/documents")).andExpect(status().isUnauthorized());
	}

	@Test
	public void testCancelDocumentListQuery() throws Exception {
		mockMvc.perform(get("/patients/1/endpoints/1/cancel")).andExpect(status().isUnauthorized());
	}

	@Test
	public void testRedoDocumentListQuery() throws Exception {
		mockMvc.perform(get("/patients/1/endpoints/1/requery")).andExpect(status().isUnauthorized());
	}

	@Test
	public void testGetDocumentContents() throws Exception {
		mockMvc.perform(get("/patients/1/documents/1")).andExpect(status().isUnauthorized());
	}

	@Test
	public void testCancelDocumentContentQuery() throws Exception {
		mockMvc.perform(get("/patients/1/documents/1/cancel")).andExpect(status().isUnauthorized());
	}

}
