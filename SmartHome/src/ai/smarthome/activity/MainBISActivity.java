package ai.smarthome.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import ai.smarthome.R;
import ai.smarthome.activity.fragmentMain.AvvioVeloceFragment;
import ai.smarthome.activity.fragmentMain.ComponentiFragment;
import ai.smarthome.activity.fragmentMain.DataFragment;
import ai.smarthome.activity.fragmentMain.ImpostazioniFragment;
import ai.smarthome.activity.fragmentMain.InfoUtenteFragment;
import ai.smarthome.activity.fragmentMain.MeteoFragment;
import ai.smarthome.activity.fragmentMain.SensoriFragment;
import ai.smarthome.database.DatabaseHelper;
import ai.smarthome.database.wrapper.Componente;
import ai.smarthome.database.wrapper.Configurazione;
import ai.smarthome.database.wrapper.Impostazione;
import ai.smarthome.database.wrapper.Oggetto;
import ai.smarthome.database.wrapper.Report;
import ai.smarthome.database.wrapper.Sensore;
import ai.smarthome.database.wrapper.Utente;
import ai.smarthome.util.Costanti;
import ai.smarthome.util.GPSTracker;
import ai.smarthome.util.UtilConfigurazione;
import ai.smarthome.util.Utilities;
import ai.smarthome.util.rest.Rest;
import ai.smarthome.util.xmlrpc.XMLRPC;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


@SuppressWarnings("deprecation")
public class MainBISActivity extends Activity {
    
	private DrawerLayout drawerLayout;
    private ListView drawerListView;
    private ActionBarDrawerToggle drawerToggle;

    private CharSequence intestazioneDrawer, intestazioneActivity;
    private String[] intestazioneOpzioni; 
   // private AsyncPosition asyncPosition= new AsyncPosition(this);
    public SQLiteDatabase db;
    private Utente user;
    private static final String W7_PACK = "org.main.wseven";
    public static final String ACTION_FROM_SERVICE = "com.example.communicationw7";
	
