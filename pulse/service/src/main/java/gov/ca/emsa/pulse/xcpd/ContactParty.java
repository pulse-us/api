package gov.ca.emsa.pulse.xcpd;

import javax.xml.bind.annotation.XmlAttribute;

public class ContactParty {
	@XmlAttribute private String contactParty;
	private Telecom telecom;

	public ContactParty() {
	}

	public Telecom getTelecom() {
		return telecom;
	}

	public void setTelecom(Telecom telecom) {
		this.telecom = telecom;
	}
}