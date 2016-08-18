package gov.ca.emsa.pulse.xcpd.soap.header;

import javax.xml.bind.annotation.XmlElement;

public class MessageId {
	@XmlElement(name = "a:MessageID")public String messageId;
}
