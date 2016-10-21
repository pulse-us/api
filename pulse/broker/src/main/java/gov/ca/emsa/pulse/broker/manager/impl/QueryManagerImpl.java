package gov.ca.emsa.pulse.broker.manager.impl;

import gov.ca.emsa.pulse.auth.user.CommonUser;
import gov.ca.emsa.pulse.broker.dao.PatientRecordDAO;
import gov.ca.emsa.pulse.broker.dao.QueryDAO;
import gov.ca.emsa.pulse.broker.dto.OrganizationDTO;
import gov.ca.emsa.pulse.broker.dto.PatientRecordDTO;
import gov.ca.emsa.pulse.broker.dto.QueryDTO;
import gov.ca.emsa.pulse.broker.dto.QueryOrganizationDTO;
import gov.ca.emsa.pulse.broker.manager.OrganizationManager;
import gov.ca.emsa.pulse.broker.manager.QueryManager;
import gov.ca.emsa.pulse.broker.saml.SAMLInput;
import gov.ca.emsa.pulse.common.domain.PatientSearch;
import gov.ca.emsa.pulse.common.domain.QueryStatus;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;

@Service
public class QueryManagerImpl implements QueryManager, ApplicationContextAware {
	private static final Logger logger = LogManager.getLogger(QueryManagerImpl.class);

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
		return result;
	}

	@Override
	@Transactional
	public List<QueryDTO> getAllQueriesForUser(String userKey) {
		List<QueryDTO> results = queryDao.findAllForUser(userKey);
		return results;
	}

	@Override
	@Transactional
	public List<QueryDTO> getActiveQueriesForUser(String userKey) {
		List<QueryDTO> results = queryDao.findAllForUserWithStatus(userKey, QueryStatus.ACTIVE.name());
		return results;
	}

	@Override
	@Transactional
	public String getQueryStatus(Long queryId) {
		QueryDTO query = queryDao.getById(queryId);
		return query.getStatus();
	}

	@Override
	@Transactional
	public QueryDTO updateQuery(QueryDTO toUpdate) {
		return queryDao.update(toUpdate);
	}

	@Override
	@Transactional
	public QueryDTO updateQueryStatusFromOrganizations(Long queryId) {
		QueryDTO query = getById(queryId);
		//see if the entire query is complete
		Boolean isStillActive = queryDao.hasActiveOrganizations(queryId);
		if(!isStillActive) {
			logger.info("Setting query " + queryId + " to COMPLETE.");
			query.setStatus(QueryStatus.COMPLETE.name());
			query.setLastReadDate(new Date());
			query = queryDao.update(query);
		}
		return query;
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
		return updated;
	}

	@Override
	@Transactional
	public QueryDTO createQuery(QueryDTO toCreate) {
		return queryDao.create(toCreate);
	}

	@Override
	public QueryDTO queryForPatientRecords(SAMLInput samlInput, PatientSearch toSearch, QueryDTO query, CommonUser user)
			throws JsonProcessingException {

		//get the list of organizations
		List<OrganizationDTO> orgsToQuery = orgManager.getAll();
		if(orgsToQuery != null && orgsToQuery.size() > 0) {
			for(QueryOrganizationDTO queryOrg : query.getOrgStatuses()) {
				PatientQueryService service = getPatientQueryService();
				service.setSamlInput(samlInput);
				service.setToSearch(toSearch);
				service.setQueryOrg(queryOrg);
				pool.execute(service);
			}
		}
		return query;
	}

	@Override
	@Transactional
	public PatientRecordDTO getPatientRecordById(Long patientRecordId) {
		PatientRecordDTO prDto = patientRecordDao.getById(patientRecordId);
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
	public void cleanupCache(Date oldestAllowedQuery) {
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
