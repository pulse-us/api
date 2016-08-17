package gov.ca.emsa.pulse.xcpd;

import javax.xml.bind.annotation.XmlAttribute;

public class QueryId {
	@XmlAttribute String root;
	@XmlAttribute String extension;

	public QueryId() {
	}

}