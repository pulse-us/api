package gov.ca.emsa.pulse.broker.dto;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

import gov.ca.emsa.pulse.common.domain.Address;
import gov.ca.emsa.pulse.common.domain.Document;
import gov.ca.emsa.pulse.common.domain.Patient;

public class DomainToDtoConverter {
	private static final Logger logger = LogManager.getLogger(DomainToDtoConverter.class);
	
	public static PatientDTO convertToPatient(Patient domainObj) {
		PatientDTO result = new PatientDTO();
		if(domainObj.getId() != null) {
			result.setId(new Long(domainObj.getId()));
		}
		result.setGivenName(domainObj.getGivenName());
		result.setFamilyName(domainObj.getFamilyName());
		result.setGender(domainObj.getGender());
		if(!StringUtils.isEmpty(domainObj.getDateOfBirth())) {
			LocalDate patientDob = null;
			try {
				patientDob = LocalDate.parse(domainObj.getDateOfBirth(), DateTimeFormatter.ISO_DATE);
			} catch(DateTimeParseException pex) {
				logger.error("Could not parse " + domainObj.getDateOfBirth() + " as a date in the format " + DateTimeFormatter.ISO_DATE);
			} 
			result.setDateOfBirth(patientDob);
		}
		result.setPhoneNumber(domainObj.getPhoneNumber());
		result.setSsn(domainObj.getSsn());
		
		if(domainObj.getAcf() != null) {
			AlternateCareFacilityDTO acf = new AlternateCareFacilityDTO();
			acf.setId(domainObj.getAcf().getId());
			acf.setName(domainObj.getAcf().getName());
			if(domainObj.getAcf().getAddress() != null) {
				Address acfAddress = domainObj.getAcf().getAddress();
				AddressDTO acfAddrDto = new AddressDTO();
				acfAddrDto.setStreetLineOne(acfAddress.getStreet1());
				acfAddrDto.setStreetLineTwo(acfAddress.getStreet2());
				acfAddrDto.setCity(acfAddress.getCity());
				acfAddrDto.setState(acfAddress.getState());
				acfAddrDto.setZipcode(acfAddress.getZipcode());
				acfAddrDto.setCountry(acfAddress.getCountry());
				acf.setAddress(acfAddrDto);
			}
			result.setAcf(acf);
		}
		
		if(domainObj.getAddress() != null) {
			AddressDTO address = new AddressDTO();
			address.setStreetLineOne(domainObj.getAddress().getStreet1());
			address.setStreetLineTwo(domainObj.getAddress().getStreet2());
			address.setCity(domainObj.getAddress().getCity());
			address.setState(domainObj.getAddress().getState());
			address.setZipcode(domainObj.getAddress().getZipcode());
			address.setCountry(domainObj.getAddress().getCountry());
			result.setAddress(address);
		}
		
		return result;
	}
	
	public static DocumentDTO convert(Document domainObj) {
		DocumentDTO result = new DocumentDTO();
		result.setName(domainObj.getName());
		result.setFormat("TODO"); //TODO:
		result.setPatientOrgMapId(domainObj.getOrgMapId());
		return result;
	}
}
