package gov.ca.emsa.pulse.service;

import gov.ca.emsa.pulse.common.domain.DocumentQuery;
import gov.ca.emsa.pulse.common.domain.PatientSearch;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;

import org.hl7.v3.PRPAIN201305UV02;

public interface SOAPToJSONService {
	public PatientSearch convertToPatientSearch(PRPAIN201305UV02 request);
	public DocumentQuery convertToDocumentQuery(AdhocQueryRequest request);
}
