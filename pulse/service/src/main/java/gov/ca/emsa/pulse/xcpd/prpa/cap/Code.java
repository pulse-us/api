package gov.ca.emsa.pulse.xcpd.prpa.cap;

import javax.xml.bind.annotation.XmlAttribute;

public class Code {
	@XmlAttribute
	public String code;
	@XmlAttribute
	public String codeSystem;

	public Code() {
	}

}