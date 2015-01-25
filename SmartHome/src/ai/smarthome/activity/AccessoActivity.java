package ai.smarthome.activity;

import java.util.Arrays;
import java.util.List;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;

import ai.smarthome.R;
import ai.smarthome.activity.fragmentAccesso.LoginFragment;
import ai.smarthome.activity.fragmentAccesso.RegistrazioneFragment;
import ai.smarthome.database.DatabaseHelper;
import ai.smarthome.database.wrapper.Componente;
import ai.smarthome.database.wrapper.Oggetto;
import ai.smarthome.database.wrapper.Utente;
import ai.smarthome.util.Costanti;
import ai.smarthome.util.Utilities;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class AccessoActivity extends FragmentActivity implements TabListener {

	private EditText  mail;
	private EditText  password;
	private EditText  password2;
	private EditText  cognome;
	private EditText  nome;
	private SQLiteDatabase db;
	
	private UiLifecycleHelper uiHelper;
    private static final List<String> PERMISSIONS = Arrays.asList("publish_actions");
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		uiHelper = new UiLifecycleHelper(this, statusCallback);
        uiHelper.onCreate(savedInstanceState);
        
		setContentView(R.layout.activity_accesso);
		
		db = new DatabaseHelper(this).getWritableDatabase();
		
		Componente.reset(db);
		Oggetto.reset(db);
		
		ActionBar ab = getActionBar();
        ab.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        ab.setHomeButtonEnabled(true);
        ab.setDisplayHomeAsUpEnabled(true);
        
        Tab tabLogin = ab.newTab();
        tabLogin.setText("Login");
        tabLogin.setTabListener(this);
        ab.addTab(tabLogin);
        
        Tab tabReg= ab.newTab();
        tabReg.setText("Registrazione");
        tabReg.setTabListener(this);
        ab.addTab(tabReg);
   	
	}
	
	private Session.StatusCallback statusCallback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            if (state.isOpened()) {
            	Log.i("SmartHomeEnvironment", "Facebook session opened");
            } 
            if (state.isClosed()) {
                Log.i("SmartHomeEnvironment", "Facebook session closed");
            }
        }
    };
    
    public boolean checkPermissions() {
        Session s = Session.getActiveSession();
        if (s != null) 
            return s.getPermissions().contains("publish_actions");
        else
            return false;
    }

    public void requestPermissions() {
        Session s = Session.getActiveSession();
        if (s != null)
            s.requestNewPublishPermissions(new Session.NewPermissionsRequest(this, PERMISSIONS));
    }

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
    }

    @Override
    public void onSaveInstanceState(Bundle savedState) {
        super.onSaveInstanceState(savedState);
        uiHelper.onSaveInstanceState(savedState);
    }
    
	public void fastLogin() {
		
		Utente user = Utente.getConnessioneVeloce(db);
        if (user != null) {
        	Intent i = new Intent(AccessoActivity.this, MainActivity.class);
        	i.putExtra(Costanti.UTENTE, user);
            startActivity(i);
            finish();
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
		Utilities.chiudiApplicazione(this);
	}

		
	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		 Fragment fragment = null;
	     int position = tab.getPosition();
		 
	     if (position == 0 ) fragment = new LoginFragment();
	     if (position == 1 ) fragment = new RegistrazioneFragment();
	     
	     ft.replace(android.R.id.content, fragment);
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		Fragment fragment = null;
	     int position = tab.getPosition();
		 
	     if (position == 0 ) fragment = new LoginFragment();
	     if (position == 1 ) fragment = new RegistrazioneFragment();
		ft.remove(fragment);
	}

	
	public void saveReg(View arg0) {
		mail = (EditText)findViewById(R.id.mailEditText);
	    password = (EditText)findViewById(R.id.passwordEditText);
	    password2 = (EditText)findViewById(R.id.password2EditText);
	    cognome = (EditText)findViewById(R.id.cognomeEditText);
	    nome = (EditText)findViewById(R.id.nomeEditText);
	    
	    if (mail.getText().toString().trim().equals("")) {
			Toast.makeText(getApplicationContext(), "Email è obbligatorio", Toast.LENGTH_SHORT).show();
			return;
		}
		if (cognome.getText().toString().trim().equals("")) {
			Toast.makeText(getApplicationContext(), "Cognome è obbligatorio", Toast.LENGTH_SHORT).show();
			return;
		}
		if (nome.getText().toString().trim().equals("")) {
			Toast.makeText(getApplicationContext(), "Nome è obbligatorio", Toast.LENGTH_SHORT).show();
			return;
		}
		if (!Utilities.emailValida(mail.getText().toString().trim())) {
			Toast.makeText(getApplicationContext(), "Email non valida", Toast.LENGTH_SHORT).show();
			return;
		}
		if (Utente.isMailRegistered(db, mail.getText().toString().trim())) {
			Toast.makeText(getApplicationContext(), "Email già registrata", Toast.LENGTH_SHORT).show();
			return;
		}
		if (password.getText().toString().trim().equals("") || password.getText().toString().trim().length() < 5 || Utilities.checkSpace(password.getText().toString())) {
			Toast.makeText(getApplicationContext(), "Password è un campo obbligatorio di almeno 5 caratteri  non vuoti", Toast.LENGTH_SHORT).show();
			return;
		}
		if (password2.getText().toString().trim().equals("") || password2.getText().toString().trim().length() < 5 || Utilities.checkSpace(password2.getText().toString())) {
			Toast.makeText(getApplicationContext(), "Inserire nuovamente la password", Toast.LENGTH_SHORT).show();
			return;
		}
		if (!password2.getText().toString().equals(password.getText().toString())) {
			Toast.makeText(getApplicationContext(), "Password digitata non corretta", Toast.LENGTH_SHORT).show();
			return;
		}
		
		Utente.setUtente(db, mail.getText().toString().trim(), password.getText().toString().trim(), cognome.getText().toString().trim(), nome.getText().toString().trim());
		Toast.makeText(getApplicationContext(), "Registrazione avvenuta con successo", Toast.LENGTH_SHORT).show();
	}
		

	public void login(View arg0) { 
		mail = (EditText)findViewById(R.id.mailEditText);
 	    password = (EditText)findViewById(R.id.passwordEditText);
 	    
		if (!mail.getText().toString().equals("")) {
			if (!password.getText().toString().equals("")) {
				Utente user = Utente.isUtenteRegistered(db, mail.getText().toString().trim(), password.getText().toString().trim());
		        if (user != null) {
		        	Utente.setConnessioneVeloce(db, user.getMail(), user.getPassword());
			        Intent i = new Intent(AccessoActivity.this, MainActivity.class);
			        i.putExtra(Costanti.UTENTE, user);
			        startActivity(i);
			        finish();
				}	
				else 
					Toast.makeText(getApplicationContext(), "Mail/password non valide.Riprova", Toast.LENGTH_SHORT).show();
			}
			else
				Toast.makeText(getApplicationContext(), "Inserire password", Toast.LENGTH_SHORT).show();
		}
		else
			Toast.makeText(getApplicationContext(), "Inserire email", Toast.LENGTH_SHORT).show();
	}
	
	public void passwordDimenticata(View arg0) {
 		Intent i = new Intent(AccessoActivity.this, PasswordActivity.class);
 	    startActivity(i);
 	}
 	
	
   
}
