package gov.ca.emsa.pulse.broker.adapter;

import gov.ca.emsa.pulse.broker.dto.LocationDTO;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AdapterFactory {
	private static final Logger logger = LogManager.getLogger(AdapterFactory.class);
	@Autowired private EHealthAdapter ehealthAdapter;
	
	public Adapter getAdapter(LocationDTO org) {
		if(org.getAdapter().equalsIgnoreCase("ehealth")) {
			return getEhealthAdapter();
		} 
		//TODO: if we want to add future types like IHE, they will go here as else if{}
		else {
			logger.error("Could not find a matching adapter for org type : " + org.getAdapter());
		}
		return null;
	}

	public EHealthAdapter getEhealthAdapter() {
		return ehealthAdapter;
	}

	public void setEhealthAdapter(EHealthAdapter ehealthAdapter) {
		this.ehealthAdapter = ehealthAdapter;
	}
}
