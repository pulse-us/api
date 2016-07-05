package gov.ca.emsa.pulse.broker.manager.impl;

import java.util.List;

import gov.ca.emsa.pulse.broker.dao.AuditDAO;
import gov.ca.emsa.pulse.broker.domain.QueryType;
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
	public AuditDTO addAuditEntry(QueryType queryType, String query, String querent){
		AuditDTO audit = new AuditDTO();
		audit.setQueryType(queryType.toString());
		audit.setQuery(query);
		audit.setQuerent(querent);
		AuditDTO aud = auditDao.create(audit);
		return aud;
	}
	
	@Override
	@Transactional
	public List<AuditDTO> getAll(){
		List<AuditDTO> audits = auditDao.findAll();
		return audits;
	}

}
