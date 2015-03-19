package ai.smarthome.activity.fragmentMain;

import ai.smarthome.R;
import ai.smarthome.database.DatabaseHelper;
import ai.smarthome.database.wrapper.Impostazione;
import ai.smarthome.database.wrapper.Utente;
import ai.smarthome.util.Costanti;
import android.app.Fragment;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class ImpostazioniFragment extends Fragment {

	View rootView;
	private SQLiteDatabase db;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	
    	Utente user = (Utente) getArguments().getSerializable(Costanti.UTENTE);
    	rootView = inflater.inflate(R.layout.fragment_impostazioni, container, false);
        String intestazione = getResources().getStringArray(R.array.opzioni_array)[user.getPosizione()];
        getActivity().setTitle(intestazione);
        
        db = new DatabaseHelper(getActivity()).getReadableDatabase();
        
        EditText indirizzo = (EditText) rootView.findViewById(R.id.editTextServer);
        indirizzo.setText(Impostazione.getIndirizzo(db));
        
        return rootView;
    }
      
}
