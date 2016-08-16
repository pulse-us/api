package gov.ca.emsa.pulse.xcpd;

import javax.xml.bind.annotation.XmlAttribute;

import gov.ca.emsa.pulse.xcpd.Id;

public class ProviderOrganization {
	@XmlAttribute private String classCode;
	@XmlAttribute private String determinderCode;
	private Id id;
	private ProviderOrgName pon;
	private ContactParty cp;

	public ProviderOrganization() {
	}

	public Id getId() {
		return id;
	}

	public void setId(Id id) {
		this.id = id;
	}

	public ProviderOrgName getPon() {
		return pon;
	}

	public void setPon(ProviderOrgName pon) {
		this.pon = pon;
	}

	public ContactParty getCp() {
		return cp;
	}

	public void setCp(ContactParty cp) {
		this.cp = cp;
	}
}