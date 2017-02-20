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
import gov.ca.emsa.pulse.broker.dto.LocationEndpointDTO;
import gov.ca.emsa.pulse.broker.dto.PatientDTO;
import gov.ca.emsa.pulse.broker.dto.PatientLocationMapDTO;
import gov.ca.emsa.pulse.broker.manager.AlternateCareFacilityManager;
import gov.ca.emsa.pulse.broker.manager.DocumentManager;
import gov.ca.emsa.pulse.broker.manager.PatientManager;
import gov.ca.emsa.pulse.broker.saml.SAMLInput;
import gov.ca.emsa.pulse.common.domain.QueryLocationStatus;

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
	public void queryForDocuments(CommonUser user, SAMLInput samlInput, PatientLocationMapDTO dto) {
		DocumentQueryService service = getDocumentQueryService();
		service.setSamlInput(samlInput);
		service.setPatientLocationMap(dto);
		service.setLocation(dto.getLocation());
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
	public void queryForDocumentContents(CommonUser user, SAMLInput samlInput, LocationDTO location, List<DocumentDTO> docsFromLocation, PatientLocationMapDTO dto) {
		boolean querySuccess = true;
		LocationEndpointDTO endpointToQuery = null;
		if(location.getEndpoints() != null) {
			for(LocationEndpointDTO endpoint : location.getEndpoints()) {
				if(endpoint.getEndpointType() != null && 
						endpoint.getEndpointType().getCode().equalsIgnoreCase(EndpointTypeEnum.DOCUMENT_RETRIEVE.getCode()) && 
						endpoint.getEndpointStatus() != null && 
						endpoint.getEndpointStatus().getName().equalsIgnoreCase(EndpointStatusEnum.ACTIVE.getName())) {
						endpointToQuery = endpoint;
					}
			}
		}
		
		if(endpointToQuery == null) {
			logger.error("The location " + location.getName() + " does not have an active document retrieval endpoint.");
			querySuccess = false;
		} else {
			Adapter adapter = adapterFactory.getAdapter(endpointToQuery);
			if(adapter != null) {
				logger.info("Starting query to endpoint with external id '" + endpointToQuery.getExternalId() + "' for document contents.");
				try {
					for(DocumentDTO doc : docsFromLocation) {
						doc.setStatus(QueryLocationStatus.Active);
						docDao.update(doc);
					}
					adapter.retrieveDocumentsContents(user, endpointToQuery, docsFromLocation, samlInput, dto);
				} catch(Exception ex) {
					logger.error("Exception thrown in adapter " + adapter.getClass(), ex);
					querySuccess = false;
				}
			}
			logger.info("Completed query to endpoint with external id '" + endpointToQuery.getEndpointStatus() + "' for contents of " + docsFromLocation.size() + " documents.");
		}
		
		if(querySuccess) {
			//store the returned document contents
			for(DocumentDTO doc : docsFromLocation) {
				if(doc.getContents() != null && doc.getContents().length > 0) {
					doc.setStatus(QueryLocationStatus.Successful);
					docDao.update(doc);
				}
			}
		} else {
			for(DocumentDTO doc : docsFromLocation) {
				if(doc.getContents() != null && doc.getContents().length > 0) {
					doc.setStatus(QueryLocationStatus.Successful);
					docDao.update(doc);
				} else {
					doc.setStatus(QueryLocationStatus.Failed);
					docDao.update(doc);
				}
			}
		}
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
		PatientLocationMapDTO patientLocationMap = patientDao.getPatientLocationMapById(cachedDoc.getPatientLocationMapId());
		PatientDTO patient = patientDao.getById(patientLocationMap.getPatientId());
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
			queryForDocumentContents(user, samlInput, patientLocationMap.getLocation(), docsToGet, patientLocationMap);
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
}
