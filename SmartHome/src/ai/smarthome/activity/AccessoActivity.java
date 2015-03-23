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

	private EditText  mail;
	private EditText  password;
	private EditText  password2;
	private EditText  cognome;
	private EditText  nome;
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
