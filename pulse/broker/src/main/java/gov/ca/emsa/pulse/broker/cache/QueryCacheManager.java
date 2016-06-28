package gov.ca.emsa.pulse.broker.cache;

import java.util.Date;
import java.util.TimerTask;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import gov.ca.emsa.pulse.broker.manager.AlternateCareFacilityManager;
import gov.ca.emsa.pulse.broker.manager.DocumentManager;
import gov.ca.emsa.pulse.broker.manager.PatientManager;
import gov.ca.emsa.pulse.broker.manager.QueryManager;

public class QueryCacheManager extends TimerTask {
	private static final Logger logger = LogManager.getLogger(QueryCacheManager.class);

	private AlternateCareFacilityManager acfManager;
	private PatientManager patientManager;
	private QueryManager queryManager;
	private long expirationMillis;
	
	@Override
	public void run() {
		Date oldestAllowed = new Date(System.currentTimeMillis() - expirationMillis);
		
		try {
			acfManager.cleanup(oldestAllowed);
		} catch(Exception ex) {
			logger.error("Error pruning ACF cache", ex);
		}
		
		try {
			//check each item in the query cache
			//if the last_read_date is
			//past the configured expiration, delete it
			//document deletes will cascade
			patientManager.cleanupPatientCache(oldestAllowed);
		} catch(Exception ex) {
			logger.error("Error pruning patient cache", ex);
		}
		
		try {
			queryManager.cleanupQueryCache(oldestAllowed);
		} catch(Exception ex) {
			logger.error("Error pruning the query cache", ex);
		}
	}

	public long getExpirationMillis() {
		return expirationMillis;
	}

	public void setExpirationMillis(long expirationMillis) {
		this.expirationMillis = expirationMillis;
	}

	public PatientManager getPatientManager() {
		return patientManager;
	}

	public void setPatientManager(PatientManager patientManager) {
		this.patientManager = patientManager;
	}

	public QueryManager getQueryManager() {
		return queryManager;
	}

	public void setQueryManager(QueryManager queryManager) {
		this.queryManager = queryManager;
	}

	public AlternateCareFacilityManager getAcfManager() {
		return acfManager;
	}

	public void setAcfManager(AlternateCareFacilityManager acfManager) {
		this.acfManager = acfManager;
	}
}
