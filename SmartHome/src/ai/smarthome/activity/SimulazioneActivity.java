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
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.ToggleButton;

public class SimulazioneActivity extends Activity  {

	private Utente user;
	private SQLiteDatabase db;
	private Button apriButton, chiudiButton, accendiButton, spegniButton, prendiButton, riponiButton, negaButton;
	private ToggleButton presenzaButton;
	private String timestamp = UtilConfigurazione.setTimestamp();
	
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
        
        
        if (!Prolog.eseguiConfigurazione(db, timestamp, user, null)) {
    		alertServerError();
    	}
        
        apriButton = (Button) findViewById(R.id.buttonApri);
        chiudiButton = (Button) findViewById(R.id.buttonChiudi);
        accendiButton = (Button) findViewById(R.id.buttonAccendi);
        spegniButton = (Button) findViewById(R.id.buttonSpegni);
        prendiButton = (Button) findViewById(R.id.buttonPrendi);
        riponiButton = (Button) findViewById(R.id.buttonRiporre);
        negaButton = (Button) findViewById(R.id.buttonNega);
        presenzaButton = (ToggleButton) findViewById(R.id.togglebuttonPresenza);
        
        if (!user.isPresente()) {
        	apriButton.setEnabled(false);
            chiudiButton.setEnabled(false);
            accendiButton.setEnabled(false);
            spegniButton.setEnabled(false);
            prendiButton.setEnabled(false);
            riponiButton.setEnabled(false);
            negaButton.setEnabled(false);
            presenzaButton.setChecked(false);
        }
        else {
        	apriButton.setEnabled(true);
        	chiudiButton.setEnabled(Componente.checkComponente(Componente.APERTO_CHIUSO, 1, db));
        	accendiButton.setEnabled(Componente.checkComponente(Componente.ACCESO_SPENTO, 0, db));
        	spegniButton.setEnabled(Componente.checkComponente(Componente.ACCESO_SPENTO, 1, db));
        	prendiButton.setEnabled(Oggetto.checkOggetto(Oggetto.CUCINA, 0, db));
            riponiButton.setEnabled(false);
        	negaButton.setEnabled(Report.checkListaDedottiNuovi(db));
        	presenzaButton.setChecked(true);
        }
        refreshListaFatti();
    
