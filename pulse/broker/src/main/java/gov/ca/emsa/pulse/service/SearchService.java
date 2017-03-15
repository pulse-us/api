package gov.ca.emsa.pulse.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

import gov.ca.emsa.pulse.auth.user.CommonUser;
import gov.ca.emsa.pulse.broker.domain.QueryType;
import gov.ca.emsa.pulse.broker.dto.DtoToDomainConverter;
import gov.ca.emsa.pulse.broker.dto.LocationDTO;
import gov.ca.emsa.pulse.broker.dto.QueryDTO;
import gov.ca.emsa.pulse.broker.dto.QueryEndpointMapDTO;
import gov.ca.emsa.pulse.broker.manager.AuditEventManager;
import gov.ca.emsa.pulse.broker.manager.LocationManager;
import gov.ca.emsa.pulse.broker.manager.QueryManager;
import gov.ca.emsa.pulse.broker.manager.impl.JSONUtils;
import gov.ca.emsa.pulse.broker.saml.SAMLInput;
import gov.ca.emsa.pulse.common.domain.PatientSearch;
import gov.ca.emsa.pulse.common.domain.Query;
import gov.ca.emsa.pulse.common.domain.QueryEndpointStatus;
import gov.ca.emsa.pulse.common.domain.QueryStatus;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "search")
@RestController
public class SearchService {

	private static final Logger logger = LogManager.getLogger(SearchService.class);
	@Autowired private QueryManager queryManager;
	@Autowired private LocationManager locationManager;
	public static String dobFormat = "yyyy-MM-dd";
	@Autowired private AuditEventManager auditManager;

	public SearchService() {
	}

	@ApiOperation(value="Query all locations for patients. This runs asynchronously and returns a query object"
			+ "which can later be used to get the results.")
	@RequestMapping(path="/search", method = RequestMethod.POST,
		produces="application/json; charset=utf-8",consumes="application/json")
    public @ResponseBody Query searchPatients(@RequestBody(required=true) PatientSearch toSearch) throws JsonProcessingException {

		CommonUser user = UserUtil.getCurrentUser();
		//auditManager.addAuditEntry(QueryType.SEARCH_PATIENT, "/search", user.getSubjectName());

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

		String queryTermsJson = JSONUtils.toJSON(toSearch);
		QueryDTO initiatedQuery = null;

		synchronized(queryManager) {
			List<LocationDTO> locationsToQuery = locationManager.getAll();
			if(locationsToQuery != null && locationsToQuery.size() > 0) {
				QueryDTO query = new QueryDTO();
				query.setUserId(user.getSubjectName());
				query.setTerms(queryTermsJson);
				query.setStatus(QueryStatus.Active);
				query = queryManager.createQuery(query);
		
				//get the list of locations		
				for(LocationDTO location : locationsToQuery) {
					QueryEndpointMapDTO queryLocMap = new QueryEndpointMapDTO();
					queryLocMap.setEndpointId(location.getId());
					queryLocMap.setQueryId(query.getId());
					queryLocMap.setStatus(QueryEndpointStatus.Active);
					queryLocMap = queryManager.createOrUpdateQueryLocation(queryLocMap);
					query.getEndpointStatuses().add(queryLocMap);
				}
	
	        	queryManager.queryForPatientRecords(input, toSearch, query, user);
	        	initiatedQuery = queryManager.getById(query.getId());  
	        }
	        return DtoToDomainConverter.convert(initiatedQuery);
		}
    }
	
	@ApiOperation(value="Re-query a location from an existing query. "
			+ "This runs asynchronously and returns a query object which can later be used to get the results.")
	@RequestMapping(path="/requery/query/{queryId}/locationMap/{queryLocationMapId}", method = RequestMethod.POST,
		produces="application/json; charset=utf-8")
    public @ResponseBody Query requeryPatients(@PathVariable("queryId") Long queryId,
    		@PathVariable("queryLocationMapId") Long queryLocationMapId) throws JsonProcessingException, IOException {

		CommonUser user = UserUtil.getCurrentUser();
		//auditManager.addAuditEntry(QueryType.SEARCH_PATIENT, "/search", user.getSubjectName());
		
        QueryDTO initiatedQuery = null;
        synchronized(queryManager) {
        	queryManager.requeryForPatientRecords(queryLocationMapId, user);
        	initiatedQuery = queryManager.getById(queryId);  
        }
        return DtoToDomainConverter.convert(initiatedQuery);
   }
}
