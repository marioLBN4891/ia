package ai.smarthome.activity;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import com.facebook.Session;

import ai.smarthome.R;
import ai.smarthome.activity.fragmentMain.AvvioVeloceFragment;
import ai.smarthome.activity.fragmentMain.ComponentiFragment;
import ai.smarthome.activity.fragmentMain.DataFragment;
import ai.smarthome.activity.fragmentMain.InfoPersonaliFragment;
import ai.smarthome.activity.fragmentMain.MeteoFragment;
import ai.smarthome.activity.fragmentMain.RiepilogoFragment;
import ai.smarthome.activity.fragmentMain.SensoriFragment;
import ai.smarthome.async.AsyncMeteo;
import ai.smarthome.database.DatabaseHelper;
import ai.smarthome.database.wrapper.Configurazione;
import ai.smarthome.database.wrapper.Utente;
import ai.smarthome.util.UtilMeteo;
import ai.smarthome.util.Utilities;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

/**
 * This example illustrates a common usage of the DrawerLayout widget
 * in the Android support library.
 * <p/>
 * <p>When a navigation (left) drawer is present, the host activity should detect presses of
 * the action bar's Up affordance as a signal to open and close the navigation drawer. The
 * ActionBarDrawerToggle facilitates this behavior.
 * Items within the drawer should fall into one of two categories:</p>
 * <p/>
 * <ul>
 * <li><strong>View switches</strong>. A view switch follows the same basic policies as
 * list or tab navigation in that a view switch does not create navigation history.
 * This pattern should only be used at the root activity of a task, leaving some form
 * of Up navigation active for activities further down the navigation hierarchy.</li>
 * <li><strong>Selective Up</strong>. The drawer allows the user to choose an alternate
 * parent for Up navigation. This allows a user to jump across an app's navigation
 * hierarchy at will. The application should treat this as it treats Up navigation from
 * a different task, replacing the current task stack using TaskStackBuilder or similar.
 * This is the only form of navigation drawer that should be used outside of the root
 * activity of a task.</li>
 * </ul>
 * <p/>
 * <p>Right side drawers should be used for actions, not navigation. This follows the pattern
 * established by the Action Bar that navigation should be to the left and actions to the right.
 * An action should be an operation performed on the current contents of the window,
 * for example enabling or disabling a data overlay on top of the current content.</p>
 */

@SuppressWarnings("deprecation")
public class MainActivity extends Activity {
    
	public static final String CONFIGURAZIONE = "configurazione";
	public static final String UTENTE = "utente";
	private DrawerLayout drawerLayout;
    private ListView drawerListView;
    private ActionBarDrawerToggle drawerToggle;

    private CharSequence intestazioneDrawer, intestazioneActivity;
    private String[] intestazioneOpzioni; 
    
    private SQLiteDatabase db;
    private Configurazione conf;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        db = new DatabaseHelper(this).getWritableDatabase();
        
        try {
			Class.forName("android.os.AsyncTask");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
        
        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
        	if(bundle.get(CONFIGURAZIONE) != null) 
        		conf = (Configurazione) bundle.get(CONFIGURAZIONE);
        	else 
        		conf = new Configurazione();
        	if(bundle.get(UTENTE) != null) {
                conf.setUtente((Utente) bundle.get(UTENTE));
                bundle.putSerializable(UTENTE, null);
        	}
        }
        
