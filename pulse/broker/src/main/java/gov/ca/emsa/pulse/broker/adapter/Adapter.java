package gov.ca.emsa.pulse.broker.adapter;

import gov.ca.emsa.pulse.broker.dto.DocumentDTO;
import gov.ca.emsa.pulse.broker.dto.LocationDTO;
import gov.ca.emsa.pulse.broker.dto.PatientLocationMapDTO;
import gov.ca.emsa.pulse.broker.dto.PatientRecordDTO;
import gov.ca.emsa.pulse.broker.saml.SAMLInput;
import gov.ca.emsa.pulse.common.domain.PatientSearch;

import java.util.List;

public interface Adapter {
	public List<PatientRecordDTO> queryPatients(LocationDTO org, PatientSearch toSearch, SAMLInput samlInput);
	public List<DocumentDTO> queryDocuments(LocationDTO org, PatientLocationMapDTO orgPatient, SAMLInput samlInput);
	public void retrieveDocumentsContents(LocationDTO org, List<DocumentDTO> documents, SAMLInput samlInput);
}
