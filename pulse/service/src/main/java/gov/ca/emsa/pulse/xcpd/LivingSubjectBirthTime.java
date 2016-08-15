package gov.ca.emsa.pulse.xcpd;

import javax.xml.bind.annotation.XmlAttribute;

public class LivingSubjectBirthTime {
	LivingSubjBirthTimeValue value;
	String semanticsText;

	public LivingSubjectBirthTime() {
	}

	public void setValue(LivingSubjBirthTimeValue lsbtv) {
		this.value = lsbtv;
	}
}