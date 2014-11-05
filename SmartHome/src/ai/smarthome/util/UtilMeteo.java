package ai.smarthome.util;

import ai.smarthome.database.wrapper.Configurazione;
import android.widget.TextView;

public class UtilMeteo {

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
    
	public static void setTextViewTempEst(TextView texttempest, int progress) {
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

	public static void setConfMeteo(Configurazione conf, int clima, int tempEst, int umidita, int vento) {
    	conf.setClimaMeteo(clima);
    	conf.setClimaTemperaturaEsterna(tempEst);
    	conf.setClimaUmidita(umidita);
    	conf.setClimaVento(vento);
    }

}
