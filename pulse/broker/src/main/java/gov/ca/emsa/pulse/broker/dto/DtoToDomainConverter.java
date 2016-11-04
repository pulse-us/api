package gov.ca.emsa.pulse.broker.dto;

import gov.ca.emsa.pulse.common.domain.Address;
import gov.ca.emsa.pulse.common.domain.AlternateCareFacility;
import gov.ca.emsa.pulse.common.domain.Document;
import gov.ca.emsa.pulse.common.domain.DocumentIdentifier;
import gov.ca.emsa.pulse.common.domain.GivenName;
import gov.ca.emsa.pulse.common.domain.NameAssembly;
import gov.ca.emsa.pulse.common.domain.NameRepresentation;
import gov.ca.emsa.pulse.common.domain.NameType;
import gov.ca.emsa.pulse.common.domain.Organization;
import gov.ca.emsa.pulse.common.domain.Patient;
import gov.ca.emsa.pulse.common.domain.PatientGender;
import gov.ca.emsa.pulse.common.domain.PatientRecordName;
import gov.ca.emsa.pulse.common.domain.PatientOrganizationMap;
import gov.ca.emsa.pulse.common.domain.PatientRecord;
import gov.ca.emsa.pulse.common.domain.PatientSearch;
import gov.ca.emsa.pulse.common.domain.Query;
import gov.ca.emsa.pulse.common.domain.QueryOrganization;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

public class DtoToDomainConverter {
	private static final Logger logger = LogManager.getLogger(DtoToDomainConverter.class);
	private static final DateTimeFormatter outFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	public static Patient convert(PatientDTO dtoObj) {
		Patient result = new Patient();
		result.setId(dtoObj.getId());
		result.setFullName(dtoObj.getFullName());
		result.setFriendlyName(dtoObj.getFriendlyName());
		result.setGender(dtoObj.getGender());
		result.setDateOfBirth(dtoObj.getDateOfBirth());
		result.setSsn(dtoObj.getSsn());
		result.setLastRead(dtoObj.getLastReadDate());
		if(dtoObj.getAcf() != null) {
			AlternateCareFacility acf = convert(dtoObj.getAcf());
			result.setAcf(acf);
		}

		if(dtoObj.getOrgMaps() != null && dtoObj.getOrgMaps().size() > 0) {
			for(PatientOrganizationMapDTO orgMapDto : dtoObj.getOrgMaps()) {
				PatientOrganizationMap orgMap = DtoToDomainConverter.convert(orgMapDto);
				result.getOrgMaps().add(orgMap);
			}
		}

		return result;
	}

	public static PatientOrganizationMap convert(PatientOrganizationMapDTO dto) {	
		PatientOrganizationMap result = new PatientOrganizationMap();
		result.setId(dto.getId());
		result.setPatientId(dto.getPatientId());
		if(dto.getOrg() != null) {
			result.setOrganization(DtoToDomainConverter.convert(dto.getOrg()));
		} else {
			Organization org = new Organization();
			org.setId(dto.getOrganizationId());
			result.setOrganization(org);
		}
		result.setDocumentsQueryStatus(dto.getDocumentsQueryStatus());
		result.setDocumentsQueryStart(dto.getDocumentsQueryStart());
		result.setDocumentsQueryEnd(dto.getDocumentsQueryEnd());

		if(dto.getDocuments() != null && dto.getDocuments().size() > 0) {
			for(DocumentDTO docDto : dto.getDocuments()) {
				Document doc = DtoToDomainConverter.convert(docDto);
				result.getDocuments().add(doc);
			}
		}
		return result;
	}

	public static Address convert(AddressDTO addressDto){
		Address address = new Address();
		address.setId(addressDto.getId());
		address.setCity(addressDto.getCity());
		address.setCountry(address.getCountry());
		address.setState(addressDto.getState());
		address.setStreet1(addressDto.getStreetLineOne());
		address.setStreet2(addressDto.getStreetLineTwo());
		address.setZipcode(addressDto.getZipcode());
		address.setCountry(addressDto.getCountry());
		return address;
	}

