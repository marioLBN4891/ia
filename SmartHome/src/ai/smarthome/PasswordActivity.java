package ai.smarthome;

import ai.smarthome.async.AsyncPasswordReminderMail;
import ai.smarthome.database.DatabaseHelper;
import ai.smarthome.database.wrapper.Utente;
import ai.smarthome.util.Utilities;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

public class PasswordActivity extends Activity {

	private EditText  username;
	private EditText  mail;
	private DatabaseHelper dbH;
	private Utente utente;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) { 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_password);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		username = (EditText)findViewById(R.id.usernameEditText);
		mail = (EditText)findViewById(R.id.mailEditText);
	    dbH = new DatabaseHelper(this);
	    
	    addListenerOnSendMailButton();
	}

	public void addListenerOnSendMailButton() {
		 
		findViewById(R.id.sendMailButton).setOnClickListener(new OnClickListener() {
 		@Override
 			public void onClick(View arg0) {
 
 				if (username.getText().toString().trim().equals("") && username.getText().toString().trim().length() < 5 && Utilities.checkSpace(username.getText().toString())) {
 					Toast.makeText(getApplicationContext(), "Username è un campo obbligatorio di almeno 5 caratteri", Toast.LENGTH_SHORT).show();
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
 				if (!dbH.isUsernameRegistered(username.getText().toString().trim()) || !dbH.isMailRegistered(mail.getText().toString().trim())) {
 					Toast.makeText(getApplicationContext(), "Username/email non validi", Toast.LENGTH_SHORT).show();
 					return;
 				}
			
 				utente = dbH.sendMailToUtenteRegistered(username.getText().toString().trim(), mail.getText().toString().trim());
 				
 				Toast.makeText(getApplicationContext(), "Invio mail in corso", Toast.LENGTH_SHORT).show();
 		        
 				new AsyncPasswordReminderMail(PasswordActivity.this).execute();
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

	public Utente getUtente() {
		return this.utente;
	}
}
