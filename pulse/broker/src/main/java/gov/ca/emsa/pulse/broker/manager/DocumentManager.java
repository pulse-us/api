package gov.ca.emsa.pulse.broker.manager;

import java.util.List;

import gov.ca.emsa.pulse.broker.dto.DocumentDTO;
import gov.ca.emsa.pulse.broker.dto.PatientOrganizationMapDTO;

public interface DocumentManager {
	public DocumentDTO create(DocumentDTO toCreate);
	public void queryForDocuments(String samlMessage, PatientOrganizationMapDTO dto);
	public List<DocumentDTO> getDocumentsForPatient(Long patientId);
	public String getDocumentById(String samlMessage, Long documentId);
}
