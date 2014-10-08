package ai.smarthome.fragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import ai.smarthome.R;
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
	
    public static final String ARG_OBJECT_NUMBER = "posizione";

    private Long dataMilliTime;
	
	private TextView dataText;
	private CalendarView calendarView; 
	
    public DataFragment() {
        // Empty constructor required for fragment subclasses
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	
    	int i = getArguments().getInt(ARG_OBJECT_NUMBER);
    	View rootView = inflater.inflate(R.layout.fragment_data, container, false);
        String object = getResources().getStringArray(R.array.opzioni_array)[i];
        getActivity().setTitle(object);
		
        dataText = (TextView)rootView.findViewById(R.id.dataText);
        calendarView = (CalendarView)rootView.findViewById(R.id.calendarView);
        
        calendarView.setShowWeekNumber(false);
        calendarView.setFirstDayOfWeek(2);

        dataMilliTime = getArguments().getLong("dataMilliTime");
        SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yyyy");
    	Date data = new Date();
        data.setTime(dataMilliTime);
        String dataConfigurata = sdf.format(data);
        dataText.setText(new StringBuilder().append("Data configurata: ").append(dataConfigurata));
        calendarView.setDate(dataMilliTime);
        
        return rootView;
       }
}
