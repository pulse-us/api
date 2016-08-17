package gov.ca.emsa.pulse.xcpd;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
@XmlAccessorType(XmlAccessType.FIELD)
public class Device{
	public String classCode;
	public String determinerCode;
	
	public Id id;
	
	public Telecom telecom;

	public String getClassCode() {
		return classCode;
	}

	public void setClassCode(String classCode) {
		this.classCode = classCode;
	}

	public String getDeterminerCode() {
		return determinerCode;
	}

	public void setDeterminerCode(String determinerCode) {
		this.determinerCode = determinerCode;
	}

	public Id getId() {
		return id;
	}

	public void setId(Id id) {
		this.id = id;
	}

	public Telecom getTelecom() {
		return telecom;
	}

	public void setTelecom(Telecom telecom) {
		this.telecom = telecom;
	}
	
	
}
