package geolocation;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;

/**
 * 
 * Classe permettant d'obtenir les coordonnees actuelles du smartphone
 * @see activities.ScanActivity#onClick(android.view.View)
 *
 */
public class CurrentLocation {
	
	private Location location;
	
	/**
	 * Constructeur
	 * @see activities.ScanActivity#onClick(android.view.View)
	 * @param mContext
	 */
	public CurrentLocation(Context mContext){
		LocationManager locationManager = (LocationManager)mContext.getSystemService(Context.LOCATION_SERVICE);
	    Criteria criteria = new Criteria();
	    String provider = locationManager.getBestProvider(criteria, false);
	    this.location = locationManager.getLastKnownLocation(provider);
	}

	/**
	 * getter sur la latitude
	 * @see activities.ScanActivity#onClick(android.view.View)
	 * @return la latitude
	 */
	public String getLatitude() {
		return Double.toString(location.getLatitude());
	}

	/**
	 * getter sur la longitude
	 * @see activities.ScanActivity#onClick(android.view.View)
	 * @return la longitude
	 */
	public String getLongitude() {
		return Double.toString(location.getLongitude());
	}

}
