package ai.smarthome.fragment;

import ai.smarthome.R;
import ai.smarthome.database.wrapper.Configurazione;
import ai.smarthome.util.UtilMeteo;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class MeteoFragment extends Fragment {
	
	public static final String CONFIGURAZIONE = "configurazione";
	private SeekBar seekMeteo, seekTempEst, seekUmidita, seekVento; 
    private TextView textMeteo, textTempEst, textUmidita, textVento;
    
    public MeteoFragment() {
        // Empty constructor required for fragment subclasses
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	
    	Configurazione conf = (Configurazione) getArguments().getSerializable(CONFIGURAZIONE);
    	View rootView = inflater.inflate(R.layout.fragment_meteo, container, false);
        String intestazione = getResources().getStringArray(R.array.opzioni_array)[conf.getPosizione()];
        getActivity().setTitle(intestazione);
        
        seekMeteo = (SeekBar) rootView.findViewById(R.id.seekMeteo);
        seekTempEst = (SeekBar) rootView.findViewById(R.id.seekTemperaturaEsterna);
        seekUmidita = (SeekBar) rootView.findViewById(R.id.seekUmidita);
        seekVento = (SeekBar) rootView.findViewById(R.id.seekVento);
        
        textMeteo = (TextView) rootView.findViewById(R.id.textMeteo);
        textTempEst = (TextView) rootView.findViewById(R.id.textTemperaturaEsterna);
        textUmidita = (TextView) rootView.findViewById(R.id.textUmidita);
        textVento = (TextView) rootView.findViewById(R.id.textVento);
        
        setTextMeteo(conf.getClimaMeteo());
        UtilMeteo.setTextViewTempEst(textTempEst, conf.getClimaTemperaturaEsterna());
        UtilMeteo.setTextViewUmidita(textUmidita, conf.getClimaUmidita());
        UtilMeteo.setTextViewVento(textVento, conf.getClimaVento());
        
        seekMeteo.setProgress(conf.getClimaMeteo());
        seekTempEst.setProgress(conf.getClimaTemperaturaEsterna());
        seekUmidita.setProgress(conf.getClimaUmidita());
        seekVento.setProgress(conf.getClimaVento());
        
        seekMeteo.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
        	@Override
        	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        		setTextMeteo(progress);
        	}

        	@Override
        	public void onStartTrackingTouch(SeekBar seekBar) {
        	}

        	@Override
        	public void onStopTrackingTouch(SeekBar seekBar) {
        	}
        	
        });
        
        
        seekTempEst.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
        	@Override
        	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        		UtilMeteo.setTextViewTempEst(textTempEst, progress);
        	}

        	@Override
        	public void onStartTrackingTouch(SeekBar seekBar) {
        	}

        	@Override
        	public void onStopTrackingTouch(SeekBar seekBar) {
        	}
        	
        });
        
        seekUmidita.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
        	@Override
        	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        		UtilMeteo.setTextViewUmidita(textUmidita, progress);
        	}

        	@Override
        	public void onStartTrackingTouch(SeekBar seekBar) {
        	}

        	@Override
        	public void onStopTrackingTouch(SeekBar seekBar) {
        	}
        	
        });
        
        
        seekVento.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
        	@Override
        	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        		UtilMeteo.setTextViewVento(textVento, progress);
        	}

        	@Override
        	public void onStartTrackingTouch(SeekBar seekBar) {
        	}

        	@Override
        	public void onStopTrackingTouch(SeekBar seekBar) {
        	}
        	
        });
        
        
        return rootView;
    }
    
    
    private void setTextMeteo(int progress) {
    	if (progress <= 35) 
    		setClima("Meteo: PIOVOSO", 10, 80, 30);
		if (progress > 35 && progress <= 65) 
			setClima("Meteo: NUVOLOSO", 40, 50, 10);
		if (progress > 65 && progress <= 80) 
			setClima("Meteo: SERENO", 70, 60, 30);
		if (progress > 80) 
			setClima("Meteo: SOLEGGIATO", 90, 90, 10);
		
    }
    
    private void setClima(String meteo, int tempEst, int umidita, int vento) {
    	textMeteo.setText(meteo);
    	seekTempEst.setProgress(tempEst);
		seekUmidita.setProgress(umidita);
		seekVento.setProgress(vento);
		}
    
    
}
