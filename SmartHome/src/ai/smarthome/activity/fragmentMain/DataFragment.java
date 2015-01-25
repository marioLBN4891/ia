package ai.smarthome.activity.fragmentMain;

import ai.smarthome.R;
import ai.smarthome.database.DatabaseHelper;
import ai.smarthome.database.wrapper.Configurazione;
import ai.smarthome.database.wrapper.Utente;
import ai.smarthome.util.Costanti;
import ai.smarthome.util.UtilConfigurazione;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.TimePicker;


@SuppressLint("SimpleDateFormat")
public class DataFragment extends Fragment {
	
	private SQLiteDatabase db;
	
    public DataFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	
    	Utente user = (Utente) getArguments().getSerializable(Costanti.UTENTE);
    	View rootView = inflater.inflate(R.layout.fragment_data, container, false);
        String intestazione = getResources().getStringArray(R.array.opzioni_array)[user.getPosizione()];
        getActivity().setTitle(intestazione);
		
        db = new DatabaseHelper(getActivity()).getWritableDatabase();
        
        Configurazione meteo = Configurazione.getConfigurazione(db);
        
        TextView dataText = (TextView)rootView.findViewById(R.id.dataText);
        CalendarView calendarView = (CalendarView)rootView.findViewById(R.id.calendarView);
        
        calendarView.setShowWeekNumber(false);
        calendarView.setFirstDayOfWeek(2);

        TimePicker timePicker = (TimePicker)rootView.findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);
        timePicker.setCurrentHour(meteo.getOra());
    	timePicker.setCurrentMinute(meteo.getMinuti());
        
        dataText.setText(new StringBuilder().append("Configurazione: ").append(UtilConfigurazione.getDataToString(meteo.getData())).append(" - ").append(UtilConfigurazione.getOraToString(meteo.getOra(), meteo.getMinuti())));
        calendarView.setDate(meteo.getData());
        
        db.close();
        return rootView;
       }
}
