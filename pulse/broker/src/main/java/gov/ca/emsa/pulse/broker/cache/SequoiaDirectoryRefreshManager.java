package gov.ca.emsa.pulse.broker.cache;

import gov.ca.emsa.pulse.broker.dto.EndpointDTO;
import gov.ca.emsa.pulse.broker.dto.LocationDTO;
import gov.ca.emsa.pulse.broker.dto.LocationEndpointMapDTO;
import gov.ca.emsa.pulse.broker.manager.EndpointManager;
import gov.ca.emsa.pulse.broker.manager.LocationManager;
import gov.ca.emsa.pulse.common.domain.Endpoint;
import gov.ca.emsa.pulse.common.domain.Location;
import gov.ca.emsa.pulse.sequoia.SequoiaToPulseConverter;
import gov.ca.emsa.pulse.sequoia.domain.SequoiaBundle;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.web.client.RestTemplate;

public class SequoiaDirectoryRefreshManager {
	private static final Logger logger = LogManager.getLogger(SequoiaDirectoryRefreshManager.class);

	private LocationManager locationManager;
	private EndpointManager endpointManager;
	private String sequoiaOrganizationDirectoryUrl;
	private String status;
	
	public void getSequoiaLocationsAndEndpoints(){
		//query locations
		logger.info("Querying the organizations from " + sequoiaOrganizationDirectoryUrl);
		RestTemplate restTemplate = new RestTemplate();
		SequoiaBundle organizations = restTemplate.getForObject(sequoiaOrganizationDirectoryUrl, SequoiaBundle.class);
		//convert from sequoia bundle to internal location and endpoint objects
		List<Location> locations = SequoiaToPulseConverter.convertSequoiaBundleToLocations(organizations);
		List<Endpoint> endpoints = SequoiaToPulseConverter.convertSequoiaBundleToEndpoints(organizations);
		logger.debug("Found " + locations.size() + " locations from " + sequoiaOrganizationDirectoryUrl);
		logger.debug("Found " + endpoints.size() + " endpoints from " + sequoiaOrganizationDirectoryUrl);
		locationManager.updateLocations(locations);
		endpointManager.updateEndpoints(endpoints);
		
		//now update the mappings between locations and endpoints
		//these mappings aren't referenced by other tables so it should be ok if any need to get deleted
		List<LocationEndpointMapDTO> locationEndpointMappings = new ArrayList<LocationEndpointMapDTO>();
		for(Location ctenLocation : locations) {
			for(Endpoint ctenEndpoint : ctenLocation.getEndpoints()) {
				//find location with same external id
				LocationDTO locationToMap = locationManager.getByExternalId(ctenLocation.getExternalId());
				//find the endpoint with the same external id
				EndpointDTO endpointToMap = endpointManager.getByExternalId(ctenEndpoint.getExternalId());
				if(locationToMap != null && endpointToMap != null) {
					LocationEndpointMapDTO toMap = new LocationEndpointMapDTO();
					toMap.setEndpointId(endpointToMap.getId());
					toMap.setLocationId(locationToMap.getId());
					locationEndpointMappings.add(toMap);
				}
			}
		}
		endpointManager.updateEndpointLocationMappings(locationEndpointMappings);
	}

	public LocationManager getLocationManager() {
		return locationManager;
	}

	public void setLocationManager(LocationManager locationManager) {
		this.locationManager = locationManager;
	}

	public EndpointManager getEndpointManager() {
		return endpointManager;
	}

	public void setEndpointManager(EndpointManager endpointManager) {
		this.endpointManager = endpointManager;
	}

	public String getSequoiaOrganizationDirectoryUrl() {
		return sequoiaOrganizationDirectoryUrl;
	}

	public void setSequoiaOrganizationDirectoryUrl(
			String sequoiaOrganizationDirectoryUrl) {
		this.sequoiaOrganizationDirectoryUrl = sequoiaOrganizationDirectoryUrl;
	}
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
}
