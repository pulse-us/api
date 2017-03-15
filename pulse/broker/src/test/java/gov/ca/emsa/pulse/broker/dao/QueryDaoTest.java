package gov.ca.emsa.pulse.broker.dao;

import gov.ca.emsa.pulse.broker.BrokerApplicationTestConfig;
import gov.ca.emsa.pulse.broker.dto.LocationDTO;
import gov.ca.emsa.pulse.broker.dto.LocationStatusDTO;
import gov.ca.emsa.pulse.broker.dto.QueryDTO;
import gov.ca.emsa.pulse.broker.dto.QueryEndpointMapDTO;
import gov.ca.emsa.pulse.common.domain.QueryStatus;

import java.util.ArrayList;
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

import gov.ca.emsa.pulse.common.domain.QueryEndpointStatus;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={BrokerApplicationTestConfig.class})
public class QueryDaoTest extends TestCase {

	@Autowired QueryDAO queryDao;
	@Autowired LocationDAO locationDao;
	private LocationDTO location1;
	private LocationDTO location2;
	
	@Test
	@Transactional
	@Rollback(true)
	public void testInsertQueryWithoutOrgs() {
		QueryDTO toInsert = new QueryDTO();
		toInsert.setStatus(QueryStatus.Active);
		toInsert.setTerms("terms");
		toInsert.setUserId("keekey");
		QueryDTO inserted = queryDao.create(toInsert);
		
		assertNotNull(inserted);
		assertNotNull(inserted.getId());
		assertTrue(inserted.getId().longValue() > 0);
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void testInsertQueryWithOrg() {
		insertOrganizations();
		
		QueryDTO toInsert = new QueryDTO();
		toInsert.setStatus(QueryStatus.Active);
		toInsert.setTerms("terms");
		toInsert.setUserId("kekey");
		
		QueryEndpointMapDTO orgQuery1 = new QueryEndpointMapDTO();
		orgQuery1.setEndpointId(location1.getId());
		orgQuery1.setStatus(QueryEndpointStatus.Active);
		toInsert.getEndpointStatuses().add(orgQuery1);
		
		QueryDTO inserted = queryDao.create(toInsert);
		assertNotNull(inserted);
		assertNotNull(inserted.getId());
		assertTrue(inserted.getId().longValue() > 0);
		assertNotNull(inserted.getEndpointStatuses());
		assertEquals(1, inserted.getEndpointStatuses().size());
		orgQuery1 = inserted.getEndpointStatuses().get(0);
		assertNotNull(inserted.getEndpointStatuses().get(0).getId());
		assertTrue(inserted.getEndpointStatuses().get(0).getId().longValue() > 0);
		
		QueryDTO selected = queryDao.getById(inserted.getId());
		assertNotNull(selected);
		assertNotNull(selected.getId());
		assertTrue(selected.getId().longValue() > 0);
		assertNotNull(selected.getEndpointStatuses());
		assertEquals(1, selected.getEndpointStatuses().size());
		orgQuery1 = selected.getEndpointStatuses().get(0);
		assertNotNull(orgQuery1.getId());
		assertTrue(orgQuery1.getId().longValue() > 0);
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void testInsertQueryWithTwoOrgs() {		
		insertOrganizations();

		QueryDTO toInsert = new QueryDTO();
		toInsert.setStatus(QueryStatus.Active);
		toInsert.setTerms("terms");
		toInsert.setUserId("kekey");
		QueryEndpointMapDTO orgQuery1 = new QueryEndpointMapDTO();
		orgQuery1.setEndpointId(location1.getId());
		orgQuery1.setStatus(QueryEndpointStatus.Active);
		toInsert.getEndpointStatuses().add(orgQuery1);
		QueryEndpointMapDTO orgQuery2 = new QueryEndpointMapDTO();
		orgQuery2.setEndpointId(location2.getId());
		orgQuery2.setStatus(QueryEndpointStatus.Active);
		toInsert.getEndpointStatuses().add(orgQuery2);
		
		QueryDTO inserted = queryDao.create(toInsert);
		assertNotNull(inserted);
		assertNotNull(inserted.getId());
		assertTrue(inserted.getId().longValue() > 0);
		assertNotNull(inserted.getEndpointStatuses());
		assertEquals(2, inserted.getEndpointStatuses().size());
		orgQuery1 = inserted.getEndpointStatuses().get(0);
		assertNotNull(orgQuery1.getId());
		assertTrue(orgQuery1.getId().longValue() > 0);
		orgQuery2 = inserted.getEndpointStatuses().get(1);
		assertNotNull(orgQuery2.getId());
		assertTrue(orgQuery2.getId().longValue() > 0);

		QueryDTO selected = queryDao.getById(inserted.getId());
		assertNotNull(selected);
		assertNotNull(selected.getId());
		assertTrue(selected.getId().longValue() > 0);
		assertNotNull(selected.getEndpointStatuses());
		assertEquals(2, selected.getEndpointStatuses().size());
		orgQuery1 = selected.getEndpointStatuses().get(0);
		assertNotNull(orgQuery1.getId());
		assertTrue(orgQuery1.getId().longValue() > 0);
		orgQuery2 = selected.getEndpointStatuses().get(0);
		assertNotNull(orgQuery2.getId());
		assertTrue(orgQuery2.getId().longValue() > 0);
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void testGetAllQueries() {		
		insertOrganizations();

		QueryDTO toInsert = new QueryDTO();
		toInsert.setStatus(QueryStatus.Active);
		toInsert.setTerms("terms");
		toInsert.setUserId("kekey");
		QueryEndpointMapDTO orgQuery1 = new QueryEndpointMapDTO();
		orgQuery1.setEndpointId(location1.getId());
		orgQuery1.setStatus(QueryEndpointStatus.Active);
		toInsert.getEndpointStatuses().add(orgQuery1);
		QueryEndpointMapDTO orgQuery2 = new QueryEndpointMapDTO();
		orgQuery2.setEndpointId(location2.getId());
		orgQuery2.setStatus(QueryEndpointStatus.Active);
		toInsert.getEndpointStatuses().add(orgQuery2);
		
		QueryDTO inserted = queryDao.create(toInsert);
		assertNotNull(inserted);
		assertNotNull(inserted.getId());
		assertTrue(inserted.getId().longValue() > 0);
		assertNotNull(inserted.getEndpointStatuses());
		assertEquals(2, inserted.getEndpointStatuses().size());
		orgQuery1 = inserted.getEndpointStatuses().get(0);
		assertNotNull(orgQuery1.getId());
		assertTrue(orgQuery1.getId().longValue() > 0);
		orgQuery2 = inserted.getEndpointStatuses().get(0);
		assertNotNull(orgQuery2.getId());
		assertTrue(orgQuery2.getId().longValue() > 0);
		
		List<QueryDTO> selected = queryDao.findAllForUser("kekey");
		assertNotNull(selected);
		assertEquals(1, selected.size());
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void testGetOpenQueries() {		
		insertOrganizations();

		QueryDTO toInsert = new QueryDTO();
		toInsert.setStatus(QueryStatus.Active);
		toInsert.setTerms("terms");
		toInsert.setUserId("kekey");
		QueryEndpointMapDTO orgQuery1 = new QueryEndpointMapDTO();
		orgQuery1.setEndpointId(location1.getId());
		orgQuery1.setStatus(QueryEndpointStatus.Active);
		toInsert.getEndpointStatuses().add(orgQuery1);
		QueryEndpointMapDTO orgQuery2 = new QueryEndpointMapDTO();
		orgQuery2.setEndpointId(location2.getId());
		orgQuery2.setStatus(QueryEndpointStatus.Active);
		toInsert.getEndpointStatuses().add(orgQuery2);
		
		QueryDTO inserted = queryDao.create(toInsert);
		assertNotNull(inserted);
		assertNotNull(inserted.getId());
		
		List<QueryStatus> statuses = new ArrayList<QueryStatus>();
		statuses.add(QueryStatus.Active);
		statuses.add(QueryStatus.Complete);
		List<QueryDTO> selected = queryDao.findAllForUserWithStatus("kekey", statuses);
		assertNotNull(selected);
		assertEquals(1, selected.size());
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void testUpdateQuery() {
		insertOrganizations();
		
		QueryDTO toInsert = new QueryDTO();
		toInsert.setStatus(QueryStatus.Active);
		toInsert.setTerms("terms");
		toInsert.setUserId("kekey");
		QueryDTO inserted = queryDao.create(toInsert);
		QueryDTO selected = queryDao.getById(inserted.getId());
		
		assertNotNull(selected);
		assertNotNull(selected.getId());
		assertTrue(selected.getId().longValue() > 0);
		assertEquals(0, selected.getEndpointStatuses().size());
		
		selected.setStatus(QueryStatus.Complete);
		selected = queryDao.update(selected);
		assertEquals(QueryStatus.Complete.name(), selected.getStatus().name());
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void testCloseQuery() {
		insertOrganizations();
		
		QueryDTO toInsert = new QueryDTO();
		toInsert.setStatus(QueryStatus.Active);
		toInsert.setTerms("terms");
		toInsert.setUserId("kekey");
		QueryDTO inserted = queryDao.create(toInsert);
		QueryDTO selected = queryDao.getById(inserted.getId());
		
		assertNotNull(selected);
		assertNotNull(selected.getId());
		assertTrue(selected.getId().longValue() > 0);
		assertEquals(0, selected.getEndpointStatuses().size());
		
		queryDao.close(selected.getId());
		
		selected = queryDao.getById(selected.getId());
		assertEquals(QueryStatus.Closed.name(), selected.getStatus().name());
	}
	
	private void insertOrganizations() {
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
	}
}
