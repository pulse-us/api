package gov.ca.emsa.pulse.broker.dto;

import gov.ca.emsa.pulse.common.domain.Address;
import gov.ca.emsa.pulse.common.domain.Document;
import gov.ca.emsa.pulse.common.domain.GivenName;
import gov.ca.emsa.pulse.common.domain.Patient;
import gov.ca.emsa.pulse.common.domain.PatientRecord;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

public class DomainToDtoConverter {
	private static final Logger logger = LogManager.getLogger(DomainToDtoConverter.class);
	
	public static PatientDTO convertToPatient(Patient domainObj) {
		PatientDTO result = new PatientDTO();
		if(domainObj.getId() != null) {
			result.setId(new Long(domainObj.getId()));
		}
		if(domainObj.getPatientName() != null){
			result.getPatientName().setFamilyName(domainObj.getPatientName().getFamilyName());
			ArrayList<GivenNameDTO> givens = new ArrayList<GivenNameDTO>();
			for(GivenName givenDto : domainObj.getPatientName().getGivenName()){
				GivenNameDTO givenName = new GivenNameDTO();
				givenName.setGivenName(givenDto.getGivenName());
				givenName.setId(givenDto.getId());
				givenName.setPatientNameId(givenDto.getPatientNameId());
				givens.add(givenName);
			}
			result.getPatientName().setGivenName(givens);
			if(domainObj.getPatientName().getSuffix() != null)
				result.getPatientName().setSuffix(domainObj.getPatientName().getSuffix());
			if(domainObj.getPatientName().getPrefix() != null)
				result.getPatientName().setPrefix(domainObj.getPatientName().getPrefix());
			if(domainObj.getPatientName().getNameType() != null)
				result.getPatientName().setNameTypeCode(domainObj.getPatientName().getNameType());
			if(domainObj.getPatientName().getNameTypeCodeDescription() != null)
				result.getPatientName().setNameTypeCodeDescription(domainObj.getPatientName().getNameTypeCodeDescription());
			if(domainObj.getPatientName().getNameRepresentationCode() != null)
				result.getPatientName().setNameRepresentationCode(domainObj.getPatientName().getNameRepresentationCode());
			if(domainObj.getPatientName().getNameRepresentationCodeDescription() != null)
				result.getPatientName().setNameRepresentationCodeDescription(domainObj.getPatientName().getNameRepresentationCodeDescription());
			if(domainObj.getPatientName().getNameAssemblyOrderCode() != null)
				result.getPatientName().setNameAssemblyOrderCode(domainObj.getPatientName().getNameAssemblyOrderCode());
			if(domainObj.getPatientName().getNameAssemblyOrderCodeDescription() != null)
				result.getPatientName().setNameAssemblyOrderCodeDescription(domainObj.getPatientName().getNameAssemblyOrderCodeDescription());
			if(domainObj.getPatientName().getEffectiveDate() != null)
				result.getPatientName().setEffectiveDate(domainObj.getPatientName().getEffectiveDate());
			if(domainObj.getPatientName().getExpirationDate() != null)
				result.getPatientName().setExpirationDate(domainObj.getPatientName().getExpirationDate());
		}
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
	
	public static PatientRecordDTO convertToPatientRecord(PatientRecord domainObj) {
		PatientRecordDTO result = new PatientRecordDTO();
		if(domainObj.getId() != null) {
			result.setId(new Long(domainObj.getId()));
		}
		if(domainObj.getPatientName() != null){
			result.getPatientName().setFamilyName(domainObj.getPatientName().getFamilyName());
			ArrayList<GivenNameDTO> givens = new ArrayList<GivenNameDTO>();
			for(GivenName givenDto : domainObj.getPatientName().getGivenName()){
				GivenNameDTO givenName = new GivenNameDTO();
				givenName.setGivenName(givenDto.getGivenName());
				givenName.setId(givenDto.getId());
				givenName.setPatientNameId(givenDto.getPatientNameId());
				givens.add(givenName);
			}
			result.getPatientName().setGivenName(givens);
			if(domainObj.getPatientName().getSuffix() != null)
				result.getPatientName().setSuffix(domainObj.getPatientName().getSuffix());
			if(domainObj.getPatientName().getPrefix() != null)
				result.getPatientName().setPrefix(domainObj.getPatientName().getPrefix());
			if(domainObj.getPatientName().getNameType() != null)
				result.getPatientName().setNameTypeCode(domainObj.getPatientName().getNameType());
			if(domainObj.getPatientName().getNameTypeCodeDescription() != null)
				result.getPatientName().setNameTypeCodeDescription(domainObj.getPatientName().getNameTypeCodeDescription());
			if(domainObj.getPatientName().getNameRepresentationCode() != null)
				result.getPatientName().setNameRepresentationCode(domainObj.getPatientName().getNameRepresentationCode());
			if(domainObj.getPatientName().getNameRepresentationCodeDescription() != null)
				result.getPatientName().setNameRepresentationCodeDescription(domainObj.getPatientName().getNameRepresentationCodeDescription());
			if(domainObj.getPatientName().getNameAssemblyOrderCode() != null)
				result.getPatientName().setNameAssemblyOrderCode(domainObj.getPatientName().getNameAssemblyOrderCode());
			if(domainObj.getPatientName().getNameAssemblyOrderCodeDescription() != null)
				result.getPatientName().setNameAssemblyOrderCodeDescription(domainObj.getPatientName().getNameAssemblyOrderCodeDescription());
			if(domainObj.getPatientName().getEffectiveDate() != null)
				result.getPatientName().setEffectiveDate(domainObj.getPatientName().getEffectiveDate());
			if(domainObj.getPatientName().getExpirationDate() != null)
				result.getPatientName().setExpirationDate(domainObj.getPatientName().getExpirationDate());
		}
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
}
