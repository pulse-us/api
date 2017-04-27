package gov.ca.emsa.pulse.broker.dto;

import gov.ca.emsa.pulse.broker.entity.GivenNameEntity;
import gov.ca.emsa.pulse.broker.entity.PulseUserEntity;

import java.util.Date;

import javax.persistence.Column;

public class PulseUserDTO {

	private Long id;

	private String assertion;

	public PulseUserDTO(){

	}

	public PulseUserDTO(PulseUserEntity entity){
		this.id = entity.getId();
		this.assertion = entity.getAssertion();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAssertion() {
		return assertion;
	}

	public void setAssertion(String assertion) {
		this.assertion = assertion;
	}

}
