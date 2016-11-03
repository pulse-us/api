package gov.ca.emsa.pulse.broker.cache;

public class CacheCleanupException extends Exception {
	private static final long serialVersionUID = -7548441857908720113L;

	public CacheCleanupException() {
		super();
	}
	
	public CacheCleanupException(String msg) {
		super(msg);
	}
}
