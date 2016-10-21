package gov.ca.emsa.pulse.broker.manager;

import java.util.HashMap;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.opensaml.xml.io.MarshallingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;

import gov.ca.emsa.pulse.auth.user.CommonUser;
import gov.ca.emsa.pulse.broker.BrokerApplicationTestConfig;
import gov.ca.emsa.pulse.broker.dao.OrganizationDAO;
import gov.ca.emsa.pulse.broker.dto.OrganizationDTO;
import gov.ca.emsa.pulse.broker.dto.QueryDTO;
import gov.ca.emsa.pulse.broker.dto.QueryOrganizationDTO;
import gov.ca.emsa.pulse.common.domain.QueryStatus;
import gov.ca.emsa.pulse.broker.manager.impl.JSONUtils;
import gov.ca.emsa.pulse.broker.saml.SAMLInput;
import gov.ca.emsa.pulse.broker.saml.SamlGenerator;
import gov.ca.emsa.pulse.common.domain.AlternateCareFacility;
import gov.ca.emsa.pulse.common.domain.Patient;
import gov.ca.emsa.pulse.common.domain.PatientSearch;
import gov.ca.emsa.pulse.common.domain.QueryOrganizationStatus;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={BrokerApplicationTestConfig.class})
public class PatientSearchTest {
	@Autowired private SamlGenerator samlGenerator;
	@Autowired QueryManager queryManager;
	@Autowired OrganizationDAO orgDao;

	ResponseEntity<Patient> mockResponseEntity;
    MockRestServiceServer mockServer;
    @Mock RestTemplate mockRestTemplate;

    @Before
    public void setUp() {
        mockRestTemplate = new RestTemplate();
        mockServer = MockRestServiceServer.createServer(mockRestTemplate);
    }

	private OrganizationDTO org1, org2;

	@Test
	@Transactional
	public void searchPatients() {
		insertOrganizations();

		PatientSearch toSearch = new PatientSearch();
		toSearch.setGivenName("John");

		 mockServer
		 	.expect(MockRestRequestMatchers.requestTo(org1.getEndpointUrl()))
		 	.andExpect(MockRestRequestMatchers.method(HttpMethod.POST))
		 	.andRespond(MockRestResponseCreators.withSuccess(
		 			"[" +
		 			  "{" +
		 			    "\"orgPatientId\": \"PERS.EH.01\"," +
		 			    "\"givenName\": \"John\"," +
		 			    "\"familyName\": \"Doe\"," +
		 			    "\"dateOfBirth\": 2016-01-10,"+
		 			    "\"gender\": \"M\"," +
		 			    "\"phoneNumber\": \"3517869574\"," +
		 			    "\"ssn\": \"451674563\"" +
		 			    "\"address\": { " +
			 			    "\"street1\": \"11 Audacious Rd\"," +
			 			    "\"street2\": \"\"," +
			 			    "\"city\": \"Carmel\"," +
			 			    "\"state\": \"CA\"," +
			 			    "\"zipcode\": \"10598\"," +
			 			 "}" +
		 			  "}" +
		 			"]"
                 , MediaType.APPLICATION_JSON));

		 mockServer
		 	.expect(MockRestRequestMatchers.requestTo(org1.getEndpointUrl()))
		 	.andExpect(MockRestRequestMatchers.method(HttpMethod.POST))
		 	.andRespond(MockRestResponseCreators.withSuccess(
		 			"[" +
		 			  "{" +
		 			    "\"orgPatientId\": \"PERS.IHE.03\","+
		 			    "\"givenName\": \"John\","+
		 			    "\"familyName\": \"Snow\","+
		 			    "\"dateOfBirth\": 1983-02-05,"+
		 			    "\"gender\": \"M\","+
		 			    "\"phoneNumber\": \"9004783666\","+
		 			    "\"ssn\": \"451663333\""+
		 			    "\"address\": { " +
			 			    "\"street1\": \"6740 Sycamore Hill\","+
			 			    "\"street2\": \"\","+
			 			    "\"city\": \"San Francisco\","+
			 			    "\"state\": \"CA\","+
			 			    "\"zipcode\": \"10088\","+
			 			 "}"+
		 			  "}"+
		 			"]"
              , MediaType.APPLICATION_JSON));

		 SAMLInput samlMessage = null;
		 try {
			 samlMessage = createSAMLInput();
		 } catch(Exception ex) {
			 Assert.fail(ex.getMessage());
		 }

		 CommonUser user = createUser();

		 QueryDTO query = null;

		 try {
			 query = createQuery(user);
		 } catch(Exception ex) {
			 Assert.fail(ex.getMessage());
		 }

		 try {
			 query = queryManager.queryForPatientRecords(samlMessage, toSearch, query, user);
		 } catch(Exception ex) {
			 Assert.fail(ex.getMessage());
		 }

		 //TODO: this is so close - but doesn't work because the calls are asynch.
		// mockServer.verify();

		 Assert.assertNotNull(query);
		 Assert.assertNotNull(query.getId());

		 queryManager.delete(query.getId());

		 if(org1 != null) {
			 orgDao.delete(org1);
		 }
		 if(org2 != null) {
			 orgDao.delete(org2);
		 }
	}

