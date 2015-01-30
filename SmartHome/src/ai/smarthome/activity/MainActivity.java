package ai.smarthome.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.facebook.Session;

import ai.smarthome.R;
import ai.smarthome.activity.fragmentMain.AvvioVeloceFragment;
import ai.smarthome.activity.fragmentMain.ComponentiFragment;
import ai.smarthome.activity.fragmentMain.DataFragment;
import ai.smarthome.activity.fragmentMain.InfoPersonaliFragment;
import ai.smarthome.activity.fragmentMain.MeteoFragment;
import ai.smarthome.activity.fragmentMain.SensoriFragment;
import ai.smarthome.async.AsyncConfigurazioneMeteo;
import ai.smarthome.async.AsyncMeteo;
import ai.smarthome.database.DatabaseHelper;
import ai.smarthome.database.wrapper.Componente;
import ai.smarthome.database.wrapper.Configurazione;
import ai.smarthome.database.wrapper.Oggetto;
import ai.smarthome.database.wrapper.Report;
import ai.smarthome.database.wrapper.Sensore;
import ai.smarthome.database.wrapper.Utente;
import ai.smarthome.util.Costanti;
import ai.smarthome.util.UtilConfigurazione;
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
    
	private DrawerLayout drawerLayout;
    private ListView drawerListView;
    private ActionBarDrawerToggle drawerToggle;

    private CharSequence intestazioneDrawer, intestazioneActivity;
    private String[] intestazioneOpzioni; 
    
    private SQLiteDatabase db;
    private Utente user;
    
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
        if(bundle != null) 
        	if(bundle.get(Costanti.UTENTE) != null) 
        		user = (Utente) bundle.get(Costanti.UTENTE);
        
        AsyncConfigurazioneMeteo asyncConfMeteo = new AsyncConfigurazioneMeteo(this);
        asyncConfMeteo.execute();
       
        Report.reset(db);
        Oggetto.reset(db);
        
        setContentView(R.layout.activity_main);
        setNavigationDrawer(savedInstanceState);
       
   }

    public void setNavigationDrawer(Bundle savedInstanceState) {
    	intestazioneActivity = intestazioneDrawer = getTitle();
        intestazioneOpzioni = getResources().getStringArray(R.array.opzioni_array);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerListView = (ListView) findViewById(R.id.left_drawer);
        
        intestazioneOpzioni[intestazioneOpzioni.length-1] += ": " + user.getNome() + " " + user.getCognome();
        
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
            selectItem(0);
        
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
        user.setPosizione(position);
        args.putSerializable(Costanti.UTENTE, user);
        
        if (position == 0 ) fragment = new AvvioVeloceFragment();
        if (position == 1 ) fragment = new DataFragment();
        if (position == 2 ) fragment = new MeteoFragment();
        if (position == 3 ) fragment = new ComponentiFragment();
        if (position == 4 ) fragment = new SensoriFragment();
        if (position == 5 ) fragment = new InfoPersonaliFragment();
        
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
    	
    	if (!mail.getText().toString().trim().equals(user.getMail())) {
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
			if (!vecchiaPass.getText().toString().trim().equals(user.getPassword())) {
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
			user.setMail(mail.getText().toString().trim());
			user.setMail(nuovaPass.getText().toString().trim());
			
			vecchiaPass.setText("");
		    nuovaPass.setText("");
		    nuovaPass2.setText("");
    }
    
    @SuppressLint("SimpleDateFormat")
	public void cambiaData(View view) {
    	
    	TextView dataText = (TextView) findViewById(R.id.dataText);
    	CalendarView calendarView = (CalendarView) findViewById(R.id.calendarView);
    	UtilConfigurazione.updateData(db, calendarView.getDate());
    	TimePicker timePicker = (TimePicker) findViewById(R.id.timePicker);
    	UtilConfigurazione.updateOrario(db, timePicker.getCurrentHour(), timePicker.getCurrentMinute());
    	timePicker.setCurrentHour(timePicker.getCurrentHour());
		timePicker.setCurrentMinute(timePicker.getCurrentMinute());
    	dataText.setText(new StringBuilder().append("Configurazione: ").append(UtilConfigurazione.getDataToString(calendarView.getDate())).append(" - ").append(UtilConfigurazione.getOraToString(timePicker.getCurrentHour(), timePicker.getCurrentMinute())));
    	Toast.makeText(getApplicationContext(), "Dati modificati con successo", Toast.LENGTH_SHORT).show();
    	
    }
    
    public void startSimulazione(View view) {
    	
 //   	ArrayList<String> lista = conf.toPrologRules("t0");
 //   	for(String stringa : lista) 
 //   		Log.i("PROLOG", stringa);
    	
    	Intent intent = new Intent(getApplicationContext(), SimulazioneActivity.class);
    	intent.putExtra(Costanti.UTENTE, user);
    	startActivity(intent);
    	
    }
    
    public void cambiaSensori(View view) {
    	
    	ArrayList<Sensore> lista = Sensore.getAllLista(db);
	    for(Sensore s : lista) {
	    	Switch switchSensore = (Switch)findViewById(s.getId());
	    	Sensore.update(db, s.getNome(), switchSensore.isChecked());
	    }	
	    Toast.makeText(getApplicationContext(), "Sensori modificati con successo", Toast.LENGTH_SHORT).show();
    }
    
    public void cambiaComponenti(View view) {
    	
    	ArrayList<Componente> lista = Componente.getAllLista(db);
	    for(Componente c : lista) {
	    	if (!(c.getNome().equals("Dispensa") || c.getNome().equals("Mobile") || c.getNome().equals("Frigorifero"))) {
	    		Switch switchComponente = (Switch)findViewById(c.getId());
	    		Componente.update(db, c.getNome(), switchComponente.isChecked());
	    	}
	    }
	    UtilConfigurazione.updateComponenti(db, 1);
	    Toast.makeText(getApplicationContext(), "Componenti modificati con successo", Toast.LENGTH_SHORT).show();
    }
   
    public void cambiaMeteo(View view) {
    	
    	SeekBar seekMeteo = (SeekBar)findViewById(R.id.seekMeteo);
    	SeekBar seekTempEst = (SeekBar)findViewById(R.id.seekTemperaturaEsterna);
    	SeekBar seekUmidita = (SeekBar)findViewById(R.id.seekUmidita);
    	SeekBar seekVento = (SeekBar)findViewById(R.id.seekVento);
    	
    	UtilConfigurazione.updateMeteo(db, "-", seekMeteo.getProgress(), seekTempEst.getProgress(), seekUmidita.getProgress(), seekVento.getProgress());
    	
        Toast.makeText(getApplicationContext(), "Clima modificato con successo", Toast.LENGTH_SHORT).show();
    }
    
    public void cambiaMeteoSole(View view) {
 		UtilConfigurazione.updateMeteo(db, "-", 90, 90, 90, 10);
 		Date data = new Date();
 		data.setMonth(Calendar.AUGUST);
 		UtilConfigurazione.updateData(db, data.getTime());
 		setClimaMeteoAvvioVeloce(Configurazione.getConfigurazione(db));
 	}
 	
    public void cambiaMeteoSereno(View view) {
 		UtilConfigurazione.updateMeteo(db, "-", 70, 70, 60, 30); 
 		Date data = new Date();
 		data.setMonth(Calendar.MAY);
 		UtilConfigurazione.updateData(db, data.getTime());
 		setClimaMeteoAvvioVeloce(Configurazione.getConfigurazione(db));
 	}
 		
    public void cambiaMeteoNuvole(View view) {
 		UtilConfigurazione.updateMeteo(db, "-", 40, 40, 50, 10);
 		Date data = new Date();
 		data.setMonth(Calendar.NOVEMBER);
 		UtilConfigurazione.updateData(db, data.getTime());
 		setClimaMeteoAvvioVeloce(Configurazione.getConfigurazione(db));
 	}
 	
	public void cambiaMeteoPioggia(View view) {
    	UtilConfigurazione.updateMeteo(db, "-", 20, 10, 80, 30);
 		Date data = new Date();
 		data.setMonth(Calendar.JANUARY);
 		UtilConfigurazione.updateData(db, data.getTime());
 		setClimaMeteoAvvioVeloce(Configurazione.getConfigurazione(db));
 	}
   
    public void cambiaMeteoMenu(View view) {
    	Toast.makeText(getApplicationContext(), "Caricamento meteo locale in corso...", Toast.LENGTH_SHORT).show();
    	AsyncMeteo asyncMeteoMenu = new AsyncMeteo(this);
		asyncMeteoMenu.execute();
	}
    
    public void setClimaMeteoAvvioVeloce(Configurazione meteo) {
    	
    	TextView localitaText = (TextView) findViewById(R.id.textLoc);
        TextView dataText = (TextView) findViewById(R.id.textData);
        TextView climaMeteo = (TextView) findViewById(R.id.textMeteo);
        TextView climaTempEst = (TextView) findViewById(R.id.textTemperatura);
        TextView climaUmidita = (TextView) findViewById(R.id.textUmidita);
        TextView climaVento = (TextView) findViewById(R.id.textVento);
        UtilConfigurazione.setTextViewLocalita(localitaText, meteo.getLocalita());
        UtilConfigurazione.setTextViewData(dataText, meteo.getData());
        UtilConfigurazione.setTextViewOrario(dataText, meteo.getOra(), meteo.getMinuti());
        UtilConfigurazione.setTextViewMeteo(climaMeteo, meteo.getMeteo());
        UtilConfigurazione.setTextViewTemperatura(climaTempEst, meteo.getTemperatura());
        UtilConfigurazione.setTextViewUmidita(climaUmidita, meteo.getUmidita());
        UtilConfigurazione.setTextViewVento(climaVento, meteo.getVento());
    }
    
}
