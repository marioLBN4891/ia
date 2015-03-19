package ai.smarthome.async;

import ai.smarthome.activity.MainActivity;
import ai.smarthome.database.wrapper.Impostazione;
import ai.smarthome.util.GPSTracker;
import ai.smarthome.util.rest.Rest;
import android.os.AsyncTask;


public class AsyncPosition extends AsyncTask<Void, Void, Void> {

	private GPSTracker gpsTracker;
	private String serverAddress;
	
	public AsyncPosition(MainActivity mainActivity) {
		gpsTracker = new GPSTracker(mainActivity);
		serverAddress = Impostazione.getIndirizzo(mainActivity.db);
	}
	
	

	@Override
	protected Void doInBackground(Void... params) {
		try {
			while (true) {
				gpsTracker.getLocation();
				gpsTracker.updateGPSCoordinates();
				String latitude = String.valueOf(gpsTracker.getLatitude());
				String longitude = String.valueOf(gpsTracker.getLongitude());
		
				Rest.sendPosition(serverAddress, "", latitude, longitude);
				Thread.sleep(3000);
			}
		
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	return null;
	
	}
		
	
}
