package gov.ca.emsa.pulse.broker.adapter;

import gov.ca.emsa.pulse.broker.domain.Patient;
import gov.ca.emsa.pulse.broker.dto.OrganizationDTO;
import gov.ca.emsa.pulse.broker.dto.PatientDTO;

public interface Adapter {
	public Patient[] queryPatients(OrganizationDTO org, PatientDTO toSearch, String samlMessage);
}
