package ai.smarthome.activity;

import ai.smarthome.R;
import ai.smarthome.database.DatabaseHelper;
import ai.smarthome.database.wrapper.Componente;
import ai.smarthome.database.wrapper.Oggetto;
import ai.smarthome.database.wrapper.Utente;
import ai.smarthome.util.Costanti;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

public class SimulazioneActivity extends Activity  {

	private Utente user;
	private SQLiteDatabase db;
	private Button apriButton, chiudiButton, accendiButton, spegniButton, prendiButton, lasciaButton, consentiButton, negaButton;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		db = new DatabaseHelper(this).getWritableDatabase();
		
		setContentView(R.layout.activity_simulazione);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            user = (Utente) bundle.get(Costanti.UTENTE);
        }
		 
        apriButton = (Button) findViewById(R.id.buttonApri);
        chiudiButton = (Button) findViewById(R.id.buttonChiudi);
        accendiButton = (Button) findViewById(R.id.buttonAccendi);
        spegniButton = (Button) findViewById(R.id.buttonSpegni);
        prendiButton = (Button) findViewById(R.id.buttonPrendi);
        lasciaButton = (Button) findViewById(R.id.buttonLascia);
        consentiButton = (Button) findViewById(R.id.buttonConsenti);
        negaButton = (Button) findViewById(R.id.buttonNega);
        
        apriButton.setEnabled(Componente.checkComponente(Componente.APERTO_CHIUSO, 0, db));
        chiudiButton.setEnabled(Componente.checkComponente(Componente.APERTO_CHIUSO, 1, db));
        accendiButton.setEnabled(Componente.checkComponente(Componente.ACCESO_SPENTO, 0, db));
        spegniButton.setEnabled(Componente.checkComponente(Componente.ACCESO_SPENTO, 1, db));
        
        prendiButton.setEnabled(Componente.checkDispensaMobile(Componente.APERTO_CHIUSO, 1, db));
        lasciaButton.setEnabled(false);
        
        consentiButton.setEnabled(false);
        negaButton.setEnabled(false);
        
        
        
        	
        
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
                	intent.putExtra(Costanti.UTENTE, user);
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
		new AlertDialog.Builder(this).setIcon(R.drawable.attenzione).setTitle("Simulazione")
        .setMessage("Termina simulazione?")
        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            	Intent intent = new Intent(SimulazioneActivity.this, MainActivity.class);
            	intent.putExtra(Costanti.UTENTE, user);
                Oggetto.reset(db);
                startActivity(intent);
                finish();
            }
        }).setNegativeButton("No", null).show();
    }


	public void exeApri(View view) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		final AlertDialog.Builder alertDialogBuilder2 = new AlertDialog.Builder(this);
		alertDialogBuilder
 			.setTitle("Apri")
 			.setSingleChoiceItems(Componente.getListaCharSequence(Componente.APERTO_CHIUSO, 0, db), -1, null)
			.setPositiveButton("Conferma",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					ListView lw = ((AlertDialog)dialog).getListView();
					String checkedItem = (String) lw.getAdapter().getItem(lw.getCheckedItemPosition());
					Componente.apri(db, checkedItem);
					chiudiButton.setEnabled(true);
					apriButton.setEnabled(Componente.checkComponente(Componente.APERTO_CHIUSO, 0, db));
					prendiButton.setEnabled(Componente.checkDispensaMobile(Componente.APERTO_CHIUSO, 1, db));
					alertDialogBuilder2
			 			.setTitle("Apri")
			 			.setSingleChoiceItems(Componente.getListaCharSequence(Componente.APERTO_CHIUSO, 0, db), -1, null)
						.setPositiveButton("Conferma",new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,int id) {
								ListView lw = ((AlertDialog)dialog).getListView();
								String checkedItem = (String) lw.getAdapter().getItem(lw.getCheckedItemPosition());
								Componente.apri(db, checkedItem);
								chiudiButton.setEnabled(true);
								apriButton.setEnabled(Componente.checkComponente(Componente.APERTO_CHIUSO, 0, db));
								prendiButton.setEnabled(Componente.checkDispensaMobile(Componente.APERTO_CHIUSO, 1, db));
							}
						})
						.setNegativeButton("Annulla",new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,int id) {
								dialog.cancel();
							}
						});
					alertDialogBuilder2.create().show();
				}
			})
			.setNegativeButton("Annulla",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					dialog.cancel();
				}
			});
 		alertDialogBuilder.create().show();
 			
	}
	
	public void exeChiudi(View view) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder
 			.setTitle("Chiudi")
 			.setSingleChoiceItems(Componente.getListaCharSequence(Componente.APERTO_CHIUSO, 1, db), -1, null)
			.setPositiveButton("Conferma",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					ListView lw = ((AlertDialog)dialog).getListView();
					String checkedItem = (String) lw.getAdapter().getItem(lw.getCheckedItemPosition());
					Componente.chiudi(db, checkedItem);
					apriButton.setEnabled(true);
					chiudiButton.setEnabled(Componente.checkComponente(Componente.APERTO_CHIUSO, 1, db));
					prendiButton.setEnabled(Componente.checkDispensaMobile(Componente.APERTO_CHIUSO, 0, db));
				}
			})
			.setNegativeButton("Annulla",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					dialog.cancel();
				}
			});
 		alertDialogBuilder.create().show();
 	
	}
	
	public void exeAccendi(View view) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder
 			.setTitle("Accendi")
 			.setSingleChoiceItems(Componente.getListaCharSequence(Componente.ACCESO_SPENTO, 0, db), -1, null)
			.setPositiveButton("Conferma",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					ListView lw = ((AlertDialog)dialog).getListView();
					String checkedItem = (String) lw.getAdapter().getItem(lw.getCheckedItemPosition());
					Componente.accendi(db, checkedItem);
					spegniButton.setEnabled(true);
					accendiButton.setEnabled(Componente.checkComponente(Componente.ACCESO_SPENTO, 0, db));
				}
			})
			.setNegativeButton("Annulla",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					dialog.cancel();
				}
			});
 		alertDialogBuilder.create().show();
 			
	}
	
	public void exeSpegni(View view) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder
 			.setTitle("Spegni")
 			.setSingleChoiceItems(Componente.getListaCharSequence(Componente.ACCESO_SPENTO, 1, db), -1, null)
			.setPositiveButton("Conferma",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					ListView lw = ((AlertDialog)dialog).getListView();
					String checkedItem = (String) lw.getAdapter().getItem(lw.getCheckedItemPosition());
					Componente.spegni(db, checkedItem);
					accendiButton.setEnabled(true);
					spegniButton.setEnabled(Componente.checkComponente(Componente.ACCESO_SPENTO, 1, db));
				}
			})
			.setNegativeButton("Annulla",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					dialog.cancel();
				}
			});
 		alertDialogBuilder.create().show();
 			
	}
	
	public void exePrendi(View view) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder
 			.setTitle("Prendi")
 			.setSingleChoiceItems(Oggetto.getListaCharSequence(db, 0), -1, null)
			.setPositiveButton("Conferma",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					ListView lw = ((AlertDialog)dialog).getListView();
					String checkedItem = (String) lw.getAdapter().getItem(lw.getCheckedItemPosition());
					Oggetto.prendi(db, checkedItem);
					lasciaButton.setEnabled(true);
				}
			})
			.setNegativeButton("Annulla",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					dialog.cancel();
				}
			});
 		alertDialogBuilder.create().show();
 			
	}
	
	public void exeLascia(View view) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder
 			.setTitle("Lascia")
 			.setSingleChoiceItems(Oggetto.getListaCharSequence(db, 1), -1, null)
			.setPositiveButton("Conferma",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					ListView lw = ((AlertDialog)dialog).getListView();
					String checkedItem = (String) lw.getAdapter().getItem(lw.getCheckedItemPosition());
					Oggetto.lascia(db, checkedItem);
				}
			})
			.setNegativeButton("Annulla",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					dialog.cancel();
				}
			});
 		alertDialogBuilder.create().show();
 			
	}
	
	public void exeConsenti(View view) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder
 			.setTitle("Consenti?")
 			.setPositiveButton("Consenti",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					
				}
			})
			.setNegativeButton("Annulla",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					dialog.cancel();
				}
			});
 		alertDialogBuilder.create().show();
 			
	}
	
	public void exeNega(View view) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder
 			.setTitle("Nega?")
 			.setPositiveButton("Nega",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					
				}
			})
			.setNegativeButton("Annulla",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					dialog.cancel();
				}
			});
 		alertDialogBuilder.create().show();
 			
	}
}
