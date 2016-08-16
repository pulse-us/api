package gov.ca.emsa.pulse.xcpd;

public class LivingSubjectAdminGender {
	private LivingSubjectAdminGenderValue value;
	public String semanticsText;

	public LivingSubjectAdminGender() {
	}

	public LivingSubjectAdminGenderValue getLivingSubjectAdministrativeGender() {
		return value;
	}

	public void setLivingSubjectAdministrativeGender(
			LivingSubjectAdminGenderValue livingSubjectAdministrativeGender) {
		this.value = livingSubjectAdministrativeGender;
	}
}