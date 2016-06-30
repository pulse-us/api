package gov.ca.emsa.pulse.broker.manager.impl;

import java.util.Date;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import gov.ca.emsa.pulse.broker.adapter.Adapter;
import gov.ca.emsa.pulse.broker.adapter.AdapterFactory;
import gov.ca.emsa.pulse.broker.domain.Document;
import gov.ca.emsa.pulse.broker.dto.DocumentDTO;
import gov.ca.emsa.pulse.broker.dto.DomainToDtoConverter;
import gov.ca.emsa.pulse.broker.dto.OrganizationDTO;
import gov.ca.emsa.pulse.broker.dto.PatientDTO;
import gov.ca.emsa.pulse.broker.dto.PatientOrganizationMapDTO;
import gov.ca.emsa.pulse.broker.dto.QueryStatus;
import gov.ca.emsa.pulse.broker.manager.DocumentManager;
import gov.ca.emsa.pulse.broker.manager.PatientManager;
import gov.ca.emsa.pulse.broker.manager.QueryManager;

@Component
public class DocumentQueryService implements Runnable {
	private static final Logger logger = LogManager.getLogger(DocumentQueryService.class);

	private PatientOrganizationMapDTO patientOrgMap;
	private OrganizationDTO org;
	@Autowired private QueryManager queryManager;
	@Autowired private PatientManager patientManager;
	@Autowired private DocumentManager docManager;
	@Autowired private AdapterFactory adapterFactory;
	private PatientDTO toSearch;
	private String samlMessage;
	
	@Override
	@Transactional
	public void run() {
		//query this organization directly for 
		Document[] searchResults = null;
		Adapter adapter = adapterFactory.getAdapter(org);
		if(adapter != null) {
			logger.info("Starting query to " + org.getAdapter() + " for documents.");
			try {
				searchResults = adapter.queryDocuments(org, patientOrgMap, samlMessage);
			} catch(Exception ex) {
				logger.error("Exception thrown in adapter " + adapter.getClass(), ex);
				patientOrgMap.setDocumentsQuerySuccess(Boolean.FALSE);
			}
		}
		//store the returned document info
		if(searchResults != null && searchResults.length > 0) {
			for(Document doc : searchResults) {
				DocumentDTO toSave = DomainToDtoConverter.convert(doc);
				toSave.setPatientOrgMapId(patientOrgMap.getId());
				//save document
				docManager.create(toSave);
			}
			patientOrgMap.setDocumentsQuerySuccess(Boolean.TRUE);
		} 
		
		patientOrgMap.setDocumentsQueryEnd(new Date());
		patientOrgMap.setDocumentsQueryStatus(QueryStatus.COMPLETE.name());
		//update patient org map
		patientManager.updateOrganizationMap(patientOrgMap);
		
		logger.info("Completed query to " + org.getAdapter() + " for documents for patient " + patientOrgMap.getPatientId());
	}

	public OrganizationDTO getOrg() {
		return org;
	}

	public void setOrg(OrganizationDTO org) {
		this.org = org;
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

	public PatientOrganizationMapDTO getPatientOrgMap() {
		return patientOrgMap;
	}

	public void setPatientOrgMap(PatientOrganizationMapDTO patientOrgMap) {
		this.patientOrgMap = patientOrgMap;
	}

	public PatientDTO getToSearch() {
		return toSearch;
	}

	public void setToSearch(PatientDTO toSearch) {
		this.toSearch = toSearch;
	}
}