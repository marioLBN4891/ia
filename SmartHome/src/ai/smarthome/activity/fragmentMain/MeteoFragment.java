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
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class MeteoFragment extends Fragment {
	
	private SeekBar seekMeteo, seekTempInt, seekTempEst, seekUmiditaInt, seekUmiditaEst, seekVento; 
    private TextView textMeteo, textTempInt, textTempEst, textUmiditaInt, textUmiditaEst, textVento;
    private SQLiteDatabase db;
    
    public MeteoFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	
    	Utente user = (Utente) getArguments().getSerializable(Costanti.UTENTE);
    	View rootView = inflater.inflate(R.layout.fragment_meteo, container, false);
        String intestazione = getResources().getStringArray(R.array.opzioni_array)[user.getPosizione()];
        getActivity().setTitle(intestazione);
        
        db = new DatabaseHelper(getActivity()).getWritableDatabase();
        
        Configurazione meteo = Configurazione.getConfigurazione(db);
        
        seekMeteo = (SeekBar) rootView.findViewById(R.id.seekMeteo);
        seekTempInt = (SeekBar) rootView.findViewById(R.id.seekTemperaturaInterna);
        seekTempEst = (SeekBar) rootView.findViewById(R.id.seekTemperaturaEsterna);
        seekUmiditaInt = (SeekBar) rootView.findViewById(R.id.seekUmiditaInterna);
        seekUmiditaEst = (SeekBar) rootView.findViewById(R.id.seekUmiditaEsterna);
        seekVento = (SeekBar) rootView.findViewById(R.id.seekVento);
        
        textMeteo = (TextView) rootView.findViewById(R.id.textMeteo);
        textTempInt = (TextView) rootView.findViewById(R.id.textTemperaturaInterna);
        textTempEst = (TextView) rootView.findViewById(R.id.textTemperaturaEsterna);
        textUmiditaInt = (TextView) rootView.findViewById(R.id.textUmiditaInterna);
        textUmiditaEst = (TextView) rootView.findViewById(R.id.textUmiditaEsterna);
        textVento = (TextView) rootView.findViewById(R.id.textVento);
        
        setTextMeteo(meteo.getMeteo());
        UtilConfigurazione.setTextViewTemperaturaInterna(textTempInt, meteo.getTemperaturaInt());
        textTempInt.setText("Temperatura interna: "+textTempInt.getText());
        UtilConfigurazione.setTextViewTemperaturaEsterna(textTempEst, meteo.getTemperaturaEst());
        textTempEst.setText("Temperatura esterna: "+textTempEst.getText());
        UtilConfigurazione.setTextViewUmidita(textUmiditaInt, meteo.getUmiditaInt());
        textUmiditaInt.setText("Umidità interna: "+textUmiditaInt.getText());
        UtilConfigurazione.setTextViewUmidita(textUmiditaEst, meteo.getUmiditaEst());
        textUmiditaEst.setText("Umidità esterna: "+textUmiditaEst.getText());
        UtilConfigurazione.setTextViewVento(textVento, meteo.getVento());
        textVento.setText("Vento: "+textVento.getText());
        
        seekMeteo.setProgress(meteo.getMeteo());
        seekTempInt.setProgress(meteo.getTemperaturaInt());
        seekTempEst.setProgress(meteo.getTemperaturaEst());
        seekUmiditaInt.setProgress(meteo.getUmiditaInt());
        seekUmiditaEst.setProgress(meteo.getUmiditaEst());
        seekVento.setProgress(meteo.getVento());
        
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
        
        
        seekTempInt.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
        	@Override
        	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        		UtilConfigurazione.setTextViewTemperaturaInterna(textTempInt, progress);
        		textTempInt.setText("Temperatura interna: "+textTempInt.getText());
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
        		UtilConfigurazione.setTextViewTemperaturaEsterna(textTempEst, progress);
        		textTempEst.setText("Temperatura esterna: "+textTempEst.getText());
        	}

        	@Override
        	public void onStartTrackingTouch(SeekBar seekBar) {
        	}

        	@Override
        	public void onStopTrackingTouch(SeekBar seekBar) {
        	}
        	
        });
        
        seekUmiditaInt.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
        	@Override
        	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        		UtilConfigurazione.setTextViewUmidita(textUmiditaInt, progress);
        		textUmiditaInt.setText("Umidità interna: "+textUmiditaInt.getText());
        	}

        	@Override
        	public void onStartTrackingTouch(SeekBar seekBar) {
        	}

        	@Override
        	public void onStopTrackingTouch(SeekBar seekBar) {
        	}
        	
        });
        
        seekUmiditaEst.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
        	@Override
        	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        		UtilConfigurazione.setTextViewUmidita(textUmiditaEst, progress);
        		textUmiditaEst.setText("Umidità esterna: "+textUmiditaEst.getText());
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
        		UtilConfigurazione.setTextViewVento(textVento, progress);
        		textVento.setText("Vento: "+textVento.getText());
        	}

        	@Override
        	public void onStartTrackingTouch(SeekBar seekBar) {
        	}

        	@Override
        	public void onStopTrackingTouch(SeekBar seekBar) {
        	}
        	
        });
        
        db.close();
        return rootView;
    }
    
    
    private void setTextMeteo(int progress) {
    	if (progress <= 35) 
    		setClima("Condizioni generali: PIOVOSO", 20, 10, 70, 80, 7);
		if (progress > 35 && progress <= 65) 
			setClima("Condizioni generali: NUVOLOSO", 50, 40, 50, 50, 4);
		if (progress > 65 && progress <= 80) 
			setClima("Condizioni generali: SERENO", 60, 70, 70, 60, 2);
		if (progress > 80) 
			setClima("Condizioni generali: SOLEGGIATO", 80, 90, 80, 90, 0);
		
    }
    
    private void setClima(String meteo, int tempInt, int tempEst, int umiditaInt, int umiditaEst, int vento) {
    	textMeteo.setText(meteo);
    	seekTempInt.setProgress(tempInt);
    	seekTempEst.setProgress(tempEst);
		seekUmiditaInt.setProgress(umiditaInt);
		seekUmiditaEst.setProgress(umiditaEst);
		seekVento.setProgress(vento);
		}
    
    
}
