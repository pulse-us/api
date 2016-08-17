package gov.ca.emsa.pulse.xcpd;

import javax.xml.bind.annotation.XmlAttribute;

public class RegistrationEvent {
	@XmlAttribute String classCode;
	@XmlAttribute String moodCode;
	private Id id;
	private StatusCode sc;
	private SubjectOne so;
	private Custodian custodian;

	public RegistrationEvent() {
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

	public SubjectOne getSo() {
		return so;
	}

	public void setSo(SubjectOne so) {
		this.so = so;
	}

	public Custodian getCustodian() {
		return custodian;
	}

	public void setCustodian(Custodian custodian) {
		this.custodian = custodian;
	}
}