package gov.ca.emsa.pulse.broker.cache;

import java.util.Date;
import java.util.TimerTask;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import gov.ca.emsa.pulse.broker.manager.DocumentManager;
import gov.ca.emsa.pulse.broker.manager.PatientManager;

public class QueryCacheManager extends TimerTask {
	private static final Logger logger = LogManager.getLogger(QueryCacheManager.class);

	private PatientManager patientManager;
	private DocumentManager docManager;
	private long expirationMillis;
	
	@Override
	public void run() {
		try {
			Date oldestAllowed = new Date(System.currentTimeMillis() - expirationMillis);
			//check each item in the query cache
			//if the last modified date (or last touched date?) is
			//past the configured expiration, delete it
			patientManager.cleanupPatientCache(oldestAllowed);
			docManager.cleanupDocumentCache(oldestAllowed);
		} catch(Exception ex) {
			logger.error("Error pruning patient or document cache", ex);
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

	public DocumentManager getDocManager() {
		return docManager;
	}

	public void setDocManager(DocumentManager docManager) {
		this.docManager = docManager;
	}
}
