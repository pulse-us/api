package gov.ca.emsa.pulse.xcpd.prpa.cap.subj;

import gov.ca.emsa.pulse.xcpd.Telecom;

import javax.xml.bind.annotation.XmlAttribute;

public class ContactParty {
	@XmlAttribute private String contactParty;
	public Telecom telecom;

	public ContactParty() {
	}

}