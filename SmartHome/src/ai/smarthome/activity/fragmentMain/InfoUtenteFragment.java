package ai.smarthome.activity.fragmentMain;

import ai.smarthome.R;
import ai.smarthome.database.wrapper.Utente;
import ai.smarthome.util.Costanti;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;

public class InfoUtenteFragment extends Fragment {

	View rootView;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	
    	Utente user = (Utente) getArguments().getSerializable(Costanti.UTENTE);
    	rootView = inflater.inflate(R.layout.fragment_infoutente, container, false);
        String intestazione = getResources().getStringArray(R.array.opzioni_array)[user.getPosizione()];
        getActivity().setTitle(intestazione);
        
        TextView textUtente = (TextView) rootView.findViewById(R.id.textUtente);
        String utente = "L'utente ";
        textUtente.setText(utente);
        
        
        final CheckBox presenzaCucina = (CheckBox) rootView.findViewById(R.id.checkBoxPresenza);
        final TextView textDomanda = (TextView) rootView.findViewById(R.id.textDomanda_presenza);
        final RadioButton rbAltracamera = (RadioButton) rootView.findViewById(R.id.radioButtonCamera);
        final RadioButton rbEsterno = (RadioButton) rootView.findViewById(R.id.radioButtonEsterno);
        
        if (user.isPresente()) {
        	presenzaCucina.setChecked(true);
        	presenzaCucina.setText(R.string.checkUserPresentTrue);
        	textDomanda.setText(R.string.dadovevieni);
			rbAltracamera.setText(R.string.daaltracamera);
			rbEsterno.setText(R.string.daesterno);
			if (user.getEsterno())
				rbEsterno.setChecked(true);
			else
				rbAltracamera.setChecked(true);
        }
        else {
        	presenzaCucina.setText(R.string.checkUserPresentFalse);
        	textDomanda.setText(R.string.dovevai);
			rbAltracamera.setText(R.string.inaltracamera);
			rbEsterno.setText(R.string.inesterno);
			if (user.getEsterno())
				rbEsterno.setChecked(true);
			else
				rbAltracamera.setChecked(true);
        }
        
        presenzaCucina.setOnClickListener(new OnClickListener() {
       	 
    		@Override
    		public void onClick(View v) {
    			if (presenzaCucina.isChecked()) {
    				presenzaCucina.setText(R.string.checkUserPresentTrue);
    				textDomanda.setText(R.string.dadovevieni);
    				rbAltracamera.setText(R.string.daaltracamera);
    				rbEsterno.setText(R.string.daesterno);
    			}
    			else {
    				presenzaCucina.setText(R.string.checkUserPresentFalse);
    				textDomanda.setText(R.string.dovevai);
    				rbAltracamera.setText(R.string.inaltracamera);
    				rbEsterno.setText(R.string.inesterno);
    			}
			}
    	});
        return rootView;
    }
      
}
