package gov.ca.emsa.pulse.broker.manager;

import gov.ca.emsa.pulse.broker.BrokerApplicationTestConfig;
import gov.ca.emsa.pulse.broker.dto.EndpointDTO;
import gov.ca.emsa.pulse.broker.dto.LocationDTO;
import gov.ca.emsa.pulse.common.domain.Endpoint;
import gov.ca.emsa.pulse.common.domain.EndpointStatus;
import gov.ca.emsa.pulse.common.domain.EndpointType;
import gov.ca.emsa.pulse.common.domain.Location;
import gov.ca.emsa.pulse.common.domain.LocationStatus;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={BrokerApplicationTestConfig.class})
public class EndpointManagerTest extends TestCase {
	
	@Autowired private EndpointManager endpointManager;
	
	@Test
	@Transactional
	@Rollback(true)
	public void createEndpointsTest(){
		EndpointStatus status = new EndpointStatus();
		status.setId(1L);
		
		EndpointType type = new EndpointType();
		type.setId(1L);
		
		ArrayList<Endpoint> endpoints = new ArrayList<Endpoint>();
		Endpoint endpoint1 = new Endpoint();
		endpoint1.setAdapter("eHealth");
		endpoint1.setEndpointStatus(status);
		endpoint1.setEndpointType(type);
		endpoint1.setExternalId("001");
		endpoint1.setExternalLastUpdateDate(new Date());
		endpoint1.setUrl("http://test.com"); 
		endpoints.add(endpoint1);
		
		Endpoint endpoint2 = new Endpoint();
		endpoint2.setAdapter("eHealth");
		endpoint2.setEndpointStatus(status);
		endpoint2.setEndpointType(type);
		endpoint2.setExternalId("002");
		endpoint2.setExternalLastUpdateDate(new Date());
		endpoint2.setUrl("http://test.com"); 
		endpoints.add(endpoint2);
		
		endpointManager.updateEndpoints(endpoints);
		
		List<EndpointDTO> allEndpoints = endpointManager.getAll();
		assertEquals(2, allEndpoints.size());
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void updateEndpointsRemoveOneTest(){
		EndpointStatus status = new EndpointStatus();
		status.setId(1L);
		
		EndpointType type = new EndpointType();
		type.setId(1L);
		
		ArrayList<Endpoint> endpoints = new ArrayList<Endpoint>();
		Endpoint endpoint1 = new Endpoint();
		endpoint1.setAdapter("eHealth");
		endpoint1.setEndpointStatus(status);
		endpoint1.setEndpointType(type);
		endpoint1.setExternalId("001");
		endpoint1.setExternalLastUpdateDate(new Date());
		endpoint1.setUrl("http://test.com"); 
		endpoints.add(endpoint1);
		
		Endpoint endpoint2 = new Endpoint();
		endpoint2.setAdapter("eHealth");
		endpoint2.setEndpointStatus(status);
		endpoint2.setEndpointType(type);
		endpoint2.setExternalId("002");
		endpoint2.setExternalLastUpdateDate(new Date());
		endpoint2.setUrl("http://test.com"); 
		endpoints.add(endpoint2);
		
		endpointManager.updateEndpoints(endpoints);
		List<EndpointDTO> endpointResults = endpointManager.getAll();
		assertEquals(2, endpointResults.size());
		endpoints.remove(0);
		
		endpointManager.updateEndpoints(endpoints);
		endpointResults = endpointManager.getAll();
		assertEquals(1, endpointResults.size());
	}
}