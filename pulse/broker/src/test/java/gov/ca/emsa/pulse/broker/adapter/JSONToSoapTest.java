package gov.ca.emsa.pulse.broker.adapter;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.bind.JAXBException;
import javax.xml.soap.SOAPException;

import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opensaml.common.SAMLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.util.Assert;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

import gov.ca.emsa.pulse.auth.jwt.JWTValidationException;
import gov.ca.emsa.pulse.broker.BrokerApplicationTestConfig;
import gov.ca.emsa.pulse.broker.adapter.service.EHealthQueryProducerService;
import gov.ca.emsa.pulse.broker.dto.GivenNameDTO;
import gov.ca.emsa.pulse.broker.saml.SAMLInput;
import gov.ca.emsa.pulse.common.domain.Address;
import gov.ca.emsa.pulse.common.domain.Document;
import gov.ca.emsa.pulse.common.domain.GivenName;
import gov.ca.emsa.pulse.common.domain.DocumentIdentifier;
import gov.ca.emsa.pulse.common.domain.Patient;
import gov.ca.emsa.pulse.common.domain.PatientRecord;
import gov.ca.emsa.pulse.common.domain.PatientSearch;
import gov.ca.emsa.pulse.common.soap.JSONToSOAPService;
import gov.ca.emsa.pulse.common.soap.SOAPToJSONService;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = BrokerApplicationTestConfig.class)
public class JSONToSoapTest {
	
	@Autowired JSONToSOAPService service;
	@Autowired SOAPToJSONService reverseService;
	@Autowired EHealthQueryProducerService ehealthService;
	@Autowired private ResourceLoader resourceLoader;
	
	@Test
	public void testUnmarshallPatientDiscoveryExampleRequest() throws IOException, SAMLException, SOAPException {
		Resource pdFile = resourceLoader.getResource("classpath:NHINPatientDiscoveryRequest.xml");
		String pdRequestStr = Resources.toString(pdFile.getURL(), Charsets.UTF_8);
		PRPAIN201305UV02 unmarshalledRequest = ehealthService.unMarshallPatientDiscoveryRequestObject(pdRequestStr);
		assertNotNull(unmarshalledRequest);
	}
	
	@Test
	public void testUnmarshallDocumentDiscoveryExampleRequest() throws IOException, SAMLException, SOAPException {
		Resource pdFile = resourceLoader.getResource("classpath:NHINQueryForDocumentsRequest.xml");
		String pdRequestStr = Resources.toString(pdFile.getURL(), Charsets.UTF_8);
		AdhocQueryRequest unmarshalledRequest = ehealthService.unMarshallDocumentQueryRequestObject(pdRequestStr);
		assertNotNull(unmarshalledRequest);
	}
	
	@Test
	public void testCreatePatientDiscoveryRequest() throws JAXBException, 
		SAMLException, SOAPException, JWTValidationException {
		PatientSearch ps = new PatientSearch();
		Patient toCreate = new Patient();
		ps.setDob("19830205");
		ArrayList<GivenName> givens = new ArrayList<GivenName>();
		GivenName given = new GivenName();
		given.setGivenName("Kathryn");
		givens.add(given);
		toCreate.getPatientName().setGivenName(givens);
		ps.getPatientName().setGivenName(givens);
		ps.getPatientName().setFamilyName("Ekey");
		ps.setGender("F");
		
		SAMLInput input = new SAMLInput();
		input.setStrIssuer("https://idp.dhv.gov");
		input.setStrNameID("UserBrianLindsey");
		input.setStrNameQualifier("My Website");
		input.setSessionId("abcdedf1234567");
		HashMap<String, String> customAttributes = new HashMap<String,String>();
		customAttributes.put("RequesterFirstName", "Katy");
		customAttributes.put("RequestReason", "Patient is bleeding.");
		customAttributes.put("PatientGivenName", ps.getPatientName().getGivenName().get(0).getGivenName());
		customAttributes.put("PatientFamilyName", ps.getPatientName().getFamilyName());
		customAttributes.put("PatientDOB", ps.getDob());
		customAttributes.put("PatientGender", ps.getGender());
		customAttributes.put("PatientHomeZip", ps.getZip());
		customAttributes.put("PatientSSN", ps.getSsn());
		input.setAttributes(customAttributes);

		PRPAIN201305UV02 request = service.convertFromPatientSearch(ps);
		String requestXml = ehealthService.marshallPatientDiscoveryRequest(input, request);
		Assert.notNull(requestXml);
		System.out.println(requestXml);
		PRPAIN201305UV02 unmarshalledRequest = ehealthService.unMarshallPatientDiscoveryRequestObject(requestXml);
		assertNotNull(unmarshalledRequest);
		PatientSearch unmarshalledSearch = reverseService.convertToPatientSearch(unmarshalledRequest);
		assertNotNull(unmarshalledSearch);
		assertEquals(ps.getPatientName().getGivenName().get(0).getGivenName(), unmarshalledSearch.getPatientName().getGivenName().get(0).getGivenName());
		assertEquals(ps.getPatientName().getFamilyName(), unmarshalledSearch.getPatientName().getFamilyName());
		assertEquals(ps.getDob(), unmarshalledSearch.getDob());
		assertEquals(ps.getGender(), unmarshalledSearch.getGender());
	}
	
