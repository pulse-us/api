package gov.ca.emsa.pulse.common.domain;

import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType.DocumentRequest;

import java.util.List;

public class DocumentRetrieve {
	private List<DocumentRequest> docRequests;

	public List<DocumentRequest> getDocRequests() {
		return docRequests;
	}

	public void setDocRequests(List<DocumentRequest> docRequests) {
		this.docRequests = docRequests;
	}
	
}
