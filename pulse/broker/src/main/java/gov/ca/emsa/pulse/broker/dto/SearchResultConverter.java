package gov.ca.emsa.pulse.broker.dto;

import gov.ca.emsa.pulse.common.domain.Patient;

public class SearchResultConverter {
	
	public static PatientRecordDTO convertToPatientRecord(Patient domainObj) {
		PatientRecordDTO result = new PatientRecordDTO();
		result.setGivenName(domainObj.getGivenName());
		result.setFamilyName(domainObj.getFamilyName());
		result.setGender(domainObj.getGender());
		result.setDateOfBirth(domainObj.getDateOfBirth());
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
