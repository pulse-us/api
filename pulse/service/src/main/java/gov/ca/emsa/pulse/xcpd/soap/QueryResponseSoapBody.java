package gov.ca.emsa.pulse.xcpd.soap;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import gov.ca.emsa.pulse.xcpd.aqr.AdhocQueryResponse;
import gov.ca.emsa.pulse.xcpd.rds.RetrieveDocumentSetResponse;

public class QueryResponseSoapBody {
	@XmlElement(name = "AdhocQueryResponse") public AdhocQueryResponse adhocQueryResponse;
}
