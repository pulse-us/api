package gov.ca.emsa.pulse.service;
import gov.ca.emsa.pulse.ServiceApplicationTestConfig;
import gov.ca.emsa.pulse.common.domain.PatientSearch;
import gov.ca.emsa.pulse.common.domain.QueryOrganization;

import org.hl7.v3.PRPAIN201305UV02;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.opensaml.common.SAMLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

import static org.junit.Assert.*;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.xml.soap.SOAPException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=ServiceApplicationTestConfig.class)
@WebAppConfiguration
public class ServiceApplicationTests {
	
	private static final String jwt = "eyJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJDQUxFTVNBIiwiYXVkIjoiQ0FMRU1TQSIsImV4cCI6MTQ3OTc3MDcwMSwianRpIjoiVFcxbEVLTFZ4X3NRTGR5NnQtY3RyQSIsImlhdCI6MTQ3Mzc3MDcwMSwibmJmIjoxNDczNzcwNDYxLCJzdWIiOiJmYWtlQHNhbXBsZS5jb20iLCJBdXRob3JpdGllcyI6WyJST0xFX1VTRVIiXSwiSWRlbnRpdHkiOlsiRmFrZSIsIlBlcnNvbiIsImZha2VAc2FtcGxlLmNvbSJdfQ.N6Kxy9m0ZXagBsBMy3egOT4Vk9pqM3C6ujFgUlok-8kaupwSF_qt-0Q7_oV6eYOgiaUuUuDTHoU7VkFwKG51DeJFZJYb_rSvy3gAAwDEahePYxNlQGpoc39LlN3EltM1U9rZ9Vj8HlgtE8DMUcmG080D0cZDpFDEdADbEchl8KkIMs_tnhs6Rour4KGymcB5lCHhdVdYH_AMKqZ6qWk2Rh2Ejd6SzRPydmcdR_7jIUKdbsgvjFr0I_l2G_tZggJssGx2zmJmyaMsWGnQOfibojugQiZ16qkpHKPAnG9sdGV6_yfHg8gU9dKx1MJOXkvcatJ3cuLRZIwHJQ-k8zLocA";
	private static final String brokerSearchCompletedQueryResponse = "[{\"id\":1,\"userToken\":\"fake@sample.com\",\"status\":\"COMPLETE\",\"terms\":{\"givenName\":\"John\",\"familyName\":\"Doe\",\"dob\":null,\"ssn\":null,\"gender\":null,\"zip\":null},\"lastRead\":1473775608988,\"orgStatuses\":[{\"id\":141,\"queryId\":1,\"org\":{\"name\":\"eHealthExchangeOrg3\",\"id\":3,\"organizationId\":3,\"adapter\":\"eHealth\",\"ipAddress\":\"127.0.0.1\",\"username\":\"org1User\",\"password\":\"password1\",\"certificationKey\":null,\"endpointUrl\":\"http://localhost:9080/mock/ehealthexchange\",\"active\":true},\"status\":\"COMPLETE\",\"startDate\":1473775522671,\"endDate\":1473775555980,\"success\":true,\"results\":[{\"id\":85,\"givenName\":\"John\",\"familyName\":\"Doe\",\"dateOfBirth\":-461030400000,\"gender\":\"M\",\"phoneNumber\":\"3517869574\",\"address\":null,\"ssn\":\"451674563\"}]},{\"id\":139,\"queryId\":24,\"org\":{\"name\":\"eHealthExchangeOrg\",\"id\":1,\"organizationId\":1,\"adapter\":\"eHealth\",\"ipAddress\":\"127.0.0.1\",\"username\":\"org1User\",\"password\":\"password1\",\"certificationKey\":null,\"endpointUrl\":\"http://localhost:9080/mock/ehealthexchange\",\"active\":true},\"status\":\"COMPLETE\",\"startDate\":1473775522668,\"endDate\":1473775608969,\"success\":true,\"results\":[{\"id\":87,\"givenName\":\"John\",\"familyName\":\"Doe\",\"dateOfBirth\":-461030400000,\"gender\":\"M\",\"phoneNumber\":\"3517869574\",\"address\":null,\"ssn\":\"451674563\"}]},{\"id\":140,\"queryId\":24,\"org\":{\"name\":\"eHealthExchangeOrg2\",\"id\":2,\"organizationId\":2,\"adapter\":\"eHealth\",\"ipAddress\":\"127.0.0.1\",\"username\":\"org1User\",\"password\":\"password1\",\"certificationKey\":null,\"endpointUrl\":\"http://localhost:9080/mock/ehealthexchange\",\"active\":true},\"status\":\"COMPLETE\",\"startDate\":1473775522670,\"endDate\":1473775607975,\"success\":true,\"results\":[{\"id\":86,\"givenName\":\"John\",\"familyName\":\"Doe\",\"dateOfBirth\":-461030400000,\"gender\":\"M\",\"phoneNumber\":\"3517869574\",\"address\":null,\"ssn\":\"451674563\"}]},{\"id\":144,\"queryId\":24,\"org\":{\"name\":\"IHEOrg3\",\"id\":6,\"organizationId\":6,\"adapter\":\"IHE\",\"ipAddress\":\"127.0.0.1\",\"username\":null,\"password\":null,\"certificationKey\":\"1234567\",\"endpointUrl\":\"http://localhost:9080/mock/ihe\",\"active\":true},\"status\":\"COMPLETE\",\"startDate\":1473775522676,\"endDate\":1473775561522,\"success\":true,\"results\":[]},{\"id\":142,\"queryId\":24,\"org\":{\"name\":\"IHEOrg\",\"id\":4,\"organizationId\":4,\"adapter\":\"IHE\",\"ipAddress\":\"127.0.0.1\",\"username\":null,\"password\":null,\"certificationKey\":\"1234567\",\"endpointUrl\":\"http://localhost:9080/mock/ihe\",\"active\":true},\"status\":\"COMPLETE\",\"startDate\":1473775522673,\"endDate\":1473775592713,\"success\":true,\"results\":[]},{\"id\":143,\"queryId\":24,\"org\":{\"name\":\"IHEOrg2\",\"id\":5,\"organizationId\":5,\"adapter\":\"IHE\",\"ipAddress\":\"127.0.0.1\",\"username\":null,\"password\":null,\"certificationKey\":\"1234567\",\"endpointUrl\":\"http://localhost:9080/mock/ihe\",\"active\":true},\"status\":\"COMPLETE\",\"startDate\":1473775522674,\"endDate\":1473775587889,\"success\":true,\"results\":[]}]}]";
	private static final String brokerSearchActiveQueryResponse = "[{\"id\":1,\"userToken\":\"fake@sample.com\",\"status\":\"ACTIVE\",\"terms\":{\"givenName\":\"John\",\"familyName\":\"Doe\",\"dob\":null,\"ssn\":null,\"gender\":null,\"zip\":null},\"lastRead\":1473775555673,\"orgStatuses\":[{\"id\":144,\"queryId\":1,\"org\":{\"name\":\"IHEOrg3\",\"id\":6,\"organizationId\":6,\"adapter\":\"IHE\",\"ipAddress\":\"127.0.0.1\",\"username\":null,\"password\":null,\"certificationKey\":\"1234567\",\"endpointUrl\":\"http://localhost:9080/mock/ihe\",\"active\":true},\"status\":\"ACTIVE\",\"startDate\":1473775522676,\"endDate\":null,\"success\":null,\"results\":[]},{\"id\":143,\"queryId\":24,\"org\":{\"name\":\"IHEOrg2\",\"id\":5,\"organizationId\":5,\"adapter\":\"IHE\",\"ipAddress\":\"127.0.0.1\",\"username\":null,\"password\":null,\"certificationKey\":\"1234567\",\"endpointUrl\":\"http://localhost:9080/mock/ihe\",\"active\":true},\"status\":\"ACTIVE\",\"startDate\":1473775522674,\"endDate\":null,\"success\":null,\"results\":[]},{\"id\":141,\"queryId\":24,\"org\":{\"name\":\"eHealthExchangeOrg3\",\"id\":3,\"organizationId\":3,\"adapter\":\"eHealth\",\"ipAddress\":\"127.0.0.1\",\"username\":\"org1User\",\"password\":\"password1\",\"certificationKey\":null,\"endpointUrl\":\"http://localhost:9080/mock/ehealthexchange\",\"active\":true},\"status\":\"ACTIVE\",\"startDate\":1473775522671,\"endDate\":null,\"success\":null,\"results\":[]},{\"id\":140,\"queryId\":24,\"org\":{\"name\":\"eHealthExchangeOrg2\",\"id\":2,\"organizationId\":2,\"adapter\":\"eHealth\",\"ipAddress\":\"127.0.0.1\",\"username\":\"org1User\",\"password\":\"password1\",\"certificationKey\":null,\"endpointUrl\":\"http://localhost:9080/mock/ehealthexchange\",\"active\":true},\"status\":\"ACTIVE\",\"startDate\":1473775522670,\"endDate\":null,\"success\":null,\"results\":[]},{\"id\":139,\"queryId\":24,\"org\":{\"name\":\"eHealthExchangeOrg\",\"id\":1,\"organizationId\":1,\"adapter\":\"eHealth\",\"ipAddress\":\"127.0.0.1\",\"username\":\"org1User\",\"password\":\"password1\",\"certificationKey\":null,\"endpointUrl\":\"http://localhost:9080/mock/ehealthexchange\",\"active\":true},\"status\":\"ACTIVE\",\"startDate\":1473775522668,\"endDate\":null,\"success\":null,\"results\":[]},{\"id\":142,\"queryId\":24,\"org\":{\"name\":\"IHEOrg\",\"id\":4,\"organizationId\":4,\"adapter\":\"IHE\",\"ipAddress\":\"127.0.0.1\",\"username\":null,\"password\":null,\"certificationKey\":\"1234567\",\"endpointUrl\":\"http://localhost:9080/mock/ihe\",\"active\":true},\"status\":\"ACTIVE\",\"startDate\":1473775522673,\"endDate\":null,\"success\":null,\"results\":[]}]}]";

