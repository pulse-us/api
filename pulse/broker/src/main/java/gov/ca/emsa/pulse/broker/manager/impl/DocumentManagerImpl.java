package gov.ca.emsa.pulse.broker.manager.impl;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.opensaml.common.binding.SAMLMessageContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import gov.ca.emsa.pulse.broker.dao.DocumentDAO;
import gov.ca.emsa.pulse.broker.dao.OrganizationDAO;
import gov.ca.emsa.pulse.broker.dao.PatientDAO;
import gov.ca.emsa.pulse.broker.dto.DocumentDTO;
import gov.ca.emsa.pulse.broker.dto.OrganizationDTO;
import gov.ca.emsa.pulse.broker.dto.PatientDTO;
import gov.ca.emsa.pulse.broker.dto.PatientOrganizationMapDTO;
import gov.ca.emsa.pulse.broker.manager.DocumentManager;
import gov.ca.emsa.pulse.broker.manager.PatientManager;

@Service
public class DocumentManagerImpl implements DocumentManager {
	@Autowired private PatientManager patientManager;
	@Autowired private DocumentDAO docDao;
	@Autowired private PatientDAO patientDao;
	@Autowired private OrganizationDAO orgDao;
	private final ExecutorService pool;

	public DocumentManagerImpl() {
		pool = Executors.newFixedThreadPool(100);
	}
	
	@Override
	@Transactional
	public DocumentDTO create(DocumentDTO toCreate) {
		return docDao.create(toCreate);
	}
	
	@Override
	@Transactional
	public void queryForDocuments(String samlMessage, PatientOrganizationMapDTO dto) {
		DocumentQueryService service = getDocumentQueryService();
		service.setSamlMessage(samlMessage);
		service.setPatientOrgMap(dto);
		service.setOrg(dto.getOrg());
		pool.execute(service);
	}
	
	@Override
	public List<DocumentDTO> getDocumentsForPatient(Long patientId) {
		//update the last read time for the patient
		PatientDTO patient = patientManager.getPatientById(patientId);
		
		//get the documents
		return docDao.getByPatientId(patientId);
	}
	
	@Override
	public String getDocumentById(String samlMessage, Long documentId) {
		String docContents = "";
		DocumentDTO cachedDoc = docDao.getById(documentId);
		if(cachedDoc != null && cachedDoc.getContents() != null && cachedDoc.getContents().length > 0) {
			docContents = new String(cachedDoc.getContents());
		} else if(cachedDoc != null) {
			PatientOrganizationMapDTO patientOrgMap = patientDao.getPatientOrgMapById(cachedDoc.getPatientOrgMapId());
			if(patientOrgMap != null && patientOrgMap.getOrg() != null) {
				OrganizationDTO org = patientOrgMap.getOrg();
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
	
	@Lookup
	public DocumentQueryService getDocumentQueryService(){
		//spring will override this method
		return null;
	}
}
