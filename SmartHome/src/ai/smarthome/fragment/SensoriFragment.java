package ai.smarthome.fragment;

import ai.smarthome.R;
import ai.smarthome.database.wrapper.Configurazione;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;

public class SensoriFragment extends Fragment {
	
    public static final String CONFIGURAZIONE = "configurazione";

    public SensoriFragment() {
        // Empty constructor required for fragment subclasses
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	
    	Configurazione conf = (Configurazione) getArguments().getSerializable(CONFIGURAZIONE);
    	View rootView = inflater.inflate(R.layout.fragment_sensori, container, false);
        String intestazione = getResources().getStringArray(R.array.opzioni_array)[conf.getPosizione()];
        getActivity().setTitle(intestazione);

        Switch temperatura = (Switch)rootView.findViewById(R.id.temperatura);
        Switch umidita = (Switch)rootView.findViewById(R.id.umidita);
        Switch vento = (Switch)rootView.findViewById(R.id.vento);
        Switch presenza = (Switch)rootView.findViewById(R.id.presenza);
        Switch sonoro = (Switch)rootView.findViewById(R.id.sonoro);
        temperatura.setChecked(conf.getSensoreTemperatura());
        umidita.setChecked(conf.getSensoreUmidita());
        vento.setChecked(conf.getSensoreVento());
        presenza.setChecked(conf.getSensorePresenza());
        sonoro.setChecked(conf.getSensoreSonoro());
        
        return rootView;
    }
}
