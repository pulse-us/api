package gov.ca.emsa.pulse.broker.manager.impl;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;

import gov.ca.emsa.pulse.broker.dao.PatientRecordDAO;
import gov.ca.emsa.pulse.broker.dao.QueryDAO;
import gov.ca.emsa.pulse.broker.domain.Patient;
import gov.ca.emsa.pulse.broker.domain.User;
import gov.ca.emsa.pulse.broker.dto.OrganizationDTO;
import gov.ca.emsa.pulse.broker.dto.PatientRecordDTO;
import gov.ca.emsa.pulse.broker.dto.QueryDTO;
import gov.ca.emsa.pulse.broker.dto.QueryOrganizationDTO;
import gov.ca.emsa.pulse.broker.dto.QueryStatus;
import gov.ca.emsa.pulse.broker.manager.OrganizationManager;
import gov.ca.emsa.pulse.broker.manager.QueryManager;

@Service
public class QueryManagerImpl implements QueryManager, ApplicationContextAware {
	@Autowired QueryDAO queryDao;
	@Autowired PatientRecordDAO patientRecordDao;
	@Autowired private OrganizationManager orgManager;
	private ApplicationContext context;
	private final ExecutorService pool;
	
	public QueryManagerImpl() {
		pool = Executors.newFixedThreadPool(100);
	}
	
	@Override
	@Transactional
	public QueryDTO getById(Long id) {
		QueryDTO result = queryDao.getById(id);
		if(result != null) {
			result.setLastReadDate(new Date());
			result = queryDao.update(result);
		}
		return result;
	}
	
	@Override
	@Transactional
	public List<QueryDTO> getAllQueriesForUser(String userKey) {
		List<QueryDTO> results = queryDao.findAllForUser(userKey);
		for(QueryDTO result : results) {
			result.setLastReadDate(new Date());
			result = queryDao.update(result);
		}
		return results;
	}

	@Override
	@Transactional
	public List<QueryDTO> getActiveQueriesForUser(String userKey) {
		List<QueryDTO> results = queryDao.findAllForUserWithStatus(userKey, QueryStatus.ACTIVE.name());
		for(QueryDTO result : results) {
			result.setLastReadDate(new Date());
			result = queryDao.update(result);
		}
		return results;
	}
	
	@Override
	@Transactional
	public String getQueryStatus(Long queryId) {
		QueryDTO query = queryDao.getById(queryId);
		query.setLastReadDate(new Date());
		queryDao.update(query);
		
		return query.getStatus();
	}
	
	@Override
	@Transactional
	public QueryDTO updateQuery(QueryDTO toUpdate) {
		toUpdate.setLastReadDate(new Date());
		return queryDao.update(toUpdate);
	}
	
	@Override
	@Transactional
	public void delete(Long queryId) {
		queryDao.delete(queryId);
	}
	
	@Override
	@Transactional
	public QueryOrganizationDTO createOrUpdateQueryOrganization(QueryOrganizationDTO toUpdate) {		
		QueryOrganizationDTO updated = null;
		if(toUpdate.getId() == null) {
			updated = queryDao.createQueryOrganization(toUpdate);
		} else {
			updated = queryDao.updateQueryOrganization(toUpdate);
		}
		
		QueryDTO query = queryDao.getById(toUpdate.getQueryId());
		query.setLastReadDate(new Date());
		queryDao.update(query);
		
		return updated;
	}
	
	@Override
	@Transactional
	public QueryDTO createQuery(QueryDTO toCreate) {
		return queryDao.create(toCreate);
	}

	@Override
	public QueryDTO queryForPatientRecords(String samlMessage, PatientRecordDTO searchParams, User user)
			throws JsonProcessingException {
		Patient queryTerms = new Patient();
		queryTerms.setFirstName(searchParams.getFirstName());
		queryTerms.setLastName(searchParams.getLastName());
		queryTerms.setDateOfBirth(searchParams.getDateOfBirth());
		queryTerms.setSsn(searchParams.getSsn());
		queryTerms.setGender(searchParams.getGender());
		if(searchParams.getAddress() != null) {
			queryTerms.getAddress().setZipcode(searchParams.getAddress().getZipcode());
		}
		String queryTermsJson = JSONUtils.toJSON(queryTerms);
		
		QueryDTO query = new QueryDTO();
		query.setUserId(user.getSubjectName());
		query.setTerms(queryTermsJson);
		query.setStatus(QueryStatus.ACTIVE.name());
		query = createQuery(query);
		
		//get the list of organizations
		List<OrganizationDTO> orgsToQuery = orgManager.getAll();
		for(OrganizationDTO org : orgsToQuery) {
			QueryOrganizationDTO queryOrg = new QueryOrganizationDTO();
			queryOrg.setOrgId(org.getId());
			queryOrg.setQueryId(query.getId());
			queryOrg.setStatus(QueryStatus.ACTIVE.name());
			queryOrg = createOrUpdateQueryOrganization(queryOrg);
			query.getOrgStatuses().add(queryOrg);
			
			PatientQueryService service = getPatientQueryService();
			service.setSamlMessage(samlMessage);
			service.setToSearch(searchParams);
			service.setQueryOrg(queryOrg);
			service.setOrg(org);
			pool.execute(service);
		}
		return query;
	}
	
	@Override
	@Transactional
	public PatientRecordDTO getPatientRecordById(Long patientRecordId) {
		PatientRecordDTO prDto = patientRecordDao.getById(patientRecordId);
		
		//update last read date
		Long queryOrgId = prDto.getQueryOrganizationId();
		QueryOrganizationDTO queryOrgDto = queryDao.getQueryOrganizationById(queryOrgId);
		if(queryOrgDto != null && queryOrgDto.getQueryId() != null) {
			QueryDTO query = queryDao.getById(queryOrgDto.getQueryId());
			query.setLastReadDate(new Date());
			queryDao.update(query);
		}
		return prDto;
	}
	
	@Override
	@Transactional
	public PatientRecordDTO addPatientRecord(PatientRecordDTO record) {
		return patientRecordDao.create(record);
	}
	
	@Override
	@Transactional
	public void removePatientRecord(Long prId) {
		patientRecordDao.delete(prId);
	}
	
	@Override
	@Transactional
	public void cleanupQueryCache(Date oldestAllowedQuery) {
		queryDao.deleteItemsOlderThan(oldestAllowedQuery);
	}
	
	@Lookup
	public PatientQueryService getPatientQueryService(){
		//spring will override this method
		return null;
	}
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.context = applicationContext;
	}
}