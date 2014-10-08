package ai.smarthome;

import ai.smarthome.database.wrapper.Configurazione;
import ai.smarthome.fragment.ComponentiFragment;
import ai.smarthome.fragment.DataFragment;
import ai.smarthome.fragment.InfoPersonaliFragment;
import ai.smarthome.fragment.MeteoFragment;
import ai.smarthome.fragment.OraFragment;
import ai.smarthome.fragment.SensoriFragment;
import ai.smarthome.fragment.RiepilogoFragment;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.ListView;
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
public class MainActivity extends Activity {
    
	public static final String CONFIGURAZIONE = "configurazione";
	
	private DrawerLayout drawerLayout;
    private ListView drawerListView;
    private ActionBarDrawerToggle drawerToggle;

    private CharSequence intestazioneDrawer;
    private CharSequence intestazioneActivity;
    private String[] intestazioneOpzioni; 

    private Configurazione conf = new Configurazione();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setNavigationDrawer(savedInstanceState);
        
      
        
    }

    public void setNavigationDrawer(Bundle savedInstanceState) {
    	intestazioneActivity = intestazioneDrawer = getTitle();
        intestazioneOpzioni = getResources().getStringArray(R.array.opzioni_array);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerListView = (ListView) findViewById(R.id.left_drawer);

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
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(intestazioneActivity);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(intestazioneDrawer);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        drawerLayout.setDrawerListener(drawerToggle);

        if (savedInstanceState == null) {
            selectItem(0);
        }
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
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
        case R.id.actionInquiry:
            Intent intent = new Intent(MainActivity.this, InquiryActivity.class);
            startActivity(intent);
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
        
        if (position == 0 ) fragment = new RiepilogoFragment();
        if (position == 1 ) fragment = new InfoPersonaliFragment();
        if (position == 2 ) fragment = new DataFragment();
        if (position == 3 ) fragment = new OraFragment();
        if (position == 4 ) fragment = new MeteoFragment();
        if (position == 5 ) fragment = new SensoriFragment();
        if (position == 6 ) fragment = new ComponentiFragment();
        
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
        new AlertDialog.Builder(this).setIcon(R.drawable.ic_launcher).setTitle("Esci")
                .setMessage("Chiudere l'applicazione?")
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).setNegativeButton("No", null).show();
    }
    
    public void cambiaOrario(View view) {
    	
    	TextView orarioText = (TextView) findViewById(R.id.orarioText);
    	TimePicker timePicker = (TimePicker) findViewById(R.id.timePicker);
    	conf.setHour(timePicker.getCurrentHour());
    	conf.setMinute(timePicker.getCurrentMinute());
    	orarioText.setText(new StringBuilder().append("Orario configurato: ").append(conf.getOraToString()));
    	timePicker.setCurrentHour(conf.getHour());
		timePicker.setCurrentMinute(conf.getMinute());
		Toast.makeText(getApplicationContext(), "Orario modificato con successo", Toast.LENGTH_SHORT).show();
    
    }
    
    @SuppressLint("SimpleDateFormat")
	public void cambiaData(View view) {
    	
    	TextView dataText = (TextView) findViewById(R.id.dataText);
    	CalendarView calendarView = (CalendarView) findViewById(R.id.calendarView);
    	conf.setDataMilliTime(calendarView.getDate());  
    	dataText.setText(new StringBuilder().append("Data configurata: ").append(conf.getDataToString()));
    	Toast.makeText(getApplicationContext(), "Data modificata con successo", Toast.LENGTH_SHORT).show();
    	
    }
    
    public void startSimulazione(View view) {
    	
    	Intent intent = new Intent(getApplicationContext(), SimulazioneActivity.class);
    	intent.putExtra("configurazione", conf);
    	startActivity(intent);
        finish();
    }
    
    public void cambiaSensori(View view) {
    	
    	Switch temperatura = (Switch)findViewById(R.id.temperatura);
        Switch umidita = (Switch)findViewById(R.id.umidita);
        Switch vento = (Switch)findViewById(R.id.vento);
        Switch presenza = (Switch)findViewById(R.id.presenza);
        Switch sonoro = (Switch)findViewById(R.id.sonoro);
        conf.setSensoreTemperatura(temperatura.isChecked());
        conf.setSensoreUmidita(umidita.isChecked());
        conf.setSensoreVento(vento.isChecked());
        conf.setSensorePresenza(presenza.isChecked());
        conf.setSensoreSonoro(sonoro.isChecked());
    	Toast.makeText(getApplicationContext(), "Sensori modificati con successo", Toast.LENGTH_SHORT).show();
    	
    }
   
   
    
     
    
}
