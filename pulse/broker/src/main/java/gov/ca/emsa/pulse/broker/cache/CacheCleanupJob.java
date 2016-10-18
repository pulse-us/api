package gov.ca.emsa.pulse.broker.cache;

import gov.ca.emsa.pulse.broker.manager.CachedDataManager;

import java.util.Date;
import java.util.TimerTask;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class CacheCleanupJob extends TimerTask {
	private static final Logger logger = LogManager.getLogger(CacheCleanupJob.class);

	private CachedDataManager manager;
	private long expirationMillis;
	
	@Override
	public void run() {
		Date oldestAllowed = new Date(System.currentTimeMillis() - expirationMillis);
		
		try {
			manager.cleanupCache(oldestAllowed);
		} catch(Exception ex) {
			logger.error("Error pruning cache", ex);
		}
	}

	public long getExpirationMillis() {
		return expirationMillis;
	}

	public void setExpirationMillis(long expirationMillis) {
		this.expirationMillis = expirationMillis;
	}

	public CachedDataManager getManager() {
		return manager;
	}

	public void setManager(CachedDataManager manager) {
		this.manager = manager;
	}
}
