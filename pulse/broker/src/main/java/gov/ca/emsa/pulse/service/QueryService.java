package gov.ca.emsa.pulse.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import gov.ca.emsa.pulse.broker.domain.Patient;
import gov.ca.emsa.pulse.broker.domain.PatientRecord;
import gov.ca.emsa.pulse.broker.domain.Query;
import gov.ca.emsa.pulse.broker.domain.User;
import gov.ca.emsa.pulse.broker.dto.QueryDTO;
import gov.ca.emsa.pulse.broker.manager.QueryManager;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "queryStatus")
@RestController
<<<<<<< HEAD
@RequestMapping("/queries")
=======
>>>>>>> blindsey/development
public class QueryService {
	@Autowired QueryManager queryManager;
	
	@ApiOperation(value = "Get all queries for the logged-in user")
	@RequestMapping(value="", method = RequestMethod.POST)
	public List<Query> getQueries(@RequestBody User user) {
		List<QueryDTO> queries = queryManager.getAllQueriesForUser(user.getUserKey());
		List<Query> results = new ArrayList<Query>();
		for(QueryDTO query : queries) {
			results.add(new Query(query));
		}
		return results;
	}
	
	@ApiOperation(value="Get the status of a query")
<<<<<<< HEAD
	@RequestMapping(value="/{queryId}", method = RequestMethod.POST)
    public Query getQueryStatus(@PathVariable(value="queryId") Long queryId,
    		@RequestBody User user) {
       QueryDTO initiatedQuery = queryManager.getById(queryId); 
       return new Query(initiatedQuery);
    }
	
	//TODO: cannot have multiple request body things
	@ApiOperation(value="Get the status of a query")
	@RequestMapping(value="/{queryId}", method = RequestMethod.POST)
    public Query create(@RequestBody List<Long> patientRecordIds,
    		@RequestBody Patient mergedPatient,
    		@RequestBody User user) {
       
=======
	@RequestMapping("/query/{queryId}")
    public Query getStatus(@PathVariable(value="queryId") Long queryId) {
       QueryDTO initiatedQuery = queryManager.getQueryStatusDetails(queryId); 
       return new Query(initiatedQuery);
    }
	
	@ApiOperation(value="Get all queries from current logged in user.")
	@RequestMapping("/queries")
    public ArrayList<Query> getStatus(@PathVariable(value="userToken") String userToken) {
       List<QueryDTO> initiatedQuery = queryManager.getAllQueriesForUser(userToken);
       ArrayList<Query> queryDomainList = new ArrayList<Query>();
       for(QueryDTO qdto : initiatedQuery){
    	   queryDomainList.add(new Query(qdto));
       }
       return queryDomainList;
>>>>>>> blindsey/development
    }
}
