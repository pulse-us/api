package gov.ca.emsa.pulse.xcpd.rds;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class RetrieveDocumentSetRequest {
	@XmlElement(name = "DocumentRequest") public ArrayList<DocumentRequest> documentRequest;
}
