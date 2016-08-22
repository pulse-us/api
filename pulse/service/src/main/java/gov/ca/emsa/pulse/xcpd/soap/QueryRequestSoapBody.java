package gov.ca.emsa.pulse.xcpd.soap;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import gov.ca.emsa.pulse.xcpd.aqr.AdhocQueryRequest;

public class QueryRequestSoapBody {
	@XmlAttribute(name = "xmlns:query") public String xmlnsQuery = "urn:oasis:names:tc:ebxml-regrep:xsd:query:3.0";
	@XmlAttribute(name = "xmlns:rim") public String xmlnsRim = "urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0";
	@XmlAttribute(name = "xmlns:rs") public String xmlnsRs = "urn:oasis:names:tc:ebxml-regrep:xsd:rs:3.0";
	@XmlElement(name = "AdhocQueryRequest") public AdhocQueryRequest adhocQueryRequest;
}
