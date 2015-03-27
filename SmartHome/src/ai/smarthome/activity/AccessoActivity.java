package ai.smarthome.activity;

import ai.smarthome.R;
import ai.smarthome.activity.fragmentAccesso.LoginFragment;
import ai.smarthome.activity.fragmentAccesso.RegistrazioneFragment;
import ai.smarthome.database.DatabaseHelper;
import ai.smarthome.database.wrapper.Impostazione;
import ai.smarthome.database.wrapper.Utente;
import ai.smarthome.util.Costanti;
import ai.smarthome.util.Utilities;
import ai.smarthome.util.xmlrpc.XMLRPC;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class AccessoActivity extends FragmentActivity implements TabListener {

	private SQLiteDatabase db;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    
		setContentView(R.layout.activity_accesso);
		
		db = new DatabaseHelper(this).getWritableDatabase();
		
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

    public void fastLogin() {
		
		Utente user = Utente.getConnessioneVeloce(db);
		String serverAddress = Impostazione.getIndirizzo(db);
        user = XMLRPC.loginUtente(user, serverAddress);
		if (user != null) {
        	Intent i = new Intent(AccessoActivity.this, MainActivity.class);
        	i.putExtra(Costanti.UTENTE, user);
            startActivity(i);
            finish();
		}	
		else
			Toast.makeText(getApplicationContext(), "Username/password non valide.Riprova", Toast.LENGTH_SHORT).show();
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

	
	

	public void login(View arg0) { 
		EditText username = (EditText)findViewById(R.id.usernameEditText);
		EditText password = (EditText)findViewById(R.id.passwordEditText);
 	    
		if (!username.getText().toString().equals("")) { 
			if (!password.getText().toString().equals("")) {
				Utente user = new Utente(username.getText().toString(), password.getText().toString());
				String serverAddress = Impostazione.getIndirizzo(db);
		        user = XMLRPC.loginUtente(user, serverAddress); 
				if (user != null) {
		        	Intent i = new Intent(AccessoActivity.this, MainActivity.class);
		        	i.putExtra(Costanti.UTENTE, user);
		            startActivity(i);
		            finish();
				}	
				else 
					Toast.makeText(getApplicationContext(), "Username/password non valide.Riprova", Toast.LENGTH_SHORT).show();
			}
			else
				Toast.makeText(getApplicationContext(), "Inserire Password", Toast.LENGTH_SHORT).show();
		}
		else
			Toast.makeText(getApplicationContext(), "Inserire Username", Toast.LENGTH_SHORT).show();
	}
	
		
   
}
