package ai.smarthome;

import java.util.Date;

import ai.smarthome.database.DatabaseHelper;
import ai.smarthome.database.wrapper.Utente;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {

	private EditText  username;
	private EditText  password;
	private Button login;
	private int counter = 3;
	private static int LOGIN_TIME_OUT = 10000;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_login);
	    
	    username = (EditText)findViewById(R.id.usernameEditText);
	    password = (EditText)findViewById(R.id.passwordEditText);
	    
	    username.setText("mariol");
	    password.setText("123456");
	    login = (Button)findViewById(R.id.loginButton);
	
	}

	
	public void accedi(View view) {
	    
		if (!username.getText().toString().equals("")) {
			if (!password.getText().toString().equals("")) {
				DatabaseHelper dbH = new DatabaseHelper(this);
				Utente user = dbH.isUtenteRegistered(username.getText().toString(), password.getText().toString());
		        if (user != null) {
		        	dbH.setAccesso(user.getId(), new Date().toString());
		        	Intent i = new Intent(LoginActivity.this, MainActivity.class);
		        	String pkg=getPackageName();
		        	i.putExtra(pkg+".user", user);
		            startActivity(i);
		            finish();
				}	
				else {
					Toast.makeText(getApplicationContext(), "Username/password non valide.Riprova", Toast.LENGTH_SHORT).show();
					if(counter==0) {
						Toast.makeText(getApplicationContext(), "Accesso temporaneamente sospeso", Toast.LENGTH_SHORT).show();
						login.setEnabled(false);
					}
				}
			}
			else
				Toast.makeText(getApplicationContext(), "Inserire password", Toast.LENGTH_SHORT).show();
		}
		else
			Toast.makeText(getApplicationContext(), "Inserire Username", Toast.LENGTH_SHORT).show();
	   
		if (counter == 0) {
			
			new Handler().postDelayed(new Runnable() {
				 
	            @Override
	            public void run() {
	            	Toast.makeText(getApplicationContext(), "Accesso riabilitato", Toast.LENGTH_SHORT).show();
	            	login.setEnabled(true);
					counter = 3;
	            }
	        }, LOGIN_TIME_OUT);
			
		}
		
	}
	

	public void registrati(View view) {
		Intent i = new Intent(LoginActivity.this, RegistrationActivity.class);
        startActivity(i);
		
	 }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
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
}
