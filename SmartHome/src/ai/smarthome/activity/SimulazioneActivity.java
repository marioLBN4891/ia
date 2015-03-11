package ai.smarthome.activity;

import java.util.ArrayList;

import ai.smarthome.R;
import ai.smarthome.database.DatabaseHelper;
import ai.smarthome.database.wrapper.Componente;
import ai.smarthome.database.wrapper.Oggetto;
import ai.smarthome.database.wrapper.Report;
import ai.smarthome.database.wrapper.Utente;
import ai.smarthome.util.Costanti;
import ai.smarthome.util.Prolog;
import ai.smarthome.util.UtilConfigurazione;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class SimulazioneActivity extends Activity  {

	private Utente user;
	private SQLiteDatabase db;
	private Button apriButton, chiudiButton, accendiButton, spegniButton, prendiButton, lasciaButton, consentiButton, negaButton;
	private String timestamp;
	
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
        
        
        String timestamp = UtilConfigurazione.setTimestamp();
        
        if (!Prolog.eseguiConfigurazione(db, timestamp)) {
    		alertServerError();
    	}
        
        apriButton = (Button) findViewById(R.id.buttonApri);
        chiudiButton = (Button) findViewById(R.id.buttonChiudi);
        accendiButton = (Button) findViewById(R.id.buttonAccendi);
        spegniButton = (Button) findViewById(R.id.buttonSpegni);
        prendiButton = (Button) findViewById(R.id.buttonPrendi);
        lasciaButton = (Button) findViewById(R.id.buttonLascia);
        consentiButton = (Button) findViewById(R.id.buttonConsenti);
        negaButton = (Button) findViewById(R.id.buttonNega);
        
        apriButton.setEnabled(true);
        chiudiButton.setEnabled(Componente.checkComponente(Componente.APERTO_CHIUSO, 1, db));
        accendiButton.setEnabled(Componente.checkComponente(Componente.ACCESO_SPENTO, 0, db));
        spegniButton.setEnabled(Componente.checkComponente(Componente.ACCESO_SPENTO, 1, db));
        
        lasciaButton.setEnabled(false);
        
        consentiButton.setEnabled(false);
        negaButton.setEnabled(false);
        
        refreshListaFatti();
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.menu_action_simulazione, menu);
	    return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case R.id.report:
			Intent intent = new Intent(SimulazioneActivity.this, ReportActivity.class);
			startActivity(intent);
			return true;
            
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
            	if (!Prolog.stopSimulazione()) {
            		new AlertDialog.Builder(getApplicationContext())
         			.setTitle("Errore")
         			.setMessage("Impossibile chiudere correttamente la sessione.")
         			.setPositiveButton("Continua",new DialogInterface.OnClickListener() {
        				public void onClick(DialogInterface dialog,int id) {
        					dialog.cancel();
        				}
        			}).create().show();
            	}
            	Intent intent = new Intent(SimulazioneActivity.this, MainActivity.class);
            	intent.putExtra(Costanti.UTENTE, user);
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
 			.setSingleChoiceItems(Componente.getListaCharSequence(Componente.APERTO_CHIUSO, 0, db), 0, null)
			.setPositiveButton("Conferma",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					ListView lw = ((AlertDialog)dialog).getListView();
					final String checkedItem = (String) lw.getAdapter().getItem(lw.getCheckedItemPosition());
					
					if (checkedItem.equals("Dispensa") || checkedItem.equals("Mobile") || checkedItem.equals("Frigorifero")) {
						alertDialogBuilder2
			 				.setTitle("Prendi")
			 				.setSingleChoiceItems(Oggetto.getListaCharSequence(checkedItem, 0, db), 0, null)
			 				.setPositiveButton("Conferma",new DialogInterface.OnClickListener() {
			 					public void onClick(DialogInterface dialog,int id) {
			 						ListView lw = ((AlertDialog)dialog).getListView();
			 						String checkedItem1 = (String) lw.getAdapter().getItem(lw.getCheckedItemPosition());
			 						String fatto = "preso("+timestamp+","+checkedItem1+")";
			 						Report report = new Report("Hai preso "+checkedItem1, checkedItem1, 1, Prolog.creaStringaFatto(fatto, "1.0"), 0, 1);
			 						if (Prolog.sendFatto(db, report)) {
			 							Oggetto.prendi(db, checkedItem1);
			 							lasciaButton.setEnabled(true);
			 							refreshListaFatti();
			 						}
			 						else
			 							alertServerError();
			 						
			 						}
			 				})
			 				.setNegativeButton("Annulla",new DialogInterface.OnClickListener() {
			 					public void onClick(DialogInterface dialog,int id) {
			 						dialog.cancel();
			 					}
			 				});
						alertDialogBuilder2.create().show();
					}
					else {
						String fatto = Componente.getProlog(db, checkedItem)+"("+timestamp+",open)";
						Report report = new Report("Hai aperto "+checkedItem, checkedItem, 1, Prolog.creaStringaFatto(fatto, "1.0"), 0, 1);
						if (Prolog.sendFatto(db, report)) {
							Componente.OnOpen(db, checkedItem);
							chiudiButton.setEnabled(Componente.checkComponente(Componente.APERTO_CHIUSO, 1, db));
							refreshListaFatti();
						}
						else
							alertServerError();
					}
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
 			.setSingleChoiceItems(Componente.getListaCharSequence(Componente.APERTO_CHIUSO, 1, db), 0, null)
			.setPositiveButton("Conferma",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					ListView lw = ((AlertDialog)dialog).getListView();
					String checkedItem = (String) lw.getAdapter().getItem(lw.getCheckedItemPosition());
					String fatto = Componente.getProlog(db, checkedItem)+"("+timestamp+",close)";
					Report report = new Report("Hai chiuso "+checkedItem, checkedItem, 0, Prolog.creaStringaFatto(fatto, "1.0"), 0, 1);
					if (Prolog.sendFatto(db, report)) {
						Componente.OffClose(db, checkedItem);
						apriButton.setEnabled(true);
						chiudiButton.setEnabled(Componente.checkComponente(Componente.APERTO_CHIUSO, 1, db));
						refreshListaFatti();
					}
					else
						alertServerError();
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
 			.setSingleChoiceItems(Componente.getListaCharSequence(Componente.ACCESO_SPENTO, 0, db), 0, null)
			.setPositiveButton("Conferma",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					ListView lw = ((AlertDialog)dialog).getListView();
					String checkedItem = (String) lw.getAdapter().getItem(lw.getCheckedItemPosition());
					String fatto = Componente.getProlog(db, checkedItem)+"("+timestamp+",on)";
					Report report = new Report("Hai acceso "+checkedItem, checkedItem, 1, Prolog.creaStringaFatto(fatto, "1.0"), 0, 1);
					if (Prolog.sendFatto(db, report)) {
						Componente.OnOpen(db, checkedItem);
						spegniButton.setEnabled(true);
						accendiButton.setEnabled(Componente.checkComponente(Componente.ACCESO_SPENTO, 0, db));
						refreshListaFatti();
					}
					else
						alertServerError();
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
 			.setSingleChoiceItems(Componente.getListaCharSequence(Componente.ACCESO_SPENTO, 1, db), 0, null)
			.setPositiveButton("Conferma",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					ListView lw = ((AlertDialog)dialog).getListView();
					String checkedItem = (String) lw.getAdapter().getItem(lw.getCheckedItemPosition());
					String fatto = Componente.getProlog(db, checkedItem)+"("+timestamp+",on)";
					Report report = new Report("Hai spento "+checkedItem, checkedItem, 0, Prolog.creaStringaFatto(fatto, "1.0"), 0, 1);
					if (Prolog.sendFatto(db, report)) {
						Componente.OffClose(db, checkedItem);
						accendiButton.setEnabled(true);
						spegniButton.setEnabled(Componente.checkComponente(Componente.ACCESO_SPENTO, 1, db));
						refreshListaFatti();
					}
					else
						alertServerError();
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
 			.setSingleChoiceItems(Oggetto.getListaCharSequence(Oggetto.CUCINA, 0, db), 0, null)
			.setPositiveButton("Conferma",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					ListView lw = ((AlertDialog)dialog).getListView();
					String checkedItem = (String) lw.getAdapter().getItem(lw.getCheckedItemPosition());
					String fatto = "preso("+timestamp+","+checkedItem+")";
					Report report = new Report("Hai preso "+checkedItem, checkedItem, 1, Prolog.creaStringaFatto(fatto, "1.0"), 0, 1);
					if (Prolog.sendFatto(db, report)) {
						Oggetto.prendi(db, checkedItem);
						lasciaButton.setEnabled(true);
						prendiButton.setEnabled(Oggetto.checkOggetto(Oggetto.CUCINA, 0, db));
						refreshListaFatti();
					}
					else
						alertServerError();
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
 			.setSingleChoiceItems(Oggetto.getListaCharSequence(null, 1, db), 0, null)
			.setPositiveButton("Conferma",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					ListView lw = ((AlertDialog)dialog).getListView();
					String checkedItem = (String) lw.getAdapter().getItem(lw.getCheckedItemPosition());
					String fatto = "lascia("+timestamp+","+checkedItem+")";
					Report report = new Report("Hai lasciato "+checkedItem, checkedItem, 0, Prolog.creaStringaFatto(fatto, "1.0"), 0, 1);
					if (Prolog.sendFatto(db, report)) {
						Oggetto.lascia(db, checkedItem);
						lasciaButton.setEnabled(Oggetto.checkOggetto(null, 1, db));
						prendiButton.setEnabled(Oggetto.checkOggetto(Oggetto.CUCINA, 0, db));
						refreshListaFatti();
					}
					else
						alertServerError();
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
	
	private void refreshListaFatti() {
	
		TableLayout tableLayout = (TableLayout) findViewById(R.id.tableSVlayout);
	    TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
	    lp.setMargins(10, 2, 10, 3);
	    
	    ArrayList<Report> lista = Report.getLista(db);
	    
	    tableLayout.removeAllViews();
	    
	    for (Report r: lista) {
	    	TextView textView1 = new TextView(this);
	    	TextView textView2 = new TextView(this);
		    textView1.setText(r.getId());
	    	textView2.setText(r.getAzione());
		    textView1.setTextSize(16);
		    textView2.setTextSize(16);
		    textView1.setLayoutParams(lp);
		    textView2.setLayoutParams(lp);
		    
		    if (r.getNuovo() == 1 && r.getSentReceived() == 0) {
		    	textView1.setTextColor(Color.RED);
			    textView1.setTypeface(null, Typeface.BOLD);
			    textView2.setTextColor(Color.RED);
			    textView2.setTypeface(null, Typeface.BOLD);
			}
		    if (r.getNuovo() == 0 && r.getSentReceived() == 0) {
		    	textView1.setTextColor(Color.BLUE);
		    	textView2.setTextColor(Color.BLACK);
			}
			    
		    if (r.getSentReceived() == 1) {
		    	textView1.setTextColor(Color.CYAN);
			    textView2.setTextColor(Color.CYAN);
			}
			
		    TableRow riga = new TableRow(this);
		    riga.setLayoutParams(lp);
		    riga.addView(textView1);
		    riga.addView(textView2);
		    tableLayout.addView(riga, 0);
		}
	    
	    Report.updateReport(db);
	}
	
	private void alertServerError() {
		new AlertDialog.Builder(getApplicationContext())
		.setIcon(R.drawable.attenzione).setTitle("Errore")
			.setMessage("Impossibile contattare il Server.")
			.setPositiveButton("Indietro",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				dialog.cancel();
			}
		}).show();
	}
}