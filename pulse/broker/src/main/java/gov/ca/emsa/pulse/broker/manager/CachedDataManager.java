package gov.ca.emsa.pulse.broker.manager;

import java.util.Date;

public interface CachedDataManager {
	public void cleanupCache(Date oldestAllowedQuery);
}
