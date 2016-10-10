package gov.ca.emsa.pulse.broker.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

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
	
	@Column(name="name_type_code")
	private String nameTypeCode;
	
	@Column(name="name_type_code_description")
	private String nameTypeCodeDescription;
	
	@Column(name="name_representation_code")
	private String nameRepresentationCode;
	
	@Column(name="name_representation_code_description")
	private String nameRepresentationCodeDescription;
	
	@Column(name="name_assembly_order_code")
	private String nameAssemblyOrderCode;
	
	@Column(name="name_assembly_oder_code_description")
	private String nameAssemblyOrderCodeDescription;
	
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

	public String getNameTypeCode() {
		return nameTypeCode;
	}

	public void setNameTypeCode(String nameTypeCode) {
		this.nameTypeCode = nameTypeCode;
	}

	public String getNameTypeCodeDescription() {
		return nameTypeCodeDescription;
	}

	public void setNameTypeCodeDescription(String nameTypeCodeDescription) {
		this.nameTypeCodeDescription = nameTypeCodeDescription;
	}

	public String getNameRepresentationCode() {
		return nameRepresentationCode;
	}

	public void setNameRepresentationCode(String nameRepresentationCode) {
		this.nameRepresentationCode = nameRepresentationCode;
	}

	public String getNameRepresentationCodeDescription() {
		return nameRepresentationCodeDescription;
	}

	public void setNameRepresentationCodeDescription(
			String nameRepresentationCodeDescription) {
		this.nameRepresentationCodeDescription = nameRepresentationCodeDescription;
	}

	public String getNameAssemblyOrderCode() {
		return nameAssemblyOrderCode;
	}

	public void setNameAssemblyOrderCode(String nameAssemblyOrderCode) {
		this.nameAssemblyOrderCode = nameAssemblyOrderCode;
	}

	public String getNameAssemblyOrderCodeDescription() {
		return nameAssemblyOrderCodeDescription;
	}

	public void setNameAssemblyOrderCodeDescription(
			String nameAssemblyOrderCodeDescription) {
		this.nameAssemblyOrderCodeDescription = nameAssemblyOrderCodeDescription;
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
	
	
}
