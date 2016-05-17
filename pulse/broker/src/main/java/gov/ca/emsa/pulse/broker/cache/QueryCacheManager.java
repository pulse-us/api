package gov.ca.emsa.pulse.broker.cache;

import java.util.TimerTask;

public class QueryCacheManager extends TimerTask {
	
	private long expirationMillis;
	
	@Override
	public void run() {
		//TODO: check each item in the query cache
		//if the last modified date (or last touched date?) is
		//past the configured expiration, delete it
	}

	public long getExpirationMillis() {
		return expirationMillis;
	}

	public void setExpirationMillis(long expirationMillis) {
		this.expirationMillis = expirationMillis;
	}
}
