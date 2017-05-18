package gov.ca.emsa.pulse.common.soap;

import gov.ca.emsa.pulse.common.domain.Document;
import gov.ca.emsa.pulse.common.domain.DocumentWrapper;
import gov.ca.emsa.pulse.common.domain.PatientRecord;
import gov.ca.emsa.pulse.common.domain.PatientSearch;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;

import java.util.List;

import javax.xml.bind.JAXBElement;

import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;

import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.PRPAMT201306UV02QueryByParameter;

public interface JSONToSOAPService {
	public PRPAIN201305UV02 convertFromPatientSearch(PatientSearch ps, String pulseOID, String orgOID);
	public AdhocQueryRequest convertToDocumentRequest(String patientId);
	public AdhocQueryResponse convertDocumentListToSOAPResponse(List<Document> docs, String patientId);
	public RetrieveDocumentSetResponseType convertDocumentSetToSOAPResponse(List<DocumentWrapper> docs);
	public RetrieveDocumentSetRequestType convertToRetrieveDocumentSetRequest(List<Document> documents);
	public JAXBElement<PRPAMT201306UV02QueryByParameter> getQueryByParameter(PRPAIN201305UV02 message);
	
	public PRPAIN201306UV02 createNoPatientRecordsResponse(PRPAIN201305UV02 request);
	public AdhocQueryResponse createNoDocumentListResponse();
	public RetrieveDocumentSetResponseType createNoDocumentSetRetrieveResponse();
}
