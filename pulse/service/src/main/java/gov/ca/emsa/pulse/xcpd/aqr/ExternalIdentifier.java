package gov.ca.emsa.pulse.xcpd.aqr;

import javax.xml.bind.annotation.XmlElement;

public class ExternalIdentifier {
	@XmlElement(name = "Name") public Name name;
	@XmlElement(name = "Description") public Description description;
}
