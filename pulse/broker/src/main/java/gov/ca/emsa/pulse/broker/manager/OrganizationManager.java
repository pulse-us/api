package gov.ca.emsa.pulse.broker.manager;

import gov.ca.emsa.pulse.broker.dto.OrganizationDTO;
import gov.ca.emsa.pulse.common.domain.Organization;

import java.util.ArrayList;
import java.util.List;

public interface OrganizationManager {
	public OrganizationDTO getById(Long id);
	public void updateOrganizations(ArrayList<Organization> orgs);
	public List<OrganizationDTO> getAll();
}
