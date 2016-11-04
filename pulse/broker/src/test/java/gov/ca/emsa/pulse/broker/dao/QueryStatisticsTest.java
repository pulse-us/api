package gov.ca.emsa.pulse.broker.dao;

import gov.ca.emsa.pulse.broker.BrokerApplicationTestConfig;
import gov.ca.emsa.pulse.broker.dto.OrganizationDTO;
import gov.ca.emsa.pulse.broker.dto.QueryDTO;
import gov.ca.emsa.pulse.broker.dto.QueryOrganizationDTO;
import gov.ca.emsa.pulse.broker.entity.PatientDiscoveryRequestStatisticsEntity;
import gov.ca.emsa.pulse.common.domain.QueryStatus;

import java.util.Calendar;
import java.util.List;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import gov.ca.emsa.pulse.common.domain.QueryOrganizationStatus;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={BrokerApplicationTestConfig.class})
public class QueryStatisticsTest extends TestCase {

	@Autowired OrganizationDAO orgDao;
	@Autowired QueryDAO queryDao;
	@Autowired PatientDiscoveryQueryStatisticsDAO statDao;
	
	private OrganizationDTO org1;
	private OrganizationDTO org2;
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
		assertNotNull(selected.getOrgStatuses());
		assertEquals(2, selected.getOrgStatuses().size());
		QueryOrganizationDTO orgQuery1 = selected.getOrgStatuses().get(0);
		assertNotNull(orgQuery1.getId());
		assertTrue(orgQuery1.getId().longValue() > 0);
		QueryOrganizationDTO orgQuery2 = selected.getOrgStatuses().get(1);
		assertNotNull(orgQuery2.getId());
		assertTrue(orgQuery2.getId().longValue() > 0);
		assertTrue(orgQuery1.getId().longValue() != orgQuery2.getId().longValue());
		
		Calendar endCal = Calendar.getInstance();
		endCal.setTime(orgQuery1.getStartDate());
		endCal.add(Calendar.HOUR, 1);
		orgQuery1.setEndDate(endCal.getTime());
		orgQuery1.setStatus(QueryOrganizationStatus.Successful);
		queryDao.updateQueryOrganization(orgQuery1);
				
		endCal = Calendar.getInstance();
		endCal.setTime(orgQuery2.getStartDate());
		endCal.add(Calendar.HOUR, 2);
		orgQuery2.setEndDate(endCal.getTime());
		orgQuery2.setStatus(QueryOrganizationStatus.Successful);
		queryDao.updateQueryOrganization(orgQuery2);
				
