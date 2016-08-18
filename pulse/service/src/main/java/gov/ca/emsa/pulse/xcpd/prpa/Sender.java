package gov.ca.emsa.pulse.xcpd.prpa;

import gov.ca.emsa.pulse.xcpd.Device;

import javax.xml.bind.annotation.XmlAttribute;

public class Sender {
	@XmlAttribute public String typeCode;
	public Device device;

}