package ai.smarthome;

import ai.smarthome.database.DatabaseHelper;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class RegistrationActivity extends Activity {

	private EditText  username;
	private EditText  password;
	private EditText  password2;
	private EditText  cognome;
	private EditText  nome;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registration); 

		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		username = (EditText)findViewById(R.id.usernameEditText);
	    password = (EditText)findViewById(R.id.passwordEditText);
	    password2 = (EditText)findViewById(R.id.password2EditText);
	    cognome = (EditText)findViewById(R.id.cognomeEditText);
	    nome = (EditText)findViewById(R.id.nomeEditText);
	    
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.registration, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	
	public void salvaRegistrazione(View view) {
		
		if (!cognome.getText().toString().equals("")) {
			if (!nome.getText().toString().equals("")) {
				if (!username.getText().toString().equals("") && username.getText().toString().length() >= 5) {
					if (!password.getText().toString().equals("") && password.getText().toString().length() >= 5) {
						if (!password2.getText().toString().equals("") && password2.getText().toString().length() >= 5) {
							if (password2.getText().toString().equals(password.getText().toString())) {
								DatabaseHelper dbH = new DatabaseHelper(this);
						        if (!dbH.isUsernameRegistered(username.getText().toString())) {
						        	dbH.setUtente(username.getText().toString(), password.getText().toString(), cognome.getText().toString(), nome.getText().toString());
						        	dbH.printLogUtenti();
						        	Toast.makeText(getApplicationContext(), "Registrazione avvenuta con successo", Toast.LENGTH_SHORT).show();
						        	Intent i = new Intent(RegistrationActivity.this, LoginActivity.class);
						        	startActivity(i);
						        	finish();
						        }
						        else
						        	Toast.makeText(getApplicationContext(), "Username già presente", Toast.LENGTH_SHORT).show();
							}
							else
								Toast.makeText(getApplicationContext(), "Password digitata non corretta", Toast.LENGTH_SHORT).show();
						}
						else
							Toast.makeText(getApplicationContext(), "Inserire nuovamente la password", Toast.LENGTH_SHORT).show();
					}
					else
						Toast.makeText(getApplicationContext(), "Password è un campo obbligatorio di almeno 5 caratteri", Toast.LENGTH_SHORT).show();
				}
				else
					Toast.makeText(getApplicationContext(), "Username è un campo obbligatorio di almeno 5 caratteri", Toast.LENGTH_SHORT).show();
			}
			else
				Toast.makeText(getApplicationContext(), "Nome è obbligatorio", Toast.LENGTH_SHORT).show();
		}
		else
			Toast.makeText(getApplicationContext(), "Cognome è obbligatorio", Toast.LENGTH_SHORT).show();
		
		
	}
}
