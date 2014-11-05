package ai.smarthome.async;

import java.io.IOException;
import java.util.Map;

import ai.smarthome.MainActivity;
import ai.smarthome.R;
import ai.smarthome.util.Geolocalization;
import ai.smarthome.util.exception.GeolocalizationException;
import ai.smarthome.util.rest.Rest;
import android.os.AsyncTask;
import android.os.Looper;
import android.os.MessageQueue;
import android.os.MessageQueue.IdleHandler;
import android.widget.Toast;


public class AsyncMeteo extends AsyncTask<Void, Void, Map<String, Integer>> {

	private final MainActivity mainActivity;
	private String posizione;
	 
	public AsyncMeteo(MainActivity mainActivity) {
		this.mainActivity = mainActivity;
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
	protected void onPostExecute(Map<String, Integer> result) {
		super.onPostExecute(result);
		mainActivity.findViewById(R.id.meteoAttualeButton).setClickable(true);
		mainActivity.findViewById(R.id.meteoSoleButton).setClickable(true);
		mainActivity.findViewById(R.id.meteoSerenoButton).setClickable(true);
		mainActivity.findViewById(R.id.meteoPioggiaButton).setClickable(true);
		mainActivity.findViewById(R.id.meteoNuvoleButton).setClickable(true);
		mainActivity.findViewById(R.id.starSimulazioneButton).setClickable(true);
		
		mainActivity.setClimaMeteoAvvioVeloce(result);
		
	}


	@Override
	protected Map<String, Integer> doInBackground(Void... params) {
	    Looper.prepare();	// Prepare looper
        MessageQueue queue = Looper.myQueue();	                 // Register Queue listener hook
        queue.addIdleHandler(new IdleHandler() {
        	int mReqCount = 0;

            @Override
            public boolean queueIdle() {
            	if (++mReqCount == 2) {
            		Looper.myLooper().quit();     // Quit looper
                    return false;
                } else
                	return true;
                }
        });
         
        String city;
        Map<String, Integer> parametri = null;
		try {
			city = Geolocalization.getCittaCorrenteToString(mainActivity.getApplicationContext());
			posizione = Geolocalization.getPosizioneCorrenteToString(mainActivity.getApplicationContext());
			parametri = Rest.getMeteoLocale(mainActivity.getApplicationContext(), city);
			Toast.makeText(mainActivity.getApplicationContext(), "Meteo: "+ posizione, Toast.LENGTH_SHORT).show();
		} catch (IOException e) {
			e.printStackTrace();
			Toast.makeText(mainActivity.getApplicationContext(), "Servizio Meteo non disponibile", Toast.LENGTH_SHORT).show();
		} catch (GeolocalizationException e) {
			e.printStackTrace();
			Toast.makeText(mainActivity.getApplicationContext(), "Servizio Meteo non disponibile", Toast.LENGTH_SHORT).show();
		}
		Looper.loop();  // Start looping Message Queue- Blocking call
    	
        return parametri;
	}
	
	
	
}
