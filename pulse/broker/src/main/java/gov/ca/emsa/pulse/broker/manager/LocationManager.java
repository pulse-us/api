package gov.ca.emsa.pulse.broker.manager;

import gov.ca.emsa.pulse.broker.dto.LocationDTO;
import gov.ca.emsa.pulse.common.domain.Location;
import gov.ca.emsa.pulse.common.domain.stats.LocationStatistics;

import java.util.Date;
import java.util.List;

public interface LocationManager {
	public LocationDTO getById(Long id);
	public void updateLocations(List<Location> locations);
	public List<LocationDTO> getAll();
	public List<LocationStatistics> getPatientDiscoveryRequestStatistics(Date startDate, Date endDate);
}
