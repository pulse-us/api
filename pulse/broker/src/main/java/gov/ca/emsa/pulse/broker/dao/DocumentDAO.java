package gov.ca.emsa.pulse.broker.dao;

import gov.ca.emsa.pulse.broker.dto.DocumentDTO;

import java.util.List;

public interface DocumentDAO {
	public DocumentDTO create(DocumentDTO dto);
	public DocumentDTO update(DocumentDTO dto);
	public void delete(Long id);
	public List<DocumentDTO> findAll();	
	public DocumentDTO getById(Long id);
	public List<DocumentDTO> getByPatientId(Long patientId);
}
