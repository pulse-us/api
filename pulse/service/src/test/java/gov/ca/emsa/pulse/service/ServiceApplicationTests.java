package gov.ca.emsa.pulse.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gov.ca.emsa.pulse.ServiceApplication;
import gov.ca.emsa.pulse.auth.jwt.JWTAuthor;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.twitter.finagle.http.Status;

import static org.junit.Assert.*;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(classes = ServiceApplication.class)
@SpringApplicationConfiguration(ServiceApplication.class)
//@ComponentScan(basePackages = {"gov.ca.emsa.pulse.auth.**","gov.ca.emsa.pulse.auth.jwt.*"})
@WebAppConfiguration
public class ServiceApplicationTests {
	
	@Rule
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
		
	}

	//@Test
	//@WithMockUser(roles={"USER"}, username = "user")
	public void endpointAcceptsSearchPatient() throws Exception {
		
		ObjectMapper mapper = new ObjectMapper();
		
		stubFor(post(urlEqualTo("/patients"))
				.withQueryParam("firstName", containing("John"))
				.withRequestBody(equalToJson("{ \"name\": \"user\"}"))
				.willReturn(aResponse()
						.withStatus(200).withBody(mapper.writeValueAsString(new Query()))));

		String tokenHeader = "Bearer " + jwt;		
		//ResultActions ra = mvc.perform(get("/search/patient").with(user("user").roles("USER")).param("firstName","John"));
		PatientSearchTerms pst = new PatientSearchTerms();
		pst.setFirstName("John");
		ResultActions ra = mvc.perform(post("/search").contentType(MediaType.APPLICATION_JSON_UTF8)
				.sessionAttr("pst", pst).header("Authorization", tokenHeader));
		
		assertEquals(Status.Ok().code(), ra.andReturn().getResponse().getStatus());
		
	}
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
