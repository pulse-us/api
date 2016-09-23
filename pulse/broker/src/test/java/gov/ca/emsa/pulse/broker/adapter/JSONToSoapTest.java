package gov.ca.emsa.pulse.broker.adapter;


import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.Source;

import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201310UV02;
import org.junit.BeforeClass;
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
import org.springframework.ws.soap.saaj.SaajSoapMessage;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

import gov.ca.emsa.pulse.auth.authentication.JWTUserConverterImpl;
import gov.ca.emsa.pulse.auth.jwt.JWTAuthorRsaJoseJImpl;
import gov.ca.emsa.pulse.auth.jwt.JWTValidationException;
import gov.ca.emsa.pulse.auth.user.User;
import gov.ca.emsa.pulse.broker.BrokerApplicationTestConfig;
import gov.ca.emsa.pulse.broker.adapter.service.EHealthQueryProducerService;
import gov.ca.emsa.pulse.broker.saml.SAMLInput;
import gov.ca.emsa.pulse.common.domain.PatientSearch;
import gov.ca.emsa.pulse.common.soap.JSONToSOAPService;
import gov.ca.emsa.pulse.common.soap.SOAPToJSONService;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = BrokerApplicationTestConfig.class)
public class JSONToSoapTest {
	
	@Autowired JSONToSOAPService service;
	@Autowired SOAPToJSONService reverseService;
	@Autowired EHealthQueryProducerService ehealthService;
	@Autowired private JWTUserConverterImpl converter;
	@Autowired private JWTAuthorRsaJoseJImpl jwtAuthor;
	@Autowired private ResourceLoader resourceLoader;
	private static final String RESOURCE_FILE_NAME = "NHINPatientDiscoveryRequest.xml";
	
	@Test
	public void testUnmarshallExampleFile() throws IOException, SAMLException, SOAPException {
		Resource pdFile = resourceLoader.getResource("classpath:" + RESOURCE_FILE_NAME);
		String pdRequestStr = Resources.toString(pdFile.getURL(), Charsets.UTF_8);
		PRPAIN201305UV02 unmarshalledRequest = ehealthService.unMarshallPatientDiscoveryRequestObject(pdRequestStr);
		assertNotNull(unmarshalledRequest);
	}
	
	@Test
	public void testCreatePatientDiscoveryRequest() throws JAXBException, 
		SAMLException, SOAPException, JWTValidationException {
		PatientSearch ps = new PatientSearch();
		ps.setDob("19830205");
		ps.setGivenName("Kathryn");
		ps.setFamilyName("Ekey");
		ps.setGender("F");
		
		SAMLInput input = new SAMLInput();
		input.setStrIssuer("https://idp.dhv.gov");
		input.setStrNameID("UserBrianLindsey");
		input.setStrNameQualifier("My Website");
		input.setSessionId("abcdedf1234567");
		HashMap<String, String> customAttributes = new HashMap<String,String>();
		customAttributes.put("RequesterFirstName", "Katy");
		customAttributes.put("RequestReason", "Patient is bleeding.");
		customAttributes.put("PatientGivenName", ps.getGivenName());
		customAttributes.put("PatientFamilyName", ps.getFamilyName());
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
		assertEquals(ps.getGivenName(), unmarshalledSearch.getGivenName());
		assertEquals(ps.getFamilyName(), unmarshalledSearch.getFamilyName());
		assertEquals(ps.getDob(), unmarshalledSearch.getDob());
		assertEquals(ps.getGender(), unmarshalledSearch.getGender());
	}
}
