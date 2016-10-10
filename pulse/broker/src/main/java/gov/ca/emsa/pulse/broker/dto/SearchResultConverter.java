package gov.ca.emsa.pulse.broker.dto;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

import gov.ca.emsa.pulse.broker.entity.GivenNameEntity;
import gov.ca.emsa.pulse.common.domain.Document;
import gov.ca.emsa.pulse.common.domain.GivenName;
import gov.ca.emsa.pulse.common.domain.Patient;

public class SearchResultConverter {
	private static final Logger logger = LogManager.getLogger(SearchResultConverter.class);

	public static PatientRecordDTO convertToPatientRecord(Patient domainObj) {
		PatientRecordDTO result = new PatientRecordDTO();
		if(domainObj.getPatientName() != null){
			PatientNameDTO patientName = new PatientNameDTO();
			patientName.setFamilyName(domainObj.getPatientName().getFamilyName());
			ArrayList<GivenNameDTO> givens = new ArrayList<GivenNameDTO>();
			for(GivenName givenDto : domainObj.getPatientName().getGivenName()){
				GivenNameDTO givenName = new GivenNameDTO();
				givenName.setGivenName(givenDto.getGivenName());
				givenName.setId(givenDto.getId());
				givenName.setPatientNameId(givenDto.getPatientNameId());
				givens.add(givenName);
			}
			patientName.setGivenName(givens);
			patientName.setSuffix(domainObj.getPatientName().getSuffix());
			patientName.setPrefix(domainObj.getPatientName().getPrefix());
			patientName.setNameTypeCode(domainObj.getPatientName().getNameTypeCode());
			patientName.setNameTypeCodeDescription(domainObj.getPatientName().getNameTypeCodeDescription());
			patientName.setNameRepresentationCode(domainObj.getPatientName().getNameRepresentationCode());
			patientName.setNameRepresentationCodeDescription(domainObj.getPatientName().getNameRepresentationCodeDescription());
			patientName.setNameAssemblyOrderCode(domainObj.getPatientName().getNameAssemblyOrderCode());
			patientName.setNameAssemblyOrderCodeDescription(domainObj.getPatientName().getNameAssemblyOrderCodeDescription());
			patientName.setEffectiveDate(domainObj.getPatientName().getEffectiveDate());
			patientName.setExpirationDate(domainObj.getPatientName().getExpirationDate());
			result.setPatientName(patientName);
		}
		result.setGender(domainObj.getGender());
		if(!StringUtils.isEmpty(domainObj.getDateOfBirth())) {
			LocalDate patientDob = null;
			try {
				 patientDob = LocalDate.parse(domainObj.getDateOfBirth(), DateTimeFormatter.BASIC_ISO_DATE);
			} catch(DateTimeParseException pex) {
				logger.error("Could not parse " + domainObj.getDateOfBirth() + " as a date in the format " + DateTimeFormatter.BASIC_ISO_DATE);
			}
			result.setDateOfBirth(patientDob);
		}
		result.setPhoneNumber(domainObj.getPhoneNumber());
		result.setSsn(domainObj.getSsn());
		result.setOrgPatientId(domainObj.getOrgPatientId());
		
		if(domainObj.getAddress() != null) {
			AddressDTO address = new AddressDTO();
			address.setStreetLineOne(domainObj.getAddress().getStreet1());
			address.setStreetLineTwo(domainObj.getAddress().getStreet2());
			address.setCity(domainObj.getAddress().getCity());
			address.setState(domainObj.getAddress().getState());
			address.setZipcode(domainObj.getAddress().getZipcode());
			result.setAddress(address);
		}
		
		return result;
	}
}
