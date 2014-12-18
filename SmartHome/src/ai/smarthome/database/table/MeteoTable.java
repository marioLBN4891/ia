package ai.smarthome.database.table;

import android.provider.BaseColumns;

public interface MeteoTable extends BaseColumns
	{
		String TABLE_NAME = "meteo";
	 
		String LOCALITA = "localita";
		String METEO = "meteo";
		String TEMPERATURA = "temperatura";
		String UMIDITA = "umidita";
		String VENTO = "vento";
		String DATA = "data";
		String ORA = "ora";
		String MINUTI = "minuti";
				
		String[] COLUMNS = new String[] {
				_ID,
				LOCALITA,
				METEO,
				TEMPERATURA,
				UMIDITA,
				VENTO,
				DATA,
				ORA,
				MINUTI
		};
	
}
