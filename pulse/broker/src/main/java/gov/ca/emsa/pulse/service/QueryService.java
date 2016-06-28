package gov.ca.emsa.pulse.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gov.ca.emsa.pulse.broker.domain.Query;
import gov.ca.emsa.pulse.broker.dto.QueryDTO;
import gov.ca.emsa.pulse.broker.manager.QueryManager;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "queryStatus")
@RestController
public class QueryService {
	@Autowired QueryManager queryManager;
	
	@ApiOperation(value="Get the status of a query")
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
    }
}
