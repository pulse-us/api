package gov.ca.emsa.pulse.broker.manager.impl;

import gov.ca.emsa.pulse.auth.user.CommonUser;
import gov.ca.emsa.pulse.broker.adapter.EHealthAdapter;
import gov.ca.emsa.pulse.broker.dao.EndpointDAO;
import gov.ca.emsa.pulse.broker.dao.LocationDAO;
import gov.ca.emsa.pulse.broker.dao.PatientRecordDAO;
import gov.ca.emsa.pulse.broker.dao.QueryDAO;
import gov.ca.emsa.pulse.broker.domain.EndpointTypeEnum;
import gov.ca.emsa.pulse.broker.dto.LocationDTO;
import gov.ca.emsa.pulse.broker.dto.EndpointDTO;
import gov.ca.emsa.pulse.broker.dto.PatientRecordDTO;
import gov.ca.emsa.pulse.broker.dto.QueryDTO;
import gov.ca.emsa.pulse.broker.dto.QueryEndpointMapDTO;
import gov.ca.emsa.pulse.broker.manager.AuditEventManager;
import gov.ca.emsa.pulse.broker.manager.QueryManager;
import gov.ca.emsa.pulse.broker.saml.SAMLInput;
import gov.ca.emsa.pulse.common.domain.PatientSearch;
import gov.ca.emsa.pulse.common.domain.QueryStatus;
import gov.ca.emsa.pulse.service.UserUtil;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
import gov.ca.emsa.pulse.common.domain.QueryEndpointStatus;

@Service
public class QueryManagerImpl implements QueryManager, ApplicationContextAware {
	private static final Logger logger = LogManager.getLogger(QueryManagerImpl.class);

	@Autowired QueryDAO queryDao;
	@Autowired LocationDAO locationDao;
	@Autowired EndpointDAO endpointDao;
	@Autowired PatientRecordDAO patientRecordDao;
	@Autowired AuditEventManager auditManager;
	private ApplicationContext context;
	private final ExecutorService pool;

	public QueryManagerImpl() {
		pool = Executors.newFixedThreadPool(100);
		logger.info("Creating a new QueryManagerImpl! " + this);
	}

	@Override
	@Transactional
	public QueryDTO getById(Long id) {
		QueryDTO result = queryDao.getById(id);
		return result;
	}

