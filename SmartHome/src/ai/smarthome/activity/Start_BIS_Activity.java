package ai.smarthome.activity;

import com.facebook.AppEventsLogger;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;

import ai.smarthome.R;
import ai.smarthome.async.AsyncConfigurazioneMeteo;
import ai.smarthome.database.wrapper.Utente;
import ai.smarthome.util.Costanti;
import ai.smarthome.util.LogView;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;


public class Start_BIS_Activity extends Activity {

	private UiLifecycleHelper uiHelper;
    
    private Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            onSessionStateChange(session, state, exception);
        }
    };
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
 
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
	    StrictMode.setThreadPolicy(policy);
	  
	    AsyncConfigurazioneMeteo asyncConfMeteo = new AsyncConfigurazioneMeteo(this);
        asyncConfMeteo.execute();
        
	    Session session = Session.openActiveSessionFromCache(this);
        
	    uiHelper = new UiLifecycleHelper(this, callback);
        uiHelper.onCreate(savedInstanceState);
        
        if (session == null) {
        	new Handler().postDelayed(new Runnable() {
                @Override
                public void run() { 
                	LogView.info("StartActivity.onCreate.run: avvio normale");
                	Intent i = new Intent(Start_BIS_Activity.this, AccessoActivity.class);
                    startActivity(i);
                    finish();
                }
            }, Costanti.START_TIME_OUT);
        }
   }

		
	@Override
    public void onBackPressed() {
        finish();
    }
	
	
    @Override
    public void onResume() {
        super.onResume();
        uiHelper.onResume();
        AppEventsLogger.activateApp(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
        AppEventsLogger.deactivateApp(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onSaveInstanceState(Bundle savedState) {
        super.onSaveInstanceState(savedState);
        uiHelper.onSaveInstanceState(savedState);
    }
    
    @SuppressWarnings("deprecation")
	private void onSessionStateChange(Session session, SessionState state, Exception exception) {
        if (session.isOpened()) 
            if (state.equals(SessionState.OPENED)) {
            	Request.executeMeRequestAsync(session, new Request.GraphUserCallback() {
                    @Override
                    public void onCompleted(GraphUser user, Response response) {
                    	LogView.info("StartActivity.onSessionStateChange: avvio sessione FB");
                    	Utente utente;
                    	try {
                    		String fbId = user.getId();
                    		String firstName = user.getFirstName();
                    		String lastName = user.getLastName();
                    		
                    		utente = new Utente(fbId, null, null, null, lastName, firstName);
                    	} catch (Exception e) {
                    		utente = new Utente("1", null, null, null, "", "");
                    	}
                    	Intent i = new Intent(Start_BIS_Activity.this, MainActivity.class);
                        i.putExtra(Costanti.UTENTE, utente);
                        startActivity(i);
                        finish();
                    }
                });
            } 
        
    }
}
