package gov.ca.emsa.pulse.broker.manager.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import gov.ca.emsa.pulse.broker.dao.PatientDAO;
import gov.ca.emsa.pulse.broker.dao.QueryDAO;
import gov.ca.emsa.pulse.broker.domain.Patient;
import gov.ca.emsa.pulse.broker.dto.DomainToDtoConverter;
import gov.ca.emsa.pulse.broker.dto.OrganizationDTO;
import gov.ca.emsa.pulse.broker.dto.PatientDTO;
import gov.ca.emsa.pulse.broker.dto.PatientQueryResultDTO;
import gov.ca.emsa.pulse.broker.dto.QueryDTO;
import gov.ca.emsa.pulse.broker.dto.QueryOrganizationDTO;
import gov.ca.emsa.pulse.broker.dto.QueryStatus;

@Component
public class PatientQueryService implements Runnable {
	private QueryDTO query;
	private OrganizationDTO org;
	@Autowired private QueryDAO queryDao;
	@Autowired private PatientDAO patientDao;
	private PatientDTO toSearch;
	private String samlMessage;
	
	@Override
	@Transactional
	public void run() {
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
		
		//look for cache hits for this organization/patientID combo			
		pulsePatientId = org.getEndpointUrl() + "/patients" + pulsePatientId;
		toSearch.setPulsePatientId(pulsePatientId);
		toSearch.setOrganization(org);
		List<PatientDTO> patientMatches = patientDao.getByPatientIdAndOrg(toSearch);
		
		boolean cached = false;
		//if no cache hit
		if(patientMatches == null || patientMatches.size() == 0) {
			//query this organization directly for patient matches
			String postUrl = org.getEndpointUrl() + "/patients";
			MultiValueMap<String,String> parameters = new LinkedMultiValueMap<String,String>();
			parameters.add("firstName", toSearch.getFirstName());
			parameters.add("lastName", toSearch.getLastName());
			parameters.add("samlMessage", samlMessage);
			RestTemplate restTemplate = new RestTemplate();
			Patient[] searchResults = restTemplate.postForObject(postUrl, parameters, Patient[].class);
			
			//cache the patients returned so we can 
			//pull them out of the cache again
			if(searchResults != null && searchResults.length > 0) {
				for(Patient patient : searchResults) {
					PatientDTO toCache = DomainToDtoConverter.convert(patient);
					toCache.setPulsePatientId(pulsePatientId);
					toCache.setOrganization(org);
					
					//cache the search results
					PatientDTO cachedPatient = patientDao.create(toCache);
					//associate them with the query
					PatientQueryResultDTO queryResult = new PatientQueryResultDTO();
					queryResult.setPatientId(cachedPatient.getId());
					queryResult.setQueryOrgId(org.getId());
					patientDao.addPatientResultForQuery(queryResult);
				}
			} 
		} else {
			cached = true;
			//update the lastReadDate of each patient cache hit
			for(PatientDTO cachedPatient : patientMatches) {
				patientDao.update(cachedPatient);
				
				PatientQueryResultDTO queryResult = new PatientQueryResultDTO();
				queryResult.setPatientId(cachedPatient.getId());
				queryResult.setQueryOrgId(org.getId());
				patientDao.addPatientResultForQuery(queryResult);
			}
		}
		
		QueryDTO currQuery = queryDao.getById(query.getId());
		for(QueryOrganizationDTO orgStatus : currQuery.getOrgStatuses()) {
			if(orgStatus.getOrgId().longValue() == org.getId().longValue()) {
				orgStatus.setStatus(QueryStatus.COMPLETE.name());
				orgStatus.setEndDate(new Date());
				orgStatus.setFromCache(new Boolean(cached));
				queryDao.update(currQuery);		
			}
		}
	}

	public QueryDTO getQuery() {
		return query;
	}

	public void setQuery(QueryDTO query) {
		this.query = query;
	}

	public OrganizationDTO getOrg() {
		return org;
	}

	public void setOrg(OrganizationDTO org) {
		this.org = org;
	}

	public QueryDAO getQueryDao() {
		return queryDao;
	}

	public void setQueryDao(QueryDAO queryDao) {
		this.queryDao = queryDao;
	}

	public PatientDAO getPatientDao() {
		return patientDao;
	}

	public void setPatientDao(PatientDAO patientDao) {
		this.patientDao = patientDao;
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
}