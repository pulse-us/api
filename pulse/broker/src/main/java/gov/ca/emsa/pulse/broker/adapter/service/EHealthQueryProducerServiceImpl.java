package gov.ca.emsa.pulse.broker.adapter.service;

import gov.ca.emsa.pulse.broker.saml.SAMLInput;
import gov.ca.emsa.pulse.broker.saml.SamlGenerator;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeaderElement;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.Source;

import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;
import org.opensaml.common.SAMLException;
import org.opensaml.xml.io.MarshallingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ws.soap.SoapHeaderElement;
import org.springframework.ws.soap.saaj.SaajSoapMessage;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

@Service
public class EHealthQueryProducerServiceImpl implements EHealthQueryProducerService{

	private static final Logger logger = LogManager.getLogger(EHealthQueryProducerServiceImpl.class);
	@Autowired private SamlGenerator samlGenerator;

	public String createSOAPFault(){
		MessageFactory factory = null;
		try {
			factory = MessageFactory.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL);
		} catch (SOAPException e1) {
			logger.error(e1);
		}
		SOAPMessage soapMessage = null;
		try {
			soapMessage = factory.createMessage();
			soapMessage.getSOAPBody().addFault(SOAPConstants.SOAP_SENDER_FAULT, "There was no SAML security header in the SOAP message.");
		} catch (SOAPException e1) {
			logger.error(e1);
		}
		
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try {
			soapMessage.writeTo(outputStream);
		} catch (SOAPException | IOException e) {
			
		}
		String fault = new String(outputStream.toByteArray());

