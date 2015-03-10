package ai.smarthome.activity.fragmentMain;

import ai.smarthome.R;
import ai.smarthome.database.DatabaseHelper;
import ai.smarthome.database.wrapper.Configurazione;
import ai.smarthome.database.wrapper.Utente;
import ai.smarthome.util.Costanti;
import ai.smarthome.util.UtilConfigurazione;
import android.app.Fragment;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class AvvioVeloceFragment extends Fragment {
	
	private SQLiteDatabase db;
	
    public AvvioVeloceFragment() {
    
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	
    	Utente user = (Utente) getArguments().getSerializable(Costanti.UTENTE);
    	View rootView = inflater.inflate(R.layout.fragment_avvioveloce, container, false);
        String intestazione = getResources().getStringArray(R.array.opzioni_array)[user.getPosizione()];
        getActivity().setTitle(intestazione);
        
        db = new DatabaseHelper(getActivity()).getWritableDatabase();
        TextView text = (TextView) rootView.findViewById(R.id.text_fragment_avvioveloce1);
        String testoAvvio = (String) text.getText();
        text.setText("Ciao " + user.getNome()+"! "+ testoAvvio);
        
        Configurazione configurazione = Configurazione.getConfigurazione(db);
        
        TextView textLoc = (TextView) rootView.findViewById(R.id.textLoc);
        TextView textData = (TextView) rootView.findViewById(R.id.textData);
        TextView textMeteo = (TextView) rootView.findViewById(R.id.textMeteo);
        TextView textTemperaturaInt = (TextView) rootView.findViewById(R.id.textTemperaturaInterna);
        TextView textTemperaturaEst = (TextView) rootView.findViewById(R.id.textTemperaturaEsterna);
        TextView textUmiditaInt = (TextView) rootView.findViewById(R.id.textUmiditaInterna);
        TextView textUmiditaEst = (TextView) rootView.findViewById(R.id.textUmiditaEsterna);
        TextView textVento = (TextView) rootView.findViewById(R.id.textVento);
        TextView textComponenti = (TextView) rootView.findViewById(R.id.textComponenti);
        
        textLoc.setText(UtilConfigurazione.setTextViewLocalita(configurazione.getLocalita()));
        textData.setText(UtilConfigurazione.setTextViewData(configurazione.getData())+ " - " + UtilConfigurazione.setTextViewOrario(configurazione.getOra(), configurazione.getMinuti()));
        textMeteo.setText(UtilConfigurazione.setTextViewMeteo(configurazione.getMeteo()));
        textTemperaturaInt.setText(UtilConfigurazione.setTextViewTemperaturaInterna(configurazione.getTemperaturaInt()));
        textTemperaturaEst.setText(UtilConfigurazione.setTextViewTemperaturaEsterna(configurazione.getTemperaturaEst()));
        textUmiditaInt.setText(UtilConfigurazione.setTextViewUmidita(configurazione.getUmiditaInt()));
        textUmiditaEst.setText(UtilConfigurazione.setTextViewUmidita(configurazione.getUmiditaEst()));
        textVento.setText(UtilConfigurazione.setTextViewVento(configurazione.getVento()));
        textComponenti.setText(UtilConfigurazione.setTextViewComponenti(configurazione.getComponenti()));

        db.close();
        return rootView;
    }
    
   
}
