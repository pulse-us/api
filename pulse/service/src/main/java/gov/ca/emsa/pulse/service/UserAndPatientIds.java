package gov.ca.emsa.pulse.service;

import java.util.ArrayList;

public class UserAndPatientIds {
	
	private User user;
	private ArrayList<Long> patientIds;
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public ArrayList<Long> getPatientIds() {
		return patientIds;
	}
	public void setPatientIds(ArrayList<Long> patientIds) {
		this.patientIds = patientIds;
	}
	
	

}
