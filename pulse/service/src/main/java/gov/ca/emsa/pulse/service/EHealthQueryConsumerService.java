package gov.ca.emsa.pulse.service;

import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;

import javax.xml.bind.JAXBException;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;

import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;
import org.opensaml.common.SAMLException;

public interface EHealthQueryConsumerService {
	public PRPAIN201305UV02 unMarshallPatientDiscoveryRequestObject(String xml) throws SOAPException, SAMLException;
	public AdhocQueryRequest unMarshallDocumentQueryRequestObject(String xml) throws SAMLException;
	public RetrieveDocumentSetRequestType unMarshallDocumentSetRetrieveRequestObject(String xml) throws SAMLException;
	public String marshallPatientDiscoveryResponse(PRPAIN201306UV02 response) 
			throws JAXBException, SOAPException; 
	public String createSOAPFault();
	public String marshallDocumentQueryResponse(AdhocQueryResponse responseObj, SOAPMessage request) 
			throws JAXBException, SOAPException;
	public String marshallDocumentSetResponse(RetrieveDocumentSetResponseType responseObj, SOAPMessage request) 
			throws JAXBException, SOAPException;
	public SOAPMessage getSoapMessageFromXml(String xml);
}