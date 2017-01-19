package gov.ca.emsa.pulse.broker.adapter;

import gov.ca.emsa.pulse.auth.user.CommonUser;
import gov.ca.emsa.pulse.broker.dto.DocumentDTO;
import gov.ca.emsa.pulse.broker.dto.DocumentQueryResults;
import gov.ca.emsa.pulse.broker.dto.LocationEndpointDTO;
import gov.ca.emsa.pulse.broker.dto.PatientLocationMapDTO;
import gov.ca.emsa.pulse.broker.dto.PatientRecordResults;
import gov.ca.emsa.pulse.broker.saml.SAMLInput;
import gov.ca.emsa.pulse.common.domain.PatientSearch;

import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.List;

public interface Adapter {
	public PatientRecordResults queryPatients(CommonUser user, LocationEndpointDTO endpoint, PatientSearch toSearch, SAMLInput samlInput) throws Exception;
	public  DocumentQueryResults queryDocuments(CommonUser user, LocationEndpointDTO endpoint, PatientLocationMapDTO patientLocationMap, SAMLInput samlInput) throws UnknownHostException, UnsupportedEncodingException;
	public void retrieveDocumentsContents(CommonUser user, LocationEndpointDTO endpoint, List<DocumentDTO> documents, SAMLInput samlInput, PatientLocationMapDTO patientLocationMap) 
			throws UnknownHostException, UnsupportedEncodingException, IheErrorException;
}
