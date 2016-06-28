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
			Address addr = new Address();
			addr.setStreetLine1(dtoObj.getAddress().getStreetLineOne());
			addr.setStreetLine1(dtoObj.getAddress().getStreetLineTwo());
			addr.setCity(dtoObj.getAddress().getCity());
			addr.setState(dtoObj.getAddress().getState());
			addr.setZipcode(dtoObj.getAddress().getZipcode());
			result.setAddress(addr);
		}
		
		return result;
	}
	
	public static Patient convert(PatientDTO dtoObj) {
		Patient result = new Patient();
		result.setId(dtoObj.getId().toString());
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
				Address acfAddr = new Address();
				acfAddr.setStreetLine1(acfAddrDto.getStreetLineOne());
				acfAddr.setStreetLine1(acfAddrDto.getStreetLineTwo());
				acfAddr.setCity(acfAddrDto.getCity());
				acfAddr.setState(acfAddrDto.getState());
				acfAddr.setZipcode(acfAddrDto.getZipcode());
				acf.setAddress(acfAddr);
			}
			result.setAcf(acf);
		}
		
		if(dtoObj.getAddress() != null) {
			Address addr = new Address();
			addr.setStreetLine1(dtoObj.getAddress().getStreetLineOne());
			addr.setStreetLine1(dtoObj.getAddress().getStreetLineTwo());
			addr.setCity(dtoObj.getAddress().getCity());
			addr.setState(dtoObj.getAddress().getState());
			addr.setZipcode(dtoObj.getAddress().getZipcode());
			result.setAddress(addr);
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
