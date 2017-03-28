package gov.ca.emsa.pulse.common.domain;

import java.util.ArrayList;
import java.util.List;

public class PatientSearch {
	private ArrayList<PatientSearchName> names;
	private String dob;
	private String ssn;
	private String gender;
	private String zip;
	private String telephone;
	private List<PatientSearchAddress> addresses;
	
	public PatientSearch(){
		names = new ArrayList<PatientSearchName>();
	}
	
	public String getDob() {
		return dob;
	}
	public void setDob(String dob) {
		this.dob = dob;
	}
	public String getSsn() {
		return ssn;
	}
	public void setSsn(String ssn) {
		this.ssn = ssn;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getZip() {
		return zip;
	}
	public void setZip(String zip) {
		this.zip = zip;
	}

	public ArrayList<PatientSearchName> getPatientNames() {
		return names;
	}

	public void setPatientNames(ArrayList<PatientSearchName> names) {
		this.names = names;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public List<PatientSearchAddress> getAddresses() {
		return addresses;
	}

	public void setAddresses(List<PatientSearchAddress> addresses) {
		this.addresses = addresses;
	}
	
}
