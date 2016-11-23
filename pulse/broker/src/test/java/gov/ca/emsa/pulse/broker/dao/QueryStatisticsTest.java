package gov.ca.emsa.pulse.broker.dao;

import gov.ca.emsa.pulse.broker.BrokerApplicationTestConfig;
import gov.ca.emsa.pulse.broker.dto.LocationDTO;
import gov.ca.emsa.pulse.broker.dto.LocationStatusDTO;
import gov.ca.emsa.pulse.broker.dto.QueryDTO;
import gov.ca.emsa.pulse.broker.dto.QueryLocationMapDTO;
import gov.ca.emsa.pulse.broker.entity.PatientDiscoveryRequestStatisticsEntity;
import gov.ca.emsa.pulse.common.domain.QueryStatus;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import gov.ca.emsa.pulse.common.domain.QueryLocationStatus;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={BrokerApplicationTestConfig.class})
public class QueryStatisticsTest extends TestCase {

	@Autowired LocationDAO locationDao;
	@Autowired QueryDAO queryDao;
	@Autowired PatientDiscoveryQueryStatisticsDAO statDao;
	
	private LocationDTO location1;
	private LocationDTO location2;
	private QueryDTO query;
	
	@Test
	@Transactional
	@Rollback(true)
	public void testGetStatisticsNoDateFilters() {		
		setupQuery();
		
		//set up end times and both success for the statuses
		//update end times and statuses
		QueryDTO selected = queryDao.getById(query.getId());
		assertNotNull(selected);
		assertNotNull(selected.getId());
		assertTrue(selected.getId().longValue() > 0);
		assertNotNull(selected.getLocationStatuses());
		assertEquals(2, selected.getLocationStatuses().size());
		QueryLocationMapDTO orgQuery1 = selected.getLocationStatuses().get(0);
		assertNotNull(orgQuery1.getId());
		assertTrue(orgQuery1.getId().longValue() > 0);
		QueryLocationMapDTO orgQuery2 = selected.getLocationStatuses().get(1);
		assertNotNull(orgQuery2.getId());
		assertTrue(orgQuery2.getId().longValue() > 0);
		assertTrue(orgQuery1.getId().longValue() != orgQuery2.getId().longValue());
		
		Calendar endCal = Calendar.getInstance();
		endCal.setTime(orgQuery1.getStartDate());
		endCal.add(Calendar.HOUR, 1);
		orgQuery1.setEndDate(endCal.getTime());
		orgQuery1.setStatus(QueryLocationStatus.Successful);
		queryDao.updateQueryLocationMap(orgQuery1);
				
		endCal = Calendar.getInstance();
		endCal.setTime(orgQuery2.getStartDate());
		endCal.add(Calendar.HOUR, 2);
		orgQuery2.setEndDate(endCal.getTime());
		orgQuery2.setStatus(QueryLocationStatus.Successful);
		queryDao.updateQueryLocationMap(orgQuery2);
				
		//get the stats		
		List<PatientDiscoveryRequestStatisticsEntity> results = statDao.getStatistics(null, null);
		assertNotNull(results);
		assertEquals(2, results.size());
		boolean foundOrg1 = false;
		boolean foundOrg2 = false;
		for(PatientDiscoveryRequestStatisticsEntity result : results) {
			if(result.getLocationName().equals(location1.getName())) {
				foundOrg1 = true;
			} else if(result.getLocationName().equals(location2.getName())) {
				foundOrg2 = true;
			}
		}
		assertTrue(foundOrg1 && foundOrg2);
		
		for(PatientDiscoveryRequestStatisticsEntity result : results) {
			assertNotNull(result.getTotalRequestCount());
			assertEquals(1, result.getTotalRequestCount().longValue());
			assertNotNull(result.getTotalRequestAverageSeconds());
			assertTrue(result.getTotalRequestAverageSeconds() > 0);
			assertNotNull(result.getSuccessfulRequestCount());
			assertEquals(1, result.getSuccessfulRequestCount().longValue());
			assertNotNull(result.getSuccessfulRequestAverageSeconds());
			assertTrue(result.getSuccessfulRequestAverageSeconds() > 0);
			assertNotNull(result.getFailedRequestCount());
			assertNull(result.getFailedRequestAverageSeconds());
			assertEquals(0, result.getFailedRequestCount().longValue());
			assertNotNull(result.getCancelledRequestCount());
			assertEquals(0, result.getCancelledRequestCount().longValue());
			assertNull(result.getCancelledRequestAverageSeconds());
		}
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void testGetStatisticsWithStartAndEndDateFiltersForAllQueries() {		
		setupQuery();
		
		//set up end times and both success for the statuses
		//update end times and statuses
		QueryDTO selected = queryDao.getById(query.getId());
		assertNotNull(selected);
		assertNotNull(selected.getId());
		assertTrue(selected.getId().longValue() > 0);
		assertNotNull(selected.getLocationStatuses());
		assertEquals(2, selected.getLocationStatuses().size());
		QueryLocationMapDTO orgQuery1 = selected.getLocationStatuses().get(0);
		assertNotNull(orgQuery1.getId());
		assertTrue(orgQuery1.getId().longValue() > 0);
		QueryLocationMapDTO orgQuery2 = selected.getLocationStatuses().get(1);
		assertNotNull(orgQuery2.getId());
		assertTrue(orgQuery2.getId().longValue() > 0);
		assertTrue(orgQuery1.getId().longValue() != orgQuery2.getId().longValue());
		
		Calendar endCal = Calendar.getInstance();
		endCal.setTime(orgQuery1.getStartDate());
		endCal.add(Calendar.HOUR, 1);
		orgQuery1.setEndDate(endCal.getTime());
		orgQuery1.setStatus(QueryLocationStatus.Successful);
		queryDao.updateQueryLocationMap(orgQuery1);
				
		endCal = Calendar.getInstance();
		endCal.setTime(orgQuery2.getStartDate());
		endCal.add(Calendar.HOUR, 2);
		orgQuery2.setEndDate(endCal.getTime());
		orgQuery2.setStatus(QueryLocationStatus.Successful);
		queryDao.updateQueryLocationMap(orgQuery2);
				
		//get the stats	
		//set the start filter to be 5 seconds before the earliest of the two queries
		Calendar startFilter = Calendar.getInstance();
		startFilter.setTime(orgQuery1.getStartDate().getTime() < orgQuery2.getStartDate().getTime() ? orgQuery1.getStartDate() : orgQuery2.getStartDate());
		startFilter.add(Calendar.SECOND, -5);
		//set the end filter to be 5 seconds after the latest of the two queries
		Calendar endFilter = Calendar.getInstance();
		endFilter.setTime(orgQuery1.getEndDate().getTime() > orgQuery2.getEndDate().getTime() ? orgQuery1.getEndDate() : orgQuery2.getEndDate());
		endFilter.add(Calendar.SECOND, 5);
		
		List<PatientDiscoveryRequestStatisticsEntity> results = statDao.getStatistics(startFilter.getTime(), endFilter.getTime());
		assertNotNull(results);
		assertEquals(2, results.size());
		boolean foundOrg1 = false;
		boolean foundOrg2 = false;
		for(PatientDiscoveryRequestStatisticsEntity result : results) {
			if(result.getLocationName().equals(location1.getName())) {
				foundOrg1 = true;
			} else if(result.getLocationName().equals(location2.getName())) {
				foundOrg2 = true;
			}
		}
		assertTrue(foundOrg1 && foundOrg2);
		
		for(PatientDiscoveryRequestStatisticsEntity result : results) {
			assertNotNull(result.getTotalRequestCount());
			assertEquals(1, result.getTotalRequestCount().longValue());
			assertNotNull(result.getTotalRequestAverageSeconds());
			assertTrue(result.getTotalRequestAverageSeconds() > 0);
			assertNotNull(result.getSuccessfulRequestCount());
			assertEquals(1, result.getSuccessfulRequestCount().longValue());
			assertNotNull(result.getSuccessfulRequestAverageSeconds());
			assertTrue(result.getSuccessfulRequestAverageSeconds() > 0);
			assertNotNull(result.getFailedRequestCount());
			assertNull(result.getFailedRequestAverageSeconds());
			assertEquals(0, result.getFailedRequestCount().longValue());
			assertNotNull(result.getCancelledRequestCount());
			assertEquals(0, result.getCancelledRequestCount().longValue());
			assertNull(result.getCancelledRequestAverageSeconds());
		}
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void testGetStatisticsWithStartDateFilterAfterQueryStartDates() {		
		setupQuery();
		
		//set up end times and both success for the statuses
		//update end times and statuses
		QueryDTO selected = queryDao.getById(query.getId());
		assertNotNull(selected);
		assertNotNull(selected.getId());
		assertTrue(selected.getId().longValue() > 0);
		assertNotNull(selected.getLocationStatuses());
		assertEquals(2, selected.getLocationStatuses().size());
		QueryLocationMapDTO orgQuery1 = selected.getLocationStatuses().get(0);
		assertNotNull(orgQuery1.getId());
		assertTrue(orgQuery1.getId().longValue() > 0);
		QueryLocationMapDTO orgQuery2 = selected.getLocationStatuses().get(1);
		assertNotNull(orgQuery2.getId());
		assertTrue(orgQuery2.getId().longValue() > 0);
		assertTrue(orgQuery1.getId().longValue() != orgQuery2.getId().longValue());
		
		Calendar endCal = Calendar.getInstance();
		endCal.setTime(orgQuery1.getStartDate());
		endCal.add(Calendar.HOUR, 1);
		orgQuery1.setEndDate(endCal.getTime());
		orgQuery1.setStatus(QueryLocationStatus.Successful);
		queryDao.updateQueryLocationMap(orgQuery1);
				
		endCal = Calendar.getInstance();
		endCal.setTime(orgQuery2.getStartDate());
		endCal.add(Calendar.HOUR, 2);
		orgQuery2.setEndDate(endCal.getTime());
		orgQuery2.setStatus(QueryLocationStatus.Successful);
		queryDao.updateQueryLocationMap(orgQuery2);
				
		//get the stats	
		
		//set the start filter to be 5 seconds after the latest of the two queries
		Calendar startFilter = Calendar.getInstance();
		startFilter.setTime(orgQuery1.getStartDate().getTime() > orgQuery2.getStartDate().getTime() ? orgQuery1.getStartDate() : orgQuery2.getStartDate());
		startFilter.add(Calendar.SECOND, 5);
		
		List<PatientDiscoveryRequestStatisticsEntity> results = statDao.getStatistics(startFilter.getTime(), null);
		assertNotNull(results);
		assertEquals(2, results.size());
		boolean foundOrg1 = false;
		boolean foundOrg2 = false;
		for(PatientDiscoveryRequestStatisticsEntity result : results) {
			if(result.getLocationName().equals(location1.getName())) {
				foundOrg1 = true;
			} else if(result.getLocationName().equals(location2.getName())) {
				foundOrg2 = true;
			}
		}
		assertTrue(foundOrg1 && foundOrg2);
		
		for(PatientDiscoveryRequestStatisticsEntity result : results) {
			assertNotNull(result.getTotalRequestCount());
			assertEquals(0, result.getTotalRequestCount().longValue());
			assertNull(result.getTotalRequestAverageSeconds());
			assertNotNull(result.getSuccessfulRequestCount());
			assertEquals(0, result.getSuccessfulRequestCount().longValue());
			assertNull(result.getSuccessfulRequestAverageSeconds());
			assertNotNull(result.getFailedRequestCount());
			assertEquals(0, result.getFailedRequestCount().longValue());
			assertNull(result.getFailedRequestAverageSeconds());
			assertNotNull(result.getCancelledRequestCount());
			assertEquals(0, result.getCancelledRequestCount().longValue());
			assertNull(result.getCancelledRequestAverageSeconds());
		}
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void testGetStatisticsWithEndDateFilterBeforeQueryEndDates() {		
		setupQuery();
		
		//set up end times and both success for the statuses
		//update end times and statuses
		QueryDTO selected = queryDao.getById(query.getId());
		assertNotNull(selected);
		assertNotNull(selected.getId());
		assertTrue(selected.getId().longValue() > 0);
		assertNotNull(selected.getLocationStatuses());
		assertEquals(2, selected.getLocationStatuses().size());
		QueryLocationMapDTO orgQuery1 = selected.getLocationStatuses().get(0);
		assertNotNull(orgQuery1.getId());
		assertTrue(orgQuery1.getId().longValue() > 0);
		QueryLocationMapDTO orgQuery2 = selected.getLocationStatuses().get(1);
		assertNotNull(orgQuery2.getId());
		assertTrue(orgQuery2.getId().longValue() > 0);
		assertTrue(orgQuery1.getId().longValue() != orgQuery2.getId().longValue());
		
		Calendar endCal = Calendar.getInstance();
		endCal.setTime(orgQuery1.getStartDate());
		endCal.add(Calendar.HOUR, 1);
		orgQuery1.setEndDate(endCal.getTime());
		orgQuery1.setStatus(QueryLocationStatus.Successful);
		queryDao.updateQueryLocationMap(orgQuery1);
				
		endCal = Calendar.getInstance();
		endCal.setTime(orgQuery2.getStartDate());
		endCal.add(Calendar.HOUR, 2);
		orgQuery2.setEndDate(endCal.getTime());
		orgQuery2.setStatus(QueryLocationStatus.Successful);
		queryDao.updateQueryLocationMap(orgQuery2);
				
		//get the stats	
		
		//set the end filter to be 5 seconds before the earliest of the two queries
		Calendar endFilter = Calendar.getInstance();
		endFilter.setTime(orgQuery1.getStartDate().getTime() < orgQuery2.getStartDate().getTime() ? orgQuery1.getStartDate() : orgQuery2.getStartDate());
		endFilter.add(Calendar.SECOND, -5);
		
		List<PatientDiscoveryRequestStatisticsEntity> results = statDao.getStatistics(null, endFilter.getTime());
		assertNotNull(results);
		assertEquals(2, results.size());
		boolean foundOrg1 = false;
		boolean foundOrg2 = false;
		for(PatientDiscoveryRequestStatisticsEntity result : results) {
			if(result.getLocationName().equals(location1.getName())) {
				foundOrg1 = true;
			} else if(result.getLocationName().equals(location2.getName())) {
				foundOrg2 = true;
			}
		}
		assertTrue(foundOrg1 && foundOrg2);
		
		for(PatientDiscoveryRequestStatisticsEntity result : results) {
			assertNotNull(result.getTotalRequestCount());
			assertEquals(0, result.getTotalRequestCount().longValue());
			assertNull(result.getTotalRequestAverageSeconds());
			assertNotNull(result.getSuccessfulRequestCount());
			assertEquals(0, result.getSuccessfulRequestCount().longValue());
			assertNull(result.getSuccessfulRequestAverageSeconds());
			assertNotNull(result.getFailedRequestCount());
			assertEquals(0, result.getFailedRequestCount().longValue());
			assertNull(result.getFailedRequestAverageSeconds());
			assertNotNull(result.getCancelledRequestCount());
			assertEquals(0, result.getCancelledRequestCount().longValue());
			assertNull(result.getCancelledRequestAverageSeconds());
		}
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void testGetStatisticsWithStartAndEndDateFiltersOutsideQueryBounds() {		
		setupQuery();
		
		//set up end times and both success for the statuses
		//update end times and statuses
		QueryDTO selected = queryDao.getById(query.getId());
		assertNotNull(selected);
		assertNotNull(selected.getId());
		assertTrue(selected.getId().longValue() > 0);
		assertNotNull(selected.getLocationStatuses());
		assertEquals(2, selected.getLocationStatuses().size());
		QueryLocationMapDTO orgQuery1 = selected.getLocationStatuses().get(0);
		assertNotNull(orgQuery1.getId());
		assertTrue(orgQuery1.getId().longValue() > 0);
		QueryLocationMapDTO orgQuery2 = selected.getLocationStatuses().get(1);
		assertNotNull(orgQuery2.getId());
		assertTrue(orgQuery2.getId().longValue() > 0);
		assertTrue(orgQuery1.getId().longValue() != orgQuery2.getId().longValue());
		
		Calendar endCal = Calendar.getInstance();
		endCal.setTime(orgQuery1.getStartDate());
		endCal.add(Calendar.HOUR, 1);
		orgQuery1.setEndDate(endCal.getTime());
		orgQuery1.setStatus(QueryLocationStatus.Successful);
		queryDao.updateQueryLocationMap(orgQuery1);
				
		endCal = Calendar.getInstance();
		endCal.setTime(orgQuery2.getStartDate());
		endCal.add(Calendar.HOUR, 2);
		orgQuery2.setEndDate(endCal.getTime());
		orgQuery2.setStatus(QueryLocationStatus.Successful);
		queryDao.updateQueryLocationMap(orgQuery2);
				
		//get the stats	
		//set the start filter to be 5 seconds after the latest of the two queries
		Calendar startFilter = Calendar.getInstance();
		startFilter.setTime(orgQuery1.getStartDate().getTime() > orgQuery2.getStartDate().getTime() ? orgQuery1.getStartDate() : orgQuery2.getStartDate());
		startFilter.add(Calendar.SECOND, 5);
		//set the end filter to be 5 seconds before the earliest of the two queries
		Calendar endFilter = Calendar.getInstance();
		endFilter.setTime(orgQuery1.getStartDate().getTime() < orgQuery2.getStartDate().getTime() ? orgQuery1.getStartDate() : orgQuery2.getStartDate());
		endFilter.add(Calendar.SECOND, -5);
		
		List<PatientDiscoveryRequestStatisticsEntity> results = statDao.getStatistics(startFilter.getTime(), endFilter.getTime());
		assertNotNull(results);
		assertEquals(2, results.size());
		boolean foundOrg1 = false;
		boolean foundOrg2 = false;
		for(PatientDiscoveryRequestStatisticsEntity result : results) {
			if(result.getLocationName().equals(location1.getName())) {
				foundOrg1 = true;
			} else if(result.getLocationName().equals(location2.getName())) {
				foundOrg2 = true;
			}
		}
		assertTrue(foundOrg1 && foundOrg2);
		
		for(PatientDiscoveryRequestStatisticsEntity result : results) {
			assertNotNull(result.getTotalRequestCount());
			assertEquals(0, result.getTotalRequestCount().longValue());
			assertNull(result.getTotalRequestAverageSeconds());
			assertNotNull(result.getSuccessfulRequestCount());
			assertEquals(0, result.getSuccessfulRequestCount().longValue());
			assertNull(result.getSuccessfulRequestAverageSeconds());
			assertNotNull(result.getFailedRequestCount());
			assertEquals(0, result.getFailedRequestCount().longValue());
			assertNull(result.getFailedRequestAverageSeconds());
			assertNotNull(result.getCancelledRequestCount());
			assertEquals(0, result.getCancelledRequestCount().longValue());
			assertNull(result.getCancelledRequestAverageSeconds());
		}
	}
	
	private void setupQuery() {
		LocationStatusDTO locStatus = new LocationStatusDTO();
		locStatus.setId(1L);
		
		location1 = new LocationDTO();
		location1.setExternalId("1");
		location1.setName("John's Hopkins Medical Center");
		location1.setDescription("A hospital");
		location1.setType("Hospital");
		location1.setExternalLastUpdateDate(new Date());
		location1.setParentOrgName("EHealth Parent Org");
		location1.setStatus(locStatus);
		location1 = locationDao.create(location1);
		
		location2 = new LocationDTO();
		location2.setExternalId("2");
		location2.setName("University of Maryland Medical Center");
		location2.setDescription("A hospital");
		location2.setType("Hospital");
		location2.setExternalLastUpdateDate(new Date());
		location2.setParentOrgName("EHealth Parent Org");
		location2.setStatus(locStatus);
		location2 = locationDao.create(location2);
		
		QueryDTO toInsert = new QueryDTO();
		toInsert.setStatus(QueryStatus.COMPLETE.name());
		toInsert.setTerms("terms");
		toInsert.setUserId("kekey");
		QueryLocationMapDTO orgQuery1 = new QueryLocationMapDTO();
		orgQuery1.setLocationId(location1.getId());
		orgQuery1.setStatus(QueryLocationStatus.Active);
		Calendar startCal = Calendar.getInstance();
		orgQuery1.setStartDate(startCal.getTime());
		toInsert.getLocationStatuses().add(orgQuery1);
		QueryLocationMapDTO orgQuery2 = new QueryLocationMapDTO();
		orgQuery2.setLocationId(location2.getId());
		orgQuery2.setStatus(QueryLocationStatus.Active);
		startCal = Calendar.getInstance();
		orgQuery2.setStartDate(startCal.getTime());
		toInsert.getLocationStatuses().add(orgQuery2);
		
		query = queryDao.create(toInsert);
		assertNotNull(query);
		assertNotNull(query.getId());
		assertTrue(query.getId().longValue() > 0);
		assertNotNull(query.getLocationStatuses());
		assertEquals(2, query.getLocationStatuses().size());
		orgQuery1 = query.getLocationStatuses().get(0);
		assertNotNull(orgQuery1.getId());
		assertTrue(orgQuery1.getId().longValue() > 0);
		orgQuery2 = query.getLocationStatuses().get(1);
		assertNotNull(orgQuery2.getId());
		assertTrue(orgQuery2.getId().longValue() > 0);
	}
}
