package gov.ca.emsa.pulse.broker.dao;

import gov.ca.emsa.pulse.broker.BrokerApplicationTestConfig;
import gov.ca.emsa.pulse.broker.dto.LocationDTO;
import gov.ca.emsa.pulse.broker.dto.QueryDTO;
import gov.ca.emsa.pulse.broker.dto.QueryLocationMapDTO;
import gov.ca.emsa.pulse.common.domain.QueryStatus;

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
	@Autowired OrganizationDAO orgDao;
	private LocationDTO org1;
	private LocationDTO org2;
	
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
		orgQuery1.setLocationId(org1.getId());
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
		orgQuery1.setLocationId(org1.getId());
		orgQuery1.setStatus(QueryLocationStatus.Active);
		toInsert.getLocationStatuses().add(orgQuery1);
		QueryLocationMapDTO orgQuery2 = new QueryLocationMapDTO();
		orgQuery2.setLocationId(org2.getId());
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
		orgQuery1.setLocationId(org1.getId());
		orgQuery1.setStatus(QueryLocationStatus.Active);
		toInsert.getLocationStatuses().add(orgQuery1);
		QueryLocationMapDTO orgQuery2 = new QueryLocationMapDTO();
		orgQuery2.setLocationId(org2.getId());
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
		org1 = new LocationDTO();
		org1.setLocationId(1L);
		org1.setName("IHE Org");
		org1.setAdapter("IHE");
		org1.setEndpointUrl("http://www.localhost.com");
		org1.setPassword("pwd");
		org1.setUsername("kekey");
		org1.setActive(true);
		org1 = orgDao.create(org1);
		
		org2 = new LocationDTO();
		org2.setLocationId(2L);
		org2.setName("eHealth Org");
		org2.setAdapter("eHealth");
		org2.setEndpointUrl("http://www.localhost.com");
		org2.setPassword("pwd");
		org2.setUsername("kekey");
		org2.setActive(true);
		org2 = orgDao.create(org2);
	}
}
