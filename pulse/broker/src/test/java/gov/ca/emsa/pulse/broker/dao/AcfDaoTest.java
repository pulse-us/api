package gov.ca.emsa.pulse.broker.dao;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import gov.ca.emsa.pulse.broker.BrokerApplicationTestConfig;
import gov.ca.emsa.pulse.broker.dto.AddressLineDTO;
import gov.ca.emsa.pulse.broker.dto.AlternateCareFacilityDTO;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={BrokerApplicationTestConfig.class})
public class AcfDaoTest {
	@Autowired AlternateCareFacilityAddressLineDAO addrLineDao;
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
	public void createAcfWithAddressLines() {
		String name = "ACF 1";
		String phoneNumber = "4105551000";
		String streetLine1 = "1000 Hilltop Circle";
		String city = "Baltimore";
		String state = "MD";
		String zip = "21227";
		String country = "USA";
		
		AlternateCareFacilityDTO dto = new AlternateCareFacilityDTO();
		dto.setName(name);
		dto.setPhoneNumber(phoneNumber);
		dto.setCity(city);
		dto.setState(state);
		dto.setZipcode(zip);
		dto.setCountry(country);
		AddressLineDTO addressLine = new AddressLineDTO();
		addressLine.setLine(streetLine1);
		List<AddressLineDTO> lines = new ArrayList<AddressLineDTO>();
		lines.add(addressLine);
		dto.setLines(lines);
		dto = acfDao.create(dto);
		
		Assert.assertNotNull(dto);
		Assert.assertNotNull(dto.getId());
		Assert.assertTrue(dto.getId().longValue() > 0);
		Assert.assertEquals(name, dto.getName());
		Assert.assertEquals(phoneNumber, dto.getPhoneNumber());
		Assert.assertEquals(city, dto.getCity());
		Assert.assertEquals(state, dto.getState());
		Assert.assertEquals(zip, dto.getZipcode());
		Assert.assertEquals(country, dto.getCountry());
		Assert.assertNotNull(dto.getLines());
		Assert.assertEquals(1, dto.getLines().size());
		Assert.assertEquals(streetLine1, dto.getLines().get(0).getLine());
		
		dto = acfDao.getById(dto.getId());
		Assert.assertNotNull(dto);
		Assert.assertNotNull(dto.getId());
		Assert.assertTrue(dto.getId().longValue() > 0);
		Assert.assertEquals(name, dto.getName());
		Assert.assertEquals(phoneNumber, dto.getPhoneNumber());
		Assert.assertEquals(city, dto.getCity());
		Assert.assertEquals(state, dto.getState());
		Assert.assertEquals(zip, dto.getZipcode());
		Assert.assertEquals(country, dto.getCountry());
		Assert.assertNotNull(dto.getLines());
		Assert.assertEquals(1, dto.getLines().size());
		Assert.assertEquals(streetLine1, dto.getLines().get(0).getLine());
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void updateAcfAddStreetLineToAddress() {
		String name = "ACF 1";
		String phoneNumber = "4105551000";
		String streetLine1 = "1000 Hilltop Circle";
		String streetLine2 = "APT 2B";
		String city = "Baltimore";
		String state = "MD";
		String zip = "21227";
		String country = "USA";
		
		AlternateCareFacilityDTO dto = new AlternateCareFacilityDTO();
		dto.setName(name);
		dto.setPhoneNumber(phoneNumber);
		dto.setCity(city);
		dto.setState(state);
		dto.setZipcode(zip);
		dto.setCountry(country);
		AddressLineDTO addressLine = new AddressLineDTO();
		addressLine.setLine(streetLine1);
		List<AddressLineDTO> lines = new ArrayList<AddressLineDTO>();
		lines.add(addressLine);
		dto.setLines(lines);
		dto = acfDao.create(dto);
		
		dto = acfDao.getById(dto.getId());
		AddressLineDTO addressLine2 = new AddressLineDTO();
		addressLine2.setLine(streetLine2);
		dto.getLines().add(addressLine2);
		
		dto = acfDao.update(dto);
		Assert.assertNotNull(dto);
		Assert.assertNotNull(dto.getId());
		Assert.assertTrue(dto.getId().longValue() > 0);
		Assert.assertEquals(name, dto.getName());
		Assert.assertEquals(phoneNumber, dto.getPhoneNumber());
		Assert.assertEquals(city, dto.getCity());
		Assert.assertEquals(state, dto.getState());
		Assert.assertEquals(zip, dto.getZipcode());
		Assert.assertEquals(country, dto.getCountry());
		Assert.assertNotNull(dto.getLines());
		Assert.assertEquals(2, dto.getLines().size());
		Assert.assertEquals(streetLine1, dto.getLines().get(0).getLine());
		Assert.assertEquals(streetLine2, dto.getLines().get(1).getLine());
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void updateAcfRemoveStreetLineFromAddress() {
		String name = "ACF 1";
		String phoneNumber = "4105551000";
		String streetLine1 = "1000 Hilltop Circle";
		String streetLine2 = "APT 2B";
		String city = "Baltimore";
		String state = "MD";
		String zip = "21227";
		String country = "USA";
		
		AlternateCareFacilityDTO dto = new AlternateCareFacilityDTO();
		dto.setName(name);
		dto.setPhoneNumber(phoneNumber);
		dto.setCity(city);
		dto.setState(state);
		dto.setZipcode(zip);
		dto.setCountry(country);
		AddressLineDTO addressLine = new AddressLineDTO();
		addressLine.setLine(streetLine1);
		AddressLineDTO addressLine2 = new AddressLineDTO();
		addressLine2.setLine(streetLine2);
		List<AddressLineDTO> lines = new ArrayList<AddressLineDTO>();
		lines.add(addressLine);
		lines.add(addressLine2);
		dto.setLines(lines);
		dto = acfDao.create(dto);
		
		dto = acfDao.getById(dto.getId());
		Assert.assertEquals(2, dto.getLines().size());
		Assert.assertEquals(streetLine1, dto.getLines().get(0).getLine());
		Assert.assertEquals(streetLine2, dto.getLines().get(1).getLine());
		
		dto.getLines().remove(1);
		dto = acfDao.update(dto);
		Assert.assertNotNull(dto);
		Assert.assertNotNull(dto.getId());
		Assert.assertTrue(dto.getId().longValue() > 0);
		Assert.assertEquals(name, dto.getName());
		Assert.assertEquals(phoneNumber, dto.getPhoneNumber());
		Assert.assertEquals(city, dto.getCity());
		Assert.assertEquals(state, dto.getState());
		Assert.assertEquals(zip, dto.getZipcode());
		Assert.assertEquals(country, dto.getCountry());
		Assert.assertNotNull(dto.getLines());
		Assert.assertEquals(1, dto.getLines().size());
		Assert.assertEquals(streetLine1, dto.getLines().get(0).getLine());
	}
	
	@Test
	@Transactional
	@Rollback(true)
	public void updateAcfChangeStreetLineInAddress() {
		String name = "ACF 1";
		String phoneNumber = "4105551000";
		String streetLine1 = "1000 Hilltop Circle";
		String streetLine2 = "APT 2B";
		String city = "Baltimore";
		String state = "MD";
		String zip = "21227";
		String country = "USA";
		
		AlternateCareFacilityDTO dto = new AlternateCareFacilityDTO();
		dto.setName(name);
		dto.setPhoneNumber(phoneNumber);
		dto.setCity(city);
		dto.setState(state);
		dto.setZipcode(zip);
		dto.setCountry(country);
		AddressLineDTO addressLine = new AddressLineDTO();
		addressLine.setLine(streetLine1);
		AddressLineDTO addressLine2 = new AddressLineDTO();
		addressLine2.setLine(streetLine2);
		List<AddressLineDTO> lines = new ArrayList<AddressLineDTO>();
		lines.add(addressLine);
		lines.add(addressLine2);
		dto.setLines(lines);
		dto = acfDao.create(dto);
		
		dto = acfDao.getById(dto.getId());
		Assert.assertEquals(2, dto.getLines().size());
		Assert.assertEquals(streetLine1, dto.getLines().get(0).getLine());
		Assert.assertEquals(streetLine2, dto.getLines().get(1).getLine());
		
		String updatedAddressLine1 = "Updated address line.";
		dto.getLines().get(0).setLine(updatedAddressLine1);
		dto = acfDao.update(dto);
		Assert.assertNotNull(dto);
		Assert.assertNotNull(dto.getId());
		Assert.assertTrue(dto.getId().longValue() > 0);
		Assert.assertEquals(name, dto.getName());
		Assert.assertEquals(phoneNumber, dto.getPhoneNumber());
		Assert.assertEquals(city, dto.getCity());
		Assert.assertEquals(state, dto.getState());
		Assert.assertEquals(zip, dto.getZipcode());
		Assert.assertEquals(country, dto.getCountry());
		Assert.assertNotNull(dto.getLines());
		Assert.assertEquals(2, dto.getLines().size());
		Assert.assertEquals(updatedAddressLine1, dto.getLines().get(0).getLine());
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
	public void updateAcfUpdateAddressValues() {
		String name = "ACF 1";
		String phoneNumber = "4105551000";
		String city = "Baltimore";
		String state = "MD";
		String zip = "21227";
		String country = "USA";
		
		AlternateCareFacilityDTO dto = new AlternateCareFacilityDTO();
		dto.setName(name);
		dto.setPhoneNumber(phoneNumber);
		dto = acfDao.create(dto);
		
		Assert.assertNotNull(dto);
		Assert.assertNotNull(dto.getId());
		Assert.assertTrue(dto.getId().longValue() > 0);
		Assert.assertEquals(name, dto.getName());
		Assert.assertEquals(phoneNumber, dto.getPhoneNumber());
		
		dto.setCity(city);
		dto.setState(state);
		dto.setZipcode(zip);
		dto.setCountry(country);		
		dto = acfDao.update(dto);
		
		Assert.assertEquals(name, dto.getName());
		Assert.assertEquals(phoneNumber, dto.getPhoneNumber());
		Assert.assertEquals(city, dto.getCity());
		Assert.assertEquals(state, dto.getState());
		Assert.assertEquals(zip, dto.getZipcode());
		Assert.assertEquals(country, dto.getCountry());
		
		dto = acfDao.getById(dto.getId());
		Assert.assertEquals(name, dto.getName());
		Assert.assertEquals(phoneNumber, dto.getPhoneNumber());
		Assert.assertEquals(city, dto.getCity());
		Assert.assertEquals(state, dto.getState());
		Assert.assertEquals(zip, dto.getZipcode());
		Assert.assertEquals(country, dto.getCountry());
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
		String country = "USA";
		
		AlternateCareFacilityDTO dto = new AlternateCareFacilityDTO();
		dto.setName(name);
		dto.setPhoneNumber(phoneNumber);
		dto.setCity(city);
		dto.setState(state);
		dto.setZipcode(zip);
		dto.setCountry(country);
		AddressLineDTO addressLine = new AddressLineDTO();
		addressLine.setLine(streetLine1);
		List<AddressLineDTO> lines = new ArrayList<AddressLineDTO>();
		lines.add(addressLine);
		dto = acfDao.create(dto);
		
		Assert.assertNotNull(dto);
		Long acfId = new Long(dto.getId());
		
		acfDao.delete(dto.getId());
		dto = acfDao.getById(dto.getId());
		Assert.assertNull(dto);
		
		List<AddressLineDTO> deletedLine = addrLineDao.getByAcf(acfId);
		Assert.assertTrue(deletedLine == null || deletedLine.size() == 0);
	}
}
