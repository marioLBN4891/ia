package ai.smarthome.activity.fragmentMain;

import java.util.ArrayList;

import ai.smarthome.R;
import ai.smarthome.database.DatabaseHelper;
import ai.smarthome.database.wrapper.Componente;
import ai.smarthome.database.wrapper.Sensore;
import ai.smarthome.database.wrapper.Utente;
import ai.smarthome.util.Costanti;
import android.app.Fragment;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.LinearLayout.LayoutParams;

public class SensoriFragment extends Fragment {
	
	private SQLiteDatabase db;
    
    public SensoriFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	
    	Utente user = (Utente) getArguments().getSerializable(Costanti.UTENTE);
    	View rootView = inflater.inflate(R.layout.fragment_sensori, container, false);
        String intestazione = getResources().getStringArray(R.array.opzioni_array)[user.getPosizione()];
        getActivity().setTitle(intestazione);

        db = new DatabaseHelper(getActivity()).getWritableDatabase();
        
        ArrayList<Sensore> lista = Sensore.getAllLista(db);
	    
	    LinearLayout linearLayout = (LinearLayout) rootView.findViewById(R.id.llSensori);
        LayoutParams layoutParam = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        
        layoutParam.setMargins(0, 20, 0, 0);
        
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
        	
        	switchElement.setTypeface(null, Typeface.BOLD);
        	switchElement.setChecked((s.getStato() == 1) ? true : false);
        	
        	switchElement.setClickable(false);
        	switchElement.setLayoutParams(layoutParam);
        	linearLayout.addView(switchElement);
        }
        db.close();
        return rootView;
    }
}
