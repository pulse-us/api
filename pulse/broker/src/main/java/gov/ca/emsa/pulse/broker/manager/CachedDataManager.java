package gov.ca.emsa.pulse.broker.manager;

import java.util.Date;

import gov.ca.emsa.pulse.broker.cache.CacheCleanupException;

public interface CachedDataManager {
	public void cleanupCache(Date oldestAllowedQuery) throws CacheCleanupException;
}