	@Test
	public void testParsePatientDiscoveryResponse() throws SAMLException, SOAPException, IOException {
		Resource pdFile = resourceLoader.getResource("classpath:NHINPatientDiscoveryResponse.xml");
		String pdResponseStr = Resources.toString(pdFile.getURL(), Charsets.UTF_8);
		PRPAIN201306UV02 resultObj = ehealthService.unMarshallPatientDiscoveryResponseObject(pdResponseStr);
		assertNotNull(resultObj);
		List<PatientRecord> patientRecords = reverseService.convertToPatientRecords(resultObj);
		assertNotNull(patientRecords);
		assertEquals(2, patientRecords.size());
		
		PatientRecord firstPatient = patientRecords.get(0);
		assertEquals("James", firstPatient.getPatientName().getGivenName().get(0).getGivenName());
		assertEquals("Jones", firstPatient.getPatientName().getFamilyName());
		assertEquals("tel:+1-481-555-7684;ext=2342", firstPatient.getPhoneNumber());
		assertEquals("19630804", firstPatient.getDateOfBirth());
		assertEquals("M", firstPatient.getGender());
		Address firstPatientAddress = firstPatient.getAddress();
		assertNotNull(firstPatientAddress);
		assertEquals("3443 North Arctic Avenue", firstPatientAddress.getStreet1());
		assertNull(firstPatientAddress.getStreet2());
		assertEquals("Some City", firstPatientAddress.getCity());
		assertEquals("IL", firstPatientAddress.getState());
	}
	
	@Test
	public void testCreateDocumentQueryRequest() throws JAXBException, 
		SAMLException, SOAPException, JWTValidationException {
		Patient patient = new Patient();
		patient.setOrgPatientId("11.5.4.4.6667.110");
		
		SAMLInput input = new SAMLInput();
		input.setStrIssuer("https://idp.dhv.gov");
		input.setStrNameID("UserBrianLindsey");
		input.setStrNameQualifier("My Website");
		input.setSessionId("abcdedf1234567");
		HashMap<String, String> customAttributes = new HashMap<String,String>();
		customAttributes.put("RequesterFirstName", "Katy");
		customAttributes.put("RequestReason", "Patient is bleeding.");
		input.setAttributes(customAttributes);

		AdhocQueryRequest request = service.convertToDocumentRequest(patient);
		String requestXml = ehealthService.marshallDocumentQueryRequest(input, request);
		Assert.notNull(requestXml);
		System.out.println(requestXml);
		AdhocQueryRequest unmarshalledRequest = ehealthService.unMarshallDocumentQueryRequestObject(requestXml);
		assertNotNull(unmarshalledRequest);
	}
	
	@Test
	public void testParseDocumentQueryResponse() throws SAMLException, SOAPException, IOException {
		Resource dqFile = resourceLoader.getResource("classpath:NHINQueryForDocumentsResponse.xml");
		String dqResponseStr = Resources.toString(dqFile.getURL(), Charsets.UTF_8);
		AdhocQueryResponse resultObj = ehealthService.unMarshallDocumentQueryResponseObject(dqResponseStr);
		assertNotNull(resultObj);
		List<Document> docs = reverseService.convertToDocumentQueryResponse(resultObj);
		assertNotNull(docs);
		assertEquals(1, docs.size());
		Document doc = docs.get(0);
		assertEquals("20080516", doc.getCreationTime());
		assertEquals("urn:oid:2.16.840.1.113883.3.166", doc.getIdentifier().getHomeCommunityId());
		assertEquals("2.16.840.1.113883.3.166.3.1", doc.getIdentifier().getRepositoryUniqueId());
		assertEquals("35452", doc.getSize());
		assertEquals("Physical Test", doc.getName());
		assertNull(doc.getDescription());
		assertEquals("SUMMARIZATION OF EPISODE NOTE", doc.getClassName());
		assertEquals("Normal", doc.getConfidentiality());
		assertEquals("HL7 CCD Document", doc.getFormat());
		assertEquals("129.6.58.92.147", doc.getIdentifier().getDocumentUniqueId());
	}
	
	@Test
	public void testCreateDocumentRetrieveRequest() throws JAXBException, 
		SAMLException, SOAPException, JWTValidationException {
		List<Document> docs = new ArrayList<Document>();
		Document doc = new Document();
		DocumentIdentifier docId = new DocumentIdentifier();
		docId.setHomeCommunityId("1.1.1.1");
		docId.setRepositoryUniqueId("2.2.2.2.2");
		docId.setDocumentUniqueId("3.3.3.3.3");
		doc.setIdentifier(docId);
		docs.add(doc);
		
		SAMLInput input = new SAMLInput();
		input.setStrIssuer("https://idp.dhv.gov");
		input.setStrNameID("UserBrianLindsey");
		input.setStrNameQualifier("My Website");
		input.setSessionId("abcdedf1234567");
		HashMap<String, String> customAttributes = new HashMap<String,String>();
		customAttributes.put("RequesterFirstName", "Katy");
		customAttributes.put("RequestReason", "Patient is bleeding.");
		input.setAttributes(customAttributes);

		RetrieveDocumentSetRequestType request = service.convertToRetrieveDocumentSetRequest(docs);
		String requestXml = ehealthService.marshallDocumentSetRequest(input, request);
		Assert.notNull(requestXml);
		System.out.println(requestXml);
	}
}
