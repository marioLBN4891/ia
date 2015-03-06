package ai.smarthome.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import ai.smarthome.database.wrapper.Configurazione;
import android.annotation.SuppressLint;
import android.database.sqlite.SQLiteDatabase;
import android.widget.TextView;

public class UtilConfigurazione {

	public static void setTextViewLocalita(TextView textlocalita, String localita) {
    	textlocalita.setText(localita);
    }
	
	public static void setTextViewData(TextView textdata, long data) {
    	textdata.setText(getDataToString(data));
    }
	
	public static void setTextViewOrario(TextView textdata, int hour, int minute) {
    	textdata.append(" - " + getOraToString(hour, minute));
    }
	
	public static void setTextViewVento(TextView textvento, int progress) {
    	if (progress >= 0 && progress <= 1) textvento.setText("CALMO");
    	if (progress >= 2 && progress <= 3) textvento.setText("BAVA DI VENTO");
    	if (progress >= 4 && progress <= 6) textvento.setText("BREZZA LEGGERA");
    	if (progress >= 7 && progress <= 10) textvento.setText("BREZZA");
    	if (progress >= 11 && progress <= 16) textvento.setText("BREZZA VIVACE");
    	if (progress >= 17 && progress <= 21) textvento.setText("BREZZA TESA");
    	if (progress >= 22 && progress <= 27) textvento.setText("VENTO FRESCO");
    	if (progress >= 28 && progress <= 33) textvento.setText("VENTO FORTE");
    	if (progress >= 34 && progress <= 40) textvento.setText("BURRASCA MODERATA");
    	if (progress >= 41 && progress <= 47) textvento.setText("BURRASCA FORTE");
    	if (progress >= 48 && progress <= 55) textvento.setText("TEMPESTA");
    	if (progress >= 56 && progress <= 63) textvento.setText("FORTUNALE");
    	if (progress >= 64) textvento.setText("URAGANO");
    	
    }
	
	public static void setTextViewMeteo(TextView textmeteo, int progress) {
		if (progress <= 35) 
			textmeteo.setText("PIOVOSO");
		if (progress > 35 && progress <= 65) 
			textmeteo.setText("NUVOLOSO");
		if (progress > 65 && progress <= 80) 
			textmeteo.setText("SERENO");
		if (progress > 80) 
			textmeteo.setText("SOLEGGIATO");
	}
    
	public static void setTextViewTemperaturaInterna(TextView texttemp, int progress) {
    	if (progress != 40 )
    		texttemp.setText(progress + " °C");
    	else
    		texttemp.setText(progress + " °C e oltre");
	
    }
    
	public static void setTextViewTemperaturaEsterna(TextView texttemp, int progress) {
    	if (progress > 0 && progress < 50)
    		texttemp.setText((progress-10) + " °C");
    	
    	if (progress == 0 || progress == 50)
    		texttemp.setText((progress-10) + " °C e oltre");
    }
	
	public static void setTextViewUmidita(TextView textumidita, int progress) {
    	textumidita.setText(progress+"%");
	}

	public static void setTextViewComponenti(TextView textcomponenti, int progress) {
		if (progress == 1) 
			textcomponenti.setText("Personalizzata");
		else
			textcomponenti.setText("Standard");
	}
	
	public static void updateMeteo(SQLiteDatabase db, String loc, int meteo, int tempInt, int tempEst, int umiditaInt, int umiditaEst, int vento) {
    	Configurazione.updateMeteo(db, loc, meteo, tempInt, tempEst, umiditaInt, umiditaEst, vento);
    }

	public static void updateData(SQLiteDatabase db, long data) {
    	Configurazione.updateData(db, data);
    }
	
	public static void updateOrario(SQLiteDatabase db, int ora, int minuti) {
    	Configurazione.updateOrario(db, ora, minuti);
    }
	
	public static void updateComponenti(SQLiteDatabase db, int componenti) {
    	Configurazione.updateComponenti(db, componenti);
    }
	
	public static String getOraToString(int hour, int minute) {
		return (pad(hour)+":"+pad(minute));
	}
	
	@SuppressLint("SimpleDateFormat")
	public static String getDataToString(long dataMilliTime) {
		Date data = new Date();
    	data.setTime(dataMilliTime);
    	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    	return sdf.format(data);
	}
	
	private static String pad(int c) {
		if (c >= 10)
		   return String.valueOf(c);
		else
		   return "0" + String.valueOf(c);
	}
}
