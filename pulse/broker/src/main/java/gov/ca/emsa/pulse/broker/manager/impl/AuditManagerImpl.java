package gov.ca.emsa.pulse.broker.manager.impl;

import gov.ca.emsa.pulse.broker.domain.Query;
import gov.ca.emsa.pulse.broker.domain.QueryType;
import gov.ca.emsa.pulse.broker.domain.User;
import gov.ca.emsa.pulse.broker.manager.AuditManager;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuditManagerImpl implements AuditManager{
	
	@Override
	@Transactional
	public void addAuditEntry(User user, QueryType queryType, Query query){
		
	}

}
