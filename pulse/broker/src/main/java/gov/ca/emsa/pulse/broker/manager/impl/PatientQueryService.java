package gov.ca.emsa.pulse.broker.manager.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import gov.ca.emsa.pulse.broker.adapter.Adapter;
import gov.ca.emsa.pulse.broker.adapter.AdapterFactory;
import gov.ca.emsa.pulse.broker.dao.impl.DocumentDAOImpl;
import gov.ca.emsa.pulse.broker.domain.Patient;
import gov.ca.emsa.pulse.broker.dto.DomainToDtoConverter;
import gov.ca.emsa.pulse.broker.dto.OrganizationDTO;
import gov.ca.emsa.pulse.broker.dto.PatientDTO;
import gov.ca.emsa.pulse.broker.dto.PatientQueryResultDTO;
import gov.ca.emsa.pulse.broker.dto.QueryDTO;
import gov.ca.emsa.pulse.broker.dto.QueryOrganizationDTO;
import gov.ca.emsa.pulse.broker.dto.QueryStatus;
import gov.ca.emsa.pulse.broker.manager.PatientManager;
import gov.ca.emsa.pulse.broker.manager.QueryManager;
import gov.ca.emsa.pulse.service.PatientService;

@Component
public class PatientQueryService implements Runnable {
	private static final Logger logger = LogManager.getLogger(PatientQueryService.class);

	private QueryOrganizationDTO query;
	private OrganizationDTO org;
	@Autowired private QueryManager queryManager;
	@Autowired private PatientManager patientManager;
	@Autowired private AdapterFactory adapterFactory;
	private PatientDTO toSearch;
	private String samlMessage;
	
	@Override
	@Transactional
	public void run() {
		//TODO: we can make a better pulse patient id
		String pulsePatientId = "";
		if(!StringUtils.isEmpty(toSearch.getFirstName())) {
			pulsePatientId += "?firstName=" + toSearch.getFirstName();
		}
		if(!StringUtils.isEmpty(toSearch.getLastName())) {
			if(pulsePatientId.contains("?")) {
				pulsePatientId += "&";
			} else {
				pulsePatientId += "?";
			}
			pulsePatientId += "lastName=" + toSearch.getLastName();
		}
		pulsePatientId = org.getEndpointUrl() + "/patients" + pulsePatientId;
		toSearch.setPulsePatientId(pulsePatientId);
		toSearch.setOrganization(org);
		List<PatientDTO> patientMatches = patientManager.searchPatients(toSearch);
		
		boolean queryError = false;
		boolean cached = false;
		//if no cache hit
		if(patientMatches == null || patientMatches.size() == 0) {
			//query this organization directly for patient matches
			Patient[] searchResults = null;
			Adapter adapter = adapterFactory.getAdapter(org);
			if(adapter != null) {
				logger.info("Starting query to " + org.getAdapter() + " for orgquery " + query.getId());
				try {
					searchResults = adapter.queryPatients(org, toSearch, samlMessage);
				} catch(Exception ex) {
					logger.error("Exception thrown in adapter " + adapter.getClass(), ex);
					queryError = true;
				}
			}
			//cache the patients returned so we can 
			//pull them out of the cache again
			if(searchResults != null && searchResults.length > 0) {
				for(Patient patient : searchResults) {
					PatientDTO toCache = DomainToDtoConverter.convert(patient);
					toCache.setPulsePatientId(pulsePatientId);
					toCache.setOrganization(org);
					
					//cache the search results
					PatientDTO cachedPatient = patientManager.create(toCache);
					//associate them with the query
					PatientQueryResultDTO queryResult = new PatientQueryResultDTO();
					queryResult.setPatientId(cachedPatient.getId());
					queryResult.setQueryOrgId(query.getId());
					patientManager.mapPatientToQuery(queryResult);
				}
			} 
		} else {
			cached = true;
			for(PatientDTO cachedPatient : patientMatches) {
				//update the lastReadDate of each patient cache hit
				patientManager.update(cachedPatient);
				
				//associate each patient with the query results
				PatientQueryResultDTO queryResult = new PatientQueryResultDTO();
				queryResult.setPatientId(cachedPatient.getId());
				queryResult.setQueryOrgId(query.getId());
				patientManager.mapPatientToQuery(queryResult);
			}
		}
		
		query.setStatus(QueryStatus.COMPLETE.name());
		query.setEndDate(new Date());
		query.setFromCache(new Boolean(cached));
		query.setSuccess(!queryError);
		queryManager.createOrUpdateQueryOrganization(query);
		logger.info("Completed query to " + org.getAdapter() + " for orgquery " + query.getId());
	}

	public QueryOrganizationDTO getQuery() {
		return query;
	}

	public void setQuery(QueryOrganizationDTO query) {
		this.query = query;
	}

	public OrganizationDTO getOrg() {
		return org;
	}

	public void setOrg(OrganizationDTO org) {
		this.org = org;
	}

	public PatientDTO getToSearch() {
		return toSearch;
	}

	public void setToSearch(PatientDTO toSearch) {
		this.toSearch = toSearch;
	}

	public String getSamlMessage() {
		return samlMessage;
	}

	public void setSamlMessage(String samlMessage) {
		this.samlMessage = samlMessage;
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
}