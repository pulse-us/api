package gov.ca.emsa.pulse.broker.dto;

import gov.ca.emsa.pulse.broker.domain.Document;
import gov.ca.emsa.pulse.broker.domain.Patient;

public class DtoToDomainConverter {
	
	public static Patient convert(PatientDTO dtoObj) {
		Patient result = new Patient();
		result.setFirstName(dtoObj.getFirstName());
		result.setLastName(dtoObj.getLastName());
		result.setGender(dtoObj.getGender());
		result.setDateOfBirth(dtoObj.getDateOfBirth());
		result.setPhoneNumber(dtoObj.getPhoneNumber());
		result.setSsn(dtoObj.getSsn());
		
		if(dtoObj.getAddress() != null) {
			result.setAddressLine1(dtoObj.getAddress().getStreetLineOne());
			result.setAddressLine2(dtoObj.getAddress().getStreetLineTwo());
			result.setCity(dtoObj.getAddress().getCity());
			result.setState(dtoObj.getAddress().getState());
			result.setZipcode(dtoObj.getAddress().getZipcode());
			
		}
		
		return result;
	}
	
	public static Document convert(DocumentDTO dtoObj) {
		Document result = new Document();
		result.setId(dtoObj.getId()+"");
		result.setName(dtoObj.getName());
		if(dtoObj.getPatient() != null) {
			Patient patient = convert(dtoObj.getPatient());
			result.setPatient(patient);
		}
		return result;
	}
}
