package ai.smarthome.async;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import ai.smarthome.database.DatabaseHelper;
import ai.smarthome.database.wrapper.Configurazione;
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
			Configurazione.updateConfigurazione(db, localita, parametri.get("meteo"), parametri.get("tempInt"), parametri.get("tempEst"), parametri.get("umiditaInt"), parametri.get("umiditaEst"), parametri.get("vento"), parametri.get("luminosita"), data, hour, minute, 0);
			LogView.info("AsyncConfigurazioneMeteo.onPostExecute: OK");
		}
		else {
			Configurazione.updateConfigurazione(db, "-", 50, 20, 30, 50, 50, 0, 50, data, hour, minute, 0);
			LogView.info("AsyncConfigurazioneMeteo.onPostExecute: ERRORE");
		}
			
	}


	@Override
	protected Map<String, Integer> doInBackground(Void... params) {
		Map<String, Integer> parametri = null;
		try {
			localita = gpsTracker.getLocality(context);
			parametri = Rest.getMeteoLocale(context, localita);
		}
		catch(Exception e) {
		}
			return parametri;
	}


	@Override
	protected void onProgressUpdate(String... values) {
		super.onProgressUpdate(values);
	}
	
	
	
}
