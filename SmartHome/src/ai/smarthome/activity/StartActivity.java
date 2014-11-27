package ai.smarthome.activity;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;

import ai.smarthome.R;
import ai.smarthome.database.wrapper.Utente;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class StartActivity extends Activity {

	private static int START_TIME_OUT = 3000;
 
	private UiLifecycleHelper uiHelper;
	public static final String UTENTE = "utente";
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
 
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
	    StrictMode.setThreadPolicy(policy);
	  
	    uiHelper = new UiLifecycleHelper(this, statusCallback);
        uiHelper.onCreate(savedInstanceState);
        
        Session session = Session.openActiveSessionFromCache(this);
        
	    if (session == null || session.isClosed()) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() { 
            	Log.i("SmartHomeEnvironment", "Avvio thread...");
            	Intent i = new Intent(StartActivity.this, AccessoActivity.class);
                startActivity(i);
                finish();
            }
        }, START_TIME_OUT);
    }
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}
	
	@Override
    public void onBackPressed() {
        finish();
    }
	
	private Session.StatusCallback statusCallback = new Session.StatusCallback() {
        @SuppressWarnings("deprecation")
		@Override
        public void call(Session session, SessionState state, Exception exception) {
            if (state.isOpened()) {
            	if (session.isOpened()) {
            		final String fbAccessToken = session.getAccessToken();
                    Request.executeMeRequestAsync(session, new Request.GraphUserCallback() {
                        @Override
                        public void onCompleted(GraphUser user, Response response) {
                            Log.i("SmartHomeEnvironment", "Avvio sessione Facebook...");
                        	
                        	String fbId = user.getId();
                            String firstName = user.getFirstName();
                            String lastName = user.getLastName();
                            String token = fbAccessToken;
                          
                            Utente utente= new Utente(fbId, null, token, lastName, firstName);
                            Intent i = new Intent(StartActivity.this, MainActivity.class);
                            i.putExtra(UTENTE, utente);
                            startActivity(i);
                            finish();
                        }
                    });
                }
            	if (session.isClosed())
            		Log.i("SmartHomeEnvironment", "state O - session C");
            }
            if (state.isClosed())
            	Log.i("SmartHomeEnvironment", "state C");
       }
    };
    
    @Override
    public void onResume() {
        super.onResume();
        uiHelper.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
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
        Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
    }

    @Override
    public void onSaveInstanceState(Bundle savedState) {
        super.onSaveInstanceState(savedState);
        uiHelper.onSaveInstanceState(savedState);
    }
}
