package gov.ca.emsa.pulse.broker.dto;

import gov.ca.emsa.pulse.common.domain.Address;
import gov.ca.emsa.pulse.common.domain.Document;
import gov.ca.emsa.pulse.common.domain.Patient;
import gov.ca.emsa.pulse.common.domain.PatientRecord;

public class DomainToDtoConverter {

	public static PatientDTO convertToPatient(Patient domainObj) {
		PatientDTO result = new PatientDTO();
		if(domainObj.getId() != null) {
			result.setId(new Long(domainObj.getId()));
		}
		result.setGivenName(domainObj.getGivenName());
		result.setFamilyName(domainObj.getFamilyName());
		result.setGender(domainObj.getGender());
		result.setDateOfBirth(domainObj.getDateOfBirth());
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