		return fault;
	} 

	private void createSecurityHeading(SOAPMessage message, SAMLInput samlInput) throws SOAPException {
		SOAPEnvelope env = message.getSOAPPart().getEnvelope();
		
		SOAPHeaderElement header1 = message.getSOAPHeader()
				.addHeaderElement(env.createName("Action", "a", "http://www.w3.org/2005/08/addressing"));
		header1.setAttributeNS("http://www.w3.org/2003/05/soap-envelope", "env:mustUnderstand", "1");
		header1.setValue("urn:hl7-org:v3:PRPA_IN201305UV02:CrossGatewayPatientDiscovery");
		message.getSOAPHeader().addChildElement(header1);

		SOAPHeaderElement header2 = message.getSOAPHeader()
				.addHeaderElement(env.createName("MessageID", "a", "http://www.w3.org/2005/08/addressing"));
		header2.setValue("urn:uuid:a02ca8cd-86fa-4afc-a27c-16c183b2055");
		message.getSOAPHeader().addChildElement(header2);

		//TODO: there are other elements in the sample - do we need them?
		
		SOAPHeaderElement securityElement = message.getSOAPHeader()
				.addHeaderElement(env.createName("Security", "wsse", "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd"));
		
		//get the SAML assertion into the header. have to "import" it because it's created by a different Document
		try {
			Document owner = securityElement.getOwnerDocument();
			org.w3c.dom.Element samlElement = samlGenerator.createSAMLElement(samlInput);
			Node importedSamlElement = owner.importNode(samlElement, true);
			securityElement.appendChild(importedSamlElement);
		} catch (MarshallingException e) {
			logger.error("Could not create SAML from input " + samlInput, e);
		}

		message.getSOAPHeader().addChildElement(securityElement);
	}
	
	public boolean checkSecurityHeading(SaajSoapMessage saajSoap){
		Iterator<SoapHeaderElement> security = saajSoap.getSoapHeader().examineAllHeaderElements();
		while(security.hasNext()){
			SoapHeaderElement headerElem = security.next();
			if(headerElem.getName().getLocalPart().equals("Security")){
				SoapHeaderElement wsse = headerElem;
				return true;
			}
		}
		return false;
	}
	
	public String marshallPatientDiscoveryRequest(SAMLInput samlInput, PRPAIN201305UV02 request) throws JAXBException{
		MessageFactory factory = null;
		try {
			factory = MessageFactory.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL);
		} catch (SOAPException e1) {
			logger.error(e1);
		}
		SOAPMessage soapMessage = null;
		try {
			soapMessage = factory.createMessage();
		} catch (SOAPException e) {
			logger.error(e);
		}
		
		try {
			createSecurityHeading(soapMessage, samlInput);
		} catch(SOAPException soap) {
			logger.error(soap);
		}
		
		JAXBElement<PRPAIN201305UV02> je = new JAXBElement<PRPAIN201305UV02>(new QName("PRPAIN201305UV02"), PRPAIN201305UV02.class, request);
		Document document = null;
		try {
			document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		Marshaller documentMarshaller = createMarshaller(createJAXBContext(request.getClass()));
		documentMarshaller.marshal(je, document);
		try {
			soapMessage.getSOAPBody().addDocument(document);
		} catch (SOAPException e1) {
			e1.printStackTrace();
		}
		OutputStream sw = new ByteArrayOutputStream();
		try {
			soapMessage.writeTo(sw);
		} catch (IOException | SOAPException e) {
			e.printStackTrace();
		}
		return sw.toString();
	}
	
	public String marshallDocumentQueryRequest(SAMLInput samlInput, AdhocQueryRequest request) throws JAXBException{
		MessageFactory factory = null;
		try {
			factory = MessageFactory.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL);
		} catch (SOAPException e1) {
			logger.error(e1);
		}
		SOAPMessage soapMessage = null;
		try {
			soapMessage = factory.createMessage();
		} catch (SOAPException e) {
			logger.error(e);
		}
		
		try {
			createSecurityHeading(soapMessage, samlInput);
		} catch(SOAPException soap) {
			logger.error(soap);
		}
		
		Document document = null;
		try {
			document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		Marshaller documentMarshaller = createMarshaller(createJAXBContext(request.getClass()));
		documentMarshaller.marshal(request, document);
		try {
			soapMessage.getSOAPBody().addDocument(document);
		} catch (SOAPException e1) {
			e1.printStackTrace();
		}
		OutputStream sw = new ByteArrayOutputStream();
		try {
			soapMessage.writeTo(sw);
		} catch (IOException | SOAPException e) {
			e.printStackTrace();
		}
		return sw.toString();
	}
	
	public String marshallDocumentSetRequest(SAMLInput samlInput, RetrieveDocumentSetRequestType request) throws JAXBException{
		MessageFactory factory = null;
		try {
			factory = MessageFactory.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL);
		} catch (SOAPException e1) {
			logger.error(e1);
		}
		SOAPMessage soapMessage = null;
		try {
			soapMessage = factory.createMessage();
		} catch (SOAPException e) {
			logger.error(e);
		}
		
		try {
			createSecurityHeading(soapMessage, samlInput);
		} catch(SOAPException soap) {
			logger.error(soap);
		}
		
		JAXBElement<RetrieveDocumentSetRequestType> je = new JAXBElement<RetrieveDocumentSetRequestType>(new QName("RetrieveDocumentSetRequest"), RetrieveDocumentSetRequestType.class, request);
		Document document = null;
		try {
			document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		Marshaller documentMarshaller = createMarshaller(createJAXBContext(request.getClass()));
		documentMarshaller.marshal(je, document);
		try {
			soapMessage.getSOAPBody().addDocument(document);
		} catch (SOAPException e1) {
			e1.printStackTrace();
		}
		OutputStream sw = new ByteArrayOutputStream();
		try {
			soapMessage.writeTo(sw);
		} catch (IOException | SOAPException e) {
			e.printStackTrace();
		}
		return sw.toString();
	}
	
	public Marshaller createMarshaller(JAXBContext jc){
		// Create JAXB marshaller
		Marshaller marshaller = null;
		try {
			marshaller = jc.createMarshaller();
		}
		catch (Exception ex) {
			logger.error(ex);
		}
		return marshaller;
	}

	public JAXBContext createJAXBContext(Class<?> classObj){
		// Create a JAXB context
		JAXBContext jc = null;
		try {
			jc = JAXBContext.newInstance(classObj);
		}
		catch (Exception ex) {
			logger.error(ex);
		}
		return jc;
	}

	public Unmarshaller createUnmarshaller(JAXBContext jc){
		// Create JAXB unmarshaller
		Unmarshaller unmarshaller = null;
		try {
			unmarshaller = jc.createUnmarshaller();
		}
		catch (Exception ex) {
			logger.error(ex);
		}
		return unmarshaller;
	}

	public PRPAIN201306UV02 unMarshallPatientDiscoveryResponseObject(String xml) throws SOAPException, SAMLException{
		MessageFactory factory = null;
		try {
			factory = MessageFactory.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL);
		} catch (SOAPException e1) {
			logger.error(e1);
		}
		SOAPMessage soapMessage = null;
		try {
			soapMessage = factory.createMessage(new MimeHeaders(), new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));
		} catch (IOException | SOAPException e) {
			logger.error(e);
		}
		SaajSoapMessage saajSoap = new SaajSoapMessage(soapMessage);
		Source requestSource = saajSoap.getSoapBody().getPayloadSource();

		// Create a JAXB context
		JAXBContext jc = createJAXBContext(PRPAIN201306UV02.class);

		// Create JAXB unmarshaller
		Unmarshaller unmarshaller = createUnmarshaller(jc);

		JAXBElement<?> requestObj = null;
		try {
			requestObj = (JAXBElement<?>) unmarshaller.unmarshal(requestSource, PRPAIN201306UV02.class);
		} catch (JAXBException e) {
			logger.error(e);
		}

		return (PRPAIN201306UV02) requestObj.getValue();
	}

	public AdhocQueryResponse unMarshallDocumentQueryResponseObject(String xml) throws SAMLException,SOAPException 
	{
		MessageFactory factory = null;
		try {
			factory = MessageFactory.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL);
		} catch (SOAPException e1) {
			logger.error(e1);
		}
		SOAPMessage soapMessage = null;
		try {
			soapMessage = factory.createMessage(new MimeHeaders(), new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));
		} catch (IOException | SOAPException e) {
			logger.error(e);
		}
		SaajSoapMessage saajSoap = new SaajSoapMessage(soapMessage);
		Source requestSource = saajSoap.getSoapBody().getPayloadSource();

		// Create a JAXB context
		JAXBContext jc = null;
		try {
			jc = JAXBContext.newInstance(AdhocQueryResponse.class);
		}
		catch (Exception ex) {
			logger.error(ex);
		}

		// Create JAXB unmarshaller
		Unmarshaller unmarshaller = null;
		try {
			unmarshaller = jc.createUnmarshaller();
		}
		catch (Exception ex) {
			logger.error(ex);
		}
		JAXBElement<?> requestObj = null;
		try {
			requestObj = (JAXBElement<?>) unmarshaller.unmarshal(requestSource, AdhocQueryResponse.class);
		} catch (JAXBException e) {
			logger.error(e);
		}
		return (AdhocQueryResponse) requestObj.getValue();
	}

	public RetrieveDocumentSetResponseType unMarshallDocumentSetRetrieveResponseObject(String xml) throws SAMLException{
		MessageFactory factory = null;
		try {
			factory = MessageFactory.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL);
		} catch (SOAPException e1) {
			logger.error(e1);
		}
		SOAPMessage soapMessage = null;
		try {
			soapMessage = factory.createMessage(new MimeHeaders(), new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));
		} catch (IOException | SOAPException e) {
			logger.error(e);
		}
		SaajSoapMessage saajSoap = new SaajSoapMessage(soapMessage);
		Source requestSource = saajSoap.getSoapBody().getPayloadSource();

		// Create a JAXB context
		JAXBContext jc = null;
		try {
			jc = JAXBContext.newInstance(RetrieveDocumentSetResponseType.class);
		}
		catch (Exception ex) {
			logger.error(ex);
		}

		// Create JAXB unmarshaller
		Unmarshaller unmarshaller = null;
		try {
			unmarshaller = jc.createUnmarshaller();
		}
		catch (Exception ex) {
			logger.error(ex);
		}
		JAXBElement<?> requestObj = null;
		try {
			requestObj = (JAXBElement<?>) unmarshaller.unmarshal(requestSource, RetrieveDocumentSetResponseType.class);
		} catch (JAXBException e) {
			logger.error(e);
		}

		return (RetrieveDocumentSetResponseType) requestObj.getValue();
	}

	public PRPAIN201305UV02 unMarshallPatientDiscoveryRequestObject(String xml) throws SOAPException, SAMLException{
		MessageFactory factory = null;
		try {
			factory = MessageFactory.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL);
		} catch (SOAPException e1) {
			logger.error(e1);
		}
		SOAPMessage soapMessage = null;
		try {
			soapMessage = factory.createMessage(new MimeHeaders(), new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));
		} catch (IOException | SOAPException e) {
			logger.error(e);
		}
		SaajSoapMessage saajSoap = new SaajSoapMessage(soapMessage);

		if(checkSecurityHeading(saajSoap)){

			Source requestSource = saajSoap.getSoapBody().getPayloadSource();

			// Create a JAXB context
			JAXBContext jc = createJAXBContext(PRPAIN201305UV02.class);

			// Create JAXB unmarshaller
			Unmarshaller unmarshaller = createUnmarshaller(jc);

			JAXBElement<?> requestObj = null;
			try {
				requestObj = (JAXBElement<?>) unmarshaller.unmarshal(requestSource, PRPAIN201305UV02.class);
			} catch (JAXBException e) {
				logger.error(e);
			}

			return (PRPAIN201305UV02) requestObj.getValue();
		}else{
			logger.error("SOAP message does not have a SAML header");
			throw new SAMLException();
		}
	}

	public AdhocQueryRequest unMarshallDocumentQueryRequestObject(String xml) throws SAMLException{
		MessageFactory factory = null;
		try {
			factory = MessageFactory.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL);
		} catch (SOAPException e1) {
			logger.error(e1);
		}
		SOAPMessage soapMessage = null;
		try {
			soapMessage = factory.createMessage(new MimeHeaders(), new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));
		} catch (IOException | SOAPException e) {
			logger.error(e);
		}
		SaajSoapMessage saajSoap = new SaajSoapMessage(soapMessage);

		if(checkSecurityHeading(saajSoap)){

			Source requestSource = saajSoap.getSoapBody().getPayloadSource();

			// Create a JAXB context
			JAXBContext jc = null;
			try {
				jc = JAXBContext.newInstance(AdhocQueryRequest.class);
			}
			catch (Exception ex) {
				logger.error(ex);
			}

			// Create JAXB unmarshaller
			Unmarshaller unmarshaller = null;
			try {
				unmarshaller = jc.createUnmarshaller();
			}
			catch (Exception ex) {
				logger.error(ex);
			}
			JAXBElement<?> requestObj = null;
			try {
				requestObj = (JAXBElement<?>) unmarshaller.unmarshal(requestSource, AdhocQueryRequest.class);
			} catch (JAXBException e) {
				logger.error(e);
			}
			return (AdhocQueryRequest) requestObj.getValue();
		}else{
			logger.error("SOAP message does not have a SAML header");
			throw new SAMLException();
		}
	}

	public RetrieveDocumentSetRequestType unMarshallDocumentSetRetrieveRequestObject(String xml) throws SAMLException{
		MessageFactory factory = null;
		try {
			factory = MessageFactory.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL);
		} catch (SOAPException e1) {
			logger.error(e1);
		}
		SOAPMessage soapMessage = null;
		try {
			soapMessage = factory.createMessage(new MimeHeaders(), new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));
		} catch (IOException | SOAPException e) {
			logger.error(e);
		}
		SaajSoapMessage saajSoap = new SaajSoapMessage(soapMessage);

		if(checkSecurityHeading(saajSoap)){

			Source requestSource = saajSoap.getSoapBody().getPayloadSource();

			// Create a JAXB context
			JAXBContext jc = null;
			try {
				jc = JAXBContext.newInstance(RetrieveDocumentSetRequestType.class);
			}
			catch (Exception ex) {
				logger.error(ex);
			}

			// Create JAXB unmarshaller
			Unmarshaller unmarshaller = null;
			try {
				unmarshaller = jc.createUnmarshaller();
			}
			catch (Exception ex) {
				logger.error(ex);
			}
			JAXBElement<?> requestObj = null;
			try {
				requestObj = (JAXBElement<?>) unmarshaller.unmarshal(requestSource, RetrieveDocumentSetRequestType.class);
			} catch (JAXBException e) {
				logger.error(e);
			}

			return (RetrieveDocumentSetRequestType) requestObj.getValue();

		}else{
			logger.error("SOAP message does not have a SAML header");
			throw new SAMLException();
		}
	}
}
