package ai.smarthome.activity;

import ai.smarthome.R;
import ai.smarthome.R.drawable;
import ai.smarthome.R.layout;
import ai.smarthome.database.wrapper.Configurazione;
import ai.smarthome.util.Utilities;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class SimulazioneActivity extends Activity {

	public static final String CONFIGURAZIONE = "configurazione";
	private Configurazione conf;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_simulazione);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            conf = (Configurazione) bundle.get(CONFIGURAZIONE);
        }
		 
		 
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
        case android.R.id.home:
        	new AlertDialog.Builder(this).setIcon(R.drawable.attenzione).setTitle("Simulazione")
            .setMessage("Termina simulazione?")
            .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                	Intent intent = new Intent(SimulazioneActivity.this, MainActivity.class);
                	intent.putExtra(CONFIGURAZIONE, conf);
                    startActivity(intent);
                    finish();
                }
            }).setNegativeButton("No", null).show();
        	return true;
        default:
            return super.onOptionsItemSelected(item);
        }
	}
	
	@Override
    public void onBackPressed() {
		Utilities.chiudiApplicazione(this);
    }


	public void exeApri(View view) {
		
	}
	
	public void exeChiudi(View view) {
		
	}
	
	public void exeAccendi(View view) {
		
	}

	public void exeSpegni(View view) {
	
	}

	public void exePrendi(View view) {
	
	}

	public void exeLascia(View view) {
	
	}

	public void exeConsenti(View view) {
	
	}

	public void exeRifiuta(View view) {
	
	}
}
