package ai.smarthome.activity.fragmentMain;

import ai.smarthome.R;
import ai.smarthome.database.wrapper.Configurazione;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.TimePicker;

public class OraFragment extends Fragment {
	
	public static final String CONFIGURAZIONE = "configurazione";
    
    public OraFragment() {
        // Empty constructor required for fragment subclasses
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	
    	Configurazione conf = (Configurazione) getArguments().getSerializable(CONFIGURAZIONE);
    	View rootView = inflater.inflate(R.layout.fragment_ora, container, false);
        String intestazione = getResources().getStringArray(R.array.opzioni_array)[conf.getPosizione()];
        getActivity().setTitle(intestazione);
        
        TextView orarioText = (TextView)rootView.findViewById(R.id.orarioText);
        TimePicker timePicker = (TimePicker)rootView.findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);
        StringBuilder orario = new StringBuilder().append("Orario configurato: ").append(conf.getOraToString());
        orarioText.setText(orario);
        timePicker.setCurrentHour(conf.getHour());
    	timePicker.setCurrentMinute(conf.getMinute());
        
        return rootView;
    }
    
}
