package ai.smarthome.fragment;

import ai.smarthome.R;
import ai.smarthome.database.wrapper.Configurazione;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

public class InfoPersonaliFragment extends Fragment {

	public static final String CONFIGURAZIONE = "configurazione";
    private TextView utente, username;
    private EditText mail;
    
    View rootView;
    private Configurazione conf;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	
    	conf = (Configurazione) getArguments().getSerializable(CONFIGURAZIONE);
    	rootView = inflater.inflate(R.layout.fragment_infopersonali, container, false);
        String intestazione = getResources().getStringArray(R.array.opzioni_array)[conf.getPosizione()];
        getActivity().setTitle(intestazione);
        
        
        utente = (TextView) rootView.findViewById(R.id.utenteEditText);
        username = (TextView) rootView.findViewById(R.id.usernameEditText);
        utente.setText(conf.getUtente().getCognome() + " " + conf.getUtente().getNome());
        username.setText(conf.getUtente().getUsername());
        
        mail = (EditText) rootView.findViewById(R.id.mailEditText);
        mail.setText(conf.getUtente().getMail());
        
        return rootView;
    }
      
}
