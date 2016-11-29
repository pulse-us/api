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
	
	@Column(name="patient_location_map_id")
	private Long patientLocationMapId;
	
	//metadata
	@Column(name="name")
	private String name;
	
	@Column(name = "format")
	private String format;
	
	@Column(name = "class_name")
	private String className;
	
	@Column(name = "confidentiality")
	private String confidentiality;
	
	@Column(name = "description")
	private String description;
	
	@Column(name = "size")
	private String size;
	
	@Column(name = "doc_creation_time")
	private String creationTime;
		
	//required to get the document back later
	@Column(name = "home_community_id")
	private String homeCommunityId; 
	
	@Column(name = "repository_unique_id")
	private String repositoryUniqueId;
	
	@Column(name = "document_unique_id")
	private String documentUniqueId;
	
	@Column(name = "contents")
	private byte[] contents;
	
	@OneToOne(optional = true, fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@JoinColumn(name = "patient_location_map_id", unique=true, nullable = true, insertable=false, updatable= false)
	private PatientLocationMapEntity patientLocationMap;
	
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

	public Long getPatientLocationMapId() {
		return patientLocationMapId;
	}

	public void setPatientLocationMapId(Long patientLocationMapId) {
		this.patientLocationMapId = patientLocationMapId;
	}

	public PatientLocationMapEntity getPatientLocationMap() {
		return patientLocationMap;
	}

	public void setPatientLocationMap(PatientLocationMapEntity patientLocationMap) {
		this.patientLocationMap = patientLocationMap;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getConfidentiality() {
		return confidentiality;
	}

	public void setConfidentiality(String confidentiality) {
		this.confidentiality = confidentiality;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(String creationTime) {
		this.creationTime = creationTime;
	}

	public String getHomeCommunityId() {
		return homeCommunityId;
	}

	public void setHomeCommunityId(String homeCommunityId) {
		this.homeCommunityId = homeCommunityId;
	}

	public String getRepositoryUniqueId() {
		return repositoryUniqueId;
	}

	public void setRepositoryUniqueId(String repositoryUniqueId) {
		this.repositoryUniqueId = repositoryUniqueId;
	}

	public String getDocumentUniqueId() {
		return documentUniqueId;
	}

	public void setDocumentUniqueId(String documentUniqueId) {
		this.documentUniqueId = documentUniqueId;
	}
}