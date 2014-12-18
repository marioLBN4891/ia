package ai.smarthome.async;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import ai.smarthome.R;
import ai.smarthome.activity.MainActivity;
import ai.smarthome.database.DatabaseHelper;
import ai.smarthome.database.wrapper.Meteo;
import ai.smarthome.util.GPSTracker;
import ai.smarthome.util.UtilMeteo;
import ai.smarthome.util.rest.Rest;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.widget.Toast;


public class AsyncGetMeteo extends AsyncTask<Void, String, Map<String, Integer>> {

	private final MainActivity mainActivity;
	private GPSTracker gpsTracker;
	private String localita;
	private SQLiteDatabase db;
	 
	public AsyncGetMeteo(MainActivity mainActivity) {
		this.mainActivity = mainActivity;
		gpsTracker = new GPSTracker(mainActivity);
		db = new DatabaseHelper(mainActivity).getWritableDatabase();
	}
	
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		mainActivity.findViewById(R.id.meteoAttualeButton).setClickable(false);
		mainActivity.findViewById(R.id.meteoSoleButton).setClickable(false);
		mainActivity.findViewById(R.id.meteoSerenoButton).setClickable(false);
		mainActivity.findViewById(R.id.meteoPioggiaButton).setClickable(false);
		mainActivity.findViewById(R.id.meteoNuvoleButton).setClickable(false);
		mainActivity.findViewById(R.id.starSimulazioneButton).setClickable(false);
	}


	@Override
	protected void onPostExecute(Map<String, Integer> parametri) {
		super.onPostExecute(parametri);
		
		final Calendar c = Calendar.getInstance();
    	int hour = c.get(Calendar.HOUR_OF_DAY);
    	int minute = c.get(Calendar.MINUTE);
    	
    	UtilMeteo.updateData(db, new Date().getTime());
    	UtilMeteo.updateOrario(db, hour, minute);
		UtilMeteo.updateMeteo(db, localita, parametri.get("meteo"), parametri.get("tempEst"), parametri.get("umidita"), parametri.get("vento"));
		
		mainActivity.findViewById(R.id.meteoAttualeButton).setClickable(true);
		mainActivity.findViewById(R.id.meteoSoleButton).setClickable(true);
		mainActivity.findViewById(R.id.meteoSerenoButton).setClickable(true);
		mainActivity.findViewById(R.id.meteoPioggiaButton).setClickable(true);
		mainActivity.findViewById(R.id.meteoNuvoleButton).setClickable(true);
		mainActivity.findViewById(R.id.starSimulazioneButton).setClickable(true);
		
		mainActivity.setClimaMeteoAvvioVeloce(Meteo.getMeteo(db));
	}


	@Override
	protected Map<String, Integer> doInBackground(Void... params) {
		Map<String, Integer> parametri = null;
		
		localita = gpsTracker.getLocality(mainActivity);
		parametri = Rest.getMeteoLocale(mainActivity, localita);
		
		publishProgress("Meteo: "+ localita);
		
		return parametri;
		
	}


	@Override
	protected void onProgressUpdate(String... values) {
		super.onProgressUpdate(values);
		Toast.makeText(mainActivity.getApplicationContext(), values[0], Toast.LENGTH_SHORT).show();
	}
	
	
	
}
