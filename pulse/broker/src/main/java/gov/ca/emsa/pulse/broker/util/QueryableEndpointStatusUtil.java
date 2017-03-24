package gov.ca.emsa.pulse.broker.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import gov.ca.emsa.pulse.broker.domain.EndpointStatusEnum;

@Component
public class QueryableEndpointStatusUtil {
	private static final Logger logger = LogManager.getLogger(QueryableEndpointStatusUtil.class);

	@Value("${endpointStatusesToQuery}")
	private String endpointStatusesToQueryProperty;
	
	private List<EndpointStatusEnum> statuses;
	
	public QueryableEndpointStatusUtil() {
		statuses = new ArrayList<EndpointStatusEnum>();
	}

	public List<EndpointStatusEnum> getStatuses() {
		if(statuses.size() == 0) {
			if(StringUtils.isEmpty(endpointStatusesToQueryProperty)) {
				statuses.add(EndpointStatusEnum.ACTIVE);
			} else {
				String[] endpointStatusArr = endpointStatusesToQueryProperty.split(",");
				for(int i = 0; i < endpointStatusArr.length; i++) {
					try {
						EndpointStatusEnum endpointStatus = EndpointStatusEnum.valueOf(endpointStatusArr[i].toUpperCase());
						if(endpointStatus != null) {
							statuses.add(endpointStatus);
						}
					} catch(IllegalArgumentException ex) {
						logger.error("Could not find endpoint status from properties file with name " + endpointStatusArr[i]);
					}
				}
			}
		}
		return statuses;
	}

	public String getEndpointStatusesToQueryProperty() {
		return endpointStatusesToQueryProperty;
	}

	public void setEndpointStatusesToQueryProperty(String endpointStatusesToQueryProperty) {
		this.endpointStatusesToQueryProperty = endpointStatusesToQueryProperty;
	}

}
