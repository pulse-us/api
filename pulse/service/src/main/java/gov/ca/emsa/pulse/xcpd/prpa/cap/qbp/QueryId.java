package gov.ca.emsa.pulse.xcpd.prpa.cap.qbp;

import javax.xml.bind.annotation.XmlAttribute;

public class QueryId {
	@XmlAttribute
	public String root;
	@XmlAttribute
	public String extension;

	public QueryId() {
	}

}