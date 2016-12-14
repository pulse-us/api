package gov.ca.emsa.pulse.broker.manager.impl;

import gov.ca.emsa.pulse.auth.user.CommonUser;
import gov.ca.emsa.pulse.broker.dao.PatientRecordDAO;
import gov.ca.emsa.pulse.broker.dao.QueryDAO;
import gov.ca.emsa.pulse.broker.dto.LocationDTO;
import gov.ca.emsa.pulse.broker.dto.PatientRecordDTO;
import gov.ca.emsa.pulse.broker.dto.QueryDTO;
import gov.ca.emsa.pulse.broker.dto.QueryLocationMapDTO;
import gov.ca.emsa.pulse.broker.manager.LocationManager;
import gov.ca.emsa.pulse.broker.manager.QueryManager;
import gov.ca.emsa.pulse.broker.saml.SAMLInput;
import gov.ca.emsa.pulse.common.domain.PatientSearch;
import gov.ca.emsa.pulse.common.domain.QueryStatus;

import java.util.ArrayList;
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
import gov.ca.emsa.pulse.common.domain.QueryLocationStatus;

@Service
public class QueryManagerImpl implements QueryManager, ApplicationContextAware {
	private static final Logger logger = LogManager.getLogger(QueryManagerImpl.class);

	@Autowired QueryDAO queryDao;
	@Autowired PatientRecordDAO patientRecordDao;
	@Autowired private LocationManager orgManager;
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
	public List<QueryDTO> getOpenQueriesForUser(String userKey) {
		List<QueryStatus> openStatuses = new ArrayList<QueryStatus>();
		openStatuses.add(QueryStatus.Active);
		openStatuses.add(QueryStatus.Complete);
		List<QueryDTO> results = queryDao.findAllForUserWithStatus(userKey, openStatuses);
		return results;
	}

	@Override
	@Transactional
	public String getQueryStatus(Long queryId) {
		QueryDTO query = queryDao.getById(queryId);
		return query.getStatus().name();
	}

	@Override
	@Transactional
	public QueryDTO updateQuery(QueryDTO toUpdate) {
		return queryDao.update(toUpdate);
	}

	@Override
	@Transactional
	public QueryDTO updateQueryStatusFromLocations(Long queryId) {
		QueryDTO query = getById(queryId);
		//if the query has not already been closed/deleted, update the status
		if(query.getStatus() != QueryStatus.Closed) {
			//see if the entire query is complete
			Boolean isStillActive = queryDao.hasActiveLocations(queryId);
			if(!isStillActive) {
				logger.info("Setting query " + queryId + " to COMPLETE.");
				query.setStatus(QueryStatus.Complete);
				query.setLastReadDate(new Date());
				query = queryDao.update(query);
			}
		}
		return query;
	}
	
	@Override
	@Transactional
	public QueryDTO cancelQueryToLocation(Long queryId, Long locationId) {
		QueryLocationMapDTO toUpdate = queryDao.getQueryLocationMapByQueryAndLocation(queryId, locationId);
		if(toUpdate == null) {
			logger.error("Could not find query organization for query ID " + queryId + " and location ID " + locationId);
			return null;
		}
		toUpdate.setStatus(QueryLocationStatus.Cancelled);
		queryDao.updateQueryLocationMap(toUpdate);
		
		return updateQueryStatusFromLocations(queryId);
	}
	
	@Override
	@Transactional
	public void close(Long queryId) {
		queryDao.close(queryId);
	}

	@Override
	@Transactional
	public QueryLocationMapDTO createOrUpdateQueryLocation(QueryLocationMapDTO toUpdate) {
		QueryLocationMapDTO updated = null;
		if(toUpdate.getId() == null) {
			updated = queryDao.createQueryLocationMap(toUpdate);
		} else {
			updated = queryDao.updateQueryLocationMap(toUpdate);
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
		List<LocationDTO> orgsToQuery = orgManager.getAll();
		if(orgsToQuery != null && orgsToQuery.size() > 0) {
			for(QueryLocationMapDTO queryOrg : query.getLocationStatuses()) {
				PatientQueryService service = getPatientQueryService();
				service.setSamlInput(samlInput);
				service.setToSearch(toSearch);
				service.setQueryLocation(queryOrg);
				service.setUser(user);
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
		PatientRecordDTO result = record;
		Long queryLocationMapId = record.getQueryLocationId();
		QueryLocationMapDTO queryLoc = queryDao.getQueryLocationById(queryLocationMapId);
		if(queryLoc != null && queryLoc.getStatus() != null && 
				queryLoc.getStatus() != QueryLocationStatus.Cancelled) {
			result = patientRecordDao.create(record);
		}
		return result;
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
