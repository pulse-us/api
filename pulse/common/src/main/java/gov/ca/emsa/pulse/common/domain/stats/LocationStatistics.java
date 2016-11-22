package gov.ca.emsa.pulse.common.domain.stats;

import java.util.Date;

import gov.ca.emsa.pulse.common.domain.Location;

public class LocationStatistics {
	private Location location;
	private Date calculationStart;
	private Date calculationEnd;
	private Long calculationNumRequests;
	
	private RequestStatistics patientDiscoveryStats;
	
	public Date getCalculationStart() {
		return calculationStart;
	}
	public void setCalculationStart(Date calculationStart) {
		this.calculationStart = calculationStart;
	}
	public Date getCalculationEnd() {
		return calculationEnd;
	}
	public void setCalculationEnd(Date calculationEnd) {
		this.calculationEnd = calculationEnd;
	}
	public Long getCalculationNumRequests() {
		return calculationNumRequests;
	}
	public void setCalculationNumRequests(Long calculationNumRequests) {
		this.calculationNumRequests = calculationNumRequests;
	}
	public RequestStatistics getPatientDiscoveryStats() {
		return patientDiscoveryStats;
	}
	public void setPatientDiscoveryStats(RequestStatistics patientDiscoveryStats) {
		this.patientDiscoveryStats = patientDiscoveryStats;
	}
	public Location getLocation() {
		return location;
	}
	public void setLocation(Location location) {
		this.location = location;
	}
}
