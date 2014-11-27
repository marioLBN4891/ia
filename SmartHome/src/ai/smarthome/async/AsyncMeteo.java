package ai.smarthome.async;

import java.util.Map;

import ai.smarthome.R;
import ai.smarthome.activity.MainActivity;
import ai.smarthome.util.rest.Rest;
import android.os.AsyncTask;
import android.widget.Toast;


public class AsyncMeteo extends AsyncTask<Void, String, Map<String, Integer>> {

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
	protected void onPostExecute(Map<String, Integer> parametri) {
		super.onPostExecute(parametri);
		mainActivity.findViewById(R.id.meteoAttualeButton).setClickable(true);
		mainActivity.findViewById(R.id.meteoSoleButton).setClickable(true);
		mainActivity.findViewById(R.id.meteoSerenoButton).setClickable(true);
		mainActivity.findViewById(R.id.meteoPioggiaButton).setClickable(true);
		mainActivity.findViewById(R.id.meteoNuvoleButton).setClickable(true);
		mainActivity.findViewById(R.id.starSimulazioneButton).setClickable(true);
		
		mainActivity.setClimaMeteoAvvioVeloce(parametri);
	}


	@Override
	protected Map<String, Integer> doInBackground(Void... params) {
	 //   Looper.prepare();	// Prepare looper
     //   MessageQueue queue = Looper.myQueue();	                 // Register Queue listener hook
     //   queue.addIdleHandler(new IdleHandler() {
     //   	int mReqCount = 0;

     //       @Override
     //       public boolean queueIdle() {
     //       	if (++mReqCount == 2) {
     //       		Looper.myLooper().quit();     // Quit looper
     //               return false;
     //           } else
     //           	return true;
     //           }
     //   });
         
        String city;
        Map<String, Integer> parametri = null;
   //     try {
			city = "Bari"; //Geolocalization.getCittaCorrenteToString(mainActivity.getApplicationContext());
			posizione = "Bari"; // Geolocalization.getPosizioneCorrenteToString(mainActivity.getApplicationContext());
			parametri = Rest.getMeteoLocale(mainActivity.getApplicationContext(), city);
			publishProgress("Meteo: "+ posizione);
		          
	  	
        return parametri;
	}


	@Override
	protected void onProgressUpdate(String... values) {
		super.onProgressUpdate(values);
		Toast.makeText(mainActivity.getApplicationContext(), values[0], Toast.LENGTH_SHORT).show();
	}
	
	
	
}