    private String lat,lon,sc,gmtTime;
    
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			lat = intent.getExtras().getString("latitude");
			lon = intent.getExtras().getString("longitude");
			sc = intent.getExtras().getString("session").equalsIgnoreCase("") ? "Anonymous" : intent.getExtras().getString("session");
			gmtTime = intent.getExtras().getString("gmtTime");
		}
	};
	
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
        
       
       // asyncPosition.execute();
       
        Componente.reset(db);
        Oggetto.reset(db);
        
        setContentView(R.layout.activity_main);
        setNavigationDrawer(savedInstanceState);
        
        if(appInstalledOrNot(W7_PACK)) {
            System.out.println("W7 is installed");
        }
        else {
            System.out.println("W7 not installed");
            Toast.makeText(getApplicationContext(), "W7 is not installed!", Toast.LENGTH_SHORT).show();
        }
        
        final IntentFilter myFilter = new IntentFilter(ACTION_FROM_SERVICE);
		registerReceiver(mReceiver, myFilter);
		
		//se il servizio di W7 non � avviato avvia l'applicazione in background
		if(!isMainServiceRunning()) {
			Intent launchIntent = getPackageManager().getLaunchIntentForPackage(W7_PACK);
			launchIntent.setAction("org.main.wseven.moveTaskToBack");
			startActivity(launchIntent);
		}
       
   }

    private boolean appInstalledOrNot(String uri) {
        PackageManager pm = getPackageManager();
        boolean app_installed = false;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        }
        catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed ;
    }
    
    public boolean isMainServiceRunning() {
		ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		for (RunningServiceInfo s : manager.getRunningServices(Integer.MAX_VALUE))
			if ("org.main.wseven.MainService".equals(s.service.getClassName()))
				return true;
		return false;
	}
    
    void sendToService() {
		CharSequence text = "";
		Log.d("BroadcastActivity", "Sending message to service: " + text);
		final Intent intent = new Intent("org.main.wseven.POSITION_REQUEST");
		intent.putExtra("data", text);
		intent.putExtra("sender", ACTION_FROM_SERVICE);
		sendBroadcast(intent);
	}
    
    public void setNavigationDrawer(Bundle savedInstanceState) {
    	intestazioneActivity = intestazioneDrawer = getTitle();
        intestazioneOpzioni = getResources().getStringArray(R.array.opzioni_array);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerListView = (ListView) findViewById(R.id.left_drawer);
        
        // intestazioneOpzioni[intestazioneOpzioni.length-1] += ": " + user.getNome() + " " + user.getCognome();
        
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
                	Utente.deleteConnessioneVeloce(db);
                	Intent intent = new Intent(MainBISActivity.this, AccessoActivity.class);
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
        if (position == 5 ) fragment = new InfoUtenteFragment();
        if (position == 6 ) fragment = new ImpostazioniFragment();
        
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
    				String serverAddress = Impostazione.getIndirizzo(db);
    				if(XMLRPC.startServer(user, serverAddress)) {
    					EditText indirizzo = (EditText) findViewById(R.id.editTextServer);
    					Impostazione.updateIndirizzo(db, indirizzo.getText().toString().trim());
    					Toast.makeText(getApplicationContext(), "Server avviato con successo", Toast.LENGTH_SHORT).show();
    				}
    				else
    					Toast.makeText(getApplicationContext(), "Impossibile avviare il Server", Toast.LENGTH_SHORT).show();
    			}
    			else
    				Toast.makeText(getApplicationContext(), "Password non corretta", Toast.LENGTH_SHORT).show();
		  }
    	});

    	alert.setNegativeButton("Annulla", null);

    	alert.show();
    	
    }
    
    public void cambiaInfoUtente(View view) {
    	CheckBox presenza = (CheckBox) findViewById(R.id.checkBoxPresenza);
    	RadioButton rbAltracamera = (RadioButton) findViewById(R.id.radioButtonCamera);
        RadioButton rbEsterno = (RadioButton) findViewById(R.id.radioButtonEsterno);
        
        if (!rbAltracamera.isChecked() && !rbEsterno.isChecked())
        	Toast.makeText(getApplicationContext(), "Seleziona una risposta alla domanda", Toast.LENGTH_SHORT).show();
        
        if(rbEsterno.isChecked())
        	user.setEsterno(true);
        else
        	user.setEsterno(false);
        user.setPresente(presenza.isChecked());
    	
    	Toast.makeText(getApplicationContext(), "Dati modificati con successo", Toast.LENGTH_SHORT).show();
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
    	ImageButton start = (ImageButton) findViewById(R.id.starSimulazioneButton);
    	start.setClickable(false);
    	
    	if(isMainServiceRunning()) {
			sendToService();
		} else {
			Intent launchIntent = getPackageManager().getLaunchIntentForPackage(W7_PACK);
			launchIntent.setAction("org.main.wseven.POSITION_REQUEST");
			startActivity(launchIntent);
		    	
			if ( (lat == null && lon == null) || lat.equals("") && lon.equals("")) {
				Toast.makeText(getApplicationContext(), "Errore in W7. Lat/Lon non riconosciuta.", Toast.LENGTH_SHORT).show();
				return;
			}
			if (gmtTime.equals("error")){
				Toast.makeText(getApplicationContext(), "Errore in W7. gmtTime non riconosciuto.", Toast.LENGTH_SHORT).show();
			}
		}
    		
    	Report.reset(db);
		
		Intent intent = new Intent(getApplicationContext(), SimulazioneActivity.class);
    	intent.putExtra(Costanti.UTENTE, user);
    	String timestamp = gmtTime.replace(" ", "");
    	timestamp = timestamp.replace("-", "");
    	timestamp = timestamp.replace("_", "");
    	timestamp = timestamp.replace(":", "");
    	timestamp = timestamp.replace(".", "");
    	    	
    	intent.putExtra("timestamp", "time"+timestamp);
    	
    	startActivity(intent);
    	finish();
    	
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
	    		boolean stato = switchComponente.isChecked();
        		if (stato) {
        			if(c.getNome().equals("Condizionatore")) {
    	    			Bundle bundle = getIntent().getExtras();
    	    	        if(bundle != null) 
    	    	        	if(bundle.get(c.getNome()) != null) 
    	    	        		Componente.update(db, c.getNome(), (Integer) bundle.get(c.getNome()));
            		}
        			else
            		if(switchComponente.getText().equals("Termosifone")) {
            			Bundle bundle = getIntent().getExtras();
    	    	        if(bundle != null) 
    	    	        	if(bundle.get(c.getNome()) != null) 
    	    	        		Componente.update(db, c.getNome(), (Integer) bundle.get(c.getNome()));
            		}
            		else
            		if(switchComponente.getText().equals("Forno a microonde")) {
            			Bundle bundle = getIntent().getExtras();
    	    	        if(bundle != null) 
    	    	        	if(bundle.get(c.getNome()) != null) 
    	    	        		Componente.update(db, c.getNome(), (Integer) bundle.get(c.getNome()));
            		}
            		else
            		if(switchComponente.getText().equals("Illuminazione")) {
            			Bundle bundle = getIntent().getExtras();
    	    	        if(bundle != null) 
    	    	        	if(bundle.get(c.getNome()) != null) 
    	    	        		Componente.update(db, c.getNome(), (Integer) bundle.get(c.getNome()));
            		}
            		else
            			Componente.update(db, c.getNome(), ((stato) ? 1:0));
            	}
	    	}
	    }
	    UtilConfigurazione.updateComponenti(db, 1);
	    Toast.makeText(getApplicationContext(), "Componenti modificati con successo", Toast.LENGTH_SHORT).show();
    }
   
    public void cambiaMeteo(View view) {
   // 	TextView textLoc = (TextView)findViewById(R.id.textEditLocalita);
    	SeekBar seekMeteo = (SeekBar)findViewById(R.id.seekMeteo);
    	SeekBar seekTempInt = (SeekBar)findViewById(R.id.seekTemperaturaInterna);
    	SeekBar seekTempEst = (SeekBar)findViewById(R.id.seekTemperaturaEsterna);
    	SeekBar seekUmiditaInt = (SeekBar)findViewById(R.id.seekUmiditaInterna);
    	SeekBar seekUmiditaEst = (SeekBar)findViewById(R.id.seekUmiditaEsterna);
    	SeekBar seekVento = (SeekBar)findViewById(R.id.seekVento);
    	SeekBar seekLumi = (SeekBar)findViewById(R.id.seekLumi);
    	
    	UtilConfigurazione.updateMeteo(db, "-", seekMeteo.getProgress(), seekTempInt.getProgress(), seekTempEst.getProgress(), seekUmiditaInt.getProgress(), seekUmiditaEst.getProgress(), seekVento.getProgress(), seekLumi.getProgress());
    	
        Toast.makeText(getApplicationContext(), "Clima modificato con successo", Toast.LENGTH_SHORT).show();
    }
    
    public void cambiaMeteoSole(View view) {
 		UtilConfigurazione.updateMeteo(db, "-", 90, 25, 40, 80, 90, 0, 100);
 		Date data = new Date();
 		data.setMonth(Calendar.AUGUST);
 		UtilConfigurazione.updateData(db, data.getTime());
 		setClimaMeteoAvvioVeloce(Configurazione.getConfigurazione(db));
 	}
 	
    public void cambiaMeteoSereno(View view) {
 		UtilConfigurazione.updateMeteo(db, "-", 70, 20, 30, 50, 60, 2, 70); 
 		Date data = new Date();
 		data.setMonth(Calendar.MAY);
 		UtilConfigurazione.updateData(db, data.getTime());
 		setClimaMeteoAvvioVeloce(Configurazione.getConfigurazione(db));
 	}
 		
    public void cambiaMeteoNuvole(View view) {
 		UtilConfigurazione.updateMeteo(db, "-", 40, 15, 25, 40, 50, 4, 40);
 		Date data = new Date();
 		data.setMonth(Calendar.NOVEMBER);
 		UtilConfigurazione.updateData(db, data.getTime());
 		setClimaMeteoAvvioVeloce(Configurazione.getConfigurazione(db));
 	}
 	
	public void cambiaMeteoPioggia(View view) {
    	UtilConfigurazione.updateMeteo(db, "-", 20, 10, 25, 70, 80, 7, 20);
 		Date data = new Date();
 		data.setMonth(Calendar.JANUARY);
 		UtilConfigurazione.updateData(db, data.getTime());
 		setClimaMeteoAvvioVeloce(Configurazione.getConfigurazione(db));
 	}
   
    public void cambiaMeteoMenu(View view) {
    	
    	findViewById(R.id.cambiaMeteoButton).setClickable(false);
    	findViewById(R.id.meteoAttualeButton).setClickable(false);
		
    	try {
    		Toast.makeText(getApplicationContext(), "Caricamento meteo locale in corso...", Toast.LENGTH_SHORT).show();
        	
    		Map<String, Integer> parametri = null;
	    	
			String localita = (new GPSTracker(this)).getLocality(this);
			parametri = Rest.getMeteoLocale(this, localita);
			
		//	TextView textLoc = (TextView) findViewById(R.id.textEditLocalita);
	        SeekBar seekMeteo = (SeekBar) findViewById(R.id.seekMeteo);
			SeekBar seekTempInt = (SeekBar) findViewById(R.id.seekTemperaturaInterna);
			SeekBar seekTempEst = (SeekBar) findViewById(R.id.seekTemperaturaEsterna);
			SeekBar seekUmiditaInt = (SeekBar) findViewById(R.id.seekUmiditaInterna);
			SeekBar seekUmiditaEst = (SeekBar) findViewById(R.id.seekUmiditaEsterna);
			SeekBar seekVento = (SeekBar) findViewById(R.id.seekVento);
	        
		//	textLoc.setText(localita);
	        seekMeteo.setProgress(parametri.get("meteo"));
	        seekTempInt.setProgress(parametri.get("tempInt"));
	        seekTempEst.setProgress(parametri.get("tempEst"));
	        seekUmiditaEst.setProgress(parametri.get("umiditaInt"));
	        seekUmiditaInt.setProgress(parametri.get("umiditaEst"));
	        seekVento.setProgress(parametri.get("vento"));
	        
			Toast.makeText(getApplicationContext(), "Meteo caricato: "+ localita, Toast.LENGTH_SHORT).show();
		}
		catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(getApplicationContext(), "Impossibile caricare meteo locale", Toast.LENGTH_SHORT).show();
	    }
    	
    	findViewById(R.id.cambiaMeteoButton).setClickable(true);
    	findViewById(R.id.meteoAttualeButton).setClickable(true);
		
    }
    
    public void setClimaMeteoAvvioVeloce(Configurazione configurazione) {
    	
    	TextView localitaText = (TextView) findViewById(R.id.textLoc);
        TextView dataText = (TextView) findViewById(R.id.textData);
        TextView climaMeteo = (TextView) findViewById(R.id.textMeteo);
        TextView climaTempInt = (TextView) findViewById(R.id.textTemperaturaInterna);
        TextView climaTempEst = (TextView) findViewById(R.id.textTemperaturaEsterna);
        TextView climaUmiditaInt = (TextView) findViewById(R.id.textUmiditaInterna);
        TextView climaUmiditaEst = (TextView) findViewById(R.id.textUmiditaEsterna);
        TextView climaVento = (TextView) findViewById(R.id.textVento);
        localitaText.setText(UtilConfigurazione.setTextViewLocalita(configurazione.getLocalita()));
        dataText.setText(UtilConfigurazione.setTextViewData(configurazione.getData())+ " - " + UtilConfigurazione.setTextViewOrario(configurazione.getOra(), configurazione.getMinuti()));
        climaMeteo.setText(UtilConfigurazione.setTextViewMeteo(configurazione.getMeteo()));
        climaTempInt.setText(UtilConfigurazione.setTextViewTemperaturaInterna(configurazione.getTemperaturaInt()));
        climaTempEst.setText(UtilConfigurazione.setTextViewTemperaturaEsterna(configurazione.getTemperaturaEst()));
        climaUmiditaInt.setText(UtilConfigurazione.setTextViewUmidita(configurazione.getUmiditaInt()));
        climaUmiditaEst.setText(UtilConfigurazione.setTextViewUmidita(configurazione.getUmiditaEst()));
        climaVento.setText(UtilConfigurazione.setTextViewVento(configurazione.getVento()));
        
    }
    
}
