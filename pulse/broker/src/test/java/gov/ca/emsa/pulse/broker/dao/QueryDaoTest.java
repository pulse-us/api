package gov.ca.emsa.pulse.broker.dao;

import gov.ca.emsa.pulse.broker.BrokerApplicationTestConfig;
import gov.ca.emsa.pulse.broker.dto.EndpointDTO;
import gov.ca.emsa.pulse.broker.dto.EndpointStatusDTO;
import gov.ca.emsa.pulse.broker.dto.EndpointTypeDTO;
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
	@Autowired EndpointDAO endpointDao;
	@Autowired LocationEndpointMapDAO mappingDao;
	private LocationDTO location1, location2;
	private EndpointDTO endpoint1, endpoint2;
	
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
	public void testInsertQueryWithEndpoint() {
		insertLocationsAndEndpoints();
		
		QueryDTO toInsert = new QueryDTO();
		toInsert.setStatus(QueryStatus.Active);
		toInsert.setTerms("terms");
		toInsert.setUserId("kekey");
		
		QueryEndpointMapDTO queryEndpointMap1 = new QueryEndpointMapDTO();
		queryEndpointMap1.setEndpointId(endpoint1.getId());
		queryEndpointMap1.setStatus(QueryEndpointStatus.Active);
		toInsert.getEndpointMaps().add(queryEndpointMap1);
		
		QueryDTO inserted = queryDao.create(toInsert);
		assertNotNull(inserted);
		assertNotNull(inserted.getId());
		assertTrue(inserted.getId().longValue() > 0);
		assertNotNull(inserted.getEndpointMaps());
		assertEquals(1, inserted.getEndpointMaps().size());
		queryEndpointMap1 = inserted.getEndpointMaps().get(0);
		assertNotNull(inserted.getEndpointMaps().get(0).getId());
		assertTrue(inserted.getEndpointMaps().get(0).getId().longValue() > 0);
		
		QueryDTO selected = queryDao.findById(inserted.getId());
		assertNotNull(selected);
		assertNotNull(selected.getId());
		assertTrue(selected.getId().longValue() > 0);
		assertNotNull(selected.getEndpointMaps());
		assertEquals(1, selected.getEndpointMaps().size());
		queryEndpointMap1 = selected.getEndpointMaps().get(0);
		assertNotNull(queryEndpointMap1.getId());
		assertTrue(queryEndpointMap1.getId().longValue() > 0);
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void testInsertQueryWithTwoEndpoints() {		
		insertLocationsAndEndpoints();

		QueryDTO toInsert = new QueryDTO();
		toInsert.setStatus(QueryStatus.Active);
		toInsert.setTerms("terms");
		toInsert.setUserId("kekey");
		QueryEndpointMapDTO queryEndpointMap1 = new QueryEndpointMapDTO();
		queryEndpointMap1.setEndpointId(endpoint1.getId());
		queryEndpointMap1.setStatus(QueryEndpointStatus.Active);
		toInsert.getEndpointMaps().add(queryEndpointMap1);
		QueryEndpointMapDTO queryEndpointMap2 = new QueryEndpointMapDTO();
		queryEndpointMap2.setEndpointId(endpoint2.getId());
		queryEndpointMap2.setStatus(QueryEndpointStatus.Active);
		toInsert.getEndpointMaps().add(queryEndpointMap2);
		
		QueryDTO inserted = queryDao.create(toInsert);
		assertNotNull(inserted);
		assertNotNull(inserted.getId());
		assertTrue(inserted.getId().longValue() > 0);
		assertNotNull(inserted.getEndpointMaps());
		assertEquals(2, inserted.getEndpointMaps().size());
		queryEndpointMap1 = inserted.getEndpointMaps().get(0);
		assertNotNull(queryEndpointMap1.getId());
		assertTrue(queryEndpointMap1.getId().longValue() > 0);
		queryEndpointMap2 = inserted.getEndpointMaps().get(1);
		assertNotNull(queryEndpointMap2.getId());
		assertTrue(queryEndpointMap2.getId().longValue() > 0);

		QueryDTO selected = queryDao.findById(inserted.getId());
		assertNotNull(selected);
		assertNotNull(selected.getId());
		assertTrue(selected.getId().longValue() > 0);
		assertNotNull(selected.getEndpointMaps());
		assertEquals(2, selected.getEndpointMaps().size());
		queryEndpointMap1 = selected.getEndpointMaps().get(0);
		assertNotNull(queryEndpointMap1.getId());
		assertTrue(queryEndpointMap1.getId().longValue() > 0);
		queryEndpointMap2 = selected.getEndpointMaps().get(0);
		assertNotNull(queryEndpointMap2.getId());
		assertTrue(queryEndpointMap2.getId().longValue() > 0);
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void testGetAllQueries() {		
		insertLocationsAndEndpoints();

		QueryDTO toInsert = new QueryDTO();
		toInsert.setStatus(QueryStatus.Active);
		toInsert.setTerms("terms");
		toInsert.setUserId("kekey");
		QueryEndpointMapDTO queryEndpointMap1 = new QueryEndpointMapDTO();
		queryEndpointMap1.setEndpointId(endpoint1.getId());
		queryEndpointMap1.setStatus(QueryEndpointStatus.Active);
		toInsert.getEndpointMaps().add(queryEndpointMap1);
		QueryEndpointMapDTO queryEndpointMap2 = new QueryEndpointMapDTO();
		queryEndpointMap2.setEndpointId(endpoint2.getId());
		queryEndpointMap2.setStatus(QueryEndpointStatus.Active);
		toInsert.getEndpointMaps().add(queryEndpointMap2);
		
		QueryDTO inserted = queryDao.create(toInsert);
		assertNotNull(inserted);
		assertNotNull(inserted.getId());
		assertTrue(inserted.getId().longValue() > 0);
		assertNotNull(inserted.getEndpointMaps());
		assertEquals(2, inserted.getEndpointMaps().size());
		queryEndpointMap1 = inserted.getEndpointMaps().get(0);
		assertNotNull(queryEndpointMap1.getId());
		assertTrue(queryEndpointMap1.getId().longValue() > 0);
		queryEndpointMap2 = inserted.getEndpointMaps().get(0);
		assertNotNull(queryEndpointMap2.getId());
		assertTrue(queryEndpointMap2.getId().longValue() > 0);
		
		List<QueryDTO> selected = queryDao.findAllForUser("kekey");
		assertNotNull(selected);
		assertEquals(1, selected.size());
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void testGetQueryEndpointMaps() {		
		insertLocationsAndEndpoints();

		QueryDTO toInsert = new QueryDTO();
		toInsert.setStatus(QueryStatus.Active);
		toInsert.setTerms("terms");
		toInsert.setUserId("kekey");
		QueryEndpointMapDTO queryEndpointMap1 = new QueryEndpointMapDTO();
		queryEndpointMap1.setEndpointId(endpoint1.getId());
		queryEndpointMap1.setStatus(QueryEndpointStatus.Active);
		toInsert.getEndpointMaps().add(queryEndpointMap1);
		QueryEndpointMapDTO queryEndpointMap2 = new QueryEndpointMapDTO();
		queryEndpointMap2.setEndpointId(endpoint2.getId());
		queryEndpointMap2.setStatus(QueryEndpointStatus.Active);
		toInsert.getEndpointMaps().add(queryEndpointMap2);
		
		QueryDTO inserted = queryDao.create(toInsert);
		QueryEndpointMapDTO endpointMapping = queryDao.findQueryEndpointByQueryAndEndpoint(inserted.getId(), endpoint1.getId());
		assertNotNull(endpointMapping);
		assertNotNull(endpointMapping.getId());
		assertTrue(endpointMapping.getId() > 0);
		
		QueryEndpointMapDTO endpointMappingById = queryDao.findQueryEndpointById(endpointMapping.getId());
		assertNotNull(endpointMappingById);
		assertNotNull(endpointMappingById.getId());
		assertTrue(endpointMapping.getId() > 0);
		assertEquals(endpointMapping.getId().longValue(), endpointMappingById.getId().longValue());
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void testGetOpenQueries() {		
		insertLocationsAndEndpoints();

		QueryDTO toInsert = new QueryDTO();
		toInsert.setStatus(QueryStatus.Active);
		toInsert.setTerms("terms");
		toInsert.setUserId("kekey");
		QueryEndpointMapDTO queryEndpointMap1 = new QueryEndpointMapDTO();
		queryEndpointMap1.setEndpointId(endpoint1.getId());
		queryEndpointMap1.setStatus(QueryEndpointStatus.Active);
		toInsert.getEndpointMaps().add(queryEndpointMap1);
		QueryEndpointMapDTO queryEndpointMap2 = new QueryEndpointMapDTO();
		queryEndpointMap2.setEndpointId(endpoint2.getId());
		queryEndpointMap2.setStatus(QueryEndpointStatus.Active);
		toInsert.getEndpointMaps().add(queryEndpointMap2);
		
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
		insertLocationsAndEndpoints();
		
		QueryDTO toInsert = new QueryDTO();
		toInsert.setStatus(QueryStatus.Active);
		toInsert.setTerms("terms");
		toInsert.setUserId("kekey");
		QueryDTO inserted = queryDao.create(toInsert);
		QueryDTO selected = queryDao.findById(inserted.getId());
		
		assertNotNull(selected);
		assertNotNull(selected.getId());
		assertTrue(selected.getId().longValue() > 0);
		assertEquals(0, selected.getEndpointMaps().size());
		
		selected.setStatus(QueryStatus.Complete);
		selected = queryDao.update(selected);
		assertEquals(QueryStatus.Complete.name(), selected.getStatus().name());
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void testCloseQuery() {
		insertLocationsAndEndpoints();
		
		QueryDTO toInsert = new QueryDTO();
		toInsert.setStatus(QueryStatus.Active);
		toInsert.setTerms("terms");
		toInsert.setUserId("kekey");
		QueryDTO inserted = queryDao.create(toInsert);
		QueryDTO selected = queryDao.findById(inserted.getId());
		
		assertNotNull(selected);
		assertNotNull(selected.getId());
		assertTrue(selected.getId().longValue() > 0);
		assertEquals(0, selected.getEndpointMaps().size());
		
		queryDao.close(selected.getId());
		
		selected = queryDao.findById(selected.getId());
		assertEquals(QueryStatus.Closed.name(), selected.getStatus().name());
	}
	
	private void insertLocationsAndEndpoints() {
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
		
		EndpointStatusDTO status = new EndpointStatusDTO();
		status.setId(1L);
		EndpointTypeDTO type = new EndpointTypeDTO();
		type.setId(1L);
		endpoint1 = new EndpointDTO();
		endpoint1.setAdapter("eHealth");
		endpoint1.setEndpointStatus(status);
		endpoint1.setEndpointType(type);
		endpoint1.setExternalId("001");
		endpoint1.setExternalLastUpdateDate(new Date());
		endpoint1.setUrl("http://test.com"); 
		endpoint1 = endpointDao.create(endpoint1);
		
		endpoint2 = new EndpointDTO();
		endpoint2.setAdapter("eHealth");
		endpoint2.setEndpointStatus(status);
		endpoint2.setEndpointType(type);
		endpoint2.setExternalId("002");
		endpoint2.setExternalLastUpdateDate(new Date());
		endpoint2.setUrl("http://test.com"); 
		endpoint2 = endpointDao.create(endpoint1);
		
		mappingDao.create(location1.getId(), endpoint1.getId());
		mappingDao.create(location2.getId(), endpoint1.getId());
		mappingDao.create(location2.getId(), endpoint2.getId());
	}
}
