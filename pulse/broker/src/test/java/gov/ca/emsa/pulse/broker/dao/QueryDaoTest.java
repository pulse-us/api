package gov.ca.emsa.pulse.broker.dao;

import gov.ca.emsa.pulse.broker.BrokerApplicationTestConfig;
import gov.ca.emsa.pulse.broker.dto.LocationDTO;
import gov.ca.emsa.pulse.broker.dto.LocationStatusDTO;
import gov.ca.emsa.pulse.broker.dto.QueryDTO;
import gov.ca.emsa.pulse.broker.dto.QueryLocationMapDTO;
import gov.ca.emsa.pulse.common.domain.QueryStatus;

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
		toInsert.setStatus(QueryStatus.ACTIVE.name());
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
		toInsert.setStatus(QueryStatus.ACTIVE.name());
		toInsert.setTerms("terms");
		toInsert.setUserId("kekey");
		
		QueryLocationMapDTO orgQuery1 = new QueryLocationMapDTO();
		orgQuery1.setLocationId(location1.getId());
		orgQuery1.setStatus(QueryLocationStatus.Active);
		toInsert.getLocationStatuses().add(orgQuery1);
		
		QueryDTO inserted = queryDao.create(toInsert);
		assertNotNull(inserted);
		assertNotNull(inserted.getId());
		assertTrue(inserted.getId().longValue() > 0);
		assertNotNull(inserted.getLocationStatuses());
		assertEquals(1, inserted.getLocationStatuses().size());
		orgQuery1 = inserted.getLocationStatuses().get(0);
		assertNotNull(inserted.getLocationStatuses().get(0).getId());
		assertTrue(inserted.getLocationStatuses().get(0).getId().longValue() > 0);
		
		QueryDTO selected = queryDao.getById(inserted.getId());
		assertNotNull(selected);
		assertNotNull(selected.getId());
		assertTrue(selected.getId().longValue() > 0);
		assertNotNull(selected.getLocationStatuses());
		assertEquals(1, selected.getLocationStatuses().size());
		orgQuery1 = selected.getLocationStatuses().get(0);
		assertNotNull(orgQuery1.getId());
		assertTrue(orgQuery1.getId().longValue() > 0);
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void testInsertQueryWithTwoOrgs() {		
		insertOrganizations();

		QueryDTO toInsert = new QueryDTO();
		toInsert.setStatus(QueryStatus.ACTIVE.name());
		toInsert.setTerms("terms");
		toInsert.setUserId("kekey");
		QueryLocationMapDTO orgQuery1 = new QueryLocationMapDTO();
		orgQuery1.setLocationId(location1.getId());
		orgQuery1.setStatus(QueryLocationStatus.Active);
		toInsert.getLocationStatuses().add(orgQuery1);
		QueryLocationMapDTO orgQuery2 = new QueryLocationMapDTO();
		orgQuery2.setLocationId(location2.getId());
		orgQuery2.setStatus(QueryLocationStatus.Active);
		toInsert.getLocationStatuses().add(orgQuery2);
		
		QueryDTO inserted = queryDao.create(toInsert);
		assertNotNull(inserted);
		assertNotNull(inserted.getId());
		assertTrue(inserted.getId().longValue() > 0);
		assertNotNull(inserted.getLocationStatuses());
		assertEquals(2, inserted.getLocationStatuses().size());
		orgQuery1 = inserted.getLocationStatuses().get(0);
		assertNotNull(orgQuery1.getId());
		assertTrue(orgQuery1.getId().longValue() > 0);
		orgQuery2 = inserted.getLocationStatuses().get(1);
		assertNotNull(orgQuery2.getId());
		assertTrue(orgQuery2.getId().longValue() > 0);

		QueryDTO selected = queryDao.getById(inserted.getId());
		assertNotNull(selected);
		assertNotNull(selected.getId());
		assertTrue(selected.getId().longValue() > 0);
		assertNotNull(selected.getLocationStatuses());
		assertEquals(2, selected.getLocationStatuses().size());
		orgQuery1 = selected.getLocationStatuses().get(0);
		assertNotNull(orgQuery1.getId());
		assertTrue(orgQuery1.getId().longValue() > 0);
		orgQuery2 = selected.getLocationStatuses().get(0);
		assertNotNull(orgQuery2.getId());
		assertTrue(orgQuery2.getId().longValue() > 0);
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void testGetAllQueries() {		
		insertOrganizations();

		QueryDTO toInsert = new QueryDTO();
		toInsert.setStatus(QueryStatus.ACTIVE.name());
		toInsert.setTerms("terms");
		toInsert.setUserId("kekey");
		QueryLocationMapDTO orgQuery1 = new QueryLocationMapDTO();
		orgQuery1.setLocationId(location1.getId());
		orgQuery1.setStatus(QueryLocationStatus.Active);
		toInsert.getLocationStatuses().add(orgQuery1);
		QueryLocationMapDTO orgQuery2 = new QueryLocationMapDTO();
		orgQuery2.setLocationId(location2.getId());
		orgQuery2.setStatus(QueryLocationStatus.Active);
		toInsert.getLocationStatuses().add(orgQuery2);
		
		QueryDTO inserted = queryDao.create(toInsert);
		assertNotNull(inserted);
		assertNotNull(inserted.getId());
		assertTrue(inserted.getId().longValue() > 0);
		assertNotNull(inserted.getLocationStatuses());
		assertEquals(2, inserted.getLocationStatuses().size());
		orgQuery1 = inserted.getLocationStatuses().get(0);
		assertNotNull(orgQuery1.getId());
		assertTrue(orgQuery1.getId().longValue() > 0);
		orgQuery2 = inserted.getLocationStatuses().get(0);
		assertNotNull(orgQuery2.getId());
		assertTrue(orgQuery2.getId().longValue() > 0);
		
		List<QueryDTO> selected = queryDao.findAllForUser("kekey");
		assertNotNull(selected);
		assertEquals(1, selected.size());
//		assertNotNull(selected.getId());
//		assertTrue(selected.getId().longValue() > 0);
//		assertNotNull(selected.getOrgStatuses());
//		assertEquals(2, selected.getOrgStatuses().size());
//		orgQuery1 = selected.getOrgStatuses().get(0);
//		assertNotNull(orgQuery1.getId());
//		assertTrue(orgQuery1.getId().longValue() > 0);
//		orgQuery2 = selected.getOrgStatuses().get(0);
//		assertNotNull(orgQuery2.getId());
//		assertTrue(orgQuery2.getId().longValue() > 0);
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void testUpdateQuery() {
		insertOrganizations();
		
		QueryDTO toInsert = new QueryDTO();
		toInsert.setStatus(QueryStatus.ACTIVE.name());
		toInsert.setTerms("terms");
		toInsert.setUserId("kekey");
		QueryDTO inserted = queryDao.create(toInsert);
		QueryDTO selected = queryDao.getById(inserted.getId());
		
		assertNotNull(selected);
		assertNotNull(selected.getId());
		assertTrue(selected.getId().longValue() > 0);
		assertEquals(0, selected.getLocationStatuses().size());
		
		selected.setStatus(QueryStatus.COMPLETE.name());
		selected = queryDao.update(selected);
		assertEquals(QueryStatus.COMPLETE.name(), selected.getStatus());
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
