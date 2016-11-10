package gov.ca.emsa.pulse.broker.dto;

import gov.ca.emsa.pulse.broker.dao.NameAssemblyDAO;
import gov.ca.emsa.pulse.broker.dao.NameRepresentationDAO;
import gov.ca.emsa.pulse.broker.dao.NameTypeDAO;
import gov.ca.emsa.pulse.broker.entity.NameAssemblyEntity;
import gov.ca.emsa.pulse.broker.entity.NameRepresentationEntity;
import gov.ca.emsa.pulse.broker.entity.NameTypeEntity;
import gov.ca.emsa.pulse.common.domain.Address;
import gov.ca.emsa.pulse.common.domain.Document;
import gov.ca.emsa.pulse.common.domain.GivenName;
import gov.ca.emsa.pulse.common.domain.NameAssembly;
import gov.ca.emsa.pulse.common.domain.NameRepresentation;
import gov.ca.emsa.pulse.common.domain.NameType;
import gov.ca.emsa.pulse.common.domain.Patient;
import gov.ca.emsa.pulse.common.domain.PatientRecordName;
import gov.ca.emsa.pulse.common.domain.PatientRecord;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import gov.ca.emsa.pulse.common.domain.AlternateCareFacility;

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
		result.setOrganizationPatientRecordId(domainObj.getOrgPatientRecordId());
		if(domainObj.getPatientRecordName() != null){
			for(PatientRecordName patientRecordName : domainObj.getPatientRecordName()){
				PatientRecordNameDTO patientRecordNameDTO = new PatientRecordNameDTO();
				patientRecordNameDTO.setFamilyName(patientRecordName.getFamilyName());
				ArrayList<GivenNameDTO> givens = new ArrayList<GivenNameDTO>();
				for(String given : patientRecordName.getGivens()){
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
		result.setFormat(domainObj.getFormat());
		result.setClassName(domainObj.getClassName());
		result.setConfidentiality(domainObj.getConfidentiality());
		result.setCreationTime(domainObj.getCreationTime());
		result.setDescription(domainObj.getDescription());
		result.setSize(domainObj.getSize());
		result.setPatientOrgMapId(domainObj.getOrgMapId());

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
