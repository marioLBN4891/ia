package ai.smarthome.fragment;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ai.smarthome.R;
import ai.smarthome.database.wrapper.Configurazione;
import android.app.Fragment;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;



public class InfoPersonaliFragment extends Fragment {
	
	public static final String CONFIGURAZIONE = "configurazione";
	private String providerId = LocationManager.GPS_PROVIDER;
    private Geocoder geo = null;
    private LocationManager locationManager=null;
    private static final int MIN_DIST=20;
    private static final int MIN_PERIOD=30000;
    View rootView;
    public InfoPersonaliFragment() {
        // Empty constructor required for fragment subclasses
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	
    	Configurazione conf = (Configurazione) getArguments().getSerializable(CONFIGURAZIONE);
    	rootView = inflater.inflate(R.layout.fragment_infopersonali, container, false);
        String intestazione = getResources().getStringArray(R.array.opzioni_array)[conf.getPosizione()];
        getActivity().setTitle(intestazione);
        
        return rootView;
    }
 
    private LocationListener locationListener = new LocationListener() {
    	@Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    	
        @Override
        public void onProviderEnabled(String provider) {
            // attivo GPS su dispositivo
            updateText(R.id.enabled, "TRUE");
        }
        
        @Override
        public void onProviderDisabled(String provider) {
            // disattivo GPS su dispositivo
            updateText(R.id.enabled, "FALSE");
        }
        
        @Override
        public void onLocationChanged(Location location) {
            updateGUI(location);
         }
     
    };
    
    @Override
	public void onResume() {
    	
        super.onResume();
        Context context = getActivity();
        geo=new Geocoder(context, Locale.getDefault());
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location!=null)
            updateGUI(location);
        if (locationManager!=null && locationManager.isProviderEnabled(providerId))
            updateText(R.id.enabled, "TRUE");
        else
            updateText(R.id.enabled, "FALSE");
        locationManager.requestLocationUpdates(providerId, MIN_PERIOD,MIN_DIST, locationListener);
    }
     
    @Override
	public void onPause() {
    	
        super.onPause();
        if (locationManager!=null && locationManager.isProviderEnabled(providerId))
            locationManager.removeUpdates(locationListener);
    }
    
    private void updateGUI(Location location) {
    	
        Date timestamp = new Date(location.getTime());
        updateText(R.id.timestamp, timestamp.toString());
        double latitude = location.getLatitude();
        updateText(R.id.latitude, String.valueOf(latitude));
        double longitude = location.getLongitude();
        updateText(R.id.longitude, String.valueOf(longitude));
        new AddressSolver().execute(location);
    }
 
    private void updateText(int id, String text) {
    	
    	TextView textView = (TextView) rootView.findViewById(id);
        textView.setText(text);
    }
    
    
    private class AddressSolver extends AsyncTask<Location, Void, String> {
 
        @Override
        protected String doInBackground(Location... params) {
        	
            Location pos=params[0];
            double latitude = pos.getLatitude();
            double longitude = pos.getLongitude();
             
            List<Address> addresses = null;
            try {
                addresses = geo.getFromLocation(latitude, longitude, 1);
            } 
            catch (IOException e) {   
            }
            
            if (addresses!=null) {
                if (addresses.isEmpty())  
                	return null;
                else 
                     if (addresses.size() > 0) {   
                         StringBuffer address=new StringBuffer();
                         Address tmp=addresses.get(0);
                         for (int y=0;y<tmp.getMaxAddressLineIndex();y++)
                            address.append(tmp.getAddressLine(y)+"\n");
                         return address.toString();
                     }
            }
            return null;
        }
         
        @Override
        protected void onPostExecute(String result) {
            if (result!=null)
                updateText(R.id.where, result);
            else
                  updateText(R.id.where, "N.A.");
        }
    
    }
    
}
