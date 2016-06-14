package gov.ca.emsa.pulse.broker.dao;

import java.util.Date;
import java.util.List;

import gov.ca.emsa.pulse.broker.dto.DocumentDTO;

public interface DocumentDAO {
	public DocumentDTO create(DocumentDTO dto);
	public DocumentDTO update(DocumentDTO dto);
	public void delete(Long id);
	public List<DocumentDTO> findAll();	
	public DocumentDTO getById(Long id);
	public List<DocumentDTO> getByPatientId(Long patientId);
	public void deleteItemsOlderThan(Date oldestItem);
}
