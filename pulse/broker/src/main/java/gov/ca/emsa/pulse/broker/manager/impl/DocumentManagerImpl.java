package gov.ca.emsa.pulse.broker.manager.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import gov.ca.emsa.pulse.broker.dao.DocumentDAO;
import gov.ca.emsa.pulse.broker.domain.Document;
import gov.ca.emsa.pulse.broker.dto.DocumentDTO;
import gov.ca.emsa.pulse.broker.dto.DomainToDtoConverter;
import gov.ca.emsa.pulse.broker.dto.OrganizationDTO;
import gov.ca.emsa.pulse.broker.dto.PatientDTO;
import gov.ca.emsa.pulse.broker.manager.DocumentManager;

@Service
public class DocumentManagerImpl implements DocumentManager {
	@Autowired private DocumentDAO docDao;
	@Autowired private Environment env;
	
	@Override
	@Transactional
	public List<DocumentDTO> queryDocumentsForPatient(PatientDTO patient) throws Exception {
		//look in the cache
		List<DocumentDTO> results = new ArrayList<DocumentDTO>();
		results = docDao.getByPatientId(patient.getId());
		
		if(results == null || results.size() == 0) {
			if(patient.getOrganization() != null) {
				String url = patient.getOrganization().getEndpointUrl() + "/documents?patientId=" + patient.getId();
				RestTemplate restTemplate = new RestTemplate();
				Document[] searchResults = restTemplate.getForObject(url, Document[].class);
				
				//cache the patients returned so we can 
				//pull them out of the cache again
				if(searchResults != null && searchResults.length > 0) {
					for(Document doc : searchResults) {
						DocumentDTO toCache = DomainToDtoConverter.convert(doc);
						//TODO: should we really be caching the search results?
						//or only caching the document(s) that someone selects in the UI?
						DocumentDTO cachedDocument = docDao.create(toCache);
						results.add(cachedDocument);
					}
				} 
			} else {
				throw new Exception("An organization to query must be specified.");
			}
		}
		return results;
	}
	
	public String getDocumentById(PatientDTO patient, String documentId) {
		//TODO: look in the cache (what is the document id and how does it get created?)
		// if it's not there, query the organization present in the patient object
		
		OrganizationDTO orgToQuery = new OrganizationDTO();
		orgToQuery.setName("mock/ehealthexchange");
		String url = "http://localhost:8080/mock/mock/ehealthexchange/document/" + documentId;
		RestTemplate restTemplate = new RestTemplate();
		String docContents = restTemplate.getForObject(url, String.class);
		
		//TODO: insert or replace this item in the cache
		return docContents;
	}
	
	@Override
	@Transactional
	public void cleanupDocumentCache(Date oldestAllowedDocument) {
		docDao.deleteItemsOlderThan(oldestAllowedDocument);
	}
}
