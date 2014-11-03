package ai.smarthome;

import ai.smarthome.database.DatabaseHelper;
import ai.smarthome.util.Utilities;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

public class RegistrazioneActivity extends Activity {

	private EditText  username;
	private EditText  password;
	private EditText  password2;
	private EditText  cognome;
	private EditText  nome;
	private EditText  mail;
	private DatabaseHelper dbH;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registrazione); 

		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		username = (EditText)findViewById(R.id.usernameEditText);
	    password = (EditText)findViewById(R.id.passwordEditText);
	    password2 = (EditText)findViewById(R.id.password2EditText);
	    cognome = (EditText)findViewById(R.id.cognomeEditText);
	    nome = (EditText)findViewById(R.id.nomeEditText);
	    mail = (EditText)findViewById(R.id.mailEditText);
	    dbH = new DatabaseHelper(this);
	    
	    addListenerOnRegistrationButton();
	    
	}
	
	
	public void addListenerOnRegistrationButton() {
		 
		findViewById(R.id.saveRegistrationButton).setOnClickListener(new OnClickListener() {
 		@Override
		public void onClick(View arg0) {
 
			if (cognome.getText().toString().trim().equals("")) {
				Toast.makeText(getApplicationContext(), "Cognome è obbligatorio", Toast.LENGTH_SHORT).show();
				return;
			}
			if (nome.getText().toString().trim().equals("")) {
				Toast.makeText(getApplicationContext(), "Nome è obbligatorio", Toast.LENGTH_SHORT).show();
				return;
			}
			if (mail.getText().toString().trim().equals("")) {
				Toast.makeText(getApplicationContext(), "Email è obbligatorio", Toast.LENGTH_SHORT).show();
				return;
			}
			if (!Utilities.emailValida(mail.getText().toString().trim())) {
				Toast.makeText(getApplicationContext(), "Email non valida", Toast.LENGTH_SHORT).show();
				return;
			}
			if (dbH.isMailRegistered(mail.getText().toString().trim())) {
				Toast.makeText(getApplicationContext(), "Email già registrata", Toast.LENGTH_SHORT).show();
				return;
			}
			if (username.getText().toString().trim().equals("") || username.getText().toString().trim().length() < 5 || Utilities.checkSpace(username.getText().toString())) {
				Toast.makeText(getApplicationContext(), "Username è un campo obbligatorio di almeno 5 caratteri non vuoti", Toast.LENGTH_SHORT).show();
				return;
			}
			if (dbH.isUsernameRegistered(username.getText().toString())) {
				Toast.makeText(getApplicationContext(), "Username già registrata", Toast.LENGTH_SHORT).show();
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
			
			dbH.setUtente(username.getText().toString().trim(), password.getText().toString().trim(), cognome.getText().toString().trim(), nome.getText().toString().trim(), mail.getText().toString().trim());
			dbH.printLogUtenti();
			Toast.makeText(getApplicationContext(), "Registrazione avvenuta con successo", Toast.LENGTH_SHORT).show();
			Intent i = new Intent(RegistrazioneActivity.this, LoginActivity.class);
			startActivity(i);
			finish();
		}
 		});
 
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
