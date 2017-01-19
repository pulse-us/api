package gov.ca.emsa.pulse.broker.manager.impl;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import gov.ca.emsa.pulse.auth.user.CommonUser;
import gov.ca.emsa.pulse.broker.adapter.Adapter;
import gov.ca.emsa.pulse.broker.adapter.AdapterFactory;
import gov.ca.emsa.pulse.broker.domain.EndpointStatusEnum;
import gov.ca.emsa.pulse.broker.domain.EndpointTypeEnum;
import gov.ca.emsa.pulse.broker.dto.DocumentDTO;
import gov.ca.emsa.pulse.broker.dto.DocumentQueryResults;
import gov.ca.emsa.pulse.broker.dto.LocationDTO;
import gov.ca.emsa.pulse.broker.dto.LocationEndpointDTO;
import gov.ca.emsa.pulse.broker.dto.PatientDTO;
import gov.ca.emsa.pulse.broker.dto.PatientLocationMapDTO;
import gov.ca.emsa.pulse.broker.manager.DocumentManager;
import gov.ca.emsa.pulse.broker.manager.PatientManager;
import gov.ca.emsa.pulse.broker.manager.QueryManager;
import gov.ca.emsa.pulse.broker.saml.SAMLInput;
import gov.ca.emsa.pulse.common.domain.QueryLocationStatus;
import gov.ca.emsa.pulse.common.domain.QueryStatus;
import gov.ca.emsa.pulse.cten.IheStatus;

@Component
public class DocumentQueryService implements Runnable {
	private static final Logger logger = LogManager.getLogger(DocumentQueryService.class);

	private PatientLocationMapDTO patientLocationMap;
	private LocationDTO location;
	@Autowired private QueryManager queryManager;
	@Autowired private PatientManager patientManager;
	@Autowired private DocumentManager docManager;
	@Autowired private AdapterFactory adapterFactory;
	private PatientDTO toSearch;
	private SAMLInput samlInput;
	private CommonUser user;
	
	@Override
	public void run() {
		//query this organization directly for 
		boolean querySuccess = true;
		DocumentQueryResults searchResults = null;
		LocationEndpointDTO endpointToQuery = null;
		if(location.getEndpoints() != null) {
			for(LocationEndpointDTO endpoint : location.getEndpoints()) {
				if(endpoint.getEndpointType() != null && 
						endpoint.getEndpointType().getName().equalsIgnoreCase(EndpointTypeEnum.DOCUMENT_DISCOVERY.getName()) && 
						endpoint.getEndpointStatus() != null && 
						endpoint.getEndpointStatus().getName().equalsIgnoreCase(EndpointStatusEnum.ACTIVE.getName())) {
						endpointToQuery = endpoint;
					}
			}
		}
		
		if(endpointToQuery == null) {
			logger.error("The location " + location.getName() + " does not have an active document discovery endpoint.");
			querySuccess = false;
		} else {
			Adapter adapter = adapterFactory.getAdapter(endpointToQuery);
			if(adapter != null) {
				logger.info("Starting query to endpoint with external id '" + endpointToQuery.getExternalId() + "'");
				try {
					searchResults = adapter.queryDocuments(user, endpointToQuery, patientLocationMap, samlInput);
				} catch(Exception ex) {
					logger.error("Exception thrown in adapter " + adapter.getClass(), ex);
					querySuccess = false;
				}
			}
			
			//store the returned document info
			if(searchResults != null && searchResults.getStatus() == IheStatus.Success) {
				List<DocumentDTO> docs = searchResults.getResults();
				if(docs != null && docs.size() > 0) {
					for(DocumentDTO doc : docs) {
						doc.setPatientLocationMapId(patientLocationMap.getId());
						//save document
						docManager.create(doc);
					}
				} else {
					logger.info("Got 0 document results from query to endpoint with external id '" + endpointToQuery.getExternalId() + "'");
				}
			} else if(searchResults != null && searchResults.getStatus() == IheStatus.Failure) {
				querySuccess = false;
			} else {
				logger.error("Got a null response back from query to endpoint with external id '" + endpointToQuery.getExternalId() + "'");
			}
			logger.info("Completed query to endpoint with external id '" + endpointToQuery.getExternalId() + "'");
		}
		
		patientLocationMap.setDocumentsQueryEnd(new Date());
		if(querySuccess) {
			patientLocationMap.setDocumentsQueryStatus(QueryLocationStatus.Successful);
		} else {
			patientLocationMap.setDocumentsQueryStatus(QueryLocationStatus.Failed);
		}
		//update patient loc map
		try {
			patientManager.updatePatientLocationMap(patientLocationMap);
		} catch(SQLException ex) {
			logger.error("Could not update patient location map with "
					+ "[id: " + patientLocationMap.getId() + ", "
					+ "orgPatientRecordId: " + patientLocationMap.getExternalPatientRecordId() + ", " 
					+ "orgId: " + patientLocationMap.getLocationId() + ", " 
					+ "patientId: " + patientLocationMap.getPatientId() + "]");
		}
	}
	
	

	public CommonUser getUser() {
		return user;
	}



	public void setUser(CommonUser user) {
		this.user = user;
	}



	public LocationDTO getLocation() {
		return location;
	}

	public void setLocation(LocationDTO location) {
		this.location = location;
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

	public PatientLocationMapDTO getPatientLocationMap() {
		return patientLocationMap;
	}

	public void setPatientLocationMap(PatientLocationMapDTO patientLocationMap) {
		this.patientLocationMap = patientLocationMap;
	}

	public PatientDTO getToSearch() {
		return toSearch;
	}

	public void setToSearch(PatientDTO toSearch) {
		this.toSearch = toSearch;
	}

	public SAMLInput getSamlInput() {
		return samlInput;
	}

	public void setSamlInput(SAMLInput samlInput) {
		this.samlInput = samlInput;
	}
}