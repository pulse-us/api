package gov.ca.emsa.pulse.broker.adapter;

import gov.ca.emsa.pulse.auth.user.CommonUser;
import gov.ca.emsa.pulse.broker.dto.DocumentDTO;
import gov.ca.emsa.pulse.broker.dto.DocumentQueryResults;
import gov.ca.emsa.pulse.broker.dto.EndpointDTO;
import gov.ca.emsa.pulse.broker.dto.PatientEndpointMapDTO;
import gov.ca.emsa.pulse.broker.dto.PatientRecordResults;
import gov.ca.emsa.pulse.broker.saml.SAMLInput;
import gov.ca.emsa.pulse.common.domain.PatientSearch;

import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.List;

public interface Adapter {
	public PatientRecordResults queryPatients(CommonUser user, EndpointDTO endpoint, PatientSearch toSearch, SAMLInput samlInput) throws Exception;
	public  DocumentQueryResults queryDocuments(CommonUser user, EndpointDTO endpoint, PatientEndpointMapDTO patientLocationMap, SAMLInput samlInput) throws UnknownHostException, UnsupportedEncodingException;
	public void retrieveDocumentsContents(CommonUser user, EndpointDTO endpoint, List<DocumentDTO> documents, SAMLInput samlInput, PatientEndpointMapDTO patientLocationMap) 
			throws UnknownHostException, UnsupportedEncodingException, IheErrorException;
}