	private static final String PATIENT_DISCOVERY_REQUEST_RESOURCE_FILE_NAME = "ValidXcpdRequest.xml";
	private static final String PATIENT_DISCOVERY_RESPONSE_RESOURCE_FILE_NAME = "ValidXcpdResponse.xml";
	
	@Value("${samlServiceUrl}")
	private String samlServiceUrl;
	@Value("${server.port}")
	private String port;
	
	@Autowired ResourceLoader resourceLoader;
	@Autowired EHealthQueryConsumerService consumerService;
	@Autowired SOAPToJSONService converter;
	@Autowired PatientSearchService pss;

	private MockRestServiceServer mockServer;
	@Mock private RestTemplate mockRestTemplate;
	
	private ExecutorService executor = Executors.newFixedThreadPool(100);
	
	@Before
    public void setUp() {
        mockRestTemplate = new RestTemplate();
        mockServer = MockRestServiceServer.createServer(mockRestTemplate);
    }
	
	//@Test
	public void testConsumerServicePatientDiscoveryRequest(){
		Resource documentsFile = resourceLoader.getResource("classpath:" + PATIENT_DISCOVERY_REQUEST_RESOURCE_FILE_NAME);
		String request = null;
		try {
			request = Resources.toString(documentsFile.getURL(), Charsets.UTF_8);
		}catch (IOException e){
			e.printStackTrace();
		}
		PRPAIN201305UV02 requestObj = null;
		try {
			requestObj = consumerService.unMarshallPatientDiscoveryRequestObject(request);
		} catch (SOAPException | SAMLException e) {
			e.printStackTrace();
		}
		assertNotNull(requestObj);
	}
	
