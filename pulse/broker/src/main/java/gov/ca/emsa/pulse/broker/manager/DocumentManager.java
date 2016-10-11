package gov.ca.emsa.pulse.broker.manager;

import gov.ca.emsa.pulse.broker.dto.DocumentDTO;
import gov.ca.emsa.pulse.broker.dto.OrganizationDTO;
import gov.ca.emsa.pulse.broker.dto.PatientOrganizationMapDTO;
import gov.ca.emsa.pulse.broker.saml.SAMLInput;

import java.util.List;

public interface DocumentManager {
	public DocumentDTO create(DocumentDTO toCreate);
	public void queryForDocuments(SAMLInput samlMessage, PatientOrganizationMapDTO dto);
	public void queryForDocumentContents(SAMLInput samlInput, OrganizationDTO org, List<DocumentDTO> docsFromOrg);
	public List<DocumentDTO> getDocumentsForPatient(Long patientId);
	public String getDocumentById(SAMLInput samlInput, Long documentId);
}