	public static AlternateCareFacility convert(AlternateCareFacilityDTO acfDto){
		AlternateCareFacility acf = new AlternateCareFacility();
		acf.setId(acfDto.getId());
		acf.setName(acfDto.getName());
		acf.setPhoneNumber(acfDto.getPhoneNumber());
		acf.setLastRead(acfDto.getLastReadDate());
		
		if(acfDto.hasAddressParts())  {
			Address acfAddr = new Address();
			if(acfDto.getLines() != null) {
				for(AddressLineDTO lineDto : acfDto.getLines()) {;
					acfAddr.getLines().add(lineDto.getLine());
				}
			}
			acfAddr.setCity(acfDto.getCity());
			acfAddr.setState(acfDto.getState());
			acfAddr.setZipcode(acfDto.getZipcode());
			acfAddr.setCountry(acfDto.getCountry());
			acf.setAddress(acfAddr);
		}
		return acf;
	}

	public static Query convert(QueryDTO queryDto){
		Query query = new Query();
		query.setId(queryDto.getId());
		query.setStatus(queryDto.getStatus());
		query.setLastRead(queryDto.getLastReadDate());

		try {
			ObjectMapper termReader = new ObjectMapper();
			PatientSearch terms = termReader.readValue(queryDto.getTerms(), PatientSearch.class);
			query.setTerms(terms);
		} catch(IOException ioex) {
			logger.error("Could not read " + queryDto.getTerms() + " as JSON.");
		}

		query.setUserToken(queryDto.getUserId());
		for(QueryOrganizationDTO qOrgDto : queryDto.getOrgStatuses()){
			QueryOrganization qOrg = DtoToDomainConverter.convert(qOrgDto);
			query.getOrgStatuses().add(qOrg);
		}
		return query;
	}

	public static QueryOrganization convert(QueryOrganizationDTO qOrgDto){
		QueryOrganization qOrg = new QueryOrganization();
		qOrg.setId(qOrgDto.getId());

		if(qOrgDto.getOrg() != null) {
			qOrg.setOrg(convert(qOrgDto.getOrg()));
		}

		qOrg.setQueryId(qOrgDto.getQueryId());
		for(PatientRecordDTO prDto : qOrgDto.getResults()){
			PatientRecord pr = DtoToDomainConverter.convert(prDto);
			qOrg.getResults().add(pr);
		}		
		qOrg.setStartDate(qOrgDto.getStartDate());
		qOrg.setEndDate(qOrgDto.getEndDate());
		qOrg.setStatus(qOrgDto.getStatus());
		return qOrg;
	}

