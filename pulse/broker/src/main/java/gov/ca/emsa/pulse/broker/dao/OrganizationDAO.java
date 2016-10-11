package gov.ca.emsa.pulse.broker.dao;

import gov.ca.emsa.pulse.broker.dto.OrganizationDTO;
import gov.ca.emsa.pulse.broker.entity.OrganizationEntity;

import java.util.List;

public interface OrganizationDAO {
	
	public OrganizationDTO create(OrganizationDTO org);
	public OrganizationDTO update(OrganizationDTO org);
	public List<OrganizationEntity> getAllEntities();
	public List<OrganizationDTO> findAll();
	public void delete(OrganizationDTO organizationDTO);
	public OrganizationDTO findById(Long id);
	public List<OrganizationDTO> findByName(String name);
}
