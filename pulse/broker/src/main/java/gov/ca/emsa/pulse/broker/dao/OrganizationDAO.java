package gov.ca.emsa.pulse.broker.dao;

import java.util.List;

import gov.ca.emsa.pulse.broker.dto.OrganizationDTO;
import gov.ca.emsa.pulse.broker.entity.OrganizationEntity;

public interface OrganizationDAO {
	
	public OrganizationEntity getOrganizationById(Long id);
	public OrganizationDTO create(OrganizationDTO org);
	public OrganizationDTO update(OrganizationDTO org);
	public List<OrganizationEntity> getAllEntities();
	public List<OrganizationDTO> findAll();
	public void delete(OrganizationDTO organizationDTO);

}
