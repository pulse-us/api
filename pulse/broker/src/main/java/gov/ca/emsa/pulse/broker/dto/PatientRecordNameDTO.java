package gov.ca.emsa.pulse.broker.dto;

import gov.ca.emsa.pulse.broker.entity.GivenNameEntity;
import gov.ca.emsa.pulse.broker.entity.PatientRecordNameEntity;

import java.util.ArrayList;
import java.util.Date;

public class PatientRecordNameDTO {

	private Long id;
	private ArrayList<GivenNameDTO> givenName;
	private Long patientRecordId;
	private String familyName;
	private String suffix;
	private String prefix;
	private String profSuffix;
	private Long nameTypeId;
	private NameTypeDTO nameType;
	private Long nameRepresentationId;
	private NameRepresentationDTO nameRepresentation;
	private Long nameAssemblyId;
	private NameAssemblyDTO nameAssembly;
	private Date effectiveDate;
	private Date expirationDate;

	public PatientRecordNameDTO(){
		givenName = new ArrayList<GivenNameDTO>();
	}

	public PatientRecordNameDTO(PatientRecordNameEntity entity)
	{
		if(entity != null) {
			this.id = entity.getId();
			givenName = new ArrayList<GivenNameDTO>();
			this.patientRecordId = entity.getPatientRecordId();
			for(GivenNameEntity givenName: entity.getGivenNames()){
				GivenNameDTO givenNameDTO = new GivenNameDTO(givenName);
				this.givenName.add(givenNameDTO);
			}
			this.familyName = entity.getFamilyName();
			if(entity.getSuffix() != null)
				this.suffix = entity.getSuffix();
			if(entity.getPrefix() != null)
				this.prefix = entity.getPrefix();
			if(entity.getNameType() != null){
				NameTypeDTO nameTypeDTO = new NameTypeDTO(entity.getNameType());
				this.nameType = nameTypeDTO;
				this.nameTypeId = entity.getNameTypeId();
			}
			if(entity.getNameRepresentation() != null){
				NameRepresentationDTO nameRepresentationDTO = new NameRepresentationDTO(entity.getNameRepresentation());
				this.nameRepresentation = nameRepresentationDTO;
				this.nameRepresentationId = entity.getNameRepresentationId();
			}
			if(entity.getNameAssembly() != null){
				NameAssemblyDTO nameAssemblyDTO = new NameAssemblyDTO(entity.getNameAssembly());
				this.nameAssembly = nameAssemblyDTO;
				this.nameTypeId = entity.getNameTypeId();
			}
			if(entity.getEffectiveDate() != null)
				this.effectiveDate = entity.getEffectiveDate();
			if(entity.getExpirationDate() != null)
				this.expirationDate = entity.getExpirationDate();
		}
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public ArrayList<GivenNameDTO> getGivenName() {
		return givenName;
	}
	public void setGivenName(ArrayList<GivenNameDTO> givenName) {
		this.givenName = givenName;
	}
	public Long getPatientRecordId() {
		return patientRecordId;
	}

	public void setPatientRecordId(Long patientRecordId) {
		this.patientRecordId = patientRecordId;
	}
	public String getFamilyName() {
		return familyName;
	}
	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	}
	public String getSuffix() {
		return suffix;
	}
	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}
	public String getPrefix() {
		return prefix;
	}
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	public NameTypeDTO getNameType() {
		return nameType;
	}
	
	public Long getNameTypeId() {
		return nameTypeId;
	}

	public void setNameTypeId(Long nameTypeId) {
		this.nameTypeId = nameTypeId;
	}

	public Long getNameRepresentationId() {
		return nameRepresentationId;
	}

	public void setNameRepresentationId(Long nameRepresentationId) {
		this.nameRepresentationId = nameRepresentationId;
	}

	public Long getNameAssemblyId() {
		return nameAssemblyId;
	}

	public void setNameAssemblyId(Long nameAssemblyId) {
		this.nameAssemblyId = nameAssemblyId;
	}

	public void setNameType(NameTypeDTO nameType) {
		this.nameType = nameType;
	}

	public NameRepresentationDTO getNameRepresentation() {
		return nameRepresentation;
	}

	public void setNameRepresentation(NameRepresentationDTO nameRepresentation) {
		this.nameRepresentation = nameRepresentation;
	}

	public NameAssemblyDTO getNameAssembly() {
		return nameAssembly;
	}

	public void setNameAssembly(NameAssemblyDTO nameAssembly) {
		this.nameAssembly = nameAssembly;
	}

	public Date getEffectiveDate() {
		return effectiveDate;
	}
	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}
	public Date getExpirationDate() {
		return expirationDate;
	}
	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	public String getProfSuffix() {
		return profSuffix;
	}

	public void setProfSuffix(String profSuffix) {
		this.profSuffix = profSuffix;
	}

	
}
