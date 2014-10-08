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
import android.widget.TextView;

@SuppressLint("SimpleDateFormat")
public class SimulazioneFragment extends Fragment {
	
    public static final String ARG_OBJECT_NUMBER = "posizione";

    public SimulazioneFragment() {
        // Empty constructor required for fragment subclasses
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	
    	int i = getArguments().getInt(ARG_OBJECT_NUMBER);
    	long dataMilliTime = getArguments().getLong("dataMilliTime");
    	int hour = getArguments().getInt("hour");
    	int minute = getArguments().getInt("minute");
    	
    	View rootView = inflater.inflate(R.layout.fragment_simulazione, container, false);
        String object = getResources().getStringArray(R.array.opzioni_array)[i];
        getActivity().setTitle(object);
        
        TextView dataText = (TextView) rootView.findViewById(R.id.dataText);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yyyy");
    	Date data = new Date();
        data.setTime(dataMilliTime);
        String dataConfigurata = sdf.format(data);
        dataText.setText(dataConfigurata);
        
        TextView orarioText = (TextView) rootView.findViewById(R.id.orarioText);
        orarioText.setText(new StringBuilder().append("Orario configurato: ").append(pad(hour)).append(":").append(pad(minute)));
        
        return rootView;
    }
    
    private static String pad(int c) {
		if (c >= 10)
		   return String.valueOf(c);
		else
		   return "0" + String.valueOf(c);
	}
}
