package gov.ca.emsa.pulse.broker.dao;

import java.util.Date;
import java.util.List;

import gov.ca.emsa.pulse.broker.entity.PatientDiscoveryRequestStatisticsEntity;

public interface PatientDiscoveryQueryStatisticsDAO {
	public List<PatientDiscoveryRequestStatisticsEntity> getStatistics(Date startFilter, Date endFilter);
}
