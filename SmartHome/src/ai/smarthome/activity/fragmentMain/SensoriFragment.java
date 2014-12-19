package ai.smarthome.activity.fragmentMain;

import java.util.ArrayList;

import ai.smarthome.R;
import ai.smarthome.database.DatabaseHelper;
import ai.smarthome.database.wrapper.Componente;
import ai.smarthome.database.wrapper.Configurazione;
import ai.smarthome.database.wrapper.Sensore;
import ai.smarthome.util.LogView;
import android.app.Fragment;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.LinearLayout.LayoutParams;

public class SensoriFragment extends Fragment {
	
    public static final String CONFIGURAZIONE = "configurazione";
    private SQLiteDatabase db;
    
    public SensoriFragment() {
        // Empty constructor required for fragment subclasses
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	
    	Configurazione conf = (Configurazione) getArguments().getSerializable(CONFIGURAZIONE);
    	View rootView = inflater.inflate(R.layout.fragment_sensori, container, false);
        String intestazione = getResources().getStringArray(R.array.opzioni_array)[conf.getPosizione()];
        getActivity().setTitle(intestazione);

        db = new DatabaseHelper(getActivity()).getWritableDatabase();
        LogView.sensori(db);
        ArrayList<Sensore> lista = Sensore.getAllLista(db);
	    
	    LinearLayout linearLayout = (LinearLayout) rootView.findViewById(R.id.llSensori);
        LayoutParams layoutParam = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        
        for(Sensore s : lista) {
        	Switch switchElement = new Switch(getActivity());
        	switchElement.setText(s.getNome());
        	switchElement.setId(s.getId());
        	
        	if(s.getTipo().equals(Componente.ACCESO_SPENTO)) {
        		switchElement.setTextOn("ON");
        		switchElement.setTextOff("OFF");
        	}
        	
        	if(s.getTipo().equals(Componente.APERTO_CHIUSO)) {
        		switchElement.setTextOn("APERTO");
        		switchElement.setTextOff("CHIUSO");
        	}
        	
        	switchElement.setChecked((s.getStato() == 1) ? true : false);
        	
        	switchElement.setClickable(false);
        	switchElement.setLayoutParams(layoutParam);
        	linearLayout.addView(switchElement);
        }
        
        return rootView;
    }
}
