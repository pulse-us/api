package gov.ca.emsa.pulse.broker.manager.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
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
	public List<DocumentDTO> queryDocumentsForPatient(String samlMessage, PatientDTO patient) throws Exception {
		//look in the cache
		List<DocumentDTO> results = new ArrayList<DocumentDTO>();
		results = docDao.getByPatientId(patient.getId());
		
		if(results == null || results.size() == 0) {
			if(patient.getOrganization() != null) {
				String url = patient.getOrganization().getEndpointUrl() + "/documents";
				// ?patientId=" + patient.getOrgPatientId();
				MultiValueMap<String,String> parameters = new LinkedMultiValueMap<String,String>();
				parameters.add("patientId", patient.getOrgPatientId());
				parameters.add("samlMessage", samlMessage);
				RestTemplate restTemplate = new RestTemplate();
				Document[] searchResults = restTemplate.postForObject(url, parameters, Document[].class);
				
				//cache the patients returned so we can 
				//pull them out of the cache again
				if(searchResults != null && searchResults.length > 0) {
					for(Document doc : searchResults) {
						DocumentDTO toCache = DomainToDtoConverter.convert(doc);
						if(toCache.getPatient() == null || toCache.getPatient().getId() == null) {
							toCache.setPatient(patient);
						}
						DocumentDTO cachedDocument = docDao.create(toCache);
						results.add(cachedDocument);
					}
				} 
			} else {
				throw new Exception("An organization to query must be specified.");
			}
		} else {
			//update the lastReadDate of each patient cache hit
			for(DocumentDTO cacheDoc : results) {
				docDao.update(cacheDoc);
			}
		}
		return results;
	}
	@Override
	public String getDocumentById(String samlMessage, Long documentId) {
		String docContents = "";
		//look in the cache (what is the document id and how does it get created?)
		// if it's not there, query the organization present in the patient object
		DocumentDTO cachedDoc = docDao.getById(documentId);
		if(cachedDoc != null && cachedDoc.getContents() != null && cachedDoc.getContents().length > 0) {
			docContents = new String(cachedDoc.getContents());
		} else if(cachedDoc != null) {
			PatientDTO patient = cachedDoc.getPatient();
			if(patient != null) {
				OrganizationDTO org = patient.getOrganization();
				if(org != null && org.getEndpointUrl() != null) {
					String url = org.getEndpointUrl() + "/document/" + documentId;
					MultiValueMap<String,String> parameters = new LinkedMultiValueMap<String,String>();
					parameters.add("samlMessage", samlMessage);
					RestTemplate restTemplate = new RestTemplate();
					String remoteDocContents = restTemplate.postForObject(url, parameters, String.class);
					if(cachedDoc != null) {
						cachedDoc.setContents(remoteDocContents.getBytes());
						docDao.update(cachedDoc);
					}
					docContents = remoteDocContents;
				}
			}
		}
		return docContents;
	}
	
	@Override
	@Transactional
	public void cleanupDocumentCache(Date oldestAllowedDocument) {
		docDao.deleteItemsOlderThan(oldestAllowedDocument);
	}
}
