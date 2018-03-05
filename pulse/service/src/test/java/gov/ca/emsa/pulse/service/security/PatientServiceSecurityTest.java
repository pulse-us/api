package gov.ca.emsa.pulse.service.security;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.sql.SQLException;

import gov.ca.emsa.pulse.ServiceApplicationTestConfig;
import gov.ca.emsa.pulse.service.PatientService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.core.JsonProcessingException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ServiceApplicationTestConfig.class)
@WebAppConfiguration
public class PatientServiceSecurityTest extends BaseSecurityTest {
	@Autowired
	PatientService patientServiceController;
	protected MockMvc mockMvc;

	@Before
	public void setUp() throws JsonProcessingException, SQLException {
		super.setUp(patientServiceController);

	}

	@Test
	public void testGetPatientsAtAcf() throws Exception {
		mockMvc.perform(get("/patients")).andExpect(status().isUnauthorized());
	}

	@Test
	public void testGetDocumentListForPatient() throws Exception {
		mockMvc.perform(get("/patients/1/documents")).andExpect(status().isUnauthorized());
	}

	@Test
	public void testCancelDocumentListQuery() throws Exception {
		mockMvc.perform(get("/patients/1/endpoints/1/cancel")).andExpect(status().isUnauthorized());
	}

	@Test
	public void testRedoDocumentListQuery() throws Exception {
		mockMvc.perform(get("/patients/1/endpoints/1/requery")).andExpect(status().isOk());
	}

	@Test
	public void testGetDocumentContents() throws Exception {
		mockMvc.perform(get("/patients/1/documents/1")).andExpect(status().isOk());
	}

	@Test
	public void testEdit() throws Exception {
		mockMvc.perform(get("/patients/1/edit")).andExpect(status().isUnauthorized());
	}

	@Test
	public void testDelete() throws Exception {
		mockMvc.perform(get("/patients/1/delete")).andExpect(status().isUnauthorized());
	}

}
