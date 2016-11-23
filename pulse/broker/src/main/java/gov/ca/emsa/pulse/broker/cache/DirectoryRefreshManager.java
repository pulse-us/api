package gov.ca.emsa.pulse.broker.cache;

import gov.ca.emsa.pulse.broker.manager.LocationManager;
import gov.ca.emsa.pulse.common.domain.Location;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.TimerTask;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.web.client.RestTemplate;

public class DirectoryRefreshManager extends TimerTask {
	private static final Logger logger = LogManager.getLogger(DirectoryRefreshManager.class);

	private LocationManager locationManager;
	private String directoryServicesUrl;
	private long expirationMillis;
	
	public void getDirectories(){
		System.out.println("Updating the directories...");
		RestTemplate restTemplate = new RestTemplate();
		Location[] remoteLocations = restTemplate.getForObject(directoryServicesUrl, Location[].class);
		ArrayList<Location> locations = new ArrayList<Location>(Arrays.asList(remoteLocations));
		locationManager.updateLocations(locations);
	}

	@Override
	public void run() {
		try {
			getDirectories();
		} catch(Exception ex) {
			logger.error("Error updating location cache", ex);
		}
		}

	public void setExpirationMillis(long directoryRefreshExpirationMillis) {
		this.expirationMillis = directoryRefreshExpirationMillis;
	}
	
	public long getExpirationMillis() {
		return expirationMillis;
	}
	
	public void setManager(LocationManager locationManager){
		this.locationManager = locationManager;
	}

	public String getDirectoryServicesUrl() {
		return directoryServicesUrl;
	}

	public void setDirectoryServicesUrl(String directoryServicesUrl) {
		this.directoryServicesUrl = directoryServicesUrl;
	}
}
