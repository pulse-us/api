package gov.ca.emsa.pulse.broker.manager.impl;

import gov.ca.emsa.pulse.broker.dao.AuditDAO;
import gov.ca.emsa.pulse.broker.dto.AuditDTO;
import gov.ca.emsa.pulse.broker.manager.AuditManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuditManagerImpl implements AuditManager{
	
	@Autowired
	private AuditDAO auditDao;
	
	@Override
	@Transactional
	public void addAuditEntry(AuditDTO audit){
		auditDao.create(audit);
	}

}