        setContentView(R.layout.activity_main);
        setNavigationDrawer(savedInstanceState);
   }

    public void setNavigationDrawer(Bundle savedInstanceState) {
    	intestazioneActivity = intestazioneDrawer = getTitle();
        intestazioneOpzioni = getResources().getStringArray(R.array.opzioni_array);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerListView = (ListView) findViewById(R.id.left_drawer);
        
        intestazioneOpzioni[0] = "Benvenuto\n" + conf.getUtente().getNome() + " " + conf.getUtente().getCognome();
        
        drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START); // set a custom shadow that overlays the main content when the drawer opens
        drawerListView.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, intestazioneOpzioni));  // set up the drawer's list view with items and click listener
        drawerListView.setOnItemClickListener(new DrawerItemClickListener());

        getActionBar().setDisplayHomeAsUpEnabled(true); // enable ActionBar app icon to behave as action to toggle nav drawer
        getActionBar().setHomeButtonEnabled(true);

        
        drawerToggle = new ActionBarDrawerToggle(		// ActionBarDrawerToggle ties together the the proper interactions between the sliding drawer and the action bar app icon
                this,                  					/* host Activity */
                drawerLayout,         					/* DrawerLayout object */
                R.drawable.ic_drawer,  					/* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  					/* "open drawer" description for accessibility */
                R.string.drawer_close  					/* "close drawer" description for accessibility */
                ) {
            @Override
			public void onDrawerClosed(View view) {
                getActionBar().setTitle(intestazioneActivity);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            @Override
			public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(intestazioneDrawer);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        drawerLayout.setDrawerListener(drawerToggle);

        if (savedInstanceState == null) 
            selectItem(1);
        
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Utilities.getLogoutMenuOnActivity(this, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        //boolean drawerOpen = drawerLayout.isDrawerOpen(drawerListView);
        //menu.findItem(R.id.actionInquiry).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
         // The action bar home/up action should open or close the drawer. ActionBarDrawerToggle will take care of this.
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action buttons
        switch(item.getItemId()) {
        case R.id.logout:
        	new AlertDialog.Builder(this).setIcon(R.drawable.logout).setTitle("Logout")
            .setMessage("Vuoi disconnetterti?")
            .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                	Session.getActiveSession().closeAndClearTokenInformation();
                	
                	Utente.deleteConnessioneVeloce(db);
                	Intent intent = new Intent(MainActivity.this, AccessoActivity.class);
                    startActivity(intent);
                    finish();
                }
            }).setNegativeButton("No", null).show();
        	return true;	
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    /* The click listner for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        
    	@Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position) {
        // update the main content by replacing fragments
        Fragment fragment = null;
        
        Bundle args = new Bundle();
        conf.setPosizione(position);
        args.putSerializable(CONFIGURAZIONE, conf);
        
        if (position == 0 ) return;
        if (position == 1 ) fragment = new AvvioVeloceFragment();
        if (position == 2 ) fragment = new RiepilogoFragment();
        if (position == 3 ) fragment = new DataFragment();
        if (position == 4 ) fragment = new MeteoFragment();
        if (position == 5 ) fragment = new ComponentiFragment();
        if (position == 6 ) fragment = new SensoriFragment();
        if (position == 7 ) fragment = new InfoPersonaliFragment();
        
        fragment.setArguments(args);

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

        // update selected item and title, then close the drawer
        drawerListView.setItemChecked(position, true);
        setTitle(intestazioneOpzioni[position]);
        drawerLayout.closeDrawer(drawerListView);
    }

    @Override
    public void setTitle(CharSequence intestazioneActivity) {
    	this.intestazioneActivity = intestazioneActivity;
        getActionBar().setTitle(intestazioneActivity);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        Utilities.chiudiApplicazione(this);
    }
    
    public void cambiaInfoPersonali(View view) {
    	
    	EditText mail = (EditText) findViewById(R.id.mailEditText); 
    	EditText vecchiaPass = (EditText) findViewById(R.id.vecchiaPasswordEditText); 
    	EditText nuovaPass = (EditText) findViewById(R.id.nuovaPasswordEditText); 
    	EditText nuovaPass2 = (EditText) findViewById(R.id.password2EditText);
    	
    	if (!mail.getText().toString().trim().equals(conf.getUtente().getMail())) {
				if (mail.getText().toString().trim().equals("")) {
					Toast.makeText(getApplicationContext(), "Email è obbligatorio", Toast.LENGTH_SHORT).show();
					return;
				}
				if (!Utilities.emailValida(mail.getText().toString().trim())) {
					Toast.makeText(getApplicationContext(), "Email non valida", Toast.LENGTH_SHORT).show();
					return;
				}
				if (Utente .isMailRegistered(db, mail.getText().toString().trim())) {
					Toast.makeText(getApplicationContext(), "Email già registrata", Toast.LENGTH_SHORT).show();
					return;
				}
			}
			
			if (vecchiaPass.getText().toString().trim().equals("") || vecchiaPass.getText().toString().trim().length() < 5 || Utilities.checkSpace(vecchiaPass.getText().toString())) {
				Toast.makeText(getApplicationContext(), "Password attuale è un campo obbligatorio", Toast.LENGTH_SHORT).show();
				return;
			}
			if (!vecchiaPass.getText().toString().trim().equals(conf.getUtente().getPassword())) {
				Toast.makeText(getApplicationContext(), "Password attuale non valida", Toast.LENGTH_SHORT).show();
				return;
			}
			if (nuovaPass.getText().toString().trim().equals("") || nuovaPass.getText().toString().trim().length() < 5 || Utilities.checkSpace(nuovaPass.getText().toString())) {
				Toast.makeText(getApplicationContext(), "Nuova Password è un campo obbligatorio di almeno 5 caratteri  non vuoti", Toast.LENGTH_SHORT).show();
				return;
			}
			if (nuovaPass2.getText().toString().trim().equals("") || nuovaPass2.getText().toString().trim().length() < 5 || Utilities.checkSpace(nuovaPass2.getText().toString())) {
				Toast.makeText(getApplicationContext(), "Inserire nuovamente la nuova password", Toast.LENGTH_SHORT).show();
				return;
			}
			if (!nuovaPass2.getText().toString().equals(nuovaPass.getText().toString())) {
				Toast.makeText(getApplicationContext(), "Password digitata non corretta", Toast.LENGTH_SHORT).show();
				return;
			}
			
			Utente.updateUtente(db, mail.getText().toString().trim(), nuovaPass.getText().toString().trim());
			
			Toast.makeText(getApplicationContext(), "Modifiche avvenute con successo", Toast.LENGTH_SHORT).show();
			conf.getUtente().setMail(mail.getText().toString().trim());
			conf.getUtente().setMail(nuovaPass.getText().toString().trim());
			
			vecchiaPass.setText("");
		    nuovaPass.setText("");
		    nuovaPass2.setText("");
    }
    
    @SuppressLint("SimpleDateFormat")
	public void cambiaData(View view) {
    	
    	TextView dataText = (TextView) findViewById(R.id.dataText);
    	CalendarView calendarView = (CalendarView) findViewById(R.id.calendarView);
    	conf.setDataMilliTime(calendarView.getDate());
    	TimePicker timePicker = (TimePicker) findViewById(R.id.timePicker);
    	conf.setHour(timePicker.getCurrentHour());
    	conf.setMinute(timePicker.getCurrentMinute());
    	timePicker.setCurrentHour(conf.getHour());
		timePicker.setCurrentMinute(conf.getMinute());
    	dataText.setText(new StringBuilder().append("Configurazione: ").append(conf.getDataToString()).append(conf.getOraToString()));
    	Toast.makeText(getApplicationContext(), "Dati modificati con successo", Toast.LENGTH_SHORT).show();
    	
    }
    
    public void startSimulazione(View view) {
    	
 //   	ArrayList<String> lista = conf.toPrologRules("t0");
 //   	for(String stringa : lista) 
 //   		Log.i("PROLOG", stringa);
    	
    	Intent intent = new Intent(getApplicationContext(), SimulazioneActivity.class);
    	intent.putExtra(CONFIGURAZIONE, conf);
    	startActivity(intent);
    }
    
    public void cambiaSensori(View view) {
    	
    	Switch temperatura = (Switch)findViewById(R.id.sens_temperatura);
        Switch umidita = (Switch)findViewById(R.id.sens_umidita);
        Switch vento = (Switch)findViewById(R.id.sens_vento);
        Switch presenza = (Switch)findViewById(R.id.sens_presenza);
        Switch sonoro = (Switch)findViewById(R.id.sens_sonoro);
        
        conf.setSensoreTemperatura(temperatura.isChecked());
        conf.setSensoreUmidita(umidita.isChecked());
        conf.setSensoreVento(vento.isChecked());
        conf.setSensorePresenza(presenza.isChecked());
        conf.setSensoreSonoro(sonoro.isChecked());
        
    	Toast.makeText(getApplicationContext(), "Sensori modificati con successo", Toast.LENGTH_SHORT).show();
    }
    
    public void cambiaComponenti(View view) {
    	
    	Switch televisione = (Switch)findViewById(R.id.televisione);
        Switch radio = (Switch)findViewById(R.id.radio);
        Switch condizionatore = (Switch)findViewById(R.id.condizionatore);
        Switch balcone = (Switch)findViewById(R.id.balcone);
        Switch macchinaCaffe = (Switch)findViewById(R.id.macchinaCaffe);
        Switch illuminazione = (Switch)findViewById(R.id.illuminazione);
        
        conf.setComponenteTelevisione(televisione.isChecked());
        conf.setComponenteRadio(radio.isChecked());
        conf.setComponenteCondizionatore(condizionatore.isChecked());
        conf.setComponenteBalcone(balcone.isChecked());
        conf.setComponenteMacchinaCaffe(macchinaCaffe.isChecked());
        conf.setComponenteIlluminazione(illuminazione.isChecked());
        
    	Toast.makeText(getApplicationContext(), "Componenti modificati con successo", Toast.LENGTH_SHORT).show();
    }
   
    public void cambiaMeteo(View view) {
    	
    	SeekBar seekMeteo = (SeekBar)findViewById(R.id.seekMeteo);
    	SeekBar seekTempEst = (SeekBar)findViewById(R.id.seekTemperaturaEsterna);
    	SeekBar seekUmidita = (SeekBar)findViewById(R.id.seekUmidita);
    	SeekBar seekVento = (SeekBar)findViewById(R.id.seekVento);
    	
    	conf.setClimaMeteo(seekMeteo.getProgress());
    	conf.setClimaTemperaturaEsterna(seekTempEst.getProgress());
        conf.setClimaUmidita(seekUmidita.getProgress());
        conf.setClimaVento(seekVento.getProgress());
        
        Toast.makeText(getApplicationContext(), "Clima modificato con successo", Toast.LENGTH_SHORT).show();
    }
    
    public void cambiaMeteoSole(View view) {
 		UtilMeteo.setConfMeteo(conf, 90, 90, 90, 10);
 		Date data = new Date();
 		data.setMonth(Calendar.AUGUST);
 		
 		conf.setDataMilliTime(data.getTime());
 		setClimaMeteoAvvioVeloce();
 	}
 	
    
    public void cambiaMeteoSereno(View view) {
 		UtilMeteo.setConfMeteo(conf, 70, 70, 60, 30); 
 		Date data = new Date();
 		data.setMonth(Calendar.MAY);
 		conf.setDataMilliTime(data.getTime());
 		setClimaMeteoAvvioVeloce();
 	}
 		
    
    public void cambiaMeteoNuvole(View view) {
 		UtilMeteo.setConfMeteo(conf, 40, 40, 50, 10);
 		Date data = new Date();
 		data.setMonth(Calendar.NOVEMBER);
 		conf.setDataMilliTime(data.getTime());
 		setClimaMeteoAvvioVeloce();
 	}
 	
    
	public void cambiaMeteoPioggia(View view) {
    	UtilMeteo.setConfMeteo(conf, 20, 10, 80, 30);
 		Date data = new Date();
 		data.setMonth(Calendar.JANUARY);
 		conf.setDataMilliTime(data.getTime());
 		setClimaMeteoAvvioVeloce();
 	}
    
	 
    public void cambiaMeteoAttuale(View view) {
    	Toast.makeText(getApplicationContext(), "Caricamento meteo in corso...", Toast.LENGTH_SHORT).show();
    	AsyncMeteo asyncMeteo = new AsyncMeteo(this);
		asyncMeteo.execute();
	}
    
    
    public void setClimaMeteoAvvioVeloce(Map<String, Integer> parametri) {
    	
    	UtilMeteo.setConfMeteo(conf, parametri.get("meteo"), parametri.get("tempEst"), parametri.get("umidita"), parametri.get("vento"));
		Date data = new Date();
		conf.setDataMilliTime(data.getTime());
		
    	TextView dataText = (TextView) findViewById(R.id.dataText);
        dataText.setText(conf.getDataToString());
        
        TextView orarioText = (TextView) findViewById(R.id.orarioText);
        orarioText.setText(conf.getOraToString());
        
        TextView climaMeteo = (TextView) findViewById(R.id.climaMeteo);
        TextView climaTempEst = (TextView) findViewById(R.id.climaTempEst);
        TextView climaUmidita = (TextView) findViewById(R.id.climaUmidita);
        TextView climaVento = (TextView) findViewById(R.id.climaVento);
        UtilMeteo.setTextViewMeteo(climaMeteo, conf.getClimaMeteo());
        UtilMeteo.setTextViewTempEst(climaTempEst, conf.getClimaTemperaturaEsterna());
        UtilMeteo.setTextViewUmidita(climaUmidita, conf.getClimaUmidita());
        UtilMeteo.setTextViewVento(climaVento, conf.getClimaVento());
    }
    
    public void setClimaMeteoAvvioVeloce() {
    	
    	TextView dataText = (TextView) findViewById(R.id.dataText);
        dataText.setText(conf.getDataToString());
        
        TextView orarioText = (TextView) findViewById(R.id.orarioText);
        orarioText.setText(conf.getOraToString());
        
        TextView climaMeteo = (TextView) findViewById(R.id.climaMeteo);
        TextView climaTempEst = (TextView) findViewById(R.id.climaTempEst);
        TextView climaUmidita = (TextView) findViewById(R.id.climaUmidita);
        TextView climaVento = (TextView) findViewById(R.id.climaVento);
        UtilMeteo.setTextViewMeteo(climaMeteo, conf.getClimaMeteo());
        UtilMeteo.setTextViewTempEst(climaTempEst, conf.getClimaTemperaturaEsterna());
        UtilMeteo.setTextViewUmidita(climaUmidita, conf.getClimaUmidita());
        UtilMeteo.setTextViewVento(climaVento, conf.getClimaVento());
    }
    
}
