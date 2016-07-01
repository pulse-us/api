package gov.ca.emsa.pulse.broker.entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="document")
public class DocumentEntity {
	
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column( name = "id", nullable = false )
	private Long id;
	
	@Column(name="patient_organization_map_id")
	private Long patientOrgMapId;
	
	@Column(name="name")
	private String name;
	
	@Column(name = "format")
	private String format;
	
	@Column(name = "contents")
	private byte[] contents;
	
	@OneToOne(optional = true, fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@JoinColumn(name = "patient_organization_map_id", unique=true, nullable = true, insertable=false, updatable= false)
	private PatientOrganizationMapEntity patientOrgMap;
	
	@Column( name = "creation_date", insertable = false, updatable = false)
	private Date creationDate;
	
	@Column( name = "last_modified_date", insertable = false, updatable = false)
	private Date lastModifiedDate;

	@Column( name = "last_read_date")
	private Date lastReadDate;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public Date getLastReadDate() {
		return lastReadDate;
	}

	public void setLastReadDate(Date lastReadDate) {
		this.lastReadDate = lastReadDate;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public byte[] getContents() {
		return contents;
	}

	public void setContents(byte[] contents) {
		this.contents = contents;
	}

	public Long getPatientOrgMapId() {
		return patientOrgMapId;
	}

	public void setPatientOrgMapId(Long patientOrgMapId) {
		this.patientOrgMapId = patientOrgMapId;
	}

	public PatientOrganizationMapEntity getPatientOrgMap() {
		return patientOrgMap;
	}

	public void setPatientOrgMap(PatientOrganizationMapEntity patientOrgMap) {
		this.patientOrgMap = patientOrgMap;
	}
}