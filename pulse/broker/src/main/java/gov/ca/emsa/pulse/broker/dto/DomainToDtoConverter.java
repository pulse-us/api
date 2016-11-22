package gov.ca.emsa.pulse.broker.dto;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import gov.ca.emsa.pulse.broker.dao.NameAssemblyDAO;
import gov.ca.emsa.pulse.broker.dao.NameRepresentationDAO;
import gov.ca.emsa.pulse.broker.dao.NameTypeDAO;
import gov.ca.emsa.pulse.common.domain.Address;
import gov.ca.emsa.pulse.common.domain.AlternateCareFacility;
import gov.ca.emsa.pulse.common.domain.Document;
import gov.ca.emsa.pulse.common.domain.Patient;
import gov.ca.emsa.pulse.common.domain.PatientRecord;
import gov.ca.emsa.pulse.common.domain.PatientRecordName;

public class DomainToDtoConverter {
	private static final Logger logger = LogManager.getLogger(DomainToDtoConverter.class);
	@Autowired static NameTypeDAO nameTypeDao;
	@Autowired static NameRepresentationDAO nameRepDao;
	@Autowired static NameAssemblyDAO nameAssemblyDao;
	
	public static PatientDTO convertToPatient(Patient domainObj) {
		PatientDTO result = new PatientDTO();
		if(domainObj.getId() != null) {
			result.setId(new Long(domainObj.getId()));
		}
		result.setFriendlyName(domainObj.getFriendlyName());
		result.setFullName(domainObj.getFullName());
		result.setGender(domainObj.getGender());
		result.setDateOfBirth(domainObj.getDateOfBirth());
		result.setSsn(domainObj.getSsn());

		if(domainObj.getAcf() != null) {
			AlternateCareFacilityDTO acf = convert(domainObj.getAcf());
			result.setAcf(acf);
		}

		return result;
	}

	public static PatientRecordDTO convertToPatientRecord(PatientRecord domainObj) {
		PatientRecordDTO result = new PatientRecordDTO();
		if(domainObj.getId() != null) {
			result.setId(new Long(domainObj.getId()));
		}
		result.setLocationPatientRecordId(domainObj.getLocationPatientRecordId());
		if(domainObj.getPatientRecordName() != null){
			for(PatientRecordName patientRecordName : domainObj.getPatientRecordName()){
				PatientRecordNameDTO patientRecordNameDTO = new PatientRecordNameDTO();
				patientRecordNameDTO.setFamilyName(patientRecordName.getFamilyName());
				ArrayList<GivenNameDTO> givens = new ArrayList<GivenNameDTO>();
				for(String given : patientRecordName.getGivenName()){
					GivenNameDTO givenName = new GivenNameDTO();
					givenName.setGivenName(given);
					givens.add(givenName);
				}
				patientRecordNameDTO.setGivenName(givens);
				if(patientRecordName.getSuffix() != null)
					patientRecordNameDTO.setSuffix(patientRecordName.getSuffix());
				if(patientRecordName.getPrefix() != null)
					patientRecordNameDTO.setPrefix(patientRecordName.getPrefix());
				if(patientRecordName.getNameType() != null){
					NameTypeDTO nameType = nameTypeDao.getByCode(patientRecordName.getNameType().getCode());
					patientRecordNameDTO.setNameType(nameType);
					patientRecordNameDTO.setNameTypeId(nameType.getId());
				}
				if(patientRecordName.getNameRepresentation() != null){
					NameRepresentationDTO nameRep = nameRepDao.getByCode(patientRecordName.getNameRepresentation().getCode());
					patientRecordNameDTO.setNameRepresentation(nameRep);
					patientRecordNameDTO.setNameTypeId(nameRep.getId());
				}
				if(patientRecordName.getNameAssembly() != null){
					NameAssemblyDTO nameAssembly = nameAssemblyDao.getByCode(patientRecordName.getNameAssembly().getCode());
					patientRecordNameDTO.setNameAssembly(nameAssembly);
					patientRecordNameDTO.setNameTypeId(nameAssembly.getId());
				}
				if(patientRecordName.getEffectiveDate() != null)
					patientRecordNameDTO.setEffectiveDate(patientRecordName.getEffectiveDate());
				if(patientRecordName.getExpirationDate() != null)
					patientRecordNameDTO.setExpirationDate(patientRecordName.getExpirationDate());
				result.getPatientRecordName().add(patientRecordNameDTO);
			}
		}
		PatientGenderDTO pgDto = new PatientGenderDTO();
		pgDto.setCode(domainObj.getGender().getCode());
		pgDto.setDescription(domainObj.getGender().getDescription());
		result.setPatientGender(pgDto);
		result.setDateOfBirth(domainObj.getDateOfBirth());

		result.setPhoneNumber(domainObj.getPhoneNumber());
		result.setSsn(domainObj.getSsn());
		
		List<PatientRecordAddressDTO> praDto = new ArrayList<PatientRecordAddressDTO>();
		for(Address pra : domainObj.getAddress()){
			PatientRecordAddressDTO address = new PatientRecordAddressDTO();
			List<PatientRecordAddressLineDTO> lines = new ArrayList<PatientRecordAddressLineDTO>();
			for(int i=0; i<pra.getLines().size(); i++){
				PatientRecordAddressLineDTO pralDto = new PatientRecordAddressLineDTO();
				pralDto.setLine(pra.getLines().get(i));
				pralDto.setLineOrder(i);
				lines.add(pralDto);
			}
			address.setPatientRecordAddressLines(lines);
			address.setCity(pra.getCity());
			address.setState(pra.getState());
			address.setZipcode(pra.getZipcode());
			praDto.add(address);
		}
		result.setAddress(praDto);

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
		result.setPatientLocationMapId(domainObj.getLocationMapId());

		if(domainObj.getIdentifier() != null) {
			result.setDocumentUniqueId(domainObj.getIdentifier().getDocumentUniqueId());
			result.setHomeCommunityId(domainObj.getIdentifier().getHomeCommunityId());
			result.setRepositoryUniqueId(domainObj.getIdentifier().getRepositoryUniqueId());
		}
		return result;
	}
	
	public static AlternateCareFacilityDTO convert(AlternateCareFacility domainObj) {
		AlternateCareFacilityDTO result = new AlternateCareFacilityDTO();
		result.setId(domainObj.getId());
		result.setName(domainObj.getName());
		result.setPhoneNumber(domainObj.getPhoneNumber());
		if(domainObj.getAddress() != null) {
			if(domainObj.getAddress().getLines() != null) {
				for(String line : domainObj.getAddress().getLines()) {
					AddressLineDTO lineDto = new AddressLineDTO();
					lineDto.setLine(line);
					result.getLines().add(lineDto);
				}
			}
			result.setCity(domainObj.getAddress().getCity());
			result.setState(domainObj.getAddress().getState());
			result.setZipcode(domainObj.getAddress().getZipcode());
			result.setCountry(domainObj.getAddress().getCountry());
		}
		return result;
	}
}
