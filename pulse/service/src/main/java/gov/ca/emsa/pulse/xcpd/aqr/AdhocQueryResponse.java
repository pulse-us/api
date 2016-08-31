package gov.ca.emsa.pulse.xcpd.aqr;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class AdhocQueryResponse {
	@XmlElement(name = "RegistryObjectList") public RegistryObjectList registryObjectList;
	@XmlAttribute(name = "status") public String status;
}
