package gov.ca.emsa.pulse.broker.manager;

import java.util.ArrayList;
import java.util.List;

import gov.ca.emsa.pulse.broker.domain.Organization;
import gov.ca.emsa.pulse.broker.dto.OrganizationDTO;

public interface OrganizationManager {
	public void updateOrganizations(ArrayList<Organization> orgs);
	public List<OrganizationDTO> getAll();
}
