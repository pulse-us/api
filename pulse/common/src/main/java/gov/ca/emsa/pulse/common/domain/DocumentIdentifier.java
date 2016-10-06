package gov.ca.emsa.pulse.common.domain;

public class DocumentIdentifier {
	//required to get the document back later
	private String homeCommunityId; //64 chars
	private String repositoryUniqueId;
	private String documentUniqueId;
		
	public DocumentIdentifier() {}
	
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
