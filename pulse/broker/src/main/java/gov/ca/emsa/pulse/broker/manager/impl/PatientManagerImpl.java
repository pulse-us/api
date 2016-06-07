package gov.ca.emsa.pulse.broker.manager.impl;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;

import gov.ca.emsa.pulse.broker.dao.PatientDAO;
import gov.ca.emsa.pulse.broker.dao.QueryDAO;
import gov.ca.emsa.pulse.broker.domain.Patient;
import gov.ca.emsa.pulse.broker.dto.OrganizationDTO;
import gov.ca.emsa.pulse.broker.dto.PatientDTO;
import gov.ca.emsa.pulse.broker.dto.QueryDTO;
import gov.ca.emsa.pulse.broker.dto.QueryOrganizationDTO;
import gov.ca.emsa.pulse.broker.dto.QueryStatus;
import gov.ca.emsa.pulse.broker.manager.OrganizationManager;
import gov.ca.emsa.pulse.broker.manager.PatientManager;
import gov.ca.emsa.pulse.broker.manager.QueryManager;

@Service
public class PatientManagerImpl implements PatientManager, ApplicationContextAware {
	@Autowired Environment env;
	@Autowired OrganizationManager orgManager;
	@Autowired PatientDAO patientDao;
	@Autowired QueryManager queryManager;
	private ApplicationContext context;
	private final ExecutorService pool;
	
	public PatientManagerImpl() {
		pool = Executors.newFixedThreadPool(100);
	}
	
	@Override
	public PatientDTO getPatientById(Long patientId) {
		return patientDao.getById(patientId);
	}
	
	@Override
	public QueryDTO queryPatients(String firstName, String lastName) throws JsonProcessingException {
		PatientDTO toSearch = new PatientDTO();
		toSearch.setFirstName(firstName);
		toSearch.setLastName(lastName);
		
		Patient queryTerms = new Patient();
		queryTerms.setFirstName(firstName);
		queryTerms.setLastName(lastName);
		String queryTermsJson = JSONUtils.toJSON(queryTerms);
		
		QueryDTO query = new QueryDTO();
		query.setUserToken("TBD");
		query.setTerms(queryTermsJson);
		query.setStatus(QueryStatus.ACTIVE.name());
		query = queryManager.createQuery(query);
		
		//get the list of organizations
		List<OrganizationDTO> orgsToQuery = orgManager.getAll();
		for(OrganizationDTO org : orgsToQuery) {
			QueryOrganizationDTO queryOrg = new QueryOrganizationDTO();
			queryOrg.setOrgId(org.getId());
			queryOrg.setQueryId(query.getId());
			queryOrg.setStatus(QueryStatus.ACTIVE.name());
			query.getOrgStatuses().add(queryOrg);
			query = queryManager.updateQuery(query);
			
			PatientQueryService service = (PatientQueryService) context.getBean("patientQueryService");
			service.setToSearch(toSearch);
			service.setQuery(query);
			service.setOrg(org);
			pool.execute(service);
		}
		
		return queryManager.getQueryStatusDetails(query.getId());
	}
	
	@Override
	public List<PatientDTO> getPatientsByQuery(Long queryId) {
		List<PatientDTO> results = patientDao.getPatientResultsForQuery(queryId);
		return results;
	}
	
	@Override
	@Transactional
	public void cleanupPatientCache(Date oldestAllowedPatient) {
		patientDao.deleteItemsOlderThan(oldestAllowedPatient);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.context = applicationContext;
	}
}
