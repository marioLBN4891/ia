package ai.smarthome.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import ai.smarthome.database.wrapper.Configurazione;
import android.annotation.SuppressLint;
import android.database.sqlite.SQLiteDatabase;

public class UtilConfigurazione {

	public static String setTextViewLocalita(String localita) {
    	return localita;
    }
	
	public static String setTextViewData(long data) {
    	return getDataToString(data);
    }
	
	public static String setTextViewOrario(int hour, int minute) {
    	return getOraToString(hour, minute);
    }
	
	public static String setTextViewVento(int progress) {
    	if (progress >= 0 && progress <= 1) return "Calmo";
    	if (progress >= 2 && progress <= 3) return "Bava di vento";
    	if (progress >= 4 && progress <= 6) return "Brezza leggera";
    	if (progress >= 7 && progress <= 10) return "Brezza";
    	if (progress >= 11 && progress <= 16) return "Brezza vivace";
    	if (progress >= 17 && progress <= 21) return "Brezza Tesa";
    	if (progress >= 22 && progress <= 27) return "Vento fresco";
    	if (progress >= 28 && progress <= 33) return "Vento forte";
    	if (progress >= 34 && progress <= 40) return "Burrasca";
    	if (progress >= 41 && progress <= 47) return "Burrasca forte";
    	if (progress >= 48 && progress <= 55) return "Tempesta";
    	if (progress >= 56 && progress <= 63) return "Fortunale";
    	if (progress >= 64) return "Uragano";
    	return "";
    }
	
	public static String setTextViewMeteo(int progress) {
		if (progress <= 35) return "Piovoso";
		if (progress > 35 && progress <= 65) return "Nuvoloso";
		if (progress > 65 && progress <= 80) return "Sereno";
		if (progress > 80) return "Soleggiato";
		return "";
	}
    
	public static String setTextViewTemperaturaInterna(int progress) {
    	if (progress != 40 ) 
    		return  progress + " °C";
    	else
    		return progress + " °C e oltre";
	}
    
	public static String setTextViewTemperaturaEsterna(int progress) {
    	if (progress > 0 && progress < 50) return (progress-10) + " °C";
    	if (progress == 0 || progress == 50) return (progress-10) + " °C e oltre";
    	return "";
	}
	
	public static String setTextViewUmidita(int progress) {
    	return progress+"%";
	}

	public static String setTextViewComponenti(int progress) {
		if (progress == 1) 
			return "Personalizzata";
		else
			return "Standard";
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
