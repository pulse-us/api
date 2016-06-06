package gov.ca.emsa.pulse.broker.dto;

import org.springframework.util.StringUtils;

import gov.ca.emsa.pulse.broker.domain.Document;
import gov.ca.emsa.pulse.broker.domain.Patient;

public class DomainToDtoConverter {
	
	public static PatientDTO convert(Patient domainObj) {
		PatientDTO result = new PatientDTO();
		if(domainObj.getId() != null) {
			result.setId(new Long(domainObj.getId().substring(5)));
		}
		result.setPulsePatientId(domainObj.getPulsePatientId());
		result.setOrgPatientId(domainObj.getOrgPatientId());
		result.setFirstName(domainObj.getFirstName());
		result.setLastName(domainObj.getLastName());
		result.setGender(domainObj.getGender());
		result.setDateOfBirth(domainObj.getDateOfBirth());
		result.setPhoneNumber(domainObj.getPhoneNumber());
		result.setSsn(domainObj.getSsn());
		
		if(!StringUtils.isEmpty(domainObj.getAddressLine1()) ||
			!StringUtils.isEmpty(domainObj.getAddressLine2()) ||
			!StringUtils.isEmpty(domainObj.getCity()) ||
			!StringUtils.isEmpty(domainObj.getState()) ||
			!StringUtils.isEmpty(domainObj.getZipcode())) {
			AddressDTO address = new AddressDTO();
			address.setStreetLineOne(domainObj.getAddressLine1());
			address.setStreetLineTwo(domainObj.getAddressLine2());
			address.setCity(domainObj.getCity());
			address.setState(domainObj.getState());
			address.setZipcode(domainObj.getZipcode());
			result.setAddress(address);
		}
		
		return result;
	}
	
	public static DocumentDTO convert(Document domainObj) {
		DocumentDTO result = new DocumentDTO();
		result.setName(domainObj.getName());
		result.setFormat("CCDA"); //TODO:
		if(domainObj.getPatient() != null) {
			PatientDTO patient = convert(domainObj.getPatient());
			result.setPatient(patient);
		}
		return result;
	}
}
