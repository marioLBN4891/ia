package ai.smarthome.activity.fragmentMain;

import java.util.ArrayList;

import ai.smarthome.R;
import ai.smarthome.database.DatabaseHelper;
import ai.smarthome.database.wrapper.Componente;
import ai.smarthome.database.wrapper.Utente;
import ai.smarthome.util.Costanti;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Switch;

public class ComponentiFragment extends Fragment {
	
	private SQLiteDatabase db;
	
    public ComponentiFragment() {
    
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	
    	Utente user = (Utente) getArguments().getSerializable(Costanti.UTENTE);
    	View rootView = inflater.inflate(R.layout.fragment_componenti, container, false);
        String intestazione = getResources().getStringArray(R.array.opzioni_array)[user.getPosizione()];
        getActivity().setTitle(intestazione);

        
        db = new DatabaseHelper(getActivity()).getWritableDatabase();
        ArrayList<Componente> lista = Componente.getAllLista(db);
	    
	    LinearLayout linearLayout = (LinearLayout) rootView.findViewById(R.id.llComponenti);
        LayoutParams layoutParam = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        layoutParam.setMargins(0, 12, 0, 0);
        
        for(Componente c : lista) {
        	if (!(c.getNome().equals("Dispensa") || c.getNome().equals("Mobile") || c.getNome().equals("Frigorifero"))) {
        		final Switch switchElement = new Switch(getActivity());
        		switchElement.setText(c.getNome());
        		switchElement.setId(c.getId());
        		if(c.getTipo().equals(Componente.ACCESO_SPENTO)) {
        			switchElement.setTextOn("Acceso");
        			switchElement.setTextOff("Spento");
        		}
        	
        		if(c.getTipo().equals(Componente.APERTO_CHIUSO)) {
        			switchElement.setTextOn("Aperto");
        			switchElement.setTextOff("Chiuso");
        		}
        	
        		switchElement.setSwitchPadding(50);
        		switchElement.setSwitchMinWidth(280);
        	
        		switchElement.setTypeface(null, Typeface.BOLD);
        		switchElement.setChecked((c.getStato() > 0) ? true : false);
        	
        		switchElement.setLayoutParams(layoutParam);
        		
        		if(c.getNome().equals("Condizionatore")) {
        			final CharSequence[] listaStati = {"potenza bassa", "potenza media", "potenza massima", "deumidificatore"};	 
        	    	creaDialog(switchElement, listaStati);
        		}
        		
        		if(c.getNome().equals("Termosifone")) {
        			final CharSequence[] listaStati = {"potenza bassa", "potenza media", "potenza massima"};	 
        	    	creaDialog(switchElement, listaStati);
        		}
        		
        		if(c.getNome().equals("Forno a microonde")) {
        			final CharSequence[] listaStati = {"riscaldamento", "scongelamento"};	 
        	    	creaDialog(switchElement, listaStati);
        		}
        		
        		if(c.getNome().equals("Illuminazione")) {
        			final CharSequence[] listaStati = {"intensità bassa", "intensità media", "intensità alta"};	 
        	    	creaDialog(switchElement, listaStati);
        		}
        		
        		linearLayout.addView(switchElement);
        	
        	}
        }
        
        db.close();
        return rootView;
    }
    
    private void creaDialog(final Switch switchElement, final CharSequence[] listaStati) {
    	switchElement.setOnClickListener(new OnClickListener() {
    		@Override
    		public void onClick(View v) {
    			if(switchElement.isChecked()) {
    				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
    				alertDialogBuilder
    				.setTitle("Stato "+switchElement.getText()+":")
    				.setSingleChoiceItems(listaStati, 0, null)
    				.setPositiveButton("Conferma",new DialogInterface.OnClickListener() {
    					public void onClick(DialogInterface dialog,int id) {
    						Intent i = getActivity().getIntent();
    						i.putExtra((String) switchElement.getText(), id);
    					}
    				})
    				.setNegativeButton("Annulla",new DialogInterface.OnClickListener() {
    					public void onClick(DialogInterface dialog,int id) {
    						switchElement.setChecked(false);
    						dialog.cancel();
    					}
    				});
    				alertDialogBuilder.create().show();
    			}
    		}
	    });
    }
    
}
