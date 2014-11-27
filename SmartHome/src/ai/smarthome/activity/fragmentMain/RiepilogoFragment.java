package ai.smarthome.activity.fragmentMain;

import ai.smarthome.R;
import ai.smarthome.database.wrapper.Configurazione;
import ai.smarthome.util.UtilMeteo;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

@SuppressLint("SimpleDateFormat")
public class RiepilogoFragment extends Fragment {
	
	public static final String CONFIGURAZIONE = "configurazione";

	private TextView dataText, orarioText, climaMeteo, climaTempEst, climaUmidita, climaVento;
	private TextView compTv, compRadio, compCondizionatore, compBalcone, compMacchinaCaffe, compIlluminazione;
	private TextView sensTemperatura, sensUmidita, sensVento, sensPresenza, sensSonoro;

    public RiepilogoFragment() {
        // Empty constructor required for fragment subclasses
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	
    	Configurazione conf = (Configurazione) getArguments().getSerializable(CONFIGURAZIONE);
    	View rootView = inflater.inflate(R.layout.fragment_riepilogo, container, false);
        String intestazione = getResources().getStringArray(R.array.opzioni_array)[conf.getPosizione()];
        getActivity().setTitle(intestazione);
        
        dataText = (TextView) rootView.findViewById(R.id.dataText);
        dataText.setText(conf.getDataToString());
        
        orarioText = (TextView) rootView.findViewById(R.id.orarioText);
        orarioText.setText(conf.getOraToString());
        
        climaMeteo = (TextView) rootView.findViewById(R.id.climaMeteo);
        climaTempEst = (TextView) rootView.findViewById(R.id.climaTempEst);
        climaUmidita = (TextView) rootView.findViewById(R.id.climaUmidita);
        climaVento = (TextView) rootView.findViewById(R.id.climaVento);
        UtilMeteo.setTextViewMeteo(climaMeteo, conf.getClimaMeteo());
        UtilMeteo.setTextViewTempEst(climaTempEst, conf.getClimaTemperaturaEsterna());
        UtilMeteo.setTextViewUmidita(climaUmidita, conf.getClimaUmidita());
        UtilMeteo.setTextViewVento(climaVento, conf.getClimaVento());
        
        compTv = (TextView) rootView.findViewById(R.id.compTv);
        compRadio = (TextView) rootView.findViewById(R.id.compRadio);
        compCondizionatore = (TextView) rootView.findViewById(R.id.compCondizionatore);
        compBalcone = (TextView) rootView.findViewById(R.id.compBalcone);
        compMacchinaCaffe = (TextView) rootView.findViewById(R.id.compMacchinaCaffe);
        compIlluminazione = (TextView) rootView.findViewById(R.id.compIlluminazione);
        compTv.setText(conf.getComponentoToString(0));
        compRadio.setText(conf.getComponentoToString(1));
        compCondizionatore.setText(conf.getComponentoToString(2));
        compBalcone.setText(conf.getComponentoToString(3));
        compMacchinaCaffe.setText(conf.getComponentoToString(4));
        compIlluminazione.setText(conf.getComponentoToString(5));
        
        sensTemperatura = (TextView) rootView.findViewById(R.id.sensTemperatura);
        sensUmidita = (TextView) rootView.findViewById(R.id.sensUmidita);
        sensVento = (TextView) rootView.findViewById(R.id.sensVento);
        sensPresenza = (TextView) rootView.findViewById(R.id.sensPresenza);
        sensSonoro = (TextView) rootView.findViewById(R.id.sensSonoro);
        sensTemperatura.setText(conf.getSensoreToString(0));
        sensUmidita.setText(conf.getSensoreToString(1));
        sensVento.setText(conf.getSensoreToString(2));
        sensPresenza.setText(conf.getSensoreToString(3));
        sensSonoro.setText(conf.getSensoreToString(4));
        
        return rootView;
    }
    
}
