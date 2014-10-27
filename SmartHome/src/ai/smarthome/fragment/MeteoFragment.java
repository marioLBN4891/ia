package ai.smarthome.fragment;

import ai.smarthome.R;
import ai.smarthome.database.wrapper.Configurazione;
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
	private SeekBar seekMeteo, seekTempEst, seekTempInt, seekUmidita, seekVento, seekVisibilita; 
    private TextView textMeteo, textTempEst, textTempInt, textUmidita, textVento, textVisibilita;
    
    
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
        seekTempInt = (SeekBar) rootView.findViewById(R.id.seekTemperaturaInterna);
        seekUmidita = (SeekBar) rootView.findViewById(R.id.seekUmidita);
        seekVento = (SeekBar) rootView.findViewById(R.id.seekVento);
        seekVisibilita = (SeekBar) rootView.findViewById(R.id.seekVisibilita);
        
        textMeteo = (TextView) rootView.findViewById(R.id.textMeteo);
        textTempEst = (TextView) rootView.findViewById(R.id.textTemperaturaEsterna);
        textTempInt = (TextView) rootView.findViewById(R.id.textTemperaturaInterna);
        textUmidita = (TextView) rootView.findViewById(R.id.textUmidita);
        textVento = (TextView) rootView.findViewById(R.id.textVento);
        textVisibilita = (TextView) rootView.findViewById(R.id.textVisibilita);
        
        setTextMeteo(conf.getClimaMeteo());
        setTextTempEst(conf.getClimaTemperaturaEsterna());
        setTextTempInt(conf.getClimaTemperaturaInterna());
        setTextUmidita(conf.getClimaUmidita());
        setTextVento(conf.getClimaVento());
        setTextVisibilita(conf.getClimaVisibilita());
        
        
        
        
        seekMeteo.setProgress(conf.getClimaMeteo());
        seekTempEst.setProgress(conf.getClimaTemperaturaEsterna());
        seekTempInt.setProgress(conf.getClimaTemperaturaInterna());
        seekUmidita.setProgress(conf.getClimaUmidita());
        seekVento.setProgress(conf.getClimaVento());
        seekVisibilita.setProgress(conf.getClimaVisibilita());
        
        
        
        
        seekMeteo.setOnSeekBarChangeListener(
        		new OnSeekBarChangeListener() {
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
        
        
        seekTempEst.setOnSeekBarChangeListener(
        		new OnSeekBarChangeListener() {
        	@Override
        	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        		
        		setTextTempEst(progress);
        	}

        	@Override
        	public void onStartTrackingTouch(SeekBar seekBar) {
        	}

        	@Override
        	public void onStopTrackingTouch(SeekBar seekBar) {
        	}
        	
        });
        
        seekTempInt.setOnSeekBarChangeListener(
        		new OnSeekBarChangeListener() {
        	@Override
        	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        		
        		setTextTempInt(progress);
        	}

        	@Override
        	public void onStartTrackingTouch(SeekBar seekBar) {
        	}

        	@Override
        	public void onStopTrackingTouch(SeekBar seekBar) {
        	}
        	
        });
        
        
        seekUmidita.setOnSeekBarChangeListener(
        		new OnSeekBarChangeListener() {
        	@Override
        	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        		
        		setTextUmidita(progress);
        	}

        	@Override
        	public void onStartTrackingTouch(SeekBar seekBar) {
        	}

        	@Override
        	public void onStopTrackingTouch(SeekBar seekBar) {
        	}
        	
        });
        
        
        seekVento.setOnSeekBarChangeListener(
        		new OnSeekBarChangeListener() {
        	@Override
        	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        		setTextVento(progress);
        	}

        	@Override
        	public void onStartTrackingTouch(SeekBar seekBar) {
        	}

        	@Override
        	public void onStopTrackingTouch(SeekBar seekBar) {
        	}
        	
        });
        
        
        seekVisibilita.setOnSeekBarChangeListener(
        		new OnSeekBarChangeListener() {
        	@Override
        	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        		setTextVisibilita(progress);
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
    	if (progress <= 20) 
    		setClima("Meteo: MOLTO PIOVOSO", 10, 30, 80, 30, 20);
		if (progress > 20 && progress <= 35) 
			setClima("Meteo: PIOVOSO", 25, 40, 60, 40, 30);
		if (progress > 35 && progress <= 50) 
			setClima("Meteo: MOLTO COPERTO", 40, 40, 50, 10, 30);
		if (progress > 50 && progress <= 65) 
			setClima("Meteo: COPERTO", 50, 60, 40, 50, 60);
		if (progress > 65 && progress <= 80) 
			setClima("Meteo: SERENO", 70, 60, 60, 30, 80);
		if (progress > 80) 
			setClima("Meteo: SOLEGGIATO", 90, 70, 90, 10, 100);
		
    }
    
    private void setClima(String meteo, int tempEst, int tempInt, int umidita, int vento, int visibilita) {
    	textMeteo.setText(meteo);
    	seekTempEst.setProgress(tempEst);
		seekTempInt.setProgress(tempInt);
		seekUmidita.setProgress(umidita);
		seekVento.setProgress(vento);
		seekVisibilita.setProgress(visibilita);
    }
    
    private void setTextTempEst(int progress) {
    	int temperatura = 10 + (progress / 4) ;
		textTempEst.setText("Temperatura Esterna: " + temperatura + "° C");
    }
    
    private void setTextTempInt(int progress) {
    	int temperatura = 10 + (progress / 4) ;
		textTempInt.setText("Temperatura Interna: " + temperatura + "° C");
    }
    
    private void setTextUmidita(int progress) {
    	if (progress <= 50) 
			textUmidita.setText("Umidità: BASSA");
		if (progress > 50) 
			textUmidita.setText("Umidità: ALTA");
    }
    
    private void setTextVento(int progress) {
    	if (progress == 0) 
			textVento.setText("Vento: ASSENTE");
		if (progress > 0 && progress <= 33) 
			textVento.setText("Vento: DEBOLE");
		if (progress > 33 && progress <= 66) 
			textVento.setText("Vento: MODERATO");
		if (progress > 66) 
			textVento.setText("Vento: FORTE");
    }
    
    private void setTextVisibilita(int progress) {
    	if (progress <= 33) 
			textVisibilita.setText("Visibilità: BASSA");
		if (progress > 33 && progress <= 66) 
			textVisibilita.setText("Visibilità: NORMALE");
		if (progress > 66) 
			textVisibilita.setText("Visibilità: ALTA");
    }
}
