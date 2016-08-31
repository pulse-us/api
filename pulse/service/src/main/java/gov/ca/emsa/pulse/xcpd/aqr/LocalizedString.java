package gov.ca.emsa.pulse.xcpd.aqr;

import javax.xml.bind.annotation.XmlAttribute;

public class LocalizedString {
	@XmlAttribute public String charset;
	@XmlAttribute public String value;
	@XmlAttribute(name = "xml:lang") public String xmllang;
	
}
