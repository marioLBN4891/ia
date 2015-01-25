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
    	if (progress == 0) 
    		textvento.setText("ASSENTE");
		if (progress > 0 && progress <= 33) 
			textvento.setText("DEBOLE");
		if (progress > 33 && progress <= 66) 
			textvento.setText("MODERATO");
		if (progress > 66) 
			textvento.setText("FORTE");
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
    
	public static void setTextViewTemperatura(TextView texttempest, int progress) {
    	int temperatura = 10 + (progress / 4) ;
    	texttempest.setText(temperatura + " °C");
    }
    
	public static void setTextViewUmidita(TextView textumidita, int progress) {
    	if (progress <= 25) 
    		textumidita.setText("BASSA");
		if (progress > 25 && progress <= 50)
			textumidita.setText("MEDIO-BASSA");
		if (progress > 50 && progress <= 75) 
			textumidita.setText("MEDIO-ALTA");
		if (progress > 75) 
			textumidita.setText("ALTA");
    }

	public static void setTextViewComponenti(TextView textcomponenti, int progress) {
		if (progress == 1) 
			textcomponenti.setText("Configurazione personalizzata");
		else
			textcomponenti.setText("Configurazione standard");
	}
	
	public static void updateMeteo(SQLiteDatabase db, String loc, int meteo, int temp, int umidita, int vento) {
    	Configurazione.updateMeteo(db, loc, meteo, temp, umidita, vento);
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
