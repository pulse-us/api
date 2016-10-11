package gov.ca.emsa.pulse.broker.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="patient_name")
public class PatientNameEntity {
	
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column( name = "id", nullable = false )
	private Long id;
	
	@Column(name = "given_name_id")
	private Long givenNameId;
	
	@OneToMany( fetch = FetchType.LAZY, mappedBy = "patientNameId", cascade = CascadeType.ALL  )
	@Column( name = "given_name_id", nullable = false  )
	private List<GivenNameEntity> givenNames = new ArrayList<GivenNameEntity>();
	
	@Column(name="family_name")
	private String familyName;
	
	@Column(name="suffix")
	private String suffix;
	
	@Column(name="prefix")
	private String prefix;
	
	@Column(name = "prof_suffix")
	private String profSuffix;
	
	@OneToOne
	@JoinColumn(name="id")
	private NameTypeEntity nameType;
	
	@OneToOne
	@JoinColumn(name="id")
	private NameRepresentationEntity nameRepresentation;
	
	@OneToOne
	@JoinColumn(name="id")
	private NameAssemblyEntity nameAssembly;
	
	@Column(name="effective_date")
	private Date effectiveDate;
	
	@Column(name="expiration_date")
	private Date expirationDate;
	
	@Column( name = "last_read_date", insertable = false, updatable = false)
	private Date lastReadDate;
	
	@Column( name = "creation_date", insertable = false, updatable = false)
	private Date creationDate;
	
	@Column( name = "last_modified_date", insertable = false, updatable = false)
	private Date lastModifiedDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<GivenNameEntity> getGivenNames() {
		return givenNames;
	}

	public void setGivenNames(List<GivenNameEntity> givenNames) {
		this.givenNames = givenNames;
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

	public Long getGivenNameId() {
		return givenNameId;
	}

	public void setGivenNameId(Long givenNameId) {
		this.givenNameId = givenNameId;
	}

	public NameTypeEntity getNameType() {
		return nameType;
	}

	public void setNameType(NameTypeEntity nameTypeCode) {
		this.nameType = nameTypeCode;
	}

	public NameRepresentationEntity getNameRepresentation() {
		return nameRepresentation;
	}

	public void setNameRepresentation(NameRepresentationEntity nameRepresentation) {
		this.nameRepresentation = nameRepresentation;
	}

	public NameAssemblyEntity getNameAssembly() {
		return nameAssembly;
	}

	public void setNameAssembly(NameAssemblyEntity nameAssembly) {
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

	public Date getLastReadDate() {
		return lastReadDate;
	}

	public void setLastReadDate(Date lastReadDate) {
		this.lastReadDate = lastReadDate;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public String getProfSuffix() {
		return profSuffix;
	}

	public void setProfSuffix(String profSuffix) {
		this.profSuffix = profSuffix;
	}
	
	
}
