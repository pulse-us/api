package gov.ca.emsa.pulse.broker.dto;

import gov.ca.emsa.pulse.broker.domain.MockPatient;

public class SearchResultConverter {
	
	public static PatientRecordDTO convertToPatientRecord(MockPatient domainObj) {
		PatientRecordDTO result = new PatientRecordDTO();
		result.setGivenName(domainObj.getGivenName());
		result.setFamilyName(domainObj.getFamilyName());
		result.setGender(domainObj.getGender());
		result.setDateOfBirth(domainObj.getDateOfBirth());
		result.setPhoneNumber(domainObj.getPhoneNumber());
		result.setSsn(domainObj.getSsn());
		result.setOrgPatientId(domainObj.getOrgPatientId());
		
		AddressDTO address = new AddressDTO();
		address.setStreetLineOne(domainObj.getAddressLine1());
		address.setStreetLineTwo(domainObj.getAddressLine2());
		address.setCity(domainObj.getCity());
		address.setState(domainObj.getState());
		address.setZipcode(domainObj.getZipcode());
		result.setAddress(address);
		
		return result;
	}
}
