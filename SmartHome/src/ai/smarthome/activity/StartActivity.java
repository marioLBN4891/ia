package ai.smarthome.activity;

import ai.smarthome.R;
import ai.smarthome.async.AsyncConfigurazioneMeteo;
import ai.smarthome.util.Costanti;
import ai.smarthome.util.LogView;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;


public class StartActivity extends Activity {

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
 
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
	    StrictMode.setThreadPolicy(policy);
	  
	    AsyncConfigurazioneMeteo asyncConfMeteo = new AsyncConfigurazioneMeteo(this);
        asyncConfMeteo.execute();
        
	    new Handler().postDelayed(new Runnable() {
	    	@Override
            public void run() { 
	    		LogView.info("StartActivity.onCreate.run: avvio normale");
                Intent i = new Intent(StartActivity.this, AccessoActivity.class);
                startActivity(i);
                finish();
            }
        }, Costanti.START_TIME_OUT);
        
   }

		
	@Override
    public void onBackPressed() {
        finish();
    }
	
	
    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
    	super.onPause();
    }

    @Override
    public void onDestroy() {
    	super.onDestroy();
    }

    
}
