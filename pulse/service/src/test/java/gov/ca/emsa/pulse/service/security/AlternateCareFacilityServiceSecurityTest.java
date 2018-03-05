package gov.ca.emsa.pulse.service.security;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.sql.SQLException;

import gov.ca.emsa.pulse.ServiceApplicationTestConfig;
import gov.ca.emsa.pulse.auth.jwt.TestUsers;
import gov.ca.emsa.pulse.service.AlternateCareFacilityService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MvcResult;
import com.fasterxml.jackson.core.JsonProcessingException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ServiceApplicationTestConfig.class)
@WebAppConfiguration
public class AlternateCareFacilityServiceSecurityTest extends BaseSecurityTest {
	@Autowired
	AlternateCareFacilityService acfServiceController;
	// @Autowired private AlternateCareFacilityManager acfManager;

	@Before
	public void setUp() throws JsonProcessingException, SQLException {
		super.setUp(acfServiceController);
	}

	@Test
	public void testGetAcfs() throws Exception {
		mockMvc.perform(get("/acfs")).andExpect(status().isUnauthorized());

		// scenario 1: user=ROLE_ADMIN, gets all acfs.
		TestUsers.setCurrentUser("ROLE_ADMIN", 999999L, 99999L); // the info doesn't matter for role_admin

		MvcResult result = mockMvc.perform(get("/acfs")).andExpect(status().isOk()).andReturn();

		// we need to setup the db - this is done in the broker level
		String out = result.getResponse().getContentAsString();

		// System.out.println(result.getResponse().getContentAsString());
		// System.out.println("Matches = " + StringUtils.countMatches(out, "<item>"));
		// assertTrue(StringUtils.countMatches(out, "<item>") > 0);

		// scenario 2a: user=ROLE_ORG_ADMIN, state=9009 (arbitrary), gets none.
		TestUsers.setCurrentUser("ROLE_ORG_ADMIN", 9009L, 100L); // the info doesn't matter for role_admin
		result = mockMvc.perform(get("/acfs")).andExpect(status().isOk()).andReturn();

		out = result.getResponse().getContentAsString();
		// assertTrue(StringUtils.countMatches(out, "<item>") == 0);

		// scenario 2b: user=ROLE_ORG_ADMIN, state=10, gets only 1 acf
		TestUsers.setCurrentUser("ROLE_ORG_ADMIN", 10L, 10000L /* ignored */);
		result = mockMvc.perform(get("/acfs")).andExpect(status().isOk()).andReturn();

		out = result.getResponse().getContentAsString();
		// assertTrue(StringUtils.countMatches(out, "<item>") == 1);

		// scenario 3a: user=ROLE_PROVIDER, state=9009, gets none.
		TestUsers.setCurrentUser("ROLE_ORG_ADMIN", 9009L, 100L);
		result = mockMvc.perform(get("/acfs")).andExpect(status().isOk()).andReturn();

		out = result.getResponse().getContentAsString();
		// assertTrue(StringUtils.countMatches(out, "<item>") == 0);

		// scenario 3b: user=ROLE_PROVIDER, state=10, gets only 1 acf.
		TestUsers.setCurrentUser("ROLE_ORG_ADMIN", 10L, 100L);
		result = mockMvc.perform(get("/acfs")).andExpect(status().isOk()).andReturn();

		out = result.getResponse().getContentAsString();
		// assertTrue(StringUtils.countMatches(out, "<item>") == 1);
	}

	@Test
	public void testgetACFById() throws Exception {
		mockMvc.perform(get("/acfs/1")).andExpect(status().isUnauthorized());

		TestUsers.setCurrentUser("ROLE_ORG_ADMIN", 10L, 100L);
		mockMvc.perform(get("/acfs/1")).andExpect(status().isUnauthorized());
	}

	@Test
	public void testCreate() throws Exception {
		/*
		 * the complete suite is run at the broker level as the db needs to be prepared
		 */
		/* scenario 0: unauthorized */
		mockMvc.perform(post("/acfs/create").contentType(contentType).content(requestJsonAcf))
				.andExpect(status().isUnauthorized());

		/* scenario 1: role provider - currently not allowed */
		TestUsers.setCurrentUser("ROLE_PROVIDER", 999999L, 99999L); // the info doesn't matter for role_admin
		mockMvc.perform(post("/acfs/create").contentType(contentType).content(requestJsonAcf))
				.andExpect(status().isUnauthorized());

	}

	@Test
	public void testAcfEdit() throws Exception {
		acfIdUsedForTest = 1L;
		String testAcfEditUrl = "/acfs/" + acfIdUsedForTest + "/edit";
		// scenario 0: unauthorized
		mockMvc.perform(post(testAcfEditUrl).contentType(contentType).content(requestJsonAcf))
				.andExpect(status().isUnauthorized());

		// scenario : provider not allowed allowed
		TestUsers.setCurrentUser("ROLE_PROVIDER", liferayStateIdUsedForTest + 1, liferayAcfIdUsedForTest + 1); // the
																												// info
																												// doesn't
		mockMvc.perform(post(testAcfEditUrl).contentType(contentType).content(requestJsonAcf))
				.andExpect(status().isUnauthorized());

		// scenario 2: org_admin from a different state not allowed
		TestUsers.setCurrentUser("ROLE_ORG_ADMIN", liferayStateIdUsedForTest + 1, 100L);
		mockMvc.perform(post(testAcfEditUrl).contentType(contentType).content(requestJsonAcf))
				.andExpect(status().isUnauthorized());
	}

	/*
	 * 
	 * @Test public void testAcfDelete() throws Exception {
	 * mockMvc.perform(post("/acfs/1/delete")) .andExpect(status().isOk()); }
	 * 
	 * public void testAcfDesignate() throws Exception {
	 * mockMvc.perform(post("/acfs/1/delete")) .andExpect(status().isOk()); }
	 *
	 *
	 *
	 *
	 *
	 *
	 *
	 * 
	 */

}