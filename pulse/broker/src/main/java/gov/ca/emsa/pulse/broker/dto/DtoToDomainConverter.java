package gov.ca.emsa.pulse.broker.dto;

import gov.ca.emsa.pulse.broker.domain.Address;
import gov.ca.emsa.pulse.broker.domain.AlternateCareFacility;
import gov.ca.emsa.pulse.broker.domain.Document;
import gov.ca.emsa.pulse.broker.domain.Patient;
import gov.ca.emsa.pulse.broker.domain.PatientRecord;

public class DtoToDomainConverter {
	
	public static PatientRecord convert(PatientRecordDTO dtoObj) {
		PatientRecord result = new PatientRecord();
		result.setId(dtoObj.getId().toString());
		result.setFirstName(dtoObj.getFirstName());
		result.setLastName(dtoObj.getLastName());
		result.setGender(dtoObj.getGender());
		result.setDateOfBirth(dtoObj.getDateOfBirth());
		result.setPhoneNumber(dtoObj.getPhoneNumber());
		result.setSsn(dtoObj.getSsn());
		
		if(dtoObj.getAddress() != null) {
			Address addr = new Address(dtoObj.getAddress());
			result.setAddress(addr);
		}
		
		return result;
	}
	
	public static Patient convert(PatientDTO dtoObj) {
		Patient result = new Patient();
		result.setId(dtoObj.getId());
		result.setFirstName(dtoObj.getFirstName());
		result.setLastName(dtoObj.getLastName());
		result.setGender(dtoObj.getGender());
		result.setDateOfBirth(dtoObj.getDateOfBirth());
		result.setPhoneNumber(dtoObj.getPhoneNumber());
		result.setSsn(dtoObj.getSsn());
		
		if(dtoObj.getAcf() != null) {
			AlternateCareFacility acf = new AlternateCareFacility();
			acf.setId(dtoObj.getAcf().getId());
			acf.setName(dtoObj.getAcf().getName());
			if(dtoObj.getAcf().getAddress() != null)  {
				AddressDTO acfAddrDto = dtoObj.getAcf().getAddress();
				Address acfAddr = new Address(acfAddrDto);
				acf.setAddress(acfAddr);
			}
			result.setAcf(acf);
		}
		
		if(dtoObj.getAddress() != null) {
			Address addr = new Address(dtoObj.getAddress());
			result.setAddress(addr);
		}
		
		return result;
	}
	
	public static Document convert(DocumentDTO dtoObj) {
		Document result = new Document();
		result.setId(dtoObj.getId()+"");
		result.setName(dtoObj.getName());
		result.setOrgMapId(dtoObj.getPatientOrgMapId());
		return result;
	}
}