		//get the stats		
		List<PatientDiscoveryRequestStatisticsEntity> results = statDao.getStatistics(null, null);
		assertNotNull(results);
		assertEquals(2, results.size());
		boolean foundOrg1 = false;
		boolean foundOrg2 = false;
		for(PatientDiscoveryRequestStatisticsEntity result : results) {
			if(result.getOrganizationName().equals(org1.getName())) {
				foundOrg1 = true;
			} else if(result.getOrganizationName().equals(org2.getName())) {
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
		assertNotNull(selected.getOrgStatuses());
		assertEquals(2, selected.getOrgStatuses().size());
		QueryOrganizationDTO orgQuery1 = selected.getOrgStatuses().get(0);
		assertNotNull(orgQuery1.getId());
		assertTrue(orgQuery1.getId().longValue() > 0);
		QueryOrganizationDTO orgQuery2 = selected.getOrgStatuses().get(1);
		assertNotNull(orgQuery2.getId());
		assertTrue(orgQuery2.getId().longValue() > 0);
		assertTrue(orgQuery1.getId().longValue() != orgQuery2.getId().longValue());
		
		Calendar endCal = Calendar.getInstance();
		endCal.setTime(orgQuery1.getStartDate());
		endCal.add(Calendar.HOUR, 1);
		orgQuery1.setEndDate(endCal.getTime());
		orgQuery1.setStatus(QueryOrganizationStatus.Successful);
		queryDao.updateQueryOrganization(orgQuery1);
				
		endCal = Calendar.getInstance();
		endCal.setTime(orgQuery2.getStartDate());
		endCal.add(Calendar.HOUR, 2);
		orgQuery2.setEndDate(endCal.getTime());
		orgQuery2.setStatus(QueryOrganizationStatus.Successful);
		queryDao.updateQueryOrganization(orgQuery2);
				
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
			if(result.getOrganizationName().equals(org1.getName())) {
				foundOrg1 = true;
			} else if(result.getOrganizationName().equals(org2.getName())) {
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
		assertNotNull(selected.getOrgStatuses());
		assertEquals(2, selected.getOrgStatuses().size());
		QueryOrganizationDTO orgQuery1 = selected.getOrgStatuses().get(0);
		assertNotNull(orgQuery1.getId());
		assertTrue(orgQuery1.getId().longValue() > 0);
		QueryOrganizationDTO orgQuery2 = selected.getOrgStatuses().get(1);
		assertNotNull(orgQuery2.getId());
		assertTrue(orgQuery2.getId().longValue() > 0);
		assertTrue(orgQuery1.getId().longValue() != orgQuery2.getId().longValue());
		
		Calendar endCal = Calendar.getInstance();
		endCal.setTime(orgQuery1.getStartDate());
		endCal.add(Calendar.HOUR, 1);
		orgQuery1.setEndDate(endCal.getTime());
		orgQuery1.setStatus(QueryOrganizationStatus.Successful);
		queryDao.updateQueryOrganization(orgQuery1);
				
		endCal = Calendar.getInstance();
		endCal.setTime(orgQuery2.getStartDate());
		endCal.add(Calendar.HOUR, 2);
		orgQuery2.setEndDate(endCal.getTime());
		orgQuery2.setStatus(QueryOrganizationStatus.Successful);
		queryDao.updateQueryOrganization(orgQuery2);
				
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
			if(result.getOrganizationName().equals(org1.getName())) {
				foundOrg1 = true;
			} else if(result.getOrganizationName().equals(org2.getName())) {
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
		assertNotNull(selected.getOrgStatuses());
		assertEquals(2, selected.getOrgStatuses().size());
		QueryOrganizationDTO orgQuery1 = selected.getOrgStatuses().get(0);
		assertNotNull(orgQuery1.getId());
		assertTrue(orgQuery1.getId().longValue() > 0);
		QueryOrganizationDTO orgQuery2 = selected.getOrgStatuses().get(1);
		assertNotNull(orgQuery2.getId());
		assertTrue(orgQuery2.getId().longValue() > 0);
		assertTrue(orgQuery1.getId().longValue() != orgQuery2.getId().longValue());
		
		Calendar endCal = Calendar.getInstance();
		endCal.setTime(orgQuery1.getStartDate());
		endCal.add(Calendar.HOUR, 1);
		orgQuery1.setEndDate(endCal.getTime());
		orgQuery1.setStatus(QueryOrganizationStatus.Successful);
		queryDao.updateQueryOrganization(orgQuery1);
				
		endCal = Calendar.getInstance();
		endCal.setTime(orgQuery2.getStartDate());
		endCal.add(Calendar.HOUR, 2);
		orgQuery2.setEndDate(endCal.getTime());
		orgQuery2.setStatus(QueryOrganizationStatus.Successful);
		queryDao.updateQueryOrganization(orgQuery2);
				
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
			if(result.getOrganizationName().equals(org1.getName())) {
				foundOrg1 = true;
			} else if(result.getOrganizationName().equals(org2.getName())) {
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
		assertNotNull(selected.getOrgStatuses());
		assertEquals(2, selected.getOrgStatuses().size());
		QueryOrganizationDTO orgQuery1 = selected.getOrgStatuses().get(0);
		assertNotNull(orgQuery1.getId());
		assertTrue(orgQuery1.getId().longValue() > 0);
		QueryOrganizationDTO orgQuery2 = selected.getOrgStatuses().get(1);
		assertNotNull(orgQuery2.getId());
		assertTrue(orgQuery2.getId().longValue() > 0);
		assertTrue(orgQuery1.getId().longValue() != orgQuery2.getId().longValue());
		
		Calendar endCal = Calendar.getInstance();
		endCal.setTime(orgQuery1.getStartDate());
		endCal.add(Calendar.HOUR, 1);
		orgQuery1.setEndDate(endCal.getTime());
		orgQuery1.setStatus(QueryOrganizationStatus.Successful);
		queryDao.updateQueryOrganization(orgQuery1);
				
		endCal = Calendar.getInstance();
		endCal.setTime(orgQuery2.getStartDate());
		endCal.add(Calendar.HOUR, 2);
		orgQuery2.setEndDate(endCal.getTime());
		orgQuery2.setStatus(QueryOrganizationStatus.Successful);
		queryDao.updateQueryOrganization(orgQuery2);
				
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
			if(result.getOrganizationName().equals(org1.getName())) {
				foundOrg1 = true;
			} else if(result.getOrganizationName().equals(org2.getName())) {
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
		org1 = new OrganizationDTO();
		org1.setOrganizationId(1L);
		org1.setName("IHE Org");
		org1.setAdapter("IHE");
		org1.setEndpointUrl("http://www.localhost.com");
		org1.setPassword("pwd");
		org1.setUsername("kekey");
		org1.setActive(true);
		org1 = orgDao.create(org1);
		
		org2 = new OrganizationDTO();
		org2.setOrganizationId(2L);
		org2.setName("eHealth Org");
		org2.setAdapter("eHealth");
		org2.setEndpointUrl("http://www.localhost.com");
		org2.setPassword("pwd");
		org2.setUsername("kekey");
		org2.setActive(true);
		org2 = orgDao.create(org2);
		
		QueryDTO toInsert = new QueryDTO();
		toInsert.setStatus(QueryStatus.COMPLETE.name());
		toInsert.setTerms("terms");
		toInsert.setUserId("kekey");
		QueryOrganizationDTO orgQuery1 = new QueryOrganizationDTO();
		orgQuery1.setOrgId(org1.getId());
		orgQuery1.setStatus(QueryOrganizationStatus.Active);
		Calendar startCal = Calendar.getInstance();
		orgQuery1.setStartDate(startCal.getTime());
		toInsert.getOrgStatuses().add(orgQuery1);
		QueryOrganizationDTO orgQuery2 = new QueryOrganizationDTO();
		orgQuery2.setOrgId(org2.getId());
		orgQuery2.setStatus(QueryOrganizationStatus.Active);
		startCal = Calendar.getInstance();
		orgQuery2.setStartDate(startCal.getTime());
		toInsert.getOrgStatuses().add(orgQuery2);
		
		query = queryDao.create(toInsert);
		assertNotNull(query);
		assertNotNull(query.getId());
		assertTrue(query.getId().longValue() > 0);
		assertNotNull(query.getOrgStatuses());
		assertEquals(2, query.getOrgStatuses().size());
		orgQuery1 = query.getOrgStatuses().get(0);
		assertNotNull(orgQuery1.getId());
		assertTrue(orgQuery1.getId().longValue() > 0);
		orgQuery2 = query.getOrgStatuses().get(1);
		assertNotNull(orgQuery2.getId());
		assertTrue(orgQuery2.getId().longValue() > 0);
	}
}
