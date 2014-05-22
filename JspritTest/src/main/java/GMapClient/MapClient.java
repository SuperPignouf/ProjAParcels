package GMapClient;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import javax.xml.xpath.XPathConstants;

import org.glassfish.jersey.client.ClientConfig;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Classe tiree de la page des exercices du cours Web Services (INFO-H-511).
 * Permet d'obtenir aupres de Google Maps une adresse exacte ainsi que des coordonnees
 * a partir d'un String.
 */
public class MapClient {
	public static final String GOOGLE_GEOCODE_REQ = "http://maps.googleapis.com/maps/api/geocode/xml?sensor=false&address=%s";
	public static final String GOOGLE_GEOCODE_STATUS = "GeocodeResponse/status";
	public static final String GOOGLE_GEOCODE_RESULT = "GeocodeResponse/result";
	public static final String GOOGLE_GEOCODE_RESULT_NAME = "address_component[type='route']/long_name";
	public static final String GOOGLE_GEOCODE_RESULT_LATITUDE = "geometry/location/lat/text()";
	public static final String GOOGLE_GEOCODE_RESULT_LONGITUDE = "geometry/location/lng/text()";
	public static final String GOOGLE_GEOCODE_RESULT_COUNTRY = "address_component[type='country']/long_name";
	public static final String GOOGLE_GEOCODE_RESULT_STATE = "address_component[type='administrative_area_level_1']/long_name";
	public static final String GOOGLE_GEOCODE_RESULT_CITY = "address_component[type='locality']/long_name";
	
	public static final String GOOGLE_IMAGERY_REQ = "http://maps.googleapis.com/maps/api/staticmap?center=%f,%f&zoom=%d&size=%s&sensor=false&format=%s&maptype=%s&language=fr";
	public static final String GOOGLE_IMAGESET_AERIAL = "satellite";
	public static final String GOOGLE_IMAGESET_ROAD = "roadmap";
	public static final String GOOGLE_IMAGESET_SIZE = "400x400";
	public static final String GOOGLE_FORMAT_PNG = "png";
	public static final Integer GOOGLE_ZOOM_LVL_MIN = 1;
	public static final Integer GOOGLE_ZOOM_LVL_MAX = 21;
	public static final Integer GOOGLE_ZOOM_LVL_DEFAULT = 14;
	
	private List<List<Location>> result;

    /**
     * Parse responses from the Google Maps API, and return them
     * as a list of Location.
     */
	public List<Location> parseGoogleServiceResponse(InputStream response) {
        //Build a DOM representation of the document
		Document doc = XMLHelper.xml(response);
		ArrayList<Location> result = new ArrayList<Location>();

		// Check the error code
        String status = XMLHelper.xpath(doc, GOOGLE_GEOCODE_STATUS, XPathConstants.STRING);

        if (!status.equals("OK")) {
        	throw new RuntimeException("Google could not serve this request. ("+status+")");	
        }
		
        // Check each node in the result
        NodeList list = XMLHelper.xpath(doc, GOOGLE_GEOCODE_RESULT, XPathConstants.NODESET);
        for (int i = 0; i < list.getLength(); ++i) {                                                            
            result.add(parseLocation(list.item(i)));                                                            
        } 
        
		return result;
	}

    private Location parseLocation(Node item) {
    	String country = XMLHelper.xpath(item, GOOGLE_GEOCODE_RESULT_COUNTRY, XPathConstants.STRING);
    	String state = XMLHelper.xpath(item, GOOGLE_GEOCODE_RESULT_STATE, XPathConstants.STRING);
    	String city = XMLHelper.xpath(item, GOOGLE_GEOCODE_RESULT_CITY, XPathConstants.STRING);
    	String name = XMLHelper.xpath(item, GOOGLE_GEOCODE_RESULT_NAME, XPathConstants.STRING);    	
    	Double lat = XMLHelper.xpath(item, GOOGLE_GEOCODE_RESULT_LATITUDE, XPathConstants.NUMBER);
    	Double lng = XMLHelper.xpath(item, GOOGLE_GEOCODE_RESULT_LONGITUDE, XPathConstants.NUMBER);
    	    	    	
    	return new Location( lat, lng, country, state, city, name );
   	}

	/**
     * Find the actual locations corresponding to a given textual location
     */
	public List<Location> getLocations(String location) throws IOException {
		// Build the url with the proper location
		String url = String.format(GOOGLE_GEOCODE_REQ, URLEncoder.encode(location, "UTF-8"));

		Client client = ClientBuilder.newClient(new ClientConfig());
		WebTarget target = client.target(url);
				
        // Send the query to Google Geocode, to find the location
		Response response = target.request().get();
		InputStream data = response.readEntity(InputStream.class);

		List<Location> locations = parseGoogleServiceResponse(data);
		
		// Close the input stream
		data.close();
		
        // Parse the data
		return locations;
	}
	

	/**
	 * Constructeur: convertit les Strings recus en objets Location grace a GoogleMaps.
	 * @param addresses Une liste d'elements d'adresse stockes sous forme de String.
	 * @throws IOException
	 * @see {@link dbHandler.ProblemBuilder#ProblemBuilder()}
	 */
	public MapClient(List<String> addresses) throws IOException {
		this.result = new ArrayList<List<Location>>();
		List<String> locations = new ArrayList<String>();
		/*
		 * Les 6 premiers elements de la liste decrivent la premiere adresse, les 6 suivants la deuxieme, etc...
		 */
		for (int i = 0; i < addresses.size(); i = i + 6){
			locations.add(addresses.get(i + 1) + " " + addresses.get(i) + " " + addresses.get(i + 2) + " " + addresses.get(i + 3) + " " + addresses.get(i + 4) + " " + addresses.get(i + 5));
		}
		for (String loc:locations){
			result.add(this.getLocations(loc.replaceAll("null", ""))); // Appel a GoogleMaps.
		}
	}
	
	/**
	 * Extrait la liste des coordonnees associees aux lieux recherches.
	 * @return Liste de coordonnees.
	 * @see {@link dbHandler.ProblemBuilder#ProblemBuilder()}
	 */
	public List<Double> getCoordinates(){
		List<Double> returnValue = new ArrayList<Double>();
		for(List<Location> listLoc:this.result){
			returnValue.add(listLoc.get(0).latitude);
			returnValue.add(listLoc.get(0).longitude);
		}
		return returnValue;	
	}
}
