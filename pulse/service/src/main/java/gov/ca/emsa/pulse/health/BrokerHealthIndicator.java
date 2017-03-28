package gov.ca.emsa.pulse.health;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@Api(value = "health")
@Component
@RestController
@RequestMapping("/health")
public class BrokerHealthIndicator implements HealthIndicator {
	private static final Logger logger = LogManager.getLogger(BrokerHealthIndicator.class);

	@Value("${brokerUrl}")
	private String brokerUrl;
	
    @Override
    
    @ApiOperation(value = "Whether or not the service is up or down.")
    @RequestMapping(value = "", method = RequestMethod.GET)
    public Health health() {
    	Health.Builder result = null;
    	
    	RestTemplate restTemplate = new RestTemplate();
    	BrokerHealth brokerHealth = null;
		try {
			brokerHealth = restTemplate.getForObject(brokerUrl + "/health", BrokerHealth.class);
		} catch(Exception ex) {
			logger.error("Exception when querying " + brokerUrl + "/health", ex);
			result = Health.down(ex);
		}
		
        if (result == null && brokerHealth == null) {
            result = Health.down();
        } else if(brokerHealth != null) {
	        result = Health.up().status(brokerHealth.getStatus());
	//        for(String key : brokerHealth.getDetails().keySet()) {
	//        	result.withDetail(key, brokerHealth.getDetails().get(key));
	//        }
        }
        return result.build();
    }
}
