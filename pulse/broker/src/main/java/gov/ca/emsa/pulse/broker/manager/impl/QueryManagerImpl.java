package gov.ca.emsa.pulse.broker.manager.impl;

import gov.ca.emsa.pulse.auth.user.CommonUser;
import gov.ca.emsa.pulse.broker.adapter.EHealthAdapter;
import gov.ca.emsa.pulse.broker.dao.LocationDAO;
import gov.ca.emsa.pulse.broker.dao.PatientRecordDAO;
import gov.ca.emsa.pulse.broker.dao.QueryDAO;
import gov.ca.emsa.pulse.broker.domain.EndpointTypeEnum;
import gov.ca.emsa.pulse.broker.dto.LocationDTO;
import gov.ca.emsa.pulse.broker.dto.LocationEndpointDTO;
import gov.ca.emsa.pulse.broker.dto.PatientRecordDTO;
import gov.ca.emsa.pulse.broker.dto.QueryDTO;
import gov.ca.emsa.pulse.broker.dto.QueryLocationMapDTO;
import gov.ca.emsa.pulse.broker.manager.AuditEventManager;
import gov.ca.emsa.pulse.broker.manager.LocationManager;
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
import gov.ca.emsa.pulse.common.domain.QueryLocationStatus;

@Service
public class QueryManagerImpl implements QueryManager, ApplicationContextAware {
	private static final Logger logger = LogManager.getLogger(QueryManagerImpl.class);

	@Autowired QueryDAO queryDao;
	@Autowired LocationDAO locationDao;
	@Autowired PatientRecordDAO patientRecordDao;
	@Autowired private LocationManager locationManager;
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
	public synchronized QueryDTO updateQueryStatusFromLocations(Long queryId) {
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
	public synchronized QueryDTO cancelQueryToLocation(Long queryLocationMapId) {
		QueryLocationMapDTO queryLocationMapToCancel = queryDao.getQueryLocationById(queryLocationMapId);
		if(queryLocationMapToCancel == null) {
			logger.error("Could not find query location map " + queryLocationMapId);
			return null;
		}
		queryLocationMapToCancel.setStatus(QueryLocationStatus.Cancelled);
		queryDao.updateQueryLocationMap(queryLocationMapToCancel);
		
		String endpointUrl = null;
		Long locationId = queryLocationMapToCancel.getLocationId();
		Long queryId = queryLocationMapToCancel.getQueryId();
		LocationDTO location = locationDao.findById(locationId);
		if(location != null) {
			for(LocationEndpointDTO ept : location.getEndpoints()) {
				if(ept.getEndpointType().getCode().equals(EndpointTypeEnum.PATIENT_DISCOVERY.getCode())) {
					endpointUrl = ept.getUrl();
				}
			}
		}
		//if we later allow other types to be cancelled this won't work.
		//Also, what if there were multiple PD endpoints for a given location? we wouldn't know which one was being cancelled.
		try {
			auditManager.createAuditEventIG("CANCELLED" , UserUtil.getCurrentUser(), endpointUrl, "", EHealthAdapter.HOME_COMMUNITY_ID);
		} catch(UnsupportedEncodingException ex) {
			logger.warn("Could not add audit record for cancelling request to location " + locationId + " for query " + queryId + ": " + ex.getMessage(), ex);
		} catch(UnknownHostException ex) {
			logger.warn("Could not add audit record for cancelling request to location " + locationId + " for query " + queryId + ": " + ex.getMessage(), ex);
		} catch(Exception ex) {
			logger.warn("Could not add audit record for cancelleing request to locaiton " +locationId + " for query " + queryId + ": " + ex.getMessage(), ex);
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
	@Transactional
	public void queryForPatientRecords(SAMLInput samlInput, PatientSearch toSearch, QueryDTO query, CommonUser user)
			throws JsonProcessingException {

		List<EndpointTypeEnum> relevantEndpointTypes = new ArrayList<EndpointTypeEnum>();
		relevantEndpointTypes.add(EndpointTypeEnum.PATIENT_DISCOVERY);
		//get the list of locations
		List<LocationDTO> locationsToQuery = locationManager.getAllWithEndpointType(relevantEndpointTypes);
		if(locationsToQuery != null && locationsToQuery.size() > 0) {
			for(QueryLocationMapDTO queryLoc : query.getLocationStatuses()) {
				PatientQueryService service = getPatientQueryService();
				service.setSamlInput(samlInput);
				service.setToSearch(toSearch);
				service.setQueryLocation(queryLoc);
				service.setUser(user);
				pool.execute(service);
			}
		} else {
			for(QueryLocationMapDTO queryLoc : query.getLocationStatuses()) {
				queryLoc.setStatus( QueryLocationStatus.Failed);
				queryLoc.setEndDate(new Date());
				createOrUpdateQueryLocation(queryLoc);
			}
			query.setStatus(QueryStatus.Complete);
			query.setLastReadDate(new Date());
			queryDao.update(query);
		}
	}

	@Override
	@Transactional
	public void requeryForPatientRecords(Long queryLocationMapId, CommonUser user)
			throws JsonProcessingException, IOException {
		QueryLocationMapDTO locationMap = queryDao.getQueryLocationById(queryLocationMapId);
		if(locationMap == null) {
			return;
		}
		
		QueryDTO query = queryDao.getById(locationMap.getQueryId());
		
		//if the location is already closed, do not continue
		if(locationMap.getStatus() != null && locationMap.getStatus() == QueryLocationStatus.Closed) {
			return;
		}
		
		//otherwise set the location to closed, query to active, and run a new query
		locationMap.setStatus(QueryLocationStatus.Closed);
		queryDao.updateQueryLocationMap(locationMap);
		query.setStatus(QueryStatus.Active);
		queryDao.update(query);
			
		QueryLocationMapDTO locationToRequery = new QueryLocationMapDTO();
		locationToRequery.setLocationId(locationMap.getLocationId());
		locationToRequery.setQueryId(query.getId());
		locationToRequery.setStatus(QueryLocationStatus.Active);
		locationToRequery = createOrUpdateQueryLocation(locationToRequery);
		query.getLocationStatuses().add(locationToRequery);
			
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
		service.setQueryLocation(locationToRequery);
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
