package gov.ca.emsa.pulse.broker.manager;

import gov.ca.emsa.pulse.broker.BrokerApplicationTestConfig;
import gov.ca.emsa.pulse.broker.dao.AddressDAO;
import gov.ca.emsa.pulse.broker.dto.AddressDTO;
import gov.ca.emsa.pulse.broker.dto.AlternateCareFacilityDTO;

import java.util.Date;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={BrokerApplicationTestConfig.class})
public class AcfManagerTest {

	@Autowired AlternateCareFacilityManager acfManager;
	@Autowired AddressDAO addrDao;
	
	@Test
	@Transactional
	@Rollback(true)
	public void createAcfNoAddress() {
		String name = "ACF 1";
		String phoneNumber = "4105551000";
		
		AlternateCareFacilityDTO dto = new AlternateCareFacilityDTO();
		dto.setName(name);
		dto.setPhoneNumber(phoneNumber);
		dto = acfManager.create(dto);
		
		Assert.assertNotNull(dto);
		Assert.assertNotNull(dto.getId());
		Assert.assertTrue(dto.getId().longValue() > 0);
		Assert.assertEquals(name, dto.getName());
		Assert.assertEquals(phoneNumber, dto.getPhoneNumber());
		Assert.assertNull(dto.getAddress());
		
		dto = acfManager.getById(dto.getId());
		Assert.assertEquals(name, dto.getName());
		Assert.assertEquals(phoneNumber, dto.getPhoneNumber());	
		Assert.assertNull(dto.getAddress());	
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void createAcfWithAddress() {
		String name = "ACF 1";
		String phoneNumber = "4105551000";
		String streetLine1 = "1000 Hilltop Circle";
		String city = "Baltimore";
		String state = "MD";
		String zip = "21227";
		
		AddressDTO addrDto = new AddressDTO();
		addrDto.setStreetLineOne(streetLine1);
		addrDto.setCity(city);
		addrDto.setState(state);
		addrDto.setZipcode(zip);
		
		AlternateCareFacilityDTO dto = new AlternateCareFacilityDTO();
		dto.setName(name);
		dto.setPhoneNumber(phoneNumber);
		dto.setAddress(addrDto);
		
		dto = acfManager.create(dto);
		
		Assert.assertNotNull(dto);
		Assert.assertNotNull(dto.getId());
		Assert.assertTrue(dto.getId().longValue() > 0);
		Assert.assertEquals(name, dto.getName());
		Assert.assertEquals(phoneNumber, dto.getPhoneNumber());
		Assert.assertNotNull(dto.getAddress());
		Assert.assertNotNull(dto.getAddress().getId());
		Assert.assertTrue(dto.getAddress().getId().longValue() > 0);
		Assert.assertEquals(streetLine1, dto.getAddress().getStreetLineOne());
		Assert.assertEquals(city, dto.getAddress().getCity());
		Assert.assertEquals(state, dto.getAddress().getState());
		Assert.assertEquals(zip, dto.getAddress().getZipcode());
		
		dto = acfManager.getById(dto.getId());
		Assert.assertEquals(name, dto.getName());
		Assert.assertEquals(phoneNumber, dto.getPhoneNumber());
		Assert.assertNotNull(dto.getAddress());
		Assert.assertNotNull(dto.getAddress().getId());
		Assert.assertTrue(dto.getAddress().getId().longValue() > 0);
		Assert.assertEquals(streetLine1, dto.getAddress().getStreetLineOne());
		Assert.assertEquals(city, dto.getAddress().getCity());
		Assert.assertEquals(state, dto.getAddress().getState());
		Assert.assertEquals(zip, dto.getAddress().getZipcode());
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void updateAcfPhoneNumber() {
		String name = "ACF 1";
		String phoneNumber = "4105551000";
		String updatedPhoneNumber = "3015551000";
		
		AlternateCareFacilityDTO dto = new AlternateCareFacilityDTO();
		dto.setName(name);
		dto.setPhoneNumber(phoneNumber);
		dto = acfManager.create(dto);
		
		Assert.assertNotNull(dto);
		Assert.assertNotNull(dto.getId());
		Assert.assertTrue(dto.getId().longValue() > 0);
		Assert.assertEquals(name, dto.getName());
		Assert.assertEquals(phoneNumber, dto.getPhoneNumber());
		
		dto.setPhoneNumber(updatedPhoneNumber);
		dto = acfManager.update(dto);
		Assert.assertEquals(updatedPhoneNumber, dto.getPhoneNumber());
		
		dto = acfManager.getById(dto.getId());
		Assert.assertEquals(updatedPhoneNumber, dto.getPhoneNumber());
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void updateAcfCreateAddress() {
		String name = "ACF 1";
		String phoneNumber = "4105551000";
		String streetLine1 = "1000 Hilltop Circle";
		String city = "Baltimore";
		String state = "MD";
		String zip = "21227";
		
		AlternateCareFacilityDTO dto = new AlternateCareFacilityDTO();
		dto.setName(name);
		dto.setPhoneNumber(phoneNumber);		
		dto = acfManager.create(dto);
		
		Assert.assertNotNull(dto);
		Assert.assertNotNull(dto.getId());
		Assert.assertTrue(dto.getId().longValue() > 0);
		Assert.assertEquals(name, dto.getName());
		Assert.assertEquals(phoneNumber, dto.getPhoneNumber());
		Assert.assertNull(dto.getAddress());
		
		AddressDTO addrDto = new AddressDTO();
		addrDto.setStreetLineOne(streetLine1);
		addrDto.setCity(city);
		addrDto.setState(state);
		addrDto.setZipcode(zip);
		dto.setAddress(addrDto);		
		dto = acfManager.update(dto);
		
		Assert.assertEquals(name, dto.getName());
		Assert.assertEquals(phoneNumber, dto.getPhoneNumber());
		Assert.assertNotNull(dto.getAddress());
		Assert.assertNotNull(dto.getAddress().getId());
		Assert.assertTrue(dto.getAddress().getId().longValue() > 0);
		Assert.assertEquals(streetLine1, dto.getAddress().getStreetLineOne());
		Assert.assertEquals(city, dto.getAddress().getCity());
		Assert.assertEquals(state, dto.getAddress().getState());
		Assert.assertEquals(zip, dto.getAddress().getZipcode());
		
		dto = acfManager.getById(dto.getId());
		Assert.assertEquals(name, dto.getName());
		Assert.assertEquals(phoneNumber, dto.getPhoneNumber());
		Assert.assertNotNull(dto.getAddress());
		Assert.assertNotNull(dto.getAddress().getId());
		Assert.assertTrue(dto.getAddress().getId().longValue() > 0);
		Assert.assertEquals(streetLine1, dto.getAddress().getStreetLineOne());
		Assert.assertEquals(city, dto.getAddress().getCity());
		Assert.assertEquals(state, dto.getAddress().getState());
		Assert.assertEquals(zip, dto.getAddress().getZipcode());
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void updateAcfAddressInfo() {
		String name = "ACF 1";
		String phoneNumber = "4105551000";
		String streetLine1 = "1000 Hilltop Circle";
		String updatedStreetLine1 = "1001 Hilltop Circle";
		String city = "Baltimore";
		String state = "MD";
		String zip = "21227";
		
		AddressDTO addrDto = new AddressDTO();
		addrDto.setStreetLineOne(streetLine1);
		addrDto.setCity(city);
		addrDto.setState(state);
		addrDto.setZipcode(zip);
		
		AlternateCareFacilityDTO dto = new AlternateCareFacilityDTO();
		dto.setName(name);
		dto.setPhoneNumber(phoneNumber);
		dto.setAddress(addrDto);
		
		dto = acfManager.create(dto);
		
		Assert.assertNotNull(dto);
		Assert.assertNotNull(dto.getId());
		Assert.assertTrue(dto.getId().longValue() > 0);
		Assert.assertEquals(name, dto.getName());
		Assert.assertEquals(phoneNumber, dto.getPhoneNumber());
		Assert.assertNotNull(dto.getAddress());
		Assert.assertNotNull(dto.getAddress().getId());
		Assert.assertTrue(dto.getAddress().getId().longValue() > 0);
		Assert.assertEquals(streetLine1, dto.getAddress().getStreetLineOne());
		Assert.assertEquals(city, dto.getAddress().getCity());
		Assert.assertEquals(state, dto.getAddress().getState());
		Assert.assertEquals(zip, dto.getAddress().getZipcode());
		
		dto.getAddress().setStreetLineOne(updatedStreetLine1);
		dto = acfManager.update(dto);
		Assert.assertEquals(name, dto.getName());
		Assert.assertEquals(phoneNumber, dto.getPhoneNumber());
		Assert.assertNotNull(dto.getAddress());
		Assert.assertNotNull(dto.getAddress().getId());
		Assert.assertTrue(dto.getAddress().getId().longValue() > 0);
		Assert.assertEquals(updatedStreetLine1, dto.getAddress().getStreetLineOne());
		Assert.assertEquals(city, dto.getAddress().getCity());
		Assert.assertEquals(state, dto.getAddress().getState());
		Assert.assertEquals(zip, dto.getAddress().getZipcode());
		
		dto = acfManager.getById(dto.getId());
		Assert.assertEquals(name, dto.getName());
		Assert.assertEquals(phoneNumber, dto.getPhoneNumber());
		Assert.assertNotNull(dto.getAddress());
		Assert.assertNotNull(dto.getAddress().getId());
		Assert.assertTrue(dto.getAddress().getId().longValue() > 0);
		Assert.assertEquals(updatedStreetLine1, dto.getAddress().getStreetLineOne());
		Assert.assertEquals(city, dto.getAddress().getCity());
		Assert.assertEquals(state, dto.getAddress().getState());
		Assert.assertEquals(zip, dto.getAddress().getZipcode());
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void updateAcfRemoveAddress() {
		String name = "ACF 1";
		String phoneNumber = "4105551000";
		String streetLine1 = "1000 Hilltop Circle";
		String city = "Baltimore";
		String state = "MD";
		String zip = "21227";
		
		AddressDTO addrDto = new AddressDTO();
		addrDto.setStreetLineOne(streetLine1);
		addrDto.setCity(city);
		addrDto.setState(state);
		addrDto.setZipcode(zip);
		
		AlternateCareFacilityDTO dto = new AlternateCareFacilityDTO();
		dto.setName(name);
		dto.setPhoneNumber(phoneNumber);
		dto.setAddress(addrDto);
		
		dto = acfManager.create(dto);
		
		Assert.assertNotNull(dto);
		Assert.assertNotNull(dto.getId());
		Assert.assertTrue(dto.getId().longValue() > 0);
		Assert.assertEquals(name, dto.getName());
		Assert.assertEquals(phoneNumber, dto.getPhoneNumber());
		Assert.assertNotNull(dto.getAddress());
		Assert.assertNotNull(dto.getAddress().getId());
		Assert.assertTrue(dto.getAddress().getId().longValue() > 0);
		Assert.assertEquals(streetLine1, dto.getAddress().getStreetLineOne());
		Assert.assertEquals(city, dto.getAddress().getCity());
		Assert.assertEquals(state, dto.getAddress().getState());
		Assert.assertEquals(zip, dto.getAddress().getZipcode());
		
		dto.setAddress(null);
		dto = acfManager.update(dto);
		Assert.assertEquals(name, dto.getName());
		Assert.assertEquals(phoneNumber, dto.getPhoneNumber());
		Assert.assertNull(dto.getAddress());
		
		dto = acfManager.getById(dto.getId());
		Assert.assertEquals(name, dto.getName());
		Assert.assertEquals(phoneNumber, dto.getPhoneNumber());
		Assert.assertNull(dto.getAddress());
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void testCleanupOldAcfs() {
		String name = "ACF 1";
		String phoneNumber = "4105551000";
		
		String name2 = "ACF 2";
		String streetLine1 = "1000 Hilltop Circle";
		String city = "Baltimore";
		String state = "MD";
		String zip = "21227";
				
		AlternateCareFacilityDTO dto1 = new AlternateCareFacilityDTO();
		dto1.setName(name);
		dto1.setPhoneNumber(phoneNumber);
		dto1 = acfManager.create(dto1);
		
		AddressDTO addrDto = new AddressDTO();
		addrDto.setStreetLineOne(streetLine1);
		addrDto.setCity(city);
		addrDto.setState(state);
		addrDto.setZipcode(zip);
		AlternateCareFacilityDTO dto2 = new AlternateCareFacilityDTO();
		dto2.setName(name2);
		dto2.setAddress(addrDto);
		dto2 = acfManager.create(dto2);
		
		//should delete neither
		Date cleanupIfOlderThan = new Date(System.currentTimeMillis()-60000);
		acfManager.cleanupCache(cleanupIfOlderThan);
		dto1 = acfManager.getById(dto1.getId());
		Assert.assertNotNull(dto1);
		
		Assert.assertNotNull(dto2.getAddress());
		Assert.assertNotNull(dto2.getAddress().getId());
		addrDto = addrDao.getById(dto2.getAddress().getId());
		Assert.assertNotNull(addrDto);
		dto2 = acfManager.getById(dto2.getId());
		Assert.assertNotNull(dto2);
		
		//should delete both
		cleanupIfOlderThan = new Date(System.currentTimeMillis()+1000);
		acfManager.cleanupCache(cleanupIfOlderThan);
		
		dto1 = acfManager.getById(dto1.getId());
		Assert.assertNull(dto1);
		
		Assert.assertNotNull(dto2.getAddress());
		Assert.assertNotNull(dto2.getAddress().getId());
		addrDto = addrDao.getById(dto2.getAddress().getId());
		Assert.assertNull(addrDto);
		dto2 = acfManager.getById(dto2.getId());
		Assert.assertNull(dto2);
	}
}