        presenzaButton.setOnClickListener(new OnClickListener() {
        	 
    		@Override
    		public void onClick(View v) {
    			user.setPresente(user.isPresente() ? false : true);
    			
    			if (user.isPresente()) 
    				domandaPresenza("Da dove arrivi?");
    			else
    				domandaPresenza("Dove vai?");
			}
    	});

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
			intent.putExtra(Costanti.UTENTE, user);
			startActivity(intent);
			finish();
            
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
			 						String fatto = "action_pick_"+Oggetto.getProlog(db, checkedItem1)+"("+timestamp+")";
			 						Report report = new Report("Hai preso "+checkedItem1, checkedItem1, 1, Prolog.creaStringaFatto(fatto, "1.0"), 0, 1, 1, 0);
		 							if (Prolog.eseguiConfigurazione(db, timestamp, user, report)) {
		 								Oggetto.cambiaStato(db, checkedItem1, 1);
			 							riponiButton.setEnabled(true);
			 							chiudiButton.setEnabled(Componente.checkComponente(Componente.APERTO_CHIUSO, 1, db));
			 							spegniButton.setEnabled(true);
			 							accendiButton.setEnabled(Componente.checkComponente(Componente.ACCESO_SPENTO, 0, db));
			 							refreshListaFatti();
			 						}
			 						else {
			 							alertServerError();
			 						}
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
						String fatto = "action_"+Componente.getProlog(db, checkedItem)+"_open("+timestamp+")";
						Report report = new Report("Hai aperto "+checkedItem, checkedItem, 1, Prolog.creaStringaFatto(fatto, "1.0"), 0, 1, 1, 0);
						if (Prolog.eseguiConfigurazione(db, timestamp, user, report)) {
							//Report.cambiaStatoComponente(db, checkedItem);
							Componente.cambiaStato(db, checkedItem, 1);
							chiudiButton.setEnabled(Componente.checkComponente(Componente.APERTO_CHIUSO, 1, db));
							refreshListaFatti();
						}
						else {
							alertServerError();
						}
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
					String fatto = "action_"+Componente.getProlog(db, checkedItem)+"_close("+timestamp+")";
					Report report = new Report("Hai chiuso "+checkedItem, checkedItem, 0, Prolog.creaStringaFatto(fatto, "1.0"), 0, 1, 1, 0);
					if (Prolog.eseguiConfigurazione(db, timestamp, user, report)) {
						Componente.cambiaStato(db, checkedItem, 0);
						//Report.cambiaStatoComponente(db, checkedItem);
						apriButton.setEnabled(true);
						chiudiButton.setEnabled(Componente.checkComponente(Componente.APERTO_CHIUSO, 1, db));
						refreshListaFatti();
					}
					else {
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
	
	public void exeAccendi(View view) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		final AlertDialog.Builder alertDialogBuilder2 = new AlertDialog.Builder(this);
		alertDialogBuilder
 			.setTitle("Accendi")
 			.setSingleChoiceItems(Componente.getListaCharSequence(Componente.ACCESO_SPENTO, 0, db), 0, null)
			.setPositiveButton("Conferma",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					ListView lw = ((AlertDialog)dialog).getListView();
					final String checkedItem = (String) lw.getAdapter().getItem(lw.getCheckedItemPosition());
					if(checkedItem.equals("Condizionatore")) {
	        			final CharSequence[] listaStati = {"potenza bassa", "potenza media", "potenza massima", "deumidificatore"};
	        			final CharSequence[] listaStatiProlog = {"low", "middle", "max", "deumidifier"};
	        			alertDialogBuilder2
	        			.setTitle("Stato "+checkedItem+":")
	        		   	.setSingleChoiceItems(listaStati, 0, null)
	        		    .setPositiveButton("Conferma",new DialogInterface.OnClickListener() {
	        		    	public void onClick(DialogInterface dialog,int id) {
	        		    		String fatto = "action_"+Componente.getProlog(db, checkedItem)+"_"+listaStatiProlog[id]+"("+timestamp+")";
	        					Report report = new Report("Hai acceso "+checkedItem, checkedItem, id, Prolog.creaStringaFatto(fatto, "1.0"), 0, 1, 1, 0);
	        					if (Prolog.eseguiConfigurazione(db, timestamp, user, report)) {
	        						Componente.cambiaStato(db, checkedItem, id);
	        						//Report.cambiaStatoComponente(db, checkedItem);
	        						spegniButton.setEnabled(true);
	        						accendiButton.setEnabled(Componente.checkComponente(Componente.ACCESO_SPENTO, 0, db));
	        						refreshListaFatti();
	        						return;
	        					}
	        					else {
	        						alertServerError();
	        						return;
	        					}
	        		    	}
	        		    })
	        		    .setNegativeButton("Annulla",new DialogInterface.OnClickListener() {
	        		    	public void onClick(DialogInterface dialog,int id) {
	        		    		dialog.cancel();
	        		    	}
	        		    }).show();
	        		}
					else
	        		if(checkedItem.equals("Termosifone")) {
	        			final CharSequence[] listaStati = {"potenza bassa", "potenza media", "potenza massima"};
	        			final CharSequence[] listaStatiProlog = {"low", "middle", "max"};
	        			alertDialogBuilder2
	        			.setTitle("Stato "+checkedItem+":")
	        		   	.setSingleChoiceItems(listaStati, 0, null)
	        		    .setPositiveButton("Conferma",new DialogInterface.OnClickListener() {
	        		    	public void onClick(DialogInterface dialog,int id) {
	        		    		String fatto = "action_"+Componente.getProlog(db, checkedItem)+"_"+listaStatiProlog[id]+"("+timestamp+")";
	        					Report report = new Report("Hai acceso "+checkedItem, checkedItem, id, Prolog.creaStringaFatto(fatto, "1.0"), 0, 1, 1, 0);
	        					if (Prolog.eseguiConfigurazione(db, timestamp, user, report)) {
	        						Componente.cambiaStato(db, checkedItem, id);
	        						//Report.cambiaStatoComponente(db, checkedItem);
	        						spegniButton.setEnabled(true);
	        						accendiButton.setEnabled(Componente.checkComponente(Componente.ACCESO_SPENTO, 0, db));
	        						refreshListaFatti();
	        						return;
	        					}
	        					else {
	        						alertServerError();
	        						return;
	        					}
	        		    	}
	        		    })
	        		    .setNegativeButton("Annulla",new DialogInterface.OnClickListener() {
	        		    	public void onClick(DialogInterface dialog,int id) {
	        		    		dialog.cancel();
	        		    	}
	        		    }).show();
	        		}
	        		else
	        		if(checkedItem.equals("Forno a microonde")) {
	        			final CharSequence[] listaStati = {"riscaldamento", "scongelamento"};	 
	        			final CharSequence[] listaStatiProlog = {"heat", "defrost"};
	        			alertDialogBuilder2
	        			.setTitle("Stato "+checkedItem+":")
	        		   	.setSingleChoiceItems(listaStati, 0, null)
	        		    .setPositiveButton("Conferma",new DialogInterface.OnClickListener() {
	        		    	public void onClick(DialogInterface dialog,int id) {
	        		    		String fatto = "action_"+Componente.getProlog(db, checkedItem)+"_"+listaStatiProlog[id]+"("+timestamp+")";
	        					Report report = new Report("Hai acceso "+checkedItem, checkedItem, id, Prolog.creaStringaFatto(fatto, "1.0"), 0, 1, 1, 0);
	        					if (Prolog.eseguiConfigurazione(db, timestamp, user, report)) {
	        						Componente.cambiaStato(db, checkedItem, id);
	        						//Report.cambiaStatoComponente(db, checkedItem);
	        						spegniButton.setEnabled(true);
	        						accendiButton.setEnabled(Componente.checkComponente(Componente.ACCESO_SPENTO, 0, db));
	        						refreshListaFatti();
	        						return;
	        					}
	        					else {
	        						alertServerError();
	        						return;
	        					}
	        		    	}
	        		    })
	        		    .setNegativeButton("Annulla",new DialogInterface.OnClickListener() {
	        		    	public void onClick(DialogInterface dialog,int id) {
	        		    		dialog.cancel();
	        		    	}
	        		    }).show();
	        		}
	        		else
	        		if(checkedItem.equals("Illuminazione")) {
	        			final CharSequence[] listaStati = {"intensità bassa", "intensità media", "intensità alta"};	 
	        			final CharSequence[] listaStatiProlog = {"100", "200", "300"};
	        			alertDialogBuilder2
	        			.setTitle("Stato "+checkedItem+":")
	        		   	.setSingleChoiceItems(listaStati, 0, null)
	        		    .setPositiveButton("Conferma",new DialogInterface.OnClickListener() {
	        		    	public void onClick(DialogInterface dialog,int id) {
	        		    		String fatto = "action_"+Componente.getProlog(db, checkedItem)+"_"+listaStatiProlog[id]+"("+timestamp+")";
	        					Report report = new Report("Hai acceso "+checkedItem, checkedItem, id, Prolog.creaStringaFatto(fatto, "1.0"), 0, 1, 1, 0);
	        					if (Prolog.eseguiConfigurazione(db, timestamp, user, report)) {
	        						Componente.cambiaStato(db, checkedItem, id);
	        						//Report.cambiaStatoComponente(db, checkedItem);
	        						spegniButton.setEnabled(true);
	        						accendiButton.setEnabled(Componente.checkComponente(Componente.ACCESO_SPENTO, 0, db));
	        						refreshListaFatti();
	        						return;
	        					}
	        					else {
	        						alertServerError();
	        						return;
	        					}
	        		    	}
	        		    })
	        		    .setNegativeButton("Annulla",new DialogInterface.OnClickListener() {
	        		    	public void onClick(DialogInterface dialog,int id) {
	        		    		dialog.cancel();
	        		    	}
	        		    }).show();
	        		}
	        		else {
	        			String fatto = "action_"+Componente.getProlog(db, checkedItem)+"_on("+timestamp+")";
	        			Report report = new Report("Hai acceso "+checkedItem, checkedItem, 1, Prolog.creaStringaFatto(fatto, "1.0"), 0, 1, 1, 0);
	        			if (Prolog.eseguiConfigurazione(db, timestamp, user, report)) {
	        				Componente.cambiaStato(db, checkedItem, 1);
	        				//Report.cambiaStatoComponente(db, checkedItem);
	        				spegniButton.setEnabled(true);
	        				accendiButton.setEnabled(Componente.checkComponente(Componente.ACCESO_SPENTO, 0, db));
	        				refreshListaFatti();
	        			}
	        			else {
	        				alertServerError();
	        			}
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
	
	public void exeSpegni(View view) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder
 			.setTitle("Spegni")
 			.setSingleChoiceItems(Componente.getListaCharSequence(Componente.ACCESO_SPENTO, 1, db), 0, null)
			.setPositiveButton("Conferma",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					ListView lw = ((AlertDialog)dialog).getListView();
					String checkedItem = (String) lw.getAdapter().getItem(lw.getCheckedItemPosition());
					String fatto = "action_"+Componente.getProlog(db, checkedItem)+"_off("+timestamp+")";
					Report report = new Report("Hai spento "+checkedItem, checkedItem, 0, Prolog.creaStringaFatto(fatto, "1.0"), 0, 1, 1, 0);
					if (Prolog.eseguiConfigurazione(db, timestamp, user, report)) {
						Componente.cambiaStato(db, checkedItem, 0);
						//Report.cambiaStatoComponente(db, checkedItem);
						accendiButton.setEnabled(true);
						spegniButton.setEnabled(Componente.checkComponente(Componente.ACCESO_SPENTO, 1, db));
						refreshListaFatti();
					}
					else {
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
	
	public void exePrendi(View view) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder
 			.setTitle("Prendi")
 			.setSingleChoiceItems(Oggetto.getListaCharSequence(Oggetto.CUCINA, 0, db), 0, null)
			.setPositiveButton("Conferma",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					ListView lw = ((AlertDialog)dialog).getListView();
					String checkedItem = (String) lw.getAdapter().getItem(lw.getCheckedItemPosition());
					String fatto = "action_pick_"+Oggetto.getProlog(db, checkedItem)+"("+timestamp+")";
					Report report = new Report("Hai preso "+checkedItem, checkedItem, 1, Prolog.creaStringaFatto(fatto, "1.0"), 0, 1, 1, 0);
					if (Prolog.eseguiConfigurazione(db, timestamp, user, report)) {
						Oggetto.cambiaStato(db, checkedItem, 0);
						riponiButton.setEnabled(true);
						chiudiButton.setEnabled(Componente.checkComponente(Componente.APERTO_CHIUSO, 1, db));
						spegniButton.setEnabled(true);
						accendiButton.setEnabled(Componente.checkComponente(Componente.ACCESO_SPENTO, 0, db));
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
	
	public void exeRiponi(View view) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder
 			.setTitle("Riporre")
 			.setSingleChoiceItems(Oggetto.getListaCharSequence(null, 1, db), 0, null)
			.setPositiveButton("Conferma",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					ListView lw = ((AlertDialog)dialog).getListView();
					String checkedItem = (String) lw.getAdapter().getItem(lw.getCheckedItemPosition());
					String fatto = "action_put_away_"+Oggetto.getProlog(db, checkedItem)+"("+timestamp+")";
					Report report = new Report("Hai riposto "+checkedItem, checkedItem, 0, Prolog.creaStringaFatto(fatto, "1.0"), 0, 1, 1, 0);
					if (Prolog.eseguiConfigurazione(db, timestamp, user, report)) {
						Oggetto.cambiaStato(db, checkedItem, 0);
						riponiButton.setEnabled(true);
						chiudiButton.setEnabled(Componente.checkComponente(Componente.APERTO_CHIUSO, 1, db));
						spegniButton.setEnabled(true);
						accendiButton.setEnabled(Componente.checkComponente(Componente.ACCESO_SPENTO, 0, db));
					}
					else {
						Oggetto.cambiaStato(db, checkedItem, 1);
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
	
	public void exeNega(View view) {
		final CharSequence myList[] = Report.getListaDedottiNuoviCharSequenceV2(db);
		final ArrayList<Integer> selList = new ArrayList<Integer>();
		boolean bl[] = new boolean[myList.length];
		
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder
 			.setTitle("Nega azioni C@SA")
 			.setMultiChoiceItems(myList, bl, new OnMultiChoiceClickListener() {
 				@Override
 				public void onClick(DialogInterface arg0, int arg1, boolean arg2) {
 					if(arg2) 
 						selList.add(arg1);
 					else 
 						if (selList.contains(arg1))
 							selList.remove(Integer.valueOf(arg1));
 				} 
 			  }) 		
			.setPositiveButton("Nega",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					ArrayList<Report> listaReport = new ArrayList<Report>();
					for (int i=0; i < selList.size(); i++) {
						Report report = Report.getProlog(db, String.valueOf(myList[selList.get(i)]));
						String fatto = "retract("+report.getProlog()+")";
						Report reportNuovo = new Report("Non confermi: "+report.getAzione().replace("C@SA: ", ""), report.getItem(), report.getStato() == 0 ? 1:0, fatto.trim(), 0, 1, 1,0);
						listaReport.add(reportNuovo);
					}	
					if (Prolog.retract(db, timestamp, user, listaReport)) {
						for(Report r: listaReport) {
							Componente.cambiaStato(db, r.getItem(), r.getStato());
							Report.cambiaStatoComponente(db, r.getItem(), r.getStato());
						}
						chiudiButton.setEnabled(Componente.checkComponente(Componente.APERTO_CHIUSO, 1, db));
	    	        	accendiButton.setEnabled(Componente.checkComponente(Componente.ACCESO_SPENTO, 0, db));
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
	
	private void refreshListaFatti() {
	
		TableLayout tableLayout = (TableLayout) findViewById(R.id.tableSVlayout);
	    TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
	    lp.setMargins(10, 2, 10, 3);
	    
	    ArrayList<Report> lista = Report.getLista(db);
	    
	    tableLayout.removeAllViews();
	    int indice = 1;
	    for (Report r: lista) {
	    	TextView textView1 = new TextView(this);
	    	TextView textView2 = new TextView(this);
		    textView1.setText(String.valueOf(indice++));
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
	    
	    apriButton.setEnabled(true);
    	chiudiButton.setEnabled(Componente.checkComponente(Componente.APERTO_CHIUSO, 1, db));
    	accendiButton.setEnabled(Componente.checkComponente(Componente.ACCESO_SPENTO, 0, db));
    	spegniButton.setEnabled(Componente.checkComponente(Componente.ACCESO_SPENTO, 1, db));
    	prendiButton.setEnabled(Oggetto.checkOggetto(Oggetto.CUCINA, 0, db));
        riponiButton.setEnabled(Oggetto.checkOggetto(Oggetto.CUCINA, 1, db));
    	negaButton.setEnabled(Report.checkListaDedottiNuovi(db));
    	presenzaButton.setChecked(true);
    	
	}
	
	private void alertServerError() {
		new AlertDialog.Builder(this)
		.setIcon(R.drawable.attenzione).setTitle("Errore")
		.setMessage("Impossibile contattare il Server.")
		.setPositiveButton("Indietro",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				Intent intent = new Intent(SimulazioneActivity.this, MainActivity.class);
            	intent.putExtra(Costanti.UTENTE, user);
                startActivity(intent);
                finish();
			}
		}).show();
	}
	
	private void domandaPresenza(String messaggio) {
		new AlertDialog.Builder(this)
		.setTitle("Domanda")
		.setMessage(messaggio)
		.setPositiveButton("Altra camera",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				user.setEsterno(false);
				if (Prolog.eseguiConfigurazione(db, timestamp, user, null)) {
					refreshListaFatti();
					if (!user.isPresente()) {
	    	        	apriButton.setEnabled(false);
	    	            chiudiButton.setEnabled(false);
	    	            accendiButton.setEnabled(false);
	    	            spegniButton.setEnabled(false);
	    	            prendiButton.setEnabled(false);
	    	            riponiButton.setEnabled(false);
	    	            negaButton.setEnabled(false);
	    	            presenzaButton.setChecked(false);
	    	        }
	    	        else {
	    	        	apriButton.setEnabled(true);
	    	        	chiudiButton.setEnabled(Componente.checkComponente(Componente.APERTO_CHIUSO, 1, db));
	    	        	accendiButton.setEnabled(Componente.checkComponente(Componente.ACCESO_SPENTO, 0, db));
	    	        	spegniButton.setEnabled(Componente.checkComponente(Componente.ACCESO_SPENTO, 1, db));
	    	        	prendiButton.setEnabled(Oggetto.checkOggetto(Oggetto.CUCINA, 0, db));
	    	            riponiButton.setEnabled(Oggetto.checkOggetto(Oggetto.CUCINA, 1, db));
	    	        	negaButton.setEnabled(Report.checkListaDedottiNuovi(db));
	    	        	presenzaButton.setChecked(true);
	    	        }
	    		}
				else
					alertServerError();
			}
		})
		.setNegativeButton("Esterno",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				user.setEsterno(true);
				if (Prolog.eseguiConfigurazione(db, timestamp, user, null)) {
					refreshListaFatti();
					if (!user.isPresente()) {
	    	        	apriButton.setEnabled(false);
	    	            chiudiButton.setEnabled(false);
	    	            accendiButton.setEnabled(false);
	    	            spegniButton.setEnabled(false);
	    	            prendiButton.setEnabled(false);
	    	            riponiButton.setEnabled(false);
	    	            negaButton.setEnabled(false);
	    	            presenzaButton.setChecked(false);
	    	        }
	    	        else {
	    	        	apriButton.setEnabled(true);
	    	        	chiudiButton.setEnabled(Componente.checkComponente(Componente.APERTO_CHIUSO, 1, db));
	    	        	accendiButton.setEnabled(Componente.checkComponente(Componente.ACCESO_SPENTO, 0, db));
	    	        	spegniButton.setEnabled(Componente.checkComponente(Componente.ACCESO_SPENTO, 1, db));
	    	        	prendiButton.setEnabled(Oggetto.checkOggetto(Oggetto.CUCINA, 0, db));
	    	            riponiButton.setEnabled(Oggetto.checkOggetto(Oggetto.CUCINA, 1, db));
	    	        	negaButton.setEnabled(Report.checkListaDedottiNuovi(db));
	    	        	presenzaButton.setChecked(true);
	    	        }
	    		}
				else
					alertServerError();
			}
		}).show();
	}
	
	private void creaDialogOpen(final String checkedItem, CharSequence[] listaStati, final CharSequence[] listaStatiProlog) {
    	new AlertDialog.Builder(this)
		.setIcon(R.drawable.attenzione)
	    .setTitle("Stato "+checkedItem+":")
	   	.setSingleChoiceItems(listaStati, 0, null)
	    .setPositiveButton("Conferma",new DialogInterface.OnClickListener() {
	    	public void onClick(DialogInterface dialog,int id) {
	    		String fatto = "action_"+Componente.getProlog(db, checkedItem)+"_"+listaStatiProlog[id]+"("+timestamp+")";
				Report report = new Report("Hai acceso "+checkedItem, checkedItem, id, Prolog.creaStringaFatto(fatto, "1.0"), 0, 1, 1, 0);
				if (Prolog.eseguiConfigurazione(db, timestamp, user, report)) {
					Componente.cambiaStato(db, checkedItem, id);
					//Report.cambiaStatoComponente(db, checkedItem);
					spegniButton.setEnabled(true);
					accendiButton.setEnabled(Componente.checkComponente(Componente.ACCESO_SPENTO, 0, db));
					refreshListaFatti();
				}
				else {
					alertServerError();
				}
	    	}
	    })
	    .setNegativeButton("Annulla",new DialogInterface.OnClickListener() {
	    	public void onClick(DialogInterface dialog,int id) {
	    		dialog.cancel();
	    	}
	    }).show();
    }
	
}