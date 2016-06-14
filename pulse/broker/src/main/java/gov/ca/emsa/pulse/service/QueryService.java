package gov.ca.emsa.pulse.service;

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
@RequestMapping("/query")
public class QueryService {
	@Autowired QueryManager queryManager;
	
	@ApiOperation(value="Get the status of a query")
	@RequestMapping("/{queryId}")
    public Query getStatus(@PathVariable(value="queryId") Long queryId) {
       QueryDTO initiatedQuery = queryManager.getQueryStatusDetails(queryId); 
       return new Query(initiatedQuery);
    }
}
