package gov.ca.emsa.pulse.broker.dto;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

import gov.ca.emsa.pulse.common.domain.AlternateCareFacility;
import gov.ca.emsa.pulse.common.domain.Document;
import gov.ca.emsa.pulse.common.domain.Patient;
import gov.ca.emsa.pulse.common.domain.PatientRecord;

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
			AlternateCareFacilityDTO acf = convert(domainObj.getAcf());
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
	
	public static PatientRecordDTO convertToPatientRecord(PatientRecord domainObj) {
		PatientRecordDTO result = new PatientRecordDTO();
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
		result.setFormat(domainObj.getFormat());
		result.setClassName(domainObj.getClassName());
		result.setConfidentiality(domainObj.getConfidentiality());
		result.setCreationTime(domainObj.getCreationTime());
		result.setDescription(domainObj.getDescription());
		result.setSize(domainObj.getSize());
		result.setPatientOrgMapId(domainObj.getOrgMapId());
		
		if(domainObj.getIdentifier() != null) {
			result.setDocumentUniqueId(domainObj.getIdentifier().getDocumentUniqueId());
			result.setHomeCommunityId(domainObj.getIdentifier().getHomeCommunityId());
			result.setRepositoryUniqueId(domainObj.getIdentifier().getRepositoryUniqueId());
		}
		return result;
	}
	
	public static AlternateCareFacilityDTO convert(AlternateCareFacility domainObj) {
		AlternateCareFacilityDTO result = new AlternateCareFacilityDTO();
		result.setId(domainObj.getId());
		result.setName(domainObj.getName());
		result.setPhoneNumber(domainObj.getPhoneNumber());
		if(domainObj.getAddress() != null) {
			if(domainObj.getAddress().getLines() != null) {
				for(String line : domainObj.getAddress().getLines()) {
					AddressLineDTO lineDto = new AddressLineDTO();
					lineDto.setLine(line);
					result.getLines().add(lineDto);
				}
			}
			result.setCity(domainObj.getAddress().getCity());
			result.setState(domainObj.getAddress().getState());
			result.setZipcode(domainObj.getAddress().getZipcode());
			result.setCountry(domainObj.getAddress().getCountry());
		}
		return result;
	}
}
