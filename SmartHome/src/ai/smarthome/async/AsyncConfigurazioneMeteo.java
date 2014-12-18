package ai.smarthome.async;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import ai.smarthome.database.DatabaseHelper;
import ai.smarthome.database.wrapper.Meteo;
import ai.smarthome.util.GPSTracker;
import ai.smarthome.util.LogView;
import ai.smarthome.util.rest.Rest;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

public class AsyncConfigurazioneMeteo extends AsyncTask<Void, String, Map<String, Integer>> {

	private final Context context;
	private GPSTracker gpsTracker;
	private String localita;
	private SQLiteDatabase db;
	
	
	public AsyncConfigurazioneMeteo(Context context) {
		this.context = context;
		gpsTracker = new GPSTracker(context);
		db = new DatabaseHelper(context).getWritableDatabase();
	}
	
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}


	@Override
	protected void onPostExecute(Map<String, Integer> parametri) {
		super.onPostExecute(parametri);
		
		final Calendar c = Calendar.getInstance();
    	int hour = c.get(Calendar.HOUR_OF_DAY);
    	int minute = c.get(Calendar.MINUTE);
    	long data = new Date().getTime();
    	
		if (parametri != null) {
			Meteo.updateConfigurazione(db, localita, parametri.get("meteo"), parametri.get("tempEst"), parametri.get("umidita"), parametri.get("vento"), data, hour, minute);
			LogView.info("AsyncConfigurazioneMeteo.onPostExecute: OK");
		}
		else {
			Meteo.updateConfigurazione(db, "-", 50, 50, 50, 50, data, hour, minute);
			LogView.info("AsyncConfigurazioneMeteo.onPostExecute: ERRORE");
		}
			
	}


	@Override
	protected Map<String, Integer> doInBackground(Void... params) {
		Map<String, Integer> parametri = null;
		
		localita = gpsTracker.getLocality(context);
		parametri = Rest.getMeteoLocale(context, localita);
		
		return parametri;
	}


	@Override
	protected void onProgressUpdate(String... values) {
		super.onProgressUpdate(values);
	}
	
	
	
}
