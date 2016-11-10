package gov.ca.emsa.pulse.common.domain;

import java.util.ArrayList;
import java.util.Date;

public class PatientRecordName {
	
	private String id;
	private ArrayList<GivenName> givens;
	private ArrayList<String> givenStrings;
	private String family;
	private String suffix;
	private String prefix;
	private String profSuffix;
	private NameType nameType;
	private NameRepresentation nameRepresentation;
	private NameAssembly nameAssembly;
	private Date effectiveDate;
	private Date expirationDate;
	
	public PatientRecordName(){
		this.givens = new ArrayList<GivenName>();
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public ArrayList<GivenName> getGivenName() {
		return givens;
	}
	public void setGivenName(ArrayList<GivenName> givens) {
		this.givens = givens;
	}
	public String getFamilyName() {
		return family;
	}
	public void setFamilyName(String familyName) {
		this.family = familyName;
	}
	public String getSuffix() {
		return suffix;
	}
	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}
	public String getPrefix() {
		return prefix;
	}
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	
	public NameType getNameType() {
		return nameType;
	}

	public void setNameType(NameType nameType) {
		this.nameType = nameType;
	}

	public NameRepresentation getNameRepresentation() {
		return nameRepresentation;
	}

	public void setNameRepresentation(NameRepresentation nameRepresentation) {
		this.nameRepresentation = nameRepresentation;
	}

	public NameAssembly getNameAssembly() {
		return nameAssembly;
	}

	public void setNameAssembly(NameAssembly nameAssembly) {
		this.nameAssembly = nameAssembly;
	}

	public Date getEffectiveDate() {
		return effectiveDate;
	}
	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}
	public Date getExpirationDate() {
		return expirationDate;
	}
	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	public String getProfSuffix() {
		return profSuffix;
	}

	public void setProfSuffix(String profSuffix) {
		this.profSuffix = profSuffix;
	}

	public ArrayList<String> getGivenStrings() {
		return givenStrings;
	}

	public void setGivenStrings(ArrayList<String> givenStrings) {
		this.givenStrings = givenStrings;
	}
	
	

}
