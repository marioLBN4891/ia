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
    private TextView textLoc, textMeteo, textTempInt, textTempEst, textUmiditaInt, textUmiditaEst, textVento;
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
        
        textLoc = (TextView) rootView.findViewById(R.id.textEditLocalita);
        textLoc.setText(meteo.getLocalita());
        
        textMeteo = (TextView) rootView.findViewById(R.id.textMeteo);
        textTempInt = (TextView) rootView.findViewById(R.id.textTemperaturaInterna);
        textTempEst = (TextView) rootView.findViewById(R.id.textTemperaturaEsterna);
        textUmiditaInt = (TextView) rootView.findViewById(R.id.textUmiditaInterna);
        textUmiditaEst = (TextView) rootView.findViewById(R.id.textUmiditaEsterna);
        textVento = (TextView) rootView.findViewById(R.id.textVento);
        
        textMeteo.append(UtilConfigurazione.setTextViewMeteo(meteo.getMeteo()));
        textTempInt.append(UtilConfigurazione.setTextViewTemperaturaInterna(meteo.getTemperaturaInt()));
        textTempEst.append(UtilConfigurazione.setTextViewTemperaturaEsterna(meteo.getTemperaturaEst()));
        textUmiditaInt.append(UtilConfigurazione.setTextViewUmidita(meteo.getUmiditaInt()));
        textUmiditaEst.append(UtilConfigurazione.setTextViewUmidita(meteo.getUmiditaEst()));
        textVento.append(UtilConfigurazione.setTextViewVento(meteo.getVento()));
        
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
        		textTempInt.setText(R.string.temperaturaInterna);
                textTempInt.append(UtilConfigurazione.setTextViewTemperaturaInterna(progress));
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
        		textTempEst.setText(R.string.temperaturaEsterna);
            	textTempEst.append(UtilConfigurazione.setTextViewTemperaturaEsterna(progress));
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
        		textUmiditaInt.setText(R.string.umidita);
            	textUmiditaInt.append(UtilConfigurazione.setTextViewUmidita(progress));
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
        		textUmiditaEst.setText(R.string.umiditaEsterna);
            	textUmiditaEst.append(UtilConfigurazione.setTextViewUmidita(progress));
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
        		textVento.setText(R.string.vento);
            	textVento.append(UtilConfigurazione.setTextViewVento(progress));
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
    		setClima(progress, 10, 25, 70, 80, 7);
		if (progress > 35 && progress <= 65) 
			setClima(progress, 15, 25, 45, 50, 4);
		if (progress > 65 && progress <= 80) 
			setClima(progress, 20, 30, 50, 60, 2);
		if (progress > 80) 
			setClima(progress, 25, 40, 80, 90, 0);
		
    }
    
    private void setClima(int meteo, int tempInt, int tempEst, int umiditaInt, int umiditaEst, int vento) {
    	textMeteo.setText(R.string.meteo);
    	textMeteo.append(UtilConfigurazione.setTextViewMeteo(meteo));
        
    	textTempInt.setText(R.string.temperaturaInterna);
        textTempInt.append(UtilConfigurazione.setTextViewTemperaturaInterna(tempInt));
    	
        textTempEst.setText(R.string.temperaturaEsterna);
    	textTempEst.append(UtilConfigurazione.setTextViewTemperaturaEsterna(tempEst));
    	
    	textUmiditaInt.setText(R.string.umidita);
    	textUmiditaInt.append(UtilConfigurazione.setTextViewUmidita(umiditaInt));
    	
    	textUmiditaEst.setText(R.string.umiditaEsterna);
    	textUmiditaEst.append(UtilConfigurazione.setTextViewUmidita(umiditaEst));
    	
    	textVento.setText(R.string.vento);
    	textVento.append(UtilConfigurazione.setTextViewVento(vento));
       
	}
    
    
}
