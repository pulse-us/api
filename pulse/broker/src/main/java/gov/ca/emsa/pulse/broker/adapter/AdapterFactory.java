package gov.ca.emsa.pulse.broker.adapter;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import gov.ca.emsa.pulse.broker.dto.OrganizationDTO;

@Component
public class AdapterFactory {
	private static final Logger logger = LogManager.getLogger(AdapterFactory.class);
	@Autowired private EHealthAdapter ehealthAdapter;
	@Autowired private IHEAdapter iheAdapter;
	
	public Adapter getAdapter(OrganizationDTO org) {
		if(org.getAdapter().equalsIgnoreCase("ehealth")) {
			return getEhealthAdapter();
		} else if(org.getAdapter().equalsIgnoreCase("ihe")) {
			return getIheAdapter();
		} else {
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

	public IHEAdapter getIheAdapter() {
		return iheAdapter;
	}

	public void setIheAdapter(IHEAdapter iheAdapter) {
		this.iheAdapter = iheAdapter;
	}
}
