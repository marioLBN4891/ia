package ai.smarthome.database.table;

import android.provider.BaseColumns;

public interface ConfigurazioneTable extends BaseColumns
	{
		String TABLE_NAME = "configurazione";
	 
		String LOCALITA = "localita";
		String METEO = "meteo";
		String TEMPERATURA = "temperatura";
		String UMIDITA = "umidita";
		String VENTO = "vento";
		String DATA = "data";
		String ORA = "ora";
		String MINUTI = "minuti";
		String COMPONENTI = "componenti";
				
		String[] COLUMNS = new String[] {
				_ID,
				LOCALITA,
				METEO,
				TEMPERATURA,
				UMIDITA,
				VENTO,
				DATA,
				ORA,
				MINUTI,
				COMPONENTI
		};
	
}
