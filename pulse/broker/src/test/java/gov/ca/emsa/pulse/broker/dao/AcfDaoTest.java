package gov.ca.emsa.pulse.broker.dao;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import gov.ca.emsa.pulse.broker.BrokerApplicationTestConfig;
import gov.ca.emsa.pulse.broker.dto.AddressDTO;
import gov.ca.emsa.pulse.broker.dto.AlternateCareFacilityDTO;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={BrokerApplicationTestConfig.class})
public class AcfDaoTest {
	@Autowired AddressDAO addrDao;
	@Autowired AlternateCareFacilityDAO acfDao;
	
	@Test
	@Transactional
	@Rollback(true)
	public void createAcfWithoutAddress() {
		String name = "ACF 1";
		String phoneNumber = "4105551000";
		
		AlternateCareFacilityDTO dto = new AlternateCareFacilityDTO();
		dto.setName(name);
		dto.setPhoneNumber(phoneNumber);
		dto = acfDao.create(dto);
		
		Assert.assertNotNull(dto);
		Assert.assertNotNull(dto.getId());
		Assert.assertTrue(dto.getId().longValue() > 0);
		Assert.assertEquals(name, dto.getName());
		Assert.assertEquals(phoneNumber, dto.getPhoneNumber());
		
		dto = acfDao.getById(dto.getId());
		Assert.assertEquals(name, dto.getName());
		Assert.assertEquals(phoneNumber, dto.getPhoneNumber());
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void createAcfWithExistingAddress() {
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
		addrDto = addrDao.create(addrDto);
		Assert.assertNotNull(addrDto);
		Assert.assertNotNull(addrDto.getId());
		Assert.assertTrue(addrDto.getId().longValue() > 0);
		long existingAddrId = addrDto.getId().longValue();
		
		AlternateCareFacilityDTO dto = new AlternateCareFacilityDTO();
		dto.setName(name);
		dto.setPhoneNumber(phoneNumber);
		dto.setAddress(addrDto);
		
		dto = acfDao.create(dto);
		
		Assert.assertNotNull(dto);
		Assert.assertNotNull(dto.getId());
		Assert.assertTrue(dto.getId().longValue() > 0);
		Assert.assertEquals(name, dto.getName());
		Assert.assertEquals(phoneNumber, dto.getPhoneNumber());
		Assert.assertNotNull(dto.getAddress());
		Assert.assertNotNull(dto.getAddress().getId());
		Assert.assertTrue(dto.getAddress().getId().longValue() > 0);
		Assert.assertEquals(existingAddrId, dto.getAddress().getId().longValue());
		Assert.assertEquals(streetLine1, dto.getAddress().getStreetLineOne());
		Assert.assertEquals(city, dto.getAddress().getCity());
		Assert.assertEquals(state, dto.getAddress().getState());
		Assert.assertEquals(zip, dto.getAddress().getZipcode());
		
		dto = acfDao.getById(dto.getId());
		Assert.assertEquals(name, dto.getName());
		Assert.assertEquals(phoneNumber, dto.getPhoneNumber());
		Assert.assertNotNull(dto.getAddress());
		Assert.assertNotNull(dto.getAddress().getId());
		Assert.assertTrue(dto.getAddress().getId().longValue() > 0);
		Assert.assertEquals(existingAddrId, dto.getAddress().getId().longValue());
		Assert.assertEquals(streetLine1, dto.getAddress().getStreetLineOne());
		Assert.assertEquals(city, dto.getAddress().getCity());
		Assert.assertEquals(state, dto.getAddress().getState());
		Assert.assertEquals(zip, dto.getAddress().getZipcode());
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void createAcfWithNewAddress() {
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
		
		dto = acfDao.create(dto);
		
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
		
		dto = acfDao.getById(dto.getId());
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
	public void updateAcfName() {
		String name = "ACF 1";
		String updatedName = "ACF 1.1";
		String phoneNumber = "4105551000";
		
		AlternateCareFacilityDTO dto = new AlternateCareFacilityDTO();
		dto.setName(name);
		dto.setPhoneNumber(phoneNumber);
		dto = acfDao.create(dto);
		
		Assert.assertNotNull(dto);
		Assert.assertNotNull(dto.getId());
		Assert.assertTrue(dto.getId().longValue() > 0);
		Assert.assertEquals(name, dto.getName());
		Assert.assertEquals(phoneNumber, dto.getPhoneNumber());
		
		dto.setName(updatedName);
		dto = acfDao.update(dto);
		Assert.assertEquals(updatedName, dto.getName());
		
		dto = acfDao.getById(dto.getId());
		Assert.assertEquals(updatedName, dto.getName());
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
		dto = acfDao.create(dto);
		
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
		dto = acfDao.update(dto);
		
		Assert.assertEquals(name, dto.getName());
		Assert.assertEquals(phoneNumber, dto.getPhoneNumber());
		Assert.assertNotNull(dto.getAddress());
		Assert.assertNotNull(dto.getAddress().getId());
		Assert.assertTrue(dto.getAddress().getId().longValue() > 0);
		Assert.assertEquals(streetLine1, dto.getAddress().getStreetLineOne());
		Assert.assertEquals(city, dto.getAddress().getCity());
		Assert.assertEquals(state, dto.getAddress().getState());
		Assert.assertEquals(zip, dto.getAddress().getZipcode());
		
		dto = acfDao.getById(dto.getId());
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
		
		dto = acfDao.create(dto);
		
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
		dto = acfDao.update(dto);
		Assert.assertEquals(name, dto.getName());
		Assert.assertEquals(phoneNumber, dto.getPhoneNumber());
		Assert.assertNotNull(dto.getAddress());
		Assert.assertNotNull(dto.getAddress().getId());
		Assert.assertTrue(dto.getAddress().getId().longValue() > 0);
		Assert.assertEquals(updatedStreetLine1, dto.getAddress().getStreetLineOne());
		Assert.assertEquals(city, dto.getAddress().getCity());
		Assert.assertEquals(state, dto.getAddress().getState());
		Assert.assertEquals(zip, dto.getAddress().getZipcode());
		
		dto = acfDao.getById(dto.getId());
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
		
		dto = acfDao.create(dto);
		
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
		dto = acfDao.update(dto);
		Assert.assertEquals(name, dto.getName());
		Assert.assertEquals(phoneNumber, dto.getPhoneNumber());
		Assert.assertNull(dto.getAddress());
		
		dto = acfDao.getById(dto.getId());
		Assert.assertEquals(name, dto.getName());
		Assert.assertEquals(phoneNumber, dto.getPhoneNumber());
		Assert.assertNull(dto.getAddress());
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void deleteAcfNoAddress() {
		String name = "ACF 1";
		String phoneNumber = "4105551000";
		
		AlternateCareFacilityDTO dto = new AlternateCareFacilityDTO();
		dto.setName(name);
		dto.setPhoneNumber(phoneNumber);
		dto = acfDao.create(dto);
		
		Assert.assertNotNull(dto);
		Assert.assertNotNull(dto.getId());
		Assert.assertTrue(dto.getId().longValue() > 0);
		Assert.assertEquals(name, dto.getName());
		Assert.assertEquals(phoneNumber, dto.getPhoneNumber());
		
		acfDao.delete(dto.getId());
		dto = acfDao.getById(dto.getId());
		Assert.assertNull(dto);
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void deleteAcfWithAddress() {
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
		addrDto = addrDao.create(addrDto);
		Assert.assertNotNull(addrDto);
		Assert.assertNotNull(addrDto.getId());
		Assert.assertTrue(addrDto.getId().longValue() > 0);
		
		AlternateCareFacilityDTO dto = new AlternateCareFacilityDTO();
		dto.setName(name);
		dto.setPhoneNumber(phoneNumber);
		dto.setAddress(addrDto);
		
		dto = acfDao.create(dto);
		
		Assert.assertNotNull(dto);
		Assert.assertNotNull(dto.getId());
		Assert.assertTrue(dto.getId().longValue() > 0);
		Assert.assertEquals(name, dto.getName());
		Assert.assertEquals(phoneNumber, dto.getPhoneNumber());
		
		acfDao.delete(dto.getId());
		dto = acfDao.getById(dto.getId());
		Assert.assertNull(dto);
		
		addrDto = addrDao.getById(addrDto.getId());
		Assert.assertNull(addrDto);
	}
}