	public PRPAIN201305UV02 createRequestObject(){
		Resource documentsFile = resourceLoader.getResource("classpath:" + PATIENT_DISCOVERY_REQUEST_RESOURCE_FILE_NAME);
		String request = null;
		try {
			request = Resources.toString(documentsFile.getURL(), Charsets.UTF_8);
		}catch (IOException e){
			e.printStackTrace();
		}
		PRPAIN201305UV02 requestObj = null;
		try {
			requestObj = consumerService.unMarshallPatientDiscoveryRequestObject(request);
		} catch (SOAPException | SAMLException e) {
			e.printStackTrace();
		}
		return requestObj;
	}
	
	//@Test
	public void testSOAPToJSONServicePatientDiscoveryRequest(){
		PRPAIN201305UV02 requestObj = createRequestObject();
		PatientSearch patientSearch = converter.convertToPatientSearch(requestObj);
		assertNotNull(patientSearch);
	}
	
	public PatientSearch createPatientSearchObject(){
		PRPAIN201305UV02 requestObj = createRequestObject();
		PatientSearch patientSearch = converter.convertToPatientSearch(requestObj);
		return patientSearch;
	}
	
	//@Before
	public void setupMockServers(){
		mockServer
	 	.expect(MockRestRequestMatchers.requestTo("http://localhost:9090/jwt"))
	 	.andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
	 	.andRespond(MockRestResponseCreators.withSuccess(
	 			"{\"token\": \""+ jwt + "\"" + "}"
	 			,MediaType.APPLICATION_JSON));
		
		mockServer
	 	.expect(MockRestRequestMatchers.requestTo("http://localhost:8090/search"))
	 	.andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
	 	.andRespond(MockRestResponseCreators.withSuccess(
	 			brokerSearchActiveQueryResponse
               ,MediaType.APPLICATION_JSON));
		
		mockServer
	 	.expect(MockRestRequestMatchers.requestTo("http://localhost:8090/queries/1"))
	 	.andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
	 	.andRespond(MockRestResponseCreators.withSuccess(
	 			brokerSearchCompletedQueryResponse
               ,MediaType.APPLICATION_JSON));
	}
	
	@Test
	public void testPatientSearch() throws Exception{
		mockServer
	 	.expect(MockRestRequestMatchers.requestTo(samlServiceUrl + "/jwt"))
	 	.andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
	 	.andRespond(MockRestResponseCreators.withSuccess(
	 			"{\"token\": \""+ jwt + "\"" + "}"
	 			,MediaType.APPLICATION_JSON));
		
		mockServer
	 	.expect(MockRestRequestMatchers.requestTo("http://localhost:" + port + "/search"))
	 	.andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
	 	.andRespond(MockRestResponseCreators.withSuccess(
	 			brokerSearchActiveQueryResponse
               ,MediaType.APPLICATION_JSON));
		
		mockServer
	 	.expect(MockRestRequestMatchers.requestTo("http://localhost:" + port + "/queries/1"))
	 	.andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
	 	.andRespond(MockRestResponseCreators.withSuccess(
	 			brokerSearchCompletedQueryResponse
               ,MediaType.APPLICATION_JSON));
		
		PatientSearch patientSearch = createPatientSearchObject();
		
		pss.searchForPatientWithTerms(patientSearch);
		
		Future<List<QueryOrganization>> future = executor.submit(pss);
		
		assertNotNull(future.get());
	}

}
