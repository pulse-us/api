package gov.ca.emsa.pulse.broker.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.util.StringUtils;

import gov.ca.emsa.pulse.broker.entity.AlternateCareFacilityAddressLineEntity;
import gov.ca.emsa.pulse.broker.entity.AlternateCareFacilityEntity;

public class AlternateCareFacilityDTO {

	private Long id;
	private String name;
	private String phoneNumber;
	private List<AddressLineDTO> lines;
	private String city;
	private String state;
	private String zipcode;
	private String country;
	private Date lastReadDate;
	private Date creationDate;
	private Date lastModifiedDate;
	
	public AlternateCareFacilityDTO(){
		lines = new ArrayList<AddressLineDTO>();
	}
	
	public AlternateCareFacilityDTO(AlternateCareFacilityEntity entity) {
		this();
		if(entity == null) {
			return;
		}
		
		this.id = entity.getId();
		this.name = entity.getName();
		this.phoneNumber = entity.getPhoneNumber();
		this.lastReadDate = entity.getLastReadDate();
		this.creationDate = entity.getCreationDate();
		this.lastModifiedDate = entity.getLastModifiedDate();
		
		if(entity.getLines() != null && entity.getLines().size() > 0) {
			for(AlternateCareFacilityAddressLineEntity lineEntity: entity.getLines()) {
				AddressLineDTO lineDto = new AddressLineDTO();
				lineDto.setCreationDate(lineEntity.getCreationDate());
				lineDto.setLastModifiedDate(lineEntity.getLastModifiedDate());
				lineDto.setId(lineEntity.getId());
				lineDto.setLine(lineEntity.getLine());
				this.lines.add(lineDto);
			}
		}
		this.city = entity.getCity();
		this.state = entity.getState();
		this.zipcode = entity.getZipcode();
		this.country = entity.getCountry();
	}
	
	public boolean hasAddressParts() {
		return (this.lines != null && this.lines.size() > 0) ||
				!StringUtils.isEmpty(this.city) ||
				!StringUtils.isEmpty(this.state) ||
				!StringUtils.isEmpty(this.country) ||
				!StringUtils.isEmpty(this.zipcode);
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getLastReadDate() {
		return lastReadDate;
	}

	public void setLastReadDate(Date lastReadDate) {
		this.lastReadDate = lastReadDate;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public List<AddressLineDTO> getLines() {
		return lines;
	}

	public void setLines(List<AddressLineDTO> lines) {
		this.lines = lines;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}
}
