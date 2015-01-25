package ai.smarthome.activity.fragmentMain;

import ai.smarthome.R;
import ai.smarthome.database.wrapper.Utente;
import ai.smarthome.util.Costanti;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

public class InfoPersonaliFragment extends Fragment {

	private TextView utente;
    private EditText mail;
    
    View rootView;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	
    	Utente user = (Utente) getArguments().getSerializable(Costanti.UTENTE);
    	rootView = inflater.inflate(R.layout.fragment_infopersonali, container, false);
        String intestazione = getResources().getStringArray(R.array.opzioni_array)[user.getPosizione()];
        getActivity().setTitle(intestazione);
        
        utente = (TextView) rootView.findViewById(R.id.utenteEditText);
        utente.setText(user.getCognome() + " " + user.getNome());
        
        mail = (EditText) rootView.findViewById(R.id.mailEditText);
        mail.setText(user.getMail());
        
        return rootView;
    }
      
}
