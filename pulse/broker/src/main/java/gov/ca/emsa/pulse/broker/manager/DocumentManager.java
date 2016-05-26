package gov.ca.emsa.pulse.broker.manager;

import java.util.Date;
import java.util.List;

import gov.ca.emsa.pulse.broker.dto.DocumentDTO;
import gov.ca.emsa.pulse.broker.dto.PatientDTO;

public interface DocumentManager {
	public List<DocumentDTO> queryDocumentsForPatient(PatientDTO patient) throws Exception;
	public void cleanupDocumentCache(Date oldestAllowedDocument);
}
