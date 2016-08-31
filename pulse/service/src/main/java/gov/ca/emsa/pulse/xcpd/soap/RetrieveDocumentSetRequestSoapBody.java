package gov.ca.emsa.pulse.xcpd.soap;

import gov.ca.emsa.pulse.xcpd.rds.RetrieveDocumentSetRequest;

import javax.xml.bind.annotation.XmlElement;

public class RetrieveDocumentSetRequestSoapBody {
	@XmlElement(name = "RetrieveDocumentSetRequest") public RetrieveDocumentSetRequest documentSetRequest;
}
