package gov.ca.emsa.pulse.broker.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Organization {
	
	private String name;
	
	public Organization(){}
	
	public Organization(String name){
		this.name = name;
	}
	
	public String getName(){
		return name;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	@Override
    public String toString() {
        return "Organization{" +
                "name=" + name +
                '}';
    }

}
