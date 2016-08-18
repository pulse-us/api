package gov.ca.emsa.pulse.xcpd.prpa;

import gov.ca.emsa.pulse.xcpd.Device;

import javax.xml.bind.annotation.XmlAttribute;

public class Receiver {
	@XmlAttribute public String typeCode;
	public Device device;

	public Receiver() {
	}
	
}