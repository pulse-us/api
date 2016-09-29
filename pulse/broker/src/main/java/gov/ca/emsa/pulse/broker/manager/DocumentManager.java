package gov.ca.emsa.pulse.broker.manager;

import java.util.List;

import gov.ca.emsa.pulse.broker.dto.DocumentDTO;
import gov.ca.emsa.pulse.broker.dto.PatientOrganizationMapDTO;
import gov.ca.emsa.pulse.broker.saml.SAMLInput;

public interface DocumentManager {
	public DocumentDTO create(DocumentDTO toCreate);
	public void queryForDocuments(SAMLInput samlMessage, PatientOrganizationMapDTO dto);
	public List<DocumentDTO> getDocumentsForPatient(Long patientId);
	public String getDocumentById(String samlMessage, Long documentId);
}
