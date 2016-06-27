package gov.ca.emsa.pulse.broker.manager;

import gov.ca.emsa.pulse.broker.domain.Query;
import gov.ca.emsa.pulse.broker.domain.QueryType;
import gov.ca.emsa.pulse.broker.domain.User;

public interface AuditManager {
	public void addAuditEntry(User user, QueryType queryType, Query query);
}
