package gov.ca.emsa.pulse.xcpd;

import javax.xml.bind.annotation.XmlAttribute;

public class InteractionId {
	@XmlAttribute public String root;
	@XmlAttribute public String extension;

	public InteractionId() {
	}

}