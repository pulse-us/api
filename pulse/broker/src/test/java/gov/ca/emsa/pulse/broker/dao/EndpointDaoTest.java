package gov.ca.emsa.pulse.broker.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import gov.ca.emsa.pulse.broker.BrokerApplicationTestConfig;
import gov.ca.emsa.pulse.broker.dto.EndpointDTO;
import gov.ca.emsa.pulse.broker.dto.EndpointMimeTypeDTO;
import gov.ca.emsa.pulse.broker.dto.EndpointStatusDTO;
import gov.ca.emsa.pulse.broker.dto.EndpointTypeDTO;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={BrokerApplicationTestConfig.class})
public class EndpointDaoTest {
	@Autowired EndpointDAO endpointDao;
	
	@Test
	@Transactional
	@Rollback(true)
	public void createEndpoint() throws SQLException {
		EndpointDTO toCreate = new EndpointDTO();
		toCreate.setAdapter("eHealth");
		EndpointStatusDTO endpointStatus = new EndpointStatusDTO();
		endpointStatus.setName("Active");
		toCreate.setEndpointStatus(endpointStatus);
		EndpointTypeDTO endpointType = new EndpointTypeDTO();
		endpointType.setCode("nwhin-xcpd");
		toCreate.setEndpointType(endpointType);
		toCreate.setExternalId("001");
		toCreate.setExternalLastUpdateDate(new Date());
		EndpointMimeTypeDTO mimeType1 = new EndpointMimeTypeDTO();
		mimeType1.setMimeType("application/xml");
		toCreate.getMimeTypes().add(mimeType1);
		EndpointMimeTypeDTO mimeType2 = new EndpointMimeTypeDTO();
		mimeType2.setMimeType("text/xml");
		toCreate.getMimeTypes().add(mimeType2);
		toCreate.setPayloadType("XML");
		toCreate.setPublicKey("1236789sdkjfksjhfdkjsdhf889798kjshdfkjshdfi8");
		toCreate.setUrl("https://localhost:3000/some/url");
		
		EndpointDTO created = endpointDao.create(toCreate);

		EndpointDTO queried = endpointDao.findById(created.getId());
		compareEndpoints(created, queried);
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void createAndUpdateEndpointFields() throws SQLException {
		EndpointDTO toCreate = new EndpointDTO();
		toCreate.setAdapter("eHealth");
		EndpointStatusDTO endpointStatus = new EndpointStatusDTO();
		endpointStatus.setName("Active");
		toCreate.setEndpointStatus(endpointStatus);
		EndpointTypeDTO endpointType = new EndpointTypeDTO();
		endpointType.setCode("nwhin-xcpd");
		toCreate.setEndpointType(endpointType);
		toCreate.setExternalId("001");
		toCreate.setExternalLastUpdateDate(new Date());
		EndpointMimeTypeDTO mimeType1 = new EndpointMimeTypeDTO();
		mimeType1.setMimeType("application/xml");
		toCreate.getMimeTypes().add(mimeType1);
		EndpointMimeTypeDTO mimeType2 = new EndpointMimeTypeDTO();
		mimeType2.setMimeType("text/xml");
		toCreate.getMimeTypes().add(mimeType2);
		toCreate.setPayloadType("XML");
		toCreate.setPublicKey("1236789sdkjfksjhfdkjsdhf889798kjshdfkjshdfi8");
		toCreate.setUrl("https://localhost:3000/some/url");
		
		EndpointDTO toUpdate = endpointDao.create(toCreate);
		
		toUpdate.setAdapter("IHE");
		endpointStatus = new EndpointStatusDTO();
		endpointStatus.setName("Off");
		toUpdate.setEndpointStatus(endpointStatus);
		endpointType = new EndpointTypeDTO();
		endpointType.setCode("nwhin-xca-query");
		toUpdate.setEndpointType(endpointType);
		toUpdate.setExternalLastUpdateDate(new Date());
		toUpdate.setPayloadType("JSON");
		toUpdate.setPublicKey("1236789sdkjfksjhfdkjsdhf889798");
		toUpdate.setUrl("https://localhost:3000/modified/url");
		
		EndpointDTO updated = endpointDao.update(toUpdate);
		assertEquals(toUpdate.getId(), updated.getId());
		EndpointDTO queried = endpointDao.findById(updated.getId());
		compareEndpoints(updated, queried);
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void createAndUpdateEndpointClearMimeTypes() throws SQLException {
		EndpointDTO toCreate = new EndpointDTO();
		toCreate.setAdapter("eHealth");
		EndpointStatusDTO endpointStatus = new EndpointStatusDTO();
		endpointStatus.setName("Active");
		toCreate.setEndpointStatus(endpointStatus);
		EndpointTypeDTO endpointType = new EndpointTypeDTO();
		endpointType.setCode("nwhin-xcpd");
		toCreate.setEndpointType(endpointType);
		toCreate.setExternalId("001");
		toCreate.setExternalLastUpdateDate(new Date());
		EndpointMimeTypeDTO mimeType1 = new EndpointMimeTypeDTO();
		mimeType1.setMimeType("application/xml");
		toCreate.getMimeTypes().add(mimeType1);
		EndpointMimeTypeDTO mimeType2 = new EndpointMimeTypeDTO();
		mimeType2.setMimeType("text/xml");
		toCreate.getMimeTypes().add(mimeType2);
		toCreate.setPayloadType("XML");
		toCreate.setPublicKey("1236789sdkjfksjhfdkjsdhf889798kjshdfkjshdfi8");
		toCreate.setUrl("https://localhost:3000/some/url");
		
		EndpointDTO toUpdate = endpointDao.create(toCreate);
		toUpdate.getMimeTypes().clear();
		
		EndpointDTO updated = endpointDao.update(toUpdate);
		assertEquals(toUpdate.getId(), updated.getId());
		EndpointDTO queried = endpointDao.findById(updated.getId());
		compareEndpoints(updated, queried);
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void createAndUpdateEndpointChangeMimeTypes() throws SQLException {
		EndpointDTO toCreate = new EndpointDTO();
		toCreate.setAdapter("eHealth");
		EndpointStatusDTO endpointStatus = new EndpointStatusDTO();
		endpointStatus.setName("Active");
		toCreate.setEndpointStatus(endpointStatus);
		EndpointTypeDTO endpointType = new EndpointTypeDTO();
		endpointType.setCode("nwhin-xcpd");
		toCreate.setEndpointType(endpointType);
		toCreate.setExternalId("001");
		toCreate.setExternalLastUpdateDate(new Date());
		EndpointMimeTypeDTO mimeType1 = new EndpointMimeTypeDTO();
		mimeType1.setMimeType("application/xml");
		toCreate.getMimeTypes().add(mimeType1);
		EndpointMimeTypeDTO mimeType2 = new EndpointMimeTypeDTO();
		mimeType2.setMimeType("text/xml");
		toCreate.getMimeTypes().add(mimeType2);
		toCreate.setPayloadType("XML");
		toCreate.setPublicKey("1236789sdkjfksjhfdkjsdhf889798kjshdfkjshdfi8");
		toCreate.setUrl("https://localhost:3000/some/url");
		
		EndpointDTO toUpdate = endpointDao.create(toCreate);
		toUpdate.getMimeTypes().clear();
		EndpointMimeTypeDTO mimeType3 = new EndpointMimeTypeDTO();
		mimeType3.setMimeType("application-vnd/xml");
		toCreate.getMimeTypes().add(mimeType3);
		
		EndpointDTO updated = endpointDao.update(toUpdate);
		assertEquals(toUpdate.getId(), updated.getId());
		EndpointDTO queried = endpointDao.findById(updated.getId());
		compareEndpoints(updated, queried);
	}
	
	private void compareEndpoints(EndpointDTO first, EndpointDTO second) {
		assertTrue(second.getId().longValue() > 0);
		assertEquals(first.getAdapter(), second.getAdapter());
		assertNotNull(first.getEndpointStatus());
		assertNotNull(second.getEndpointStatus());
		assertEquals(first.getEndpointStatus().getId().longValue(), second.getEndpointStatus().getId().longValue());
		assertEquals(first.getEndpointStatus().getName(), second.getEndpointStatus().getName());
		assertNotNull(first.getEndpointType());
		assertNotNull(second.getEndpointType());
		assertEquals(first.getEndpointType().getId().longValue(), second.getEndpointType().getId().longValue());
		assertEquals(first.getEndpointType().getCode(), second.getEndpointType().getCode());
		assertEquals(first.getEndpointType().getName(), second.getEndpointType().getName());
		assertEquals(first.getExternalId(), second.getExternalId());
		assertEquals(first.getExternalLastUpdateDate(), second.getExternalLastUpdateDate());
		assertEquals(first.getId().longValue(), second.getId().longValue());
		for(EndpointMimeTypeDTO mimeType : first.getMimeTypes()) {
			boolean hasCheckedMimeType = false;
			for(EndpointMimeTypeDTO otherMimeType : second.getMimeTypes()) {
				if(mimeType.getId().longValue() == otherMimeType.getId().longValue()) {
					hasCheckedMimeType = true;
					assertEquals(mimeType.getMimeType(), otherMimeType.getMimeType());
				}
			}
			assertTrue(hasCheckedMimeType);
		}
		assertEquals(first.getPayloadType(), second.getPayloadType());
		assertEquals(first.getPublicKey(), second.getPublicKey());
		assertEquals(first.getUrl(), second.getUrl());
	}
}
