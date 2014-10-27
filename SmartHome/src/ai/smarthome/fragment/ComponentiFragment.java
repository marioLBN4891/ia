package ai.smarthome.fragment;

import ai.smarthome.R;
import ai.smarthome.database.wrapper.Configurazione;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;

public class ComponentiFragment extends Fragment {
	
	public static final String CONFIGURAZIONE = "configurazione";
	
    public ComponentiFragment() {
        // Empty constructor required for fragment subclasses
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	
     //   StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
    //    StrictMode.setThreadPolicy(policy);
        		
        Configurazione conf = (Configurazione) getArguments().getSerializable(CONFIGURAZIONE);
    	View rootView = inflater.inflate(R.layout.fragment_componenti, container, false);
        String intestazione = getResources().getStringArray(R.array.opzioni_array)[conf.getPosizione()];
        getActivity().setTitle(intestazione);

        Switch televisione = (Switch)rootView.findViewById(R.id.televisione);
        Switch radio = (Switch)rootView.findViewById(R.id.radio);
        Switch condizionatore = (Switch)rootView.findViewById(R.id.condizionatore);
        Switch balcone = (Switch)rootView.findViewById(R.id.balcone);
        Switch macchinaCaffe = (Switch)rootView.findViewById(R.id.macchinaCaffe);
        Switch illuminazione = (Switch)rootView.findViewById(R.id.illuminazione);
        televisione.setChecked(conf.getComponenteTelevisione());
        radio.setChecked(conf.getComponenteRadio());
        condizionatore.setChecked(conf.getComponenteCondizionatore());
        balcone.setChecked(conf.getComponenteBalcone());
        macchinaCaffe.setChecked(conf.getComponenteMacchinaCaffe());
        illuminazione.setChecked(conf.getComponenteIlluminazione());
       
        return rootView;
    }
}
