package ai.smarthome.fragment;

import ai.smarthome.R;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.TimePicker;

public class OraFragment extends Fragment {
	
    public static final String ARG_OBJECT_NUMBER = "posizione";

    private int hour;
	private int minute;
	
	private TextView orarioText;
	private TimePicker timePicker;
	
    public OraFragment() {
        // Empty constructor required for fragment subclasses
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	
    	
    	int i = getArguments().getInt(ARG_OBJECT_NUMBER);
    	View rootView = inflater.inflate(R.layout.fragment_ora, container, false);
        String object = getResources().getStringArray(R.array.opzioni_array)[i];
        getActivity().setTitle(object);
		
        orarioText = (TextView)rootView.findViewById(R.id.orarioText);
        timePicker = (TimePicker)rootView.findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);
        
        hour = getArguments().getInt("hour");
        minute = getArguments().getInt("minute");
        
        orarioText.setText(new StringBuilder().append("Orario configurato: ").append(pad(hour)).append(":").append(pad(minute)));
        timePicker.setCurrentHour(hour);
    	timePicker.setCurrentMinute(minute);
        
        return rootView;
    }
    
    private static String pad(int c) {
		if (c >= 10)
		   return String.valueOf(c);
		else
		   return "0" + String.valueOf(c);
	}
}
