package gov.ca.emsa.pulse.xcpd;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

public class Subject {
	@XmlAttribute public String typeCode;
	
	public RegistrationEvent re;

}
