package gov.ca.emsa.pulse.xcpd.rds;

import javax.xml.bind.annotation.XmlElement;

public class DocumentRequest {
	@XmlElement(name = "RepositoryUniqueId") public String repositoryUniqueId;
	@XmlElement(name = "DocumentUniqueId") public String documentUniqueId;
}
