package gov.ca.emsa.pulse.broker.manager.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
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
	
	@Override
	@Transactional
	public void run() {
		System.out.println("Query with id " + query.getId());		
		String queryUrl = "";
		if(!StringUtils.isEmpty(toSearch.getFirstName())) {
			queryUrl += "?firstName=" + toSearch.getFirstName();
		}
		if(!StringUtils.isEmpty(toSearch.getLastName())) {
			if(queryUrl.contains("?")) {
				queryUrl += "&";
			} else {
				queryUrl += "?";
			}
			queryUrl += "lastName=" + toSearch.getLastName();
		}
		
		//look for cache hits for this organization/patientID combo			
		String url = org.getEndpointUrl() + "/patients" + queryUrl;
		toSearch.setPulsePatientId(url);
		toSearch.setOrganization(org);
		List<PatientDTO> patientMatches = patientDao.getByPatientIdAndOrg(toSearch);
		
		boolean cached = false;
		//if no cache hit
		if(patientMatches == null || patientMatches.size() == 0) {
			//query this organization directly for patient matches
			RestTemplate restTemplate = new RestTemplate();
			Patient[] searchResults = restTemplate.getForObject(url, Patient[].class);
			
			//cache the patients returned so we can 
			//pull them out of the cache again
			if(searchResults != null && searchResults.length > 0) {
				for(Patient patient : searchResults) {
					PatientDTO toCache = DomainToDtoConverter.convert(patient);
					toCache.setPulsePatientId(url);
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

}
