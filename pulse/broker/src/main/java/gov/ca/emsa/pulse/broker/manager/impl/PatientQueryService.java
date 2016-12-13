package gov.ca.emsa.pulse.broker.manager.impl;

import java.util.Date;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import gov.ca.emsa.pulse.auth.user.CommonUser;
import gov.ca.emsa.pulse.broker.adapter.Adapter;
import gov.ca.emsa.pulse.broker.adapter.AdapterFactory;
import gov.ca.emsa.pulse.broker.domain.EndpointStatusEnum;
import gov.ca.emsa.pulse.broker.domain.EndpointTypeEnum;
import gov.ca.emsa.pulse.broker.dto.LocationDTO;
import gov.ca.emsa.pulse.broker.dto.LocationEndpointDTO;
import gov.ca.emsa.pulse.broker.dto.PatientRecordDTO;
import gov.ca.emsa.pulse.broker.dto.QueryLocationMapDTO;
import gov.ca.emsa.pulse.broker.manager.LocationManager;
import gov.ca.emsa.pulse.broker.manager.PatientManager;
import gov.ca.emsa.pulse.broker.manager.QueryManager;
import gov.ca.emsa.pulse.broker.saml.SAMLInput;
import gov.ca.emsa.pulse.common.domain.PatientSearch;
import gov.ca.emsa.pulse.common.domain.QueryLocationStatus;

@Component
public class PatientQueryService implements Runnable {
	

	private static final Logger logger = LogManager.getLogger(PatientQueryService.class);

	private QueryLocationMapDTO queryLocationMap;
	@Autowired private QueryManager queryManager;
	@Autowired private PatientManager patientManager;
	@Autowired private LocationManager locationManager;
	@Autowired private AdapterFactory adapterFactory;
	private PatientSearch toSearch;
	private SAMLInput samlInput;
	private CommonUser user;
	
	@Override
	public void run() {
		boolean queryError = false;
		//query this organization directly for patient matches
		List<PatientRecordDTO> searchResults = null;
		LocationEndpointDTO endpointToQuery = null;
		
		LocationDTO location = locationManager.getById(queryLocationMap.getLocationId());
		if(location == null) {
			logger.error("Could not find location with id " + queryLocationMap.getLocationId());
			return;
		} else if(location.getEndpoints() != null) {
			for(LocationEndpointDTO endpoint : location.getEndpoints()) {
				if(endpoint.getEndpointType() != null && 
					endpoint.getEndpointType().getName().equalsIgnoreCase(EndpointTypeEnum.PATIENT_DISCOVERY.getName()) && 
					endpoint.getEndpointStatus() != null && 
					endpoint.getEndpointStatus().getName().equalsIgnoreCase(EndpointStatusEnum.ACTIVE.getName())) {
					endpointToQuery = endpoint;
				}
			}
		}
		
		if(endpointToQuery == null) {
			logger.error("The location " + location.getName() + " does not have an active patient discovery endpoint.");
			return;
		}
		
		Adapter adapter = adapterFactory.getAdapter(endpointToQuery);
		if(adapter != null) {
			logger.info("Starting query to endpoint with external id '" + endpointToQuery.getExternalId() + "'");
			try {
				searchResults = adapter.queryPatients(user, endpointToQuery, toSearch, samlInput);
				logger.info("Successfully queried endpoint with external id '" + endpointToQuery.getExternalId() + "'");
			} catch(Exception ex) {
				logger.error("Exception thrown in adapter " + adapter.getClass(), ex);
				queryError = true;
			}
		}
		//store the patients returned so we can retrieve them later when all orgs have finished querying
		if(searchResults != null && searchResults.size() > 0) {
			logger.info("Found " + searchResults.size() + " results for endpoint with external id '" + endpointToQuery.getExternalId() + "'");
			for(PatientRecordDTO patient : searchResults) {
				patient.setQueryLocationId(queryLocationMap.getId());
					
				//save the search results
				queryManager.addPatientRecord(patient);
				logger.info("Added patient record to the orgStatus " + queryLocationMap.getId());
			}
		} else {
			logger.info("Found 0 results for endpoint with external id '" + endpointToQuery.getExternalId() + "'");
		}
		
		synchronized(queryLocationMap.getQueryId()) {
			queryLocationMap.setStatus(queryError ? QueryLocationStatus.Failed : QueryLocationStatus.Successful);
			queryLocationMap.setEndDate(new Date());
			queryManager.createOrUpdateQueryLocation(queryLocationMap);
			queryManager.updateQueryStatusFromLocations(queryLocationMap.getQueryId());
		}
		logger.info("Completed query to endpoint with external id '" + 
				endpointToQuery.getExternalId());
	}
	
	public CommonUser getUser() {
		return user;
	}

	public void setUser(CommonUser user) {
		this.user = user;
	}

	public QueryLocationMapDTO getQueryLocation() {
		return queryLocationMap;
	}

	public void setQueryLocation(QueryLocationMapDTO queryLocationMap) {
		this.queryLocationMap = queryLocationMap;
	}

	public QueryManager getQueryManager() {
		return queryManager;
	}

	public void setQueryManager(QueryManager queryManager) {
		this.queryManager = queryManager;
	}

	public PatientManager getPatientManager() {
		return patientManager;
	}

	public void setPatientManager(PatientManager patientManager) {
		this.patientManager = patientManager;
	}

	public AdapterFactory getAdapterFactory() {
		return adapterFactory;
	}

	public void setAdapterFactory(AdapterFactory adapterFactory) {
		this.adapterFactory = adapterFactory;
	}

	public PatientSearch getToSearch() {
		return toSearch;
	}

	public void setToSearch(PatientSearch toSearch) {
		this.toSearch = toSearch;
	}

	public LocationManager getLocationManager() {
		return locationManager;
	}

	public void setLocationManager(LocationManager locationManager) {
		this.locationManager = locationManager;
	}

	public SAMLInput getSamlInput() {
		return samlInput;
	}

	public void setSamlInput(SAMLInput samlInput) {
		this.samlInput = samlInput;
	}
}