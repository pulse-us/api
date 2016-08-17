package gov.ca.emsa.pulse.xcpd;

import java.util.ArrayList;

public class PatientPerson {
	private Name name;
	private ArrayList<Telecom> telecoms;
	private AdminGenderCode agc;
	private BirthTime bt;
	private Address add;
	private ArrayList<AsOtherIds> asOtherIds;

	public PatientPerson() {
	}

	public Name getName() {
		return name;
	}

	public void setName(Name name) {
		this.name = name;
	}

	public ArrayList<Telecom> getTelecoms() {
		return telecoms;
	}

	public void setTelecoms(ArrayList<Telecom> telecoms) {
		this.telecoms = telecoms;
	}

	public AdminGenderCode getAgc() {
		return agc;
	}

	public void setAgc(AdminGenderCode agc) {
		this.agc = agc;
	}

	public BirthTime getBt() {
		return bt;
	}

	public void setBt(BirthTime bt) {
		this.bt = bt;
	}

	public Address getAdd() {
		return add;
	}

	public void setAdd(Address add) {
		this.add = add;
	}

	public ArrayList<AsOtherIds> getAsOtherIds() {
		return asOtherIds;
	}

	public void setAsOtherIds(ArrayList<AsOtherIds> asOtherIds) {
		this.asOtherIds = asOtherIds;
	}
}