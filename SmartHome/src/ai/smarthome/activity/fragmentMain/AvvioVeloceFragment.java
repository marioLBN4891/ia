package ai.smarthome.activity.fragmentMain;


import ai.smarthome.R;
import ai.smarthome.database.DatabaseHelper;
import ai.smarthome.database.wrapper.Configurazione;
import ai.smarthome.database.wrapper.Meteo;
import ai.smarthome.util.UtilMeteo;
import android.app.Fragment;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class AvvioVeloceFragment extends Fragment {
	
	public static final String CONFIGURAZIONE = "configurazione";
	private SQLiteDatabase db;
	
    public AvvioVeloceFragment() {
        // Empty constructor required for fragment subclasses
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	
    	Configurazione conf = (Configurazione) getArguments().getSerializable(CONFIGURAZIONE);
    	View rootView = inflater.inflate(R.layout.fragment_avvioveloce, container, false);
        String intestazione = getResources().getStringArray(R.array.opzioni_array)[conf.getPosizione()];
        getActivity().setTitle(intestazione);
        
        db = new DatabaseHelper(getActivity()).getWritableDatabase();
        TextView text = (TextView) rootView.findViewById(R.id.text_fragment_avvioveloce1);
        String testoAvvio = (String) text.getText();
        text.setText("Ciao <b>" + conf.getUtente().getNome()+"</b>! "+ testoAvvio);
        
        Meteo meteo = Meteo.getMeteo(db);
        TextView textLoc = (TextView) rootView.findViewById(R.id.textLoc);
        TextView textData = (TextView) rootView.findViewById(R.id.textData);
        TextView textOrario = (TextView) rootView.findViewById(R.id.textOrario);
        TextView textMeteo = (TextView) rootView.findViewById(R.id.textMeteo);
        TextView textTemperatura = (TextView) rootView.findViewById(R.id.textTemperatura);
        TextView textUmidita = (TextView) rootView.findViewById(R.id.textUmidita);
        TextView textVento = (TextView) rootView.findViewById(R.id.textVento);
        
        UtilMeteo.setTextViewLocalita(textLoc, meteo.getLocalita());
        UtilMeteo.setTextViewData(textData, meteo.getData());
        UtilMeteo.setTextViewOrario(textOrario, meteo.getOra(), meteo.getMinuti());
        UtilMeteo.setTextViewMeteo(textMeteo, meteo.getMeteo());
        UtilMeteo.setTextViewTemperatura(textTemperatura, meteo.getTemperatura());
        UtilMeteo.setTextViewUmidita(textUmidita, meteo.getUmidita());
        UtilMeteo.setTextViewVento(textVento, meteo.getVento());
        
        return rootView;
    }
    
   
}
