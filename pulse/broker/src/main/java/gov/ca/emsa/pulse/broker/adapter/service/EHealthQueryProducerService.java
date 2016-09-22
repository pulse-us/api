package gov.ca.emsa.pulse.broker.adapter.service;

import javax.xml.bind.JAXBException;
import javax.xml.soap.SOAPException;

import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;

import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201310UV02;
import org.opensaml.common.SAMLException;

public interface EHealthQueryProducerService {
	public PRPAIN201310UV02 unMarshallPatientDiscoveryResponseObject(String xml) throws SOAPException, SAMLException;
	public AdhocQueryRequest unMarshallDocumentQueryResponseObject(String xml) throws SAMLException;
	public RetrieveDocumentSetRequestType unMarshallDocumentSetRetrieveResponseObject(String xml) throws SAMLException;
	public String marshallPatientDiscoveryRequest(PRPAIN201305UV02 response) throws JAXBException;
	public String createSOAPFault();
	public String marshallDocumentQueryRequest(AdhocQueryResponse responseObj) throws JAXBException;
	public String marshallDocumentSetRequest(RetrieveDocumentSetResponseType responseObj) throws JAXBException;
	public PRPAIN201305UV02 unMarshallPatientDiscoveryRequestObject(String xml) throws SOAPException, SAMLException;
	public AdhocQueryRequest unMarshallDocumentQueryRequestObject(String xml) throws SAMLException;
	public RetrieveDocumentSetRequestType unMarshallDocumentSetRetrieveRequestObject(String xml) throws SAMLException;
}