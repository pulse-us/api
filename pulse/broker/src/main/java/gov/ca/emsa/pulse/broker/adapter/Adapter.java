package gov.ca.emsa.pulse.broker.adapter;

import gov.ca.emsa.pulse.broker.domain.Document;
import gov.ca.emsa.pulse.broker.domain.Patient;
import gov.ca.emsa.pulse.broker.dto.OrganizationDTO;
import gov.ca.emsa.pulse.broker.dto.PatientOrganizationMapDTO;
import gov.ca.emsa.pulse.broker.dto.PatientRecordDTO;

public interface Adapter {
	public Patient[] queryPatients(OrganizationDTO org, PatientRecordDTO toSearch, String samlMessage);
	public Document[] queryDocuments(OrganizationDTO org, PatientOrganizationMapDTO orgPatient, String samlMessage);
}
