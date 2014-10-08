package ai.smarthome.fragment;

import ai.smarthome.R;
import ai.smarthome.database.wrapper.Configurazione;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;


@SuppressLint("SimpleDateFormat")
public class DataFragment extends Fragment {
	
	public static final String CONFIGURAZIONE = "configurazione";
	
    public DataFragment() {
        // Empty constructor required for fragment subclasses
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	
    	Configurazione conf = (Configurazione) getArguments().getSerializable(CONFIGURAZIONE);
    	View rootView = inflater.inflate(R.layout.fragment_data, container, false);
        String intestazione = getResources().getStringArray(R.array.opzioni_array)[conf.getPosizione()];
        getActivity().setTitle(intestazione);
		
        TextView dataText = (TextView)rootView.findViewById(R.id.dataText);
        CalendarView calendarView = (CalendarView)rootView.findViewById(R.id.calendarView);
        
        calendarView.setShowWeekNumber(false);
        calendarView.setFirstDayOfWeek(2);

        dataText.setText(new StringBuilder().append("Data configurata: ").append(conf.getDataToString()));
        calendarView.setDate(conf.getDataMilliTime());
        
        return rootView;
       }
}
