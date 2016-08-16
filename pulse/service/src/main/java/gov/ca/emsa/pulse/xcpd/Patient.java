package gov.ca.emsa.pulse.xcpd;

import javax.xml.bind.annotation.XmlAttribute;

import gov.ca.emsa.pulse.xcpd.Id;
import gov.ca.emsa.pulse.xcpd.StatusCode;

public class Patient {
	@XmlAttribute String classCode;
	private Id id;
	private StatusCode sc;
	private PatientPerson pp;
	private ProviderOrganization po;
	private SubjectOfOne soo;

	public Patient() {
	}

	public Id getId() {
		return id;
	}

	public void setId(Id id) {
		this.id = id;
	}

	public StatusCode getSc() {
		return sc;
	}

	public void setSc(StatusCode sc) {
		this.sc = sc;
	}

	public PatientPerson getPp() {
		return pp;
	}

	public void setPp(PatientPerson pp) {
		this.pp = pp;
	}

	public ProviderOrganization getPo() {
		return po;
	}

	public void setPo(ProviderOrganization po) {
		this.po = po;
	}

	public SubjectOfOne getSoo() {
		return soo;
	}

	public void setSoo(SubjectOfOne soo) {
		this.soo = soo;
	}
}