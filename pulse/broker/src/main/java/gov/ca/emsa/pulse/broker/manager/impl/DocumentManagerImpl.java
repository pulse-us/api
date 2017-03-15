package gov.ca.emsa.pulse.broker.manager.impl;

import gov.ca.emsa.pulse.auth.user.CommonUser;
import gov.ca.emsa.pulse.broker.adapter.Adapter;
import gov.ca.emsa.pulse.broker.adapter.AdapterFactory;
import gov.ca.emsa.pulse.broker.dao.DocumentDAO;
import gov.ca.emsa.pulse.broker.dao.PatientDAO;
import gov.ca.emsa.pulse.broker.domain.EndpointStatusEnum;
import gov.ca.emsa.pulse.broker.domain.EndpointTypeEnum;
import gov.ca.emsa.pulse.broker.dto.DocumentDTO;
import gov.ca.emsa.pulse.broker.dto.LocationDTO;
import gov.ca.emsa.pulse.broker.dto.EndpointDTO;
import gov.ca.emsa.pulse.broker.dto.PatientDTO;
import gov.ca.emsa.pulse.broker.dto.PatientEndpointMapDTO;
import gov.ca.emsa.pulse.broker.manager.AlternateCareFacilityManager;
import gov.ca.emsa.pulse.broker.manager.DocumentManager;
import gov.ca.emsa.pulse.broker.manager.PatientManager;
import gov.ca.emsa.pulse.broker.saml.SAMLInput;
import gov.ca.emsa.pulse.common.domain.QueryEndpointStatus;

import java.sql.SQLException;
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
	public void queryForDocuments(CommonUser user, SAMLInput samlInput, PatientEndpointMapDTO dto) {
		DocumentQueryService service = getDocumentQueryService();
		service.setSamlInput(samlInput);
		service.setPatientLocationMap(dto);
		service.setEndpoint(dto.getEndpoint());
		service.setUser(user);
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
	public void queryForDocumentContents(CommonUser user, SAMLInput samlInput, EndpointDTO endpoint, List<DocumentDTO> docsFromEndpoints, PatientEndpointMapDTO patientEndpointMap) {
		DocumentRetrievalService service = getDocumentRetrievalService();
		service.setSamlInput(samlInput);
		service.setEndpoint(endpoint);
		service.setPatientEndpointMap(patientEndpointMap);
		service.setDocuments(docsFromEndpoints);
		service.setUser(user);
		pool.execute(service);
	}
	
	@Override
	public String getDocumentById(CommonUser user, SAMLInput samlInput, Long documentId) throws SQLException {
		String docContents = "";		
		
		DocumentDTO cachedDoc = docDao.getById(documentId);
		if(cachedDoc == null) {
			logger.error("Could not find the document with id " + documentId);
			return null;
		}
		
		//update patient last read time when document is cached or viewed
		PatientEndpointMapDTO patientEndpointMap = patientDao.getPatientEndpointMapById(cachedDoc.getPatientEndpointMapId());
		PatientDTO patient = patientDao.getById(patientEndpointMap.getPatientId());
		patient.setLastReadDate(new Date());
		patientManager.update(patient);
		if(patient.getAcf() != null) {
			patient.getAcf().setLastReadDate(new Date());
			acfManager.updateLastModifiedDate(patient.getAcf().getId());
		}
		
		
		if(cachedDoc.getContents() != null && cachedDoc.getContents().length > 0) {
			docContents = new String(cachedDoc.getContents());
		} else {
			List<DocumentDTO> docsToGet = new ArrayList<DocumentDTO>();
			docsToGet.add(cachedDoc);
			queryForDocumentContents(user, samlInput, patientEndpointMap.getEndpoint(), docsToGet, patientEndpointMap);
			byte[] retrievedContents = docsToGet.get(0).getContents();
			docContents = retrievedContents == null ? "" : new String(retrievedContents);
		}
		return docContents;
	}
	
	@Override
	public DocumentDTO getDocumentObjById(Long documentId) throws SQLException {		
		DocumentDTO doc = docDao.getById(documentId);
		
		return doc;
	}
	
	@Lookup
	public DocumentQueryService getDocumentQueryService(){
		//spring will override this method
		return null;
	}
	
	@Lookup
	public DocumentRetrievalService getDocumentRetrievalService() {
		//spring will override this method
		return null;
	}
}
