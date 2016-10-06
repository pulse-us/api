package gov.ca.emsa.pulse.broker.adapter.service;

import javax.xml.bind.JAXBException;
import javax.xml.soap.SOAPException;

import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;

import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;
import org.opensaml.common.SAMLException;

import gov.ca.emsa.pulse.broker.saml.SAMLInput;

public interface EHealthQueryProducerService {
	public PRPAIN201306UV02 unMarshallPatientDiscoveryResponseObject(String xml) throws SOAPException, SAMLException;
	public AdhocQueryResponse unMarshallDocumentQueryResponseObject(String xml) throws SAMLException, SOAPException;
	public RetrieveDocumentSetResponseType unMarshallDocumentSetRetrieveResponseObject(String xml) throws SAMLException;
	public String marshallPatientDiscoveryRequest(SAMLInput samlInput, PRPAIN201305UV02 response) throws JAXBException;
	public String createSOAPFault();
	public String marshallDocumentQueryRequest(SAMLInput samlInput, AdhocQueryRequest request) throws JAXBException;
	public String marshallDocumentSetRequest(SAMLInput samlInput, RetrieveDocumentSetRequestType requestObj) throws JAXBException;
	public PRPAIN201305UV02 unMarshallPatientDiscoveryRequestObject(String xml) throws SOAPException, SAMLException;
	public AdhocQueryRequest unMarshallDocumentQueryRequestObject(String xml) throws SAMLException;
	public RetrieveDocumentSetRequestType unMarshallDocumentSetRetrieveRequestObject(String xml) throws SAMLException;

}