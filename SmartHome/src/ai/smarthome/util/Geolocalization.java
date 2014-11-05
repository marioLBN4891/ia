package ai.smarthome.util;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import ai.smarthome.util.exception.GeolocalizationException;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class Geolocalization {
	
	private static List<Address> getPosizioneCorrente(Context context) throws IOException {
		
		boolean gps_enabled=false, network_enabled=false;
	    Location location = null;;
	    Double MyLat = null, MyLong = null;
	    
    	LocationManager locManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        
    	LocationListener locListener = new LocationListener() {
             public void onLocationChanged(Location location) {
            	 if (location != null) {
                }
             }

             public void onProviderDisabled(String provider) {
             }

             public void onProviderEnabled(String provider) {
             }

             public void onStatusChanged(String provider, int status, Bundle extras) {
             }
        };
       
       	gps_enabled=locManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
       	network_enabled=locManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        
       if(gps_enabled) {
        	locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locListener);
       		location=locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
       }
       if(network_enabled && location==null) {
            locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locListener);
            location=locManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER); 
       }
       if (location != null) {
            MyLat = location.getLatitude();
            MyLong = location.getLongitude();
        } 
        else {
            Location loc= getLastKnownLocation(context);
            if (loc != null) {
                MyLat = loc.getLatitude();
                MyLong = loc.getLongitude();
            }
        }
        
        locManager.removeUpdates(locListener); // removes the periodic updates from location listener to //avoid battery drainage. If you want to get location at the periodic intervals call this method using //pending intent.
       
        // Getting address from found locations.
        Geocoder geocoder;
        geocoder = new Geocoder(context, Locale.getDefault());
        return geocoder.getFromLocation(MyLat, MyLong, 1);
       
    }
   
	
	public static String getPosizioneCorrenteToString(Context context) throws IOException, GeolocalizationException {
		List<Address> addresses = getPosizioneCorrente(context);
		if (addresses != null) {
			String State= addresses.get(0).getAdminArea();
			String City = addresses.get(0).getLocality();
			String Country = addresses.get(0).getCountryName();
			return ""+ City + " ("+ State +") - " + Country;
		}
		throw new GeolocalizationException("GeolocalizazionException: posizione non disponibile");
	}
	
	
	public static String getCittaCorrenteToString(Context context) throws IOException, GeolocalizationException {
		List<Address> addresses = getPosizioneCorrente(context);
		if (addresses != null) 
			return addresses.get(0).getLocality();
		throw new GeolocalizationException("GeolocalizazionException: posizione non disponibile");
	}
	
	
   
// below method to get the last remembered location. because we don't get locations all the times .At some instances we are unable to get the location from GPS. so at that moment it will show us the last stored location. 

    public static Location getLastKnownLocation(Context context)
    {
        Location location = null;
        LocationManager locationmanager = (LocationManager)context.getSystemService("location");
        List<String> list = locationmanager.getAllProviders();
        boolean i = false;
        Iterator<String> iterator = list.iterator();
        do {
        	//System.out.println("---------------------------------------------------------------------");
            if(!iterator.hasNext())
                break;
            String s = (String)iterator.next();
            //if(i != 0 && !locationmanager.isProviderEnabled(s))
            if(i != false && !locationmanager.isProviderEnabled(s))
                continue;
           // System.out.println("provider ===> "+s);
            Location location1 = locationmanager.getLastKnownLocation(s);
            if(location1 == null)
                continue;
            if(location != null)
            {
                //System.out.println("location ===> "+location);
                //System.out.println("location1 ===> "+location);
                float f = location.getAccuracy();
                float f1 = location1.getAccuracy();
                if(f >= f1)
                {
                    long l = location1.getTime();
                    long l1 = location.getTime();
                    if(l - l1 <= 600000L)
                        continue;
                }
            }
            location = location1;
           // System.out.println("location  out ===> "+location);
            //System.out.println("location1 out===> "+location);
            i = locationmanager.isProviderEnabled(s);
           // System.out.println("---------------------------------------------------------------------");
        } while(true);
        return location;
    }
    
}
