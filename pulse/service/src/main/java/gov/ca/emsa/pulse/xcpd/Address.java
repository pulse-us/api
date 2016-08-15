package gov.ca.emsa.pulse.xcpd;

public class Address {
	private StreetAddressLine sal;
	private City city;
	private State state;

	public Address() {
	}

	public StreetAddressLine getSal() {
		return sal;
	}

	public void setSal(StreetAddressLine sal) {
		this.sal = sal;
	}

	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}
}