	public static PatientRecord convert(PatientRecordDTO prDto){
		PatientRecord pr = new PatientRecord();
		pr.setId(prDto.getId());
		pr.setSsn(prDto.getSsn());
		if(prDto.getPatientRecordName() != null){
			for(PatientRecordNameDTO PatientRecordNameDTO : prDto.getPatientRecordName()){
				PatientRecordName patient = new PatientRecordName();
				patient.setFamilyName(PatientRecordNameDTO.getFamilyName());
				ArrayList<GivenName> givens = new ArrayList<GivenName>();
				for(GivenNameDTO givenDto : PatientRecordNameDTO.getGivenName()){
					GivenName givenName = new GivenName();
					givenName.setGivenName(givenDto.getGivenName());
					givenName.setId(givenDto.getId());
					givenName.setPatientRecordNameId(givenDto.getPatientRecordNameId());
					givens.add(givenName);
				}
				patient.setGivenName(givens);
				if(PatientRecordNameDTO.getSuffix() != null)
					patient.setSuffix(PatientRecordNameDTO.getSuffix());
				if(PatientRecordNameDTO.getPrefix() != null)
					patient.setPrefix(PatientRecordNameDTO.getPrefix());
				if(PatientRecordNameDTO.getNameType() != null){
					NameType nameType = new NameType();
					nameType.setCode(PatientRecordNameDTO.getNameType().getCode());
					nameType.setDescription(PatientRecordNameDTO.getNameType().getDescription());
					nameType.setId(PatientRecordNameDTO.getNameType().getId());
					patient.setNameType(nameType);
				}
				if(PatientRecordNameDTO.getNameRepresentation() != null){
					NameRepresentation nameRep = new NameRepresentation();
					nameRep.setCode(PatientRecordNameDTO.getNameType().getCode());
					nameRep.setDescription(PatientRecordNameDTO.getNameType().getDescription());
					nameRep.setId(PatientRecordNameDTO.getNameType().getId());
					patient.setNameRepresentation(nameRep);
				}
				if(PatientRecordNameDTO.getNameAssembly() != null){
					NameAssembly nameAssembly = new NameAssembly();
					nameAssembly.setCode(PatientRecordNameDTO.getNameType().getCode());
					nameAssembly.setDescription(PatientRecordNameDTO.getNameType().getDescription());
					nameAssembly.setId(PatientRecordNameDTO.getNameType().getId());
					patient.setNameAssembly(nameAssembly);
				}
				if(PatientRecordNameDTO.getEffectiveDate() != null)
					patient.setEffectiveDate(PatientRecordNameDTO.getEffectiveDate());
				if(PatientRecordNameDTO.getExpirationDate() != null)
					patient.setExpirationDate(PatientRecordNameDTO.getExpirationDate());
				pr.getPatientRecordName().add(patient);
			}
		}
		PatientGender pg = new PatientGender();
		pg.setCode(prDto.getPatientGender().getCode());
		pg.setDescription(prDto.getPatientGender().getDescription());
		pr.setGender(pg);
		pr.setPhoneNumber(prDto.getPhoneNumber());
		pr.setDateOfBirth(prDto.getDateOfBirth());		
		if(prDto.getAddress() != null) {
			Address address = new Address();
			address.setStreet1(prDto.getAddress().getStreetLineOne());
			address.setStreet2(prDto.getAddress().getStreetLineTwo());
			address.setCity(prDto.getAddress().getCity());
			address.setState(prDto.getAddress().getState());
			address.setZipcode(prDto.getAddress().getZipcode());
			address.setCountry(prDto.getAddress().getCountry());
			pr.setAddress(address);
		}
		return pr;
	}

	public static Organization convert(OrganizationDTO orgDto){
		Organization org = new Organization();
		org.setId(orgDto.getId());
		org.setName(orgDto.getName());
		org.setAdapter(orgDto.getAdapter());
		org.setActive(orgDto.isActive());
		org.setOrganizationId(orgDto.getOrganizationId());
		if(orgDto.getCertificationKey() != null){
			org.setCertificationKey(orgDto.getCertificationKey());
		}
		if(orgDto.getEndpointUrl() != null){
			org.setEndpointUrl(orgDto.getEndpointUrl());
		}
		if(orgDto.getIpAddress() != null){
			org.setIpAddress(orgDto.getIpAddress());
		}
		if(orgDto.getUsername() != null){
			org.setUsername(orgDto.getUsername());
		}
		if(orgDto.getPassword() != null){
			org.setPassword(orgDto.getPassword());
		}
		return org;
	}

	public static Document convert(DocumentDTO dtoObj) {
		Document result = new Document();
		result.setId(dtoObj.getId()+"");
		result.setName(dtoObj.getName());
		result.setFormat(dtoObj.getFormat());
		result.setCached(dtoObj.getContents() != null && dtoObj.getContents().length > 0);
		result.setOrgMapId(dtoObj.getPatientOrgMapId());

		result.setClassName(dtoObj.getClassName());
		result.setConfidentiality(dtoObj.getConfidentiality());
		result.setCreationTime(dtoObj.getCreationTime());
		result.setDescription(dtoObj.getDescription());
		result.setSize(dtoObj.getSize());

		DocumentIdentifier docId = new DocumentIdentifier();
		docId.setDocumentUniqueId(dtoObj.getDocumentUniqueId());
		docId.setHomeCommunityId(dtoObj.getHomeCommunityId());
		docId.setRepositoryUniqueId(dtoObj.getRepositoryUniqueId());
		result.setIdentifier(docId);
		return result;
	}
}
