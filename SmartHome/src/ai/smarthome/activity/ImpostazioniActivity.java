package ai.smarthome.activity;

import ai.smarthome.R;
import ai.smarthome.database.DatabaseHelper;
import ai.smarthome.database.wrapper.Impostazione;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class ImpostazioniActivity extends Activity {

	private SQLiteDatabase db;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_impostazioni);
	
		db = new DatabaseHelper(this).getReadableDatabase();
        
        EditText indirizzo = (EditText) findViewById(R.id.editTextServer);
        indirizzo.setText(Impostazione.getIndirizzo(db));
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

    @Override
    public void onBackPressed() {
		finish();
	}
	
    public void cambiaImpostazioni(View view) {
    	AlertDialog.Builder alert = new AlertDialog.Builder(this);

    	alert.setTitle("Conferma operazione");
    	alert.setMessage("Inserisci password di sistema:");

    	final EditText input = new EditText(this);
    	input.setText("smartkitchen");
    	alert.setView(input);

    	alert.setPositiveButton("Conferma", new DialogInterface.OnClickListener() {
    		public void onClick(DialogInterface dialog, int whichButton) {
    			String value = input.getText().toString();
    			String accesso = "smartkitchen";
    			if (value.equals(accesso)) {
    				Toast.makeText(getApplicationContext(), "Password corretta", Toast.LENGTH_SHORT).show();
    				EditText indirizzo = (EditText) findViewById(R.id.editTextServer);
    				Impostazione.updateIndirizzo(db, indirizzo.getText().toString().trim());
    				Toast.makeText(getApplicationContext(), "Indirizzo Server modificato con successo", Toast.LENGTH_SHORT).show();
    			}
    			else
    				Toast.makeText(getApplicationContext(), "Password non corretta", Toast.LENGTH_SHORT).show();
		  }
    	});

    	alert.setNegativeButton("Annulla", null);

    	alert.show();
    	
    }
}


