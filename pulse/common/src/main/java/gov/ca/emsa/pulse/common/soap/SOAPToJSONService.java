package gov.ca.emsa.pulse.common.soap;

import gov.ca.emsa.pulse.common.domain.Document;
import gov.ca.emsa.pulse.common.domain.DocumentQuery;
import gov.ca.emsa.pulse.common.domain.DocumentRetrieve;
import gov.ca.emsa.pulse.common.domain.Patient;
import gov.ca.emsa.pulse.common.domain.PatientRecord;
import gov.ca.emsa.pulse.common.domain.PatientSearch;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType.DocumentResponse;

import java.util.List;

import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;

import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;

public interface SOAPToJSONService {
	public PatientSearch convertToPatientSearch(PRPAIN201305UV02 request);
	public List<PatientRecord> convertToPatientRecords(PRPAIN201306UV02 response);
	public List<Patient> convertToPatients(PRPAIN201306UV02 request);
	public DocumentQuery convertToDocumentQuery(AdhocQueryRequest request);
	public List<Document> convertToDocumentQueryResponse(AdhocQueryResponse response);
	public DocumentRetrieve convertToDocumentSetRequest(RetrieveDocumentSetRequestType request);
	public List<DocumentResponse> convertToDocumentSetResponse(RetrieveDocumentSetResponseType response);
}
