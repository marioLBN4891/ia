package ai.smarthome.async;

import java.util.Map;

import ai.smarthome.R;
import ai.smarthome.activity.MainActivity;
import ai.smarthome.util.GPSTracker;
import ai.smarthome.util.rest.Rest;
import android.os.AsyncTask;
import android.widget.SeekBar;
import android.widget.Toast;


public class AsyncMeteo extends AsyncTask<Void, String, Map<String, Integer>> {

	private final MainActivity mainActivity;
	private GPSTracker gpsTracker;
	private String localita;
	private SeekBar seekMeteo, seekTempInt, seekTempEst, seekUmiditaInt, seekUmiditaEst, seekVento;
	
	public AsyncMeteo(MainActivity mainActivity) {
		this.mainActivity = mainActivity;
		gpsTracker = new GPSTracker(mainActivity);
	}
	
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		mainActivity.findViewById(R.id.cambiaMeteoButton).setClickable(false);
	}


	@Override
	protected void onPostExecute(Map<String, Integer> parametri) {
		super.onPostExecute(parametri);
		
		mainActivity.findViewById(R.id.cambiaMeteoButton).setClickable(true);
		
		seekMeteo = (SeekBar) mainActivity.findViewById(R.id.seekMeteo);
        seekTempInt = (SeekBar) mainActivity.findViewById(R.id.seekTemperaturaInterna);
        seekTempEst = (SeekBar) mainActivity.findViewById(R.id.seekTemperaturaEsterna);
        seekUmiditaInt = (SeekBar) mainActivity.findViewById(R.id.seekUmiditaInterna);
        seekUmiditaEst = (SeekBar) mainActivity.findViewById(R.id.seekUmiditaEsterna);
        seekVento = (SeekBar) mainActivity.findViewById(R.id.seekVento);
        
        seekMeteo.setProgress(parametri.get("meteo"));
        seekTempInt.setProgress(parametri.get("tempInt"));
        seekTempEst.setProgress(parametri.get("tempEst"));
        seekUmiditaEst.setProgress(parametri.get("umiditaInt"));
        seekUmiditaInt.setProgress(parametri.get("umiditaEst"));
        seekVento.setProgress(parametri.get("vento"));
	}


	@Override
	protected Map<String, Integer> doInBackground(Void... params) {
		Map<String, Integer> parametri = null;
		
		localita = gpsTracker.getLocality(mainActivity);
		parametri = Rest.getMeteoLocale(mainActivity, localita);
		
		publishProgress("Meteo caricato: "+ localita);
		
		return parametri;
		
	}


	@Override
	protected void onProgressUpdate(String... values) {
		super.onProgressUpdate(values);
		Toast.makeText(mainActivity.getApplicationContext(), values[0], Toast.LENGTH_SHORT).show();
	}
	
	
	
}
