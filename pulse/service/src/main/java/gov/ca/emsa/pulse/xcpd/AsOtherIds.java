package gov.ca.emsa.pulse.xcpd;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class AsOtherIds {
	@XmlAttribute public String classCode;
	
	@XmlRootElement(name = "id")
	public class Id{
		@XmlAttribute public String root;
		@XmlAttribute public String extension;
	}
	
	@XmlRootElement(name = "scopingOrganization")
	public class ScopingOrganization{
		@XmlAttribute public String classCode;
		@XmlAttribute public String determinerCode;
		
		@XmlRootElement(name = "id")
		public class Id{
			@XmlAttribute public String root;
			@XmlAttribute public String extension;
		}
	}
}