	private void insertOrganizations() {
		org1 = new OrganizationDTO();
		org1.setOrganizationId(1L);
		org1.setName("IHE Org");
		org1.setAdapter("IHE");
		org1.setEndpointUrl("http://localhost:8090/mock/ihe/patients");
		org1.setActive(true);
		org1 = orgDao.create(org1);

		org2 = new OrganizationDTO();
		org2.setOrganizationId(2L);
		org2.setName("eHealth Org");
		org2.setAdapter("eHealth");
		org2.setEndpointUrl("http://localhost:8090/mock/ehealthexchange/patients");
		org2.setPassword("pwd");
		org2.setUsername("kekey");
		org2.setActive(true);
		org2 = orgDao.create(org2);
	}

	private String createSAMLMessage() throws MarshallingException {
		SAMLInput input = createSAMLInput();
		String samlMessage = samlGenerator.createSAML(input);
		return samlMessage;
	}

	private SAMLInput createSAMLInput() {
		SAMLInput input = new SAMLInput();
		input.setStrIssuer("https://idp.dhv.gov");
		input.setStrNameID("UserBrianLindsey");
		input.setStrNameQualifier("My Website");
		input.setSessionId("abcdedf1234567");

		HashMap<String, String> customAttributes = new HashMap<String,String>();
		customAttributes.put("RequesterFirstName", "Katy");
		customAttributes.put("RequestReason", "Patient is bleeding.");
		customAttributes.put("PatientGivenName", "John");
		customAttributes.put("PatientFamilyName", "");
		customAttributes.put("PatientDOB", "");
		customAttributes.put("PatientGender", "");
		customAttributes.put("PatientHomeZip", "");
		customAttributes.put("PatientSSN", "");

		input.setAttributes(customAttributes);
		return input;
	}
	
	private CommonUser createUser() {
		CommonUser user = new CommonUser();
		AlternateCareFacility acf = new AlternateCareFacility();
		acf.setId(1L);
		acf.setName("Tent 1");
		user.setAcf(acf);
		user.setSubjectName("kekey");
		user.setAuthenticated(true);
		user.setEmail("kekey@ainq.com");
		user.setFirstName("Katy");
		user.setLastName("Ekey");
		return user;
	}

	private QueryDTO createQuery(CommonUser user) throws JsonProcessingException {
		Patient queryTerms = new Patient();
		queryTerms.setGivenName("John");
		String queryTermsJson = JSONUtils.toJSON(queryTerms);

		QueryDTO query = new QueryDTO();
		query.setUserId(user.getSubjectName());
		query.setTerms(queryTermsJson);
		query.setStatus(QueryStatus.ACTIVE.name());
		query = queryManager.createQuery(query);

		//get the list of organizations
		List<OrganizationDTO> orgsToQuery = orgDao.findAll();
		for(OrganizationDTO org : orgsToQuery) {
			QueryOrganizationDTO queryOrg = new QueryOrganizationDTO();
			queryOrg.setOrgId(org.getId());
			queryOrg.setQueryId(query.getId());
			queryOrg.setStatus(QueryOrganizationStatus.Active);
			queryOrg = queryManager.createOrUpdateQueryOrganization(queryOrg);
			query.getOrgStatuses().add(queryOrg);
		}

		return query;
	}
}
