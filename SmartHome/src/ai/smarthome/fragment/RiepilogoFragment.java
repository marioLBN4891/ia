package ai.smarthome.fragment;

import ai.smarthome.R;
import ai.smarthome.database.wrapper.Configurazione;
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

	private TextView dataText, orarioText, climaMeteo, climaTempEst, climaTempInt, climaUmidita, climaVento, climaVisibilita;
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
        climaTempInt = (TextView) rootView.findViewById(R.id.climaTempInt);
        climaUmidita = (TextView) rootView.findViewById(R.id.climaUmidita);
        climaVento = (TextView) rootView.findViewById(R.id.climaVento);
        climaVisibilita = (TextView) rootView.findViewById(R.id.climaVisibilita);
        setTextMeteo(conf.getClimaMeteo());
        setTextTempEst(conf.getClimaTemperaturaEsterna());
        setTextTempInt(conf.getClimaTemperaturaInterna());
        setTextUmidita(conf.getClimaUmidita());
        setTextVento(conf.getClimaVento());
        setTextVisibilita(conf.getClimaVisibilita());
        
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
    
    
    private void setTextMeteo(int progress) {
    	if (progress <= 20) 
    		climaMeteo.setText("Meteo: MOLTO PIOVOSO");
		if (progress > 20 && progress <= 35) 
			climaMeteo.setText("Meteo: PIOVOSO");
		if (progress > 35 && progress <= 50) 
			climaMeteo.setText("Meteo: MOLTO COPERTO");
		if (progress > 50 && progress <= 65) 
			climaMeteo.setText("Meteo: COPERTO");
		if (progress > 65 && progress <= 80) 
			climaMeteo.setText("Meteo: SERENO");
		if (progress > 80) 
			climaMeteo.setText("Meteo: SOLEGGIATO");
	}
    
    private void setTextTempEst(int progress) {
    	int temperatura = 10 + (progress / 4) ;
    	climaTempEst.setText("Temperatura Esterna: " + temperatura + "° C");
    }
    
    private void setTextTempInt(int progress) {
    	int temperatura = 10 + (progress / 4) ;
    	climaTempInt.setText("Temperatura Interna: " + temperatura + "° C");
    }
    
    private void setTextUmidita(int progress) {
    	if (progress <= 50) 
			climaUmidita.setText("Umidità: BASSA");
		if (progress > 50) 
			climaUmidita.setText("Umidità: ALTA");
    }
    
    private void setTextVento(int progress) {
    	if (progress == 0) 
    		climaVento.setText("Vento: ASSENTE");
		if (progress > 0 && progress <= 33) 
			climaVento.setText("Vento: DEBOLE");
		if (progress > 33 && progress <= 66) 
			climaVento.setText("Vento: MODERATO");
		if (progress > 66) 
			climaVento.setText("Vento: FORTE");
    }
    
    private void setTextVisibilita(int progress) {
    	if (progress <= 33) 
    		climaVisibilita.setText("Visibilità: BASSA");
		if (progress > 33 && progress <= 66) 
			climaVisibilita.setText("Visibilità: NORMALE");
		if (progress > 80) 
			climaVisibilita.setText("Visibilità: ALTA");
    }
}
