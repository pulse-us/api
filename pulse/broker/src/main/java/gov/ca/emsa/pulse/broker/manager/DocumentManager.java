package gov.ca.emsa.pulse.broker.manager;

import java.util.List;

import gov.ca.emsa.pulse.broker.dto.DocumentDTO;
import gov.ca.emsa.pulse.broker.dto.PatientRecordDTO;

public interface DocumentManager {
	public List<DocumentDTO> queryDocumentsForPatient(String samlMessage, PatientRecordDTO patient) throws Exception;
	public String getDocumentById(String samlMessage, Long documentId);
}
