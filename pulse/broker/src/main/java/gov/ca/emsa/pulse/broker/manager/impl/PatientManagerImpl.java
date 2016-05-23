package gov.ca.emsa.pulse.broker.manager.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import gov.ca.emsa.pulse.broker.dao.PatientDAO;
import gov.ca.emsa.pulse.broker.domain.Patient;
import gov.ca.emsa.pulse.broker.dto.DomainToDtoConverter;
import gov.ca.emsa.pulse.broker.dto.OrganizationDTO;
import gov.ca.emsa.pulse.broker.dto.PatientDTO;
import gov.ca.emsa.pulse.broker.manager.OrganizationManager;
import gov.ca.emsa.pulse.broker.manager.PatientManager;

@Service
public class PatientManagerImpl implements PatientManager {
	@Autowired Environment env;
	@Autowired OrganizationManager orgManager;
	@Autowired PatientDAO patientDao;
	
	public List<PatientDTO> queryPatients(String firstName, String lastName) {
		PatientDTO toSearch = new PatientDTO();
		toSearch.setFirstName(firstName);
		toSearch.setLastName(lastName);
		
		List<PatientDTO> results = new ArrayList<PatientDTO>();
		
		//get the list of organizations
		List<OrganizationDTO> orgsToQuery = orgManager.getAll();
		for(OrganizationDTO org : orgsToQuery) {
			//look for cache hits for this organization/patientID combo
			OrganizationDTO orgToQuery = new OrganizationDTO();
			orgToQuery.setName("mock/ehealthexchange");
			
			String url = env.getProperty("mockBaseUrl") + "/ehealthexchange/patients";
			if(!StringUtils.isEmpty(firstName)) {
				url += "?firstName=" + firstName;
			}
			if(!StringUtils.isEmpty(lastName)) {
				if(url.contains("?")) {
					url += "&lastName=" + lastName;
				} else {
					url += "?lastName=" + lastName;
				}
			}
			toSearch.setPatientId(url);
			toSearch.setOrganization(org);
			List<PatientDTO> patientMatches = patientDao.getByPatientIdAndOrg(toSearch);
			
			//if no cache hit
			if(patientMatches == null || patientMatches.size() == 0) {
				//query this organization directly for patient matches
				RestTemplate restTemplate = new RestTemplate();
				List<Patient> searchResults = restTemplate.getForObject(url, List.class);
				
				//cache the patients returned so we can 
				//pull them out of the cache again
				if(searchResults != null && searchResults.size() > 0) {
					for(Patient patient : searchResults) {
						PatientDTO toCache = DomainToDtoConverter.convert(patient);
						toCache.setPatientId(url);
						toCache.setOrganization(orgToQuery);
						//TODO: should we really be caching the search results?
						//or only caching the patient(s) that someone selects in the UI?
						PatientDTO cachedPatient = patientDao.create(toCache);
						results.add(cachedPatient);
					}
				} 
			} else {
				results.addAll(patientMatches);
			}
		}
		return results;
	}
	
	@Override
	@Transactional
	public void cleanupPatientCache(Date oldestAllowedPatient) {
		patientDao.deleteItemsOlderThan(oldestAllowedPatient);
	}
}
