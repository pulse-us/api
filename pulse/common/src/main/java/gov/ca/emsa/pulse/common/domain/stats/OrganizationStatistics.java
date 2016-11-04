package gov.ca.emsa.pulse.common.domain.stats;

import java.util.Date;

import gov.ca.emsa.pulse.common.domain.Organization;

public class OrganizationStatistics {
	private Organization org;
	private Date calculationStart;
	private Date calculationEnd;
	private Long calculationNumRequests;
	
	private RequestStatistics patientDiscoveryStats;
	
	public Organization getOrg() {
		return org;
	}
	public void setOrg(Organization org) {
		this.org = org;
	}
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
}
