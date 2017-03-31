package gov.ca.emsa.pulse.broker.entity;

import java.util.Date;

import javax.persistence.Basic;
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

import org.hibernate.annotations.ColumnTransformer;

@Entity
@Table(name="document")
public class DocumentEntity {
	
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column( name = "id", nullable = false )
	private Long id;
	
	@Column(name="patient_endpoint_map_id")
	private Long patientEndpointMapId;
	
	//metadata
	@Column(name="name_enc")
	@ColumnTransformer(
			read = "pgp_pub_decrypt(name_enc, dearmor((SELECT * from private_key())))", 
			write = "pgp_pub_encrypt(?, dearmor((SELECT * from public_key())))")
	private String name;
	
	@Column(name = "format_enc")
	@ColumnTransformer(
			read = "pgp_pub_decrypt(format_enc, dearmor((SELECT * from private_key())))", 
			write = "pgp_pub_encrypt(?, dearmor((SELECT * from public_key())))")
	private String format;
	
	@Column(name = "class_name_enc")
	@ColumnTransformer(
			read = "pgp_pub_decrypt(class_name_enc, dearmor((SELECT * from private_key())))", 
			write = "pgp_pub_encrypt(?, dearmor((SELECT * from public_key())))")
	private String className;
	
	@Column(name = "confidentiality_enc")
	@ColumnTransformer(
			read = "pgp_pub_decrypt(confidentiality_enc, dearmor((SELECT * from private_key())))", 
			write = "pgp_pub_encrypt(?, dearmor((SELECT * from public_key())))")
	private String confidentiality;
	
	@Column(name = "description_enc")
	@ColumnTransformer(
			read = "pgp_pub_decrypt(description_enc, dearmor((SELECT * from private_key())))", 
			write = "pgp_pub_encrypt(?, dearmor((SELECT * from public_key())))")
	private String description;
	
	@Column(name = "size_enc")
	@ColumnTransformer(
			read = "pgp_pub_decrypt(size_enc, dearmor((SELECT * from private_key())))", 
			write = "pgp_pub_encrypt(?, dearmor((SELECT * from public_key())))")
	private String size;
	
	@Column(name = "doc_creation_time_enc")
	@ColumnTransformer(
			read = "pgp_pub_decrypt(doc_creation_time_enc, dearmor((SELECT * from private_key())))", 
			write = "pgp_pub_encrypt(?, dearmor((SELECT * from public_key())))")
	private String creationTime;
		
	//required to get the document back later
	@Column(name = "home_community_id")
	private String homeCommunityId; 
	
	@Column(name = "repository_unique_id")
	private String repositoryUniqueId;
	
	@Column(name = "document_unique_id")
	private String documentUniqueId;
	
	@Column(name = "contents_enc")
	@ColumnTransformer(
			read = "pgp_pub_decrypt(contents_enc, dearmor((SELECT * from private_key())))", 
			write = "pgp_pub_encrypt(?, dearmor((SELECT * from public_key())))")
	private byte[] contents;
	
	@OneToOne(optional = true, fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@JoinColumn(name = "patient_endpoint_map_id", unique=true, nullable = true, insertable=false, updatable= false)
	private PatientEndpointMapEntity patientEndpointMap;
	
	@Column(name = "status_id")
	private Long statusId;
	
	@Basic( optional = true )
	@OneToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "status_id", unique=true, nullable = false, insertable=false, updatable= false)
	private QueryEndpointStatusEntity status;
	
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

	public Long getPatientEndpointMapId() {
		return patientEndpointMapId;
	}

	public void setPatientEndpointMapId(Long patientEndpointMapId) {
		this.patientEndpointMapId = patientEndpointMapId;
	}

	public PatientEndpointMapEntity getPatientEndpointMap() {
		return patientEndpointMap;
	}

	public void setPatientEndpointMap(PatientEndpointMapEntity patientEndpointMap) {
		this.patientEndpointMap = patientEndpointMap;
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

	public Long getStatusId() {
		return statusId;
	}

	public void setStatusId(Long statusId) {
		this.statusId = statusId;
	}

	public QueryEndpointStatusEntity getStatus() {
		return status;
	}

	public void setStatus(QueryEndpointStatusEntity status) {
		this.status = status;
	}
}