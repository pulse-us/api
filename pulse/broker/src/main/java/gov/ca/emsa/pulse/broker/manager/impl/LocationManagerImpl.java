package gov.ca.emsa.pulse.broker.manager.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gov.ca.emsa.pulse.broker.dao.LocationDAO;
import gov.ca.emsa.pulse.broker.dao.PatientDiscoveryQueryStatisticsDAO;
import gov.ca.emsa.pulse.broker.dao.impl.LocationDAOImpl;
import gov.ca.emsa.pulse.broker.dto.DomainToDtoConverter;
import gov.ca.emsa.pulse.broker.dto.LocationDTO;
import gov.ca.emsa.pulse.broker.entity.PatientDiscoveryRequestStatisticsEntity;
import gov.ca.emsa.pulse.broker.manager.LocationManager;
import gov.ca.emsa.pulse.common.domain.Location;
import gov.ca.emsa.pulse.common.domain.LocationStatus;
import gov.ca.emsa.pulse.common.domain.stats.LocationStatistics;
import gov.ca.emsa.pulse.common.domain.stats.RequestStatistics;

@Service
public class LocationManagerImpl implements LocationManager {
	private static final Logger logger = LogManager.getLogger(LocationManagerImpl.class);

	@Autowired private LocationDAO locationDao;
	@Autowired private PatientDiscoveryQueryStatisticsDAO statsDao;
	
	public LocationDTO getById(Long id) {
		return locationDao.findById(id);
	}
	
	@Transactional
	public void updateLocations(List<Location> newLocations){
		List<LocationDTO> currentLocations = getAll();
		
		//look at the old locations for anything not in the new locations and delete
		for(LocationDTO oldLocation : currentLocations) {
			boolean stillExists = false;
			for(Location newLocation : newLocations) {
				if(oldLocation.getExternalId().equals(newLocation.getExternalId())) {
					stillExists = true;
				}
			}
			
			if(!stillExists) {
				locationDao.delete(oldLocation);
			}
		}
		
		//look at the new locations for anything that should be created or updated
		for(Location newLocation : newLocations) {
			String externalId = newLocation.getExternalId();
			LocationDTO existingLocation = locationDao.findByExternalId(externalId);
			if(existingLocation == null) {
				try {
					LocationDTO toCreate = DomainToDtoConverter.convert(newLocation);
					locationDao.create(toCreate);
				} catch(Exception ex) {
					logger.error("Error creating location with external id " + externalId, ex);
				}
			} else {
				try {
					LocationDTO toUpdate = DomainToDtoConverter.convert(newLocation);
					toUpdate.setId(existingLocation.getId());
					locationDao.update(toUpdate);
				} catch(Exception ex) {
					logger.error("Error updating location with id " + existingLocation.getId(), ex);
				}
			}
		}
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<LocationDTO> getAll() {
		List<LocationDTO> results = locationDao.findAll();
		return results;
	}

	@Override
	@Transactional(readOnly = true)
	public List<LocationStatistics> getPatientDiscoveryRequestStatistics(Date startDate, Date endDate) {
		List<PatientDiscoveryRequestStatisticsEntity> stats = statsDao.getStatistics(startDate, endDate);
		List<LocationStatistics> results = new ArrayList<LocationStatistics>();
		if(stats != null) {
			for(PatientDiscoveryRequestStatisticsEntity stat : stats) {
				LocationStatistics result = new LocationStatistics();
				result.setCalculationStart(startDate);
				result.setCalculationEnd(endDate);
				//don't have a filter param for calculating based on the last N events yet
				Location loc = new Location();
				loc.setId(stat.getLocationId());
				loc.setName(stat.getLocationName());
				loc.setType(stat.getLocationType());
				if(stat.getLocationStatus() != null) {
					LocationStatus status = new LocationStatus();
					status.setName(stat.getLocationStatus());
					loc.setStatus(status);
				}
				result.setLocation(loc);
				RequestStatistics orgStat = new RequestStatistics();
				orgStat.setRequestCount(stat.getTotalRequestCount());
				orgStat.setRequestAvgCompletionSeconds(stat.getTotalRequestAverageSeconds() != null ? stat.getTotalRequestAverageSeconds().longValue() : null);
				orgStat.setRequestSuccessCount(stat.getSuccessfulRequestCount());
				orgStat.setRequestSuccessAvgCompletionSeconds(stat.getSuccessfulRequestAverageSeconds() != null ? stat.getSuccessfulRequestAverageSeconds().longValue() : null);
				orgStat.setRequestFailureCount(stat.getFailedRequestCount());
				orgStat.setRequestFailureAvgCompletionSeconds(stat.getFailedRequestAverageSeconds() != null ? stat.getFailedRequestAverageSeconds().longValue() : null);
				orgStat.setRequestCancelledCount(stat.getCancelledRequestCount());
				orgStat.setRequestCancelledAvgCompletionSeconds(stat.getCancelledRequestAverageSeconds() != null ? stat.getCancelledRequestAverageSeconds().longValue() : null);
				result.setPatientDiscoveryStats(orgStat);
				results.add(result);
			}
		}
		return results;
	}
}
