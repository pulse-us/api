package gov.ca.emsa.pulse.common.soap;

import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import gov.ca.emsa.pulse.common.domain.DocumentQuery;
import gov.ca.emsa.pulse.common.domain.DocumentRetrieve;
import gov.ca.emsa.pulse.common.domain.PatientSearch;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;

import org.hl7.v3.PRPAIN201305UV02;

public interface SOAPToJSONService {
	public PatientSearch convertToPatientSearch(PRPAIN201305UV02 request);
	public DocumentQuery convertToDocumentQuery(AdhocQueryRequest request);
	public DocumentRetrieve convertToDocumentSetRequest(RetrieveDocumentSetRequestType request);
}
