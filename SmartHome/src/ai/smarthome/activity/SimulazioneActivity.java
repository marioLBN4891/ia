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
	private int contatoreAzioni = 0;
	
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		db = new DatabaseHelper(this).getWritableDatabase();
		
		setContentView(R.layout.activity_simulazione);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		ArrayList<String> listaFattiDedotti = new ArrayList<String>();
		Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            user = (Utente) bundle.get(Costanti.UTENTE);
            listaFattiDedotti = (ArrayList<String>) bundle.get("listaFattiDedotti");
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
        
        if (listaFattiDedotti != null && !listaFattiDedotti.isEmpty())
        	Report.insertFattiDedotti(db, listaFattiDedotti);
        
        ArrayList<Report> lista = Report.getLista(db);
		
		TableLayout tableLayout = (TableLayout) findViewById(R.id.tableSVlayout);
	    TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
	    lp.setMargins(10, 2, 10, 3);
	        
	    for (Report r: lista) {
	    	contatoreAzioni++;
		    
	    	TextView textView1 = new TextView(this);
		    textView1.setText("t" + r.getId());
		    textView1.setTextColor(Color.BLUE);
		    textView1.setTextSize(16);
		    textView1.setLayoutParams(lp);
		    
		    TextView textView2 = new TextView(this);
		    textView2.setText(r.getAzione());
		    textView2.setTextColor(Color.BLACK);
		    textView2.setTextSize(16);
		    textView2.setLayoutParams(lp);
		        
		    TableRow riga = new TableRow(this);
		    riga.setLayoutParams(lp);
		    riga.addView(textView1);
		    riga.addView(textView2);
		    tableLayout.addView(riga, 0);
		}
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
			 						Oggetto.prendi(db, checkedItem1);
			 						lasciaButton.setEnabled(true);
			 						
			 						String fatto = "preso("+checkedItem1+","+contatoreAzioni+")";
			 						viewSelectedAction("Hai Preso: ", checkedItem1, fatto, 1, Prolog.eseguiFatto(contatoreAzioni, fatto, "1"));
			 						
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
						Componente.apri(db, checkedItem);
						chiudiButton.setEnabled(Componente.checkComponente(Componente.APERTO_CHIUSO, 1, db));
						
						String fatto1 = checkedItem+"Open("+contatoreAzioni+")";
 						viewSelectedAction("Hai Aperto: ", checkedItem, fatto1, 1, Prolog.eseguiFatto(contatoreAzioni, fatto1, "1"));
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
					Componente.chiudi(db, checkedItem);
					chiudiButton.setEnabled(Componente.checkComponente(Componente.APERTO_CHIUSO, 1, db));
					
					String fatto1 = checkedItem+"Close("+contatoreAzioni+")";
					viewSelectedAction("Hai Chiuso: ", checkedItem, fatto1, 0, Prolog.eseguiFatto(contatoreAzioni, fatto1, "1"));
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
					Componente.accendi(db, checkedItem);
					spegniButton.setEnabled(true);
					accendiButton.setEnabled(Componente.checkComponente(Componente.ACCESO_SPENTO, 0, db));
					
					String fatto1 = checkedItem+"On("+contatoreAzioni+")";
					viewSelectedAction("Hai Acceso: ", checkedItem, fatto1, 1, Prolog.eseguiFatto(contatoreAzioni, fatto1, "1"));
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
					Componente.spegni(db, checkedItem);
					accendiButton.setEnabled(true);
					spegniButton.setEnabled(Componente.checkComponente(Componente.ACCESO_SPENTO, 1, db));
					
					String fatto1 = checkedItem+"Off("+contatoreAzioni+")";
					viewSelectedAction("Hai Spento: ", checkedItem, fatto1, 0, Prolog.eseguiFatto(contatoreAzioni, fatto1, "1"));
					
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
					Oggetto.prendi(db, checkedItem);
					lasciaButton.setEnabled(true);
					prendiButton.setEnabled(Oggetto.checkOggetto(Oggetto.CUCINA, 0, db));
					
					String fatto = "preso("+checkedItem+","+contatoreAzioni+")";
					viewSelectedAction("Hai preso: ", checkedItem, fatto, 1, Prolog.eseguiFatto(contatoreAzioni, fatto, "1"));
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
					Oggetto.lascia(db, checkedItem);
					lasciaButton.setEnabled(Oggetto.checkOggetto(null, 1, db));
					prendiButton.setEnabled(Oggetto.checkOggetto(Oggetto.CUCINA, 0, db));
					
					String fatto = "lascia("+checkedItem+","+contatoreAzioni+")";
					viewSelectedAction("Hai Lasciato: ", checkedItem, fatto, 0, Prolog.eseguiFatto(contatoreAzioni, fatto, "1"));
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
	
	private void viewSelectedAction(String message, String checkedItem, String prolog, int stato, ArrayList<String> listaFattiDedotti) {
	
		TableLayout tableLayout = (TableLayout) findViewById(R.id.tableSVlayout);
	    TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
	    lp.setMargins(10, 2, 10, 3);
	    
	    ArrayList<Report> lista = Report.getLista(db);
	    
	    Report.insert(db, contatoreAzioni, message+checkedItem, prolog, checkedItem, stato);
	    Report.insertFattiDedotti(db, listaFattiDedotti);
		
	    tableLayout.removeAllViews();
	    
	    for (Report r: lista) {
	    	TextView textView1 = new TextView(this);
		    textView1.setText("t" + r.getId());
		    textView1.setTextColor(Color.BLUE);
		    textView1.setTextSize(16);
		    textView1.setLayoutParams(lp);
		    
		    TextView textView2 = new TextView(this);
		    textView2.setText(r.getAzione());
		    textView2.setTextColor(Color.BLACK);
		    textView2.setTextSize(16);
		    textView2.setLayoutParams(lp);
		        
		    TableRow riga = new TableRow(this);
		    riga.setLayoutParams(lp);
		    riga.addView(textView1);
		    riga.addView(textView2);
		    tableLayout.addView(riga, 0);
		}
	    
	    TextView textView1 = new TextView(this);
	    textView1.setText("t" + contatoreAzioni);
	    textView1.setTextColor(Color.RED);
	    textView1.setTypeface(null, Typeface.BOLD);
	    textView1.setTextSize(16);
	    textView1.setLayoutParams(lp);
	    
	    TextView textView2 = new TextView(this);
	    textView2.setText(message + checkedItem);
	    textView2.setTextColor(Color.RED);
	    textView2.setTypeface(null, Typeface.BOLD);
	    textView2.setTextSize(16);
	    textView2.setLayoutParams(lp);
	        
	    TableRow riga = new TableRow(this);
	    riga.setLayoutParams(lp);
	    riga.addView(textView1);
	    riga.addView(textView2);
	    tableLayout.addView(riga, 0);
	    
	    
	    ArrayList<Report> listaDed = Report.listaDedottiToListaReport(listaFattiDedotti);
	    
	    for (Report r: listaDed) {
	    	TextView textView3 = new TextView(this);
		    textView3.setText("t" + r.getId());
		    textView3.setTextColor(Color.CYAN);
		    textView3.setTextSize(16);
		    textView3.setLayoutParams(lp);
		    
		    TextView textView4 = new TextView(this);
		    textView4.setText(r.getAzione());
		    textView4.setTextColor(Color.CYAN);
		    textView4.setTextSize(16);
		    textView4.setLayoutParams(lp);
		        
		    TableRow rigaDed = new TableRow(this);
		    rigaDed.setLayoutParams(lp);
		    rigaDed.addView(textView3);
		    rigaDed.addView(textView4);
		    tableLayout.addView(rigaDed, 0);
		}
	    
	    contatoreAzioni++;
		
	}
}