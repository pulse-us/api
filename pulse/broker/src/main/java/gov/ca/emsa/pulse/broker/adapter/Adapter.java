package gov.ca.emsa.pulse.broker.adapter;

import gov.ca.emsa.pulse.broker.dto.DocumentDTO;
import gov.ca.emsa.pulse.broker.dto.LocationDTO;
import gov.ca.emsa.pulse.broker.dto.LocationEndpointDTO;
import gov.ca.emsa.pulse.broker.dto.PatientLocationMapDTO;
import gov.ca.emsa.pulse.broker.dto.PatientRecordDTO;
import gov.ca.emsa.pulse.broker.saml.SAMLInput;
import gov.ca.emsa.pulse.common.domain.PatientSearch;

import java.util.List;

public interface Adapter {
	public List<PatientRecordDTO> queryPatients(LocationEndpointDTO endpoint, PatientSearch toSearch, SAMLInput samlInput) throws Exception;
	public List<DocumentDTO> queryDocuments(LocationEndpointDTO endpoint, PatientLocationMapDTO patientLocationMap, SAMLInput samlInput);
	public void retrieveDocumentsContents(LocationEndpointDTO endpoint, List<DocumentDTO> documents, SAMLInput samlInput);
}
