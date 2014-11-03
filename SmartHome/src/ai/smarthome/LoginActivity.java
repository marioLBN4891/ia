package ai.smarthome;

import java.util.Date;

import ai.smarthome.database.DatabaseHelper;
import ai.smarthome.database.wrapper.Utente;
import ai.smarthome.util.Utilities;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {

	private EditText  username;
	private EditText  password;
	private DatabaseHelper dbH;
	
	public static final String UTENTE = "utente";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_login);
	    
	    username = (EditText)findViewById(R.id.usernameEditText);
	    password = (EditText)findViewById(R.id.passwordEditText);
	    dbH = new DatabaseHelper(this);
	    
	    fastLogin();
	    
	    addListenerOnLoginButton();
	    addListenerOnRegistrationButton();
	    addListenerOnPasswordReminderButton();
	
	    
	}

	public void addListenerOnLoginButton() {
		 
		findViewById(R.id.loginButton).setOnClickListener(new OnClickListener() {
 			@Override
			public void onClick(View arg0) {
 
				if (!username.getText().toString().equals("")) {
					if (!password.getText().toString().equals("")) {
						Utente user = dbH.isUtenteRegistered(username.getText().toString(), password.getText().toString());
				        if (user != null) {
				        	dbH.setAccesso(user.getId(), new Date().toString());
				        	CheckBox checkbox = (CheckBox) findViewById(R.id.checkConnessione);
				        	if (checkbox.isChecked()) 
				        		dbH.setConnessioneVeloce(user.getUsername(), user.getPassword());
				        	Intent i = new Intent(LoginActivity.this, MainActivity.class);
				        	i.putExtra(UTENTE, user);
				            startActivity(i);
				            finish();
						}	
						else 
							Toast.makeText(getApplicationContext(), "Username/password non valide.Riprova", Toast.LENGTH_SHORT).show();
					}
					else
						Toast.makeText(getApplicationContext(), "Inserire password", Toast.LENGTH_SHORT).show();
				}
				else
					Toast.makeText(getApplicationContext(), "Inserire Username", Toast.LENGTH_SHORT).show();
			   
			}
 		});
 	}
	
	
	public void addListenerOnRegistrationButton() {
		 
		findViewById(R.id.registrationButton).setOnClickListener(new OnClickListener() {
 			@Override
			public void onClick(View arg0) {
 				Intent i = new Intent(LoginActivity.this, RegistrazioneActivity.class);
		        startActivity(i);
			}
 		});
 
	}
	
	
	public void addListenerOnPasswordReminderButton() {
		 
		findViewById(R.id.passwordDimenticataButton).setOnClickListener(new OnClickListener() {
 			@Override
			public void onClick(View arg0) {
 				Intent i = new Intent(LoginActivity.this, PasswordActivity.class);
 		        startActivity(i);
 		    }
 		});
 
	}
		
	
	
	public void fastLogin() {
		
		Utente user = dbH.getConnessioneVeloce();
        if (user != null) {
        	dbH.setAccesso(user.getId(), new Date().toString());
        	Intent i = new Intent(LoginActivity.this, MainActivity.class);
        	Log.d("aab", user.getCognome() +" "+ user.getNome());
        	i.putExtra(UTENTE, user);
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
}
