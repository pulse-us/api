package gov.ca.emsa.pulse.broker.manager;

import gov.ca.emsa.pulse.broker.dto.LocationDTO;
import gov.ca.emsa.pulse.common.domain.Location;
import gov.ca.emsa.pulse.common.domain.stats.LocationStatistics;
import gov.ca.emsa.pulse.common.domain.stats.RequestStatistics;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public interface OrganizationManager {
	public LocationDTO getById(Long id);
	public void updateOrganizations(ArrayList<Location> orgs);
	public List<LocationDTO> getAll();
	public List<LocationStatistics> getPatientDiscoveryRequestStatistics(Date startDate, Date endDate);
}