	@Override
	@Transactional
	public QueryEndpointMapDTO getQueryEndpointMapById(Long id) {
		return queryDao.getQueryEndpointById(id);
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
	public synchronized QueryDTO updateQuery(QueryDTO toUpdate) {
		return queryDao.update(toUpdate);
	}

	@Override
	@Transactional
	public synchronized QueryDTO updateQueryStatusFromEndpoints(Long queryId) {
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
	public synchronized QueryDTO cancelQueryToEndpoint(Long queryEndpointMapId) {
		QueryEndpointMapDTO queryEndpointMapToCancel = queryDao.getQueryEndpointById(queryEndpointMapId);
		if(queryEndpointMapToCancel == null) {
			logger.error("Could not find query endpoint map with id " + queryEndpointMapId);
			return null;
		}
		queryEndpointMapToCancel.setStatus(QueryEndpointStatus.Cancelled);
		queryDao.updateQueryEndpointMap(queryEndpointMapToCancel);
		
		String endpointUrl = null;
		EndpointDTO endpoint = endpointDao.findById(queryEndpointMapToCancel.getEndpointId());
		if(endpoint != null) {
			endpointUrl = endpoint.getUrl();
		}
		Long queryId = queryEndpointMapToCancel.getQueryId();
		
		//if we later allow other types to be cancelled this won't work.
		//Also, what if there were multiple PD endpoints for a given location? we wouldn't know which one was being cancelled.
		try {
			auditManager.createAuditEventIG("CANCELLED" , UserUtil.getCurrentUser(), endpointUrl, "", EHealthAdapter.HOME_COMMUNITY_ID);
		} catch(UnsupportedEncodingException ex) {
			logger.warn("Could not add audit record for cancelling request to endpoint " + endpointUrl + " for query " + queryId + ": " + ex.getMessage(), ex);
		} catch(UnknownHostException ex) {
			logger.warn("Could not add audit record for cancelling request to endpoint " + endpointUrl + " for query " + queryId + ": " + ex.getMessage(), ex);
		} catch(Exception ex) {
			logger.warn("Could not add audit record for cancelleing request to endpoint " +endpointUrl + " for query " + queryId + ": " + ex.getMessage(), ex);
		}

		return getById(queryId);
	}
	
	@Override
	@Transactional
	public void close(Long queryId) {
		queryDao.close(queryId);
	}

	@Override
	@Transactional
	public QueryEndpointMapDTO createOrUpdateQueryLocation(QueryEndpointMapDTO toUpdate) {
		QueryEndpointMapDTO updated = null;
		if(toUpdate.getId() == null) {
			updated = queryDao.createQueryEndpointMap(toUpdate);
		} else {
			updated = queryDao.updateQueryEndpointMap(toUpdate);
		}
		return updated;
	}

	@Override
	@Transactional
	public QueryDTO createQuery(QueryDTO toCreate) {
		return queryDao.create(toCreate);
	}

	@Override
	@Transactional
	public void queryForPatientRecords(SAMLInput samlInput, PatientSearch toSearch, QueryDTO query, CommonUser user)
			throws JsonProcessingException {

		if(query.getEndpointMaps() != null && query.getEndpointMaps().size() > 0) {
			for(QueryEndpointMapDTO queryEndpointMap : query.getEndpointMaps()) {
				PatientQueryService service = getPatientQueryService();
				service.setSamlInput(samlInput);
				service.setToSearch(toSearch);
				service.setQueryEndpointMap(queryEndpointMap);
				service.setEndpoint(queryEndpointMap.getEndpoint());
				service.setUser(user);
				pool.execute(service);
			}
		} else {
			query.setStatus(QueryStatus.Complete);
			query.setLastReadDate(new Date());
			queryDao.update(query);
		}
	}

	@Override
	@Transactional
	public void requeryForPatientRecords(Long queryEndpointMapId, CommonUser user) 
			throws JsonProcessingException, IOException {
		QueryEndpointMapDTO queryEndpointMap = queryDao.getQueryEndpointById(queryEndpointMapId);
		if(queryEndpointMap == null) {
			return;
		}
		
		QueryDTO query = queryDao.getById(queryEndpointMap.getQueryId());
		
		//if this request already been closed, do not continue
		if(queryEndpointMap.getStatus() != null && queryEndpointMap.getStatus() == QueryEndpointStatus.Closed) {
			return;
		}
		
		//otherwise set the request to closed, query to active, and run a new query
		queryEndpointMap.setStatus(QueryEndpointStatus.Closed);
		queryDao.updateQueryEndpointMap(queryEndpointMap);
		query.setStatus(QueryStatus.Active);
		queryDao.update(query);
			
		QueryEndpointMapDTO endpointMapForRequery = new QueryEndpointMapDTO();
		endpointMapForRequery.setEndpointId(queryEndpointMap.getEndpointId());
		endpointMapForRequery.setQueryId(query.getId());
		endpointMapForRequery.setStatus(QueryEndpointStatus.Active);
		endpointMapForRequery = createOrUpdateQueryLocation(endpointMapForRequery);
		query.getEndpointMaps().add(endpointMapForRequery);
			
		PatientSearch toSearch = JSONUtils.fromJSON(query.getTerms(), PatientSearch.class);
		
		SAMLInput input = new SAMLInput();
		input.setStrIssuer(user.getSubjectName());
		input.setStrNameQualifier("My Website");
		input.setSessionId(user.getSubjectName());
		HashMap<String, String> customAttributes = new HashMap<String,String>();
		customAttributes.put("RequesterFirstName", user.getFirstName());
		customAttributes.put("RequestReason", "Patient is bleeding.");
		customAttributes.put("PatientGivenName", toSearch.getPatientNames().get(0).getGivenName().get(0));
		customAttributes.put("PatientDOB", toSearch.getDob());
		customAttributes.put("PatientGender", toSearch.getGender());
		customAttributes.put("PatientHomeZip", toSearch.getZip());
		customAttributes.put("PatientSSN", toSearch.getSsn());
		input.setAttributes(customAttributes);

		List<EndpointTypeEnum> relevantEndpointTypes = new ArrayList<EndpointTypeEnum>();
		relevantEndpointTypes.add(EndpointTypeEnum.PATIENT_DISCOVERY);
		
		PatientQueryService service = getPatientQueryService();
		service.setSamlInput(input);
		service.setToSearch(toSearch);
		service.setQueryEndpointMap(endpointMapForRequery);
		service.setEndpoint(endpointMapForRequery.getEndpoint());
		service.setUser(user);
		pool.execute(service);
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
		Long queryEndpointMapId = record.getQueryEndpointId();
		QueryEndpointMapDTO queryLoc = queryDao.getQueryEndpointById(queryEndpointMapId);
		if(queryLoc != null && queryLoc.getStatus() != null && 
				queryLoc.getStatus() != QueryEndpointStatus.Cancelled) {
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
