package gov.ca.emsa.pulse.service;

import java.util.ArrayList;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import junit.framework.TestCase;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.twitter.finagle.http.Status;

import static org.junit.Assert.*;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
public class ServiceApplicationTests {
	
	/*@Rule
	public WireMockRule wireMockRule = new WireMockRule(8090);
	
	@Autowired
	private WebApplicationContext context;
	
	@Autowired
	private JWTAuthor jwtAuthor;

	private String jwt;
	
	private MockMvc mvc;
	
	@Before
	public void setup() {
		mvc = MockMvcBuilders.webAppContextSetup(context).apply(SecurityMockMvcConfigurers.springSecurity()).build();
		Map<String, List<String>> claims = new HashMap<String, List<String>>();
		List<String> roles = new ArrayList<String>();
		roles.add("ROLE_USER");
		claims.put("Authorities", roles);
		Map<String, List<String>> identity = new HashMap<String, List<String>>();
		List<String> id = new ArrayList<String>();
		id.add("brian");
		id.add("lindsey");
		id.add("blindsey@ainq.com");
		claims.put("Identity", id);
		jwt = jwtAuthor.createJWT("user", claims);
		
	}*/
	
	@Test
	public void test(){
		assertEquals(1+1,2);
	}

	/*@Test
	public void endpointAcceptsSearchPatient() throws Exception {
		
		ObjectMapper mapper = new ObjectMapper();
		
		stubFor(post(urlEqualTo("/patients"))
				.withQueryParam("firstName", containing("John"))
				.withRequestBody(equalToJson("{ \"name\": \"user\", \"acf\":\"ACF1\"}"))
				.willReturn(aResponse()
						.withStatus(200).withBody(mapper.writeValueAsString(new Query()))));

		String tokenHeader = "Bearer " + jwt;
		PatientSearchTerms pst = new PatientSearchTerms();
		pst.setFirstName("John");
		ResultActions ra = mvc.perform(post("/search").contentType(MediaType.APPLICATION_JSON_UTF8)
				.sessionAttr("patientSearchTerms", pst).header("Authorization", tokenHeader));
		
		assertEquals(Status.Ok().code(), ra.andReturn().getResponse().getStatus());
		
	}*/
	/* 
	@Test
	public void endpointRejectsSearchPatient() throws Exception {
		setup();
		mvc.perform(get("/search/patient"))
		.andExpect(status().isUnauthorized());
	}
	
	@Test
	public void endpointAcceptsRequestQuery() throws Exception {
		setup();
	
		mvc.perform(get("/search/patient/query/1").with(user("user").roles("USER")))
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
	}
	
	@Test
	public void endpointRejectsRequestQuery() throws Exception {
		setup();
		mvc.perform(get("/search/patient/query/1"))
		.andExpect(status().isUnauthorized());
	}
	
	@Test
	public void endpointAcceptsRequestSearchDocuments() throws Exception {
		setup();
		
		mvc.perform(get("/search/documents").with(user("user").roles("USER")))
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
	}
	
	@Test
	public void endpointRejectsRequestSearchDocuments() throws Exception {
		setup();
		mvc.perform(get("/search/documents"))
		.andExpect(status().isUnauthorized());
	}
	
	@Test
	public void endpointAcceptsRequestGetDocument() throws Exception {
		setup();
		
		mvc.perform(get("/search/document/1").with(user("user").roles("USER")))
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
	}
	
	@Test
	public void endpointRejectsRequestGetDocument() throws Exception {
		setup();
		mvc.perform(get("/search/document/1"))
		.andExpect(status().isUnauthorized());
	}
	*/
}
