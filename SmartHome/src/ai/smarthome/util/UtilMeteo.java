package ai.smarthome.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import ai.smarthome.database.wrapper.Meteo;
import android.annotation.SuppressLint;
import android.database.sqlite.SQLiteDatabase;
import android.widget.TextView;

public class UtilMeteo {

	public static void setTextViewLocalita(TextView textlocalita, String localita) {
    	textlocalita.setText(localita);
    }
	
	public static void setTextViewData(TextView textdata, long data) {
    	textdata.setText(getDataToString(data));
    }
	
	public static void setTextViewOrario(TextView textora, int hour, int minute) {
    	textora.setText(getOraToString(hour, minute));
    }
	
	public static void setTextViewVento(TextView textvento, int progress) {
    	if (progress == 0) 
    		textvento.setText("Vento: ASSENTE");
		if (progress > 0 && progress <= 33) 
			textvento.setText("Vento: DEBOLE");
		if (progress > 33 && progress <= 66) 
			textvento.setText("Vento: MODERATO");
		if (progress > 66) 
			textvento.setText("Vento: FORTE");
    }
	
	public static void setTextViewMeteo(TextView textmeteo, int progress) {
		if (progress <= 35) 
			textmeteo.setText("Meteo: PIOVOSO");
		if (progress > 35 && progress <= 65) 
			textmeteo.setText("Meteo: NUVOLOSO");
		if (progress > 65 && progress <= 80) 
			textmeteo.setText("Meteo: SERENO");
		if (progress > 80) 
			textmeteo.setText("Meteo: SOLEGGIATO");
	}
    
	public static void setTextViewTemperatura(TextView texttempest, int progress) {
    	int temperatura = 10 + (progress / 4) ;
    	texttempest.setText("Temperatura Esterna: " + temperatura + "° C");
    }
    
	public static void setTextViewUmidita(TextView textumidita, int progress) {
    	if (progress <= 25) 
    		textumidita.setText("Umidità: BASSA");
		if (progress > 25 && progress <= 50)
			textumidita.setText("Umidità: MEDIO-BASSA");
		if (progress > 50 && progress <= 75) 
			textumidita.setText("Umidità: MEDIO-ALTA");
		if (progress > 75) 
			textumidita.setText("Umidità: ALTA");
    }

	public static void updateMeteo(SQLiteDatabase db, String loc, int meteo, int temp, int umidita, int vento) {
    	Meteo.updateMeteo(db, loc, meteo, temp, umidita, vento);
    }

	public static void updateData(SQLiteDatabase db, long data) {
    	Meteo.updateData(db, data);
    }
	
	public static void updateOrario(SQLiteDatabase db, int ora, int minuti) {
    	Meteo.updateOrario(db, ora, minuti);
    }
	
	public static String getOraToString(int hour, int minute) {
		return (pad(hour)+":"+pad(minute));
	}
	
	@SuppressLint("SimpleDateFormat")
	public static String getDataToString(long dataMilliTime) {
		Date data = new Date();
    	data.setTime(dataMilliTime);
    	SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yyyy");
    	return sdf.format(data);
	}
	
	private static String pad(int c) {
		if (c >= 10)
		   return String.valueOf(c);
		else
		   return "0" + String.valueOf(c);
	}
}
