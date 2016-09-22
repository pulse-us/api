package gov.ca.emsa.pulse.broker.adapter;


import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

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
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opensaml.common.SAMLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.util.Assert;
import org.springframework.ws.soap.saaj.SaajSoapMessage;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

import gov.ca.emsa.pulse.broker.BrokerApplicationTestConfig;
import gov.ca.emsa.pulse.broker.adapter.service.EHealthQueryProducerService;
import gov.ca.emsa.pulse.common.domain.PatientSearch;
import gov.ca.emsa.pulse.common.soap.JSONToSOAPService;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = BrokerApplicationTestConfig.class)
public class JSONToSoapTest {
	
	@Autowired JSONToSOAPService service;
	@Autowired EHealthQueryProducerService ehealthService;
	
	@Test
	public void testCreatePatientDiscoveryRequest() throws JAXBException, SAMLException, SOAPException {
		PatientSearch ps = new PatientSearch();
		ps.setDob("19830205");
		ps.setGivenName("Kathryn");
		ps.setFamilyName("Ekey");
		ps.setGender("F");
		
		PRPAIN201305UV02 request = service.convertFromPatientSearch(ps);
		String requestXml = ehealthService.marshallPatientDiscoveryRequest(request);
		Assert.notNull(requestXml);
		System.out.println(requestXml);
		PRPAIN201305UV02 unmarshalledRequest = ehealthService.unMarshallPatientDiscoveryRequestObject(requestXml);
		
	}
}
