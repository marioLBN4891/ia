package ai.smarthome.fragment;

import ai.smarthome.R;
import ai.smarthome.database.wrapper.Configurazione;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

@SuppressLint("SimpleDateFormat")
public class RiepilogoFragment extends Fragment {
	
	public static final String CONFIGURAZIONE = "configurazione";


    public RiepilogoFragment() {
        // Empty constructor required for fragment subclasses
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	
    	Configurazione conf = (Configurazione) getArguments().getSerializable(CONFIGURAZIONE);
    	View rootView = inflater.inflate(R.layout.fragment_riepilogo, container, false);
        String intestazione = getResources().getStringArray(R.array.opzioni_array)[conf.getPosizione()];
        getActivity().setTitle(intestazione);
        
        TextView dataText = (TextView) rootView.findViewById(R.id.dataText);
        dataText.setText(conf.getDataToString());
        
        TextView orarioText = (TextView) rootView.findViewById(R.id.orarioText);
        orarioText.setText(conf.getOraToString());
        
        TextView sensoriText = (TextView) rootView.findViewById(R.id.sensoriText);
      //  sensoriText.append((conf.getSensoriToString()));
        
        return rootView;
    }
    
    
}
