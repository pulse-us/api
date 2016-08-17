package gov.ca.emsa.pulse.xcpd;

import java.util.ArrayList;

public class ParameterList {
	private LivingSubjectAdminGender livingSubjectAdministrativeGender;
	private LivingSubjectBirthTime livingSubjectBirthTime;
	private LivingSubjectName livingSubjectName;
	private ArrayList<LivingSubjectId> livingSubjId;

	public ParameterList() {
	}

	public LivingSubjectAdminGender getLivingSubjectAdministrativeGender() {
		return livingSubjectAdministrativeGender;
	}

	public void setLivingSubjectAdministrativeGender(
			LivingSubjectAdminGender livingSubjectAdministrativeGender) {
		this.livingSubjectAdministrativeGender = livingSubjectAdministrativeGender;
	}

	public LivingSubjectBirthTime getLivingSubjectBirthTime() {
		return livingSubjectBirthTime;
	}

	public void setLivingSubjectBirthTime(
			LivingSubjectBirthTime livingSubjectBirthTime) {
		this.livingSubjectBirthTime = livingSubjectBirthTime;
	}

	public LivingSubjectName getLivingSubjectName() {
		return livingSubjectName;
	}

	public void setLivingSubjectName(LivingSubjectName livingSubjectName) {
		this.livingSubjectName = livingSubjectName;
	}

	public ArrayList<LivingSubjectId> getLivingSubjId() {
		return livingSubjId;
	}

	public void setLivingSubjId(ArrayList<LivingSubjectId> livingSubjId) {
		this.livingSubjId = livingSubjId;
	}
}