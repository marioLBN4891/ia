package ai.smarthome.activity;

import ai.smarthome.R;
import ai.smarthome.async.AsyncPasswordReminderMail;
import ai.smarthome.database.DatabaseHelper;
import ai.smarthome.database.wrapper.Utente;
import ai.smarthome.util.Utilities;
import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

public class PasswordActivity extends Activity {

	private EditText  mail;
	private SQLiteDatabase db;
	private Utente utente;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) { 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_password);
		
		db = new DatabaseHelper(this).getWritableDatabase();
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		mail = (EditText)findViewById(R.id.mailEditText);
	    addListenerOnSendMailButton();
	}

	public void addListenerOnSendMailButton() {
		 
		findViewById(R.id.sendMailButton).setOnClickListener(new OnClickListener() {
 		@Override
 			public void onClick(View arg0) {
 
 				if (mail.getText().toString().trim().equals("")) {
 					Toast.makeText(getApplicationContext(), "Email è obbligatorio", Toast.LENGTH_SHORT).show();
 					return;
 				}
 				if (!Utilities.emailValida(mail.getText().toString().trim())) {
 					Toast.makeText(getApplicationContext(), "Email non valida", Toast.LENGTH_SHORT).show();
 					return;
 				}
 				if (!Utente.isMailRegistered(db, mail.getText().toString().trim())) {
 					Toast.makeText(getApplicationContext(), "Email non registrata", Toast.LENGTH_SHORT).show();
 					return;
 				}
			
 				
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
