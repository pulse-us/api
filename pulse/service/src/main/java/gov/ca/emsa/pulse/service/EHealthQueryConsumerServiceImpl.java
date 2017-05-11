package gov.ca.emsa.pulse.service;

import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.UUID;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPHeaderElement;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.Source;

import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.PRPAIN201310UV02;
import org.opensaml.common.SAMLException;
import org.opensaml.xml.io.MarshallingException;
import org.springframework.stereotype.Service;
import org.springframework.ws.soap.SoapHeaderElement;
import org.springframework.ws.soap.saaj.SaajSoapMessage;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

@Service
public class EHealthQueryConsumerServiceImpl implements EHealthQueryConsumerService{

	private static final Logger logger = LogManager.getLogger(EHealthQueryConsumerServiceImpl.class);

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

	public boolean checkSecurityHeading(SaajSoapMessage saajSoap){
		Iterator<SoapHeaderElement> security = saajSoap.getSoapHeader().examineAllHeaderElements();
		while(security.hasNext()){
			SoapHeaderElement headerElem = security.next();
			if(headerElem.getName().getLocalPart().equals("Security")){
				return true;
			}
		}
		return false;
	}
	
	public String marshallPatientDiscoveryResponse(PRPAIN201306UV02 response) 
			throws JAXBException, SOAPException {
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

		JAXBElement<PRPAIN201306UV02> je = new JAXBElement<PRPAIN201306UV02>(new QName("urn:hl7-org:v3", "PRPA_IN201306UV02"), PRPAIN201306UV02.class, response);
		Document document = null;
		try {
			document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		Marshaller documentMarshaller = createMarshaller(createJAXBContext(response.getClass()));
		documentMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		documentMarshaller.marshal(je, document);
		try {
			soapMessage.getSOAPBody().addDocument(document);
		} catch (SOAPException e1) {
			e1.printStackTrace();
		}
		
		//for some reason the semanticText tag values aren't marshalled
		//so need to add them back in here... don't know why!
		addSemanticTextValues(soapMessage);
		OutputStream sw = new ByteArrayOutputStream();
		try {
			soapMessage.writeTo(sw);
		} catch (IOException | SOAPException e) {
			e.printStackTrace();
		}
		return sw.toString();
	}
	
	public String marshallDocumentQueryResponse(AdhocQueryResponse response, SOAPMessage requestSoap) 
		throws JAXBException, SOAPException {
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
		
		SOAPHeaderElement messageIdHeader = null;
		SOAPHeader requestSoapHeader = requestSoap.getSOAPHeader();
		Iterator<SOAPHeaderElement> headerIter = requestSoapHeader.examineAllHeaderElements();
		while(headerIter.hasNext() && messageIdHeader == null) {
			SOAPHeaderElement headerElem = headerIter.next();
			if(headerElem.getElementName().getLocalName().equals("MessageID")) {
				messageIdHeader = headerElem;
			}
		}
		if(messageIdHeader == null) {
			logger.error("Cannot create a response because there is no MessageId header in the request.");
			throw new SOAPException("No MessageId header was found in the SOAP request.");
		}
		createDocumentQueryResponseHeaders(soapMessage, messageIdHeader);
		JAXBElement<AdhocQueryResponse> je = new JAXBElement<AdhocQueryResponse>(new QName("AdhocQueryResponse"), AdhocQueryResponse.class, response);
		Document document = null;
		try {
			document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		Marshaller documentMarshaller = createMarshaller(createJAXBContext(response.getClass()));
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
	
	public String marshallDocumentSetResponse(RetrieveDocumentSetResponseType response, SOAPMessage requestSoap) 
		throws JAXBException, SOAPException {
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
		
		SOAPHeaderElement messageIdHeader = null;
		SOAPHeader requestSoapHeader = requestSoap.getSOAPHeader();
		Iterator<SOAPHeaderElement> headerIter = requestSoapHeader.examineAllHeaderElements();
		while(headerIter.hasNext() && messageIdHeader == null) {
			SOAPHeaderElement headerElem = headerIter.next();
			if(headerElem.getElementName().getLocalName().equals("MessageID")) {
				messageIdHeader = headerElem;
			}
		}
		if(messageIdHeader == null) {
			logger.error("Cannot create a response because there is no MessageId header in the request.");
			throw new SOAPException("No MessageId header was found in the SOAP request.");
		}
		createDocumentSetRetrieveResponseHeaders(soapMessage, messageIdHeader);
		JAXBElement<RetrieveDocumentSetResponseType> je = new JAXBElement<RetrieveDocumentSetResponseType>(new QName("RetrieveDocumentSetResponse"), RetrieveDocumentSetResponseType.class, response);
		Document document = null;
		try {
			document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		Marshaller documentMarshaller = createMarshaller(createJAXBContext(response.getClass()));
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

	@Override
	public SOAPMessage getSoapMessageFromXml(String xml) {
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
		return soapMessage;
	}
	
	private void createDocumentQueryResponseHeaders(SOAPMessage message, SOAPHeaderElement requestMessageIdHeader) throws SOAPException {
		SOAPEnvelope env = message.getSOAPPart().getEnvelope();
		
		//elements in the sample
		//<a:Action s:mustUnderstand="1">urn:ihe:iti:2007:CrossGatewayQueryResponse</a:Action>
		//<a:RelatesTo>urn:uuid:def119ad-dc13-49c1-a3c7-e3742531f9b3</a:RelatesTo>
		
		SOAPHeaderElement actionHeader = message.getSOAPHeader()
				.addHeaderElement(env.createName("Action", "a", "http://www.w3.org/2005/08/addressing"));
		actionHeader.setAttributeNS("http://www.w3.org/2003/05/soap-envelope", "env:mustUnderstand", "1");
		actionHeader.setValue("urn:ihe:iti:2007:CrossGatewayQueryResponse");
		message.getSOAPHeader().addChildElement(actionHeader);

		SOAPHeaderElement relatesToHeader = message.getSOAPHeader()
				.addHeaderElement(env.createName("RelatesTo", "a", "http://www.w3.org/2005/08/addressing"));
		relatesToHeader.setValue(requestMessageIdHeader.getValue());
		message.getSOAPHeader().addChildElement(relatesToHeader);
	}
	
	private void createDocumentSetRetrieveResponseHeaders(SOAPMessage message, SOAPHeaderElement requestMessageIdHeader) throws SOAPException {
		SOAPEnvelope env = message.getSOAPPart().getEnvelope();
		
		//elements in the sample
//		<a:Action s:mustUnderstand="1">urn:ihe:iti:2007:CrossGatewayRetrieveResponse</a:Action>
//		<a:RelatesTo>urn:uuid:0fbfdced-6c01-4d09-a110-2201afedaa02</a:RelatesTo> 
		
		SOAPHeaderElement actionHeader = message.getSOAPHeader()
				.addHeaderElement(env.createName("Action", "a", "http://www.w3.org/2005/08/addressing"));
		actionHeader.setAttributeNS("http://www.w3.org/2003/05/soap-envelope", "env:mustUnderstand", "1");
		actionHeader.setValue("urn:ihe:iti:2007:CrossGatewayRetrieveResponse");
		message.getSOAPHeader().addChildElement(actionHeader);

		SOAPHeaderElement relatesToHeader = message.getSOAPHeader()
				.addHeaderElement(env.createName("RelatesTo", "a", "http://www.w3.org/2005/08/addressing"));
		relatesToHeader.setValue(requestMessageIdHeader.getValue());
		message.getSOAPHeader().addChildElement(relatesToHeader);
	}
	
	private void addSemanticTextValues(SOAPMessage soapMessage) throws SOAPException {
		NodeList parameterNodeList = null;
		Node parent = soapMessage.getSOAPBody().getFirstChild();
		NodeList children = parent.getChildNodes();
		for(int i = 0; i < children.getLength(); i++) {
			if(children.item(i).getNodeName().equals("controlActProcess")) {
				NodeList capChildren = children.item(i).getChildNodes();
				for(int j = 0; j < capChildren.getLength(); j++) {
					if(capChildren.item(j).getNodeName().equals("queryByParameter")) {
						NodeList queryChildren = capChildren.item(j).getChildNodes();
						for(int k = 0; k < queryChildren.getLength(); k++) {
							if(queryChildren.item(k).getNodeName().equals("parameterList")) {
								parameterNodeList = queryChildren.item(k).getChildNodes();
							}
						}
					}
				}
			}
		}
		if(parameterNodeList == null) {
			return;
		}
		for(int i=0;i<parameterNodeList.getLength();i++){
			Node node = parameterNodeList.item(i);
			NodeList nodeList2 = node.getChildNodes();
			for(int j=0;j<nodeList2.getLength();j++){
				Node node2 = nodeList2.item(j);
				if(node2.getNodeName().equals("semanticsText")){
					switch(node2.getParentNode().getNodeName()){
					case "livingSubjectAdministrativeGender":
						node2.setTextContent("LivingSubject.administrativeGender");
						break;
					case "livingSubjectBirthTime":
						node2.setTextContent("LivingSubject.birthTime");
						break;
					case "livingSubjectId":
						node2.setTextContent("LivingSubject.id");
						break;
					case "livingSubjectName":
						node2.setTextContent("LivingSubject.name");
						break;
					case "patientAddress":
						node2.setTextContent("Patient.addr");
						break;
					case "patientTelecom":
						node2.setTextContent("Patient.telecom");
						break;
					}
				}
			}
		}
	}
}
