package gov.ca.emsa.pulse.broker.dto;

import gov.ca.emsa.pulse.common.domain.GivenName;
import gov.ca.emsa.pulse.common.domain.NameAssembly;
import gov.ca.emsa.pulse.common.domain.NameRepresentation;
import gov.ca.emsa.pulse.common.domain.NameType;
import gov.ca.emsa.pulse.common.domain.Patient;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

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
			if(domainObj.getPatientName().getNameType() != null){
				NameTypeDTO nameType = new NameTypeDTO();
				nameType.setCode(domainObj.getPatientName().getNameType().getCode());
				nameType.setDescription(domainObj.getPatientName().getNameType().getDescription());
				nameType.setId(domainObj.getPatientName().getNameType().getId());
				result.getPatientName().setNameType(nameType);
			}
			if(domainObj.getPatientName().getNameRepresentation() != null){
				NameRepresentationDTO nameRep = new NameRepresentationDTO();
				nameRep.setCode(domainObj.getPatientName().getNameType().getCode());
				nameRep.setDescription(domainObj.getPatientName().getNameType().getDescription());
				nameRep.setId(domainObj.getPatientName().getNameType().getId());
				result.getPatientName().setNameRepresentation(nameRep);
			}
			if(domainObj.getPatientName().getNameAssembly() != null){
				NameAssemblyDTO nameAssembly = new NameAssemblyDTO();
				nameAssembly.setCode(domainObj.getPatientName().getNameType().getCode());
				nameAssembly.setDescription(domainObj.getPatientName().getNameType().getDescription());
				nameAssembly.setId(domainObj.getPatientName().getNameType().getId());
				result.getPatientName().setNameAssembly(nameAssembly);
			}
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
