package gov.ca.emsa.pulse.broker.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import gov.ca.emsa.pulse.broker.BrokerApplicationTestConfig;
import gov.ca.emsa.pulse.broker.dto.QueryDTO;
import gov.ca.emsa.pulse.broker.dto.QueryOrganizationDTO;
import gov.ca.emsa.pulse.broker.dto.QueryStatus;
import junit.framework.TestCase;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={BrokerApplicationTestConfig.class})
public class QueryDaoTest extends TestCase {

	@Autowired QueryDAO queryDao;
	
	@Test
	@Transactional
	public void testInsertQueryWithoutOrgs() {
		QueryDTO toInsert = new QueryDTO();
		toInsert.setStatus(QueryStatus.ACTIVE.name());
		toInsert.setTerms("terms");
		toInsert.setUserToken("kekey");
		QueryDTO inserted = queryDao.create(toInsert);
		
		assertNotNull(inserted);
		assertNotNull(inserted.getId());
		assertTrue(inserted.getId().longValue() > 0);
	}
	
	@Test
	@Transactional
	public void testInsertQueryWithOrg() {
		QueryDTO toInsert = new QueryDTO();
		toInsert.setStatus(QueryStatus.ACTIVE.name());
		toInsert.setTerms("terms");
		toInsert.setUserToken("kekey");
		QueryOrganizationDTO orgQuery1 = new QueryOrganizationDTO();
		orgQuery1.setOrgId(1L);
		orgQuery1.setStatus(QueryStatus.ACTIVE.name());
		toInsert.getOrgStatuses().add(orgQuery1);
		
		QueryDTO inserted = queryDao.create(toInsert);
		
		QueryDTO selected = queryDao.getById(inserted.getId());
		
		assertNotNull(selected);
		assertNotNull(selected.getId());
		assertTrue(selected.getId().longValue() > 0);
		assertNotNull(selected.getOrgStatuses());
		assertEquals(1, selected.getOrgStatuses().size());
		orgQuery1 = selected.getOrgStatuses().get(0);
		assertNotNull(orgQuery1.getId());
		assertTrue(orgQuery1.getId().longValue() > 0);
	}
	
	@Test
	@Transactional
	public void testInsertQueryWithOrgs() {
		QueryDTO toInsert = new QueryDTO();
		toInsert.setStatus(QueryStatus.ACTIVE.name());
		toInsert.setTerms("terms");
		toInsert.setUserToken("kekey");
		QueryOrganizationDTO orgQuery1 = new QueryOrganizationDTO();
		orgQuery1.setOrgId(1L);
		orgQuery1.setStatus(QueryStatus.ACTIVE.name());
		toInsert.getOrgStatuses().add(orgQuery1);
		QueryOrganizationDTO orgQuery2 = new QueryOrganizationDTO();
		orgQuery2.setOrgId(1L);
		orgQuery2.setStatus(QueryStatus.ACTIVE.name());
		toInsert.getOrgStatuses().add(orgQuery2);
		
		QueryDTO inserted = queryDao.create(toInsert);
		
		QueryDTO selected = queryDao.getById(inserted.getId());
		
		assertNotNull(selected);
		assertNotNull(selected.getId());
		assertTrue(selected.getId().longValue() > 0);
		assertNotNull(selected.getOrgStatuses());
		assertEquals(2, selected.getOrgStatuses().size());
		orgQuery1 = selected.getOrgStatuses().get(0);
		assertNotNull(orgQuery1.getId());
		assertTrue(orgQuery1.getId().longValue() > 0);
		orgQuery2 = selected.getOrgStatuses().get(0);
		assertNotNull(orgQuery2.getId());
		assertTrue(orgQuery2.getId().longValue() > 0);
	}
	
	@Test
	@Transactional
	public void testUpdateQuery() {
		QueryDTO toInsert = new QueryDTO();
		toInsert.setStatus(QueryStatus.ACTIVE.name());
		toInsert.setTerms("terms");
		toInsert.setUserToken("kekey");
		QueryDTO inserted = queryDao.create(toInsert);
		QueryDTO selected = queryDao.getById(inserted.getId());
		
		assertNotNull(selected);
		assertNotNull(selected.getId());
		assertTrue(selected.getId().longValue() > 0);
		assertEquals(0, selected.getOrgStatuses().size());
		
		QueryOrganizationDTO orgQuery1 = new QueryOrganizationDTO();
		orgQuery1.setOrgId(1L);
		orgQuery1.setQueryId(selected.getId());
		orgQuery1.setStatus(QueryStatus.ACTIVE.name());
		selected.getOrgStatuses().add(orgQuery1);
		selected = queryDao.update(selected);
		
		assertEquals(1, selected.getOrgStatuses().size());
		orgQuery1 = selected.getOrgStatuses().get(0);
		orgQuery1.setStatus(QueryStatus.COMPLETE.name());
		selected = queryDao.update(selected);
		selected = queryDao.getById(selected.getId());
		orgQuery1 = selected.getOrgStatuses().get(0);
		assertEquals(QueryStatus.COMPLETE.name(), orgQuery1.getStatus());
		assertEquals(QueryStatus.COMPLETE.name(), selected.getStatus());
	}
}
