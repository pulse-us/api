package gov.ca.emsa.pulse.broker.dao;

import gov.ca.emsa.pulse.broker.dto.DocumentDTO;
import gov.ca.emsa.pulse.common.domain.QueryEndpointStatus;

import java.util.List;

public interface DocumentDAO {
	public DocumentDTO create(DocumentDTO dto);
	public DocumentDTO update(DocumentDTO dto);
	public void delete(Long id);
	public List<DocumentDTO> findAll();	
	public DocumentDTO getById(Long id);
	public List<DocumentDTO> getDocumentsWithStatusForPatient(Long patientId, List<QueryEndpointStatus> statuses);
}
