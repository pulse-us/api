package gov.ca.emsa.pulse.broker.cache;

import java.util.Date;
import java.util.TimerTask;

import org.springframework.transaction.annotation.Transactional;

import gov.ca.emsa.pulse.broker.dao.DocumentDAO;
import gov.ca.emsa.pulse.broker.dao.PatientDAO;
import gov.ca.emsa.pulse.broker.manager.DocumentManager;
import gov.ca.emsa.pulse.broker.manager.PatientManager;

public class QueryCacheManager extends TimerTask {
	
	private PatientManager patientManager;
	private DocumentManager docManager;
	private long expirationMillis;
	
	@Override
	public void run() {
		Date oldestAllowed = new Date(System.currentTimeMillis() - expirationMillis);
		//check each item in the query cache
		//if the last modified date (or last touched date?) is
		//past the configured expiration, delete it
		patientManager.cleanupPatientCache(oldestAllowed);
		docManager.cleanupDocumentCache(oldestAllowed);
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
