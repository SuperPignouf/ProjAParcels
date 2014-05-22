package GMapClient;

/**
 * Structure de donnees tiree de la page des exercices du cours Web Services (INFO-H-511)
 */
class Location {
	double latitude;
	double longitude;
	String country,state,city,name;

	public double getLatitude() {
		return latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public String getCountry() {
		return country;
	}
	public String getState() {
		return state;
	}
	public String getCity() {
		return city;
	}
	public String getName() {
		return name;
	}
	public Location(double latitude, double longitude, String country,
			String state, String city, String name) {
		super();
		this.latitude = latitude;
		this.longitude = longitude;
		this.country = country;
		this.state = state;
		this.city = city;
		this.name = name;
	}

	@Override
	public String toString() {
		return name + ", " + city + ", " + state + ", " + country + 
				" (@" + latitude +"," + longitude + ")";
	}
}