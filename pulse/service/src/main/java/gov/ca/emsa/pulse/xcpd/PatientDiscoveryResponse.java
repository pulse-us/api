package gov.ca.emsa.pulse.xcpd;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

// cross gateway patient discovery response message
@XmlRootElement(name = "PRPA_IN201306UV02")
@XmlAccessorType(XmlAccessType.FIELD)
public class PatientDiscoveryResponse {
	
	@XmlAttribute(name = "xmlns:xsi") public String xmlnsxsi = "http://www.w3.org/2001/XMLSchema-instance";
	@XmlAttribute(name = "xmlns") public String xmlns = "urn:hl7-org:v3";
	@XmlAttribute(name = "xsi:schemaLocation") public String xsiSchema = "PRPA_IN201306UV02.xsd";
	@XmlAttribute(name = "ITSVersion") public String itsVersion = "XML_1.0";
	
	public CreationTime creationTime;
	
	public InteractionId interactionId;
	
	public ProcessingCode processingCode;
	
	public ProcessingModeCode processingModeCode;

	public AcceptAckCode acceptAckCode;
	
	public Receiver receiver;
	
	public Sender sender;
	
	public Acknowledgement acknowledgement;
	
	public ControlActProcess controlActProcess;

}
