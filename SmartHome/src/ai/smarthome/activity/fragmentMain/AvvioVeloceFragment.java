package ai.smarthome.activity.fragmentMain;


import ai.smarthome.R;
import ai.smarthome.database.wrapper.Configurazione;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class AvvioVeloceFragment extends Fragment {
	
	public static final String CONFIGURAZIONE = "configurazione";
	
    public AvvioVeloceFragment() {
        // Empty constructor required for fragment subclasses
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	
    	Configurazione conf = (Configurazione) getArguments().getSerializable(CONFIGURAZIONE);
    	View rootView = inflater.inflate(R.layout.fragment_avvioveloce, container, false);
        String intestazione = getResources().getStringArray(R.array.opzioni_array)[conf.getPosizione()];
        getActivity().setTitle(intestazione);
        
        TextView text = (TextView) rootView.findViewById(R.id.text_fragment_avvioveloce1);
        String testoAvvio = (String) text.getText();
        text.setText("Ciao " + conf.getUtente().getNome()+"! "+ testoAvvio);
        
         return rootView;
    }
    
   
}
