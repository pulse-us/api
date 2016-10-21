package gov.ca.emsa.pulse.broker.manager.impl;

import gov.ca.emsa.pulse.broker.adapter.Adapter;
import gov.ca.emsa.pulse.broker.adapter.AdapterFactory;
import gov.ca.emsa.pulse.broker.dao.DocumentDAO;
import gov.ca.emsa.pulse.broker.dao.PatientDAO;
import gov.ca.emsa.pulse.broker.dto.DocumentDTO;
import gov.ca.emsa.pulse.broker.dto.OrganizationDTO;
import gov.ca.emsa.pulse.broker.dto.PatientDTO;
import gov.ca.emsa.pulse.broker.dto.PatientOrganizationMapDTO;
import gov.ca.emsa.pulse.broker.manager.AlternateCareFacilityManager;
import gov.ca.emsa.pulse.broker.manager.DocumentManager;
import gov.ca.emsa.pulse.broker.manager.PatientManager;
import gov.ca.emsa.pulse.broker.saml.SAMLInput;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DocumentManagerImpl implements DocumentManager {
	private static final Logger logger = LogManager.getLogger(DocumentManagerImpl.class);

	@Autowired private PatientManager patientManager;
	@Autowired private AlternateCareFacilityManager acfManager;
	@Autowired private DocumentDAO docDao;
	@Autowired private PatientDAO patientDao;
	@Autowired private AdapterFactory adapterFactory;

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
	public void queryForDocuments(SAMLInput samlInput, PatientOrganizationMapDTO dto) {
		DocumentQueryService service = getDocumentQueryService();
		service.setSamlInput(samlInput);
		service.setPatientOrgMap(dto);
		service.setOrg(dto.getOrg());
		pool.execute(service);
	}
	
	@Override
	public List<DocumentDTO> getDocumentsForPatient(Long patientId) {
		return docDao.getByPatientId(patientId);
	}
	
	//for the time being, we want this method to be synchronous 
	//we are using it to get just one document at a time but it could accept
	//multiple documents from the same organization if we want to do that in the future
	@Override
	@Transactional
	public void queryForDocumentContents(SAMLInput samlInput, OrganizationDTO org, List<DocumentDTO> docsFromOrg) {
		boolean querySuccess = true;
		Adapter adapter = adapterFactory.getAdapter(org);
		if(adapter != null) {
			logger.info("Starting query to " + org.getAdapter() + " for document contents.");
			try {
				adapter.retrieveDocumentsContents(org, docsFromOrg, samlInput);
			} catch(Exception ex) {
				logger.error("Exception thrown in adapter " + adapter.getClass(), ex);
				querySuccess = false;
			}
		}
		
		if(querySuccess) {
			//store the returned document contents
			for(DocumentDTO doc : docsFromOrg) {
				if(doc.getContents() != null && doc.getContents().length > 0) {
					docDao.update(doc);
				}
			}
			
			logger.info("Completed query to " + org.getAdapter() + " for contents of " + docsFromOrg.size() + " documents.");
		}
	}
	
	@Override
	public String getDocumentById(SAMLInput samlInput, Long documentId) {
		String docContents = "";		
		
		DocumentDTO cachedDoc = docDao.getById(documentId);
		if(cachedDoc == null) {
			logger.error("Could not find the document with id " + documentId);
			return null;
		}
		
		//update patient last read time when document is cached or viewed
		PatientOrganizationMapDTO patientOrgMap = patientDao.getPatientOrgMapById(cachedDoc.getPatientOrgMapId());
		PatientDTO patient = patientDao.getById(patientOrgMap.getPatientId());
		patient.setLastReadDate(new Date());
		patientManager.update(patient);
		if(patient.getAcf() != null) {
			patient.getAcf().setLastReadDate(new Date());
			acfManager.update(patient.getAcf());
		}
		
		
		if(cachedDoc.getContents() != null && cachedDoc.getContents().length > 0) {
			docContents = new String(cachedDoc.getContents());
		} else {
			List<DocumentDTO> docsToGet = new ArrayList<DocumentDTO>();
			docsToGet.add(cachedDoc);
			queryForDocumentContents(samlInput, patientOrgMap.getOrg(), docsToGet);
			byte[] retrievedContents = docsToGet.get(0).getContents();
			docContents = retrievedContents == null ? "" : new String(retrievedContents);
		}
		return docContents;
	}
	
	@Lookup
	public DocumentQueryService getDocumentQueryService(){
		//spring will override this method
		return null;
	}
}
